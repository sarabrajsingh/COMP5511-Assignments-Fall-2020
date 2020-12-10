package assignment4;
import java.util.*;

public class BinarySearchTree<T> {

  private class Node<T> {
    private T key;
    private ArrayList<Record> values;
    private Node left;
    private Node right;

    // constructor for Node object
    private Node(T key, Record value) {
      this.key = key;
      this.values = new ArrayList<>();
      values.add(value);
      this.right = null;
      this.left = null;
    }

    private ArrayList<Record> getValues() {
      return this.values;
    }
  }

  public Node root;

  // constructor for BST object
  public BinarySearchTree() {
    this.root = null;
  }

  /* does a comparison based on the data type of T, returns 1 if
  key1 is smaller than key2, 2 if key1 is greater than key2 and
  0 if they are equal */
  public static int compare(Object key1, Object key2) {
    if (key1 instanceof String) {
      String k1 = (String) key1;
      String k2 = (String) key2;
      int x = k1.compareTo(k2);
      if (x < 0) {
        return 1;
      } else if (x > 0) {
        return 2;
      } else {
        return 0;
      }
    } else {
      Float k3 = (Float) key1;
      Float k4 = (Float) key2;
      if (k3 < k4) {
        return 1;
      } else if (k3 > k4) {
        return 2;
      } else {
        return 0;
      }
    }
  }

  // insert a record in the BST
  @SuppressWarnings("unchecked")
  public void insert(T key, Record r) {
    // if root is null, set the node as root
    if (this.root == null) {
      this.root = new Node<>(key, r);
      return;
    }
    Node current = this.root;
    while(true) {
      // look left
      if (compare(key, current.key) == 1) {
        // if left node is not null, set current to the left subtree
        if (current.left != null) {
          current = current.left;
        // if left node is null, insert new Node to the left of current
        } else {
          current.left = new Node<>(key, r);
          break;
        }
      // look right
      } else if (compare(key, current.key) == 2) {
        // if right node is not null, set current to the right subtree
        if (current.right != null) {
          current = current.right;
        // if right node is null, insert new Node to the right of current
        } else {
          current.right = new Node<>(key, r);
          break;
        }
      } else {
        current.values.add(r);
        break;
      }
    }
  }

  // search a given id in the BST
  @SuppressWarnings("unchecked")
  public String search(T key) {
    Node n = this.root;
    // traverse until we reach a dead end
    while (n != null) {
      if (compare(key, n.key) == 1) {
        n = n.left;
      } else if (compare(key, n.key) == 2) {
        n = n.right;
      // if found, return n
    } else if (compare(key, n.key) == 0){
        ArrayList<Record> records = n.getValues();
        String ll = "";
        for (Record r : records) {
          String l = r.getRecordLog();
          ll = ll + l + "\n";
        }
        return ll;
      }
    }
    String o = "Record was not found";
    return o;
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
  public Node balanceTree(Node root) {
    // store nodes of given BST in sorted order
    ArrayList<Node> nodes = new ArrayList<>();
    nodesList(root, nodes);

    /* constructs a balanced BST from the nodes list by getting
    the middle of the array, setting it as root, and recursively
    doing the same for all left and right trees */
    int n = nodes.size();
    return balanceTreeUtil(nodes, 0, n-1);
  }

  public static void main(String[] args) {
    System.out.println("Succesfully created a Binary Search Tree.");

    // unit tests
    BinarySearchTree<String> bs = new BinarySearchTree<>();
    // BinarySearchTree<Float> bs = new BinarySearchTree<>();
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
    bs.insert("ABWNT", e4);

    // bs.insert((float) 46.092213, e1);
    // bs.insert((float) 48.161366, e2);
    // bs.insert((float) 46.987778, e3);
    // bs.insert((float) 46.6175, e4);
    // bs.insert((float) 47.338722, e5);

    // System.out.println(compare((float)48.161366, (float) 47.338722));

    bs.preOrderTraversal(bs.root);
    System.out.println();
    System.out.println(bs.search("ABWNT"));
    // System.out.println(bs.search((float) 48.161366));

    int h0 = bs.height(bs.root);
    System.out.println(h0);

    bs.root = bs.balanceTree(bs.root);

    int h1 = bs.height(bs.root);
    System.out.println(h1);

  }
}
