/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs;

/**
 *
 * @author nmarlo.student
 */
class Edge {
    public int Start;
    public int End;
    public String Label;
    
    Edge(int startVert, int endVert, String edgeLabel){
        Start = startVert;
        End = endVert;
        Label = edgeLabel;
    }
}
