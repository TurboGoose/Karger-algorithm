import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            Graph g = GraphFactory.generate();
            System.out.println(g.findMinCut());
        } catch (FileNotFoundException | CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
