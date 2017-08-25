import edu.princeton.cs.introcs.In;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

/**
 * Created by Albin on 2016-09-27.
 */
public class Driver {

    public static void main(String[] args) {
        PrefixRecursive prefixTree = new PrefixRecursive();

        URL url = ClassLoader.getSystemResource("kap1.txt");

        if (url != null) {
            System.out.println("Reading from: " + url);
        } else {
            System.out.println("Couldn't find file: kap1.txt");
        }

        In input = new In(url);

        while (!input.isEmpty()) {
            String line = input.readLine().trim();
            String[] words = line.split("(\\. )|:|,|;|!|\\?|( - )|--|(\' )| ");
            String lastOfLine = words[words.length - 1];

            if (lastOfLine.endsWith(".")) {
                words[words.length - 1] = lastOfLine.substring(0, lastOfLine.length() - 1);
            }

            for (String word : words) {
                String word2 = word.replaceAll("\"|\\(|\\)", "");

                if (word2.isEmpty()) {
                    continue;
                }

                //System.out.println(word2);
                prefixTree.put(word2);
            }
        }
        System.out.println("Size:\t\t\t " + prefixTree.count());
        System.out.println("Size distinct:\t " + prefixTree.distinct());

        Scanner in = new Scanner(System.in);


        Iterator<Map.Entry<String, Integer>> iter = prefixTree.iterator();

        Map.Entry<String, Integer>[] max = new Map.Entry[10];
        Map.Entry<String, Integer>[] min = new Map.Entry[10];
        int i = 0;

        // 1,2
        while(iter.hasNext()) {
            Map.Entry<String, Integer> candidate = iter.next();
            if(i < max.length){
                max[i] = candidate;
                min[i++] = candidate;
            }else{
                // Sort
                insertionSort(max);
                insertionSort(min);

                // Add
                if(max[0].getValue() < candidate.getValue())
                    max[0] = candidate;
                if(min[min.length-1].getValue() > candidate.getValue())
                    min[min.length-1] = candidate;
            }
        }
        insertionSort(max);
        insertionSort(min);

        System.out.println("1. Max: ");

        for(int j = max.length - 1; j >= 0; j--) {
            System.out.println(max[j]);
        }

        System.out.println("\n2. Min: ");

        for(int j = 0; j < max.length; j++) {
            System.out.println(min[j]);
        }

        // 3
        String keyPrefix = "";
        int compare = 0;
        for(char j = 0; j < 26; j++){
            for(char k = 0; k < 26; k++){
                char c1 = (char)(j + 'a');
                char c2 = (char)(k + 'a');
                String key = c1 + "" + c2;
                if(prefixTree.count(key) > compare){
                    compare = prefixTree.count(key);
                    keyPrefix = key;
                }
            }
        }

        System.out.println("\n3. Prefix of length 2: " + keyPrefix);

        String keyS = "";
        compare = 0;
        for(char j = 0; j < 26; j++) {
            char c1 = (char) (j + 'a');
            String key = c1 + "";
            if (prefixTree.distinct(key) > compare) {
                compare = prefixTree.distinct(key);
                keyS = key;
            }
        }

        System.out.println("\n4. Letter with highest distinct: " + keyS);


        while(true) {
            System.out.println("\nSearch for word: ");
            String key = in.next();
            System.out.println("Key: " + key + " Value: " + prefixTree.get(key));
            System.out.println("Count: " + prefixTree.count(key) + " Distinct: " + prefixTree.distinct(key));
        }
    }

    private static void insertionSort(Map.Entry<String, Integer>[] array) {
        for(int i = 1; i < array.length; i++){
            int j = i;
            while(j > 0 && array[j-1].getValue() > array[j].getValue()){
                Map.Entry<String, Integer> temp = array[j-1];
                array[j-1] = array[j];
                array[j] = temp;
                j--;
            }
        }
    }
}
