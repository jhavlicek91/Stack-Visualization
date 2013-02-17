
public class vertexInfo {

	String value;
	String type;
	
	public vertexInfo(String name, String type){
		this.value = name;
		this.type = type;
		
	}
	
	
	public boolean isEqual(vertexInfo vi){
		return ( this.value.equals(vi.value) && this.type.equals(vi.type) );
	}
}
