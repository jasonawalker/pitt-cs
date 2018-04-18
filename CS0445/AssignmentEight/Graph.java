import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Queue;

public class Graph {

	private Map<String,List<Edge>> graphData = new HashMap<String,List<Edge>>();
	private boolean isDirected;
	private boolean isWeighted;
	
	public Graph(boolean isDirected, boolean isWeighted) {
	  this.isDirected = isDirected;
	  this.isWeighted = isWeighted;
	}
	
	// this method will add edges to the graph based on a string.
	// this string is in the form "from,to" 
	// each pair of adjacent nodes should be separated by a semicolon.
	public void addEdges(String edgeList) {
	  for (String edgePair : edgeList.split(";")) {
	    String[] edges = edgePair.split(",");
	    if (edges.length == 2) {
	      addEdge(edges[0], edges[1]);
	    }
	  }
	}
	
	// add an edge from one node to another; if this is a weighted graph this will throw an exception
	public void addEdge(String from, String to) {
	  if (isWeighted) {
	    throw new UnsupportedOperationException("Graph is weighted.");
	  }
	  addEdge(from, to, 1);
	}
	
	// add an edge from one node to another; 
	// this method will handle reversing the edge for undirected graphs
	public void addEdge(String from, String to, int weight) {
		List<Edge> connections = graphData.get(from);
		if (connections == null) {
			connections = new LinkedList<Edge>();
			graphData.put(from, connections);
		}
		connections.add(new Edge(to, weight));
		if (!isDirected) {
		  connections = graphData.get(to);
		  if (connections == null) {
		    connections = new LinkedList<Edge>();
		    graphData.put(to, connections);
		  }
		  connections.add(new Edge(from, weight));
		}
	}
	
	// determine if you can travel directly from "from" to "to"
	public boolean isAdjacent(String from, String to) {
	  List<Edge> edges = graphData.get(from);
	  if (edges != null) {
	    for (Edge edge : edges) {
	      if (edge.adjacentNode.equals(to)) {
	        return true;
	      }
	    }
	  }
	  return false;
	}
	
	// return the weight between "from" and "to"
	// if nodes are not adjacent, return -1
	public int getWeight(String from, String to) {
	  List<Edge> edges = graphData.get(from);
	  if (edges != null) {
	    for (Edge edge : edges) {
	      if (edge.adjacentNode.equals(to)) {
	        return edge.weight;
	      }
	    }
	  }
	  return -1;
	}
	
	// represent the weight and an adjacent node
	private class Edge {
	  private int weight = 1;
	  private String adjacentNode;
	  
	  public Edge(String adjacentNode, int weight) {
	    this.adjacentNode = adjacentNode;
	  }
	}
	
	public static void main(String[] args) {
	  String nodes = "A,B;B,C;C,D;D,E;A,K;K,E;A,F;F,G;G,H;H,I;I,J;J,E";
	  Graph graph = new Graph(false, false);
	  graph.addEdges(nodes);
	  System.out.println(graph.getShortestPath("A", "E")); // should print [A, K, E]
	}
	
	public List<String> getShortestPath(String from, String to) {
		Queue<String> q = new LinkedList<String>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(from, null);
		q.add(from);
		while(!q.isEmpty()) {
			String currentVertex = q.remove();
			if(currentVertex.equals(to)) {
				return calcPath(map,from, to, currentVertex);
			} else {
				List<Edge> currentEdges = graphData.get(currentVertex);
				for(int i = 0; i < currentEdges.size(); i++){
					String neighborVertex = currentEdges.get(i).adjacentNode;
					if(!map.containsKey(neighborVertex)) {
						q.add(neighborVertex);
						map.put(neighborVertex, currentVertex);
					}
				}
			}
		}
		return null;
	}
	
	public List<String> calcPath(HashMap<String, String> map, String from, String to, String currentVertex){
		LinkedList<String> path = new LinkedList<String>();
		path.add(currentVertex);
		while(!currentVertex.equals(from)) {
			currentVertex = map.get(currentVertex);
			path.add(0, currentVertex);
		}
		return path;
	}
}