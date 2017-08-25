import se.kth.id1020.Edge;
import se.kth.id1020.Graph;

/**
 * Created by Albin on 2016-10-05.
 */
public class Paths {
    private int[] subTreesArray;
    private Graph graph;

    public Paths(Graph graph) {
        this.graph = graph;
        subTreesArray = new int[this.graph.numberOfVertices()];
    }

    public int getNUmberOfSubTrees() {
        int numSubTrees = 0;
        for (int i = 0; i < graph.numberOfVertices(); i++)
            if (subTreesArray[i] == 0) dfs(i, ++numSubTrees);
        return numSubTrees;
    }

    private void dfs(int i, int subTree) {
        subTreesArray[i] = subTree;
        for(Edge edge : graph.adj(i)) {
            int next = edge.to;
            if (subTreesArray[next] == 0) dfs(next, subTree);
        }
    }
}
