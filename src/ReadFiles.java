import java.util.*;

public class ReadFiles {

	private Scanner file;
	private LinkedList<Packet> pcks = new LinkedList<Packet>();
	private Map<String, Integer> nRetranspFLuxs = new HashMap<String, Integer>();
	private String path;
	
	public ReadFiles(String path) {
		this.path = path;
	}

	public Packet[] read() {

		String[] listfiles; // files contem os arquivos do path

		listfiles = HandleFiles.getFilepaths(path); // Lista todos os arquivos
													// de um diretorio

		for (int numbfiles = 0; numbfiles < listfiles.length; numbfiles++) {

			file = HandleFiles.OpenFilestoRead(path + "//"
					+ listfiles[numbfiles]); // Abre arquivos no path para
												// leitura file é arquivo que
												// será lido
			// System.out.println(path + "//" + listfiles[numbfiles]);
			// System.out.println("file"+listfiles[numbfiles]);

			double LastAccTraf = 0;

			// Ler primeiro pacote, ler segundo, seta AccTraf do primeiro add
			// ele e chama o segundo de aux...
			Packet aux = ReadOnePacket();
			while (file.hasNext()) {

				Packet pck = ReadOnePacket();

				double accepTraffic = (double) aux.size()/(pck.getTpflext() - aux.getTpflext());
				
				aux.setAccepTraffic(accepTraffic);
				LastAccTraf = accepTraffic;
				System.out.println("Acc Traff: " + LastAccTraf);
				pcks.add(aux);

				aux = pck;
			} // end of while

			aux.setAccepTraffic(LastAccTraf);
			System.out.println("Acc Traff: " + LastAccTraf);
			pcks.add(aux); // Adiciona o ultimo auxiliar na lista.

		}
		// Collections.sort(pcks, pcks);
		Packet[] Aux = pcks.toArray(new Packet[0]); // Converte Lista pcks em
													// Array Aux e retorna
		return Aux;

	}

	private Packet ReadOnePacket() {
		String binTarget = String.format("%16s",
				Integer.toBinaryString(Integer.parseInt(file.next(), 16)))
				.replace(" ", "0");
		int size = Integer.parseInt(file.next(), 16);
		String binSource = String.format("%16s",
				Integer.toBinaryString(Integer.parseInt(file.next(), 16)))
				.replace(" ", "0");
		Integer.parseInt(file.next() + file.next(), 16);

		double Tpflext = Double.parseDouble(file.next());
		System.out.println("Tpflext: " + Tpflext);
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
		Packet pck = new Packet(target, size, source, Latency, Tpflext);

		return pck;

	}

	public Map<String, Integer> getnRetranspFluxs() {
		return this.nRetranspFLuxs;
	}

}
