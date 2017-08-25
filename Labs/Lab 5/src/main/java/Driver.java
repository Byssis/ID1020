import se.kth.id1020.DataSource;
import se.kth.id1020.Graph;
import se.kth.id1020.Vertex;

import java.util.Iterator;

/**
 * Created by Albin on 2016-10-07.
 */
public class Driver {

    public static void main(String[] args) {
        Graph g = DataSource.load();
        Paths path = new Paths(g);
        SP sp = new SP(g, "Renyn", true);
        SP spSteps = new SP(g, "Renyn", false);

        // work on g
        System.out.println("Number of edges: " + g.numberOfEdges());
        System.out.println("Number of vertices: " + g.numberOfVertices());
        System.out.println("Number of subtrees: " + path.getNUmberOfSubTrees());

        System.out.println();

        System.out.println("Steps between Renyn and Parses: " + spSteps.distanceTo("Parses"));
        Iterator<Vertex> iterSteps = spSteps.path("Parses");
        while (iterSteps.hasNext())
            System.out.println(iterSteps.next());

        System.out.println();

        System.out.println("Distance between Renyn and Parses: " + sp.distanceTo("Parses"));
        Iterator<Vertex> iter = sp.path("Parses");
        while (iter.hasNext())
            System.out.println(iter.next());
    }
}

