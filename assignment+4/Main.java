/* A system that indexes and provides search features for the GNIS file.

The system supports the following operations:
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

  // writes the results to the output file
  public static void writeResults(ArrayList<String> queries, ArrayList<String> results, String output) throws IOException {
    FileWriter writer = new FileWriter(output);
    for(int i=0; i<results.size(); i++) {
      String row = queries.get(i) + ":\n" + results.get(i) + "\n";
      writer.write(row + System.lineSeparator());
    }
    writer.close();
  }

  // find the common ids from the results of latitude and longitude searches
  private static String findMatches(String latitudes, String longitudes) {
    String[] latitudeArray = latitudes.split("\n");
    String[] longitudeArray = longitudes.split("\n");
    // extract the cgndb id from the search results for each record
    ArrayList<String> latitudeIds = new ArrayList<>();
    for (String lat : latitudeArray) {
      String latId = lat.substring(12, 17);
      latitudeIds.add(latId);
    }
    ArrayList<String> longitudeIds = new ArrayList<>();
    for (String lon : longitudeArray) {
      String longId = lon.substring(12, 17);
      longitudeIds.add(longId);
    }
    // add the matching ids to the matches array
    ArrayList<String> matches = new ArrayList<>();
    for (int i=0; i<latitudeIds.size(); i++) {
      for (int j=0; j<longitudeIds.size(); j++) {
        String latId = latitudeIds.get(i);
        String longId = longitudeIds.get(j);

        if (latId.equals(longId)) {
          matches.add(latitudeArray[i]);
        }
      }
    }
    /* combine the final matches into a string if there are any,
    otherwise result a not found string */
    String finalResult = "";
    if (!matches.isEmpty()) {
      for (String m: matches) {
        finalResult = finalResult + m + "\n";
      }
      return finalResult;
    } else {
      String o = "Record was not found";
      return o;
    }
  }

  public static void main(String[] args) throws IOException {
    System.out.println("\nREADING FILE AND PRINTING SOME EXAMPLE RECORDS FOR DEMONSTRATION...");
    System.out.println("===========================\n");
    // read the file names
    String record = "";
    String query = "";
    String output = "";
    try {
      record = args[0];
      query = args[1];
      output = args[2];
    } catch(ArrayIndexOutOfBoundsException e) {
      System.out.println("Did not receive the required file names. \nExiting program.\n");
      System.exit(0);
    }

    // display some info about the chosen file
    int lineCount = lineCounter(record);
    System.out.printf("Total records: %s \n\n", lineCount);

    Record[] csv = new Record[lineCount];
    try {
      csv = readCSV(record, lineCount);
    } catch(FileNotFoundException e) {
      System.out.println("File was not found.");
    }

    System.out.println("\nBUILDING IN MEMORY DATA STRUCTURES...");
    System.out.println("===========================\n");

    // create a balanced Binary Search Tree for ID look up
    System.out.println(">>> Creating a Binary Search Tree for ID look up");
    BinarySearchTree<String> bs = new BinarySearchTree<>();
    for (Record r : csv) {
      bs.insert(r.cgndbId, r);
    }

    System.out.println(">>> Balancing the Binary Search Tree");
    int h0 = bs.height(bs.root);
    bs.root = bs.balanceTree(bs.root);
    int h1 = bs.height(bs.root);
    System.out.printf(">>> Height before and after balancing the BST: %s vs %s\n", h0, h1);

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

    /* Create a BST for Lat and Long Lookup. We need to create a
    separate Binary Search Tree for each.*/
    System.out.println(">>> Creating Binary Search Trees for Latitude and Longitude look up");
    BinarySearchTree<Float> bs2 = new BinarySearchTree<>();
    for (Record r : csv) {
      bs2.insert(r.latitude, r);
    }

    BinarySearchTree<Float> bs3 = new BinarySearchTree<>();
    for (Record r : csv) {
      bs3.insert(r.longitude, r);
    }

    System.out.println(">>> Balancing the Binary Search Trees");
    int h2 = bs2.height(bs2.root);
    bs2.root = bs2.balanceTree(bs2.root);
    int h3 = bs2.height(bs2.root);
    System.out.printf(">>> Height before and after balancing the Latitude BST: %s vs %s\n", h2, h3);
    int h4 = bs3.height(bs3.root);
    bs3.root = bs3.balanceTree(bs3.root);
    int h5 = bs3.height(bs3.root);
    System.out.printf(">>> Height before and after balancing the Longitude BST: %s vs %s\n", h4, h5);

    // execute queries
    System.out.println("\nREADING QUERIES AND SAVING RESULTS TO LOG FILE...");
    System.out.println("===========================\n");
    Query q = new Query();

    try {
      q = readQueries(query);
    } catch(FileNotFoundException e) {
      System.out.println("File was not found.");
    }

    ArrayList<String> results = new ArrayList<String>();
    ArrayList<String> queries = new ArrayList<String>();

    // Perform the searches
    if (q.cgndbId != null) {
      for(String id : q.cgndbId) {
        String result = bs.search(id);
        results.add(result);
        queries.add(id);
      }
    }

    if (q.geographicName != null) {
      for(String n : q.geographicName) {
        String result = inv.get(n);
        results.add(result);
        queries.add(n);
      }
    }

    if (q.genericTerm != null) {
      for(String g : q.genericTerm) {
        String result = inv2.get(g);
        results.add(result);
        queries.add(g);
      }
    }

    if (q.location != null) {
      for(String l : q.location) {
        String result = inv3.get(l);
        results.add(result);
        queries.add(l);
      }
    }

    /* if the number of latitude and longitudes provided to query are not
    the same, the program will not execute search for latitude and longitude */
    if (q.latitude != null && q.longitude != null) {
      if (q.latitude.size() == q.longitude.size()) {
        for(int i=0; i<q.latitude.size(); i++) {
          Float lat = q.latitude.get(i);
          Float lon = q.longitude.get(i);
          String result1 = bs2.search(lat);
          String result2 = bs3.search(lon);
          String matches = findMatches(result1, result2);
          results.add(matches);
          queries.add(lat + ", " + lon);
        }
      }
    }

    writeResults(queries, results, output);
  }
}
