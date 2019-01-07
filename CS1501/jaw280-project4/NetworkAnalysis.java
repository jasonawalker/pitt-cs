import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.ArrayList;

public class NetworkAnalysis {

    private FordFulkerson ff;
    private Dijkstra d;
    private static Scanner sc = new Scanner(System.in);

    private static Graph readGraph(String t) throws FileNotFoundException {
        Scanner reader = new Scanner(new File(t));
        reader.useDelimiter("\\s+|\\n");
        int size = Integer.parseInt(reader.next());
        Vertex[] g = new Vertex[size];
        ArrayList<Edge> edges = new ArrayList<>();

        for(int i = 0; i<size; i++){
            g[i] = new Vertex(new LinkedList<>(), i);
        }

        int from, to;
        String type;
        int bandwidth, length;

        while(reader.hasNext()){
            from = Integer.parseInt(reader.next());
            to = Integer.parseInt(reader.next());
            type = reader.next();
            bandwidth = Integer.parseInt(reader.next());
            length = Integer.parseInt(reader.next());

            Edge edge1 = new Edge(g[from], g[to], type, bandwidth, length);
            Edge edge2 = new Edge(g[to], g[from], type, bandwidth, length);

            g[from].getEdges().add(edge1);
            g[to].getEdges().add(edge2);

            edges.add(edge1);
            edges.add(edge2);
        }

        reader.close();
        System.out.println("Successfully loaded graph from " + t + "\n");
        return new Graph(g, size, edges);
    }

    public static void startDriver(Graph g){
        int opt = 0;
        while (opt != 5){
            System.out.println("Select an option: \n" +
                    "1) Find lowest latency path between two points\n" +
                    "2) Determine if graph is connected via copper cables only\n" +
                    "3) Find maximum amount of data transmittable between two points\n" +
                    "4) Determine if graph stays connected if any 2 vertices fail\n" +
                    "5) Quit\n");
            opt = sc.nextInt();

            switch (opt){
                case 1:
                    shortestPath(g);
                    break;
                case 2:
                    copperConnected(g);
                    break;
                case 3:
                    maxFlow(g);
                    break;
                case 4:
                    determineStability(g);
                    break;
                case 5:
                    System.out.println("Goodbye");
                    break;
                default:
                    System.out.println("Not an option");
                    break;
            }
        }

    }

    private static void determineStability(Graph g){
        boolean s = g.getStability();
        if(s){
            System.out.println("This graph will remain connected if any 2 vertices fail\n");
        } else {
            System.out.println("This graph will NOT remain connected if any 2 vertices fail\n");
        }
    }

    private static void maxFlow(Graph g){
        System.out.println("Enter source vertex: ");
        int v1 = sc.nextInt();
        System.out.println("Enter target vertex: ");
        int v2 = sc.nextInt();

        g.findMaxFlow(v1, v2);
    }

    private static void copperConnected(Graph g){
        boolean c = g.getCopperConnected();
        if(c){
            System.out.println("This graph is copper-connected\n");
        } else {
            System.out.println("This graph is NOT copper-connected\n");
        }

    }

    private static void shortestPath(Graph g){
        System.out.println("Enter source vertex: ");
        int v1 = sc.nextInt();
        System.out.println("Enter target vertex: ");
        int v2 = sc.nextInt();

        g.findShortestPath(v1, v2);
    }

    private static void debugger(Graph g){
//        System.out.println(g.toString());
//        g.findShortestPath(4,1);
//        System.out.println("CC: "+ g.getCopperConnected());
//        g.findMaxFlow(0,4);
//        System.out.println(g.getStability());

    }

    public static void main(String[] args){
        Graph g;

        if(args.length != 1){
            throw new IllegalArgumentException("illegal argument");
        }

        try{
            g = readGraph(args[0]);
            startDriver(g);
//            debugger(g);
        } catch(FileNotFoundException e){
            System.out.println("file not found");
        }
    }
}
