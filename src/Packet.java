
public class Packet implements Comparable<Packet> {
	private int size;
	private double latency;
	private double accepTraffic;
	private double tpflext;
	private String source;
	private String target;
	private int id;

	public Packet(String target, int size, String source, double latency,
			double tpflext, int id) {
		this.size = size+2;
		this.latency = latency;
		this.source = source;
		this.target = target;
		this.tpflext = tpflext;
		this.id = id;
	}

	public double latency() {
		return latency;
	}

	public int size() {
		return size;
	}

	public double tpfext() {
		return tpflext;
	}
	
	public String src() {
		return source;
	}

	public String dst() {
		return target;
	}

	public double accepTraffic() {
		return accepTraffic;
	}

	public void setAccepTraffic(double acceptraffic) {
		this.accepTraffic = acceptraffic;
	}

	@Override
	public int compareTo(Packet that) {
		if(this.id < that.id) return -1;
		if(this.id > that.id) return +1;
		return 0;
	}

	@Override
	public String toString() {
		String s = "#"+id+": "+source+"->"+target+", lat: "+latency+" accTraf: "+accepTraffic+", tpfext: "+tpflext;
		return s;
	}
}
