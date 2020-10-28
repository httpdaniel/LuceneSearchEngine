import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class CreateIndex {

    // Directory where the search index will be saved
    private static String INDEX_DIRECTORY = "../index";

    public static void main (String[] args) throws IOException {

        // Ensure cran files are passed in
        if (args.length <= 0) {
            System.out.println("Expected cran files as input");
            System.exit(1);
        }

        // Analyzer that is used to process text field
        Analyzer analyzer = new StandardAnalyzer();

        // Store index on disk
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        // Set new open mode to create new index
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        IndexWriter iwriter = new IndexWriter(directory, config);

        // ArrayList to store documents after parsing
        ArrayList<Document> documents = createDocuments();

        // Save documents to index
        iwriter.addDocuments(documents);

        // Commit changes and close
        iwriter.close();
        directory.close();

    }

    public static ArrayList<Document> createDocuments() {

        // Create array list for parsed documents to be stored to
        ArrayList<Document> docs = new ArrayList<>();

        // Template for creating lucene document
        Document doc = new Document();
        docs.add(doc);

        // Return list of documents
        return docs;
    }
}
