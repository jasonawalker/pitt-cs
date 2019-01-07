public class Edge {
    private Vertex from;
    private Vertex to;
    private String type;
    private int bandwidth;
    private int length;
    private double time; //nanoseconds
    private int flow;

    public Edge(Vertex from, Vertex to, String type, int bandwidth, int length) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.bandwidth = bandwidth;
        this.length = length;
        this.time = type.equals("copper") ? (length/0.23) : (length/0.20);
        this.flow = 0;
    }

    public Vertex getFrom() { return from; }
    public Vertex getTo() { return to; }
    public String getType() { return type; }
    public int getBandwidth() { return bandwidth; }
    public int getLength() { return length; }
    public double getTime() { return time; }
    public int getFlow() { return flow; }

    public void setFlow(int f) {this.flow = f; }

    @Override
    public String toString() {
        String s = "Edge {" + from.getId() +
                "->" + to.getId() +
                ", " + type +
                ", " + bandwidth +
                ", " + length +
                '}';

        s += " " + time;
        return s;
    }

    public int other(int v){
        if(v == from.getId()){
            return to.getId();
        } else if (v == to.getId()){
            return from.getId();
        } else {
            throw new IllegalArgumentException("other exception");
        }
    }
    public int capacityTo(int v){
        if(v == from.getId()){
            return flow;
        } else if (v == to.getId()){
            return bandwidth - flow;
        } else {
            throw new IllegalArgumentException("capacity exception");
        }
    }
    public void addFlow(int v, int f){
        if(v==from.getId()){
            flow -= f;
        } else if(v==to.getId()){
            flow += f;
        } else {
            throw new IllegalArgumentException("flow exception");
        }
    }
}
