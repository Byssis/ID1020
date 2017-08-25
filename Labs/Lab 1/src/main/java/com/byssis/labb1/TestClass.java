
package com.byssis.labb1;

import edu.princeton.cs.algs4.Stopwatch;
import java.util.Scanner;

/**
 *
 * @author Albin
 */
public class TestClass {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("Level: ");

            int n = in.nextInt();
            Pascal pascal = new RecursivePascalHashMap();
            
            Stopwatch sw = new Stopwatch();
            pascal.printPascal(n);  
            System.out.println(sw.elapsedTime());
        }
    }
}
