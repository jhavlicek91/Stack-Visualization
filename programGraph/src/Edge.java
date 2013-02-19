
public class Edge {

	Vertex source;
	Vertex destination;
	String name;
	int id;
	
	public Edge(int id, String name, Vertex source, Vertex destination){
		this.name = name;
		this.source = source;
		this.destination = destination;
		this.id = id;
	}
	
}
