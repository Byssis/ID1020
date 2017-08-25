package com.byssis.lab3;

import java.util.Iterator;

/**
 *
 * @author Albin Byström 
 */
public class LinkedList {

    private class Node {

        private int item;
        private Node next;

        public Node() {
            next = null;
        }

        public Node(int item) {
            this.item = item;
            next = null;
        }        

        public void setNext(Node next) {
            this.next = next;
        }

        public void setNext(int item) {
            this.next = new Node(item);
        }

        public void set(int item) {
            this.item = item;
        }

        public boolean hasNext() {
            return (next == null) ? false : true;
        }
    }

    private Node head;
    private Node tail;
    private Node current;
    private int length;
    private int count;

    // Create empty linkedlist
    public LinkedList() {
        head = null;
        current = null;
        tail = null;
        length = 0;
    }
    
    // Create linked list from array
    public LinkedList(int a[]) {
        head = null;
        current = null;
        tail = null;
        length = 0;
        for (int i = 0; i < a.length; i++) {
            this.insert(a[i]);
        }     
    }
    
    // Create linked list from linkedlist
    public LinkedList(LinkedList a) {
        head = null;
        current = null;
        tail = null;
        length = 0;
        a.resetIter();
        while(a.current != null) {
            this.insert(a.goForward());              
        }
        a.resetIter();
    }
    
    // Insert value at the end of the list
    public void insert(int item) {
        if (head == null) {
            head = new Node(item);
            tail = head;
            current = head;
        } else {
            tail.setNext(item);
            tail = tail.next;
        }
        length++;
    }
    
    // Insert value in front
    public void insertFront(int item) {
        if (head == null) {
            head = new Node(item);
            tail = head;
        } else {
            Node temp = new Node(item);
            temp.setNext(head);
            head = temp;
        }
        length++;
    }
    
    // Swap value with the next inline node 
    public void swapWithNext() {
        if (!current.hasNext()) {
            throw new java.util.NoSuchElementException("No elements left, can not swap");
        }
        int item1 = current.item;
        current.set(current.next.item);
        current.next.set(item1);
    }
    
    // return cuurent value
    public int getCurrentValue() {
        return current.item;
    }
    
    // return next value
    public int getNextValue() {
        if (!current.hasNext()) {
            throw new java.util.NoSuchElementException(" No more elements ");
        }
        return current.next.item;
    }
    
    // return value and go Forward to next node 
    public int goForward() {
        if (current == null) {
            throw new java.util.NoSuchElementException(" end of iteration ");
        }
        int temp = current.item;
        current = current.next;
        return temp;
    }
    
    // reset interrator 
    public void resetIter() {
        current = head;
    }
    
    // return length of list
    public int length() {
        return length;
    }
    
    // print list
    public void printList() {
        print(head);
    }
    
    // count inversion (n^2)
    public int inversionCount() {
        int count = 0;

        Node a = head;
        Node b = a.next;

        while (a.next != null) {
            b = a.next;
            while (b != null) {
                if (a.item > b.item) {
                    count++;
                }
                b = b.next;
            }
            a = a.next;
        }
        return count;
    }
    
    // count inversion n log n
    public int inversionCountMergeSort() {
        count = 0;                                  // Used global variable to count inversion, not the finest way of doing this but it works
        head = mergeSort(head, 0, this.length);        
        return count;
    }
    
    // merge sort, used to count inversions
    private Node mergeSort(Node head, int low, int high) {
        // Base case
        if (head == null || head.next == null) {
            return head;
        }
        Node middle = getMiddle(head);                  // Get middle node in list head
        Node secondHalf = middle.next;                  // Split head in two parts
        middle.next = null;
        int midL = (low + high) / 2 + (low + high) % 2; // Calculate mid value for left side
        int midH = (low + high) / 2;                    // Calculate mid value for right side
        
        // Recursive merge and mergeSort
        return merge(mergeSort(head, low, midL), mergeSort(secondHalf, low, midH), low, midL);
    }
    
    // Used to merge to list togather, a and b
    private Node merge(Node a, Node b, int low, int mid) {
        Node tempHead = new Node();                     // Temp head for merger of a and a
        Node current = tempHead;                        // Pointer to merge list

        int leftCount = low;                            
        while (a != null && b != null) {
            if (a.item <= b.item) {                     // Choose wich list to take item from
                current.next = a;
                a = a.next;
                leftCount++;
            } else {
                current.next = b;
                b = b.next;
                count += (mid - leftCount);             // Counts number of node b jumps over in a
            }
            current = current.next;
        }
        current.next = (a == null) ? b : a;             // If a or b i empty, take the remaing item and put them in merge list

        return tempHead.next;
    }

    // returns middle node of a linked list
    private Node getMiddle(Node head) {
        if (head == null) {
            return head;
        }
        Node slowPointer = head;                        // Slow pointer speed 1X
        Node fastPointer = head;                        // Fast pointer speed 2X
        while (fastPointer.next != null && fastPointer.next.next != null) {
            slowPointer = slowPointer.next;
            fastPointer = fastPointer.next.next;
        }        
        return slowPointer;                             // When fast pointer is finsihed return slow pointer
    }
    
    // prints list from node a
    private void print(Node a) {
        while (a != null) {
            System.out.print(a.item + " ");
            a = a.next;
        }
        System.out.println();
    }
}
