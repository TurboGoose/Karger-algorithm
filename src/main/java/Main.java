import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main implements Cloneable {
    public static int V_NUM = 20;
    public static double PROB = 0.5;
    public static int WARM_UP_RUNS = 4;
    public static int CALCULATION_RUNS = 100;

    public static void main(String[] args) throws IOException {
        warmUp(WARM_UP_RUNS);
//        writeResultsToFile("results.txt", runCalculationExperiment(CALCULATION_RUNS));
        runRandomCalculationExperiment();
    }

    public static void writeResultsToFile(String filename, List<Long> results) throws IOException {
        try (PrintWriter out = new PrintWriter(filename)) {
            out.printf("V_NUM = %d; PROB = %f; RUNS = %d%n", V_NUM, PROB, CALCULATION_RUNS);
            results.forEach(out::println);
        }
    }

    public static void runRandomCalculationExperiment() throws FileNotFoundException {
        final int NUM_GRAPHS = 1000;
        final int V_MIN = 10, V_MAX = 50;
        Random random = new Random();
        List<Integer> vertices = new ArrayList<>();
        List<Integer> edges = new ArrayList<>();
        List<Long> elapsed = new ArrayList<>();

        int runs = NUM_GRAPHS / (V_MAX - V_MIN);
        for (int verts = V_MIN; verts < V_MAX + 1; verts++) {
            for (int j = 0; j < runs; j++) {
                double prob = Math.min(Math.max(random.nextDouble(), 0.5), 0.8);
                System.out.printf("%d) V = %d, P = %f, ", verts * runs + j, verts, prob);
                Graph graph = GraphFactory.generate(verts, prob);
                long elapsedTime = findMinCut(graph);
                System.out.printf("E = %d, MS = %d%n", graph.edges.size(), elapsedTime);
                vertices.add(verts);
                edges.add(graph.edges.size());
                elapsed.add(elapsedTime);
            }
        }

        try (PrintWriter out = new PrintWriter("result.csv")) {
            out.println("V, E, elapsed");
            for (int i = 0; i < vertices.size(); i++) {
                out.printf("%d, %d, %d%n", vertices.get(i), edges.get(i), elapsed.get(i));
            }
        }
        System.out.println("Completed!");
    }

    public static List<Long> runCalculationExperiment(int runs) {
        List<Long> elapsedTimes = new ArrayList<>(runs);
        for (int i = 0; i < runs; i++) {
            System.out.print(i + ") ");
            Graph graph = GraphFactory.generate(V_NUM, PROB);
            elapsedTimes.add(findMinCut(graph));
        }
        return elapsedTimes;
    }

    public static void warmUp(int runs) {
        System.out.println("------------------- warm up -------------------");
        for (int i = 0; i < runs; i++) {
            findMinCut(GraphFactory.generate(V_NUM, PROB));
        }
        System.out.println("------------------- end of warm up -------------------");
    }

    public static long findMinCut(Graph graph) {
        int n = graph.vertices.size();
        int minCut = n;
        int iterations = (int) (Math.pow(n, 2) * Math.log(n));
        long totalTimeMillis = 0;

        for (int i = 0; i < iterations; i++){
            Graph copyGraph = new Graph(graph);

            // measuring time here
            long startTime = System.currentTimeMillis();
            copyGraph.randomContract();
            minCut = Math.min(copyGraph.edges.size(), minCut);
            long elapsedTime = System.currentTimeMillis() - startTime;
            totalTimeMillis += elapsedTime;
        }
        System.out.println(totalTimeMillis);
        return totalTimeMillis;
    }
}
