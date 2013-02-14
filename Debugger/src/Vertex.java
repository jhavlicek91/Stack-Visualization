public class Vertex {


	public int data;
	public String type; //Can be Main, function, object, variable
	public String name;
	

	public Vertex(int data, String type, String name){
		this.data = data;
		this.type = type;
		this.name = name;
	}

	public Vertex(String type, String name){
		this.type = type;
		this.name = name;
	}

}