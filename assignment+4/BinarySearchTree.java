package assignment4;
import java.util.*;

public class BinarySearchTree {

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

  public Node root;

  // constructor for BST object
  public BinarySearchTree() {
    this.root = null;
  }

  public void insert(String key, Record value) {
    // if root is null, set the node as root
    if (this.root == null) {
      this.root = new Node(key, value);
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
          current.left = new Node(key, value);
          break;
        }
      // look right
      } else if (key.compareTo(current.key) > 0) {
        // if right node is not null, set current to the right subtree
        if (current.right != null) {
          current = current.right;
        // if right node is null, insert new Node to the right of current
        } else {
          current.right = new Node(key, value);
          break;
        }
      } else {
        System.out.println("This key already exists");
        break;
      }
    }
  }

  // conducts a search, recursively, within the BST
  private Node recursiveSearch(Node n, String key) {
      // base case: key is equal to search key
      if (n.key==key) {
          return n;
      }
      // key is less than root's key
      if (key.compareTo(n.key) < 0) {
        return recursiveSearch(n.left, key);
      }
      return recursiveSearch(n.right, key);
  }

  // conducts a search, iteratively, within the BST
  private Node iterativeSearch(String key) {
    Node n = this.root;
    // traverse until we reach a dead end
    while (n != null) {
      if (key.compareTo(n.key) < 0) {
        n = n.left;
      } else if (key.compareTo(n.key) > 0) {
        n = n.right;
      // if found, return n
      } else if (key.compareTo(n.key) == 0) {
        return n;
      }
    }
    return null;
  }

  // search method exposed to users
  public String search(String key) {
    try {
      // Can use iterative or recursive search methods here
      Node n = iterativeSearch(key);
      // Node n = recursiveSearch(this.root, key);
      Record r = n.value;
      r.printRecord();
      System.out.println();
      String l = r.getRecordLog();
      return l;
    } catch(NullPointerException e) {
      String o = "Record was not found\n";
      System.out.println(o);
      return o;
    }
  }

  // prints the elements of the BST
  public void preOrderTraversal(Node n) {
    if (n != null) {
      System.out.print(n.key + " ");
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
    /* using index in Inorder traversal, construct
    left and right subtrees */
    node.left = balanceTreeUtil(nodes, start, mid-1);
    node.right = balanceTreeUtil(nodes, mid+1, end);
    return node;
  }

  // balances a given BST
  public Node balanceTree() {
    // store nodes of given BST in sorted order
    ArrayList<Node> nodes = new ArrayList<>();
    nodesList(this.root, nodes);

    /* constucts a balanced BST from the nodes list by getting
    the middle of the array, setting it as root, and recursively
    doing the same for all left and right trees */
    int n = nodes.size();
    return balanceTreeUtil(nodes, 0, n-1);
  }

  public static void main(String[] args) {
    System.out.println("Succesfully created a Binary Search Tree.");

    // testing
    BinarySearchTree bs = new BinarySearchTree();
    Record e1 = new Record("EJEIZ,Lac Lucie,Lake,46.987778,-75.38472,Quebec");
    Record e2 = new Record("EFOWB,Rapides Boisvert,Rapids,46.6175,-74.263336,Quebec");
    Record e3 = new Record("FDLAP,Mud Lake,Lake,48.161366,-79.93589,Ontario");
    Record e4 = new Record("FBUEQ,Keswil Creek,Creek,46.092213,-78.97416,Ontario");
    Record e5 = new Record("ABWNT,Coopers Head,Head,47.338722,-53.90362,Newfoundland and Labrador");

    bs.insert("EJEIZ", e1);
    bs.insert("EFOWB", e2);
    bs.insert("FDLAP", e3);
    bs.insert("FBUEQ", e4);
    bs.insert("ABWNT", e5);

    bs.preOrderTraversal(bs.root);
    System.out.println();
    bs.search("ABWNT");

    int h0 = bs.height(bs.root);
    System.out.println(h0);

    bs.balanceTree(bs.root);

    int h1 = bs.height(bs.root);
    System.out.println(h1);

  }
}
