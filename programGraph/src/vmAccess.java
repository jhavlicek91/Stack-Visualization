import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;


public class vmAccess {

public static ArrayList<LocalVariable> cs = new ArrayList<LocalVariable>();

  public vmAccess()
      throws IOException, InterruptedException {
	  
    //Connect to virtual machine
    VirtualMachine vm = new vmAcquirer().connect(8000);
    vm.suspend();
    
    //Access virtual machine's stack and run DFS to 
    //acquire all objects and variables
    accessStack(vm);

    //resume the vm
    vm.resume();

  }

  /* Print the stack */
  private static void accessStack(VirtualMachine vm) {
	  List<ThreadReference> tr = vm.allThreads();
	  
	  //create new data repository
	  Data d = new Data();
	    
	    try {
		    	//Get number of stack frames
		    	List<StackFrame> sf = tr.get(3).frames();
			    int fc = tr.get(3).frameCount();
			    
			    
			    //Go through each stack frame and collect variables
				for(int j = 1; j < fc; j++){
					
				   List<LocalVariable> vv = sf.get(j).visibleVariables();
				   
				   //Go through each variable
				   for(int k = 0; k < vv.size(); k++){
					   
					   //Gather data from the stack
					   Type type = vv.get(k).type();
					   String name = vv.get(k).name();
					   Value value = sf.get(j).getValue(vv.get(k));
					   String sig = vv.get(k).type().signature();
					   
					   
					  //Display helpful information
				      System.out.println(type + " : " + name + " : " + value + " : " + sig);
				      
				      //If local variable in the stack is an object find it in the virtual machine
				      //Check if it has any fields. start dfs
				      
				      if ((value) instanceof ArrayReference) System.out.println(vv.get(k).name() + " is an array");
				      else if ((value) instanceof StringReference) System.out.println(vv.get(k).name() + " is a String"); 
				      else if ((value) instanceof ObjectReference) {
				    	  
				    	    //data.addVertex(new vertexInfo())
				    	  //How to get fields and values
				    	  ObjectReference or = (ObjectReference) value;
				    	  DFS(or);
		
				      }
				      
				      else if(value.equals(null)){}
				      else {
				    	  System.out.println(vv.get(k).name() + " is a " + type + " with value " + value);
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
  
  
  static void DFS(ObjectReference or){
		Stack<ObjectReference> s = new Stack<ObjectReference>();
		//ArrayList<Vertex> send = new ArrayList<Vertex>();
		ObjectReference popped;
		s.push(or);
		
		while(!s.isEmpty()){
			popped = s.pop();
			ReferenceType rt = popped.referenceType();
	    	List<Field> fields = rt.fields();
	    	
	    	for(int i = 0; i < fields.size(); i++){
	    		Field f = fields.get(i);
	    		Value fieldValue = popped.getValue(f);
	    		
	    		//Need to make sure that it is not a java.lang class
	    		if(fieldValue instanceof ObjectReference) {
	    			s.push( (ObjectReference) fieldValue);
	    			System.out.println(f + " is an object");
	    		}
	    		else System.out.println(f + " is a variable with value " + fieldValue);
	    	}
				
		}
		
  }
  


}