
public class vertexInfo {

	String name;
	String type;
	
	public vertexInfo(String name, String type){
		this.name = name;
		this.type = type;
		
	}
	
	
	public boolean isEqual(vertexInfo vi){
		return ( this.name.equals(vi.name) && this.type.equals(vi.type) );
	}
}
