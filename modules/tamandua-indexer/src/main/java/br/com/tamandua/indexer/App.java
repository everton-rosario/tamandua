package br.com.tamandua.indexer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.tamandua.indexer.IndexerProperties.Property;
import br.com.tamandua.searcher.SearchManager;
import br.com.tamandua.searcher.exception.InvalidIndexException;
import br.com.tamandua.service.util.PermalinkTool;
import br.com.tamandua.service.util.RadioPermalinkHelper;
import br.com.tamandua.service.util.StringNormalizer;

/**
 * Class App
 *
 * @author Everton Rosario (erosario@gmail.com)
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static final String LIMIT = " LIMIT ?, ?";
    // Opcoes existentes para a aplicacao
    private static Options options = getOptions();
	private static IndexerProperties indexerProperties;
	private static ExecutorService executor;
    
    /** @param args 
     * @throws Exception 
     * @throws SQLException 
     */
    public static void main(String[] args) throws SQLException, Exception {
        //args = new String[] { "--index", "--full", "--threads=10"/*,"--letters=q", "--artistId=116683"/*, "--musicId=345", "--lyricId=678" */};
        //args = new String[] { "--index", /*"--full",*/ "--threads=6","--letters=q",/* "--artistId=95852", "--musicId=345", "--lyricId=678" */};
        //args = new String[] { "--clean", /*"--full" ,"--letters=q", "--artistId=116683", "--musicId=345",*/ "--lyricId=678" };
    	//args = new String[] { "--match-radio=C:/export/tamandua-index/csv_parceiro_letras.csv" };
        //args = new String[] { "--optimize" };
        
        // interpretador de comandos
        CommandLineParser parser = new PosixParser();

        try {
            // Realiza o parse da linha de comando
            CommandLine line = parser.parse( options, args );
            String lettersToUse = ""; 

            // Verifica se nao existe alguma inconsistencia com a chamada de prompt de comando
            if (!isValidLine(line) || line.hasOption("help") || line.hasOption("projecthelp")) {
                showUsage();
                return;
            }
            
            // Define que letras utilizar
            if (line.hasOption("letters")) {
                lettersToUse = line.getOptionValue("letters");
            }
            
            int threads = 1;
            if(line.hasOption("threads")){
                threads = Integer.parseInt(line.getOptionValue("threads"));
            }
            executor = Executors.newFixedThreadPool(threads);


            /*
             * Ou indexa ou limpa, nunca faz ambos
             */
            if(IndexerProperties.getInstance().getProperties().isEmpty()){
	            IndexerProperties.setFileResource(Thread.currentThread().getClass().getResource("/tamandua-indexer.properties").getPath());
	            indexerProperties = IndexerProperties.getInstance();
            }
            if (line.hasOption("--index")) {
                indexer(line.hasOption("full"), lettersToUse, line.getOptionValue("artistId"), line.getOptionValue("musicId"), line.getOptionValue("lyricId"));
            } else if (line.hasOption("--clean")) {
            	cleaner(line.hasOption("full"), lettersToUse, line.getOptionValue("artistId"), line.getOptionValue("musicId"), line.getOptionValue("lyricId"));
            } else if (line.hasOption("--match-radio")) {
            	String filePath = line.getOptionValue("match-radio");
            	matchRadio(filePath);
            } else if (line.hasOption("--optimize")) {
                optimizeIndex();
            } else if (line.hasOption("--index-artist")) {
                indexEntities(Property.INDEXER_ARTISTS_QUERY_COUNT, Property.INDEXER_ARTISTS_QUERY, Property.INDEXER_INDEX_ARTISTS_DIRECTORY);
            } else if (line.hasOption("--index-artist-images")) {
                indexEntities(Property.INDEXER_ARTIST_IMAGES_QUERY_COUNT, Property.INDEXER_ARTIST_IMAGES_QUERY, Property.INDEXER_INDEX_ARTIST_IMAGES_DIRECTORY);
            } else if (line.hasOption("--index-integration")) {
                indexEntities(Property.INDEXER_INTEGRATION_QUERY_COUNT, Property.INDEXER_INTEGRATION_QUERY, Property.INDEXER_INDEX_INTEGRATION_DIRECTORY);
            } else if (line.hasOption("--clean-artist")) {
            	cleanerArtists(Property.INDEXER_INDEX_ARTISTS_DIRECTORY);
            } else if (line.hasOption("--clean-integration")) {
                cleanerArtists(Property.INDEXER_INDEX_INTEGRATION_DIRECTORY);
            }
        }
        catch(ParseException exp) {
            showUsage();
        }	
    }

	private static void matchRadio(String filePath)
			throws FileNotFoundException, IOException, InvalidIndexException {
		File csvFile = new File(filePath);
		File csvRadioFile = new File(csvFile.getParent() + csvFile.getName() + ".radio");
		File csvTocaletraFile = new File(csvFile.getParent() + csvFile.getName() + ".toca");

		int matched = 0;
		int matchedTraducao = 0;
		int matchedCifrada = 0;
		int currentLine = 1;
		
		// Opens the searcher Index
		SearchManager.open();
		SearchManager searchManager = SearchManager.getInstance();
		String[] columnNames = new String [] {"idt_track", "name_artist", "nam_track", "album_name"};

		// Leitura no arquivo csv de entrada com os id da Radio, nome da musica, nome do artista e nome do album na radio.
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String trackLine = br.readLine();

		// Escrita no csvRadio
		BufferedWriter bwRadio = new BufferedWriter(new FileWriter(csvRadioFile));
		BufferedWriter bwTocaletra = new BufferedWriter(new FileWriter(csvTocaletraFile));
		
		while (trackLine != null) {
			String[] tokens = trackLine.split(",");
			try {
				String idtTrack = tokens[0];
				String namArtist = tokens[1].replace("\"", "");
				String namTrack = tokens[2].replace("\"", "");
				String namAlbum = tokens[3].replace("\"", "");
				
		        List<Document> resultList = searchManager.doCategorySearch(StringNormalizer.normalizeText(namArtist), StringNormalizer.normalizeText(namTrack));
		        
//		        System.out.println("Searching For:");
//		        System.out.println("\tidtTrack:"+idtTrack);
//		        System.out.println("\tnamArtist:"+namArtist);
//		        System.out.println("\tnamTrack:"+namTrack);
//		        System.out.println("\tnamAlbum:"+namAlbum);
		        
		        String radioPermalink = RadioPermalinkHelper.getCompletePermalinkForTrack(namArtist, namTrack, idtTrack, null);
		        PermalinkTool linkTool = new PermalinkTool();
		        if (resultList.size() > 0 ) {
		        	int index = 0;
		        	
		        	boolean hasOriginal = false;
		        	boolean hasTraduction = false;
		        	boolean hasCipher = false;
		        	for (Document document : resultList) {
		        		boolean isModerated = StringUtils.isNotBlank(document.getField("text").stringValue());
		        		
		        		String musicTitle = document.getField("music_title").stringValue();
						String artistName = document.getField("artist_name").stringValue();
						String idMusic = document.getField("id_music").stringValue();
						String idLyric = document.getField("id_lyric").stringValue();
						String idArtist = document.getField("id_artist").stringValue();
						if (isModerated && artistName.contains(namArtist) && musicTitle.contains(namTrack)) {
		        			//System.out.println("Radio: " + radioPermalink);
		        			String linkToMusicUri = linkTool.linkToMusicUri(document.getField("music_uri").stringValue());
							//System.out.println("TL"+(++index)+": " + linkToMusicUri + " is ok to use? " + isModerated);
		        			if (StringNormalizer.normalizeArtistName(musicTitle).contains("traducao")) {
                                if (!hasTraduction) {
    		        				matchedTraducao++;
    		        				hasTraduction = true;
                                    bwTocaletra.write("TRADUCAO,\""+namArtist+"\",\""+namTrack+"\",\""+namAlbum+"\",\""+idtTrack+"\",\""+radioPermalink+"\",\""+idMusic+"\",\""+idLyric+"\",\""+idArtist+"\",\""+linkToMusicUri+"\"\n");
                                    matched++;
                                }
		        			}
		        			if (StringNormalizer.normalizeArtistName(musicTitle).contains("cifra")) {
		        			    if (!hasCipher) {
    		        				matchedCifrada++;
    		        				hasCipher = true;
                                    bwTocaletra.write("CIFRA,\""+namArtist+"\",\""+namTrack+"\",\""+namAlbum+"\",\""+idtTrack+"\",\""+radioPermalink+"\",\""+idMusic+"\",\""+idLyric+"\",\""+idArtist+"\",\""+linkToMusicUri+"\"\n");
                                    matched++;
		        			    }
		        			} else {
		        			    if (!hasOriginal) {
    		        			    hasOriginal = true;
    		        			    bwTocaletra.write("LETRA,\""+namArtist+"\",\""+namTrack+"\",\""+namAlbum+"\",\""+idtTrack+"\",\""+radioPermalink+"\",\""+idMusic+"\",\""+idLyric+"\",\""+idArtist+"\",\""+linkToMusicUri+"\"\n");
    		        			    matched++;
		        			    }
		        			}
		        			
		        		}
					}
		        	bwTocaletra.flush();
		        	System.out.println();
		        }
		        //System.out.println("Found " + resultList.size() + " ocurrences");
		        
		        
			} catch (IndexOutOfBoundsException ex) {
				LOGGER.warn("Faltando propriedade de indice");
			}
			trackLine = br.readLine();
			currentLine++;
		}

		// Closes the searcher Index
		SearchManager.close();
		br.close();
		bwRadio.close();
		bwTocaletra.close();
		
		System.out.println("Total documents searched: " + currentLine);
		System.out.println("Total documents matched: " + matched);
		System.out.println("Total documents matchedTraducao: " + matchedTraducao);
		System.out.println("Total documents matchedCifrada: " + matchedCifrada);
	}

    /**
     * Processa para remover os itens na ordem especificada. Sempre realiza apenas uma das prioridades. Se informada FULL e o artistId apenas a Full sera executada. 
     * Se informadas letras, apenas as letras informadas serao processadas. Sempre executa apenas a ordem informada
     * @param isFull Full, prioridade 1.
     * @param lettersToUse Letras, prioridade 2.
     * @param artistId Artista, prioridade 3.
     * @param musicId Musica, prioridade 4.
     * @param lyricId Letra da musica, prioridade 5.
     * @throws IOException 
     */
    private static void cleaner(boolean isFull, String lettersToUse, String artistId, String musicId, String lyricId) throws IOException {
    	System.out.println("cleaning...");
    	long start = System.currentTimeMillis();
    	int numberOfDocs = 0;
    	
    	String indexDirectoryPath = indexerProperties.getProperty(Property.INDEXER_INDEX_DIRECTORY);
        File indexDirectoryFile = new File(indexDirectoryPath);
		Directory directory = FSDirectory.open(indexDirectoryFile);
        IndexReader reader = IndexReader.open(directory, false); // we don't want read-only because we are about to delete

        Term term = null; 

    	/* FULL */
    	if (isFull) {
    		System.out.println("full cleaning the indexed documents...");
    		/* Deleta o diretorio do indice */
    		numberOfDocs = reader.maxDoc();
    		reader.close();
    		System.out.println("full cleaning starting to clean [" + numberOfDocs + "]...");
    		FileUtils.deleteDirectory(indexDirectoryFile);
    		System.out.println("full cleanned index!");

    		return;


      	/* LETRAS */
    	} else if (StringUtils.isNotBlank(lettersToUse)) {
//    		if (lettersToUse.length() > 15) {
//    			System.out.println("WARNING! to clean more than 15 letters, use FULL for faster cleaning. Will be clean FULL now!");
//    			cleaner(true, null, null, null, null);
//    			return;
//    		} else {
//    			System.out.println("indexing letters: [" + lettersToUse + "]...");
//    			for (int i = 0; i < lettersToUse.length(); i++) {
//					String letter = "" + lettersToUse.charAt(i);
//		    		numberOfDocs += readCount(indexerProperties.getProperty(Property.INDEXER_LETTER_QUERY_COUNT), letter.toLowerCase());
//		    		System.out.println("Indexing letter: " + letter);
//		    		indexData(indexerProperties.getProperty(Property.INDEXER_LETTER_QUERY), letter.toLowerCase());
//				}
//    		}

    	/* ARTIST ID */
    	} else if (StringUtils.isNotBlank(artistId)) {
    		System.out.println("cleaning artistId: [" + artistId + "]...");
			term = new Term("id_artist", artistId);
			System.out.println("deleting artistId ["+artistId+"] from index...");

    	/* MUSIC ID */
    	} else if (StringUtils.isNotBlank(musicId)) {
    		System.out.println("cleaning musicId: [" + musicId + "]...");
			term = new Term("id_music", musicId);
			System.out.println("deleting musicId ["+musicId+"] from index...");

       	/* LYRIC ID */
    	} else if (StringUtils.isNotBlank(lyricId)) {
    		System.out.println("cleaning lyricId: [" + lyricId + "]...");
			term = new Term("id_lyric", lyricId);
			System.out.println("deleting lyricId ["+lyricId+"] from index...");
    	}

    	numberOfDocs = reader.deleteDocuments(term);
		System.out.println("deleted [" + numberOfDocs + "] documents!");
    	
        reader.close();
        directory.close();

    	long totalTime = System.currentTimeMillis() - start;
    	System.out.println("Indexing took ["+totalTime+"] miliseconds for ["+numberOfDocs+"] documents");
	}
    
    /**
     * Processa para remover o indice de artistas.
     * @throws IOException 
     */
    private static void cleanerArtists(Property indexerIndexArtistsDirectory) throws IOException {
    	System.out.println("cleaning...");
    	long start = System.currentTimeMillis();
    	int numberOfDocs = 0;
    	
        String indexDirectoryPath = indexerProperties.getProperty(indexerIndexArtistsDirectory);
        File indexDirectoryFile = new File(indexDirectoryPath);
		Directory directory = FSDirectory.open(indexDirectoryFile);
        IndexReader reader = IndexReader.open(directory, false); // we don't want read-only because we are about to delete
    	
		System.out.println("full cleaning the indexed artists documents...");
		/* Deleta o diretorio do indice */
		numberOfDocs = reader.maxDoc();
		reader.close();
		System.out.println("full cleaning artists index starting to clean [" + numberOfDocs + "]...");
		FileUtils.deleteDirectory(indexDirectoryFile);
		System.out.println("full cleanned artists index!");
    }

    /**
     * Processa para indexar os itens na ordem especificada. Sempre realiza apenas uma das prioridades. Se informada FULL e o artistId apenas a Full sera executada. 
     * Se informadas letras, apenas as letras informadas serao processadas. Sempre executa apenas a ordem informada
     * @param isFull Full, prioridade 1.
     * @param lettersToUse Letras, prioridade 2.
     * @param artistId Artista, prioridade 3.
     * @param musicId Musica, prioridade 4.
     * @param lyricId Letra da musica, prioridade 5.
     * @throws Exception 
     * @throws SQLException 
     */
	private static void indexer(boolean isFull, String lettersToUse, String artistId, String musicId, String lyricId) throws SQLException, Exception {
    	System.out.println("indexing...");
    	long start = System.currentTimeMillis();
    	int numberOfDocs = 0;

    	/* FULL */
    	if (isFull) {
    		System.out.println("full indexing counting amount of docs to index...");
    		numberOfDocs = readCount(indexerProperties.getProperty(Property.INDEXER_FULL_QUERY_COUNT));
    		System.out.println("full indexing starting to index [" + numberOfDocs + "]");
    		indexData(indexerProperties.getProperty(Property.INDEXER_FULL_QUERY), numberOfDocs);

      	/* LETRAS */
    	} else if (StringUtils.isNotBlank(lettersToUse)) {
    		if (lettersToUse.length() > 15) {
    			System.out.println("WARNING! to index more than 15 letters, use FULL for faster indexing. Will be index FULL now!");
    			indexer(true, null, null, null, null);
    			return;
    		} else {
    			System.out.println("indexing letters: [" + lettersToUse + "]...");
    			for (int i = 0; i < lettersToUse.length(); i++) {
					String letter = "" + lettersToUse.charAt(i);
		    		if (letter.equals("#")) {
		    		    numberOfDocs += readCount(indexerProperties.getProperty(Property.INDEXER_DOUBLELETTER_QUERY_COUNT));
	                    System.out.println("Indexing letter: " + letter);
	                    indexData(indexerProperties.getProperty(Property.INDEXER_DOUBLELETTER_QUERY), numberOfDocs);
		    		} else {
                        numberOfDocs += readCount(indexerProperties.getProperty(Property.INDEXER_LETTER_QUERY_COUNT), letter.toLowerCase());
    		    		System.out.println("Indexing letter: " + letter);
                        indexData(indexerProperties.getProperty(Property.INDEXER_LETTER_QUERY), numberOfDocs, letter.toLowerCase());
		    		}
				}
    		}

    	/* ARTIST ID */
    	} else if (StringUtils.isNotBlank(artistId)) {
    		System.out.println("indexing artistId: [" + artistId + "]...");
    		numberOfDocs = readCount(indexerProperties.getProperty(Property.INDEXER_ARTIST_QUERY_COUNT), artistId);
    		System.out.println("indexing starting to index artistId ["+artistId+"] the number of documents [" + numberOfDocs + "]");
    		indexData(indexerProperties.getProperty(Property.INDEXER_ARTIST_QUERY), numberOfDocs, artistId);

    	/* MUSIC ID */
    	} else if (StringUtils.isNotBlank(musicId)) {
    		System.out.println("indexing musicId: [" + musicId + "]...");
    		numberOfDocs = readCount(indexerProperties.getProperty(Property.INDEXER_MUSIC_QUERY_COUNT), musicId);
    		System.out.println("indexing starting to index musicId ["+musicId+"] the number of documents [" + numberOfDocs + "]");
    		indexData(indexerProperties.getProperty(Property.INDEXER_MUSIC_QUERY), numberOfDocs, musicId);

       	/* LYRIC ID */
    	} else if (StringUtils.isNotBlank(lyricId)) {
    		System.out.println("indexing lyricId: [" + lyricId + "]...");
    		numberOfDocs = 1;
    		System.out.println("indexing starting to index lyricId ["+lyricId+"] the number of documents [" + numberOfDocs + "]");
    		indexData(indexerProperties.getProperty(Property.INDEXER_LYRIC_QUERY), numberOfDocs, lyricId);
    	}
    	
    	long totalTime = System.currentTimeMillis() - start;
    	System.out.println("Indexing took ["+totalTime+"] miliseconds for ["+numberOfDocs+"] documents");
	}
	
    /**
     * Processa para indexar os artistas.
     * @throws IOException 
     */
	private static void indexEntities(Property indexerArtistsQueryCount, Property indexerArtistsQuery, Property indexerIndexArtistsDirectory) throws SQLException, Exception {
    	System.out.println("indexing Artists...");
    	long initialTime = System.currentTimeMillis();
    	int numberOfDocs = 0;

		System.out.println("full indexing artists counting amount of docs to index...");
        numberOfDocs = readCount(indexerProperties.getProperty(indexerArtistsQueryCount));
		System.out.println("full indexing artists starting to index [" + numberOfDocs + "]");
        String query = indexerProperties.getProperty(indexerArtistsQuery);
		
		// Quantia de artistas indexadas por vez
		int fetchSize = Integer.parseInt(indexerProperties.getProperty(Property.INDEXER_DB_FETCHSIZE));
		int start = 0;
		int end = Math.min(numberOfDocs, fetchSize);
		
		JDBCIndexer.initPropsDB(indexerProperties);
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		
		try {
			connection = JDBCIndexer.getConnection();
			preparedStatement = connection.prepareStatement(query + LIMIT);

			// O arquivo de indexacao
            String indexDirectoryPath = indexerProperties.getProperty(indexerIndexArtistsDirectory);
			String stopWordsFilePath = indexerProperties.getProperty(Property.INDEXER_INDEX_STOPWORDS);
			IndexWriter writer = new IndexWriter(FSDirectory.open(new File(indexDirectoryPath)), 
			                                     new StandardAnalyzer(Version.LUCENE_30, new File(stopWordsFilePath)), 
			                                     true, 
			                                     IndexWriter.MaxFieldLength.UNLIMITED);

			// Enquanto nao atingir o numero de documentos definido
			while (start < numberOfDocs) {
    			// executa a consulta
                preparedStatement.setFetchSize(fetchSize);
                preparedStatement.setInt(1, start);
                preparedStatement.setInt(2, end);
    			resultSet = preparedStatement.executeQuery();
    			System.out.println("Indexing artists from ["+(start+1)+"] to ["+(start+end)+"]");

    			while (resultSet.next()) {
    				int columnCount = resultSet.getMetaData().getColumnCount();
    				Map<String, String> documentMap = new HashMap<String, String>();
    				for (int j = 1; j <= columnCount; j++) {
    					String columnLabel = resultSet.getMetaData().getColumnLabel(j);
    					String columnValue = resultSet.getString(j) == null ? "" : getNormalizedValue(columnLabel, resultSet.getString(j));
    					documentMap.put(columnLabel, columnValue);
    				}

    				if (!documentMap.isEmpty()) {
    					// Indexa no Lucene
    					//System.out.println(documentMap);
    					Document lyricDocument = getLyricDocument(documentMap);
    					writer.addDocument(lyricDocument);
    				}
    			}

    			start += end;
    			end = Math.min(fetchSize, numberOfDocs - start);
			}
			//writer.optimize();
			writer.close();
		} finally {
			JDBCIndexer.close(resultSet, preparedStatement, connection);
		}
		
    	long totalTime = System.currentTimeMillis() - initialTime;
    	System.out.println("Indexing artists took ["+totalTime+"] miliseconds for ["+numberOfDocs+"] documents");
	}

	/**
	 * Faz a consulta de contagem para saber quantos registros.
	 * @param query a consulta que deve retornar um "select count()"
	 * @param params parametros que serao usados na consulta "?"
	 * @return quantia de linhas (portanto a coluna 1 da linha 1 deve ser um numero)
	 * @throws SQLException
	 * @throws Exception
	 */
	private static int readCount(String query, String ... params) throws SQLException, Exception {
		JDBCIndexer.initPropsDB(indexerProperties);
		ResultSet resultset = null;
		PreparedStatement preparedStatement = null;
		Connection connection = null;

		try {
			connection = JDBCIndexer.getConnection();
			preparedStatement = connection.prepareStatement(query);
	
			// Preenche os parametros na ordem enviada
			int i = 1;
			for (String param : params) {
				preparedStatement.setString(i++, param);
			}
	
			// executa a consulta
			ResultSet resultSet = preparedStatement.executeQuery();
	
			// Obtem o resultado
			int totalLines = 0; 
			if (resultSet.next()) {
				totalLines = resultSet.getInt(1);
			}
			return totalLines;
		} finally {
			JDBCIndexer.close(resultset, preparedStatement, connection);
		}
	}

	private static void optimizeIndex() throws CorruptIndexException, LockObtainFailedException, IOException {
        JDBCIndexer.initPropsDB(indexerProperties);

        // O arquivo de indexacao
        String indexDirectoryPath = indexerProperties.getProperty(Property.INDEXER_INDEX_DIRECTORY);
        String stopWordsFilePath = indexerProperties.getProperty(Property.INDEXER_INDEX_STOPWORDS);

        IndexWriter writer = null;              
        try{
            writer = new IndexWriter(FSDirectory.open(new File(indexDirectoryPath)), 
                            new StandardAnalyzer(Version.LUCENE_30, new File(stopWordsFilePath)), 
                            false, 
                            IndexWriter.MaxFieldLength.UNLIMITED);
        } catch (FileNotFoundException e) {
            writer = new IndexWriter(FSDirectory.open(new File(indexDirectoryPath)), 
                            new StandardAnalyzer(Version.LUCENE_30, new File(stopWordsFilePath)), 
                            true, 
                            IndexWriter.MaxFieldLength.UNLIMITED);
        }
            
        writer.setUseCompoundFile(false);
        writer.setRAMBufferSizeMB(Integer.parseInt(indexerProperties.getProperty(Property.INDEXER_MEMORY_RAM_BUF)));
        writer.setMergeFactor(20);
        
        writer.optimize();
        writer.close();

	}
	
	/**
	 * Realiza a indexacao do conteudo
	 * @param query
	 * @param params
	 * @throws SQLException
	 * @throws Exception
	 */
	private static void indexData(String query, int numberOfDocs, String ... params) throws SQLException, Exception {
		JDBCIndexer.initPropsDB(indexerProperties);
		Connection connection = null;

		try {
		    // Quantia de letras indexadas por vez
		    int fetchSize = Integer.parseInt(indexerProperties.getProperty(Property.INDEXER_DB_FETCHSIZE));
		    int start = 0;
		    int end = Math.min(numberOfDocs, start + fetchSize);

		    connection = JDBCIndexer.getConnection();

			// O arquivo de indexacao
			String indexDirectoryPath = indexerProperties.getProperty(Property.INDEXER_INDEX_DIRECTORY);
			String stopWordsFilePath = indexerProperties.getProperty(Property.INDEXER_INDEX_STOPWORDS);

			IndexWriter writer = null;				
			try{
				writer = new IndexWriter(FSDirectory.open(new File(indexDirectoryPath)), 
                         		new StandardAnalyzer(Version.LUCENE_30, new File(stopWordsFilePath)), 
                         		false, 
                         		IndexWriter.MaxFieldLength.UNLIMITED);
			} catch (FileNotFoundException e) {
				writer = new IndexWriter(FSDirectory.open(new File(indexDirectoryPath)), 
		                 		new StandardAnalyzer(Version.LUCENE_30, new File(stopWordsFilePath)), 
		                 		true, 
		                 		IndexWriter.MaxFieldLength.UNLIMITED);
			}
				
			writer.setUseCompoundFile(false);
			writer.setRAMBufferSizeMB(Integer.parseInt(indexerProperties.getProperty(Property.INDEXER_MEMORY_RAM_BUF)));
			writer.setMergeFactor(20);
			
			// Enquanto nao atingir o numero de documentos definido
			while (end <= numberOfDocs) {
			    executor.execute(new DocumentIndexer(query + LIMIT, start, end, new Document(), writer, params));
    			
    			if (start == end || end == numberOfDocs) {
    			    break;
    			}
    			start = end;
    			end = Math.min(start + fetchSize, numberOfDocs);
			}

			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			writer.optimize();
			writer.close();
		} finally {
			JDBCIndexer.close(connection);
		}

	}

	private static String getNormalizedValue(String columnLabel, String value) {
	    // Campos que nao devem ser normalizados
	    if ("lyric_uri".equals(columnLabel)  ||
            "music_uri".equals(columnLabel)  ||
	        "artist_uri".equals(columnLabel) ||
            "id_lyric".equals(columnLabel)   ||
            "id_music".equals(columnLabel)   ||
            "id_artist".equals(columnLabel)  ||
            "idt_track_radio".equals(columnLabel) ||
            "url_video_clip".equals(columnLabel) ||
            "image_url".equals(columnLabel)) {
	        return value;
	    }
	        
		return StringNormalizer.normalizeText(value);
	}

	/**
	 * Obtem o documento que representa cada lyric do sistema
	 * @param documentMap Um mapa com cada campo=valor
	 * @return Objeto Document devidamente preenchido com campo=valor e configuracao adequada de cada campo.
	 */
	private static Document getLyricDocument(Map<String, String> documentMap) {
		Document doc = new Document();
		for (String key : documentMap.keySet()) {
			doc.add(new Field(key, 
					          documentMap.get(key),
					          indexerProperties.getStore(key), 
					          indexerProperties.getIndex(key), 
					          indexerProperties.getTermVector(key)));
		}
		return doc;
	}

	/**
     * Verificacao se os parametros informados sao validos individualmente utilizando obrigatoriamente o "PROVIDER"
     * @param line Linha parseada do commons-cli
     * @return Se a linha esta valida (true) ou invalida(false)
     */
    private static boolean isValidLine(CommandLine line) {
        if (line.hasOption("help") || 
            line.hasOption("projecthelp") ||
            line.hasOption("index") && (line.hasOption("full") || line.hasOption("letters") || line.hasOption("lyricId") || line.hasOption("musicId") || line.hasOption("artistId")) ||
            line.hasOption("clean") && (line.hasOption("full") || line.hasOption("letters") || line.hasOption("lyricId") || line.hasOption("musicId") || line.hasOption("artistId")) ||
            line.hasOption("index-artist") || line.hasOption("clean-artist") ||             
            line.hasOption("index-artist-images") || line.hasOption("clean-artist-images") ||             
            line.hasOption("index-integration") || line.hasOption("clean-integration") ||             
            line.hasOption("match-radio") ||
            line.hasOption("optimize")) {
            
            return true; 
        } else {
            return false;
        }
    }

    /**
     * Mostra como deve ser utilizado a aplicacao.
     */
    private static void showUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "tamandua", options );
    }

    /**
     * @return opcoes utilizadas pelo sistema 
     */
    private static Options getOptions() {
        // create the Options
        Options options = new Options();

        Option help        = new Option( "help", "print this message" );
        Option projecthelp = new Option( "projecthelp", "print project help information" );
        
        Option usedletters = OptionBuilder.withLongOpt( "letters" )
                                          .withDescription( "Letras a serem utilizadas" )
                                          .hasArg()
                                          .withArgName("[a-z0]")
                                          .create();
        Option optimize = OptionBuilder.withLongOpt( "optimize" )
                                       .withDescription( "Executa uma otimizacao dos arquivos de indices" )
                                       .create();
        Option index = OptionBuilder.withLongOpt( "index" )
                                    .withDescription( "Indica que a operacao a ser executada e de indexacao" )
                                    .create();
        Option clean = OptionBuilder.withLongOpt( "clean" )
							        .withDescription( "Indica que a operacao a ser executada e de limpeza de uma indexacao previa" )
							        .create();
        Option full = OptionBuilder.withLongOpt( "full" )
							       .withDescription( "Indica que a operacao a ser executada e de indexacao ou clean FULL, em todo o acervo" )
							       .create();
        Option lyricId = OptionBuilder.withLongOpt( "lyricId" )
                                      .withDescription( "Indica o id da lyric que sera indexada" )
                                      .hasArg()
                                      .withArgName("ID_LYRIC")
                                      .create();
        Option musicId = OptionBuilder.withLongOpt( "musicId" )
								      .withDescription( "Indica o id da musica que sera indexado (indexa todas as letras, traducoes e tablaturas da musica informada)" )
								      .hasArg()
								      .withArgName("ID_MUSIC")
								      .create();
        Option artistId = OptionBuilder.withLongOpt( "artistId" )
								       .withDescription( "Indica o id do artista que sera indexado" )
								       .hasArg()
								       .withArgName("ID_ARTIST")
								       .create();
        Option matchFile = OptionBuilder.withLongOpt( "match-radio" )
                                        .withDescription( "Arquivo csv que contem um id, nome do artista, noma da faixa e nome do album" )
                                        .hasArg()
                                        .withArgName("FILE_PATH")
                                        .create();

        Option indexArtist = OptionBuilder.withLongOpt( "index-artist" )
								        .withDescription( "Indica que sera feita a indexacao dos nomes dos artistas" )
								        .create();
        Option cleanArtist = OptionBuilder.withLongOpt( "clean-artist" )
								        .withDescription( "Indica que sera feita a limpeza dos nomes dos artistas" )
								        .create();

        Option indexArtistImages = OptionBuilder.withLongOpt( "index-artist-images" )
                                                .withDescription( "Indica que sera feita a indexacao das imagens dos artistas" )
                                                .create();
        Option cleanArtistImages = OptionBuilder.withLongOpt( "clean-artist-images" )
                                                .withDescription( "Indica que sera feita a limpeza do indice de imagens dos artistas" )
                                                .create();

        Option indexIntegration = OptionBuilder.withLongOpt( "index-integration" )
                                               .withDescription( "Indica que sera feita a indexacao dos dados para integracao com servicos terceiros como Radio UOL e YouTube" )
                                               .create();
        Option cleanIntegration = OptionBuilder.withLongOpt( "clean-integration" )
                                               .withDescription( "Indica que sera feita a indexacao dos dados para integracao com servicos terceiros como Radio UOL e YouTube" )
                                               .create();

        Option threads = OptionBuilder.withLongOpt("threads")
                                      .withDescription( "Numero de threads que serao utilizadas durante o processo.")
                                      .hasArg()
                                      .withArgName("Numero de threads. Deve ser maior que zero.")
                                      .create();

        
        // Faz o bind das opcoes criadas ao conjunto de instrucoes aceitas
        options.addOption(help);
        options.addOption(projecthelp);
        options.addOption(usedletters);
        options.addOption(index);
        options.addOption(clean);
        options.addOption(full);
        options.addOption(lyricId);
        options.addOption(musicId);
        options.addOption(artistId);
        options.addOption(matchFile);
        options.addOption(indexArtist);
        options.addOption(cleanArtist);
        options.addOption(indexIntegration);
        options.addOption(indexArtistImages);
        options.addOption(cleanArtistImages);
        options.addOption(threads);
        options.addOption(optimize);

        return options;
    }
    
    

}
