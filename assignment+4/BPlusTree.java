import java.util.ArrayList;
import java.util.Collections;

public class BPlusTree {
  public static int order;
  public Node root;

  // constructor for the B+ Tree object
  public BPlusTree(int order) {
      this.order = order;
      this.root = null;
  }

  // parent class of internal and leaf nodes
  public static class Node {
    public ArrayList<Float> keys;
    public static int maxKeys;
    public static int minKeys;

    public Node() {
      this.maxKeys = order - 1;
      this.minKeys = (int) Math.ceil(order/2.0) - 1;
    }

    // check if a node is full
    private boolean isFull() {
      if(this.keys.size() < this.maxKeys) {
        return false;
      } else {
        return true;
      }
    }
  }

  /* Represents internal nodes in the B+ Tree. Internal nodes dont store pointers to the
  actual Record objects. They point to children nodes. */
  private static class InternalNode extends Node {
    private ArrayList<Node> children;
    private InternalNode parent;
    private int maxChildren;
    private int minChildren;

    // constructor for an empty Internal Node
    private InternalNode() {
      this.maxChildren = order;
      this.minChildren = (int) Math.ceil(order/2.0);
      this.keys = new ArrayList<>();
      this.children = new ArrayList<>();
      this.parent = null;
    }

    // constructor for an Internal Node with one key
    private InternalNode(Float key) {
      this.maxChildren = order;
      this.minChildren = (int) Math.ceil(order/2.0);
      this.keys = new ArrayList<>();
      keys.add(key);
      this.children = new ArrayList<>();
      this.parent = null;
    }

    // constructor for an Internal Node with multiple keys
    private InternalNode(ArrayList<Float> keys) {
      this.maxChildren = order;
      this.minChildren = (int) Math.ceil(order/2.0);
      this.keys = keys;
      this.children = new ArrayList<>();
      this.parent = null;
    }

    // insert key in the appropriate position in the internal node
    private void insert(Float key) {
      // if the node is not empty, find where to insert it
      if (!keys.isEmpty()) {
        // add new key if it doesnt exist in the node already
        if (!keys.contains(key)) {
          // add key
          keys.add(key);
          // sort keys
          Collections.sort(keys);
        // if key exists in the node already, do nothing
        } else {
          return;
        }
      // if the node is empty, insert key in first position
      } else {
        this.keys.add(key);
      }
  	}
  }

  /* Represents leaf nodes in the B+ Tree. These store pointers to Records
  and to their sibling leaf nodes. */
  private static class LeafNode extends Node {

    private ArrayList<LeafNodeEntry> values;
    private LeafNode next;
    private InternalNode parent;

    // constructor for an empty leaf
    private LeafNode() {
      this.keys = new ArrayList<>();
      this.values = new ArrayList<>();
      this.parent = null;
    }

    // constructor for a new leaf with one record
    private LeafNode(Float key, Record record) {
      this.keys = new ArrayList<>();
      this.keys.add(key);
      this.values = new ArrayList<>();
      LeafNodeEntry l = new LeafNodeEntry(record);
      this.values.add(l);
      this.parent = null;
    }

    // constructor for a new leaf with multiple records
    private LeafNode(ArrayList<Float> keys, ArrayList<LeafNodeEntry> values) {
      this.keys = keys;
      this.values = values;
      this.parent = null;
    }

    // insert key and record in the appropriate position in the leaf node
    private void insert(Float key, Record record) {
      // if the leaf is not empty, find where to insert it
      if (!keys.isEmpty()) {
        // add new key if it doesnt exist in the leaf already
        if (!keys.contains(key)) {
          // add key
          keys.add(key);
          // add record
          LeafNodeEntry newLeaf0 = new LeafNodeEntry(record);
          this.values.add(newLeaf0);
          // preserve current order - soon to become old order
          ArrayList<Float> old = new ArrayList<>();
          for (int i = 0; i < keys.size(); i++) {
            old.add(keys.get(i));
          }
          // sort keys
          Collections.sort(keys);
          // align order of keys to order of values
          ArrayList<LeafNodeEntry> newValues = new ArrayList<>();
          for (Float k : keys) {
            int newIndex = keys.indexOf(k);
            int oldIndex = old.indexOf(k);
            newValues.add(newIndex, this.values.get(oldIndex));
          }
          this.values = newValues;
        /* if key exists in the leaf already, add the record to its list
        of pointers */
        } else {
          int j = keys.indexOf(key);
          LeafNodeEntry existingLeaf = values.get(j);
          existingLeaf.insert(record);
        }
      // if the leaf is empty, insert key in first position
      } else {
        this.keys.add(key);
        LeafNodeEntry newLeaf1 = new LeafNodeEntry(record);
        this.values.add(newLeaf1);
      }
  	}

    // print the contents of a given leaf
    private void printLeaf() {
      for (int i=0; i<this.keys.size(); i++) {
        System.out.printf("%s --> ", keys.get(i));
        this.values.get(i).printLeafNodeEntry();
      }
    }
  }

  /* Stores an array of records that corresponds to a single key
  and value or values */
  private static class LeafNodeEntry {
    private ArrayList<Record> records;

    // constructor of a LeafNodeEntry
    private LeafNodeEntry(Record r) {
      this.records = new ArrayList<>();
      this.records.add(r);
    }

    // insert a record to an entry
    private void insert(Record r) {
      this.records.add(r);
    }

    private void printLeafNodeEntry() {
      for (Record r : records) {
        r.printRecord();
      }
    }
  }

  public LeafNode findLeaf(Float key) {
    Node n = this.root;
    // if root is leaf, return leaf
    if (n instanceof LeafNode) {
      return (LeafNode) this.root;
    /* if root is an internal node, traverse its children
    one by one until we reach the right leaf */
    } else {
      while (n instanceof InternalNode) {
        for (Node child : n.children) {
          Float max = Collections.max(child.keys);
          if (key > max) {
            continue;
          } else {
            n = child;
          }
        }
      }
      return (LeafNode) n;
    }
  }

  // splits a given Node from the midpoint
  public Node split(Node n) {
    int mid = (int) Math.ceil((this.order - 1) / 2.0);
    if (n instanceof LeafNode) {
      LeafNode l = (LeafNode) l;
      // separate the first half of the leaf node
      ArrayList<Float> firstHalfKeys = new ArrayList<Float>(l.keys.subList(0, mid-1));
      ArrayList<LeafNodeEntry> firstHalfValues = new ArrayList<LeafNodeEntry>(l.values.subList(0, mid-1));
      LeafNode p0 = new LeafNode(firstHalfKeys, firstHalfValues);
      // separate the second half of the lead node
      ArrayList<Float> secondHalfKeys = new ArrayList<Float>(n.keys.subList(mid, l.keys.size()-1));
      ArrayList<LeafNodeEntry> secondHalfValues = new ArrayList<LeafNodeEntry>(l.values.subList(mid, n.keys.size()-1));
      LeafNode p1 = new LeafNode(firstHalfKeys, firstHalfValues);
      // create an internal node from the mid point if n doesnt have a parent
      if (l.parent == null) {
        InternalNode p3 = new InternalNode(l.keys.get(mid));
        p3.children.add(p0);
        p3.children.add(p1);
        p0.parent = p3;
        p1.parent = p3;
      // if n has a parent already, check if it has space. if not, split it too
      } else {
        if (!l.parent.isFull()) {
          l.parent.insert(l.keys.get(mid));
        // if the parent doesnt have space, recursively call the function again
        } else {
          split(l.parent);
        }
      }
    // if n is an Internal Node
    } else {
      // if no parent, create a new parent and assign the new nodes as its children
      if (n.parent == null) {
        ArrayList<Float> firstHalfKeys = new ArrayList<Float>(n.keys.subList(0, mid-1));
        ArrayList<Float> secondHalfKeys = new ArrayList<Float>(n.keys.subList(mid-1, n.keys.size()-1));
        InternalNode p4 = new InternalNode(n.keys.get(mid));
        InternalNode p5 = new InternalNode(firstHalfKeys);
        InternalNode p6 = new InternalNode(secondHalfKeys);
        p4.children.add(p5);
        p4.children.add(p6);
        p5.parent = p4;
        p6.parent = p4;
      // if there is already a parent, see if it has space
      } else {
        if (!n.parent.isFull()) {
          n.parent.insert(mid);
        // if the parent doesnt have space, recursively call the function again
        } else {
          split(n.parent);
        }
      }
    }
  }

  // insert a record in the B+ Tree
  public void insert(Float key, Record value) {
    // create a brand new B+ Tree if root is null
    if(this.root == null) {
      leaf = new LeafNode(key, value);
      this.root = leaf;
      return;
    // if root is not null, find where key needs to be inserted
    } else {
      leaf = findLeaf(key);
      // if leaf is full, we need to split the node
      if (leaf.isFull()) {
        tempLeaf = new LeafNode(key, value);
      // if no split is required, insert the key in leaf
      } else {
        leaf.insert(key, value);
      }
    }
  }

  public static void main(String[] args) {
    System.out.println("Succesfully created a B+ Tree.");

    // unit tests
    Record e1 = new Record("EJEIZ,Lac Lucie,Lake,46.987778,-75.38472,Quebec");
    Record e2 = new Record("EFOWB,Rapides Boisvert,Rapids,46.6175,-74.263336,Quebec");
    Record e3 = new Record("FDLAP,Mud Lake,Lake,48.161366,-79.93589,Ontario");
    Record e4 = new Record("FBUEQ,Keswil Creek,Creek,46.092213,-78.97416,Ontario");
    Record e5 = new Record("ABWNT,Coopers Head,Head,47.338722,-53.90362,Newfoundland and Labrador");

    BPlusTree bpt = new BPlusTree(4);
    Float l1 = Float.parseFloat("47.338722");
    Float l2 = Float.parseFloat("46.092213");
    Float l3 = Float.parseFloat("43.092213");
    Float l4 = Float.parseFloat("42.092213");
    Float l5 = Float.parseFloat("47.338722");

    LeafNode n = new LeafNode();
    n.insert(l1, e1);
    n.printLeaf();
    System.out.println();

    n.insert(l3, e2);
    n.printLeaf();
    System.out.println();

    n.insert(l2, e3);
    n.printLeaf();
    System.out.println();

    n.insert(l4, e3);
    n.printLeaf();
    System.out.println();

    n.insert(l5, e3);
    n.printLeaf();
    System.out.println();
  }
}
