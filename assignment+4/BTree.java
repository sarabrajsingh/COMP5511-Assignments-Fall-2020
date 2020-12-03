package assignment4;
import java.util.*;

public class BTree {

  public static void main(String[] args) {

    private class Node {
      private String key;
      private Record value;
      private Node left;
      private Node right;

      // constructor for Node object
      private Node(String key, Record value) {
        this.key = key;
        this.value = value;
        this.right = null;
        this.left = null;
      }
    }
  }
}
