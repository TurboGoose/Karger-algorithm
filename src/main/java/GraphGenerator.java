import java.util.*;

public class GraphGenerator {

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
}
