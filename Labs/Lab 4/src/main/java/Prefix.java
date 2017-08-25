import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Albin on 2016-09-29.
 */
public class Prefix {
    final int NODE_ARRAY_SIZE = 26;
    private class Node implements Map.Entry<String, Integer> {
        char c;
        Node[] children;
        Node parent;
        boolean end;
        int size;
        int distinct;
        int count;
        int index;

        public  Node(char c){
            this.c = c;
            children = new Node[NODE_ARRAY_SIZE];
            size = 0;
            distinct = 0;
        }
        public  Node(){
            children = new Node[NODE_ARRAY_SIZE];
            size = 0;
            distinct = 0;
        }

        public void resetIndex(){
            index = 0;
        }

        public String getKey() {
            return "" + c;
        }

        public Integer getValue() {
            return size;
        }

        public Integer setValue(Integer value) {
            return null;
        }
    }

    private Node root;
    private int count;

    public Prefix(){
        root = new Node();
        count = 0;
    }

    public void put(String s){
        s = s.toLowerCase();
        Node pointer = root;
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            int index = getIndex(c);
            if(pointer.children[index] == null) {
                pointer.children[index] = new Node(c);
                pointer.children[index].parent = (i > 0) ? pointer : null;
                count++;
            }
            pointer = pointer.children[index];
        }
        pointer.end = true;
        if(pointer.size == 0 &&  pointer.parent != null){
            //pointer.parent.distinct++;
            updateDistinct(pointer);
        }
        pointer.size++;
        updateCount(pointer.parent);
    }

    private void updateDistinct(Node pointer) {
        Node parent = pointer.parent;
        // Base case
        if (parent == null)
            return;
        parent.distinct++;
        updateDistinct(parent);
    }

    private void updateCount(Node pointer) {
        if(pointer == null) return;
        pointer.count++;
        updateCount(pointer.parent);
    }

    private int getIndex(char c){
        return c - 'a';
    }

    public int get(String s){
        Node pointer = getNode(s);
        if(pointer == null) return 0;
        return (pointer.end)? pointer.size : 0;
    }

    public int count(String s){
        Node pointer = getNode(s);
        return (pointer == null)? 0 : pointer.count;
    }

    public int distinct(String s){
        Node pointer = getNode(s);
        return (pointer == null)? 0 : pointer.distinct;
    }

    private Node getNode(String s){
        s = s.toLowerCase();
        Node pointer = root;
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            int index = getIndex(c);
            if(pointer.children[index] == null) return null;
            pointer = pointer.children[index];
        }
        return pointer;
    }

    public int size(){
        return root.count;
    }

    public int sizeDistinct(){
        return root.distinct;
    }

    /*public Iterator iterrator(){
        return new PrefixIterator();
    }*/

  /*  private class PrefixIterator implements Iterator<Map.Entry<String, Integer>>{
        Node pointer, headPointer;
        int index, iterate;
        public PrefixIterator(){
            index = 0;
            iterate = 0;
        }

        public boolean hasNext(){
            return (pointer == null) ? false : true;
        }

        public Map.Entry<String, Integer> next() {
            StringBuilder sb = new StringBuilder();
            pointer = root.children[index];
            while(!pointer.children[iterate].end){
                sb.append(pointer.c);
            }



        }
    }*/
}
