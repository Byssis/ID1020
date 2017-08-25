import se.kth.id1020.TinySearchEngineBase;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Albin on 2016-09-20.
 */
public class TinySearchEngineProbing implements TinySearchEngineBase {
    LinearProbing<String, Item> list;

    public TinySearchEngineProbing() {
        list = new LinearProbing();
    }

    public void insert(Word word, Attributes attributes) {
        String key = word.word;
        if (list.contains(word.word)) {
            list.get(key).mergeAttr(attributes);
        }

        Item item = new Item(word, attributes);
        list.put(word.word, item);
    }

    public List<Document> search(String s) {

        Item item = list.get(s);
        if (item == null) return null;
        ArrayList<Document> documents = new ArrayList<Document>();
        for (int i = 0; i < item.attributes.size(); i++) {
            documents.add(item.attributes.get(i).document);
        }
        return documents;
    }


    public class LinearProbing<Key, Value> {
        private static final int INIT_CAPACITY = 4;

        private int n;           // number of key-value pairs in the symbol table
        private int m;           // size of linear probing table
        private Key[] keys;      // the keys
        private Value[] vals;    // the values

        public LinearProbing() {
            this(INIT_CAPACITY);
        }

        public LinearProbing(int capacity) {
            m = capacity;
            n = 0;
            keys = (Key[]) new Object[m];
            vals = (Value[]) new Object[m];
        }

        public int size() {
            return n;
        }

        private int hash(Key key) {
            return (key.hashCode() & 0x7fffffff) % m;
        }

        private void resize(int capacity) {
            LinearProbing<Key, Value> temp = new LinearProbing<Key, Value>(capacity);
            for (int i = 0; i < m; i++) {
                if (keys[i] != null) {
                    temp.put(keys[i], vals[i]);
                }
            }
            keys = temp.keys;
            vals = temp.vals;
            m = temp.m;
        }

        public void put(Key key, Value val) {
            if (key == null) throw new NullPointerException("first argument to put() is null");

            if (val == null) {
                delete(key);
                return;
            }

            // double table size if 50% full
            if (n >= m / 2) resize(2 * m);

            int i;
            for (i = hash(key); keys[i] != null; i = (i + 1) % m) {
                if (keys[i].equals(key)) {
                    vals[i] = val;
                    return;
                }
            }
            keys[i] = key;
            vals[i] = val;
            n++;
        }

        public Value get(Key key) {
            if (key == null) throw new NullPointerException("argument to get() is null");
            for (int i = hash(key); keys[i] != null; i = (i + 1) % m)
                if (keys[i].equals(key))
                    return vals[i];
            return null;
        }

        public void delete(Key key) {
            if (key == null) throw new NullPointerException("argument to delete() is null");
            if (!contains(key)) return;

            // find position i of key
            int i = hash(key);
            while (!key.equals(keys[i])) {
                i = (i + 1) % m;
            }

            // delete key and associated value
            keys[i] = null;
            vals[i] = null;

            // rehash all keys in same cluster
            i = (i + 1) % m;
            while (keys[i] != null) {
                // delete keys[i] an vals[i] and reinsert
                Key keyToRehash = keys[i];
                Value valToRehash = vals[i];
                keys[i] = null;
                vals[i] = null;
                n--;
                put(keyToRehash, valToRehash);
                i = (i + 1) % m;
            }

            n--;

            // halves size of array if it's 12.5% full or less
            if (n > 0 && n <= m / 8) resize(m / 2);

        }

        public boolean contains(Key key) {
            if (key == null) throw new NullPointerException("argument to contains() is null");
            return get(key) != null;
        }

    }


}
