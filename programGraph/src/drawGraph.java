import java.awt.BorderLayout;
import javax.swing.JFrame;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import java.util.ArrayList;
import java.util.Hashtable;

public class drawGraph {

    public drawGraph(Data d) {
        JFrame f = new JFrame();
        f.setSize(500, 500);
        f.setLocation(300, 200);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          
        int xLoc = 20;
        int yLoc = 20;
                      
        Object temp;
        String style = "";
        Vertex v;
        int i;
                
                
        Hashtable<Integer, Object> findVert = new Hashtable<Integer, Object>();
        
        final mxGraph graph = new mxGraph();
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        f.getContentPane().add(BorderLayout.CENTER, graphComponent);
        f.setVisible(true);

        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        
        try {
            for(i = 0; i < d.vertices.size(); i++){
            	
                v = d.vertices.get(i);
                if(v.type.equals("Array Reference"))       style = "fontColor=black;fillColor=#74A0EF";
                else if(v.type.equals("Object Reference")) style = "fontColor=black;fillColor=#FF8330";
                else if(v.type.equals("Primitive"))        style = "fontColor=black;fillColor=#BCE640";
                else                                       style = "fontColor=black;fillColor=#FF6262";
 
                temp = graph.insertVertex(parent, null, d.vertices.get(i).value, xLoc, yLoc, 80, 30, style);
                findVert.put(d.vertices.get(i).id, temp);
                yLoc += 50;
                style = "";
                
              }
            
              for(i = 0; i < d.edges.size(); i++){
                  graph.insertEdge(parent, null, d.edges.get(i).name, findVert.get(d.edges.get(i).source), findVert.get(d.edges.get(i).destination));
               }

        } finally {
            graph.getModel().endUpdate();
        }

        // define layout
        mxFastOrganicLayout layout = new mxFastOrganicLayout(graph);
        layout.setForceConstant(150);
        
        // layout using morphing
        graph.getModel().beginUpdate();
        try {
            layout.execute(graph.getDefaultParent());
        } 
        finally {
            mxMorphing morph = new mxMorphing(graphComponent, 20, 1.2, 20);

            morph.addListener(mxEvent.DONE, new mxIEventListener() {

                @Override
                public void invoke(Object arg0, mxEventObject arg1) {
                    graph.getModel().endUpdate();
                    // fitViewport();
                 }

            });

            morph.startAnimation();
        }

    }

    public static void main(String[] args) {
        drawGraph t = new drawGraph();

    }
}