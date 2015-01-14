
import java.io.*;

public class TrafficMeasurer {
	private Evaluation[] OL; // cada uma das cargas oferecidas
	private String rede; // nome da rede
	private String graphsPath;

	public static void main(String[] args) {
		String path = "./evaluate";
		
		if (args.length == 1) {
			path = "./" + args[0];
		}
		
		System.out.println("Evaluating traffic from " + path);
		TrafficMeasurer eval = new TrafficMeasurer("", path, path);
		eval.makeCNFs();
		eval.genHistograms();
		eval.makeRelats();
	}

	public TrafficMeasurer(String nome, String pathTst, String graphsPath) {
		this.graphsPath = graphsPath;
		this.rede = nome;
		File folder = new File(pathTst);
		String[] pathOL = folder.list();
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

	/* Gera os relatórios de cada subteste */
	public void makeRelats() {
		for (int i = 0; i < OL.length; i++)
			OL[i].makeRelat();
	}
}