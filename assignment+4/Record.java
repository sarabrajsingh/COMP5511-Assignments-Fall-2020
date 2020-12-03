// a custom class that represents each unique record from the GNIS file
package assignment4;

public class Record {
  public String cgndbId;
  public String geographicName;
  public String genericTerm;
  public float latitude;
  public float longitude;
  public String location;

  // constructor for Record object
  public Record(String l) {
    // regex allows us to split only on commas that are outside quotes
    String[] elements = l.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    int size = elements.length;

    this.cgndbId = elements[0];
    this.geographicName = elements[1];
    this.genericTerm = elements[2];
    this.latitude = Float.parseFloat(elements[3]);
    this.longitude = Float.parseFloat(elements[4]);
    this.location = null;

    // if there is a location, assign it to the class variable
    if (size == 6) {
      this.location = elements[5];
    }
  }

  // print the info of a given record on console
  public void printRecord() {
    System.out.printf("|| CGNDBID: %s ---> ", this.cgndbId);
    System.out.printf("Geographic Name: %s | ", this.geographicName);
    System.out.printf("Generic Term: %s | ", this.genericTerm);
    System.out.printf("Latitude: %s | ", this.latitude);
    System.out.printf("Longitude: %s | ", this.longitude);
    System.out.printf("Location: %s ||\n", this.location);
  }

  // return the entire record as a string
  public String getRecordLog() {
    String s0 = String.format("|| CGNDBID: %s ---> ", this.cgndbId);
    String s1 = String.format("Geographic Name: %s | ", this.geographicName);
    String s2 = String.format("Generic Term: %s | ", this.genericTerm);
    String s3 = String.format("Latitude: %s | ", this.latitude);
    String s4 = String.format("Longitude: %s | ", this.longitude);
    String s5 = String.format("Location: %s ||", this.location);
    return s0 + s1 + s2 + s3 + s4 + s5;
  }

  public static void main(String[] args) {
    // testing
    String example = "EJEIZ,Lac Lucie,Lake,46.987778,-75.38472,Quebec";
    Record r = new Record(example);
    r.printRecord();
    String logOutput = r.getRecordLog();
    System.out.println(logOutput);
  }
}
