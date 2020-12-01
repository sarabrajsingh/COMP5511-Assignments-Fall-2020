/* GOAL: Implement a system that indexes and provides search features for the GNIS file.

The system needs to build and maintain several in memory index data structures to support the following operations:
- Retreive records matching given geographic coordinates
- Retrieve records matching given unique record ID
- Retrieve records that fall within a given geographic region */

package assignment4;
import java.io.*;
import java.util.*;

public class Main {

    // reads a given csv file line by line, returns an arraylist of records
    public static int lineCounter(String filename) throws IOException {
      File file = new File(filename);
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);

      int lineCount = 0;
      while (br.readLine() != null) {
        lineCount++;
      }
      return lineCount;
    }

    public static Record[] readCSV(String filename, int lineCount) throws IOException {
      File file = new File(filename);
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);

      // initialize an array that will store the collection of all record objects
      Record[] records = new Record[lineCount-1];

      // skip first row
      br.readLine();
      String row;
      int c = 0;

      while((row = br.readLine()) != null) {
        Record r = new Record(row);
        records[c] = r;

        // print every 1000th record for demonstration purposes
        if (c%1000 == 0) {
          r.printRecord();
        }
        c++;
      }
      br.close();

      return records;
    }

  public static void main(String[] args) throws IOException {
    System.out.println("READING FILE AND PRINTING SOME EXAMPLE RECORDS FOR DEMONSTRATION...");
    // choose the file to work with
    String filename = "data/cgn_yt_csv_eng.csv";

    // count lines and instantiate a record array of the same size
    int lineCount = lineCounter(filename);
    System.out.printf("File chosen: %s \n", filename.split("/")[1]);
    System.out.printf("Total records: %s \n", lineCount);
    Record[] csv = new Record[lineCount];
    System.out.println("===========================");

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

    // create a B Tree for Lat/Long Lookup

    System.out.println();
    System.out.println("READING QUERIES...");
    System.out.println("===========================");

    try {
      File file = new File("log_files/queries.txt");
      Scanner sc = new Scanner(file);
      sc.useDelimiter("\n");
      System.out.println(sc.next());
      while(sc.hasNext()){
        System.out.println(sc.next());
      }
      sc.close();
    } catch(FileNotFoundException e) {
      System.out.println("File was not found.");
    }

    // Perform the searches

  }
}
