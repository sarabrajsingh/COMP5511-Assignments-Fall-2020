import java.text.Normalizer;
import java.util.ArrayList;
import java.util.NoSuchElementException;

// list of terms, pointing to the respective record
public class InvertedIndex<T> {

  private static final String[] STOP_WORDS = { "a", "la", "le", "de", "des", "the", "aux", "du", "of", "sur" };
  public ArrayList<Entry> entries;

  private class Entry {
    private String term;
    private ArrayList<T> documents;

    // constructor for Entry object
    private Entry(String term, T document) {
      this.term = term;
      this.documents = new ArrayList<T>();
      this.documents.add(document);
    }
    @Override
    public String toString(){
      return new String(term + documents.toString());
    }
  }

  // constructor for the InvertedIndex object
  public InvertedIndex() {
    if(Main.NUM_RECORDS != null){
      this.entries = new ArrayList<Entry>(Main.NUM_RECORDS);
    } else {
      this.entries = new ArrayList<Entry>();
    }
  }

  public int getSize() {
    return this.entries.size();
  }

  public void addEntry(Entry e) {
    this.entries.add(e);
  }

  /* returns the index within the ArrayList of an entry with
  an equal key, if it exists */
  private int findEntry(String term) {
    // if the inverted index is empty, return -1
    if (getSize() == 0) {
      return -1;
    }
    // loop through all of the entries to see if same term exists
    for(int i = 0; i < this.entries.size(); i++){
      if(term.equals(this.entries.get(i).term)){
        return i;
      }
    }
    return -1;
  }

  // remove stop words from terms
  public String termCleaner(String term) {
    // step 1 - get rid of accents by iterating through each character, and
    // replacing the accented value with its non-accented value
    char[] output = new char[term.length()];
    String string = Normalizer.normalize(term, Normalizer.Form.NFD);
    int counter = 0;
    for (int i = 0; i < term.length(); i++) {
      char c = string.charAt(i);
      if (c <= '\u007F') {
        output[counter++] = c;
      }
    }
    String flattenedTerm = (new String(output)).toLowerCase();
    // step 2 - split the new flattenedTerm, where delimiter = " ", and find the
    // difference between this new list and the stopWords[] list. The difference
    // will be our term without stopWords
    StringBuilder sb = new StringBuilder();
    for (String word : flattenedTerm.split(" ")) {
      boolean isWordStopWord = false;
      for (String stopWord : InvertedIndex.STOP_WORDS) {
        if (stopWord.equals(word)) {
          isWordStopWord = true;
          break;
        }
      }
      if(!isWordStopWord){
        sb.append(word).append(" ");
      }
    }
    return sb.toString().trim();
  }

  // enters a term into the inverted index
  public void put(String term, T document) {
    String cleanTerm = termCleaner(term);
    int i = findEntry(cleanTerm);
    // if the term doesn't already exist, create a new entry
    if (i == -1) {
      addEntry(new Entry(cleanTerm, document));
      return;
    }
    /* if term aready exist, add the document to its
    list of documents by creating a new array */
    this.entries.get(i).documents.add(document);
  }

  // gets the documents for a given term
  public ArrayList<T> get(String term) throws NoSuchElementException {
    String cleanTerm = termCleaner(term);
    int i = findEntry(cleanTerm);
    if (i == -1) {
      throw new NoSuchElementException();
    }
    return this.entries.get(i).documents;
  }

  @Override
  public String toString(){
    StringBuilder sb = new StringBuilder();
    for (Entry e : entries){
      sb.append(e.term + " : " + e.documents.toString())
        .append("\n");
    }
    return sb.toString().trim();
  }

  // public static void main(String[] args) {
  //   System.out.println("Succesfully created an Inverted Index.");

  //   // code for testing
  //   InvertedIndex<String> inv = new InvertedIndex<String>();
  //   Record e1 = new Record("EJEIZ", "Lac Lucie", "Lake", 46.987778, -75.38472, "Quebec", "Quebec");
  //   Record e2 = new Record("EFOWB", "Rapides Boisvert", "Rapids", 46.6175, -74.263336, "Quebec", "Quebec");
  //   Record e3 = new Record("FDLAP", "Mud Lake", "Lake", 48.161366, -79.93589, "Ontario", "Quebec");
  //   Record e4 = new Record("FBUEQ", "Keswil Creek", "Creek", 46.092213, -78.97416, "Ontario", "Quebec");
  //   Record e5 = new Record("ABWNT", "Coopers Head", "Head", 47.338722, -53.90362, "Newfoundland and Labrador", "Quebec");
  //   Record e6 = new Record("TRALALA","Coopers Head","Head",47.338722,-53.90362,"Newfoundland and Labrador", "Quebec");

  //   inv.put("Lac Lucie", e1.getCgndbId());
  //   inv.put("Rapides Boisvert", e2.getCgndbId());
  //   inv.put("Mud Lake", e3.getCgndbId());
  //   inv.put("Keswil Creek", e4.getCgndbId());
  //   inv.put("Coopers Head", e5.getCgndbId());
  //   inv.put("Coopers of Head", e6.getCgndbId());

  //   // String get1 = inv.get("Lac Lucie");
  //   // String get2 = inv.get("Rapides Boisvert");
  //   // String get3 = inv.get("Mud Lake");
  //   // String get4 = inv.get("Keswil Creek");
  //   // String get5 = inv.get("Coopers Head");
  //   // System.out.println(get1 + get2 + get3 + get4 + get5);

  //   System.out.println(inv);
}