
import java.io.*;

public class TrafficMeasurer {
	private Evaluation[] OL; // cada uma das cargas oferecidas
	//private int OL.length; // quantidade de cargas oferecidas
	private String rede; // nome da rede
	// private String trafficDistri; // distribuição espacial do tráfego adotada
	//private String pathTst; // path do teste
	//private String[] pathOL; // path das cargas oferecidas
	//private int dimX; // tamanho da rede no eixo X
	//private int dimY; // tamanho da rede no eixo Y
	//private String topology; // topologia da rede
	private String graphsPath;

	public static void main(String[] args) {
		String path = "./evaluate";
		//int dimX = 11, dimY = 11;
		
		if (args.length == 1) {
			path = "./" + args[0];
		}
		
		System.out.println("Evaluating traffic from " + path);
		TrafficMeasurer eval = new TrafficMeasurer("", path, path);
		eval.makeCNFs();
		//eval.genHistograms();
		eval.makeRelats();
	}

	public TrafficMeasurer(String nome, String pathTst, String graphsPath) {
		this.graphsPath = graphsPath;
		// dimensões da rede
		//this.dimX = dX;
		//this.dimY = dY;
		// topologia da rede
		//this.topology = "MESH";
		// nome da rede
		this.rede = nome;
		// path principal
		//pathTst = pth;
		// lista pastas (subtestes)
		File folder = new File(pathTst);
		String[] pathOL = folder.list();
		// seta quantidade de subtestes
		//OL.length = OL.length;
		// aloca OL
		OL = new Evaluation[pathOL.length];
		// aloca e inicializa cada OL[i]
		for (int i = 0; i < OL.length; i++)
			OL[i] = new Evaluation(pathTst, graphsPath, rede, pathOL[i], rede);
	}

	public void genHistograms() {
		for (Evaluation e : OL) {
			e.makeHistLat();
			e.makeHistAccepTraff();
		}
	}

	/* gera os arquivos 'Distribuição espacial' de um determinado tipo */
	/*
	public void makeDistris() {
		for (int i = 0; i < OL.length; i++) {
			OL[i].makeSpatDistriLat();
			// OL[i].makeSpatDistriLatN( tipo );
			OL[i].makeSpatDistriAccepTraff();
			OL[i].makeHistogramAccepTraff();
			// OL[i].printRetransmissions();
		}
	}
	*/

	/*
	 * public void plotDistris( char tipo ) { for( int i = 0; i < OL.length; i++ ) {
	 * OL[i].plotSpatDistriLat( tipo ); OL[i].plotSpatDistriLatN( tipo );
	 * OL[i].plotSpatDistriAccepTraff( tipo ); OL[i].plotSpatDistriAccepTraff3D(
	 * tipo ); } }
	 * 
	 * public void plotDistris() { boolean H = false; for( int i = 0; i < OL.length;
	 * i++ ) if( OL[i].getNPck( 'H' ) != 0 ) { H = true; break; } if( H == true
	 * ) { plotDistris( 'T' ); plotDistris( 'H' ); plotDistris( 'L' ); } else
	 * plotDistris( 'T' ); }
	 */

	/* Gera o arquivo para a confecção do CNF de Latência */
	private void makeCNFLat() {
		double offerload[] = new double[OL.length];
		double latmean[] = new double[OL.length];
		for (int i = 0; i < OL.length; i++) {
			offerload[i] = OL[i].OfferedLoad() / 100.0;
			latmean[i] = OL[i].latencyMean();
		}
		HandleFiles.WriteFile(graphsPath + "//result" + rede + "//", "CNF_Lat",
				offerload, latmean, OL.length);
	}

	/* Gera o arquivo para a confecção do CNF de Latência Normalizada */
	/*
	 * public void makeCNFLatN( char tipo ) { double offerload[] = new double[
	 * OL.length ]; double latNmean[] = new double[ OL.length ]; for( int i = 0; i < OL.length;
	 * i++ ) { offerload[i] = OL[i].getOL()/100.0; latNmean[i] =
	 * OL[i].getLatNMean( tipo ); } HandleFiles hand = new HandleFiles();
	 * hand.WriteFile( graphsPath + "//", "CNF_Latn" + tipo+rede, offerload,
	 * latNmean, OL.length ); }
	 */

	/* Gera o arquivo para a confecção do CNF de Tráfego Aceito */
	private void makeCNFAccepTraff() {
		double offerload[] = new double[OL.length];
		double accepTraffmean[] = new double[OL.length];
		for (int i = 0; i < OL.length; i++) {
			offerload[i] = OL[i].OfferedLoad() / 100.0;
			accepTraffmean[i] = OL[i].getAccepTraffMean();
		}
		HandleFiles.WriteFile(graphsPath + "//result" + rede + "//", "CNF_AT",
				offerload, accepTraffmean, OL.length);
	}

	/* Gera os arquivos para a confecção dos CNF's de um determinado tipo */
	public void makeCNFs() {
		File dir = new File(graphsPath + "//result" + rede);
		dir.mkdir();

		makeCNFAccepTraff();
		makeCNFLat();
	}

	/*
	 * public void plotCNFLat( char tipo ) { PlotGraphics.plotCNF_Lat(
	 * graphsPath + "//result_"+rede+"//", "CNF_Lat", rede ); }
	 * 
	 * public void plotCNFLatN( char tipo ) { PlotGraphics.plotCNF_LatN(
	 * graphsPath + "//", "CNF_Latn" + tipo+rede, rede ); }
	 * 
	 * public void plotCNFAccepTraff( char tipo ) { PlotGraphics.plotCNF_AT(
	 * graphsPath + "//result_"+rede+"//", "CNF_AT", rede ); }
	 * 
	 * public void plotCNFs( char tipo ) { plotCNFLat( tipo ); //plotCNFLatN(
	 * tipo ); plotCNFAccepTraff( tipo ); }
	 */
	/*
	 * public void printRetrans() { for( int i = 0; i < OL.length; i++ ) {
	 * OL[i].printRetransmissions(graphsPath);
	 * 
	 * } }
	 */
	/*
	 * public void plotCNFs() { boolean high = false; for( int i = 0; i < OL.length;
	 * i++ ) if( OL[i].getNPck( 'H' ) != 0) { high = true; break; } if ( high ==
	 * true ) { plotCNFs( 'T' ); plotCNFs( 'H' ); plotCNFs( 'L' ); } else
	 * plotCNFs( 'T' ); }
	 */
	/* Gera os relatórios de cada subteste */
	public void makeRelats() {
		for (int i = 0; i < OL.length; i++)
			OL[i].makeRelat();
	}
}