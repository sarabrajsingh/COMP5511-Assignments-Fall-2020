// a custom class that represents each unique record from the GNIS file
package assignment4;
import java.util.*;

public class Record {
  public String cgndbId;
  public String geographicName;
  public String genericTerm;
  public float latitude;
  public float longitude;
  public String province;

  // constructor for Record object
  public Record(String l) {
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

  public void printRecord() {
    System.out.printf("|| CGNDBID: %s ---> ", this.cgndbId);
    System.out.printf("Geographic Name: %s | ", this.geographicName);
    System.out.printf("Generic Term: %s | ", this.genericTerm);
    System.out.printf("Latitude: %s | ", this.latitude);
    System.out.printf("Longitude: %s | ", this.longitude);
    System.out.printf("Province: %s ||", this.province);
    System.out.println();
  }
}
