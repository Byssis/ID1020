/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byssis.lab3;

/**
 *
 * @author Albin
 */
public class BubbleSort {
    /*
        bubbleSort(LinkedList a)
        Sorts and counts number of inversion of LinkedList input a
        This method will rearange input a in ascending order    
    */
    public static int bubbleSort(LinkedList a) {
        int numOfSwap = 0;                                      // Variable to count numbers of swap
        if (a.length() <= 0) {                                  // Checks if list is empty
            return 0;
        }
        int R = a.length() - 2;                                 // calculate R, R is possibale swaps per iterration
        boolean swap = true;                                    // Swap is true when two items swaps during one iterration

        while (R >= 0 && swap) {                                // Continue when R > 0 and swap is true
            a.resetIter();                                      // Restet iterrator for a
            swap = false;           
            for (int i = 0; i <= R; i++) {                      
                if (a.getCurrentValue() > a.getNextValue()) {   // if a[i] > a[i++] then swap
                    swap = true;                                
                    a.swapWithNext();                           // Swap with next 
                    numOfSwap++;                                // Count number of swaps
                }
                a.goForward();                                  // Move pointer for interrator
            }            
            R--;                            
        }
        return numOfSwap;                                       // return number of swaps
    }
}
