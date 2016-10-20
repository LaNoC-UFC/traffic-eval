import java.io.*;
import java.util.*;

public class Evaluation {
	private ArrayList<Package> pcks;
    private int OL;
	private String strOL;
	private String path;
	private String outPath;
	private int nDot = 30;
	public List<Double> latencies;
	public List<Double> accTraffics;

    public Evaluation(String inPath, String outPath, String rede,
			String offerLoad, String net) {
		this.strOL = offerLoad;
		this.outPath = outPath;
		this.path = inPath + File.separator + offerLoad;
		this.OL = Integer.parseInt(offerLoad.substring(1));
		ReadFiles read = new ReadFiles(path);
		this.pcks = read.read();
		latencies = read.latencies();
		accTraffics = read.accTraffics();
	}

	public void writeHistogramOfLatency() {
		makeHist(latencies ,"ED_Lat", new Package.ByLatencyComparator());
	}

	public void writeHistogramOfAccepTraff() {
		makeHist(accTraffics , "ED_AT" , new Package.ByAcceptedTrafficComparator());
	}

	public void makeRelat() {
		String[] Relat = new String[4];
		int i = 0;
		Relat[i++] = " - " + OL + "%";
		Relat[i++] = "Quantidade total de pacotes:  " + pcks.size();
			Relat[i++] = "Latencia Total:  [" + Collections.min(latencies, null) + " : "
					+ averageLatency() + "/" + latencyStdDev() + " : "
					+ Collections.max(latencies, null) + "]";
			Relat[i++] = "Trafego Aceito Total:  [" + Collections.min(accTraffics, null)
					+ " : " + averageAccepTraff() + "/"
					+ accepTraffStdDev() + " : " + Collections.max(accTraffics, null)
					+ "]";
			HandleFiles.writeToFile(outPath + File.separator + "Report" + strOL, Relat);
	}

	public int OfferedLoad() {
		return OL;
	}

	public double averageLatency() {
		double accLatency = 0.0;
		for(double latency : latencies) {
			accLatency += latency;
		}
		return accLatency/latencies.size();
	}

	private void makeHist(List<Double> vector ,String fileName, Comparator<Package> comparator) {
		int i = 0;
		double vectors[] = new double[nDot];
		int nPcks[] = new int[nDot];
		Collections.sort(pcks, comparator);
		double step = (Collections.max(vector, null) + Collections.min(vector, null)) / (double) nDot;
		for (int j = 1; j < nDot; j++) {
			vectors[j] = (double)(j+1)*step;
		}
		for(Double vec : vector) {
			if(vec <= vectors[i]){
				nPcks[i]++;
			}
			else if(++i < nDot){
				nPcks[i]++;
			}
		}
		HandleFiles.writeToFile(outPath + File.separator + fileName + strOL, nPcks, vectors);
	}

	private double latencyStdDev() {
		if (pcks.size() != 0) {
			double latMean = averageLatency();
			double sum = 0;
			for(Package pck: pcks)
				sum += Math.pow(pck.latency() - latMean, 2);
			return Math.sqrt(sum / pcks.size());
		}
		return -1.0;
	}

	public double averageAccepTraff() {
		double accAccTraffic = 0.0;
		for(double accTraffic : accTraffics) {
			accAccTraffic += accTraffic;
		}
		return accAccTraffic/accTraffics.size();
	}

	private double accepTraffStdDev() {
		if (pcks.size() != 0) {
			double accepTraffMean = averageAccepTraff();
			double sum = 0;
			for(Package pck: pcks)
					sum += Math.pow(pck.acceptedTraffic() - accepTraffMean, 2);
			return Math.sqrt(sum / (double) pcks.size());
		}
		return -1.0;
	}
}
