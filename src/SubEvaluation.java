
public class SubEvaluation {
	private Packet[] pcks;
	private int OL; // carga oferecida
	String strOL;
	private int nPacks; // número total de pacotes
	//private int dX; // dimensão em X
	//private int dY; // dimensão em Y
	//private String topology; // topologia da rede
	private String path; // caminho do teste
	private String graphsPath;
	//private String rede;
	//private String traffic; // tipo de tráfego
	//private String net; // Nome da rede
	private int nDot = 30; // quantidade de pontos dos gráficos de distribuição
							// espacial
	//Map<String, Integer> nRetranspFLuxs = new HashMap<String, Integer>();

	// Construtores

	public SubEvaluation(String pathTst, String graphsPath, String rede,
			String offerLoad, int dimX, int dimY, String net) {
		this.strOL = offerLoad;
		//this.rede = rede;
		this.graphsPath = graphsPath;
		this.path = pathTst + "//" + offerLoad;
		//this.dX = dimX;
		//this.dY = dimY;
		//this.topology = "MESH";
		this.OL = Integer.parseInt(offerLoad.substring(1));// ,3) ); //Carga
															// oferecida
		//this.traffic = "UNIFORME"; // trafego Aceito
		//this.net = net; // Nome da rede
		ReadFiles read = new ReadFiles(path);
		//this.nRetranspFLuxs = read.getnRetranspFluxs();
		this.pcks = read.read();
		// armazena dados nos Packet[]
		this.nPacks = pcks.length;
	}

	/*
	 * public void printRetransmissions(String graphsPath1) { String[] fluxs =
	 * new String[nRetranspFLuxs.size()]; int[] values = new
	 * int[nRetranspFLuxs.size()]; int i=0; for(String key :
	 * nRetranspFLuxs.keySet()) { fluxs[i] = key; values[i] =
	 * nRetranspFLuxs.get(key);
	 * 
	 * i++; }
	 * 
	 * HandleFiles hand = new HandleFiles();
	 * 
	 * hand.WriteArFile( graphsPath1 + "//result_"+rede+"//", "Ar"+strOL, fluxs,
	 * values, nRetranspFLuxs.size() );
	 * 
	 * 
	 * }
	 */

	/*
	 * public int getNPck(  ) { int nPck = 0; for( int i = 0 ; i <
	 * nPacks ; i++ ) if ( pcks[i].isType( tipo ) ) nPck++; return nPck; }
	 */

	/*
	private int getNPck(int Xs, int Ys) {
		int nPck = 0;
		for (int i = 0; i < nPacks; i++)
			if (pcks[i].getXs() == Xs && pcks[i].getYs() == Ys)
				nPck++;
		return nPck;
	}
	*/

	// retorna o hopcount de um determinado pacote
	/*
	private int hopCount(Packet pck) {
		int hop = Math.abs(pck.getXs() - pck.getXt())
					+ Math.abs(pck.getYs() - pck.getYt());
		return (hop != 0) ? hop : -1;
	}
	*/

	/* geta o hopcount acumulado de um determinado tipo */
	/*
	private int getHopCountAcc() {
		int hCAcc = 0;
		for (int i = 0; i < nPacks; i++)
			hCAcc += hopCount(pcks[i]);
		return hCAcc;
	}

	private double getHopCountMean() {
		int hCAcc = getHopCountAcc();
		return hCAcc / nPacks;
	}
	*/

	// Arquivos

	/* Gera o arquivo para a confecção do Distribuição Espacial da Latência Pura */
	public void makeSpatDistriLat() {
		int nPcks[] = new int[nDot]; // contém as quantidades pacotes com a
										// latência correspondente
		double lats[] = new double[nDot]; // contém as latências distintas
		lats[0] = this.latencyMin();
		lats[nDot - 1] = this.latencyMax();
		double step = (lats[nDot - 1] - lats[0]) / nDot;
		for (int i = 1; i < nDot; i++) {
			lats[i] = lats[i - 1] + step;
			for (int j = 0; j < nPacks; j++)
				if (pcks[j].latency() <= lats[i]
						&& pcks[j].latency() >= lats[i - 1])
					nPcks[i - 1]++;
		}

		HandleFiles.WriteFile(graphsPath + "//", "ED_Lat" + strOL, nPcks, lats, nDot);
	}

	/*
	 * Gera o arquivo para a confecção do Distribuição Espacial da Latência
	 * Normalizada
	 */
	/*
	 * public void makeSpatDistriLatN(  ) { int nPcks[] = new int[ nDot
	 * ]; // contém as quantidades pacotes com a latência correspondente double
	 * latNs[] = new double[ nDot ]; // contém as latências distintas latNs[0] =
	 * this.latencyNMin( tipo ); latNs[ nDot-1 ] = this.latencyNMax( tipo );
	 * double step = ( latNs[ nDot-1 ] - latNs[0] )/nDot; for( int i = 1; i <
	 * nDot; i++ ) { latNs[i] = latNs[i-1] + step; for( int j = 0; j < nPacks;
	 * j++ ) if ( pcks[j].isType( tipo ) && this.latencyN( pcks[j] ) <= latNs[i]
	 * && this.latencyN( pcks[j] ) >= latNs[i-1] ) nPcks[i-1]++; }
	 * 
	 * HandleFiles hand = new HandleFiles(); hand.WriteFile( graphsPath + "//",
	 * "ED_Latn" + tipo+rede+strOL, nPcks, latNs, nDot ); }
	 */

	/*
	 * Gera o arquivo para a confecção do Distribuição Espacial do Tráfego
	 * Aceito
	 */
	public void makeSpatDistriAccepTraff() {
		int nPcks[] = new int[nDot]; // contém as quantidades pacotes com a
										// latência correspondente
		double accepTraffs[] = new double[nDot]; // contém as latências
													// distintas
		accepTraffs[0] = this.getAccepTraffMin();
		accepTraffs[nDot - 1] = this.getAccepTraffMax();
		double step = (accepTraffs[nDot - 1] - accepTraffs[0]) / nDot;
		for (int i = 1; i < nDot; i++) {
			accepTraffs[i] = accepTraffs[i - 1] + step;
			for (int j = 0; j < nPacks; j++)
				if (pcks[j].accepTraffic() <= accepTraffs[i]
						&& pcks[j].accepTraffic() >= accepTraffs[i - 1])
					nPcks[i - 1]++;
		}

		HandleFiles.WriteFile(graphsPath + "//", "ED_AT" + strOL, nPcks, accepTraffs, nDot);
	}

	/*
	public void makeHistogramAccepTraff() {
		double AccepTrafMean[] = new double[dX * dY];
		int Xs[] = new int[dX * dY];
		int Ys[] = new int[dX * dY];

		for (int i = 0; i < dX; i++)
			for (int j = 0; j < dY; j++) {
				Xs[dX * i + j] = i;
				Ys[dX * i + j] = j;
				AccepTrafMean[dX * i + j] = getAccepTraffMean(i, j);
			}

		HandleFiles hand = new HandleFiles();
		hand.WriteFile(graphsPath + "//", "HistAccTraf" + strOL,
				Xs, Ys, AccepTrafMean);
	}
	*/

	/* Escreve arquivo de relatório */
	public void makeRelat() {
		String[] Relat = new String[13];
		int i = 0;
		// nome da rede - tipo de tráfego - carga oferecida
		Relat[i++] = " - " + OL + "%";
		// porcentagem de alta e baixa prioridade
		Relat[i++] = "Quantidade total de pacotes:  " + nPacks;
		// valor de hopcount médio
		//Relat[i++] = "HopCount medio: " + getHopCountMean();
			// latência pura: média+-desvio / [máximo,mínimo]
			Relat[i++] = "Latencia Total:  [" + latencyMin() + " : "
					+ latencyMean() + "/" + latencyStdDev() + " : "
					+ latencyMax() + "]";
			// latência normalizada: média+-desvio / [máximo,mínimo]
			// Relat[i++] = "Latencia Normalizada Total:  [" + latencyNMin() +
			// " : " + latencyNMean() + "/" + latencyNStdDev() + " : " +
			// latencyNMax() + "]";
			// tráfego aceito: média+-desvio / [máximo,mínimo]
			Relat[i++] = "Trafego Aceito Total:  [" + getAccepTraffMin()
					+ " : " + getAccepTraffMean() + "/"
					+ getAccepTraffStdDev() + " : " + getAccepTraffMax()
					+ "]";

			HandleFiles.WriteFile(graphsPath + "//", "Relatorio" + strOL, Relat);
	}

	/*
	 * 
	 * public void plotSpatDistriLat(  ) { PlotGraphics.plotED_Lat(
	 * graphsPath + "//", "ED_Lat" + tipo+path.substring(0,
	 * path.length()-1)+rede, latencyMean( tipo ), latencyStdDev( tipo ), net ); }
	 * 
	 * public void plotSpatDistriLatN(  ) { PlotGraphics.plotED_Lat(
	 * graphsPath + "//", "ED_LatN" + tipo+path.substring(0,
	 * path.length()-1)+rede, latencyNMean( tipo ), latencyNStdDev( tipo ), net );
	 * }
	 * 
	 * public void plotSpatDistriAccepTraff(  ) {
	 * PlotGraphics.plotED_Lat( graphsPath + "//", "ED_AT" +
	 * tipo+path.substring(0, path.length()-1)+rede, getAccepTraffMean( tipo ),
	 * getAccepTraffStdDev( tipo ), net ); }
	 * 
	 * public void plotSpatDistriAccepTraff3D(  ) {
	 * PlotGraphics.plotED_Traf3D( graphsPath + "//", "HistAccTraf" +
	 * tipo+path.substring(0, path.length()-1)+rede, net ); }
	 */

	/* geta a carga oferecida do subteste */
	public int OfferedLoad() {
		return OL;
	}

	// Latências Puras

	private double latencyAcc() // acumulada
	{
		double latAcc = 0;
		for (int i = 0; i < nPacks; i++)
			latAcc += pcks[i].latency();
		return latAcc;
	}

	public double latencyMean() // média
	{
		return (nPacks != 0) ? latencyAcc() / (double) nPacks : -1.0;
	}

	private double latencyStdDev() // desvio padrão
	{
		if (nPacks != 0) {
			double latMean = latencyMean();
			double sum = 0;
			for (int i = 0; i < nPacks; i++)
					sum += Math.pow(pcks[i].latency() - latMean, 2);
			return Math.sqrt(sum / nPacks);
		}
		return -1.0;
	}

	private double latencyMax() // máxima
	{
		double latMax = 0;
		for (int i = 0; i < nPacks; i++)
			if (pcks[i].latency() > latMax)
				latMax = pcks[i].latency();
		return latMax;
	}

	private double latencyMin() // mínima
	{
		double latMin = Double.POSITIVE_INFINITY;
		for (int i = 0; i < nPacks; i++)
			if (pcks[i].latency() < latMin)
				latMin = pcks[i].latency();
		return latMin;
	}

	// Latências Normalizadas

	/* geta latência normalizada de um pacote */
	/*
	 * private double latencyN( Packet pck ) { return pck.latency()/hopCount(pck);
	 * }
	 * 
	 * public double latencyNAcc(  ) // acumulada { int latAcc = 0; for(
	 * int i = 0; i < nPacks; i++ ) if ( pcks[i].isType(  ) ) latAcc +=
	 * latencyN( pcks[i] ); return latAcc; }
	 * 
	 * public double latencyNMean(  ) // média { int nPck = getNPck(
	 *  ); return (nPck != 0) ? latencyNAcc(  )/nPck : -1.0; }
	 * 
	 * public double latencyNStdDev(  ) // desvio padrão { int nPck =
	 * getNPck(  ); if ( nPck != 0 ) { double latNMean = latencyNMean( 
	 * ); double sum = 0; for( int i = 0; i < nPacks; i++) if ( pcks[i].isType(
	 * tipo ) ) sum += Math.pow( latencyN( pcks[i] ) - latNMean , 2 ); return
	 * Math.sqrt( sum / nPck ); } return -1.0; }
	 * 
	 * public double latencyNMax(  ) // máxima { double latNMax = 0; for
	 * ( int i = 0; i < nPacks; i++ ) if ( pcks[i].isType( tipo ) && ( latencyN(
	 * pcks[i] ) > latNMax ) ) latNMax = latencyN( pcks[i] ); return latNMax; }
	 * 
	 * public double latencyNMin(  ) // mínima { double latNMin =
	 * latencyNAcc( tipo ); for( int i = 0; i < nPacks; i++ ) if (
	 * pcks[i].isType( tipo ) && ( latencyN( pcks[i] ) < latNMin ) ) latNMin =
	 * latencyN( pcks[i] ); return latNMin; }
	 */
	// Tráfegos Aceitos

	private double getAccepTraffAcc() // acumulado
	{
		double accepTraffAcc = 0;
		for (int i = 0; i < nPacks; i++)
			accepTraffAcc += pcks[i].accepTraffic();
		return accepTraffAcc;
	}

	/*
	private double getAccepTraffAcc(int Xs, int Ys) // acumulado para
																// certo nodo de
																// origem
	{
		double accepTraffAcc = 0;
		for (int i = 0; i < nPacks; i++)
			if (pcks[i].getXs() == Xs
					&& pcks[i].getYs() == Ys)
				accepTraffAcc += pcks[i].getAccepTraff();
		return accepTraffAcc;
	}

	private double getAccepTraffMean(int Xs, int Ys ) // médio para
																// certo nodo de
																// origem
	{
		int nPck = getNPck(Xs, Ys );
		return (nPck != 0) ? getAccepTraffAcc(Xs, Ys ) / nPck : -1.0;
	}
	*/

	public double getAccepTraffMean() // média
	{
		return (nPacks != 0) ? getAccepTraffAcc() / (double) nPacks : -1.0;
	}

	private double getAccepTraffStdDev() // desvio padrão
	{
		if (nPacks != 0) {
			double accepTraffMean = getAccepTraffMean();
			double sum = 0;
			for (int i = 0; i < nPacks; i++)
					sum += Math.pow(pcks[i].accepTraffic() - accepTraffMean, 2);
			return Math.sqrt(sum / (double) nPacks);
		}
		return -1.0;
	}

	private double getAccepTraffMax() // máximo
	{
		double accepTraffMax = 0;
		for (int i = 0; i < nPacks; i++)
			if ((pcks[i].accepTraffic() > accepTraffMax))
				accepTraffMax = pcks[i].accepTraffic();
		return accepTraffMax;
	}

	private double getAccepTraffMin() // mínimo
	{
		double accepTraffMin = Double.POSITIVE_INFINITY;
		for (int i = 0; i < nPacks; i++)
			if ((pcks[i].accepTraffic() < accepTraffMin))
				accepTraffMin = pcks[i].accepTraffic();
		return accepTraffMin;
	}

}