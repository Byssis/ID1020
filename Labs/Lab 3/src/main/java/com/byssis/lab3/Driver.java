/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byssis.lab3;

import static com.byssis.lab3.BubbleSort.bubbleSort;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Albin
 */
public class Driver {

    public static void main(String[] args) {
        int inv;
        int n;
        Stopwatch sw;
        double time;        
        
        LinkedList a = new LinkedList();
        LinkedList b;
        Scanner in = new Scanner(System.in);
        Random rand = new Random();        
        
        System.out.println("Enter size of test array: ");
        n = in.nextInt();        
        
        for (int i = 0; i < n; i++) {
            a.insert(rand.nextInt(n));
        }
        b = new LinkedList(a);   // Copy list 
        
        sw = new Stopwatch();
        inv = a.inversionCount();
        time = sw.elapsedTime();
        System.out.println("N: " + n + " Inversion normal (n^2): " + inv + " Time: " + time + "s");
        
        sw = new Stopwatch();
        inv = a.inversionCountMergeSort();
        time = sw.elapsedTime();
        System.out.println("N: " + n + " MergeSort Inversion (nlogn): " + inv + " Time: " + time + "s");                
        
        sw = new Stopwatch();
        inv = bubbleSort(b);
        time = sw.elapsedTime();
        System.out.println("N: " + n + " Bubble sort swap (n^2): " + inv + " Time: " + time + "s");         
    }
}
