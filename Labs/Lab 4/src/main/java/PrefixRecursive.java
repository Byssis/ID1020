import java.util.Iterator;
import java.util.Map;

/**
 * Created by Albin on 2016-09-30.
 */
public class PrefixRecursive {
    final int ARRAY_SIZE = 28;                                       

    private class Node implements Map.Entry<String, Integer> {
        Node parent;                                            // Pointer to parent
        Node[] childArray;                                       // Array of children
        int value, childValueSum, distinctChildCount;           // Values
        String word;                                            // Key
        int iterartorChildIndex;

        public Node(Node parent) {
            this.parent = parent;
            this.childArray = new Node[ARRAY_SIZE];
            this.value = 0;
            this.childValueSum = 0;
            this.distinctChildCount = 0;
            this.iterartorChildIndex = 0;
        }

        @Override
        public String getKey() {
            return word;
        }

        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public Integer setValue(Integer value) {
            this.value = value;
            return null;
        }

        public String toString(){
            return word + " " + value;
        }
    }

    private Node root;

    public PrefixRecursive() {
        root = new Node(null);
    }

    public void put(String key) {
        if (key == null || key.length() == 0) return;
        key = key.toLowerCase();
        put(root, key, 0);
    }

    private void put(Node currentNode, String key, int index) {
        // Base case
        if (index == key.length()) {
            if (currentNode.value == 0) updateDistinct(currentNode);
            currentNode.value++;
            currentNode.word = key;
            return;
        }
        currentNode.childValueSum++;
        int childIndex = keyToIndex(key, index);
        if (currentNode.childArray[childIndex] == null) currentNode.childArray[childIndex] = new Node(currentNode);
        put(currentNode.childArray[childIndex], key, index + 1);
    }

    private int keyToIndex(String key, int index) {
        char c = key.charAt(index);
        return (Character.isLetter(c)) ? c - 'a' : ARRAY_SIZE - 2;
    }

    public int get(String key) {
        key = key.toLowerCase();
        Node node = getNode(root, key, 0);
        return (node == null) ? 0 : node.value;
    }

    public int count() {
        return root.childValueSum;
    }

    public int count(String key) {
        key = key.toLowerCase();
        Node node = getNode(root, key, 0);
        return (node == null) ? 0 : node.childValueSum + node.value;
    }

    public int distinct() {
        return root.distinctChildCount;
    }

    public int distinct(String key) {
        key = key.toLowerCase();
        Node node = getNode(root, key, 0);
        return (node == null) ? 0 : node.distinctChildCount;
    }

    private void updateDistinct(Node currentNode) {
        if (currentNode == null) return;
        currentNode.distinctChildCount++;
        updateDistinct(currentNode.parent);
    }

    private Node getNode(Node currentNode, String key, int index) {
        if (index == key.length()) return currentNode;
        final int childIndex = keyToIndex(key, index);
        if (currentNode.childArray[childIndex] == null) return null;
        return getNode(currentNode.childArray[childIndex], key, index + 1);
    }

    public Iterator<Map.Entry<String, Integer>> iterator() {
        return new PrefixIterator();
    }

    private class PrefixIterator implements Iterator<Map.Entry<String, Integer>> {
        private Node pointer;                           // Pointer to current node
        private int count = 0;

        public PrefixIterator() {
            pointer = root;
            pointer.iterartorChildIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return (count < root.distinctChildCount) ? true : false;
        }

        @Override
        public Map.Entry<String, Integer> next() {
            while (pointer.childArray[pointer.iterartorChildIndex] == null) {
                if (pointer.iterartorChildIndex + 1 >= ARRAY_SIZE) {
                    pointer.iterartorChildIndex = 0;                                // Reset iterartorChildIndex
                    pointer = pointer.parent;                                       // Go up
                }
                if(pointer.iterartorChildIndex + 1 < ARRAY_SIZE)pointer.iterartorChildIndex++;                                      // Go left
            }
            pointer = pointer.childArray[pointer.iterartorChildIndex];
            while (pointer.value == 0) {
                if (pointer.childArray[pointer.iterartorChildIndex] != null) {
                    pointer = pointer.childArray[pointer.iterartorChildIndex];       // Go down
                    pointer.iterartorChildIndex = 0;                                 // Reset iterartorChildIndex
                } else
                    pointer.iterartorChildIndex++;
            }
            count++;
            return pointer;
        }
    }
}
