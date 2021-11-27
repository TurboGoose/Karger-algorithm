import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class GraphFactory {
    public static Graph generate() throws FileNotFoundException {
        return getFromFile("graph.txt"); // add random generation here
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
