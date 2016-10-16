import java.io.File;
import java.util.*;

public class ReadFiles {

	private Scanner file;
	private ArrayList<Package> pcks = new ArrayList<Package>();
	private String path;
	private double[] lat = {Double.POSITIVE_INFINITY, 0, Double.NEGATIVE_INFINITY};
	private double[] accTraffic = {Double.POSITIVE_INFINITY, 0, Double.NEGATIVE_INFINITY};

	
	public ReadFiles(String path) {
		this.path = path;
	}

	public ArrayList<Package> read() {

		String[] listfiles = HandleFiles.filesAt(path); // Lista todos os arquivos
		
		lat[1] = 0;
		for (int numbfiles = 0; numbfiles < listfiles.length; numbfiles++) {

			file = HandleFiles.openFile(path + File.pathSeparator + listfiles[numbfiles]);
			while(file.hasNext()) {
				Package act = ReadOnePacket();
				pcks.add(act);
				double latency = act.latency();
				lat[0] = (latency < lat[0]) ? latency : lat[0];
				lat[1] += latency;
				lat[2] = (latency > lat[2]) ? latency : lat[2];
			}
			file.close();
		}
			
		Collections.sort(pcks); // ordena pelo número de identificação do pacote
		double acceptedTraffic = 0;
		for(int i = 0; i < pcks.size()-1; i++) {
			Package act = pcks.get(i);
			Package next = pcks.get(i+1);
			
			if(act.src().equals(next.src()))
				acceptedTraffic = (double)act.size()/(next.tpfext()-act.tpfext());
			act.setAcceptedTraffic(acceptedTraffic);
			
			accTraffic[0] = (acceptedTraffic < accTraffic[0]) ? acceptedTraffic : accTraffic[0];
			accTraffic[1] += acceptedTraffic;
			accTraffic[2] = (acceptedTraffic > accTraffic[2]) ? acceptedTraffic : accTraffic[2];
		}
		pcks.get(pcks.size()-1).setAcceptedTraffic(acceptedTraffic);

		accTraffic[1] /= (double)pcks.size();
		lat[1] /= (double)pcks.size();

		return pcks;
	}

	private Package ReadOnePacket() {
		String binTarget = String.format("%16s", Integer.toBinaryString(Integer.parseInt(file.next(), 16))).replace(" ", "0");
		int size = Integer.parseInt(file.next(), 16) + 2;
		String binSource = String.format("%16s", Integer.toBinaryString(Integer.parseInt(file.next(), 16))).replace(" ", "0");
		
		int id = Integer.parseInt(file.next() + file.next(), 16);
		
		double Tpflext = Double.parseDouble(file.next());
		double Latency = Double.parseDouble(file.next());

		file.nextLine(); // Pega o que sobrou da linha

		int tX = Integer.parseInt(
				binTarget.substring(0, binTarget.length() / 2), 2);
		int tY = Integer
				.parseInt(
						binTarget.substring(binTarget.length() / 2,
								binTarget.length()), 2);
		int sX = Integer.parseInt(
				binSource.substring(0, binSource.length() / 2), 2);
		int sY = Integer
				.parseInt(
						binSource.substring(binSource.length() / 2,
								binSource.length()), 2);

		String target = tX + "." + tY;
		String source = sX + "." + sY;

		Package pck = new Package(target, size, source, Latency, Tpflext, id);

		return pck;
	}

	public double[] latStats() {
		return lat;
	}

	public double[] accTrafficStats() {
		return accTraffic;
	}

}
