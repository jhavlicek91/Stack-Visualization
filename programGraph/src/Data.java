import java.util.HashMap;


public class Data {

	int vid;
	int eid;
	
	HashMap<Integer, vertexInfo> vertexes = new HashMap<Integer, vertexInfo>();
	HashMap<Integer, String> edges = new HashMap<Integer, String>();
	HashMap<Integer, HashMap<Integer, Integer>> connections = new HashMap<Integer, HashMap<Integer, Integer>>();
	
	public Data(){
		this.vid = 0;
		this.eid = 0;
	}
	
	public void createVertex(vertexInfo vi){
		vertexes.put(vid, vi);
		vid++;
	}
	
	public void createEdge(String name){
		edges.put(eid, name);
		eid++;
	}
	
	public void addConnection(int edge, int v1, int v2){
		//connections.add
	}
	
	
	
	
}
