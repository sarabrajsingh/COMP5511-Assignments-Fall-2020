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
    System.out.println("===========================");
    // choose the file to work with
    String filename = "data/cgn_on_csv_eng.csv";
    // display some info about the chosen file
    int lineCount = lineCounter(filename);
    System.out.printf("\nFile chosen: %s \n", filename.split("/")[1]);
    System.out.printf("Total records: %s \n\n", lineCount);

    Record[] csv = new Record[lineCount];
    try {
      csv = readCSV(filename, lineCount);
    } catch(FileNotFoundException e) {
      System.out.println("File was not found.");
    }

    // create BST for ID look up
    BinarySearchTree bs = new BinarySearchTree();
    for (Record r : csv) {
      bs.insert(r.cgndbId, r);
    }

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

    // create an Inverted Index for Location Lookup
    InvertedIndex inv3 = new InvertedIndex();
    for (Record r : csv) {
      inv3.put(r.location, r);
    }

    // create a B Tree for Lat/Long Lookup
    System.out.println("\nREADING QUERIES AND SAVING RESULTS TO LOG FILE...");
    System.out.println("===========================");
    Query q = new Query();

    try {
      q = readQueries("log_files/queries.txt");
    } catch(FileNotFoundException e) {
      System.out.println("File was not found.");
    }

    // Perform the searches
    if (q.cgndbId != null) {
      for(String id : q.cgndbId) {
        System.out.printf("Querying %s: ", id);
        bs.search(id);
      }
    }

    if (q.geographicName != null) {
      for(String n : q.geographicName) {
        System.out.printf("Querying %s: ", n);
        inv.get(n);
      }
    }

    if (q.genericTerm != null) {
      for(String g : q.genericTerm) {
        System.out.printf("Querying %s: ", g);
        inv2.get(g);
      }
    }

    if (q.location != null) {
      for(String l : q.location) {
        System.out.printf("Querying %s: ", l);
        inv3.get(l);
      }
    }
  }
}
