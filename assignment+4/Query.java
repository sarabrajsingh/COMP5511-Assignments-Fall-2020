// a custom class that represents a given query
package assignment4;
import java.util.*;

public class Query {
  public ArrayList<String> cgndbId = new ArrayList<>();
  public ArrayList<String> geographicName = new ArrayList<>();
  public ArrayList<String> genericTerm = new ArrayList<>();
  public ArrayList<Float> latitude = new ArrayList<>();
  public ArrayList<Float> longitude = new ArrayList<>();
  public ArrayList<String> province = new ArrayList<>();

  // constructor for Query object
  public Query(String[] query) {

    for(String id : query[0].split(",")) {
      if (id != "None") {
        this.cgndbId.add(id.trim());
      } else {
        this.cgndbId = null;
      }
    }

    for(String n : query[1].split(",")) {
      if (n != "None") {
        this.geographicName.add(n.trim());
      } else {
        this.geographicName = null;
      }
    }

    for(String t : query[2].split(",")) {
      if (t != "None") {
        this.genericTerm.add(t.trim());
      } else {
        this.genericTerm = null;
      }
    }

    for(String f : query[3].split(",")) {
      if (f != "None") {
        Float l = Float.parseFloat(f.trim());
        this.latitude.add(l);
      } else {
        this.latitude = null;
      }
    }

    for(String f : query[4].split(",")) {
      if (f != "None") {
        Float l = Float.parseFloat(f.trim());
        this.longitude.add(l);
      } else {
        this.longitude = null;
      }
    }

    for(String p : query[5].split(",")) {
      if (p != "None") {
        this.province.add(p.trim());
      } else {
        this.province = null;
      }
    }
  }

  // print the info of a query
  public void printQuery() {
    System.out.printf("|| CGNDBID: %s ---> ", this.cgndbId);
    System.out.printf("Geographic Name: %s | ", this.geographicName);
    System.out.printf("Generic Term: %s | ", this.genericTerm);
    System.out.printf("Latitude: %s | ", this.latitude);
    System.out.printf("Longitude: %s | ", this.longitude);
    System.out.printf("Province: %s ||\n", this.province);
  }

  public static void main(String[] args) {
    // testing
    String[] example = {"IAUCC",  "Abbottsfield",  "None",  "54.7327778",  "-112.4972222",  "Quebec"};
    Query q = new Query(example);
  }
}
