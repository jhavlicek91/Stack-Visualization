import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.sun.jdi.*;


public class vmAccess {

   Data toGraph = new Data(); //Graph data to be drawn later
   ArrayList<Long> visited = new ArrayList<Long>(); //Used to tell if an object has been DFS'ed yet

  public vmAccess()
      throws IOException, InterruptedException, IncompatibleThreadStateException, ClassNotLoadedException {
		
    //Connect to virtual machine
    VirtualMachine vm = new vmAcquirer().connect(8005);
    vm.suspend();
    
    //Access virtual machine's stack and run DFS to 
    //acquire all objects and variables
    accessStack(vm);

    //resume the vm
    vm.resume();

  }

  /* Print the stack */
  private void accessStack(VirtualMachine vm) throws IncompatibleThreadStateException, ClassNotLoadedException  {
	 	    	
	    	 	List<ThreadReference> tr = vm.allThreads();
	    	 	Vertex currVertex;
	    	 
		    	//Get number of stack frames
		    	List<StackFrame> sf = tr.get(3).frames();
			    int fc = tr.get(3).frameCount();
			    			    
			    //Go through each stack frame and collect variables
				for(int j = 0; j < fc; j++){
					
				   try {
					   
					   //Set current vertex to the frame currently on
					   currVertex = toGraph.addVertex("Frame " + j, "Frame");
					   
					   List<LocalVariable> vv = sf.get(j).visibleVariables();
					  
					   //Go through each variable in the frame
					   for(int k = 0; k < vv.size(); k++){
						   
						  //Only create a vertex for a frame once
						  if(toGraph.getVertex("Frame " + j) == null) currVertex = toGraph.addVertex("Frame " + j, "Frame");
				          else currVertex = toGraph.getVertex("Frame " + j);
						  
						  //Gather data from the variable
						  LocalVariable ll = vv.get(k);
						  Value value = sf.get(j).getValue(ll);
						  Type type = ll.type();
						  String name = ll.name();
						  
						  //add variable from stack frame to the graph
						  addToGraph(type, name, value, currVertex);
					   }
				   }
				   
				    catch (AbsentInformationException e) {
						// TODO Auto-generated catch block
				    	System.out.println("Cannot trace Stack frame" + j);
				    	} 
				   		   
				}
				
				//Connect frames to each other through edges
				for(int m = 0; m < fc -1; m++) toGraph.addEdge("Frame", toGraph.getVertex("Frame " + m), toGraph.getVertex("Frame " + (m+1)));
					   			    
  }
  
  
  void addToGraph(Type type, String name,  Value value, Vertex from){
	  
	  Vertex nextVertex;
	  Vertex firstVertex = from;
	  String v;
      String displayValue;
	  
	  try {
	            
	      //What to do if the thing to add is a string
	      if ((value) instanceof StringReference) {
	    	  
	    	  v = value.toString();
	    	  
	    	  //add Connection
	    	  nextVertex = toGraph.addVertex(v , "String");
	    	  toGraph.addEdge(name, firstVertex, nextVertex);
	    	  
	      }
	      
	      //What to do if the thing to add is an array
	      else if ((value) instanceof ArrayReference) {
	    	  
	    	  v = value.toString();
	    	  
	    	  //These are the behind the scene items in the stack
	    	  if(name.equals("b") || name.equals("buffer") || name.equals("args")) {
	    		  
	    		  nextVertex = toGraph.addVertex(v.substring(12, v.length()), "Array Reference");
	    		  toGraph.addEdge(name, firstVertex, nextVertex);
	    	  }
	    	  
	    	  else {
	    		  
		    	  //add Initial Connection
		    	  nextVertex = toGraph.addVertex(v.substring(12, v.length()), "Array Reference");
		    	  toGraph.addEdge(name,from, nextVertex);
		    	  firstVertex = nextVertex;
		    	  
		    	  //Get array values and component type of array
		    	  
		    	  ArrayReference ar = (ArrayReference) value;
		    	  List<Value> arrayValues = ar.getValues();
		    	  ArrayType newType = (ArrayType) type;
		    	  Type ct = newType.componentType();
		    	  
		    	  //add Connections from array to each of its children
		    	  for(int z = 0; z < ar.length(); z++) addToGraph(ct, "From Array", arrayValues.get(z), firstVertex);
		    	  
	    	  }

	      }
	      
	     //What to do if the thing to add is an object
	      else if ((value) instanceof ObjectReference) {
	    	  
	    	  ObjectReference or = (ObjectReference) value;
	    	  
	    	  //Check if the reference is to a java. object
	    	  v = value.toString();
	    	  String vsub = v.substring(12, v.length());
	    	  
	    	  //What to do if the object reference is a java.lang object
	    	  if(vsub.length() >= 9 && vsub.substring(0,4).equals("java") ){
	    		  
	    		  handleJavaObjects(vsub, name, or, firstVertex);

	    	  }
	    	  
	    	  else {
	    		  
	    		  //Start depth first searching the object if it hasn't already been visited
		    	  if(!visited.contains( or.uniqueID() ) ) {
		    		  nextVertex = toGraph.addVertex(vsub, "Object Reference");
		    		  recursiveDepthFirstSearch(or, nextVertex);
		    	  }
		    	  else nextVertex = toGraph.getVertex(vsub);
		    	  
		    	  toGraph.addEdge(name, firstVertex, nextVertex);
		    	  
	    	  }
	      }
	      
	     //What to do if the thing to add is a primitive
	      else if(value instanceof PrimitiveValue){
	    	  
	    	  //add connection
	    	  nextVertex = toGraph.addVertex(value.toString(), "Primitive");
	    	  toGraph.addEdge(name, firstVertex, nextVertex);
	    	  
	      }
	      
	    //What to do if the thing to add is null
	      else {
	    	  
	    	  //add Connection
	    	  nextVertex = toGraph.addVertex("null", "null");
	    	  toGraph.addEdge(name, firstVertex, nextVertex);
	    	  
	      }
	  }
	  
	  catch (ClassNotLoadedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); }
	  
  }
  
   void recursiveDepthFirstSearch(ObjectReference or, Vertex curr) throws ClassNotLoadedException{
	  
	//Initialize variables
	String value;  
	String displayValue;
	Vertex next = null;
	String name;
	Type t;
	ObjectReference castObject;
	  
	//Set the object/vertex as visited
	visited.add( ((ObjectReference) or).uniqueID() );
	  
	ReferenceType rt = or.referenceType();
  	List<Field> fields = rt.fields();
  	
  	
  	//Go through all of the objects fields
  	for(int i = 0; i < fields.size(); i++){
  		
  		//Gather info from the field
		Field f = fields.get(i);
		Value fieldValue = or.getValue(f);
		name = f.name();
		t = f.type();
		
		
		if(fieldValue instanceof ObjectReference){
			if(!visited.contains( ((ObjectReference) fieldValue).uniqueID()) ) {
				
				value = fieldValue.toString();
				displayValue = value.substring(12, value.length());
				castObject = (ObjectReference) fieldValue;
				
				addToGraph(t, name, fieldValue, curr);
			}
			
			//If vertex exists already
			else {
				
				value = fieldValue.toString();
				displayValue = value.substring(12, value.length());
				
				next = toGraph.getVertex(displayValue);
				toGraph.addEdge(name, curr, next);	
			}
		}
		
		else{
			//If the field is not an object
			addToGraph(t, name, fieldValue, curr);
		}
		
  	}
	    
	  
  }
  
  void handleJavaObjects(String value, String name, ObjectReference or, Vertex currVertex) throws ClassNotLoadedException{
	  
	  Vertex nextVertex;
	  
	  //If that object is an Integer
	  if(value.length() >= 17 && value.substring(0,17).equals("java.lang.Integer") ) {
		 Field f =  or.referenceType().fieldByName("value") ;
		 nextVertex = toGraph.addVertex( or.getValue(f).toString(), "Primitive");
    	 toGraph.addEdge(name, currVertex, nextVertex);
	  }
	  
	  //If the object is an arraylist
	  else if(value.length() >= 19 && value.substring(0,19).equals("java.util.ArrayList") ) {
		  
		 nextVertex = toGraph.addVertex(value, "Object Reference");
    	 toGraph.addEdge(name, currVertex, nextVertex); 
		  
		 Field f = or.referenceType().fieldByName("elementData");
		 Value v = or.getValue(f);
		 Type t = f.type();
		 addToGraph(t, f.name(),  v, nextVertex);
		 
	  }
	  
	  else {
    	  nextVertex = toGraph.addVertex(value, "Object Reference");
    	  toGraph.addEdge(name, currVertex, nextVertex);
	  }
	  
	  
  }
  


}