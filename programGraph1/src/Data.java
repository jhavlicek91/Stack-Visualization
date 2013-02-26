import java.util.ArrayList;

public class Data {

	int vid;
	int eid;
	
	ArrayList<Vertex> vertices = new ArrayList<Vertex>(); //vertex id and relevant info
	ArrayList<Edge> edges = new ArrayList<Edge>(); //edge id and name
	
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
	
	
	public Vertex getVertex(String value){
		
		
		for(int i = 0; i < vertices.size(); i++){
			if(vertices.get(i).value.equals(value)) return vertices.get(i);
		}
		
		return (null);
	}
	

	
	
	
	
}
