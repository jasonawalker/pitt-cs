import java.util.Queue;
import java.util.LinkedList;

public class FordFulkerson{
    private int v;
    private boolean visited[];
    private Edge[] edgeTo;
    private int maxFlow;


    public FordFulkerson(Graph g, int s, int t){
        for(Edge e : g.getAllEdges()){
            e.setFlow(0);
        }

        v = g.getSize();
        maxFlow = excess(g, t);

        while(hasAugmentingPath(g,s,t)){
            int capacity = Integer.MAX_VALUE;
            for(int v=t; v != s; v=edgeTo[v].other(v)){
                capacity = Math.min(capacity, edgeTo[v].capacityTo(v));
            }

            for(int v=t; v != s; v=edgeTo[v].other(v)) {
                edgeTo[v].addFlow(v, capacity);
            }

            maxFlow += capacity;
        }

    }

    private int excess(Graph g, int v){
        int ex = 0;
        Vertex[] graph = g.getVertices();

        for(Edge e : graph[v].getEdges()){
            if(v == e.getFrom().getId()){
                ex -= e.getFlow();
            } else {
                ex += e.getFlow();
            }
        }

        return ex;
    }

    private boolean hasAugmentingPath(Graph g, int s, int t){
        Vertex[] graph = g.getVertices();
        edgeTo = new Edge[g.getSize()];
        visited = new boolean[g.getSize()];

        Queue<Integer> q = new LinkedList<>();
        q.add(s);
        visited[s] = true;

        while(!q.isEmpty() && !visited[t]){
            int v = q.remove();

            for(Edge e : graph[v].getEdges()){
                int w = e.other(v);

                if(e.capacityTo(w) > 0){
                    if(!visited[w]){
                        edgeTo[w] = e;
                        visited[w] = true;
                        q.add(w);
                    }
                }
            }
        }

        return visited[t];
    }

    public int getMaxFlow(){ return maxFlow; }

}
