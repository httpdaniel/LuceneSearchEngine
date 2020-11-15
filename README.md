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
$ java -cp target/LuceneSearchEngine-1.0-SNAPSHOT.jar CreateIndex 1    // For custom analyser
```

Or

``` sh
$ java -cp target/LuceneSearchEngine-1.0-SNAPSHOT.jar CreateIndex 2    // For standard analyser
```

## Querying the engine

``` sh
$ java -cp target/LuceneSearchEngine-1.0-SNAPSHOT.jar QueryEngine 1    // For BM25 Similarity
```

Or

``` sh
$ java -cp target/LuceneSearchEngine-1.0-SNAPSHOT.jar QueryEngine 2    // For TF-IDF Similarity
```

Or

``` sh
$ java -cp target/LuceneSearchEngine-1.0-SNAPSHOT.jar QueryEngine 3    // For boolean similarity
```

The results will be outputted to a file "SearchResults.txt" in the main folder

## Evaluating the results

This step requires the installation of trec eval

``` sh
$ cd trec_eval-9.0.7

$ make

$ ./trec_eval ../cran/cranqrel ../SearchResults.txt
```

## To display only Mean Average Precision & Recall

``` sh
$ ./trec_eval -m map -m recall ../cran/cranqrel ../SearchResults.txt
```

# Results 

Findings from previous experiments can be found in the /results folder for various ranges of scoring functions and evaluations
