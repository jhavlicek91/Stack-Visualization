import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.sun.jdi.*;


public class vmAccess {

   static Data toGraph = new Data();
   static ArrayList<Long> visited = new ArrayList<Long>();

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
	    	 	Vertex currVertex;
	
		    	//Get number of stack frames
		    	List<StackFrame> sf = tr.get(3).frames();
			    int fc = tr.get(3).frameCount();
			    			    
			    //Go through each stack frame and collect variables
				for(int j = 0; j < fc; j++){
					
				//Set current vertex to the frame currently on
				currVertex = toGraph.addVertex("Frame " + j, "Frame");
					
				   try {
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
  
  
  static void addToGraph(Type type, String name,  Value value, Vertex from){
	  
	  Vertex nextVertex;
	  Vertex firstVertex = from;
	  String v;
      String displayValue;
	  
	  try {
	            
	      //What to do if the thing to add is a string
	      if ((value) instanceof StringReference) {
	    	  
	    	  v = value.toString();
	    	  
	    	  //add Connection
	    	  nextVertex = toGraph.addVertex(v.substring(12, v.length()), "String");
	    	  toGraph.addEdge(name, firstVertex, nextVertex);
	    	  
	      }
	      
	      //What to do if the thing to add is an array
	      else if ((value) instanceof ArrayReference) {
	    	  
	    	  v = value.toString();
	    	  
	    	  //These are the behidn the scene items in the stack
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
	    	  
	    	  nextVertex = toGraph.addVertex(vsub, "Object Reference");
	    	  
	    	  
	    	  //What to do if the object reference is a java.lang object
	    	  if(vsub.length() >= 9 && vsub.substring(0,9).equals("java.lang")){
	    		  
	    		  //If that object is an Integer
	    		  if(vsub.length() >= 17 && vsub.substring(0,17).equals("java.lang.Integer") ) {
	    			 Field f =  or.referenceType().fieldByName("value") ;
	    			 nextVertex = toGraph.addVertex( or.getValue(f).toString(), "Primitive");
			    	 toGraph.addEdge(name, firstVertex, nextVertex);
	    		  }
	    	  }
	    	  
	    	  else {
	    		  
	    		  //Start depth first searching the object
	    		  if(!visited.contains( or.uniqueID() ) )  DFS2(or, nextVertex);
		    	  nextVertex = toGraph.getVertex(vsub);
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
  
  /*static void DFS(ObjectReference or, String n) throws ClassNotLoadedException{
		Stack<objectName> s = new Stack<objectName>();
		objectName popped;
		String name = n;
		Type t;
		String value;
		s.push(new objectName(or, n ));
		boolean jl; 
		boolean hasFields;
		
		while(!s.isEmpty()){
			popped = s.pop();
			ReferenceType rt = popped.or.referenceType();
	    	List<Field> fields = rt.fields();

	    	value = popped.or.toString();
    		String displayValue = value.substring(12, value.length());
    		
    		//If vertex exists already 
    		if(toGraph.getVertex(displayValue) == null) nextVertex = toGraph.addVertex(displayValue, "Object Reference");
    		else nextVertex = toGraph.getVertex(displayValue); 
    		
    		currEdge = toGraph.addEdge(popped.name, currVertex, nextVertex);
    		currVertex = nextVertex;
	    	
	    	
    		if(!visited.contains( ((ObjectReference) popped.or).uniqueID()) ) {
    			visited.add( ((ObjectReference) popped.or).uniqueID() );	
    		
		    	for(int i = 0; i < fields.size(); i++){
		    		Field f = fields.get(i);
		    		Value fieldValue = popped.or.getValue(f);
		    		t = f.type();
		    		name = f.name();
			    	
			    	if(fieldValue instanceof ObjectReference) { 	
			    		
			    		//check if the reference is a java. ADD To IF STATEMENT
			    		value = fieldValue.toString();
				    	String vsub = value.substring(12, value.length());
				    	
				    	//Does obeject have fields
				    	hasFields = (( (ObjectReference) fieldValue).referenceType().allFields().size() != 0 );
				    	
				    	if(vsub.length() >= 9 && vsub.substring(0,9).equals("java.lang")) jl = true;
				    	else jl = false;
				    	
				    	if(!jl && hasFields)  s.push( new objectName ((ObjectReference) fieldValue, name ) );
				    	else addToGraph(t, name, fieldValue);

			    	}
			    		
			    	else {
			    		
			    		//Process data and add to graph
			    		addToGraph(t, name, fieldValue);
			    			
			    	}
		    		
		    	}
		    	
		    	
    		}

				
		}
		
  }*/
  
  static void DFS2(ObjectReference or, Vertex curr) throws ClassNotLoadedException{
	  
	//Initialize variables
	String value;  
	String displayValue;
	Vertex next = null;
	String name;
	Type t;
	boolean jl;
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
				
				//Check if the object is a java.lang one
				if(displayValue.length() >= 9 && displayValue.substring(0,9).equals("java.lang")) jl = true;
		    	else jl = false;
				
				if(!jl){
					next = toGraph.addVertex(displayValue, "Object Reference");
					DFS2((ObjectReference) fieldValue, next);
				}
				else {
					
					//If the object is an integer class
					if(displayValue.length() >= 17 && displayValue.substring(0,17).equals("java.lang.Integer") ) {
		    			 f =  castObject.referenceType().fieldByName("value") ;
		    			 next = toGraph.addVertex( castObject.getValue(f).toString(), "Primitive");
		    		  }
					
				}
				
				//add edge to graph
				toGraph.addEdge(name, curr, next);
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
  


}