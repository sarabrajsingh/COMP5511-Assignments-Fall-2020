package assignment4;
import java.util.*;

public class BinarySearchTree<V> {

  private class Node<V> {
    private String key;
    private V value;
    private Node left;
    private Node right;

    // constructor for Node class
    private Node(String key, V value) {
      this.key = key;
      this.value = value;
      this.right = null;
      this.left = null;
    }
  }

  public Node root;

  // constructor for BST class
  public BinarySearchTree() {
    this.root = null;
  }

  public void insert(String key, V value) {
    // base case: Root is null
    if (this.root == null) {
      this.root = new Node<>(key, value);
      return;
    }

    Node current = this.root;

    while(true) {
      // look left
      if (key.compareTo(current.key) < 0) {
        // if left node is not null, set current to the left subtree
        if (current.left != null) {
          current = current.left;
        // if left node is null, insert new Node to the left of current
        } else {
          current.left = new Node<>(key, value);
          break;
        }
      // look right
      } else if (key.compareTo(current.key) > 0) {
        // if right node is not null, set current to the right subtree
        if (current.right != null) {
          current = current.right;
        // if right node is null, insert new Node to the right of current
        } else {
          current.right = new Node<>(key, value);
          break;
        }
      } else {
        System.out.println("This key already exists");
        break;
      }
    }
  }

  private Node pSearch(Node root, String key) {
      // base case: root is null or key is present at root
      if (root==null || root.key==key)
          return root;

      // key is less than root's key
      if (key.compareTo(root.key) < 0) {
        return pSearch(root.left, key);
      }

      return pSearch(root.right, key);
  }

  // search method exposed to users
  public void search(String key) {
      Node result = pSearch(this.root, key);
      System.out.println(result.value);
  }

  // prints a given binary tree using inorder traversal
  public void inorderTraversal() {
    Stack<Node> s = new Stack<Node>();
    Node current = this.root;

    if (current == null) {
      System.out.println("Tree is empty.");
      return;
    }

    // traverse the tree
    while (current != null || s.size() > 0) {
        // reach the left most Node of the current Node
        while (current !=  null) {
            s.push(current);
            current = current.left;
        }
        // current must be NULL at this point
        current = s.pop();
        System.out.print(current.key + " ");
        // now, visit the right subtree
        current = current.right;
    }
    // print a new line when we reach the end of the tree
    System.out.println("");
  }

  public static void main(String[] args) {
    System.out.println("Succesfully created a Binary Search Tree.");
  }
}
