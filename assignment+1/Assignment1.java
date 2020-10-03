import java.io.File;
import java.util.Scanner;

public class Assignment1 {

  private static class LinkedStack {
    /* Node class is made up of a string (line)
    and a reference to the next Node */
    private static class Node {
      private String line;
      private Node next;

      // Constructor for the Node Class
      private Node (String line) {
        this.line = line;
        this.next = null;
      }
    }

    // Class variable that represents the head Node at the top
    private Node head;

    // Constructor for the LinkedStack Class
    public LinkedStack () {
      this.head = null;
    }

    // Returns the Node at the top of the LinkedStack
    public void printTopNode() {
      System.out.println(head.line);
    }

    /* Push operation creates a Node from the given line,
    puts it on top of the stack and creates a link between the
    old and new head Nodes if the LinkedStack is not empty */
    public void push (String line) {
      Node newHead = new Node(line);

      if (head == null) {
        // Make the given line the head node and exit
        head = newHead;
      } else {
        // Get the old head Node
        Node oldHead = head;
        // Make the new node the head Node
        newHead.next = oldHead;
        head = newHead;
      }
    }

    /* Pop operation finds the Node at the top of the Stack,
    removes it by making the Node before it the head Node */
    public void pop() throws Exception {
      // Do not pop if the LinkedStack is empty
      if (head == null) {
        throw new Exception("The pop operation can not be executed as the Linked Stack is empty");
      }

      Node oldHead = head;
      head = oldHead.next;
    }

    // Print the elements of a given LinkedStack
    public void print() throws Exception {
      // if LinkedStack is empty, exit
      if (head == null) {
        throw new Exception("The Linked Stack can not be printed as it doesn't contain any elements");
      }

      Node currentNode = head;

      System.out.println("============ HEAD ============");
      while (currentNode != null) {
        System.out.println(currentNode.line);
        currentNode = currentNode.next;
      }
      System.out.println("============ TAIL ============");
    }
  }

  public static void main(String[] args) {

    try {
      // Read the file
      File file = new File("ds20s-a1.txt");
      Scanner myReader = new Scanner(file);

      // create an instance of LinkedStack object
      LinkedStack myLinkedStack = new LinkedStack();

      while (myReader.hasNextLine()) {
        String line = myReader.nextLine();

        // If the line is not equal to POP or PRINT, push it to the LinkedStack
        if (!line.equals("POP") && !line.equals("PRINT")) {
          myLinkedStack.push(line);
        // If the line is equal to POP, remove the top node from the LinkedStack
        } else if (line.equals("POP")) {
          myLinkedStack.pop();
        } else {
          myLinkedStack.print();
        }
      }

      System.out.println();
      System.out.println("============ FINAL RESULT ============");
      System.out.print("Top Node at the end: ");
      myLinkedStack.printTopNode();
      myReader.close();

    // Catch errors that may arise in reading the file
    } catch (Exception e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}
