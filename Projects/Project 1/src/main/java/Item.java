import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Word;

import java.util.ArrayList;

/**
 * Created by Albin on 2016-09-20.
 */
public class Item {
    Word word;
    ArrayList<Attributes> attributes;


    public Item(Word word, Attributes attributes) {
        this.word = word;
        this.attributes = new ArrayList<Attributes>();

        this.attributes.add(attributes);

    }

    public void mergeAttr(Attributes attributes) {
        this.attributes.add(attributes);
    }

    public int compareTo(Item item) {
        return this.word.word.compareTo(item.word.word);
    }

    public int compareTo(String item) {
        return this.word.word.compareTo(item);
    }

    private int binarySearch(String s, ArrayList<Attributes> attributes, int low, int high) {

        return 0;
    }

}

