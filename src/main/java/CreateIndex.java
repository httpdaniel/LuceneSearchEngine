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

        String cranPath = "../cran/cran.all.1400";

        // Create array list for parsed documents to be stored to
        ArrayList<Document> docs = new ArrayList<>();

        // Parse File - TODO

        // Template for creating lucene document
        //Document doc = createDocument(id,title,author,bib,content);
        //docs.add(doc);

        // Return list of documents
        return docs;
    }

    public static Document createDocument(String id, String title, String author, String bib, String content) {

        Document document = new Document();
        document.add(new TextField("ID", id, Field.Store.YES));
        document.add(new TextField("Title", title, Field.Store.YES));
        document.add(new TextField("Author", author, Field.Store.YES));
        document.add(new TextField("Bibliography", bib, Field.Store.YES));
        document.add(new TextField("Content", content, Field.Store.YES));

        return document;
    }
}
