import java.util.*;

class Vertex {
    public int id;
    public List<Edge> neighbours;

    public Vertex(int id){
        this.id = id;
        neighbours = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return id == vertex.id && Objects.equals(neighbours, vertex.neighbours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, neighbours);
    }
}

class Edge {
    public Vertex u;
    public Vertex v;

    public Edge(Vertex u, Vertex v){
        this.u = u;
        this.v = v;
    }

    public Vertex getAnother(Vertex u){
        return u == this.u ? v : this.u;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(u, edge.u) && Objects.equals(v, edge.v);
    }

    @Override
    public int hashCode() {
        return Objects.hash(u, v);
    }
}

public class Graph {
    public final Map<Integer, Vertex> vertices;
    public final List<Edge> edges;

    public Graph(Map<Integer, Vertex> vertices, List<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    public Graph(Graph graph) {
        vertices = new HashMap<>();
        for (Vertex v : graph.vertices.values()) {
            vertices.put(v.id, new Vertex(v.id));
        }

        edges = new ArrayList<>();
        for (Edge e : graph.edges) {
            addEdge(e.u, e.v, 1);
        }
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
            if (u.neighbours.get(i).equals(e)){
                u.neighbours.remove(i);
                i--;
            }
        }
        for (int i = 0; i < v.neighbours.size(); i++){
            if (v.neighbours.get(i).equals(e)){
                v.neighbours.remove(i);
                i--;
            }
        }
        for (int i = 0; i < edges.size(); i++){
            if (edges.get(i).equals(e)){
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

    public void printGraph(){
        Hashtable<Integer, Vertex> vertices = new Hashtable<>(this.vertices);
        System.out.println("vertices:");
        Enumeration<Integer> enumKey = vertices.keys();
        while (enumKey.hasMoreElements()){
            Integer id = enumKey.nextElement();
            System.out.print(id + ": ");
            for (Edge e : vertices.get(id).neighbours){
                System.out.print(e.u.id + "-" + e.v.id + ", ");
            }
            System.out.println();
        }
        System.out.println("edges:");
        for (Edge e : edges){
            System.out.print(e.u.id + "-" + e.v.id + ",z ");
        }
        System.out.println();
    }
}