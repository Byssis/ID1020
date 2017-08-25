import edu.princeton.cs.algs4.IndexMinPQ;
import se.kth.id1020.Edge;
import se.kth.id1020.Graph;
import se.kth.id1020.Vertex;

import java.util.Iterator;
import java.util.Stack;

/**
 * Created by Albin on 2016-10-06.
 */
public class SP {
    private class StepInfo{
        private double distance;
        private Edge prevVertex;
        public StepInfo(){
            distance = Double.POSITIVE_INFINITY;
            prevVertex = null;
        }

        public StepInfo(double d){
            distance = d;
            prevVertex = null;
        }
    }

    private Graph graph;
    private StepInfo[] steps;
    private IndexMinPQ<Double> pq;
    private boolean distanceOrJump;

    public SP(Graph g, String start, boolean distanceOrJump){
        this.graph = g;
        this.distanceOrJump = distanceOrJump;
        steps = new StepInfo[g.numberOfVertices()];
        pq = new IndexMinPQ<Double>(g.numberOfVertices());

        int startId = labelToKey(start);
        if(steps[startId] == null) steps[startId] = new StepInfo(0);
        pq.insert(startId, 0.0);
        while (!pq.isEmpty())
            relax(g, pq.delMin());
    }

    private int labelToKey(String label){
        for(Vertex vertex : graph.vertices()){
            if(vertex.label.equals(label))
                return vertex.id;
        }
        return -1;
    }

    private void relax(Graph g, int a) {
        for(Edge egde : g.adj(a)){
            int v = egde.to;
            if(steps[v] == null) steps[v] = new StepInfo();
            if(steps[v].distance > steps[a].distance + ((distanceOrJump)? egde.weight : 1)){      // Distance or jumps
                steps[v].distance = steps[a].distance + ((distanceOrJump)? egde.weight : 1);
                steps[v].prevVertex = egde;
                if(pq.contains(v)) pq.changeKey(v, steps[v].distance);
                else               pq.insert(v, steps[v].distance);
            }
        }
    }

    public double distanceTo(String end){
        return distanceTo(labelToKey(end));
    }

    public double distanceTo(int v){
        return steps[v].distance;
    }

    public Iterator<Vertex> path(String end){
        return new PathIterator<Vertex>(labelToKey(end));
    }

    private class PathIterator<Vettex> implements Iterator<Vertex>{
        StepInfo pointer;
        Stack<Vertex> stack;


        private PathIterator(int v){
            stack = new Stack<Vertex>();
            pointer = steps[v];
            stack.push(graph.vertex(v));
            while (pointer.prevVertex != null) {
                stack.push(graph.vertex(pointer.prevVertex.from));
                pointer = steps[pointer.prevVertex.from];
            }
        }

        public boolean hasNext() {
            return (stack.empty()) ? false :  true;
        }

        public Vertex next() {
            return stack.pop();
        }
    }
}
