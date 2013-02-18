import java.io.IOException;
import java.util.Scanner;


public class Main {

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		
		//Get the name of the file to run
		/*System.out.println("Enter the name of the file to run");
		Scanner s = new Scanner(System.in);
		String file = s.next();*/
		
		//Compile the file and set it to wait for virtual 
		//machine to connect
		//runShell(file);
		
		//Access the Virtual Machine
		vmAccess vma = new vmAccess();
		Data d = vmAccess.toGraph;
		
		//Print out data
		for(int i=0; i < d.edges.size(); i++) System.out.println(i + " " + d.edges.get(i));
		for(int j=0; j < d.vertices.size(); j++) System.out.println(j + " " + d.vertices.get(j).value + " " + d.vertices.get(j).type );
		
		//Draw graph

		
	}
	
	  //Function that opens up the specified file using terminal commands and
	  //lets this program connect to it with the virtual machine
	  public static void runShell(String file){
		  
		  String cmd1 = "javac -g " + file;
		  String run = file.substring(0, file.length() - 5);
		  String cmd2 = "java -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n " + run;
			  
			  try {
				    // Run command
				    Runtime.getRuntime().exec(cmd1).waitFor();
				    Runtime.getRuntime().exec(cmd2).waitFor();
				    
				} catch (Exception e) {
				    e.printStackTrace(System.err);
				}
			  
			  try {
				    Thread.sleep(100);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}

	  }
}
