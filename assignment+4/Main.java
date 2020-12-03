/* GOAL: Implement a system that indexes and provides search features for the GNIS file.

The system needs to build and maintain several in memory index data structures to support the following operations:
- Retreive records matching given geographic coordinates
- Retrieve records matching given unique record ID
- Retrieve records that fall within a given geographic region */

package assignment4;
import java.io.*;
import java.util.*;

public class Main {

    // returns a BufferedReader object for a given file
    public static BufferedReader fileReader(String filename) throws IOException {
      File file = new File(filename);
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);
      return br;
    }

    // counts the number of rows in a given csv
    public static int lineCounter(String filename) throws IOException {
      BufferedReader br = fileReader(filename);
      int lineCount = 0;
      while (br.readLine() != null) {
        lineCount++;
      }
      return lineCount;
    }

    // reads a given csv file line by line and returns an array of records
    public static Record[] readCSV(String filename, int lineCount) throws IOException {
      BufferedReader br = fileReader(filename);
      Record[] records = new Record[lineCount-1];
      // skip first row
      br.readLine();
      String row;
      int c = 0;
      while((row = br.readLine()) != null) {
        Record r = new Record(row);
        records[c] = r;
        // print every 10000th record for demonstration purposes
        if (c%10000 == 0) {
          r.printRecord();
        }
        c++;
      }
      br.close();
      return records;
    }

  // reads the .txt file that stores the queries
  public static Query readQueries(String filename) throws IOException {
    BufferedReader br = fileReader(filename);
    String line;
    String[] queries = new String[6];
    int c = 0;
    while ((line = br.readLine()) != null) {
      queries[c] = line.split(":")[1];
      c++;
    }
    Query q = new Query(queries);
    q.printQuery();
    return q;
  }

  public static void main(String[] args) throws IOException {
    System.out.println("\nREADING FILE AND PRINTING SOME EXAMPLE RECORDS FOR DEMONSTRATION...");
    System.out.println("===========================\n");
    // read the file names
    String record_file = args[0];
    String query_file = args[1];
    String output_file = args[2];

    // display some info about the chosen file
    int lineCount = lineCounter(record_file);
    System.out.printf("Total records: %s \n\n", lineCount);

    Record[] csv = new Record[lineCount];
    try {
      csv = readCSV(record_file, lineCount);
    } catch(FileNotFoundException e) {
      System.out.println("File was not found.");
    }

    System.out.println("\nBUILDING IN MEMORY DATA STRUCTURES...");
    System.out.println("===========================\n");

    // create a balanced BST for ID look up
    System.out.println(">>> Creating a Binary Search Tree for ID look up");
    BinarySearchTree bs = new BinarySearchTree();
    for (Record r : csv) {
      bs.insert(r.cgndbId, r);
    }

    System.out.println(">>> Balancing the Binary Search Tree");
    int h0 = bs.height(bs.root);
    bs.root = bs.balanceTree(bs.root);
    int h1 = bs.height(bs.root);
    System.out.printf(">>> BST height before and after balancing the BST: %s vs %s\n", h0, h1);

    System.out.println(">>> Creating Inverted Indices for Geographic Name, Location and Generic Term look up");
    // create an Inverted Index for Geographic Name Lookup
    InvertedIndex inv = new InvertedIndex();
    for (Record r : csv) {
      inv.put(r.geographicName, r);
    }

    // create an Inverted Index for Generic Term Lookup
    InvertedIndex inv2 = new InvertedIndex();
    for (Record r : csv) {
      inv2.put(r.genericTerm, r);
    }

    /* create an Inverted Index for Location Lookup. Since the location
    field has a lot of null values, we need to make sure to check for nullness
    before inserting records into the Inverted Index */
    InvertedIndex inv3 = new InvertedIndex();
    for (Record r : csv) {
      if (r.location != null) {
        inv3.put(r.location, r);
      }
    }

    // create a B Tree for Lat/Long Lookup

    // execute queries
    System.out.println("\nREADING QUERIES AND SAVING RESULTS TO LOG FILE...");
    System.out.println("===========================");
    Query q = new Query();

    try {
      q = readQueries(query_file);
    } catch(FileNotFoundException e) {
      System.out.println("File was not found.");
    }

    ArrayList<String> log = new ArrayList<String>();

    // Perform the searches
    if (q.cgndbId != null) {
      for(String id : q.cgndbId) {
        System.out.printf("Querying %s: \n", id);
        String result = bs.search(id);
        log.add(result);
      }
    }

    if (q.geographicName != null) {
      for(String n : q.geographicName) {
        System.out.printf("Querying %s: \n", n);
        String result = inv.get(n);
        log.add(result);
      }
    }

    if (q.genericTerm != null) {
      for(String g : q.genericTerm) {
        System.out.printf("Querying %s: \n", g);
        String result = inv2.get(g);;
        log.add(result);
      }
    }

    if (q.location != null) {
      for(String l : q.location) {
        System.out.printf("Querying %s: \n", l);
        String result = inv3.get(l);
        log.add(result);
        }
      }

    System.out.println(log.toString());

    }
}
