import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;

//How to compile java -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n  Test


public class FieldMonitor {

public static ArrayList<LocalVariable> cs = new ArrayList<LocalVariable>();

  public static void main(String[] args)
      throws IOException, InterruptedException {
	  
	//Get the name of the file to run
	/*System.out.println("Enter the name of the file to run");
	Scanner s = new Scanner(System.in);
	String file = s.next();*/
	
	//Compile the file and set it to wait for virtual 
	//machine to connect
	//runShell(file);
	  
    //connect to virtual machine
    VirtualMachine vm = new VMAcquirer().connect(8000);
    vm.suspend();
    
    
    printStack(vm);

    //resume the vm
    vm.resume();

  }

  /* Print the stack */
  private static void printStack(VirtualMachine vm) {
	  List<ThreadReference> tr = vm.allThreads();
	    
	    try {
		    	System.out.println("thread"+3);
		    	//Get number of stack frames
		    	List<StackFrame> sf = tr.get(3).frames();
			    int fc = tr.get(3).frameCount();
			    
			    
			    //Go through each stack frame and collect variables
				for(int j = 1; j < fc; j++){
				   List<LocalVariable> vv = sf.get(j).visibleVariables();
				   //Go through each variable
				   for(int k = 0; k < vv.size(); k++){
					   
					   //Display helpful information
				      System.out.println(vv.get(k).type() + " : " + vv.get(k).name() + " : " + sf.get(j).getValue(vv.get(k)) + " : " + vv.get(k).type().signature());
				      
				      //If local variable in the stack is an object find it in the virtual machine
				      //Check if it has any fields. start dfs
				      
				      if (sf.get(j).getValue(vv.get(k)) instanceof ObjectReference) {
				    	    
				    	  //How to get fields and values
				    	  Value v = sf.get(j).getValue(vv.get(k));
				    	  ObjectReference or = (ObjectReference) v;
				    	  
				    	  DFS(or);
		
				      }
				      
				   }
				}
					   			
		} 
	    
	    catch (IncompatibleThreadStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); } 
	    catch (AbsentInformationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); }
	    catch (ClassNotLoadedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); }
  }
  
  
  //Function that opens up the specified file using terminal commands and
  //lets this program connect to it with the virtual machine
  public static void runShell(String file){
	  
	  String cmd2 = "javac -g "  + file;
	  String run = file.substring(0, file.length() - 5);
	  String cmd1 = "java -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n  " + run;
		  
		  try {
			    // Run command
			    Process process = Runtime.getRuntime().exec(cmd1);
			} catch (Exception e) {
			    e.printStackTrace(System.err);
			}

  }
  
  static void DFS(ObjectReference or){
		Stack<ObjectReference> s = new Stack<ObjectReference>();
		//ArrayList<Vertex> send = new ArrayList<Vertex>();
		ObjectReference popped;
		
		s.push(or);
		
		while(!s.isEmpty()){
			popped = s.pop();
			ReferenceType rt = popped.referenceType();
	    	List<Field> fields = rt.allFields();
	    	
	    	for(int i = 0; i < fields.size(); i++){
	    		if(fields.get(i) instanceof ObjectReference) {
	    			s.push((ObjectReference) fields.get(i));
	    			System.out.println(fields.get(i) + " is an object");
	    		}
	    		else System.out.println(fields.get(i) + " is a variable");
	    	}
				
		}
		
  }
  


}