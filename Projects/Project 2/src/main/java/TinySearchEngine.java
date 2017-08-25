import se.kth.id1020.TinySearchEngineBase;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Sentence;
import se.kth.id1020.util.Word;

import java.util.*;

/**
 * Created by Albin on 2016-10-07.
 */
public class TinySearchEngine implements TinySearchEngineBase {
    /*
        Node class
        Node for parse tree. Holds word or operator and cache key.
        First the parse tree has to be build completely and then update
        cache keys recursively.
      */
    private class Node {
        private Node left;
        private Node right;

        private String content;
        private String cacheKey;

        public Node() {
            this.left = null;
            this.right = null;
        }
    }

    /*
        DSR object, document search result object. Object that hold information about a certain document and is used
        when creating search result and the purpose of it is to have the ability to sort after relevance.
        Holds name of doc, number of that specific document, relevance.
     */
    private class DSR {
        private String name;
        private int count;
        private double relevance;
        private Integer popularity;
        private Document document;

        public DSR(Attributes attributes) {
            this.document = attributes.document;
            this.name = attributes.document.name;
            this.count = 1;
            this.popularity = attributes.document.popularity;
        }
        /*
         Copy a DSR object, used at merge to avoid destroying previous cache result,
         reason object linking
        */
        public DSR(DSR dsr) {
            this.document = dsr.document;
            this.name = dsr.document.name;
            this.count = dsr.count;

            this.popularity = dsr.document.popularity;
            this.relevance = dsr.relevance;
        }

        private void mergeAttributes(DSR dsr) {
            this.count += dsr.count;
            this.relevance += dsr.relevance;
        }

        private void setRelevance(int size) {
            double sizeD = documentWordCount.get(name);
            double tf = count / sizeD;
            double idf = Math.log10((double) documentWordCount.size() / (double) size);
            this.relevance = tf * idf;
        }

        public String toString() {
            return "Name: " + name + " Relevance: " + relevance + " Popularity: " + popularity;
        }
    }

    private HashMap<String, List<Attributes>> divider;
    private Map<String, Integer> documentWordCount;
    private Map<String, Map<String, DSR>> cache;
    private Node root;

    public TinySearchEngine() {
        divider = new HashMap<String, List<Attributes>>();
        documentWordCount = new HashMap<String, Integer>();
        cache = new HashMap<String, Map<String, DSR>>();
    }

    public void insert(Sentence sentence, Attributes attributes) {
        for (Word word : sentence.getWords()) {                                 // For every word in a sentence
            String key = word.word.toLowerCase();                               // lower case word as key for hashmap
            if (!divider.containsKey(key))
                divider.put(key, new ArrayList<Attributes>());                  // Checks if word entry already exists
            divider.get(key).add(attributes);                                   // Merge attributes
        }
        String key = attributes.document.name;                                  // Document name as key
        int count = sentence.getWords().size();
        if (documentWordCount.containsKey(key))                                 // Checks if key already exists
            documentWordCount.put(key, documentWordCount.get(key) + count);     // Update document count
        else
            documentWordCount.put(key, count);                                  // Create new entry
    }

    /*
        Search
        input s = input query

        1. Build parse tree from input query.
        2. Update parse tree with cache keys.
        3. Build a result list of DSR from the parse tree.
        4. Sort result list of DSR.
        5. Convert from list of DSR to list of document.
        6. return list of document.
     */
    public List<Document> search(String s) {
        // 1. Build parse tree
        String[] query = s.split(" orderby ");
        root = new Node();
        parseTree(root, new Scanner(query[0]));

        // 2. Build cacheKeys
        setCacheKeys(root);

        // 3. Build result
        List<DSR> result = buildResultList(root);

        // 4. Sort result
        // default: relevance asc
        boolean asc = true;
        boolean relevance = true;
        if (query.length > 1) {
            Scanner sc = new Scanner(query[1]);
            while (sc.hasNext()) {
                String next = sc.next();
                if (next.equals("dec"))
                    asc = false;
                if (next.equals("popularity"))
                    relevance = false;
            }
        }
        Collections.sort(result, new DSRcomparator(relevance, asc));            // Sort list

        // 5. Convert from list of DSR to list of
        List<Document> docList = new ArrayList<Document>();                     // Result list
        for (DSR dsr : result) {                                                // Foreach dsr
            docList.add(dsr.document);                                          // get all document
            System.out.println(dsr);
        }
        result = null;                                                          // For garbage collector

        // 6. return list of document
        return docList;                                                         // return result
    }

    /*
        Converts hashmap result from buildSubResult to list
     */
    private List<DSR> buildResultList(Node root) {
        return new ArrayList<DSR>(bulidSubResult(root).values());
    }

    /*
        1. Check if available in cache, return if exist.
        2. Check if content node is an "operation" or "word" and generate map.
           If "word" generate map from searchWord else call buildSubResult recursively
           and merge left and right node with appropriate merge method.
        3. Add to cache.
     */
    private Map<String, DSR> bulidSubResult(Node current) {
        // 1. Check if available in cache
        if (cache.containsKey(current.cacheKey))
            return cache.get(current.cacheKey);

        // 2. Check if content node is an operation or word
        Map<String, DSR> map;
        if (current.content.equals("+")) {                                      // Intersection
            map = mergeIntersection(bulidSubResult(current.left), bulidSubResult(current.right));
        } else if (current.content.equals("|")) {                               // Union
            map = mergeUnion(bulidSubResult(current.left), bulidSubResult(current.right));
        } else if (current.content.equals("-")) {                               // Difference
            map = mergeDifference(bulidSubResult(current.left), bulidSubResult(current.right));
        } else
            map = searchWord(current.content);

        // 3. Add to cache
        cache.put(current.cacheKey, map);
        return map;
    }

    /*
        Create cache keys for all nodes
        1. Start cache key with current content.
        2. If current is an operator append left and right child's cache key recursively.
        3. Set current cache key and return it.
     */
    private String setCacheKeys(Node current) {
        // 1. Start cache key with current content.
        StringBuilder sb = new StringBuilder();
        sb.append(current.content);
        sb.append(" ");

        // 2. If current is an operator append left and right child's cache key recursively.
        if (current.content.equals("+") || current.content.equals("|")) {
            String left = setCacheKeys(current.left);
            String right = setCacheKeys(current.right);
            if (left.compareTo(right) < 0) {
                sb.append(left);
                sb.append(" ");
                sb.append(right);
            } else {
                sb.append(right);
                sb.append(" ");
                sb.append(left);
            }
        } else if (current.content.equals("-")) {
            String left = setCacheKeys(current.left);
            String right = setCacheKeys(current.right);
            sb.append(left);
            sb.append(" ");
            sb.append(right);
        }

        // 3. Set current cache key and return it.
        current.cacheKey = sb.toString();
        return sb.toString();
    }

    /*
        Build parse tree
        1. Set current.
        2. If operator create left and right node.
     */
    private void parseTree(Node current, Scanner sc) {
        //  1. Set current
        current.content = sc.next();

        // 2. If operator create left and right node
        if (current.content.equals("+") || current.content.equals("|") || current.content.equals("-")) {
            current.left = new Node();
            current.right = new Node();
            parseTree(current.left, sc);
            parseTree(current.right, sc);
        }
    }

    /*
        Merge two maps with operation "intersection"
        Get everything that exist only in both map1 and map2.
        IMPORTANT! Create new map for every merge so merge do not interfere with previous sub queries
        or the cache. For the same reason create copies of DSR when merging.

        1. Go through each entry in map1, if it exist in map2. Create copy and add it to result map
     */
    private Map<String, DSR> mergeIntersection(Map<String, DSR> map1, Map<String, DSR> map2) {
        Map<String, DSR> result = new HashMap<String, DSR>();
        for (String key : map1.keySet()) {                                          // Foreach entry in map1
            if (map2.containsKey(key)) {                                            // If exist in both, copy and merge
                DSR merge = new DSR(map1.get(key));
                merge.mergeAttributes(map2.get(key));
                result.put(key, merge);
            }
        }
        return result;
    }

    /*
        Merge two maps with operation "union"
        Get everything that exist in both map1 and map2.
        IMPORTANT! Create new map for every merge so merge do not interfere with previous sub queries
        or the cache. For the same reason create copies of DSR when merging.
     */
    private Map<String, DSR> mergeUnion(Map<String, DSR> map1, Map<String, DSR> map2) {
        Map<String, DSR> result = new HashMap<String, DSR>();
        for (String key : map1.keySet()) {
            DSR merge = new DSR(map1.get(key));
            if (map2.containsKey(key))
                merge.mergeAttributes(map2.get(key));
            result.put(key, merge);
        }
        for (String key : map2.keySet()) {
            if (!map1.containsKey(key))
                result.put(key, new DSR(map2.get(key)));
        }
        return result;
    }

    /*
        Merge two maps with operation "Difference"
        Get everything that exist only in map1 and not map2.
        IMPORTANT! Create new map for every merge, so merge do not interfere with previous sub queries
        or the cache. For the same reason create copies of DSR when merging two DSR.
     */
    private Map<String, DSR> mergeDifference(Map<String, DSR> map1, Map<String, DSR> map2) {
        Map<String, DSR> result = new HashMap<String, DSR>();
        for (String key : map1.keySet()) {
            if (!map2.containsKey(key)) result.put(key, new DSR(map1.get(key)));
        }
        return result;
    }

    /*
        Retrieve attribute list from divider and process it into a hashmap of DSR
        1. Check if word exist in index
        2. Retrieve attribute list from index and remove duplicates and create hashmap of DSR
        3. Set relevance for each DSR
     */
    private Map<String, DSR> searchWord(String word) {
        Map<String, DSR> map = new HashMap<String, DSR>();
        // 1. Check if word exist in index
        if (!divider.containsKey(word)) return map;                                     // return empty map

        // 2. Retrieve attribute list from index and remove duplicates and create hashmap of DSR
        for (Attributes attributes : divider.get(word)) {                               //
            if (map.containsKey(attributes.document.name))
                map.get(attributes.document.name).count++;
            else {
                map.put(attributes.document.name, new DSR(attributes));
            }
        }

        // 3. Set relevance for each DSR
        for (String key : map.keySet()) {
            map.get(key).setRelevance(map.size());
        }
        return map;
    }

    /*
        1. Build parse tree
        2. Read parse tree
     */
    public String infix(String s) {
        // 1. Build parse tree
        Scanner sc = new Scanner(s);
        Node start = new Node();
        parseTree(start,sc);

        // 2. Read parse tree
        return (infix(start));
    }

    private String infix(Node node) {
        String current = node.content;
        if (current.equals("+"))
            return "( " + infix(node.left) + " + " + infix(node.right) + " )";
        else if (current.equals("|"))
            return "( " + infix(node.left) + " | " + infix(node.right) + " )";
        else if (current.equals("-"))
            return "( " + infix(node.left) + " - " + infix(node.right) + " )";
        else
            return current;
    }

    public void postInserts() {

    }

    public void preInserts() {

    }

    private class DSRcomparator implements Comparator<DSR> {
        private boolean relevance;
        private boolean asc;

        public DSRcomparator(boolean relevance, boolean asc) {
            this.relevance = relevance;
            this.asc = asc;
        }

        public int compare(DSR o1, DSR o2) {
            if (!relevance)
                return Integer.compare(o1.popularity, o2.popularity) * ((asc) ? 1 : -1);
            else
                return Double.compare(o1.relevance, o2.relevance) * ((asc) ? 1 : -1);
        }
    }
}
