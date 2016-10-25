import java.io.File;
import java.util.*;

public class ReadFiles {

	private Scanner file;
	private ArrayList<Package> pcks = new ArrayList<Package>();
	private String path;
	List<Double> latencies = new ArrayList<>();
	List<Double> accTraffics = new ArrayList<>();

	
	public ReadFiles(String path) {
		this.path = path;
	}

	public ArrayList<Package> read() {

		String[] listfiles = HandleFiles.filesAt(path); // Lista todos os arquivos

		for (int numbfiles = 0; numbfiles < listfiles.length; numbfiles++) {

			file = HandleFiles.openFile(path + File.separator + listfiles[numbfiles]);
			while(file.hasNext()) {
				Package act = ReadOnePacket();
				pcks.add(act);
				latencies.add(act.latency());
			}
			file.close();
		}
			
		Collections.sort(pcks); // ordena pelo número de identificação do pacote
		double acceptedTraffic = 0;
		for(int i = 0; i < pcks.size()-1; i++) {
			Package act = pcks.get(i);
			Package next = pcks.get(i+1);
			
			if(act.src().equals(next.src())) {
				acceptedTraffic = (double)act.size()/(next.entryTime()-act.entryTime());
			}
			act.setAcceptedTraffic(acceptedTraffic);
			accTraffics.add(acceptedTraffic);
		}
		pcks.get(pcks.size()-1).setAcceptedTraffic(acceptedTraffic);

		return pcks;
	}

	private Package ReadOnePacket() {
		String binTarget = String.format("%16s", Integer.toBinaryString(Integer.parseInt(file.next(), 16))).replace(" ", "0");
		int size = Integer.parseInt(file.next(), 16) + 2;
		String binSource = String.format("%16s", Integer.toBinaryString(Integer.parseInt(file.next(), 16))).replace(" ", "0");
		
		int id = Integer.parseInt(file.next() + file.next(), 16);
		
		double entryTime = Double.parseDouble(file.next());
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

		Package pck = new Package(target, size, source, Latency, entryTime, id);

		return pck;
	}

	public List<Double> latencies() {
		return this.latencies;
	}

	public List<Double> accTraffics() {
		return this.accTraffics;
	}

}
