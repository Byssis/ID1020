import se.kth.id1020.TinySearchEngineBase;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Word;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Albin on 2016-09-22.
 */
public class TinySearchEngineBTS implements TinySearchEngineBase {
    BinarySearchTree bts = new BinarySearchTree();
    int count = 0;
    public void insert(Word word, Attributes attributes) {
        bts.add(word.word, attributes);
        count++;
    }

    public List<Document> search(String s) {
        System.out.println(count);
        List<Attributes> list = bts.get(s);
        List<Document> documents = new ArrayList<Document>();

        for(int i = 0; i < list.size(); i++){
            documents.add(list.get(i).document);
        }

        return documents;
    }
}
