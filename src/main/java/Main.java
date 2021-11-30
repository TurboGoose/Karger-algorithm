public class Main {

    public static void main(String[] args) {
        Graph graph = GraphGenerator.generate(30, 0.5);
        graph.printGraph();
        int minCut = findMinCut(graph);
        System.out.println("Min cut for graph: " + minCut);
    }

    public static int findMinCut(Graph graph) {
        int n = graph.vertices.size();
        int minCut = n;
        int iterations = (int) (Math.pow(n, 2) * Math.log(n));
        for (int i = 0; i < iterations; i++){
            Graph copyGraph = new Graph(graph);
            copyGraph.randomContract();
            minCut = Math.min(copyGraph.edges.size(), minCut);
        }
        return minCut;
    }
}
