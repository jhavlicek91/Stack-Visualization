import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JTextField;
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.IncompatibleThreadStateException;


public class RunProgram implements ActionListener {
	public JTextField text;
	
	public RunProgram(JTextField field) {
		this.text = field;	
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		//Get the text in the field when the button is clicked
		String file = text.getText();
		String program;
		int fileLength = file.length();
		
		//Only runs the program if the file in the textbox ends in .java
		if(fileLength > 5 && file.substring(fileLength - 5 , fileLength).equals(".java")) {
			
			//Start running the program in the virtual machine
			program = runShell(file);
			
			//Access the Virtual Machine
			try {
				
				//Gather data from the program in the virtual machine
				vmAccess vma = new vmAccess();
				Data d = vma.toGraph;
				
				//call function for visualizing the info in data object
				new drawGraph(d, program);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IncompatibleThreadStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotLoadedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		else {
			//if the file does not end in .java
			text.setText("Enter the name of a .java file");
		}
	}
	
	//Function that opens up the specified file using terminal commands and
	//lets this program connect to it with the virtual machine
	public static String runShell(String file){
		  
		  String cmd1 = "javac -g " + file;
		  String run = file.substring(0, file.length() - 5);
		  String cmd2 = "java -Xdebug -Xrunjdwp:transport=dt_socket,address=8005,server=y,suspend=n " + run;
			  
			  try{
				    // Run commands
				   Runtime.getRuntime().exec(cmd1).waitFor();
				   Runtime.getRuntime().exec(cmd2);
				 
				   Thread.sleep(2000);
				    
				} catch (Exception e) {
				    e.printStackTrace(System.err); }
			  
			  return run;

	  }

}
