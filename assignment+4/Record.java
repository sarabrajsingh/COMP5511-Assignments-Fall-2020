// a custom class that represents each unique record from the GNIS file

public class Record implements Comparable<Record> {
  private String cgndbId;
  private String geographicName;
  private String genericTerm;
  private Double latitude;
  private Double longitude;
  private String location;
  private String province;

  public static String SORT_BY = "CGNDBID";

  // default constructor for Record object
  public Record(String cgndbId, String geographicName, String genericTerm, Double latitude, Double longitude,
      String location, String province) {
    this.cgndbId = cgndbId;
    this.geographicName = geographicName;
    this.genericTerm = genericTerm;
    this.latitude = latitude;
    this.longitude = longitude;
    this.location = location;
    this.province = province;
  }

  // constructor with only cgndbID
  public Record(String cgndbId) {
    this.cgndbId = cgndbId;
  }

  // constructor with only latitude
  public Record(Double latitude) {
    this.latitude = latitude;
  }

  public String getCgndbId() {
    return this.cgndbId;
  }

  public String getGeographicName() {
    return this.geographicName;
  }

  public String getGenericTerm() {
    return this.genericTerm;
  }

  public Double getLatitude() {
    return this.latitude;
  }

  public Double getLongitude() {
    return this.longitude;
  }

  public String getLocation() {
    return this.location;
  }

  public String getProvince() {
    return this.province;
  }

  // toString() override for ease of readability
  @Override
  public String toString() {
    return String.format(
        "|| CGNDBID: %s ---> Geographic Name: %s | Generic Term: %s | Latitude: %s | Longitude: %s | Location: %s | Province: %s ||",
        this.cgndbId, this.geographicName, this.genericTerm, this.latitude, this.longitude, this.location, this.province);
  }

  // comparable override based on CGNDBID
  @Override
  public int compareTo(Record record) {
    // compareTo based on CGNDBIDs only (default)
    Integer returnValue = null;
    switch(Record.SORT_BY) {
      case "CGNDBID":
        returnValue = record.getCgndbId().compareTo(this.cgndbId);
        break;
      case "LATITUDE":
        returnValue = record.getLatitude().compareTo(this.latitude);
        break;
      case "LONGITUDE":
        returnValue = record.getLongitude().compareTo(this.longitude);
        break;
    }
    return returnValue;
  }
  // public static void main(String[] args) {
  //   // testing
  //   Record r1 = new Record("EJEIX", "Lac Lucie", "Lake", 45.987778, -75.38472, "Quebec", "Quebec");
  //   Record r2 = new Record("EJEIZ", "Lac Lucie", "Lake", 45.987778, -75.38472, "Quebec", "Quebec");
  //   Record r3 = new Record(45.987778);
  //   Record r4 = new Record(45.469997);
  // }
}
