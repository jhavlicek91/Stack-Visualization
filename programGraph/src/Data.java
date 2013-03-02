import java.util.ArrayList;

public class Data {

	int vid; //vertex ID number
	int eid; //Edge ID number
	
	public ArrayList<Vertex> vertices = new ArrayList<Vertex>(); //vertex id and relevant info
	public ArrayList<Edge> edges = new ArrayList<Edge>(); //edge id and name
	
	public Data(){
		this.vid = 0;
		this.eid = 0;
	}
	
	//Add a new vertex to the object and return it 
	public Vertex addVertex(String value, String type){
		vertices.add(new Vertex(vid, value, type));
		vid++;
		
		return vertices.get(vid - 1);
	}
	
	//Adds a new edge between two previously defined vertices
	public Edge addEdge(String name, Vertex source, Vertex destination){
		edges.add( new Edge(eid, name, source, destination ));
		eid++;
		
		return edges.get(eid - 1);
	}
	
	//Checks if a vertex has been created already, if it has 
	//return the vertex, else return a null value
	public Vertex getVertex(String value){
		
		for(int i = 0; i < vertices.size(); i++){
			if(vertices.get(i).value.equals(value)) return vertices.get(i);
		}
		
		return (null);
	}
	

	
	
	
	
}
