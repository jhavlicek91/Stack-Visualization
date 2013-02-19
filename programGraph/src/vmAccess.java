import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import com.sun.jdi.*;


public class vmAccess {

public static ArrayList<LocalVariable> cs = new ArrayList<LocalVariable>();

   static Data toGraph = new Data();
   static Vertex currVertex;
   static Vertex nextVertex;
   static Edge currEdge;
   static String currEdgeName;

  public vmAccess()
      throws IOException, InterruptedException, IncompatibleThreadStateException, ClassNotLoadedException {
	  
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
  private static void accessStack(VirtualMachine vm) throws IncompatibleThreadStateException, ClassNotLoadedException  {
	 	    	
	    	 	List<ThreadReference> tr = vm.allThreads();
	
		    	//Get number of stack frames
		    	List<StackFrame> sf = tr.get(3).frames();
			    int fc = tr.get(3).frameCount();
			    System.out.println(fc);
			    
			    
			    //Go through each stack frame and collect variables
				for(int j = 0; j < fc; j++){
					
				   try {
					   List<LocalVariable> vv = sf.get(j).visibleVariables();
					  
					   //Go through each variable
					   for(int k = 0; k < vv.size(); k++){
						   
						  LocalVariable ll = vv.get(k);
						  Value value = sf.get(j).getValue(ll);
						  
						  //Gather data from the variable
						  Type type = ll.type();
						  String name = ll.name();
						  
						  currVertex = toGraph.addVertex("Main", "Main");
						  
						  addToGraph(type, name, value);
					   }
				   }
				   
				    catch (AbsentInformationException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
				    	System.out.println("Cannot trace Stack frame" + j);
				    	} 
				   		   
				}				
					   			    
  }
  
  
  static void addToGraph(Type type, String name,  Value value){
	  
	  try {
		   
		  //Display helpful information
	      //System.out.println(type + " : " + name + " : " + value );
	      
	      //Create connection between main and what is in the stack
	      currEdgeName = name;
	      
	      //If local variable in the stack is an object find it in the virtual machine
	      //Check if it has any fields. start dfs
	      
	      if ((value) instanceof StringReference) {
	    	  
	    	  //add Connection
	    	  nextVertex = toGraph.addVertex(value.toString(), "String");
	    	  currEdge = toGraph.addEdge(name, currVertex, nextVertex);
	    	  toGraph.addConnection(currEdge, currVertex, nextVertex);
	    	  
	      }
	      
	      else if ((value) instanceof ArrayReference) {
	    	  
	    	  if(name.equals("b") || name.equals("buffer") || name.equals("args")) {
	    		  nextVertex = toGraph.addVertex(value.toString(), "Array Reference");
	    		  currEdge = toGraph.addEdge(name, currVertex, nextVertex);
		    	  toGraph.addConnection(currEdge, currVertex, nextVertex);
	    	  }
	    	  
	    	  else {
	    		  
		    	  //add Connection
		    	  nextVertex = toGraph.addVertex(value.toString(), "Array Reference");
		    	  currEdge = toGraph.addEdge(name, currVertex, nextVertex);
		    	  toGraph.addConnection(currEdge, currVertex, nextVertex);
		    	  currVertex = nextVertex;
		    	  
		    	  //Get array values and component type of array
		    	  
		    	  ArrayReference ar = (ArrayReference) value;
		    	  List<Value> arrayValues = ar.getValues();
		    	  ArrayType newType = (ArrayType) type;
		    	  Type ct = newType.componentType();
		    	  
		    	  //add Connections from array to each of its children
		    	  for(int z = 0; z < ar.length(); z++){
		    		  nextVertex = toGraph.addVertex( arrayValues.get(z).toString(), ct.toString());
		    		  currEdge = toGraph.addEdge("From Array", currVertex, nextVertex);
		    		  toGraph.addConnection(currEdge, currVertex, nextVertex);
		    	  }
	    	  }

	      }
	      
	      
	      else if ((value) instanceof ObjectReference) {
	    	  
	    	  //add connection to object reference
	    	  nextVertex = toGraph.addVertex(value.toString(), "Object Reference");
	    	  currEdge = toGraph.addEdge(name, currVertex, nextVertex);
	    	  toGraph.addConnection(currEdge, currVertex, nextVertex);
	    	  currVertex = nextVertex;
	    	   
	    	  //How to get fields and values
	    	  ObjectReference or = (ObjectReference) value;
	    	  DFS(or);
	
	      }
	      
	      //If type is a primitive 
	      else if(value instanceof PrimitiveValue){
	    	  
	    	  nextVertex = toGraph.addVertex(value.toString(), "Primitive");
	    	  currEdge = toGraph.addEdge(name, currVertex, nextVertex);
	    	  toGraph.addConnection(currEdge, currVertex, nextVertex);
	    	  
	      }
	      
	      //If it is null
	      else {
	    	  //add Connection
	    	  nextVertex = toGraph.addVertex("null", "null");
	    	  currEdge = toGraph.addEdge(name, currVertex, nextVertex);
	    	  toGraph.addConnection(currEdge, currVertex, nextVertex);
	      }
	  }
	  
	  catch (ClassNotLoadedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); }
	  
  }
  
  static void DFS(ObjectReference or) throws ClassNotLoadedException{
		Stack<ObjectReference> s = new Stack<ObjectReference>();
		ObjectReference popped;
		s.push(or);
		
		ArrayList<Long> visited = new ArrayList<Long>();

		
		while(!s.isEmpty()){
			popped = s.pop();
			ReferenceType rt = popped.referenceType();
	    	List<Field> fields = rt.fields();
	    	
	    	for(int i = 0; i < fields.size(); i++){
	    		Field f = fields.get(i);
	    		Value fieldValue = popped.getValue(f);
	    		Type t = f.type();
	    		String name = f.name();
	    		

		    	if(fieldValue instanceof ObjectReference ) { 
		    			
		    		if(!visited.contains( ((ObjectReference) fieldValue).uniqueID()) ) {
		    			
			    		visited.add( ((ObjectReference) fieldValue).uniqueID() );
			    			
			    		//Add data to graph
			    		nextVertex = toGraph.addVertex(fieldValue.toString(), "Object Reference");
			    		currEdge = toGraph.addEdge(name, currVertex, nextVertex);
			    		toGraph.addConnection(currEdge, currVertex, nextVertex);
			    		currVertex = nextVertex;
			    			
			    		s.push( (ObjectReference) fieldValue);
		    		}
		    	}
		    		
		    	else {
		    		//Process data and add to graph
		    		addToGraph(t, name, fieldValue);
		    			
		    	}
	    		
	    	}

				
		}
		
  }
  


}