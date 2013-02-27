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
		
		String file = text.getText();
		int fileLength = file.length();
		
		if(fileLength > 5 && file.substring(fileLength - 5 , fileLength).equals(".java")) {
			runShell(file);
			
			//Access the Virtual Machine
			try {
				new vmAccess();
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
			Data d = vmAccess.toGraph;
			
			//call function for visualizing the info in data object
			new drawGraph(d);
		}
		
		else {
			text.setText("Enter the name of a .java file");
		}
	}
	
	//Function that opens up the specified file using terminal commands and
	//lets this program connect to it with the virtual machine
	public static void runShell(String file){
		  
		  String cmd1 = "javac -g " + file;
		  String run = file.substring(0, file.length() - 5);
		  System.out.println(run+"g");
		  String cmd2 = "java -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=y " + run;
			  
			  try{
				    // Run commands
				    Process p1 = Runtime.getRuntime().exec(cmd1);
				    int exitCode = p1.waitFor();
				    System.out.println(exitCode);
				    Process p2 = Runtime.getRuntime().exec(cmd2);
				    exitCode = p2.waitFor();
				    System.out.println(exitCode);
				 
				    Thread.sleep(15);
				    
				} catch (Exception e) {
				    e.printStackTrace(System.err); }

	  }

}
