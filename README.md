# Lucene Search Engine

A Java implementation of a Lucene-based retrieval system.

This search engine is built upon the Cranfield collection, a corpus of 1400 text documents which can be found [here](http://ir.dcs.gla.ac.uk/resources/test_collections/cran/)

# Running The Project

## Getting started

```
git clone https://github.com/httpdaniel/LuceneSearchEngine.git

cd LuceneSearchEngine

mvn package

```

## Creating an index

```
java -cp target/LuceneSearchEngine-1.0-SNAPSHOT.jar CreateIndex

```

## Querying the engine

```
java -cp target/LuceneSearchEngine-1.0-SNAPSHOT.jar QueryEngine

```
