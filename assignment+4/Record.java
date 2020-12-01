// a custom class that represents each unique record from the GNIS file
package assignment4;

public class Record {
  public String cgndbId;
  public String geographicName;
  public String genericTerm;
  public float latitude;
  public float longitude;
  public String province;

  // constructor for Record object
  public Record(String l) {
    String[] elements = l.split(",");
    int size = elements.length;

    this.cgndbId = elements[0];
    this.genericTerm = elements[size-4];
    this.latitude = Float.parseFloat(elements[size-3]);
    this.longitude = Float.parseFloat(elements[size-2]);
    this.province = elements[size-1];

    /* if size is greater than 6, it means geographic name contains one
    or more commas. We need to merge multiple elements in the list
    to get the entire geographic name into one string */
    if (size > 6) {
      String[] sub = new String[size-5];
      // get a sublist of every element thats a part of the geographic name
      for (int i = 0; i < sub.length; i++) {
        sub[i] = elements[i+1];
      }

      // merge the individual elements into one string
      String name = "";
      for(String s : sub) {
        name = name + s + ",";
      }
      this.geographicName = name.substring(1, name.length()-2);
    // if the geographic name does not contain any commas, get the name from index 1
    } else {
      this.geographicName = elements[1];
    }
  }

  // print the info of a given record on console
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
