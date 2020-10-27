import java.util.Stack;
import java.util.Arrays;

public class Assignment2PQ2 {

  // a really basic Binary Tree Class
  public static class BinaryTree {
    public static class Node<G> {
      public G data;
      public Node left;
      public Node right;

      // constructor of Node
      public Node(G data) {
        this.data = data;
        this.right = null;
        this.left = null;
      }

      public Node getRight() {
        return this.right;
      }

      public Node getLeft() {
        return this.left;
      }

      public boolean isParent() {
        if (this.right == null & this.left == null) {
          return false;
        } else {
          return true;
        }
      }
    }

    public Node root;

    // constructor of BinaryTree
    public BinaryTree() {
      this.root = null;
    }

    public void rightInsert(Node new_node, Node parent) {
      // if tree is empty, set the node as root
      if (this.root == null) {
        this.root = new_node;
      // otherwise, insert the new node on the right
      } else {
        Node rightChild = parent.getRight();

        if (rightChild == null) {
          parent.right = new_node;
        } else {
          System.out.println("Couldn\'t insert this node to the Binary tree because the parent already has a right child");
        }
      }
    }

    public void leftInsert(Node new_node, Node parent) {
      // if tree is empty, set the node as root
      if (this.root == null) {
        this.root = new_node;
      /* otherwise, insert the new node on the right */
      } else {
        Node leftChild = parent.getLeft();

        if (leftChild == null) {
          parent.left = new_node;
        } else {
          System.out.println("Couldn\'t insert this node to the Binary tree because the parent already has a left child");
        }
      }
    }

    // prints a given binary tree using inorder traversal
    public void printTree() {
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
          System.out.print(current.data + " ");
          // now, visit the right subtree
          current = current.right;
      }
      // print a new line when we reach the end of the tree
      System.out.println("");
    }
  }

  public static Stack createStack(BinaryTree bt) {
    Stack<Object> evaluationStack = new Stack<>();
    Stack<BinaryTree.Node> traversalStack = new Stack<BinaryTree.Node>();

    BinaryTree.Node current = bt.root;

    if (current == null) {
      System.out.println("There is no arithmetic operation to evaluate on the given tree.");
    }

    while (current != null || traversalStack.size() > 0) {
        // reach the left most Node of the current Node
        while (current != null) {
            traversalStack.push(current);
            evaluationStack.push(current.data);
            current = current.left;
        }
        // current must be NULL at this point
        current = traversalStack.pop();
        // now, visit the right subtree
        current = current.right;
    }
    return evaluationStack;
  }

  public static Integer solvePostfix(Stack s) {
    Stack<Object> temp = new Stack<>();
    Integer size = s.size();

    for (Integer i=0; i<size; i++) {
      while (s.size() > 0) {
        Object element = s.pop();

        if (element instanceof Integer) {
          temp.push(element);
        } else {
          Integer o1 = (Integer) temp.pop();
          Integer o2 = (Integer) temp.pop();
          Integer result = null;

          if (element == "+") {
            result = o1 + o2;
          } else if (element == "-") {
            result = o1 - o2;
          } else if (element == "\\") {
            result = o1 / o2;
          } else if (element == "x") {
            result = o1 * o2;
          }

          temp.push(result);
        }
      }
    }
    Integer final_result = (Integer) temp.pop();
    return final_result;
  }

  public static void main(String args[]) {
    BinaryTree bt = new BinaryTree();

    // Populate the binary tree
    BinaryTree.Node<String> r0 = new BinaryTree.Node<>("+");
    bt.rightInsert(r0, null);

    BinaryTree.Node<String> r1 = new BinaryTree.Node<>("x");
    bt.leftInsert(r1, r0);

    BinaryTree.Node<String> r2 = new BinaryTree.Node<>("x");
    bt.rightInsert(r2, r0);

    BinaryTree.Node<Integer> r3 = new BinaryTree.Node<>(2);
    bt.leftInsert(r3, r1);

    BinaryTree.Node<String> r4 = new BinaryTree.Node<>("-");
    bt.rightInsert(r4, r1);

    BinaryTree.Node<Integer> r5 = new BinaryTree.Node<>(5);
    bt.leftInsert(r5, r4);

    BinaryTree.Node<Integer> r6 = new BinaryTree.Node<>(1);
    bt.rightInsert(r6, r4);

    BinaryTree.Node<Integer> r7 = new BinaryTree.Node<>(3);
    bt.leftInsert(r7, r2);

    BinaryTree.Node<Integer> r8 = new BinaryTree.Node<>(2);
    bt.rightInsert(r8, r2);

    bt.printTree();

    Stack s = createStack(bt);
    System.out.println(Arrays.toString(s.toArray()));

    Integer result = solvePostfix(s);
    System.out.println(result);
  }
}
