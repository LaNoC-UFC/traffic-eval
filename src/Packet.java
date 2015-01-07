
public class Packet {
	private int size;
	private double latency;
	private double accepTraffic;
	private double tpflext;
	/*
	private String source;
	private String target;
	private String[] flux = new String[2]; // [0]-src [1]-dst
	*/
	public Packet(String target, int size, String source, double latency,
			double tpflext) {
		this.size = size;
		this.latency = latency;
		//this.source = source;
		//this.target = target;
		this.tpflext = tpflext;
	}

	/*
	private String[] getflux() {
		return flux;
	}

	private int getXt() {
		return Integer.parseInt(target.substring(2, 3));
	}

	private int getXs() {
		return Integer.parseInt(source.substring(2, 3));
	}

	private int getYt() {
		return Integer.parseInt(target.substring(3, 4));
	}

	private int getYs() {
		return Integer.parseInt(source.substring(3, 4));
	}

	private String getSource() {
		return source;
	}

	private String getTarget() {
		return target;
	}
	*/

	public double latency() {
		return latency;
	}

	public int size() {
		return size+2;
	}

	public double getTpflext() {
		return tpflext;
	}

	public double accepTraffic() {
		return accepTraffic;
	}

	public void setAccepTraffic(double acceptraffic) {
		this.accepTraffic = acceptraffic;
	}

	/*
	 * public void setLatency(double latency) { this.latency = latency; }

	private void setFlux(String src, String dst) {
		this.flux[0] = src;
		this.flux[1] = dst;
	}

	private void setSource(String source) {
		this.source = source;
	}

	private void setTarget(String target) {
		this.target = target;
	}

	private void setLastAccepTraffic(double acceptraffic) {
		this.accepTraffic = acceptraffic;
	}

	 */
}
