import se.kth.id1020.TinySearchEngineBase;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Albin on 2016-09-22.
 */
public class TinySearchEngineNew implements TinySearchEngineBase {
    private List<List<Item>> divider;

    public TinySearchEngineNew(){
        divider = new ArrayList<List<Item>>();

    }

    public void insert(Word inputWord, Attributes inputAttributes) {
        int index = getDividerIndex(inputWord.word.charAt(0));
        if(divider.get(index) == null) divider.add(index, new ArrayList<Item>());

        List<Item> items = divider.get(index);



    }

    public List<Document> search(String s) {
        return null;
    }

    private int getDividerIndex(char s){
        int indexUpper = s - 'A';
        int indexLower = s - 'a';
        if(indexUpper >= 0 && indexUpper < ('Z' - 'A'))
            return indexUpper;
        else if (indexLower >= 0 && indexLower < ('z' - 'a')){
            return indexLower;
        }
        else
            return 'Z' - 'A' + 1;
    }
}
