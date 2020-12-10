# COMP5511-Assignments-Fall-2020

This program provides a variety of search functionalities for geographic locations obtained from datasets published on the Canada Geographic Names Information System. The program creates in-memory data structures in order to provide users the ability to query a given file, using a geographic location's id, latitude, longitude, name, generic term and location. Users can input any number of queries for any number of features. The matching results are presented in an output file. 

# Quick Start

1. Run `bash compile.sh` in order to compile all the classes within the package.
2. Run `java Main.java <path to data file> <path to query file> <path to output file>`. If you want to use the files already contained in this repository, you would run `data/cgn_on_csv_eng.csv log_files/queries.txt log_files/output.txt`. The query file needs to follow the structure outlined in the Queries section.

It is important to note that the data file provided to the program needs to contain only the following 6 columns, in this exact order: CGNDB ID,	Geographical Name, Generic Term, Latitude, Longitude, Location

# Directory

* `Main.java`: Reads the data and query files, builds the in-memory data structures and performs the searches. 
* `Record.java`: The class that represents a single geographic location. When main.java reads a data file, it creates an Array of Record objects. 
* `Query.java`: The class that represents the queries given by the user. Each feature that can be searched is represented by an array of objects so that users can conduct any number of queries.
* `BinarySearchTree.java`: A Binary Search Tree implementation that can perform insert, search, pre order traversal and height balancing. Used for CGNDB ID, Latitude and Longitude searches. 
* `BplusTree.java`: An attempt at a B+ Tree implementation. Incomplete and not actually used in Main.java. Included here to demonstrate the work that was done. 
* `InvertedIndex.java`: An Inverted Index implementation mapping string-type features ("terms") found in geographic locations to their corresponding geographic location record(s). If a single term is associated to multiple records, it is mapped to an array of records, so that there is no duplication of terms. Able to perform insert, search and stop word clean-up. It is used for Geographic Name, Location and Generic Term searches.
* `data/*`: Contains example data files.
* `log_files/*`: Contains example query and output files.

# Queries

The query file needs to follow the structure below. The query file can accept any number of inputs for each geographic feature, separated by a comma. If a given feature is not to be queried, users need to enter "None" in that field, as shown below for generic term. Latitude and Longitudes are paired based on their index in the query file. In the below example, because 41.9666670 and -82.5166670 are both at index 0 and 47.82957 and -91.814186 are both at index 1, the program knows that they are to be paired together in that fashion for the coordinate search. The number of latitudes inputted by the user must match the number of longitudes inputted. 

```
CGNDBID: IAUCC, KAHRO, KAFQX, FDVKW, FBWII
GEOGRAPHIC_NAME: Abbottsfield, Esker Creek, Safety Pin Bend, Oak Point
GENERIC_TERM: None
LATITUDE: 41.9666670, 47.82957
LONGITUDE: -82.5166670, -91.814186
LOCATION: Zealand
```

# Output

This is a sample output created by the program.

```
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
