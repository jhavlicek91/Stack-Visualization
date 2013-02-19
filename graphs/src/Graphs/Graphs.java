package Graphs;

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

public class Graphs {

    public Graphs() {
        JFrame f = new JFrame();
        f.setSize(500, 500);
        f.setLocation(300, 200);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        int i;
                
                int xLoc = 20;
                int yLoc = 20;
                
            
		ArrayList<Vertex> verts = new ArrayList<Vertex>();
                ArrayList<Edge> edges = new ArrayList<Edge>();
                
                Object temp;
                String style = "";
                Vertex v;
                
                
                Hashtable<Integer, Object> findVert = new Hashtable<Integer, Object>();
                
                verts.add(new Vertex(0, "Array Reference", "9"));   
                verts.add(new Vertex(1, "Object Reference", "hello"));
                verts.add(new Vertex(2, "String", "world"));
                verts.add(new Vertex(3, "Primitive", "30"));
                
                edges.add(new Edge(verts.get(3).ID, verts.get(1).ID, "no"));
                edges.add(new Edge(verts.get(3).ID, verts.get(0).ID, "maybe"));
                edges.add(new Edge(verts.get(0).ID, verts.get(1).ID, "world"));
                edges.add(new Edge(verts.get(2).ID, verts.get(0).ID, "yes"));
                edges.add(new Edge(verts.get(1).ID, verts.get(2).ID, "test"));
        
        final mxGraph graph = new mxGraph();
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        f.getContentPane().add(BorderLayout.CENTER, graphComponent);
        f.setVisible(true);

        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try {
            for(i = 0; i < verts.size(); i++){
                        v = verts.get(i);
                        if(v.type.equals("Array Reference")){
                            style = "fontColor=black;fillColor=#74A0EF";
                        }else if(v.type.equals("Object Reference")){
                            style = "fontColor=black;fillColor=#FF8330";
                        }else if(v.type.equals("Primitive")){
                            style = "fontColor=black;fillColor=#BCE640";
                        }else{
                            style = "fontColor=black;fillColor=#FF6262";
                        }
                        temp = graph.insertVertex(parent, null, verts.get(i).Data, xLoc, yLoc, 80, 30, style);
                        findVert.put(verts.get(i).ID, temp);
                        yLoc += 50;
                        style = "";
                    }
                    for(i = 0; i < edges.size(); i++){
                        graph.insertEdge(parent, null, edges.get(i).Label, findVert.get(edges.get(i).Start), findVert.get(edges.get(i).End));
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
        } finally {
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
        Graphs t = new Graphs();

    }
}