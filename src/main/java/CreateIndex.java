import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class CreateIndex {

    // Directory where the search index will be saved
    private static final String INDEX_DIRECTORY = "index";

    public static void main (String[] args) throws IOException {

        // Set of stop words for engine to ignore
        CharArraySet stopwords = CharArraySet.copy(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);

        // Custom analyzer that is used to process text field i.e. tokenizing, stop-word removal, stemming
        Analyzer analyzer = new CustomAnalyzer(stopwords);

        // Store index on disk
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        // Set open mode to create new index
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        IndexWriter iwriter = new IndexWriter(directory, config);

        // ArrayList to store documents after parsing
        ArrayList<Document> documents = getDocuments();

        // Save documents to index
        iwriter.addDocuments(documents);

        // Commit changes and close
        iwriter.close();
        directory.close();

    }

    public static ArrayList<Document> getDocuments() {

        // Path for cran documents
        String cranPath = "cran/cran.all.1400";

        // Create array list for parsed documents to be stored to
        ArrayList<Document> docs = new ArrayList<>();

        // Read in data from cran, parse it, and create documents
        try {
            FileReader fr = new FileReader(cranPath);
            BufferedReader br = new BufferedReader(fr);

            String line = "";
            int id = 0;

            System.out.println("Parsing file..");

            // Parse data
            // Checks for identifier i.e. title, reads and stores lines until the next identifier is reached - repeating this cycle
            while(line != null) {
                StringBuilder title, author, bib, content;
                title = new StringBuilder();
                author = new StringBuilder();
                bib = new StringBuilder();
                content = new StringBuilder();
                line = br.readLine();
                id++;
                if (line.startsWith(".I")) {
                    line = br.readLine();
                }
                if (".T".equals(line)) {
                    line = br.readLine();
                    while (!line.equals(".A")) {
                        title.append(line).append(" ");
                        line = br.readLine();
                    }
                }
                if (".A".equals(line)) {
                    line = br.readLine();
                    while (!line.equals(".B")) {
                        author.append(line).append(" ");
                        line = br.readLine();
                    }
                }
                if (".B".equals(line)) {
                    line = br.readLine();
                    while (!line.equals(".W")) {
                        bib.append(line).append(" ");
                        line = br.readLine();
                    }
                }
                if (".W".equals(line)) {
                    line = br.readLine();
                    while (line!=null && !line.contains(".I")) {
                        content.append(line).append(" ");
                        line = br.readLine();
                    }
                }

                Document doc = createDocument(id, title.toString(), author.toString(), bib.toString(), content.toString());
                docs.add(doc);

            }

            br.close();
        }
        catch(FileNotFoundException fnfe) {
            System.out.println("Unable to open cran file.");
        }
        catch(IOException ioe) {
            System.out.println( "Unable to read cran file.");
        }

        // Return list of documents
        return docs;
    }

    public static Document createDocument(int id, String title, String author, String bib, String content) {

        // Create new Lucene document with passed in parameters
        Document document = new Document();
        document.add(new TextField("ID", String.valueOf(id), Field.Store.YES));
        document.add(new TextField("Title", title, Field.Store.YES));
        document.add(new TextField("Author", author, Field.Store.YES));
        document.add(new TextField("Bibliography", bib, Field.Store.YES));
        document.add(new TextField("Content", content, Field.Store.YES));

        // Return Lucene document
        return document;
    }
}
