import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import com.sun.jdi.*;


public class vmAccess {

public static ArrayList<LocalVariable> cs = new ArrayList<LocalVariable>();

   static Data toGraph = new Data();
   static int currVertex = 0;
   static int nextVertex;
   static int fromMain = 0;
   static int currEdge;

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
	  toGraph.addVertex("Main", "Main");
	  
	    try {
		    	//Get number of stack frames
		    	List<StackFrame> sf = tr.get(3).frames();
			    int fc = tr.get(3).frameCount();
			    
			    
			    //Go through each stack frame and collect variables
				for(int j = 1; j < fc; j++){
					
				   List<LocalVariable> vv = sf.get(j).visibleVariables();
				   
				   //Go through each variable
				   for(int k = 0; k < vv.size(); k++){
					   
					  LocalVariable ll = vv.get(k);
					  Value value = sf.get(j).getValue(ll);
					  
					  //Gather data from the variable
					  Type type = ll.type();
					  String name = ll.name();
					  
					  addToGraph(type, name, value);
					  
				      
				      fromMain++;
				   }
				}
				
				System.out.println(fromMain);
					   			
		} 
	    
	    catch (IncompatibleThreadStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); } 
	    catch (AbsentInformationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); } 
	    catch (ClassNotLoadedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

  }
  
  
  static void addToGraph(Type type, String name,  Value value){
	  
	  try {
		   
		  //Display helpful information
	      //System.out.println(type + " : " + name + " : " + value );
	      
	      //Create connection between main and what is in the stack
	      currEdge = toGraph.addEdge(name);
	      
	      //If local variable in the stack is an object find it in the virtual machine
	      //Check if it has any fields. start dfs
	      
	      if ((value) instanceof StringReference) {
	    	  
	    	  //add Connection
	    	  nextVertex = toGraph.addVertex(value.toString(), "String");
	    	  toGraph.addConnection(currEdge, currVertex, nextVertex);
	    	  
	      }
	      
	      else if ((value) instanceof ArrayReference) {
	    	  
	    	  if(name.equals("b") || name.equals("buffer") || name.equals("args")) {
	    		  nextVertex = toGraph.addVertex(value.toString(), "Array Reference");
		    	  toGraph.addConnection(currEdge, currVertex, nextVertex);
	    	  }
	    	  
	    	  else {
	    		  
		    	  //add Connection
		    	  nextVertex = toGraph.addVertex(value.toString(), "Array Reference");
		    	  toGraph.addConnection(currEdge, currVertex, nextVertex);
		    	  currVertex = nextVertex;
		    	  
		    	  //Get array values and component type of array
		    	  
		    	  ArrayReference ar = (ArrayReference) value;
		    	  List<Value> arrayValues = ar.getValues();
		    	  ArrayType newType = (ArrayType) type;
		    	  Type ct = newType.componentType();
		    	  
		    	  //add Connections from array to each of its children
		    	  for(int z = 0; z < ar.length(); z++){
		    		  currEdge = toGraph.addEdge("From Array");
		    		  nextVertex = toGraph.addVertex( arrayValues.get(z).toString(), ct.toString());
		    		  toGraph.addConnection(currEdge, currVertex, nextVertex);
		    	  }
	    	  }

	      }
	      
	      
	      else if ((value) instanceof ObjectReference) {
	    	  
	    	  //add connection to object reference
	    	  nextVertex = toGraph.addVertex(value.toString(), "Object Reference");
	    	  toGraph.addConnection(currEdge, currVertex, nextVertex);
	    	  currVertex = nextVertex;
	    	   
	    	  //How to get fields and values
	    	  ObjectReference or = (ObjectReference) value;
	    	  DFS(or, currVertex);
	
	      }
	      
	      //If type is a primitive 
	      else if(value instanceof PrimitiveValue){
	    	  
	    	  nextVertex = toGraph.addVertex(value.toString(), type.toString());
	    	  toGraph.addConnection(currEdge, currVertex, nextVertex);
	    	  
	      }
	      
	      //If it is null
	      else {
	    	  //add Connection
	    	  nextVertex = toGraph.addVertex("null", "null");
	    	  toGraph.addConnection(currEdge, currVertex, nextVertex);
	      }
	  }
	  
	  catch (ClassNotLoadedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); }
	  
  }
  
  static void DFS(ObjectReference or, int currVertex) throws ClassNotLoadedException{
		Stack<ObjectReference> s = new Stack<ObjectReference>();
		ObjectReference popped;
		s.push(or);
		
		while(!s.isEmpty()){
			popped = s.pop();
			ReferenceType rt = popped.referenceType();
	    	List<Field> fields = rt.fields();
	    	
	    	for(int i = 0; i < fields.size(); i++){
	    		Field f = fields.get(i);
	    		Value fieldValue = popped.getValue(f);
	    		Type t = f.type();
	    		String name = f.name();
	    		

	    		//Need to make sure that it is not a java.lang class
	    		if(fieldValue instanceof ObjectReference) {
	    			
	    			//Add data to graph
	    			currEdge = toGraph.addEdge(name);
	    			nextVertex = toGraph.addVertex(fieldValue.toString(), "Object Reference");
	    			toGraph.addConnection(currEdge, currVertex, nextVertex);
	    			currVertex = nextVertex;
	    			
	    			s.push( (ObjectReference) fieldValue);
	    			//System.out.println(f + " is an object");
	    		}
	    		
	    		else {
	    			//Process data and add to graph
	    			addToGraph(t, name, fieldValue);
	    			//System.out.println(f + " is a variable with value " + fieldValue);
	    		}
	    	}
				
		}
		
  }
  


}