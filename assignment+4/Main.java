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
    public static ArrayList<Record> readCSV(String filename) throws IOException {
      File file = new File(filename);
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);

      // initialize an arraylist that will store the collection of all record objects
      ArrayList<Record> records = new ArrayList<>();

      // skip first row
      br.readLine();
      String row;
      int c = 0;
      while((row = br.readLine()) != null) {
        Record r = new Record(row);
        records.add(r);
        // print every 5000th record for demonstration purposes
        if (c%5000 == 0) {
          r.printRecord();
        }
        c++;
      }
      br.close();

      return records;
    }

  public static void main(String[] args) {
    System.out.println("READING FILE AND PRINTING SOME EXAMPLE RECORDS FOR DEMONSTRATION...");
    System.out.println("===========================");

    ArrayList<Record> csv = new ArrayList<>();
    try {
      csv = readCSV("cgn_canada_csv_eng.csv");
    } catch(FileNotFoundException e) {
      System.out.println("File was not found.");
    } catch(IOException e) {
      e.printStackTrace();
    }

    // Create BST for ID look up
    BinarySearchTree bs = new BinarySearchTree();
    for (Record r : csv) {
      bs.insert(r.cgndbId, r);
    }

    bs.search("FDLAP");

    System.out.println();
    System.out.println("READING QUERIES...");
    System.out.println("===========================");

    try {
      File file = new File("queries.txt");
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

  }
}
