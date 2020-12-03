// a custom class that represents a given query
package assignment4;
import java.util.*;

public class Query {
  public ArrayList<String> cgndbId = new ArrayList<>();
  public ArrayList<String> geographicName = new ArrayList<>();
  public ArrayList<String> genericTerm = new ArrayList<>();
  public ArrayList<Float> latitude = new ArrayList<>();
  public ArrayList<Float> longitude = new ArrayList<>();
  public ArrayList<String> location = new ArrayList<>();

  // constructor an empty Query object, for declaration
  public Query() {
    this.cgndbId = null;
    this.genericTerm = null;
    this.latitude = null;
    this.longitude = null;
    this.location = null;
  }

  // constructor for Query object
  public Query(String[] query) {

    for(String id : query[0].split(",")) {
      if (!id.trim().equals("None"))  {
        this.cgndbId.add(id.trim());
      } else {
        this.cgndbId = null;
      }
    }

    for(String n : query[1].split(",")) {
      if (!n.trim().equals("None"))  {
        this.geographicName.add(n.trim());
      } else {
        this.geographicName = null;
      }
    }

    for(String t : query[2].split(",")) {
      if (!t.trim().equals("None")) {
        System.out.println(t.trim());
        this.genericTerm.add(t.trim());
      } else {
        this.genericTerm = null;
      }
    }

    for(String f : query[3].split(",")) {
      if (!f.trim().equals("None"))  {
        Float l = Float.parseFloat(f.trim());
        this.latitude.add(l);
      } else {
        this.latitude = null;
      }
    }

    for(String f : query[4].split(",")) {
      if (!f.trim().equals("None"))  {
        Float l = Float.parseFloat(f.trim());
        this.longitude.add(l);
      } else {
        this.longitude = null;
      }
    }

    for(String l : query[5].split(",")) {
      if (!l.trim().equals("None"))  {
        this.location.add(l.trim());
      } else {
        this.location = null;
      }
    }
  }

  // print the info of a query
  public void printQuery() {
    if (this.cgndbId != null) {
      System.out.printf(">>> CGNDBID(s): %s \n", this.cgndbId);
    }

    if (this.geographicName != null) {
      System.out.printf(">>> Geographic Name(s): %s \n", this.geographicName);
    }

    if (this.genericTerm != null) {
      System.out.printf(">>> Generic Term(s): %s \n", this.genericTerm);
    }

    if (this.latitude != null & this.longitude != null) {
      System.out.printf(">>> Latitude(s) & Longitude(s): [%s, %s]\n", this.latitude, this.longitude);
    }

    if (this.location != null) {
      System.out.printf(">>> Location(s): %s \n\n", this.location);
    }
  }

  public static void main(String[] args) {
    // testing
    String[] example = {"IAUCC",  "Abbottsfield",  "None",  "54.7327778",  "-112.4972222",  "Quebec"};
    Query q = new Query(example);
  }
}
