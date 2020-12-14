# COMP5511-Assignments-Fall-2020

This program provides a variety of search functionalities for geographic locations obtained from datasets published on the Canada Geographic Names Information System. The program creates in-memory data structures in order to provide users the ability to query a given file, using a geographic location's id, latitude, longitude, name, generic term and location. Users can input any number of queries for any number of features. The matching results are presented in an output file. 

## CSV Data

Please use the original CSV data files from `open.canada.ca` for this program - https://open.canada.ca/data/en/dataset/e27c6eba-3c5d-4051-9db2-082dc6411c2c. The CSV header must obey the following convention:
<br/>
| | | | | | | | | | | | | | | |
|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|
| CGNDB ID | Geographical Name | Language | Syllabic Form | Generic Term | Generic Category | Concise Code | Toponmyic Feature ID | Latitude | Longitude | Location | Province-Territory | Relevance at Scale | Decision Date | Source |
<br/>

## Program Execution

1. Run `bash compile.sh` in order to compile all the classes within the package.
2. Run `java Main.java <path to data file> <path to query file> <path to output file>`. If you want to use the files already contained in this repository, you would run `java Main.java data/cgn_on_csv_eng.csv log_files/queries.txt log_files/output.txt`. The query file needs to follow the structure outlined in the Queries section.

#### Example Program Execution
```bash
bash compile.sh && java Main.java data/cgn_qc_csv_eng.csv log_files/queries.txt log_files/output.txt
```

# Directory
#### Root Directory Hierarchy
```bash
.
├── BinarySearchTree.java
├── BplusTree.java
├── compile.sh
├── data
│   ├── cgn_*.csv
├── InvertedIndex.java
├── log_files
│   ├── output.txt
│   └── queries.txt
├── Main.java
├── out.log
├── Query.java
├── README.md
└── Record.java
```

* `Main.java`: Reads the data and query files, builds the in-memory data structures and performs the searches. 
* `Record.java`: The class that represents geographic locations. When main.java reads a data file, it creates an Array of Record objects. 
* `Query.java`: The class that represents the queries given by the user. Each feature that can be searched is represented by an array of objects so that users can conduct any number of queries.
* `BinarySearchTree.java`: A Binary Search Tree implementation that can perform insert, search, pre order traversal and height balancing. Used for CGNDB ID, Latitude and Longitude searches. 
* `BplusTree.java`: An attempt at a B+ Tree implementation. Incomplete and not actually used in Main.java. Included here to demonstrate the work that was done. 
* `InvertedIndex.java`: An Inverted Index implementation mapping string-type features - terms - found in geographic locations to their corresponding geographic location record(s). If a single term is associated to multiple records, it is mapped to an array of records, so that there is no duplication of terms. Able to perform insert, search and stop word clean-up. It is used for Geographic Name, Location and Generic Term searches.
* `data/*`: Contains example data files.
* `log_files/*`: Contains example query and output files.
* `compile.sh`: A bash script that compiles the classes used in Main.java.

# Queries

The query file needs to follow the structure below. The query file can accept any number of inputs for each geographic feature, separated by a comma. If a given feature is not to be queried, users need to enter "None" in that field, as shown below for generic term. Latitude and Longitudes are paired based on their index in the query file. In the below example, because 41.9666670 and -82.5166670 are both at index 0 and 47.82957 and -91.814186 are both at index 1, the program knows that they are to be paired together in that fashion for the coordinate search. The number of latitudes entered by the user must match the number of longitudes. 

```bash
CGNDBID: IAUCC, KAHRO, KAFQX, FDVKW, FBWII
GEOGRAPHIC_NAME: Abbottsfield, Esker Creek, Safety Pin Bend, Oak Point
GENERIC_TERM: None
LATITUDE: 41.9666670, 47.82957
LONGITUDE: -82.5166670, -91.814186
LOCATION: Zealand
```

# Output

This is a sample output created by the program.

#### Stdout to Terminal

```bash
READING FILE AND PRINTING SOME EXAMPLE RECORDS FOR DEMONSTRATION...
===========================

|| CGNDBID: FARBX ---> Geographic Name: Clarity Lake | Generic Term: Lake | Latitude: 47.82957 | Longitude: -83.34689 | Location: Sudbury | Province: Ontario ||
|| CGNDBID: FBJFN ---> Geographic Name: Grassy Bay | Generic Term: Bay | Latitude: 49.918295 | Longitude: -91.814184 | Location: Kenora | Province: Ontario ||
|| CGNDBID: FCAFG ---> Geographic Name: Lonely Creek | Generic Term: Creek | Latitude: 48.5016667 | Longitude: -91.5894444 | Location: Rainy River | Province: Ontario ||
|| CGNDBID: FCHGR ---> Geographic Name: Paudash Lake | Generic Term: Dispersed Rural Community | Latitude: 44.98927 | Longitude: -78.02365 | Location: Haliburton | Province: Ontario ||
|| CGNDBID: FCSGD ---> Geographic Name: Steep Rock Rapids | Generic Term: Rapids | Latitude: 51.578798 | Longitude: -90.377008 | Location: Kenora | Province: Ontario ||

Total Records: 58075

BUILDING IN MEMORY DATA STRUCTURES...
===========================

>>> Creating a Binary Search Tree for ID look up
>>> Balancing the Binary Search Tree
>>> Height before and after balancing the BST: 1610 vs 16
>>> Creating Inverted Indices for Geographic Name, Location and Generic Term look up
>>> Creating Binary Search Trees for Latitude and Longitude look up
>>> Balancing the Binary Search Trees
>>> Height before and after balancing the Latitude BST: 37 vs 16
>>> Height before and after balancing the Longitude BST: 39 vs 16

READING QUERIES AND SAVING RESULTS TO LOG FILE...
===========================

>>> CGNDBID(s): [IAUCC, KAHRO, KAFQX, FDVKW, FBWII] 
>>> Geographic Name(s): [Abbottsfield, Esker Creek, Safety Pin Bend, Oak Point] 
>>> Generic Term(s): null 
>>> Latitude(s) & Longitude(s): [[41.966667, 47.82957], [-82.516667, -91.814186]]
>>> Location(s): [Zealand] 


>>> Could not find IAUCC, moving onto next ID
>>> Could not find KAHRO, moving onto next ID
>>> Could not find KAFQX, moving onto next ID

>>> Could not find Abbottsfield, moving onto next Geographic Name
>>> Could not find Safety Pin Bend, moving onto next Geographic Name

>>> Generic Terms not present in Query. Moving onto next Query object

>>> Successfully wrote results to [log_files/output.txt]
```

#### Example of the Output File

```bash
IAUCC:
Record was not found

KAHRO:
Record was not found

KAFQX:
Record was not found

FDVKW:
|| CGNDBID: FDVKW ---> Geographic Name: Monaghan Lake | Generic Term: Lake | Latitude: 49.795914 | Longitude: -79.71903 | Location: Cochrane ||

FBWII:
|| CGNDBID: FBWII ---> Geographic Name: La Renouche | Generic Term: Dispersed Rural Community | Latitude: 45.546112 | Longitude: -74.486115 | Location: Prescott ||

Abbottsfield:
Term was not found

Esker Creek:
|| CGNDBID: FBCXC ---> Geographic Name: Esker Creek | Generic Term: Creek | Latitude: 48.12905 | Longitude: -80.997925 | Location: Timiskaming ||

Safety Pin Bend:
Term was not found

Oak Point:
|| CGNDBID: FCGKM ---> Geographic Name: Oak Point | Generic Term: Point | Latitude: 46.13821 | Longitude: -82.28358 | Location: Algoma ||
|| CGNDBID: FCGKK ---> Geographic Name: Oak Point | Generic Term: Point | Latitude: 44.2359 | Longitude: -76.33486 | Location: Frontenac ||
|| CGNDBID: FCGKL ---> Geographic Name: Oak Point | Generic Term: Point | Latitude: 49.546944 | Longitude: -94.679726 | Location: Kenora ||
|| CGNDBID: FCGKP ---> Geographic Name: Oak Point | Generic Term: Point | Latitude: 44.29217 | Longitude: -77.98542 | Location: Northumberland ||
|| CGNDBID: FCGKO ---> Geographic Name: Oak Point | Generic Term: Point | Latitude: 44.85684 | Longitude: -79.29059 | Location: Muskoka ||

Zealand:
|| CGNDBID: FIDTE ---> Geographic Name: Airport Road Conservation Reserve | Generic Term: Conservation Reserve | Latitude: 49.82636 | Longitude: -92.76316 | Location: Zealand ||

41.966667, -82.51667:
|| CGNDBID: FEZVK ---> Geographic Name: Parc national du Canada de la Pointe-Pelée | Generic Term: National Park | Latitude: 41.966667 | Longitude: -82.51667 | Location: Essex ||
|| CGNDBID: FEZVJ ---> Geographic Name: Point Pelee National Park of Canada | Generic Term: National Park | Latitude: 41.966667 | Longitude: -82.51667 | Location: Essex ||

47.82957, -91.814186:
Record was not found
```

# Contributors
| | |
|-|-|
| Dunya Oguz | 40181540 | https://github.com/dunyaoguz |
| Hugo Joncour | 40139230 | https://github.com/SanteauX |
| Sarabraj Singh | 29473858 | https://github.com/sarabrajsingh |
| John Purcell | 27217439 | https://github.com/johnpurcell514 |
