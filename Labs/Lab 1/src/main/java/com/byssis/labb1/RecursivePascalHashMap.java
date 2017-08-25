
package com.byssis.labb1;

import java.util.HashMap;

public class RecursivePascalHashMap extends ErrorPascal {
    
    private boolean reverse = false;
    
    // Hashmap used for to cache previous calculate numbers 
    private HashMap<String, Integer> hashMap = new HashMap<String, Integer>();

    @Override
    public void printPascal(int n) {
        // Variable validation
        errorCheck(n, 0);
        // Base case
        if (n == 0) {
            printRow(n);
        } else if (n > 0) {
            if (reverse) {
                // Print triangle reversed
                printRow(n);                    // printRow help function for printing rows
                printPascal(n - 1);
            } else {
                // Print triangle normal
                printPascal(n - 1);
                printRow(n);
            }
        }
    }

    private void printRow(int n) {
        int k = 0;
        
        //Print row
        while (k <= n) {
            System.out.print((binom(n, k++)) + " ");
        }
        System.out.println();
    }

    @Override
    public int binom(int n, int k) {
        // Variable validation
        errorCheck(n, k);
        
        int kV = k;
        if (((n + 1) / 2 + (n + 1) % 2) <= k) {
            kV = n - k;
        }
        String key = n + " " + kV;
        if (!hashMap.containsKey(key)) {
            // Base case
            if (k == 0 || n == k) {
                hashMap.put(key, 1);
            } else // Recurion bi(n,k) = bi(n-1, k-1) + bi(n-1, k) 
            {
                hashMap.put(key, binom(n - 1, k - 1) + binom(n - 1, k));
            }
        }
        return hashMap.get(key);
    }   
}
