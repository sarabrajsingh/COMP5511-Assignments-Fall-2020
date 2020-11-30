/* GOAL: Implement a system that indexes and provides search features for the GNIS file.

The system needs to build and maintain several in memory index data structures to support the following operations:
- Retreive records matching given geographic coordinates
- Retrieve records matching given unique record ID
- Retrieve records that fall within a given geographic region */

package assignment4;
import java.io.*;
import java.util.*;

public class Main {

    // a custom class that represents each unique record from the GNIS file
    private static class Record {
      private String cgndbId;
      private String geographicName;
      private String genericTerm;
      private float latitude;
      private float longitude;
      private String province;

      private Record(String l) {
        ArrayList<String> elements = new ArrayList<String>(Arrays.asList(l.split(",")));
        this.cgndbId = elements.get(0);
        this.genericTerm = elements.get(elements.size()-4);
        this.latitude = Float.parseFloat(elements.get(elements.size()-3));
        this.longitude = Float.parseFloat(elements.get(elements.size()-2));
        this.province = elements.get(elements.size()-1);

        ArrayList<String> sub = new ArrayList<String>(elements.subList(1,elements.size()-4));

        /* if geographic name contains one or more commas, multiple
        elements in the list need to be merged to get the entire
        geographic name into one string */
        if (sub.size() > 1) {
          System.out.println(this.cgndbId);
          String name = "";
          for(String s : sub) {
            name = name + s + ",";
          }
          this.geographicName = name.substring(1, name.length()-2);
        // if the geographic name does not contain any commas, get the name from index 1
        } else {
          this.geographicName = elements.get(1);
        }
      }

      private void printRecord() {
        System.out.printf("|| CGNDBID: %s ---> ", this.cgndbId);
        System.out.printf("Geographic Name: %s | ", this.geographicName);
        System.out.printf("Generic Term: %s | ", this.genericTerm);
        System.out.printf("Latitude: %s | ", this.latitude);
        System.out.printf("Longitude: %s | ", this.longitude);
        System.out.printf("Province: %s ||", this.province);
        System.out.println();
      }
    }

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
    BinarySearchTree<Record> bs = new BinarySearchTree<>();
    for (Record r : csv) {
      bs.insert(r.cgndbId, r);
    }

    System.out.println(bs.root);
    bs.search("KAFIM");

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
