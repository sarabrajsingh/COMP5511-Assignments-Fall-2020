package assignment4;
import java.util.*;

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
  }

  /* Represents internal nodes in the B+ Tree. Internal nodes dont store pointers to the
  actual Record objects. They point to children nodes. */
  private static class InternalNode extends Node {
    private ArrayList<Node> children;
    private int maxChildren;
    private int minChildren;

    private InternalNode(Float[] keys) {
      this.maxChildren = order;
      this.minChildren = (int) Math.ceil(order/2.0);
      this.keys = new ArrayList<>();
      this.children = new ArrayList<>();
    }
  }

  /* Represents leaf nodes in the B+ Tree. These store pointers to Records
  and to their sibling leaf nodes. */
  private static class LeafNode extends Node {

    // Stores an array of records that corresponds to a single key
    private class LeafNodeEntry {
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

    private ArrayList<LeafNodeEntry> values;
    private LeafNode next;

    // constructor for an empty leaf
    private LeafNode() {
      this.keys = new ArrayList<>();
      this.values = new ArrayList<>();
    }

    // constructor for a new leaf with one record
    private LeafNode(Float key, Record record) {
      this.keys = new ArrayList<>();
      this.keys.add(key);
      this.values = new ArrayList<>();
      LeafNodeEntry l = new LeafNodeEntry(record);
      this.values.add(l);
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

    // check if a leaf is full
    private boolean isFull() {
      if(this.keys.size() < this.maxKeys) {
        return false;
      } else {
        return true;
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

  public LeafNode findLeaf(Float key) {
    Node n = this.root;
    // if root is leaf
    if (n instanceof LeafNode) {
      return this.root;
    /* if root is an internal node, traverse its children
    one by one until we reach the right leaf */
    } else {
      while (n instanceof InternalNode) {
        for (Node child : n.children) {
          max = Collections.max(child.keys);
          if (key < max) 
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
