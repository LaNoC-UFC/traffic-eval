import java.util.Comparator;

class Package implements Comparable<Package> {
    private int size;
    private double latency;
    private double acceptedTraffic;
    private double tpflext;
    private String source;
    private String target;
    private int id;

    Package(String target, int size, String source, double latency, double tpflext, int id) {
        this.size = size;
        this.latency = latency;
        this.source = source;
        this.target = target;
        this.tpflext = tpflext;
        this.id = id;
    }

    double latency() {
        return latency;
    }

    int size() {
        return size;
    }

    double tpfext() {
        return tpflext;
    }

    String src() {
        return source;
    }

    double acceptedTraffic() {
        return acceptedTraffic;
    }

    void setAcceptedTraffic(double acceptedTraffic) {
        this.acceptedTraffic = acceptedTraffic;
    }

    @Override
    public int compareTo(Package that) {
        if (this.id < that.id) {
            return -1;
        }
        if (this.id > that.id) {
            return +1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "#" + id +
                ": " + source +
                "->" + target +
                ", lat: " + latency +
                " accTraf: " + acceptedTraffic +
                ", tpfext: " + tpflext;
    }

    static class ByLatencyComparator implements Comparator<Package> {
        @Override
        public int compare(Package one, Package another) {
            if (one.latency < another.latency) {
                return -1;
            }
            if (one.latency > another.latency) {
                return +1;
            }
            return 0;
        }
    }

    static class ByAcceptedTrafficComparator implements Comparator<Package> {
        @Override
        public int compare(Package one, Package another) {
            if (one.acceptedTraffic < another.acceptedTraffic) {
                return -1;
            }
            if (one.acceptedTraffic > another.acceptedTraffic) {
                return +1;
            }
            return 0;
        }
    }
}
