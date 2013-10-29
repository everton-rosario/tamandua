/**
 * 
 */
package br.com.tamandua.searcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.FilterIndexReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

import br.com.tamandua.indexer.IndexerProperties;
import br.com.tamandua.indexer.IndexerProperties.Property;
import br.com.tamandua.searcher.QueryHelper.FieldConfig;
import br.com.tamandua.searcher.exception.InvalidIndexException;

/**
 * @author Everton Rosario (erosario@gmail.com)
 */
/** Simple command-line based search demo. */
public class SearchManager {
    
    private static Logger LOGGER = Logger.getLogger(SearchManager.class);
    private static SearchManager instance = null;
    private static IndexReader reader;
    private static IndexReader readerArtists;
    private static IndexReader readerIntegration;
    private static IndexReader readerImages;
    
  /** Use the norms from one field for all fields.  Norms are read into memory,
   * using a byte of memory per document per searched field.  This can cause
   * search of large collections with a large number of fields to run out of
   * memory.  If all of the fields contain only a single token, then the norms
   * are all identical, then single norm vector may be shared. */
  private static class OneNormsReader extends FilterIndexReader {
    private String field;

    public OneNormsReader(IndexReader in, String field) {
      super(in);
      this.field = field;
    }

    @Override
    public byte[] norms(String field) throws IOException {
      return in.norms(this.field);
    }
  }

  private SearchManager(){}

  public synchronized static void open() throws InvalidIndexException {
      String index = IndexerProperties.getInstance().getProperty(Property.INDEXER_INDEX_DIRECTORY);
      String indexArtists = IndexerProperties.getInstance().getProperty(Property.INDEXER_INDEX_ARTISTS_DIRECTORY);
      String indexIntegration = IndexerProperties.getInstance().getProperty(Property.INDEXER_INDEX_INTEGRATION_DIRECTORY);
      String indexImages = IndexerProperties.getInstance().getProperty(Property.INDEXER_INDEX_ARTIST_IMAGES_DIRECTORY);
      try {
        reader = IndexReader.open(NIOFSDirectory.open(new File(index)), true); // only searching, so read-only=true
        readerArtists = IndexReader.open(NIOFSDirectory.open(new File(indexArtists)), true); // only searching, so read-only=true
        readerIntegration = IndexReader.open(NIOFSDirectory.open(new File(indexIntegration)), true); // only searching, so read-only=true
        readerImages = IndexReader.open(NIOFSDirectory.open(new File(indexImages)), true); // only searching, so read-only=true
    } catch (CorruptIndexException e) {
        throw new InvalidIndexException(e.getMessage(), e);
    } catch (IOException e) {
        throw new InvalidIndexException(e.getMessage(), e);
    } 
  }
  
  public synchronized static void close() {
      try {
        reader.close();
        readerArtists.close();
        readerIntegration.close();
        readerImages.close();
    } catch (IOException e) {
        LOGGER.warn("Closing index throwed this exception.", e);
    }
  }

  /** Simple command-line based search demo. */
  public static void main(String[] args) throws Exception {
      
    String usage =
      "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-raw] [-norms field] [-paging hitsPerPage]";
    usage += "\n\tSpecify 'false' for hitsPerPage to use streaming instead of paging search.";
    if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
      System.out.println(usage);
      System.exit(0);
    }

    IndexerProperties.setFileResource(Thread.currentThread().getClass().getResource("/tamandua-indexer.properties").getPath());
    String index = IndexerProperties.getInstance().getProperty(Property.INDEXER_INDEX_ARTIST_IMAGES_DIRECTORY);
    SearchManager sm = new SearchManager();
    sm.open();
    sm.searchByMusicURI("/fernando-e-sorocaba/madri");
    sm.close();
    String field = "contents";
    String queries = null;
    int repeat = 0;
    boolean raw = false;
    String normsField = null;
    boolean paging = true;
    int hitsPerPage = 10;
    
    for (int i = 0; i < args.length; i++) {
      if ("-index".equals(args[i])) {
        index = args[i+1];
        i++;
      } else if ("-field".equals(args[i])) {
        field = args[i+1];
        i++;
      } else if ("-queries".equals(args[i])) {
        queries = args[i+1];
        i++;
      } else if ("-repeat".equals(args[i])) {
        repeat = Integer.parseInt(args[i+1]);
        i++;
      } else if ("-raw".equals(args[i])) {
        raw = true;
      } else if ("-norms".equals(args[i])) {
        normsField = args[i+1];
        i++;
      } else if ("-paging".equals(args[i])) {
        if (args[i+1].equals("false")) {
          paging = false;
        } else {
          hitsPerPage = Integer.parseInt(args[i+1]);
          if (hitsPerPage == 0) {
            paging = false;
          }
        }
        i++;
      }
    }
    IndexReader reader = IndexReader.open(FSDirectory.open(new File(index)), true); // only searching, so read-only=true
    

    if (normsField != null)
      reader = new OneNormsReader(reader, normsField);

    Searcher searcher = new IndexSearcher(reader);
    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

    BufferedReader in = null;
    if (queries != null) {
      in = new BufferedReader(new FileReader(queries));
    } else {
      in = new BufferedReader(new InputStreamReader(System.in));
    }
    QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, field, analyzer);
    while (true) {
      if (queries == null)                        // prompt the user
        System.out.println("Enter query: ");

      String line = in.readLine();

      if (line == null || line.length() == -1)
        break;

      line = line.trim();
      if (line.length() == 0)
        break;
      
      Query query = parser.parse(line);
      System.out.println("Searching for: " + query.toString(field));

            
      if (repeat > 0) {                           // repeat & time as benchmark
        Date start = new Date();
        for (int i = 0; i < repeat; i++) {
          searcher.search(query, null, 100);
        }
        Date end = new Date();
        System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
      }

      if (paging) {
        doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null);
      } else {
        doStreamingSearch(searcher, query);
      }
    }
    reader.close();
  }
  
  /**
   * This method uses a custom HitCollector implementation which simply prints out
   * the docId and score of every matching document. 
   * 
   *  This simulates the streaming search use case, where all hits are supposed to
   *  be processed, regardless of their relevance.
   */
  public static void doStreamingSearch(final Searcher searcher, Query query) throws IOException {
    Collector streamingHitCollector = new Collector() {
      private Scorer scorer;
      private int docBase;
      
      // simply print docId and score of every matching document
      @Override
      public void collect(int doc) throws IOException {
        System.out.println("doc=" + doc + docBase + " score=" + scorer.score());
      }

      @Override
      public boolean acceptsDocsOutOfOrder() {
        return true;
      }

      @Override
      public void setNextReader(IndexReader reader, int docBase)
          throws IOException {
        this.docBase = docBase;
      }

      @Override
      public void setScorer(Scorer scorer) throws IOException {
        this.scorer = scorer;
      }
      
    };
    
    searcher.search(query, streamingHitCollector);
  }

  /**
   * This demonstrates a typical paging search scenario, where the search engine presents 
   * pages of size n to the user. The user can then go to the next page if interested in
   * the next hits.
   * 
   * When the query is executed for the first time, then only enough results are collected
   * to fill 5 result pages. If the user wants to page beyond this limit, then the query
   * is executed another time and all hits are collected.
   * 
   */
  public static void doPagingSearch(BufferedReader in, Searcher searcher, Query query, 
                                     int hitsPerPage, boolean raw, boolean interactive) throws IOException {
 
    // Collect enough docs to show 5 pages
    TopScoreDocCollector collector = TopScoreDocCollector.create(
        5 * hitsPerPage, false);
    searcher.search(query, collector);
    ScoreDoc[] hits = collector.topDocs().scoreDocs;
    
    int numTotalHits = collector.getTotalHits();
    System.out.println(numTotalHits + " total matching documents");

    int start = 0;
    int end = Math.min(numTotalHits, hitsPerPage);
        
    while (true) {
      if (end > hits.length) {
        System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
        System.out.println("Collect more (y/n) ?");
        String line = in.readLine();
        if (line.length() == 0 || line.charAt(0) == 'n') {
          break;
        }

        collector = TopScoreDocCollector.create(numTotalHits, false);
        searcher.search(query, collector);
        hits = collector.topDocs().scoreDocs;
      }
      
      end = Math.min(hits.length, start + hitsPerPage);
      
      for (int i = start; i < end; i++) {
        if (raw) {                              // output raw format
          System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
          continue;
        }

        Document doc = searcher.doc(hits[i].doc);
        String path = doc.get("id_music");
        if (path != null) {
          System.out.println((i+1) + ". " + path);
          String title = doc.get("music_title");
          if (title != null) {
            System.out.println("   Title: " + doc.get("music_title"));
            System.out.println("   Artist: " + doc.get("artist_name"));
            System.out.println("   Music: " + doc.get("music_uri"));
          }
        } else {
          System.out.println((i+1) + ". " + "No path for this document");
        }
                  
      }

      if (!interactive) {
        break;
      }

      if (numTotalHits >= end) {
        boolean quit = false;
        while (true) {
          System.out.print("Press ");
          if (start - hitsPerPage >= 0) {
            System.out.print("(p)revious page, ");  
          }
          if (start + hitsPerPage < numTotalHits) {
            System.out.print("(n)ext page, ");
          }
          System.out.println("(q)uit or enter number to jump to a page.");
          
          String line = in.readLine();
          if (line.length() == 0 || line.charAt(0)=='q') {
            quit = true;
            break;
          }
          if (line.charAt(0) == 'p') {
            start = Math.max(0, start - hitsPerPage);
            break;
          } else if (line.charAt(0) == 'n') {
            if (start + hitsPerPage < numTotalHits) {
              start+=hitsPerPage;
            }
            break;
          } else {
            int page = Integer.parseInt(line);
            if ((page - 1) * hitsPerPage < numTotalHits) {
              start = (page - 1) * hitsPerPage;
              break;
            } else {
              System.out.println("No such page");
            }
          }
        }
        if (quit) break;
        end = Math.min(numTotalHits, start + hitsPerPage);
      }
      
    }

  }
    /**
     * Busca aberta por termos paginada
     * @return 
     * @throws InvalidIndexException 
     */
    public List<Document> doPagingSearch(String terms,
                             final Integer pageNumber,
                             final Integer max,
                             final String sort,
                             final Integer hitsPerPage,
                             final boolean ascendingSort) throws InvalidIndexException {
 
        try {
    
            Searcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
            String field = "contents";
    
            QueryParser parser = new QueryParser(Version.LUCENE_30, field, analyzer);
            Query query;
            /*query = parser.parse("artist_name:\"" + terms + "\"^6 or music_title:\"1.5"+ terms + "\" or text:\"" + terms + "\"");*/
            query = parser.parse(QueryHelper.getLuceneQuery(terms, " /", QueryHelper.CONNECTOR_OR, QueryHelper.CONNECTOR_AND, true, new FieldConfig[]{FieldConfig.ARTIST_NAME, FieldConfig.MUSIC_TITLE, FieldConfig.LIRIC_TEXT}));
            System.out.println("Searching for: " + query.toString(field));
    
            long startSearch = System.currentTimeMillis();
            searcher.search(query, null, 100);
            long durationSearch = System.currentTimeMillis() - startSearch;
    
            System.out.println("Time: " + durationSearch + "ms");
    
            // Collect enough docs to show 2 pages
            TopScoreDocCollector collector = TopScoreDocCollector.create(10 * hitsPerPage, false);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
    
            int numTotalHits = collector.getTotalHits();
            System.out.println(numTotalHits + " total matching documents");
    
            int start = 0;
            if ((pageNumber - 1) * hitsPerPage < numTotalHits) {
                start = (pageNumber - 1) * hitsPerPage;
            }
            int end = Math.min(numTotalHits, start + hitsPerPage);
            
            List<Document> results = new ArrayList<Document>(hitsPerPage);
            for (int i = start; i < end; i++) {
                results.add(searcher.doc(hits[i].doc));
            }
            return results;
        } catch (ParseException e) {
            LOGGER.error("Query parser exception." , e);
            return Collections.EMPTY_LIST;
        } catch (IOException e) {
            throw new InvalidIndexException("Invalid Index, IOException occurred", e);
        }
    }

    public static SearchManager getInstance() {
        if (instance == null) {
            instance = new SearchManager();
        }
        return instance;
    }

	public List<Document> doCategorySearch(String artistName, String musicTitle) throws InvalidIndexException {

		try {

			Searcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
			String field = "contents";

			QueryParser parser = new QueryParser(Version.LUCENE_30, field,
					analyzer);
			Query query;
			query = parser.parse("artist_name:\"" + artistName + "\" and music_title:\""
					+ musicTitle + "\"");
			System.out.println("Searching for: " + query.toString(field));

			long startSearch = System.currentTimeMillis();
			searcher.search(query, null, 100);
			long durationSearch = System.currentTimeMillis() - startSearch;

			System.out.println("Time: " + durationSearch + "ms");

			// Collect enough docs to show 2 pages
			TopScoreDocCollector collector = TopScoreDocCollector.create(
					5, false);
			searcher.search(query, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			int numTotalHits = collector.getTotalHits();
			System.out.println(numTotalHits + " total matching documents");

			int start = 0;
			int end = Math.min(numTotalHits, 5);

			List<Document> results = new ArrayList<Document>(5);
			for (int i = start; i < end; i++) {
				results.add(searcher.doc(hits[i].doc));
			}
			return results;
		} catch (ParseException e) {
			LOGGER.error("Query parser exception.", e);
			return Collections.EMPTY_LIST;
		} catch (IOException e) {
			throw new InvalidIndexException(
					"Invalid Index, IOException occurred", e);
		}
	}
	
	public List<Document> searchByArtist(String artistName) throws InvalidIndexException {

		try {

			Searcher searcher = new IndexSearcher(readerArtists);
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
			String field = "contents";

			QueryParser parser = new QueryParser(Version.LUCENE_30, field,
					analyzer);
			Query query;
			//query = parser.parse("artist_name:\"" + artistName.concat("*") + "\"");
			query = new WildcardQuery(new Term("artist_name", artistName.concat("*")));
			System.out.println("Searching for: " + query.toString(field));

			long startSearch = System.currentTimeMillis();
			searcher.search(query, null, 100);
			long durationSearch = System.currentTimeMillis() - startSearch;

			System.out.println("Time: " + durationSearch + "ms");

			// Collect enough docs to show 2 pages
			TopScoreDocCollector collector = TopScoreDocCollector.create(
					10, false);
			searcher.search(query, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			int numTotalHits = collector.getTotalHits();
			System.out.println(numTotalHits + " total matching documents");
			
			int start = 0;
			int end = Math.min(numTotalHits, 10);

			List<Document> results = new ArrayList<Document>(10);
			for (int i = start; i < end; i++) {
				results.add(searcher.doc(hits[i].doc));
			}			
			return results;
		/*} catch (ParseException e) {
			LOGGER.error("Query parser exception.", e);
			return Collections.EMPTY_LIST;*/
		} catch (IOException e) {
			throw new InvalidIndexException(
					"Invalid Index, IOException occurred", e);
		}
	}

    public List<Document> searchByMusicURI(String musicUri) throws InvalidIndexException {
        try {

            Searcher searcher = new IndexSearcher(readerIntegration);
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
            String field = "contents";

            QueryParser parser = new QueryParser(Version.LUCENE_30, field, analyzer);
            Query query;
            query = parser.parse("music_uri:" + musicUri);
            System.out.println("Searching for: " + query.toString(field));

            long startSearch = System.currentTimeMillis();
            searcher.search(query, null, 100);
            long durationSearch = System.currentTimeMillis() - startSearch;

            System.out.println("Time: " + durationSearch + "ms");

            // Collect enough docs to show 2 pages
            TopScoreDocCollector collector = TopScoreDocCollector.create(
                    5, false);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            int numTotalHits = collector.getTotalHits();
            System.out.println(numTotalHits + " total matching documents");

            int start = 0;
            int end = Math.min(numTotalHits, 5);

            List<Document> results = new ArrayList<Document>(5);
            for (int i = start; i < end; i++) {
                results.add(searcher.doc(hits[i].doc));
            }
            return results;
        } catch (ParseException e) {
            LOGGER.error("Query parser exception.", e);
            return Collections.EMPTY_LIST;
        } catch (IOException e) {
            throw new InvalidIndexException(
                    "Invalid Index, IOException occurred", e);
        }
    }

    public List<Document> searchImagesByArtistURI(String artistUri) throws InvalidIndexException {
        try {

            Searcher searcher = new IndexSearcher(readerImages);
            Analyzer analyzer = new KeywordAnalyzer();
            String field = "contents";

            QueryParser parser = new QueryParser(Version.LUCENE_30, field, analyzer);
            Query query;
            query = parser.parse("artist_uri:" + artistUri);
            System.out.println("Searching for: " + query.toString(field));

            long startSearch = System.currentTimeMillis();
            searcher.search(query, null, 100);
            long durationSearch = System.currentTimeMillis() - startSearch;

            System.out.println("Time: " + durationSearch + "ms");

            // Collect enough docs to show 2 pages
            TopScoreDocCollector collector = TopScoreDocCollector.create(
                    100, false);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            int numTotalHits = collector.getTotalHits();
            System.out.println(numTotalHits + " total matching documents");

            int start = 0;
            int end = Math.min(numTotalHits, 100);

            List<Document> results = new ArrayList<Document>();
            for (int i = start; i < end; i++) {
                results.add(searcher.doc(hits[i].doc));
            }
            return results;
        } catch (ParseException e) {
            LOGGER.error("Query parser exception.", e);
            return Collections.EMPTY_LIST;
        } catch (IOException e) {
            throw new InvalidIndexException(
                    "Invalid Index, IOException occurred", e);
        }
    }

}

