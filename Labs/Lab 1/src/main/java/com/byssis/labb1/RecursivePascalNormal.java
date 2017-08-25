/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byssis.labb1;


/**
 *
 * @author Albin
 */
public class RecursivePascalNormal extends ErrorPascal {

    static boolean reverse = true;

    public void printPascal(int n) {
        errorCheck(n,0);
        if (n >= 0) {
            if (reverse) {
                printRow(n);
                printPascal(n - 1);
            } else {
                printPascal(n - 1);
                printRow(n);
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

    public int binom(int n, int k) {
        errorCheck(n,k);
        // Base case
        if (k == 0 || n == k) {
            return 1;
        }
        // Recurion bi(n,k) = bi(n-1, k-1) + bi(n-1, k) 
        return binom(n - 1, k - 1) + binom(n - 1, k);
    }
    
    
}
