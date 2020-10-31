import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

            // Print the results
            System.out.println("Documents: " + hits.length);

            for (ScoreDoc hit : hits) {
                Document hitDoc = isearcher.doc(hit.doc);
                System.out.println(docID + " " + "0" + " " + hitDoc.get("ID") + " " + hit.score);
            }
        }

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
            int id = 0;

            System.out.println("Parsing Queries...");

            while (line!=null) {
                StringBuilder content = new StringBuilder();
                line = br.readLine();
                id++;
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

}
