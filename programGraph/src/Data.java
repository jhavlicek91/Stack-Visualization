import java.util.ArrayList;
import java.util.HashMap;


public class Data {

	int vid;
	int eid;
	
	ArrayList<Vertex> vertices = new ArrayList<Vertex>(); //vertex id and relevant info
	ArrayList<Edge> edges = new ArrayList<Edge>(); //edge id and name
	HashMap<Vertex, HashMap<Vertex, Edge>> connections = new HashMap<Vertex, HashMap<Vertex, Edge>>(); //vertex and the edge id as well as the vertex id
	
	public Data(){
		this.vid = 0;
		this.eid = 0;
	}
	
	public Vertex addVertex(String value, String type){
		vertices.add(new Vertex(vid, value, type));
		vid++;
		
		return vertices.get(vid - 1);
	}
	
	public Edge addEdge(String name, Vertex source, Vertex destination){
		edges.add( new Edge(eid, name, source, destination ));
		eid++;
		
		return edges.get(eid - 1);
	}
	
	public void addConnection(Edge edge, Vertex v1, Vertex v2){
		
		//Check if vertex already exists in the map
		
		if( connections.containsKey(v1) ) {
			connections.get(v1).put(v2, edge);
		}
		
		else {
			connections.put(v1, new HashMap<Vertex, Edge>());
			connections.get(v1).put(v2, edge);
			
		}
	}
	

	
	
	
	
}
