package assignment4;

// list of terms, pointing to the respective record
public class InvertedIndex {

  public Entry[] entries;

  private class Entry {
    private String term;
    private Record[] documents;

    // constructor for Entry object
    private Entry(String term, Record[] documents) {
      this.term = term;
      this.documents = documents;
    }

    private void printEntry() {
      System.out.printf("Term: %s", term);
      System.out.println();
      System.out.println("Documents:");
      for (Record d : documents) {
        d.printRecord();
      }
    }
  }

  // constructor for the InvertedIndex object
  public InvertedIndex() {
    this.entries = new Entry[0];
  }

  public int getSize() {
    return this.entries.length;
  }

  public void addEntry(Entry e) {
    // create a new array thats one larger
    Entry[] newEntries = new Entry[getSize() + 1];
    // copy old entries into new entires
    for(int i = 0; i < getSize(); i++) {
      newEntries[i] = this.entries[i];
    }
    // add the new item to the new entries array
    this.entries = newEntries;
    this.entries[getSize()-1] = e;
    return;
  }

  /* returns the index within the ArrayList of an entry with
  an equal key, if it exists */
  private int findEntry(String term) {
    // if the inverted index is empty, return -1
    if (getSize() == 0) {
      return -1;
    }
    // loop through all of the entries to see if same term exists
    for(int i = 0; i < getSize(); i++) {
      Entry e = entries[i];
      if (term.equals(e.term)) {
        return i;
      }
    }
    return -1;
  }

  // remove stop words from terms
  public String termCleaner(String term) {
    String[] stopWords = {"à", "la", "le", "de", "des", "the", "aux", "du", "of", "sur"};
    String[] words = term.split(" ");
    String cleanedTerm = "";
    for (String w : words) {
      boolean clean = true;
      for (String s : stopWords) {
        if (w.toLowerCase().equals(s)) {
          clean = false;
          break;
        }
      }
      if (clean == true) {
        cleanedTerm = cleanedTerm + w + " ";
      }
    }
    return cleanedTerm.trim();
  }

  // enters a term into the inverted index
  public void put(String term, Record document) {
    String cleanTerm = termCleaner(term);
    int i = findEntry(cleanTerm);
    // if the term doesn't already exist, create a new entry
    if (i == -1) {
      Record[] documents = new Record[1];
      documents[0] = document;
      Entry e = new Entry(cleanTerm, documents);
      addEntry(e);
      return;
    }
    /* if term aready exist, add the document to its
    list of documents by creating a new array */
    Entry e = entries[i];
    Record[] newDocuments = new Record[e.documents.length + 1];
    for(int j = 0; j < e.documents.length; j++) {
      newDocuments[j] = e.documents[j];
    }
    newDocuments[newDocuments.length - 1] = document;
    e.documents = newDocuments;
    return;
  }

  // gets the documents for a given term
  public String get(String term) {
    String cleanTerm = termCleaner(term);
    int i = findEntry(cleanTerm);
    if (i == -1) {
      String o = "Term was not found";
      return o;
    }
    Entry e = entries[i];
    Record[] records = e.documents;
    String output = "";
    for (Record r : records) {
      output = output + r.getRecordLog() + "\n";
    }
    return output;
  }

  public void printInvertedIndex() {
    for (Entry e: entries) {
      for (Record r : e.documents) {
        System.out.printf("%s: %s\n", e.term, r.cgndbId);
      }
    }
  }

  public static void main(String[] args) {
    System.out.println("Succesfully created an Inverted Index.");

    // code for testing
    InvertedIndex inv = new InvertedIndex();
    Record e1 = new Record("EJEIZ,Lac Lucie,Lake,46.987778,-75.38472,Quebec");
    Record e2 = new Record("EFOWB,Rapides Boisvert,Rapids,46.6175,-74.263336,Quebec");
    Record e3 = new Record("FDLAP,Mud Lake,Lake,48.161366,-79.93589,Ontario");
    Record e4 = new Record("FBUEQ,Keswil Creek,Creek,46.092213,-78.97416,Ontario");
    Record e5 = new Record("ABWNT,Coopers Head,Head,47.338722,-53.90362,Newfoundland and Labrador");

    inv.put("Lac Lucie", e1);
    inv.put("Rapides Boisvert", e2);
    inv.put("Mud Lake", e3);
    inv.put("Keswil Creek", e4);
    inv.put("Coopers Head", e5);

    inv.get("Lac Lucie");
    inv.get("Rapides Boisvert");
    inv.get("Mud Lake");
    inv.get("Keswil Creek");
    inv.get("Coopers Head");

    Record e6 = new Record("TRALALA,Coopers Head,Head,47.338722,-53.90362,Newfoundland and Labrador");;
    inv.put("Coopers of Head", e6);
    inv.get("Coopers Head");

    inv.printInvertedIndex();
  }
}
