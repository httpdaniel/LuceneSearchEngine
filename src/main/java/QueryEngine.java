import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class QueryEngine {

    // Location of search index
    private static final String INDEX_DIRECTORY = "index";

    // Maximum number of returned results
    private static final int MAX_RESULTS = 20;

    public static void main(String[] args) throws IOException {

        // Open folder that contains search index
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));

        // Create objects to read and search across index
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);

        // Stopwords and analyzer
        CharArraySet stopwords = CharArraySet.copy(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);
        Analyzer analyzer = new CustomAnalyzer(stopwords);

        // Use BM25 similarity for scoring
        //isearcher.setSimilarity(new BM25Similarity());

        // Use VSM similarity for scoring
        //isearcher.setSimilarity(new ClassicSimilarity());

        // Use boolean similarity for scoring
        isearcher.setSimilarity(new BooleanSimilarity());

        // Booster to add weight to more important fields
        HashMap<String, Float> boost = new HashMap<>();
        boost.put("Title", 0.65f);
        boost.put("Author", 0.04f);
        boost.put("Bibliography", 0.02f);
        boost.put("Content", 0.35f);

        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[] {"Title", "Author", "Bibliography", "Content"}, analyzer, boost);
        queryParser.setAllowLeadingWildcard(true);

        // Get list of queries
        ArrayList<String> queries = getQueries();

        ArrayList<String> results = new ArrayList<>();

        int docID = 0;
        // Search and score queries
        for (String q : queries) {
            docID++;
            Query query = null;
            try {
                query = queryParser.parse(q);
            } catch (ParseException e) {
                System.out.println("Unable to parse query.");
            }

            // Score documents
            ScoreDoc[] hits = isearcher.search(query, MAX_RESULTS).scoreDocs;

            for (ScoreDoc hit : hits) {
                Document hitDoc = isearcher.doc(hit.doc);
                results.add(docID + " " + "0" + " " + hitDoc.get("ID") + " " + hit.score + " " + "STD" + "\n");
            }
        }

        writeToFile(results);
        ireader.close();
        directory.close();
    }

    public static ArrayList<String> getQueries() {

        // Path for cran queries
        String cranPath = "cran/cran.qry";

        // Create array list for parsed queries to be added to
        ArrayList<String> queries = new ArrayList<>();

        // Parse file and create Lucene queries
        try {

            FileReader fr = new FileReader(cranPath);
            BufferedReader br = new BufferedReader(fr);

            String line = "";

            System.out.println("Parsing Queries...");

            while (line!=null) {
                StringBuilder content = new StringBuilder();
                line = br.readLine();

                if (line.contains(".I")) {
                    line = br.readLine();
                }
                if (".W".equals(line)) {
                    line = br.readLine();
                    while (line != null && !line.contains(".I")) {
                        content.append(line).append(" ");
                        line = br.readLine();
                    }
                }
                // Add query to array
                queries.add(content.toString().trim());
            }

        } catch (FileNotFoundException fnfe) {
            System.out.println("Unable to open cran file.");
        } catch (IOException ioe) {
            System.out.println("Unable to read cran file.");
        }

        return queries;
    }

    public static void writeToFile(ArrayList<String> results) {
        File resultFile = new File("SearchResults.txt");
        BufferedWriter bw = null;

        try {
            FileWriter fw = new FileWriter(resultFile);
            bw = new BufferedWriter(fw);

            for (String res : results) {
                bw.write(res);
            }

        } catch (IOException ioe) {
            System.out.println("Unable to write to file.");
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
