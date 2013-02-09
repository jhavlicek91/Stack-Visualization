
public class Vertex {
	

	public int data;
	public String type; //Can be Main, function, object, variable
	
	public Vertex(int data, String type){
		this.data = data;
		this.type = type;
	}
	
	public Vertex(String type){
		this.type = type;
	}
	
}
