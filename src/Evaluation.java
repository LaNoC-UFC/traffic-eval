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

    public Evaluation(String inPath, String outPath, String rede,
                      String offerLoad, String net, String targetAddress, String sourceAddress) {
        this.strOL = offerLoad;
        this.outPath = outPath;
        this.path = inPath + File.separator + offerLoad;
        this.OL = Integer.parseInt(offerLoad.substring(1));
        ReadFiles read = new ReadFiles(path);
        this.pcks = read.readSpecificTarget(targetAddress, sourceAddress);
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

    public double averageLatency() {
        return averageOf(latencies);
    }

    private double latencyStdDev() {
        return stdDeviationOf(latencies);
    }

    public double averageAccepTraff() {
        return averageOf(accTraffics);
    }

    private double accepTraffStdDev() {
        return stdDeviationOf(accTraffics);
    }

    private double stdDeviationOf(List<Double> values) {
        assert (values.size() != 0) : "Size Values is 0";
        double average = averageOf(values);
        double accumulator = 0.0;
        for(double value: values) {
            accumulator += Math.pow(value - average, 2);
        }
        return Math.sqrt(accumulator / values.size());
    }

    private double averageOf(List<Double> values){
        assert (values.size() != 0) : "Size Values is 0";
        double accumulator = 0.0;
        for (double value : values) {
            accumulator += value;
        }
        return accumulator / values.size();
    }
}
