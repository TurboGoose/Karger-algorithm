import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class GraphFactory {

    public static Graph generate(int numVertices, double probability) {
        Map<Integer, Vertex> vertices = new HashMap<>();
        List<Edge> edges = new ArrayList<>();
        Random random = new Random();

        // generate vertices
        for (int id = 0; id < numVertices; id++) {
            vertices.put(id, new Vertex(id));
        }

        // generate edges
        List<Vertex> vertexList = new ArrayList<>(vertices.values());
        int n = vertexList.size();
        for (int i = 0; i < n - 1; i++) {
            Vertex u = vertexList.get(i);
            for (int j = i + 1; j < n; j++) {
                Vertex v = vertexList.get(j);
                if (random.nextFloat() < probability) {
                    Edge edge = new Edge(u, v);
                    u.neighbours.add(edge);
                    v.neighbours.add(edge);
                    edges.add(edge);
                }
            }
        }
        return new Graph(vertices, edges);
    }

    public static Graph getFromFile(String inputFileName) throws FileNotFoundException {
        Hashtable<Integer, Vertex> vertices = new Hashtable<>();
        ArrayList<Edge> edges = new ArrayList<>();
        Scanner in = new Scanner(new File(inputFileName));

        //add all vertices
        while (in.hasNextLine()){
            Scanner line = new Scanner(in.nextLine());
            int id = line.nextInt();
            Vertex v = new Vertex(id);
            vertices.put(id, v);
        }
        in = new Scanner(new File(inputFileName));

        //add edges
        while (in.hasNextLine()){
            Scanner line = new Scanner(in.nextLine());
            int idU = line.nextInt();
            Vertex u = vertices.get(idU);
            while (line.hasNextInt()){
                int idV = line.nextInt();
                Vertex v = vertices.get(idV);
                if (u.id < v.id){
                    Edge e = new Edge(vertices.get(Math.min(idU, idV)), vertices.get(Math.max(idU, idV)));
                    vertices.get(idU).neighbours.add(e);
                    vertices.get(idV).neighbours.add(e);
                    edges.add(e);
                }
            }
        }
        return new Graph(vertices, edges);
    }
}
