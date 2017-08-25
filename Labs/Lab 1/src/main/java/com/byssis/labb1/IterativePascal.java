package com.byssis.labb1;

public class IterativePascal extends ErrorPascal {

    static boolean reverse = true;    
    int[][] memory;

    public void printPascal(int n) {
        errorCheck(n,0);
        setupArray(n);
        if(reverse)
            for (int i = 0; i <= n; i++) {
                printRow(i);
            }
        else
            for (int i = n; i >= 0; i--) {
                printRow(i);
            }
    }

    private void setupArray(int n) {
        if (memory == null || memory.length < n + 1) {
            memory = new int[n + 1][];
            for (int i = 0; i <= n; i++) {
                memory[i] = new int[i + 1];
            }
        }
    }

    private void printRow(int n) {
        int k = 0;
        while (k <= n) {
            System.out.print((binom(n, k++)) + " ");
        }
        System.out.println();
    }

    @Override
    public int binom(int n, int k) {
        errorCheck(n,k);
        if (memory[n][k] == 0) {
            for (int i = 0; i <= n; i++) {
                memory[i][0] = 1;
                memory[i][i] = 1;
                for (int j = 1; j < i; j++) {
                    memory[i][j] = memory[i - 1][j - 1] + memory[i - 1][j];
                }
            }
        }
        return memory[n][k];
    }   
}
