
import java.util.*;

public class BinarySearchTree<T extends Comparable<T>> {

  private class Node {
    private T record; // CGNDBID
    private Node left;
    private Node right;

    // constructor for Node object
    public Node(T record) {
      this.record = record;
      this.left = null;
      this.right = null;
    }
  }

  // keep track of the root of the BST
  public Node root;

  // constructor for BST object
  public BinarySearchTree() {
    this.root = null;
  }

  // insertion wrapper function
  public void insert(T incomingRecord) {
    this.root = insertRecord(this.root, incomingRecord);
  }
  // insertion function implementation
  private Node insertRecord(Node current, T incomingRecord) {
    // if root is null, set the node as root
    if (current == null) {
      return new Node(incomingRecord);
    }
    if(current.record.compareTo(incomingRecord) < 0) {
      current.left = insertRecord(current.left, incomingRecord);
    } else if (current.record.compareTo(incomingRecord) > 0) {
      current.right = insertRecord(current.right, incomingRecord);
    }
    return current; 
  }

  // search function wrapper
  public T search(T searchRecord) {
    T found = searchNode(this.root, searchRecord);
    if(found == null){
      throw new NoSuchElementException("record not found");
    }
    return found;
  }

  // search function implementation
  private T searchNode(Node current, T searchRecord) {

    T found = null;
    while(current != null){
      if(current.record.compareTo(searchRecord) < 0){
        current = current.left;
      } else if (current.record.compareTo(searchRecord) > 0) {
        current = current.right;
      } else if (current.record.compareTo(searchRecord) == 0) {
        found = current.record;
        break;
      }
    }

    return found;
  }

  // inorder traversal
  public void inorder(){
    inorderTraversal(this.root);
  }

  private void inorderTraversal(Node root){
    if(root != null){
      inorderTraversal(root.left);
      System.out.println(root.record);
      inorderTraversal(root.right);
    }
  }

  // preorder traversal - prints the elements of the BST 
  public void preorderTraversal(){
    preOrderTraversal(this.root);
  }
  private void preOrderTraversal(Node n) {
    if (n != null) {
      System.out.print(n.record);
      preOrderTraversal(n.left);
      preOrderTraversal(n.right);
    }
  }

  // computes the height of the BST
  public int height(Node node) {
    // base case: tree is empty
    if (node == null) {
      return 0;
    }
    int l = height(node.left);
    int r = height(node.right);
    if (l > r) {
      return l + 1;
    } else {
      return r + 1;
    }
  }

  // creates an array of all the nodes in inorder
  private void nodesList(Node root, ArrayList<Node> nodes) {
    // base case: tree is empty
    if (root == null) {
      return;
    }
    nodesList(root.left, nodes);
    nodes.add(root);
    nodesList(root.right, nodes);
  }

  // recursive function to construct binary tree
  private Node balanceTreeUtil(ArrayList<Node> nodes, int start, int end) {
    // base case
    if (start > end) {
      return null;
    }
    // get the middle element and make it root
    int mid = (start + end) / 2;
    Node node = nodes.get(mid);
    /*
     * using index in Inorder traversal, construct left and right subtrees
     */
    node.left = balanceTreeUtil(nodes, start, mid - 1);
    node.right = balanceTreeUtil(nodes, mid + 1, end);
    return node;
  }

  // balances a given BST
  public Node balanceTree(Node root) {
    // store nodes of given BST in sorted order
    ArrayList<Node> nodes = new ArrayList<>();
    nodesList(root, nodes);

    /*
     * constructs a balanced BST from the nodes list by getting the middle of the
     * array, setting it as root, and recursively doing the same for all left and
     * right trees
     */
    int n = nodes.size();
    return balanceTreeUtil(nodes, 0, n - 1);
  }

  // functional tests
  // public static void main(String[] args) {
  //   System.out.println("Succesfully created a Binary Search Tree.");

  //   // unit tests
  //   BinarySearchTree<Record> bs = new BinarySearchTree<>();
  //   // BinarySearchTree<Float> bs = new BinarySearchTree<>();
  //   Record e1 = new Record("EJEIZ","Lac Lucie","Lake",46.987778,-75.38472,"Quebec", "Quebec");
  //   Record e2 = new Record("EFOWB","Rapides Boisvert","Rapids",46.6175,-74.263336,"Quebec", "Quebec");
  //   Record e3 = new Record("FDLAP","Mud Lake","Lake",48.161366,-79.93589,"Ontario", "Quebec");
  //   Record e4 = new Record("FBUEQ","Keswil Creek","Creek",46.092213,-78.97416,"Ontario", "Quebec");
  //   Record e5 = new Record("ABWNT","Coopers Head","Head",47.338722,-53.90362,"Newfoundland and Labrador", "Quebec");

  //   bs.insert(e1);
  //   bs.insert(e2);
  //   bs.insert(e3);
  //   bs.insert(e4);
  //   bs.insert(e5);
  //   bs.insert(e4);

  //   // bs.insert( 46.092213, e1);
  //   // bs.insert( 48.161366, e2);
  //   // bs.insert( 46.987778, e3);
  //   // bs.insert( 46.6175, e4);
  //   // bs.insert( 47.338722, e5);

  //   // System.out.println(compare(48.161366,  47.338722));

  //   // bs.preOrderTraversal(bs.root);
  //   // System.out.println();
  //   System.out.println(bs.search(e5).getCgndbId());
  //   // // System.out.println(bs.search( 48.161366));

  //   int h0 = bs.height(bs.root);
  //   System.out.println(h0);

  //   bs.root = bs.balanceTree(bs.root);

  //   int h1 = bs.height(bs.root);
  //   System.out.println(h1);
  // }
}
