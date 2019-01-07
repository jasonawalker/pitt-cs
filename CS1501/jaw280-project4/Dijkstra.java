import java.util.Stack;

public class Dijkstra {
    private double[] distTo;
    private Edge[] edgeTo;
    private IndexMinPQ<Double> pq;

    public Dijkstra(Graph g, int s){
        Vertex[] vertices = g.getVertices();

        distTo = new double[g.getSize()];
        edgeTo = new Edge[g.getSize()];
        pq = new IndexMinPQ<>(g.getSize());

        for(int i = 0; i < g.getSize(); i++){ distTo[i] = Double.POSITIVE_INFINITY; }
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);

        while(!pq.isEmpty()){
            int v = pq.delMin();
            for(Edge e : vertices[v].getEdges()){
                relax(e);
            }
        }
    }

    private void relax(Edge e){
        int from = e.getFrom().getId();
        int to = e.getTo().getId();

        if(distTo[to] > distTo[from] + e.getTime()){
            distTo[to] = distTo[from] + e.getTime();
            edgeTo[to] = e;

            if(pq.contains(to)){
                pq.decreaseKey(to, distTo[to]);
            } else {
                pq.insert(to, distTo[to]);
            }
        }
    }

    public double getDistTo(int v){ return distTo[v]; }
    public boolean hasPathTo(int v){return distTo[v] < Double.POSITIVE_INFINITY; }
    public Stack<Edge> getPathTo(int v){
        Stack<Edge> path = new Stack<>();
        for(Edge e = edgeTo[v]; e != null; e = edgeTo[e.getFrom().getId()]){
            path.push(e);
        }
        return path;
    }
}
