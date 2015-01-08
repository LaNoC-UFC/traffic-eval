import java.util.*;

public class ReadFiles {

	private Scanner file;
	private ArrayList<Packet> pcks = new ArrayList<Packet>();
	//private Map<String, Integer> nRetranspFLuxs = new HashMap<String, Integer>();
	private String path;
	private double[] lat = {Double.POSITIVE_INFINITY, 0, Double.NEGATIVE_INFINITY};
	private double[] accTraffic = {Double.POSITIVE_INFINITY, 0, Double.NEGATIVE_INFINITY};

	
	public ReadFiles(String path) {
		this.path = path;
	}

	public ArrayList<Packet> read() {

		String[] listfiles; // files contem os arquivos do path

		listfiles = HandleFiles.getFilepaths(path); // Lista todos os arquivos
													// de um diretorio
		
		lat[1] = 0;
		for (int numbfiles = 0; numbfiles < listfiles.length; numbfiles++) {

			file = HandleFiles.OpenFilestoRead(path +"//"+ listfiles[numbfiles]);
			while(file.hasNext()) {
				Packet act = ReadOnePacket();
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
			Packet act = pcks.get(i);
			Packet next = pcks.get(i+1);
			
			if(act.src().equals(next.src()))
				acceptedTraffic = (double)act.size()/(next.tpfext()-act.tpfext());
			act.setAccepTraffic(acceptedTraffic);
			
			System.out.println(act);
			accTraffic[0] = (acceptedTraffic < accTraffic[0]) ? acceptedTraffic : accTraffic[0];
			accTraffic[1] += acceptedTraffic;
			accTraffic[2] = (acceptedTraffic > accTraffic[2]) ? acceptedTraffic : accTraffic[2];
		}
		pcks.get(pcks.size()-1).setAccepTraffic(acceptedTraffic);

		accTraffic[1] /= (double)pcks.size();
		lat[1] /= (double)pcks.size();

		//Packet[] Aux = pcks.toArray(new Packet[0]);
		//return Aux;
		return pcks;

	}

	private Packet ReadOnePacket() {
		String binTarget = String.format("%16s", Integer.toBinaryString(Integer.parseInt(file.next(), 16))).replace(" ", "0");
		int size = Integer.parseInt(file.next(), 16);
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

		// System.out.println("Target: " + target + " Source: " + source);
		Packet pck = new Packet(target, size, source, Latency, Tpflext, id);

		return pck;
	}

	/*
	public Map<String, Integer> getnRetranspFluxs() {
		return this.nRetranspFLuxs;
	}
	*/
	
	public double[] latStats() {
		return lat;
	}

	public double[] accTrafficStats() {
		return accTraffic;
	}

}
