# Lucene Search Engine

A Java implementation of a Lucene-based retrieval system

This search engine is built upon the Cranfield collection, a corpus of 1400 text documents which can be found [here](http://ir.dcs.gla.ac.uk/resources/test_collections/cran/)

# Running The Project

## Building the project

``` sh
$ git clone https://github.com/httpdaniel/LuceneSearchEngine.git

$ cd LuceneSearchEngine

$ mvn package
```

## Creating an index

``` sh
$ java -cp target/LuceneSearchEngine-1.0-SNAPSHOT.jar CreateIndex
```

## Querying the engine

``` sh
$ java -cp target/LuceneSearchEngine-1.0-SNAPSHOT.jar QueryEngine
```

The results will be outputted to a file "SearchResults.txt" in the main folder

# Results 

Findings from previous experiments can be found in the /results folder for various ranges of scoring functions and evaluations
