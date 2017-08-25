import se.kth.id1020.TinySearchEngineBase;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Albin on 2016-09-19.
 */
public class TinySearchEngine implements TinySearchEngineBase {
    private final int INIT_CAP = 4;
    private int[] sizeOfDividerSubArrays;
    private Item[][] divider;

    public TinySearchEngine() {
        divider = new Item[27][];
        sizeOfDividerSubArrays = new int[27];
    }

    public void insert(Word inputWord, Attributes attributes) {
        int dividerIndex = getDividerIndex(inputWord.word);                                         // Get divider index

        if (divider[dividerIndex] == null) {                                                        // Check if array is not initialized
            divider[dividerIndex] = new Item[INIT_CAP];
        }
        Item[] items = divider[dividerIndex];                                                       // Get relevant item array

        int searchHit = search(inputWord.word, items, 0, sizeOfDividerSubArrays[dividerIndex]);     // Check if word already exists
        if (searchHit >= 0) {
            items[searchHit].mergeAttr(attributes);                                                 // Merge attributes with already existing entry
            return;
        }

        items = (Item[]) checkResize(items, sizeOfDividerSubArrays[dividerIndex]);                  // checkResize array if needed
        items[sizeOfDividerSubArrays[dividerIndex]++] = new Item(inputWord, attributes);            // add new entry in last

        insertionSort(items, dividerIndex);                                                         // modified insertion sort

        divider[dividerIndex] = items;                                                              // update divider[] pointer
    }

    private void insertionSort(Item[] items, int dividerIndex) {
        Item tempItem;
        for (int i = sizeOfDividerSubArrays[dividerIndex] - 1; i > 0 && sizeOfDividerSubArrays[dividerIndex] >= 2; i--) {
            if (items[i].compareTo(items[i - 1]) < 0) {
                tempItem = items[i];
                items[i] = items[i - 1];
                items[i - 1] = tempItem;
            } else
                break;
        }
    }

    private void insertionSort(List<DocumentSR> items) {
        DocumentSR tempItem;
        for (int i = items.size() - 1; i > 0 && items.size() >= 2; i--) {
            if (items.get(i).compareTo(items.get(i - 1).document.name) < 0) {
                tempItem = items.get(i);
                items.set(i, items.get(i - 1));
                items.set(i - 1, tempItem);
            } else
                break;
        }
    }

    private static Object[] checkResize(Object[] items, int index) {
        if (index < items.length)
            return items;

        Object[] temp = new Item[index * 2];

        for (int i = 0; i < index; i++) {
            temp[i] = items[i];
        }
        return temp;
    }

    public List<Document> search(String s) {
        List<DocumentSR> searchResult = new ArrayList<DocumentSR>();
        DocumentSR prevDsr = null;
        String[] queryArray = s.split(" ");
        int i;
        for (i = 0; i < queryArray.length && !queryArray[i].equals("orderby"); i++) {
            int dividerIndex = getDividerIndex(queryArray[i]);
            Item[] items = divider[dividerIndex];

            int searchHit = search(queryArray[i], items, 0, sizeOfDividerSubArrays[dividerIndex]);
            if (searchHit < 0) {
                continue;
            }

            List<Attributes> attributesList = items[searchHit].attributes;
            for (int j = 0; j < attributesList.size(); j++) {
                Attributes attrSR = attributesList.get(j);
                String docName = attrSR.document.name;
                int docSearchHit = search(docName, searchResult, 0, searchResult.size());
                if (prevDsr != null && prevDsr.compareTo(docName) == 0) {
                    prevDsr.mergeDocumentSR(attrSR);
                } else if (docSearchHit >= 0) {
                    DocumentSR dsr = searchResult.get(docSearchHit);
                    dsr.mergeDocumentSR(attrSR);
                    prevDsr = dsr;
                } else {
                    searchResult.add(new DocumentSR(attrSR));
                    insertionSort(searchResult);
                }
            }
        }

        SortOrder order;
        if (i < queryArray.length && queryArray[i].equals("orderby")) {
            if (queryArray[++i].equals("count")) {
                order = SortOrder.COUNT;
            } else if (queryArray[i].equals("popularity")) {
                order = SortOrder.POPULARITY;
            } else if (queryArray[i].equals("name")) {
                order = SortOrder.NAME;
            } else if (queryArray[i].equals("occurrence")) {
                order = SortOrder.OCCURRENCE;
            } else
                order = SortOrder.NAME;
            boolean ascending = (++i < queryArray.length && queryArray[i].equals("desc")) ? false : true;

            bubbleSort(searchResult, order, ascending);
        }
        return toDocList(searchResult);
    }

    private int search(String key, Item[] a, int lo, int hi) {
        // possible key indices in [lo, hi)
        if (hi <= lo) return -1;
        int mid = lo + (hi - lo) / 2;
        int cmp = a[mid].compareTo(key);
        if (cmp > 0) return search(key, a, lo, mid);
        else if (cmp < 0) return search(key, a, mid + 1, hi);
        else return mid;
    }

    private int search(String key, List<DocumentSR> a, int lo, int hi) {
        // possible key indices in [lo, hi)
        if (hi <= lo) return -1;
        int mid = lo + (hi - lo) / 2;
        int cmp = a.get(mid).compareTo(key);
        if (cmp > 0) return search(key, a, lo, mid);
        else if (cmp < 0) return search(key, a, mid + 1, hi);
        else return mid;
    }

    private int getDividerIndex(String word) {
        char s = word.charAt(0);
        int indexUpper = s - 'A';
        int indexLower = s - 'a';
        if (indexUpper >= 0 && indexUpper < ('Z' - 'A'))
            return indexUpper;
        else if (indexLower >= 0 && indexLower < ('z' - 'a')) {
            return indexLower;
        } else
            return 'Z' - 'A' + 1;
    }

    private void bubbleSort(List<DocumentSR> a, SortOrder order, boolean ascending) {
        if (a.size() <= 0) {                                    // Checks if list is empty
            return;
        }
        int R = a.size() - 2;                                   // calculate R, R is possibale swaps per iterration
        boolean swap = true;                                    // Swap is true when two items swaps during one iterration

        while (R >= 0 && swap) {                                // Continue when R > 0 and swap is true
            swap = false;
            for (int i = 0; i <= R; i++) {
                if (a.get(i).compareTo(a.get(i + 1), order, ascending) > 0) {   // if a[i] > a[i++] then swap
                    swap = true;
                    DocumentSR temp = a.get(i);
                    a.set(i, a.get(i + 1));
                    a.set(i + 1, temp);
                }
            }
            R--;
        }
    }

    /*private int compare(DocumentSR a, DocumentSR b, SortOrder order, boolean ascending) {
        int result = 0;
        switch (order) {
            case NAME:
                result = a.compareTo(b.getDocName());
                break;
            case COUNT:
                result = a.documentCount - b.documentCount;
                break;
            case POPULARITY:
                result = a.document.popularity - b.document.popularity;
                break;
            case OCCURRENCE:
                result = a.maxOccurrence - b.maxOccurrence;
                break;
        }

        if (!ascending) {
            result *= -1;
        }

        return result;
    }*/

    private List<Document> toDocList(List<DocumentSR> searchResult) {
        List<Document> list = new ArrayList<Document>();
        for (int i = 0; i < searchResult.size(); i++) {
            list.add(searchResult.get(i).document);
        }
        return list;
    }
}
