import java.util.ArrayList;
import java.util.Collections;


public class Evaluation {
	private ArrayList<Package> pcks;
	private int OL; // carga oferecida
	private String strOL;
	private String path; // caminho do teste
	private String outPath;
	private int nDot = 30; // quantidade de pontos do histograma
	private double[] lat;
	private double[] accTraffic;

	public Evaluation(String inPath, String outPath, String rede,
			String offerLoad, String net) {
		this.strOL = offerLoad;
		this.outPath = outPath;
		this.path = inPath + "//" + offerLoad;
		this.OL = Integer.parseInt(offerLoad.substring(1));
		ReadFiles read = new ReadFiles(path);
		this.pcks = read.read();
		lat = read.latStats();
		accTraffic = read.accTrafficStats();
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

		HandleFiles.WriteFile(outPath + "//", "ED_Lat" + strOL, nPcks, lats, nDot);
	}

	/*
	 * Gera o arquivo para a confecção do Distribuição Espacial do Tráfego
	 * Aceito
	 */
	public void makeHistAccepTraff() {
		int nPcks[] = new int[nDot]; // contém as quantidades pacotes
		double accepTraffs[] = new double[nDot];
		double step = (this.getAccepTraffMax()+this.getAccepTraffMin()) / (double) nDot;
		for (int i = 1; i < nDot; i++)
			accepTraffs[i] = (double)(i+1)*step;
		Collections.sort(pcks, new Package.ByAcceptedTrafficComparator());
		int i = 0;
		for(Package pck: pcks)
			if(pck.acceptedTraffic() <= accepTraffs[i]) nPcks[i]++;
			else if(++i < nDot) nPcks[i]++;

		HandleFiles.WriteFile(outPath + "//", "ED_AT" + strOL, nPcks, accepTraffs, nDot);
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
					+ latencyMean() + "/" + latencyStdDev() + " : "
					+ latencyMax() + "]";
			// tráfego aceito: média+-desvio / [máximo,mínimo]
			Relat[i++] = "Trafego Aceito Total:  [" + getAccepTraffMin()
					+ " : " + getAccepTraffMean() + "/"
					+ getAccepTraffStdDev() + " : " + getAccepTraffMax()
					+ "]";

			HandleFiles.WriteFile(outPath + "//", "Relatorio" + strOL, Relat);
	}

	/* geta a carga oferecida do subteste */
	public int OfferedLoad() {
		return OL;
	}

	public double latencyMean() // média
	{
		return lat[1];
	}

	private double latencyStdDev() // desvio padrão
	{
		if (pcks.size() != 0) {
			double latMean = lat[1];
			double sum = 0;
			for(Package pck: pcks)
				sum += Math.pow(pck.latency() - latMean, 2);
			return Math.sqrt(sum / pcks.size());
		}
		return -1.0;
	}

	private double latencyMax() // máxima
	{
		return lat[2];
	}

	private double latencyMin() // mínima
	{
		return lat[0];
	}

	public double getAccepTraffMean() // média
	{
		return accTraffic[1];
	}

	private double getAccepTraffStdDev() // desvio padrão
	{
		if (pcks.size() != 0) {
			double accepTraffMean = accTraffic[1];
			double sum = 0;
			for(Package pck: pcks)
					sum += Math.pow(pck.acceptedTraffic() - accepTraffMean, 2);
			return Math.sqrt(sum / (double) pcks.size());
		}
		return -1.0;
	}

	private double getAccepTraffMax() // máximo
	{
		return accTraffic[2];
	}

	private double getAccepTraffMin() // mínimo
	{
		return accTraffic[0];
	}

}