package com.byssis.labb1;

public abstract class ErrorPascal implements Pascal {  

    abstract public void printPascal(int row);

    abstract public int binom(int n, int k);

    public void errorCheck(int n, int k) {
        if (n < 0 || k < 0 || k > n) {
            throw new IllegalArgumentException("Input variable n must be greater than 0");
        } else if (k < 0) {
            throw new IllegalArgumentException("Inuput variable k can not be negative");
        } else if (k > n) {
            throw new IllegalArgumentException("Inuput variable k can not greater than n");
        }
    }
}
