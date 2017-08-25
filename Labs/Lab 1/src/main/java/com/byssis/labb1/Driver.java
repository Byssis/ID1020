/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byssis.labb1;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.Scanner;

/**
 *
 * @author Albin
 */
public class Driver {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("N value: ");

            int n = in.nextInt();
            Pascal pascal = new RecursivePascal();
            
            Stopwatch sw = new Stopwatch();
            pascal.printPascal(n);           
            
            System.out.println(sw.elapsedTime());
            
            pascal = new IterativePascal();
            sw = new Stopwatch();
            pascal.printPascal(n);
            System.out.println(sw.elapsedTime());
            
            pascal = new RecursivePascalHashMap();
            sw = new Stopwatch();
            pascal.printPascal(n);
            System.out.println(sw.elapsedTime());
            
            pascal = new RecursivePascalNormal();
            sw = new Stopwatch();
            pascal.printPascal(n); 
            System.out.println(sw.elapsedTime());
                      
            
        }
    }
}
