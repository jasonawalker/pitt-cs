import java.util.Arrays;
import java.util.LinkedList;

public class Vertex {
    private LinkedList<Edge> edges;
    private int id;

    public Vertex(LinkedList<Edge> edges, int id) {
        this.edges = edges;
        this.id = id;
    }

    public LinkedList<Edge> getEdges() { return edges; }
    public int getId() { return id; }

    @Override
    public String toString() {
        Edge[] edgeArray = edges.toArray(new Edge[edges.size()]);

        return id + "{" + "Edges=" + Arrays.toString(edgeArray) + '}';
    }
}
