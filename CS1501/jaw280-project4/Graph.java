import java.util.LinkedList;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Queue;

public class Graph {
    private Vertex[] vertices;
    private ArrayList<Edge> edges;
    private int size;
    private boolean copper;
    private boolean stable;

    public Graph(Vertex[] vertices, int size, ArrayList<Edge> edges) {
        this.vertices = vertices;
        this.size = size;
        this.edges = edges;
        this.copper = copperConnected();
        this.stable = determineStability();
    }

    public Vertex[] getVertices() { return vertices; }
    public int getSize() { return size; }
    public boolean getCopperConnected(){ return copper; }
    public boolean getStability(){ return stable; }
    public ArrayList<Edge> getAllEdges(){ return edges; }

    @Override
    public String toString() {
        String s = "";

        for(int i = 0; i < size; i++){
            s += vertices[i].toString() + "\n";
        }

        return s;
    }

    private boolean copperConnected(){
        boolean[] visited = new boolean[size];
        Queue<Vertex> q = new LinkedList<>();

        q.add(vertices[0]);

        while(!q.isEmpty()){
            Vertex x = q.remove();
            if(!visited[x.getId()]){
                visited[x.getId()] = true;
            }

            for(Edge e : x.getEdges()){
                if(!(visited[e.getTo().getId()]) && e.getType().equals("copper")){
                    q.add(e.getTo());
                }
            }
        }

        for(int i=0; i<visited.length; i++){
            if(!visited[i]){
                return false;
            }
        }

        return true;

    }

    public void findShortestPath(int source, int dest){
        Dijkstra sp = new Dijkstra(this, source);
        Stack<Edge> path = sp.getPathTo(dest);
        int bandwidth = 0;
        Edge curr = path.peek();

        System.out.print("Path: ");
        while(!path.empty()){
            curr = path.pop();
            bandwidth += curr.getBandwidth();
            System.out.print(curr.getFrom().getId() + " -> ");
        }
        System.out.print(curr.getTo().getId());
        System.out.println("\nBandwidth along path: " + bandwidth + " mbps\n");

    }

    public void findMaxFlow(int source, int dest){
        FordFulkerson mf = new FordFulkerson(this, source, dest);
        int flow = mf.getMaxFlow();
        System.out.println("Maximum amount of data: "+ flow + " mb\n");
    }

    public boolean determineStability(){
        boolean stable = true;
        for(int i=0; i<size-1; i++){
            for(int j=i+1; j<size; j++){
                stable = removeAndCheck(i, j);
                if(!stable) break;
            }
            if(!stable) break;
        }

        return stable;
    }

    private boolean removeAndCheck(int v1, int v2){
        boolean[] visited = new boolean[size];
        visited[v1] = true;
        visited[v2] = true;

        Queue<Vertex> q = new LinkedList<>();

        if(v1 == 0 || v2 == 0){
            if(v1 == 1 || v2 == 1){
                q.add(vertices[2]);
            } else {
                q.add(vertices[1]);
            }
        } else {
            q.add(vertices[0]);
        }

        while(!q.isEmpty()){
            Vertex x = q.remove();
            if(!visited[x.getId()]){
                visited[x.getId()] = true;
            }

            for(Edge e : x.getEdges()){
                if(!(visited[e.getTo().getId()]) && (e.getTo().getId() != v1) && (e.getTo().getId() != v2)){
                    q.add(e.getTo());
                }
            }
        }


        boolean stable = true;
        for(int i=0; i<visited.length; i++){
            if(!visited[i]){
                stable = false;
                break;
            }
        }
        return stable;

    }
}
