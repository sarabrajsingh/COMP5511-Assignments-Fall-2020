/* A system that indexes and provides search features for the GNIS file.

The system supports the following operations:
- Retreive records matching given geographic coordinates
- Retrieve records matching given unique record ID
- Retrieve records that fall within a given geographic region */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Main {

  public static Integer NUM_RECORDS = null; // globally available # of records count

  // reads a given csv file line by line and returns an array of records
  public static ArrayList<Record> readCSV(String filename) {
    ArrayList<Record> records = new ArrayList<Record>();
    BufferedReader br = null;
    String line = null;
    int counter = 0; // we'll use this counter to
    try {
      br = new BufferedReader(new FileReader(filename));
      br.readLine(); // skip the first line, usually the header in CSV files
      while ((line = br.readLine()) != null) {
        // some logic to handle bad inputs from CSV, like "," in the geographicName
        String[] input = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // use this regex to split the incoming line
                                                                        // from the CSV file
        // build a record object based on CSV input
        String cgndbID = null;
        String geographicName = null;
        String geographicTerm = null;
        Double latitude = null;
        Double longitude = null;
        String location = null;
        String province = null;

        for (int i = 0; i < input.length; i++) {
          try {
            switch (i) {
              case 0:
                cgndbID = input[0];
                break;
              case 1:
                geographicName = input[1];
                break;
              case 4:
                geographicTerm = input[4];
                break;
              case 8:
                latitude = Double.parseDouble(input[8]);
                break;
              case 9:
                longitude = Double.parseDouble(input[9]);
                break;
              case 10:
                location = input[10];
              case 11:
                province = input[11];
            }
          } catch (NumberFormatException e) {
            continue;
          }
        }

        Record record = new Record(cgndbID, geographicName, geographicTerm, latitude, longitude, location, province);
        if (counter % 10000 == 0 && counter > 0) {
          System.out.println(record);
        }
        records.add(record);
        counter++;
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(0);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println("\nTotal Records: " + counter);
    // update static variable NUM_RECORDS for use in other classes
    Main.NUM_RECORDS = counter;
    return records;
  }

  // // reads the .txt file that stores the queries
  public static Query readQueries(String filename) {
    BufferedReader br = null;
    String[] queries = new String[6]; // we're always expecting 6 query params
    try {
      br = new BufferedReader(new FileReader(filename));
      String line;
      int c = 0;
      while ((line = br.readLine()) != null) {
        queries[c] = line.split(":")[1]; // ditch the first index
        c++;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    Query q = new Query(queries);
    return q;
  }

  // writes the results to the output file
  private static void writeResults(StringBuilder sb, String filename) {
    BufferedWriter bw = null;
    try {
      bw = new BufferedWriter(new FileWriter(filename));
      bw.append(sb);
      System.out.println(">>> Successfully wrote results to [" + filename + "]");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (bw != null) {
        try {
          bw.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static void main(String[] args) throws IOException {
    System.out.println("\nREADING FILE AND PRINTING SOME EXAMPLE RECORDS FOR DEMONSTRATION...");
    System.out.println("===========================\n");
    // read arguments from the command line
    String csvFile = "";
    String query = "";
    String output = "";
    try {
      csvFile = args[0];
      query = args[1];
      output = args[2];
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Did not receive the required file names. \nExiting program.\n");
      System.exit(0);
    }

    // populate masterRecords arrayList with CSV contents
    ArrayList<Record> masterRecords = readCSV(csvFile);

    System.out.println("\nBUILDING IN MEMORY DATA STRUCTURES...");
    System.out.println("===========================\n");

    // // create a balanced Binary Search Tree for ID look up
    System.out.println(">>> Creating a Binary Search Tree for ID look up");
    BinarySearchTree<Record> bs = new BinarySearchTree<Record>();
    for (Record r : masterRecords) {
      bs.insert(r);
    }

    System.out.println(">>> Balancing the Binary Search Tree");
    int h0 = bs.height(bs.root);
    bs.root = bs.balanceTree(bs.root);
    int h1 = bs.height(bs.root);
    System.out.printf(">>> Height before and after balancing the BST: %s vs %s\n", h0, h1);

    System.out.println(">>> Creating Inverted Indices for Geographic Name, Location and Generic Term look up");

    // create an Inverted Index for Geographic Name Lookup
    InvertedIndex<String> inv1 = new InvertedIndex<String>();
    for (Record r : masterRecords) {
      inv1.put(r.getGeographicName(), r.getCgndbId());
    }

    // create an Inverted Index for Generic Term Lookup
    InvertedIndex<String> inv2 = new InvertedIndex<String>();
    for (Record r : masterRecords) {
      inv2.put(r.getGenericTerm(), r.getCgndbId());
    }

    /*
     * create an Inverted Index for Location Lookup. Since the location field has a
     * lot of null values, we need to make sure to check for nullness before
     * inserting records into the Inverted Index
     */
    InvertedIndex<String> inv3 = new InvertedIndex<String>();
    for (Record r : masterRecords) {
      if (r.getLocation() != null) {
        inv3.put(r.getLocation(), r.getCgndbId());
      }
    }

    // /*
    // * Create a BST for Lat and Long Lookup. We need to create a separate Binary
    // * Search Tree for each.
    // */
    System.out.println(">>> Creating Binary Search Trees for Latitude and Longitude look up");
    BinarySearchTree<Record> bs2 = new BinarySearchTree<Record>();
    Record.SORT_BY = "LATITUDE";
    for (Record r : masterRecords) {
      bs2.insert(r); // BST sorted by LATITUDE
    }

    BinarySearchTree<Record> bs3 = new BinarySearchTree<Record>();
    Record.SORT_BY = "LONGITUDE";
    for (Record r : masterRecords) {
      bs3.insert(r); // BST sorted by LONGITUDE
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

    Query q = readQueries(query);
    System.out.println(q);

    StringBuilder sb = new StringBuilder();

    // Perform the searches and start building output to log file
    // search for CGNDBIDs

    if (q.getCgndbId() != null) {
      if (q.getCgndbId().size() > 0) {
        for (String id : q.getCgndbId()) {
          try {
            Record.SORT_BY = "CGNDBID";
            Record found = bs.search(new Record(id)); // we want the errors to throw first before we write anything to
                                                      // the log file, hence the bs.search() method
            sb.append(found.getCgndbId() + ":").append(System.lineSeparator()).append(found)
                .append(System.lineSeparator()).append(System.lineSeparator());
          } catch (NoSuchElementException e) {
            sb.append(id + ":").append(System.lineSeparator()).append("Record was not found")
                .append(System.lineSeparator()).append(System.lineSeparator());
            System.out.println(">>> Could not find " + id + ", moving onto next ID");
            continue;
          }
        }
      }
    } else {
      System.out.println(">>> CGNDBID not present in Query. Moving onto next Query object");
    }

    sb.append(System.lineSeparator());
    System.out.println();

    // search for Geographic Names
    if (q.getGeographicName() != null) {
      if (q.getGeographicName().size() > 0) {
        for (String n : q.getGeographicName()) {
          try {
            Record.SORT_BY = "CGNDBID";
            ArrayList<String> results = inv1.get(n); // use BST to search for the record of every ID found by the
                                                     // inverted index
            sb.append(n + ":").append(System.lineSeparator());
            for (int i = 0; i < results.size(); i++) {
              sb.append(bs.search(new Record(results.get(i)))).append(System.lineSeparator());
            }
          } catch (NoSuchElementException e) {
            sb.append(n + ":").append(System.lineSeparator()).append("Term was not found")
                .append(System.lineSeparator()).append(System.lineSeparator());
            System.out.println(">>> could not find " + n + ", moving onto next Geographic Name");
          }
        }
      }
    } else {
      System.out.println(">>> Geographic Name not present in Query. Moving onto next Query object");
    }

    sb.append(System.lineSeparator());
    System.out.println();

    // search for generic terms
    if (q.getGenericTerm() != null) {
      if (q.getGenericTerm().size() > 0) {
        for (String g : q.getGenericTerm()) {
          try {
            Record.SORT_BY = "CGNDBID";
            ArrayList<String> results = inv1.get(g);
            sb.append(g + ":").append(System.lineSeparator());
            for (int i = 0; i < results.size(); i++) {
              sb.append(bs.search(new Record(results.get(i)))).append(System.lineSeparator());
            }
          } catch (NoSuchElementException e) {
            sb.append(g + ":").append(System.lineSeparator()).append("Term was not found")
                .append(System.lineSeparator());
            System.out.println(">>> could not find " + g + ", moving onto next Generic Term");
          }
        }
      }
    } else {
      System.out.println(">>> Generic Terms not present in Query. Moving onto next Query object");
    }

    sb.append(System.lineSeparator());

    // search for locations
    if (q.getLocation() != null) {
      if (q.getLocation().size() > 0) {
        for (String l : q.getLocation()) {
          try {
            Record.SORT_BY = "CGNDBID";
            ArrayList<String> results = inv3.get(l);
            sb.append(l + ":").append(System.lineSeparator());
            for (int i = 0; i < results.size(); i++) {
              sb.append(bs.search(new Record(results.get(i)))).append(System.lineSeparator());
            }
          } catch (NoSuchElementException e) {
            sb.append(l + ":").append(System.lineSeparator()).append("Location was not found")
                .append(System.lineSeparator());
            System.out.println(">>> could not find " + l + ", moving onto next Location");
          }
        }
      }
    } else {
      System.out.println(">>> Locations not present in Query. Moving onto next Query object");
    }

    sb.append(System.lineSeparator());
    System.out.println();

    // latitude lookups
    if (q.getLatitude() != null && q.getLongitude() != null) {
      if (q.getLatitude().size() > 0 && q.getLongitude().size() > 0) {
        for (Double lat : q.getLatitude()) {
          try {
            Record.SORT_BY = "LATITUDE";
            Record found = bs2.search(new Record(lat));
            sb.append("Latitude [").append(found.getLatitude()).append("]:").append(System.lineSeparator())
                .append(found).append(System.lineSeparator());
          } catch (NoSuchElementException e) {
            sb.append(lat + ":").append(System.lineSeparator()).append("Latitude was not found")
                .append(System.lineSeparator());
            System.out.println(">>> could not find " + lat + ", moving onto next Latitude");
            continue;
          }
        }
      }
      // dump StringBuilder to an output
      writeResults(sb, output);
    }
  }
}