package com.byssis.labb1;

/**
 *
 * @author Albin
 */
public class RecursivePascal extends ErrorPascal {

    private boolean reverse = false;
    private int[][] memory;

    @Override
    public void printPascal(int n) {
        errorCheck(n, 0);
        setupArray(n);
        if (n == 0) {
            printRow(n);
        } else if (n > 0) {
            if (reverse) {
                printRow(n);
                printPascal(n - 1);
            } else {
                printPascal(n - 1);
                printRow(n);
            }
        }
    }

    private void setupArray(int n) {
        // if the memory is empty or too small
        if (memory == null || memory.length < n + 1) {
            memory = new int[n + 1][];
            for (int i = 0; i <= n; i++) {
                memory[i] = new int[i + 1];
            }
        }
    }

    private void printRow(int n) {
        int k = 0;
        //triangle[n] = new int[n+1]; 
        while (k <= n) {
            System.out.print((binom(n, k++)) + " ");
        }
        System.out.println();
    }

    public int binom(int n, int k) {        
        errorCheck(n, k);
        int kKey = k;
        if (((n + 1) / 2 + (n + 1) % 2) <= k && k > 0) {
            kKey = n - k;
        }
        if (memory[n][kKey] == 0) {
            // Base case
            if (k == 0 || n == k) {
                memory[n][kKey] = 1;
            } else // Recurion bi(n,k) = bi(n-1, k-1) + bi(n-1, k) 
            {
                memory[n][kKey] = binom(n - 1, k - 1) + binom(n - 1, k);
            }
        }
        return memory[n][kKey];
    }

    /*private void errorCheck(int n, int k) {
        if (n < 0 || k < 0 || k > n) {
            throw new IllegalArgumentException("Input variable n must be greater than 0");
        } else if (k < 0) {
            throw new IllegalArgumentException("Inuput variable k can not be negative");
        } else if (k > n) {
            throw new IllegalArgumentException("Inuput variable k can not greater than n");
        }
    }*/
}
