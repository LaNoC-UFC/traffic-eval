import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
	23/09/11 - Rafael Mota
	26/09/11 - George Harinson
	05/10/11 - João Marcelo
	24/06/14 - Rafael Mota
 */

public class SubEvaluation
{
	private Packet[] pcks;
	private int OL; // carga oferecida
	String strOL;
	private int nPacks; // número total de pacotes
	private int dX; // dimensão em X
	private int dY; // dimensão em Y
	private String topology; // topologia da rede
	private String path; // caminho do teste
	private String graphsPath; 
	private String rede;
	private String traffic; // tipo de tráfego
	private String net; // Nome da rede
	private int nDot = 30; // quantidade de pontos dos gráficos de distribuição espacial
	Map<String,Integer> nRetranspFLuxs = new HashMap<String,Integer>();
	
	// Construtores

	public SubEvaluation(String pathTst,String graphsPath,String rede, String offerLoad, int dimX, int dimY, String net )
	{
		this.strOL=offerLoad;
		this.rede=rede;
		this.graphsPath = graphsPath;
		this.path = pathTst + "//" + offerLoad;
		this.dX = dimX;
		this.dY = dimY;
		this.topology = "MESH";
        this.OL = Integer.parseInt( offerLoad.substring(1) );//,3) ); //Carga oferecida
		this.traffic = "UNIFORME"; //trafego Aceito
		this.net = net; //Nome da rede
		ReadFiles read = new ReadFiles();
		this.nRetranspFLuxs=read.getnRetranspFluxs();
		this.pcks = read.read(path);
		//armazena dados nos Packet[]
		this.nPacks = pcks.length;
	}
	
	public void printRetransmissions(String graphsPath1)
	{
		String[] fluxs = new String[nRetranspFLuxs.size()];
		int[] values = new int[nRetranspFLuxs.size()];
		int i=0;
		for(String key : nRetranspFLuxs.keySet())
		{	
			fluxs[i] = key;
			values[i] = nRetranspFLuxs.get(key);
						
			i++;			
		}
		
		HandleFiles hand = new HandleFiles();
			
		hand.WriteArFile( graphsPath1 + "//result_"+rede+"//", "Ar"+strOL, fluxs, values, nRetranspFLuxs.size() );
		
			
	}
	

	public int getNPck( char tipo ) 
	{
		int nPck = 0;
		for( int i = 0 ; i < nPacks ; i++ )
			if ( pcks[i].isType( tipo ) ) nPck++;
		return nPck;
	}
        
    public int getNPck( int Xs,int Ys, char tipo )
	{
		int nPck = 0;
		for( int i = 0 ; i < nPacks ; i++ )
			if ( pcks[i].isType( tipo )&& pcks[i].getXs() == Xs && pcks[i].getYs() == Ys ) nPck++;
		return nPck;
	}

	//retorna o hopcount de um determinado pacote
	private int hopCount( Packet pck )
	{
		int hop = 0;
        if ( topology.compareTo( "MESH" ) == 0 )
		{
			hop = Math.abs( pck.getXs() - pck.getXt() ) + Math.abs( pck.getYs() - pck.getYt() );
		}
		else if ( topology.compareTo( "TORUS") == 0 )
		{
			int hX = Math.min( Math.abs( pck.getXs() - pck.getXt() ), pck.getXs() + dX + 1 - pck.getXt() );
			int hY = Math.min( Math.abs( pck.getYs() - pck.getYt() ), pck.getYs() + dY + 1 - pck.getYt() );
			hop = hX + hY;
		}
		return ( hop != 0) ? hop : -1;
	}
	
	/* geta o hopcount acumulado de um determinado tipo */
	public int getHopCountAcc( char tipo )
	{
		int hCAcc = 0;
		for( int i = 0; i < nPacks; i++ )
			if ( pcks[i].isType( tipo ) ) hCAcc += hopCount( pcks[i] );
		return hCAcc;
	}
	
	/* geta o hopcount médio de um determinado tipo */
	public double getHopCountMean( char tipo )
	{
		int hCAcc = getHopCountAcc( tipo );
		return hCAcc/getNPck( tipo );
	}
	
	// Arquivos

	/* Gera o arquivo para a confecção do Distribuição Espacial da Latência Pura */
	public void makeSpatDistriLat( char tipo )
	{
		int nPcks[] = new int[ nDot ]; // contém as quantidades pacotes com a latência correspondente
		double lats[] = new double[ nDot ]; // contém as latências distintas
		lats[0] = this.getLatMin( tipo );
		lats[ nDot-1 ] = this.getLatMax( tipo );
		double step = ( lats[ nDot-1 ] - lats[0] )/nDot;
		for( int i = 1; i < nDot; i++ )
		{
			lats[i] = lats[i-1] + step;
			for( int j = 0; j < nPacks; j++ )
				if ( pcks[j].isType( tipo ) && pcks[j].getLat() <= lats[i] && pcks[j].getLat() >= lats[i-1] )
					nPcks[i-1]++;
		}
                               
		HandleFiles hand = new HandleFiles();
        hand.WriteFile( graphsPath + "//", "ED_Lat" + tipo+rede+strOL, nPcks, lats, nDot );
	}

	/* Gera o arquivo para a confecção do Distribuição Espacial da Latência Normalizada */
	public void makeSpatDistriLatN( char tipo )
	{
		int nPcks[] = new int[ nDot ]; // contém as quantidades pacotes com a latência correspondente
		double latNs[] = new double[ nDot ]; // contém as latências distintas
		latNs[0] = this.getLatNMin( tipo );
		latNs[ nDot-1 ] = this.getLatNMax( tipo );
		double step = ( latNs[ nDot-1 ] - latNs[0] )/nDot;
		for( int i = 1; i < nDot; i++ )
		{
			latNs[i] = latNs[i-1] + step;
			for( int j = 0; j < nPacks; j++ )
				if ( pcks[j].isType( tipo ) && this.getLatN( pcks[j] ) <= latNs[i] && this.getLatN( pcks[j] ) >= latNs[i-1] )
					nPcks[i-1]++;
		}
                               
		HandleFiles hand = new HandleFiles();
        hand.WriteFile( graphsPath + "//", "ED_Latn" + tipo+rede+strOL, nPcks, latNs, nDot );
	}

	/* Gera o arquivo para a confecção do Distribuição Espacial do Tráfego Aceito */
	public void makeSpatDistriAccepTraff( char tipo )
	{
		int nPcks[] = new int[ nDot ]; // contém as quantidades pacotes com a latência correspondente
		double accepTraffs[] = new double[ nDot ]; // contém as latências distintas
		accepTraffs[0] = this.getAccepTraffMin( tipo );
		accepTraffs[ nDot-1 ] = this.getAccepTraffMax( tipo );
		double step = ( accepTraffs[ nDot-1 ] - accepTraffs[0] )/nDot;
		for( int i = 1; i < nDot; i++ )
		{
			accepTraffs[i] = accepTraffs[i-1] + step;
			for( int j = 0; j < nPacks; j++ )
				if ( pcks[j].isType( tipo ) && pcks[j].getAccepTraff() <= accepTraffs[i] && pcks[j].getAccepTraff() >= accepTraffs[i-1] )
					nPcks[i-1]++;
		}
                               
		HandleFiles hand = new HandleFiles();
        hand.WriteFile( graphsPath + "//", "ED_AT" + tipo+rede+strOL, nPcks, accepTraffs, nDot );
	}

	public void makeHistogramAccepTraff (char tipo)
	{
		double AccepTrafMean[] = new double[dX*dY];
		int Xs[] = new int[dX*dY];
        int Ys[] = new int[dX*dY];   
            
        for(int i=0; i < dX; i++)
			for(int j=0; j < dY; j++)
			{
				Xs[dX*i+j] = i;
				Ys[dX*i+j] = j;
				AccepTrafMean[dX*i+j] = getAccepTraffMean(i, j, tipo);
			}
            
		HandleFiles hand = new HandleFiles();
		hand.WriteFile( graphsPath + "//", "HistAccTraf" + tipo+rede+strOL, Xs, Ys, AccepTrafMean );
    }

	/* Escreve arquivo de relatório */
	public void makeRelat()
	{       
		HandleFiles HD = new HandleFiles();
		String[] Relat =  new String[13];
		int i = 0;
		// nome da rede - tipo de tráfego - carga oferecida
		Relat[i++] = net + " - " + traffic + " - " + OL + "%";
		// porcentagem de alta e baixa prioridade 
		Relat[i++] = "Quantidade total de pacotes:  " + getNPck( 'T' );
		Relat[i++] = "Quantidade de pocotes com prioridade:  " + getNPck( 'H' );
		// valor de hopcount médio
		Relat[i++] = "HopCount medio: " + getHopCountMean( 'T' );
		if( this.getNPck( 'H' ) != 0 )
		{
			// latência pura: média+-desvio / [máximo,mínimo]
			Relat[i++] = "Latencia Total:  [" + getLatMin('T') + " : " + getLatMean('T') + "/" + getLatStdDev('T') + " : " + getLatMax('T') + "]";
			Relat[i++] = "Latencia H:  [" + getLatMin('H') + " : " + getLatMean('H') + "/" + getLatStdDev('H') + " : " + getLatMax('H') + "]";
			Relat[i++] = "Latencia L:  [" + getLatMin('L') + " : " + getLatMean('L') + "/" + getLatStdDev('L') + " : " + getLatMax('L') + "]";
			// latência normalizada: média+-desvio / [máximo,mínimo]
			Relat[i++] = "Latencia Normalizada Total:  [" + getLatNMin('T') + " : " + getLatNMean('T') + "/" + getLatNStdDev('T') + " : " + getLatNMax('T') + "]";
			Relat[i++] = "Latencia Normalizada H:  [" + getLatNMin('H') + " : " + getLatNMean('H') + "/" + getLatNStdDev('H') + " : " + getLatNMax('H') + "]";
			Relat[i++] = "Latencia Normalizada L:  [" + getLatNMin('L') + " : " + getLatNMean('L') + "/" + getLatNStdDev('L') + " : " + getLatNMax('L') + "]";
			// tráfego aceito: média+-desvio / [máximo,mínimo]
			Relat[i++] = "Trafego Aceito Total:  [" + getAccepTraffMin('T') + " : " + getAccepTraffMean('T') + "/" + getAccepTraffStdDev('T') + " : " + getAccepTraffMax('T') + "]";
			Relat[i++] = "Trafego Aceito H:  [" + getAccepTraffMin('H') + " : " + getAccepTraffMean('H') + "/" + getAccepTraffStdDev('H') + " : " + getAccepTraffMax('H') + "]";
			Relat[i++] = "Trafego Aceito L:  [" + getAccepTraffMin('L') + " : " + getAccepTraffMean('L') + "/" + getAccepTraffStdDev('L') + " : " + getAccepTraffMax('L') + "]";
		}
		else if( this.getNPck( 'T' ) != 0 )
		{
			// latência pura: média+-desvio / [máximo,mínimo]
			Relat[i++] = "Latencia Total:  [" + getLatMin('T') + " : " + getLatMean('T') + "/" + getLatStdDev('T') + " : " + getLatMax('T') + "]";
			// latência normalizada: média+-desvio / [máximo,mínimo]
			Relat[i++] = "Latencia Normalizada Total:  [" + getLatNMin('T') + " : " + getLatNMean('T') + "/" + getLatNStdDev('T') + " : " + getLatNMax('T') + "]";
			// tráfego aceito: média+-desvio / [máximo,mínimo]
			Relat[i++] = "Trafego Aceito Total:  [" + getAccepTraffMin('T') + " : " + getAccepTraffMean('T') + "/" + getAccepTraffStdDev('T') + " : " + getAccepTraffMax('T') + "]";
			
		}
		HD.WriteFile( graphsPath + "//", "Relatorio"+rede+strOL, Relat );
	}

	public void plotSpatDistriLat( char tipo )
	{
		PlotGraphics.plotED_Lat( graphsPath + "//", "ED_Lat" + tipo+path.substring(0, path.length()-1)+rede, getLatMean( tipo ), getLatStdDev( tipo ), net );
	}
	
	public void plotSpatDistriLatN( char tipo )
	{
		PlotGraphics.plotED_Lat( graphsPath + "//", "ED_LatN" + tipo+path.substring(0, path.length()-1)+rede, getLatNMean( tipo ), getLatNStdDev( tipo ), net );
	}
	
	public void plotSpatDistriAccepTraff( char tipo )
	{
		PlotGraphics.plotED_Lat( graphsPath + "//", "ED_AT" + tipo+path.substring(0, path.length()-1)+rede, getAccepTraffMean( tipo ), getAccepTraffStdDev( tipo ), net );
	}

        public void plotSpatDistriAccepTraff3D( char tipo )
        {
            PlotGraphics.plotED_Traf3D( graphsPath + "//", "HistAccTraf" + tipo+path.substring(0, path.length()-1)+rede, net );
        }
	
	/* geta a carga oferecida do subteste */
	public int getOL()
	{
		return OL;
	}

	// Latências Puras

	public double getLatAcc( char tipo ) // acumulada
	{
		double latAcc = 0;
		for( int i = 0; i < nPacks; i++ )
			if ( pcks[i].isType( tipo ) ) latAcc += pcks[i].getLat();
		return latAcc;
	}

	public double getLatMean( char tipo ) // média
	{
		double nPck = (double)getNPck( tipo );
		return  (nPck != 0) ? getLatAcc( tipo )/nPck : -1.0;
	}

	public double getLatStdDev( char tipo ) // desvio padrão
	{
		int nPck = getNPck( tipo );
		if ( nPck != 0 )
		{
			double latMean = getLatMean( tipo );
			double sum = 0;
			for( int i = 0; i < nPacks; i++)
				if ( pcks[i].isType( tipo ) ) sum += Math.pow( pcks[i].getLat() - latMean , 2 );
			return Math.sqrt( sum / nPck );
		}
		return -1.0;
	}

	public double getLatMax( char tipo ) // máxima
	{
		double latMax = 0;
		for ( int i = 0; i < nPacks; i++ )
			if ( pcks[i].isType( tipo ) && ( pcks[i].getLat() > latMax ) ) latMax = pcks[i].getLat();
		return latMax;
	}

	public double getLatMin( char tipo ) // mínima
	{
		double latMin = getLatAcc( tipo );
		for( int i = 0; i < nPacks; i++ )
			if ( pcks[i].isType( tipo ) && ( pcks[i].getLat() < latMin ) ) latMin = pcks[i].getLat();
		return latMin;
	}

	// Latências Normalizadas

	/* geta latência normalizada de um pacote */
	private double getLatN( Packet pck )
	{
		return pck.getLat()/hopCount(pck);
	}

	public double getLatNAcc( char tipo ) // acumulada
	{
		int latAcc = 0;
		for( int i = 0; i < nPacks; i++ )
			if ( pcks[i].isType( tipo ) ) latAcc += getLatN( pcks[i] );
		return latAcc;
	}

	public double getLatNMean( char tipo ) // média
	{
		int nPck = getNPck( tipo );
		return  (nPck != 0) ? getLatNAcc( tipo )/nPck : -1.0;
	}

	public double getLatNStdDev( char tipo ) // desvio padrão
	{
		int nPck = getNPck( tipo );
		if ( nPck != 0 )
		{
			double latNMean = getLatNMean( tipo );
			double sum = 0;
			for( int i = 0; i < nPacks; i++)
				if ( pcks[i].isType( tipo ) ) sum += Math.pow( getLatN( pcks[i] ) - latNMean , 2 );
			return Math.sqrt( sum / nPck );
		}
		return -1.0;
	}

	public double getLatNMax( char tipo ) // máxima
	{
		double latNMax = 0;
		for ( int i = 0; i < nPacks; i++ )
			if ( pcks[i].isType( tipo ) && ( getLatN( pcks[i] ) > latNMax ) ) 
				latNMax = getLatN( pcks[i] );
		return latNMax;
	}

	public double getLatNMin( char tipo ) // mínima
	{
		double latNMin = getLatNAcc( tipo );
		for( int i = 0; i < nPacks; i++ )
			if ( pcks[i].isType( tipo ) && ( getLatN( pcks[i] ) < latNMin ) ) 
                            latNMin = getLatN( pcks[i] );
		return latNMin;
	}

	// Tráfegos Aceitos

	public double getAccepTraffAcc( char tipo ) // acumulado
	{
		double accepTraffAcc = 0;
		for( int i = 0; i < nPacks; i++ )
			if ( pcks[i].isType( tipo ) ) accepTraffAcc += pcks[i].getAccepTraff();
		return accepTraffAcc;
	}
        
        public double getAccepTraffAcc(int Xs, int Ys, char tipo ) // acumulado para certo nodo de origem
	{
		double accepTraffAcc = 0;
		for( int i = 0; i < nPacks; i++ )
			if ( pcks[i].isType( tipo )&& pcks[i].getXs() == Xs && pcks[i].getYs() == Ys  ) accepTraffAcc += pcks[i].getAccepTraff();
		return accepTraffAcc;
	}
        
        public double getAccepTraffMean(int Xs,int Ys, char tipo ) // médio para certo nodo de origem
	{
		int nPck = getNPck(Xs, Ys, tipo );
		return  (nPck != 0) ? getAccepTraffAcc(Xs, Ys, tipo )/nPck : -1.0;
	}

	public double getAccepTraffMean( char tipo ) // média
	{
		int nPck = getNPck( tipo );
		return  (nPck != 0) ? getAccepTraffAcc( tipo )/nPck : -1.0;
	}

	public double getAccepTraffStdDev( char tipo ) // desvio padrão
	{
		int nPck = getNPck( tipo );
		if ( nPck != 0 )
		{
			double accepTraffMean = getAccepTraffMean( tipo );
			double sum = 0;
			for( int i = 0; i < nPacks; i++)
				if ( pcks[i].isType( tipo ) ) sum += Math.pow( pcks[i].getAccepTraff() - accepTraffMean , 2 );
			return Math.sqrt( sum / nPck );
		}
		return -1.0;
	}

	public double getAccepTraffMax( char tipo ) // máximo
	{
		double accepTraffMax = 0;
		for ( int i = 0; i < nPacks; i++ )
			if ( pcks[i].isType( tipo ) && ( pcks[i].getAccepTraff() > accepTraffMax ) ) accepTraffMax = pcks[i].getAccepTraff();
		return accepTraffMax;
	}

	public double getAccepTraffMin( char tipo ) // mínimo
	{
		double accepTraffMin = getAccepTraffAcc( tipo );
		for( int i = 0; i < nPacks; i++ )
			if ( pcks[i].isType( tipo ) && ( pcks[i].getAccepTraff() < accepTraffMin ) ) accepTraffMin = pcks[i].getAccepTraff();
		return accepTraffMin;
	}

}