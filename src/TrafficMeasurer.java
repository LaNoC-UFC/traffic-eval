import java.io.*;
import java.util.*;

public class TrafficMeasurer {
    private Evaluation[] OL;
    private String rede;
    private String outPath;
    private String targetAddress;
    private String sourceAddress ;

    public static void main(String[] args) {
        String inPath = "./evaluate";
        String outPath = "./results";

        String sourceAddress = "0001";
        String targetAddress = "0000";

        if (args.length == 1) {
            inPath = args[0];
        }
        else if (args.length == 2) {
            inPath = args[0];
            outPath = args[1];
        }

        System.out.println("Evaluating traffic from " + inPath);

        TrafficMeasurer eval = null;
        if(sourceAddress.isEmpty() && targetAddress.isEmpty()){
            eval = new TrafficMeasurer("", inPath, outPath);
        }else{
            eval = new TrafficMeasurer("", inPath, outPath, targetAddress, sourceAddress);
        }

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
        OL = new Evaluation[pathOL.length];
        for (int i = 0; i < OL.length; i++)
            OL[i] = new Evaluation(inPath, outPath, rede, pathOL[i], rede);
    }

    public TrafficMeasurer(String nome, String inPath, String outPath, String targetAddress, String sourceAddress) {
        this.outPath = outPath;
        this.rede = nome;

        this.targetAddress = targetAddress;
        this.sourceAddress = sourceAddress ;

        File folder = new File(inPath);
        String[] pathOL = folder.list();
        OL = new Evaluation[pathOL.length];
        for (int i = 0; i < OL.length; i++)
            OL[i] = new Evaluation(inPath, outPath, rede, pathOL[i], rede, targetAddress, sourceAddress);
    }

    public void genHistograms() {
        for (Evaluation e : OL) {
            e.writeHistogramOfLatency();
            e.writeHistogramOfAccepTraff();
        }
    }

    public void makeCNFs() {
        File dir = new File(outPath + rede);
        dir.mkdir();
        makeCNFAccepTraff();
        makeCNFLat();
    }

    public void makeRelats() {
        for (int i = 0; i < OL.length; i++)
            OL[i].makeRelat();
    }

    private void makeCNFLat()  {
        double[] averageLatency = new double[OL.length];
        for (int i = 0; i < OL.length; i++) {
            averageLatency[i] = OL[i].averageLatency();
        }
        makeCNF(averageLatency , "CNF_Lat");
    }

    private void makeCNFAccepTraff() {
        double[] averageAccepTraff = new double[OL.length];
        for (int i = 0; i < OL.length; i++) {
            averageAccepTraff[i] = OL[i].averageAccepTraff();
        }
        makeCNF(averageAccepTraff, "CNF_AT");
    }

    private void makeCNF (double[] vectormean, String filename) {
        double offerload[] = new double[vectormean.length];
        for (int i = 0; i < OL.length; i++) {
            offerload[i] = OL[i].OfferedLoad() / 100.0;
        }
        HandleFiles.writeToFile(outPath + rede + File.separator + filename, offerload, vectormean);
    }
}
