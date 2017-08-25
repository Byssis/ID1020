import se.kth.id1020.util.Attributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Albin on 2016-09-23.
 */
public class BinarySearchTree {
    private Node root;

    private class Node{
        private String key;
        private ArrayList<Attributes> value;

        private Node left, right;
        private int size;

        public Node(String key,Attributes value){
            this.value = new ArrayList<Attributes>();
            this.key = key;
            this.value.add(value);
        }
    }

    public BinarySearchTree(){

    }

    public void add(String key, Attributes value){
        if(key == null || value == null) return;
        root = add(root, key, value);
    }

    private Node add(Node head, String key, Attributes value){
        if(head == null) return new Node(key, value);
        int compare = head.key.compareTo(key);
        if(compare > 0) head.left = add(head.left, key, value);
        else if(compare < 0) head.right = add(head.right, key, value);
        else mergeAttributes(head, value);
        head.size = 1 + size(head.right) + size(head.left);
        return head;
    }

    private void mergeAttributes(Node head, Attributes value) {
        head.value.add(value);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if(node == null) return 0;
        return node.size;
    }

    public List<Attributes> get(String key){
         return get(root, key);
    }

    private List<Attributes> get(Node head, String key){
        if(key == null) return null;
        int compare = head.key.compareTo(key);
        if(compare > 0) return get(head.left, key);
        else if(compare < 0) return get(head.right, key);
        else return head.value;
    }

}
