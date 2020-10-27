public class Assignment2PQ1 {

  // A custom linked list class
  public static class MyLinkedList<G> {

    public static class Node<G> {
      public G data;
      public Node next;

      // Constructor for the Node class
      public Node (G data) {
        this.data = data;
        this.next = null;
      }
    }

    // Class variable that represents the head Node at the top
    public Node head;

    // Constructor for the LinkedList Class
    public MyLinkedList() {
      this.head = null;
    }

    public Node peek() {
      return this.head;
    }

    public void add(G data) {
      Node<G> new_node = new Node<>(data);

      if (this.head == null) {
        this.head = new_node;
      } else {
        Node next = this.head;
        while (next.next != null) {
          next = next.next;
        }
        next.next = new_node;
      }
    }

    public void print() {
      if (this.head == null) {
        System.out.println("The Linked List doesn't contain any elements");
      } else {
        Node next = this.head;
        while (next != null) {
          System.out.println(next.data);
          next = next.next;
        }
      }
    }
  }

  public static int iterativeCount(MyLinkedList ll) {
    MyLinkedList.Node head = ll.peek();

    /* if head is null, linked list is empty,
    therefore, return 0 */
    if (head == null) {
      return 0;

    /* if head is not null, iterate through the
    elements of the linked list */
    } else {
      MyLinkedList.Node next_element = head;
      int count = 0;

      while (next_element != null) {
        next_element = next_element.next;
        count++;
      }
      return count;
    }
  }

  public static int recursiveCount(MyLinkedList.Node node) {
    // Base case
    if (node == null) {
      return 0;
    } else {
      return 1 + recursiveCount(node.next);
    }
  }

  public static void main(String args[]) {

    // Create a LinkedList instance
    MyLinkedList<Integer> ll = new MyLinkedList<>();

    ll.add(1);
    ll.add(2);
    ll.add(3);
    ll.add(4);
    ll.add(5);
    ll.add(6);

    int c = iterativeCount(ll);
    System.out.format("Iterative count yields: %s\n", c);

    int c1 = recursiveCount(ll.peek());
    System.out.format("Recursive count yields: %s\n", c1);
  }
}
