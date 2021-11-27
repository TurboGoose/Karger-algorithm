/**
 * @author QingxiaoDong
 *
 * This Graph object is based on adjacent lists of Edges and Vertices. Each edge
 * points to its endpoints, each vertex points to edges incident on it.
 * It has following methods: addEdge(), removeEdge(), randomContract(), printGraph()
 * and findMinCut() which is the main purpose of this program.
 *
 * randomContract() is a randomized contraction algorithm[due to Karger, early 90s]
 * for finding the minimum cut (i.e., the minimum-possible number of crossing edges).
 * It has following procedures:
 * 1) pick a remaining edge (u,v) uniformly at random
 * 2) merge (or contract ) u and v into a single vertex
 * 3) remove self-loops
 * 4) return cut represented by final 2 vertices.
 *
 * - Time complexity is O(n^2 log n) or O((n + m)*n). (Determined by the number of
 * repetitions in the findMinCut() method.
 * - Space complexity is O(m + n).
 * - n is the number of vertices.
 * - m is the number of edges.
 *
 * @InputFileDescription: The file contains the adjacency list representation
 * of a simple undirected graph. There are 200 vertices labeled 1 to 200. The
 * first column in the file represents the vertex label, and the particular row
 * (other entries except the first column) tells all the vertices that the
 * vertex is adjacent to. So for example, the 6th row looks like : "6	155	56
 * 52	120	......". This just means that the vertex with label 6 is adjacent to
 * (i.e., shares an edge with) the vertices with labels 155,56,52,120,......,etc
 *
 */

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

class Vertex{
    public int id;
    public ArrayList<Edge> neighbours;

    public Vertex(int id){
        this.id = id;
        neighbours = new ArrayList<>();
    }
}

class Edge{
    public Vertex u;
    public Vertex v;

    public Edge(Vertex u, Vertex v){
        this.u = u;
        this.v = v;
    }

    public boolean isSame(Edge e){
        return e.u == this.u && e.v == this.v;
    }

    public Vertex getAnother(Vertex u){
        return u == this.u ? v : this.u;
    }
}

public class Graph implements Cloneable {

    private final Hashtable<Integer, Vertex> vertices;
    private final ArrayList<Edge> edges;

    public Graph(Hashtable<Integer, Vertex> vertices, ArrayList<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    public void addEdge(Vertex u, Vertex v, int count){
        int idU = u.id;
        int idV = v.id;
        Edge e = new Edge(vertices.get(Math.min(idU, idV)), vertices.get(Math.max(idU, idV)));
        for (int i = 0; i < count; i++){
            vertices.get(idU).neighbours.add(e);
            vertices.get(idV).neighbours.add(e);
            edges.add(e);
        }
    }

    public int removeEdge(Vertex u, Vertex v){
        int count = 0;
        int idU = u.id;
        int idV = v.id;
        Edge e = new Edge(vertices.get(Math.min(idU, idV)), vertices.get(Math.max(idU, idV)));
        for (int i = 0; i < u.neighbours.size(); i++){
            if (u.neighbours.get(i).isSame(e)){
                u.neighbours.remove(i);
                i--;
            }
        }
        for (int i = 0; i < v.neighbours.size(); i++){
            if (v.neighbours.get(i).isSame(e)){
                v.neighbours.remove(i);
                i--;
            }
        }
        for (int i = 0; i < edges.size(); i++){
            if (edges.get(i).isSame(e)){
                edges.remove(i);
                i--;
                count++;
            }
        }
        return count;
    }

    public void randomContract(){
        Random generator = new Random();
        while (vertices.size() > 2){
            int index = generator.nextInt(edges.size());
            Edge toRemove = edges.get(index);
            int idV = toRemove.v.id;
            Vertex u = toRemove.u;
            Vertex v = toRemove.v;
            removeEdge(u, v);
            while (v.neighbours.size() > 0){
                Vertex w = v.neighbours.get(0).getAnother(v);
                addEdge(u, w, removeEdge(v, w));
            }
            vertices.remove(idV);
        }
    }

    public int findMinCut() throws CloneNotSupportedException {
        int n = vertices.size();
        int minCut = n;
        int nRepeat = n; // (int) (Math.pow(n, 2) * Math.log(n));
        for (int i = 0; i < nRepeat; i++){
            Graph g = (Graph) this.clone();
            g.randomContract();
            int crossingEdges = g.edges.size();
            if (crossingEdges < minCut){
                minCut = crossingEdges;
            }
        }
        return minCut;
    }

    public void printGraph(){
        System.out.println("vertices:");
        Enumeration<Integer> enumKey = vertices.keys();
        while (enumKey.hasMoreElements()){
            Integer id = enumKey.nextElement();
            System.out.print(id + ": ");
            for (Edge e : vertices.get(id).neighbours){
                System.out.print(e.u.id + "-" + e.v.id + " ");
            }
            System.out.println();
        }
        System.out.println("edges:");
        for (Edge e : edges){
            System.out.print(e.u.id + "-" + e.v.id + " ");
        }
        System.out.println();
    }
}