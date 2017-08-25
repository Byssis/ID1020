import java.util.Iterator;
import java.util.Map;

/**
 * Created by Albin on 2016-09-27.
 */
public class PrefixTree {
    private class Node{
        private String key;
        private int value;

        private Node left, right, parent;
        private int sum, size;

        public Node(String key, int value, int size ){
            this.key = key;
            this.value = value;
            this.sum = 1;
            this.size = size;
        }
    }

    private Node root;

    public void put(String key){
        if(key == null) return;
        root = put(root, key, 1);
    }

    private Node put(Node node, String key, int value){
        if(node == null) return new Node(key, value, 0);
        int compare = node.key.compareTo(key);
        if(compare > 0) node.left = put(node.left, key, value);
        else if(compare < 0) node.right = put(node.right, key, value);
        else node.value = value;
        node.sum = value + count(node.right) + count(node.left);
        node.size = 1 + size(node.right) + size(node.left);
        node.left.parent = node;
        node.right.parent = node;
        return node;
    }

    private int size(Node node) {
        if(node == null) return 0;
        return node.size;
    }

    public int get(String key){
        return get(root, key);
    }

    private int get(Node node, String key){
        if(key == null) return 0;
        int compare = node.key.compareTo(key);
        if(compare > 0) return get(node.left, key);
        else if(compare < 0) return get(node.right, key);
        else return node.value;
    }

    public int count(String key){
        return count(getNode(root, key));
    }

    private Node getNode(Node parent, String key){
        if(parent == null) return null;
        int compare = parent.key.compareTo(key);
        if(compare > 0) return getNode(parent.left, key);
        else if(compare < 0) return getNode(parent.right, key);
        return parent;
    }

    private int count(Node node){
        if(node == null) return 0;
        else return node.sum;
    }

    public int distinct(String key){
        return size(getNode(root,key));
    }

    public Iterator<Map.Entry<java.lang.String, Integer>> iterator(){
        return new PrefixIterator();
    }

    public class PrefixIterator implements Iterator<Map.Entry<java.lang.String, Integer>>{
        public boolean hasNext() {
            return false;
        }

        public Map.Entry<String, Integer> next() {
            return null;
        }

        public void remove() {

        }
    }
}
