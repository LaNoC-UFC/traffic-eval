import java.io.*;

public class TrafficMeasurer {
	private Evaluation[] OL; // cada uma das cargas oferecidas
	private String rede; // nome da rede
	private String outPath;

	public static void main(String[] args) {
		String inPath = "./evaluate";
		String outPath = "./results";

		if (args.length == 1) {
			inPath = args[0];
		}
		else if (args.length == 2) {
			inPath = args[0];
			outPath = args[1];
		}

		System.out.println("Evaluating traffic from " + inPath);
		TrafficMeasurer eval = new TrafficMeasurer("", inPath, outPath);
		eval.makeCNFs();
		eval.genHistograms();
		eval.makeRelats();
		System.out.println("Results in " + outPath);
	}

	public TrafficMeasurer(String nome, String inPath, String outPath) {
		this.outPath = outPath;
		this.rede = nome;
		File folder = new File(inPath);
		String[] pathOL = folder.list();
		// aloca OL
		OL = new Evaluation[pathOL.length];
		// aloca e inicializa cada OL[i]
		for (int i = 0; i < OL.length; i++)
			OL[i] = new Evaluation(inPath, outPath, rede, pathOL[i], rede);
	}

	public void genHistograms() {
		for (Evaluation e : OL) {
			e.makeHistLat();
			e.makeHistAccepTraff();
		}
	}

	/* Gera os arquivos para a confecção dos CNF's de um determinado tipo */
	public void makeCNFs() {
		File dir = new File(outPath + rede);
		dir.mkdir();

		makeCNFAccepTraff();
		makeCNFLat();
	}

	/* Gera os relatórios de cada subteste */
	public void makeRelats() {
		for (int i = 0; i < OL.length; i++)
			OL[i].makeRelat();
	}
	
	/* Gera o arquivo para a confecção do CNF de Latência */
	private void makeCNFLat() {
		double offerload[] = new double[OL.length];
		double latmean[] = new double[OL.length];
		for (int i = 0; i < OL.length; i++) {
			offerload[i] = OL[i].OfferedLoad() / 100.0;
			latmean[i] = OL[i].latencyMean();
		}
		HandleFiles.writeToFile(outPath + rede + File.pathSeparator + "CNF_Lat", offerload, latmean);
	}

	/* Gera o arquivo para a confecção do CNF de Tráfego Aceito */
	private void makeCNFAccepTraff() {
		double offerload[] = new double[OL.length];
		double accepTraffmean[] = new double[OL.length];
		for (int i = 0; i < OL.length; i++) {
			offerload[i] = OL[i].OfferedLoad() / 100.0;
			accepTraffmean[i] = OL[i].getAccepTraffMean();
		}
		HandleFiles.writeToFile(outPath + rede + File.pathSeparator + "CNF_AT", offerload, accepTraffmean);
	}

}