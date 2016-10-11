import java.io.File;
import java.util.*;

public class Evaluation {
	private ArrayList<Package> pcks;
	private int OL; // carga oferecida
	private String strOL;
	private String path; // caminho do teste
	private String outPath;
	private int nDot = 30; // quantidade de pontos do histograma
	private List<Double> latencies;
	private List<Double> accTraffics;

	public Evaluation(String inPath, String outPath, String rede,
			String offerLoad, String net) {
		this.strOL = offerLoad;
		this.outPath = outPath;
		this.path = inPath + File.pathSeparator + offerLoad;
		this.OL = Integer.parseInt(offerLoad.substring(1));
		ReadFiles read = new ReadFiles(path);
		this.pcks = read.read();
		latencies = read.latencies();
		accTraffics = read.accTraffics();
	}

	/* Gera o arquivo para a confecção do Distribuição Espacial da Latência Pura */
	public void makeHistLat() {
		int nPcks[] = new int[nDot]; // contém as quantidades pacotes
		double lats[] = new double[nDot]; // contém as latências distintas
		double step = (this.latencyMax() + this.latencyMin()) / (double) nDot;
		for(int i = 0; i < nDot; i++)
			lats[i] = (double)(i+1)*step;
		Collections.sort(pcks, new Package.ByLatencyComparator());
		int i = 0;
		for(Package pck: pcks)
			if(pck.latency() <= lats[i]) nPcks[i]++;
			else if(++i < nDot) nPcks[i]++;

		HandleFiles.writeToFile(outPath + File.pathSeparator + "ED_Lat" + strOL, nPcks, lats);
	}

	/*
	 * Gera o arquivo para a confecção do Distribuição Espacial do Tráfego
	 * Aceito
	 */
	public void makeHistAccepTraff() {
		int nPcks[] = new int[nDot]; // contém as quantidades pacotes
		double accepTraffs[] = new double[nDot];
		double step = (this.accepTraffMax()+this.accepTraffMin()) / (double) nDot;
		for (int i = 1; i < nDot; i++)
			accepTraffs[i] = (double)(i+1)*step;
		Collections.sort(pcks, new Package.ByAcceptedTrafficComparator());
		int i = 0;
		for(Package pck: pcks)
			if(pck.acceptedTraffic() <= accepTraffs[i]) nPcks[i]++;
			else if(++i < nDot) nPcks[i]++;

		HandleFiles.writeToFile(outPath + File.pathSeparator + "ED_AT" + strOL, nPcks, accepTraffs);
	}

	/* Escreve arquivo de relatório */
	public void makeRelat() {
		String[] Relat = new String[4];
		int i = 0;
		// nome da rede - tipo de tráfego - carga oferecida
		Relat[i++] = " - " + OL + "%";
		// porcentagem de alta e baixa prioridade
		Relat[i++] = "Quantidade total de pacotes:  " + pcks.size();
			// latência pura: média+-desvio / [máximo,mínimo]
			Relat[i++] = "Latencia Total:  [" + latencyMin() + " : "
					+ averageLatency() + "/" + latencyStdDev() + " : "
					+ latencyMax() + "]";
			// tráfego aceito: média+-desvio / [máximo,mínimo]
			Relat[i++] = "Trafego Aceito Total:  [" + accepTraffMin()
					+ " : " + averageAccepTraff() + "/"
					+ accepTraffStdDev() + " : " + accepTraffMax()
					+ "]";

			HandleFiles.writeToFile(outPath + File.pathSeparator + "Report" + strOL, Relat);
	}

	/* geta a carga oferecida do subteste */
	public int OfferedLoad() {
		return OL;
	}

	public double averageLatency() // média
	{
		return 0.0;
	}

	private double latencyStdDev() // desvio padrão
	{
		if (pcks.size() != 0) {
			double latMean = averageLatency();
			double sum = 0;
			for(Package pck: pcks)
				sum += Math.pow(pck.latency() - latMean, 2);
			return Math.sqrt(sum / pcks.size());
		}
		return -1.0;
	}

	private double latencyMax() // máxima
	{
		return 0.0;
	}

	private double latencyMin() // mínima
	{
		return 0.0;
	}

	public double averageAccepTraff() // média
	{
		return 0.0;
	}

	private double accepTraffStdDev() // desvio padrão
	{
		if (pcks.size() != 0) {
			double accepTraffMean = averageLatency();
			double sum = 0;
			for(Package pck: pcks)
					sum += Math.pow(pck.acceptedTraffic() - accepTraffMean, 2);
			return Math.sqrt(sum / (double) pcks.size());
		}
		return -1.0;
	}

	private double accepTraffMax() // máximo
	{
		return 0.0;
	}

	private double accepTraffMin() // mínimo
	{
		return 0.0;
	}

}