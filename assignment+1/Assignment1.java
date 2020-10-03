import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;

public class Assignment1 {

    static class LinkedListStack<T> {
        // an inner class to define a single linked list node
        private class Node {
            T value;
            Node next;
        }
        // keep track of the head of the stack, head.next will point to the next element in the stack
        private Node head;

        // constructor for the stack class
        public LinkedListStack(){
            this.head = null;
        }

        // pop implementation
        public T pop() {
            if(head == null){
                throw new CustomException("empty stack");
            }

            T value = head.value;
            head = head.next;

            return value;
        }

        // push implementation
        public void push(T value){
            Node oldHead = this.head;
            this.head = new Node();
            this.head.value = value;
            this.head.next = oldHead;
        }

        // print the stack from top to bottom
        public void print(){
            System.out.print("(HEAD) ");

            Node n = head; // store local copy of head here in print() function

            if(head == null){
                throw new CustomException("empty stack");
            }

            while(n != null){
                if(n.next != null) {
                    System.out.print(n.value + " -> ");
                } else {
                    System.out.print(n.value + " ");
                }
                n = n.next;
            }

            System.out.println("(TAIL)");
        }
    }
    public static void main(String[] args){
        // instantiate an instance of our stack backed by a linked list
        LinkedListStack<String> lls = new LinkedListStack<>();

        // get a filesystem reference to the flat file to load; as per assignment1 instructions
        Path path = Paths.get("ds20s-a1.txt");

        // use the scanner class, in conjunction with a switch statement; to read the file line by line and determine the resultant set of data to be inserted into the stack
        try (Scanner scanner = new Scanner(path)){
            while(scanner.hasNextLine()){
                String line = (scanner.nextLine()).trim();
                switch(line){
                    case "POP":
                        lls.pop();
                        break;
                    case "PRINT":
                        lls.print();
                        System.out.println(); // insert extra new line for readability
                        break;
                    default:
                        lls.push(line);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static class CustomException extends RuntimeException {
        private static final long serialVersionUID = 1;

        //
        public CustomException(){

        }

        public CustomException(String message){
            super(message);
        }
    }
}