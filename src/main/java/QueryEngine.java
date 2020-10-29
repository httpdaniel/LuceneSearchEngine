import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

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

        // Builder class for creating a query
        BooleanQuery.Builder query = new BooleanQuery.Builder();

        // Words that we want to search for and their relevant field
        Query term1 = new TermQuery(new Term("Content", "boundary"));
        Query term2 = new TermQuery(new Term("Content", "detection"));
        //Query term3 = new TermQuery(new Term("Content", "friction"));

        // Construct query using boolean operations
        query.add(new BooleanClause(term1, BooleanClause.Occur.SHOULD));    // And
        query.add(new BooleanClause(term2, BooleanClause.Occur.MUST));  // Or
        //query.add(new BooleanClause(term3, BooleanClause.Occur.MUST));  // Not

        // Get set of results from searcher
        ScoreDoc[] hits = isearcher.search(query.build(), MAX_RESULTS).scoreDocs;

        // Print the results
        System.out.println("Documents: " + hits.length);

        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            System.out.println(i + ") " + hitDoc.get("Title") + " " + hits[i].score);
        }

        ireader.close();
        directory.close();
    }

}
