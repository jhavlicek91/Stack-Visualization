/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs;

/**
 *
 * @author nmarlo.student
 */
class Vertex {
    public int ID;
    public String type;
    public String Data;
    
    Vertex(int vertexID, String vertexType, String dataString){
        ID = vertexID;
        type = vertexType;
        Data = dataString;
    }
}
