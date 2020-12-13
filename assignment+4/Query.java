// a custom class that represents a given query

import java.util.*;

public class Query {
  // data members
  private ArrayList<String> cgndbId;
  private ArrayList<String> geographicName;
  private ArrayList<String> genericTerm;
  private ArrayList<Double> latitude;
  private ArrayList<Double> longitude;
  private ArrayList<String> location;

  // getters
  public ArrayList<String> getCgndbId() {
    return this.cgndbId;
  }

  public ArrayList<String> getGeographicName() {
    return this.geographicName;
  }

  public ArrayList<String> getGenericTerm() {
    return this.genericTerm;
  }

  public ArrayList<Double> getLatitude() {
    return this.latitude;
  }

  public ArrayList<Double> getLongitude() {
    return this.longitude;
  }

  public ArrayList<String> getLocation() {
    return this.location;
  }

   // constructor for Query object
  public Query(String[] query) {

    this.cgndbId = new ArrayList<String>();
    this.geographicName = new ArrayList<String>();
    this.genericTerm = new ArrayList<String>();
    this.latitude = new ArrayList<Double>();
    this.longitude = new ArrayList<Double>();
    this.location = new ArrayList<String>();

    for(String id : query[0].split(",")) {
      if (!id.trim().equals("None"))  {
        this.cgndbId.add(id.trim());
      } else {
        this.cgndbId = null;
      }
    }

    for(String name : query[1].split(",")) {
      if (!name.trim().equals("None"))  {
        this.geographicName.add(name.trim());
      } else {
        this.geographicName = null;
      }
    }

    for(String t : query[2].split(",")) {
      if (!t.trim().equals("None")) {
        this.genericTerm.add(t.trim());
      } else {
        this.genericTerm = null;
      }
    }

    for(String lat : query[3].split(",")) {
      if (!lat.trim().equals("None"))  {
        this.latitude.add(Double.parseDouble(lat));
      } else {
        this.latitude = null;
      }
    }

    for(String longi : query[4].split(",")) {
      if (!longi.trim().equals("None"))  {
        this.longitude.add(Double.parseDouble(longi));
      } else {
        this.longitude = null;
      }
    }

    for(String loc : query[5].split(",")) {
      if (!loc.trim().equals("None"))  {
        this.location.add(loc.trim());
      } else {
        this.location = null;
      }
    }
  }

  // print the info of a query
  @Override
  public String toString() {
    return String.format(
      ">>> CGNDBID(s): %s \n>>> Geographic Name(s): %s \n>>> Generic Term(s): %s \n>>> Latitude(s) & Longitude(s): [%s, %s]\n>>> Location(s): %s \n\n",
      this.cgndbId, this.geographicName, this.genericTerm, this.latitude, this.longitude, this.location
    );
  }
  // public static void main(String[] args) {
  //   // testing
  //   String[] example = {"IAUCC",  "Abbottsfield",  "None",  "54.7327778",  "-112.4972222",  "Quebec"};
  //   Query query = new Query(example);
  //   System.out.println(query);
  // }
}
