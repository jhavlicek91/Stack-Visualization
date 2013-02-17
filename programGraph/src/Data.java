import java.util.HashMap;


public class Data {

	int vid;
	int eid;
	
	HashMap<Integer, vertexInfo> vertices = new HashMap<Integer, vertexInfo>(); //vertex id and relevant info
	HashMap<Integer, String> edges = new HashMap<Integer, String>(); //edge id and name
	HashMap<Integer, HashMap<Integer, Integer>> connections = new HashMap<Integer, HashMap<Integer, Integer>>(); //vertex and the edge id as well as the vertex id
	
	public Data(){
		this.vid = 0;
		this.eid = 0;
	}
	
	public int addVertex(String value, String type){
		vertices.put(vid, new vertexInfo(value, type));
		vid++;
		
		return (vid - 1);
	}
	
	public int addEdge(String value){
		edges.put(eid, value);
		eid++;
		
		return (eid - 1);
	}
	
	public void addConnection(int edge, int v1, int v2){
		//connections.add
	}
	
	
	
	
}
