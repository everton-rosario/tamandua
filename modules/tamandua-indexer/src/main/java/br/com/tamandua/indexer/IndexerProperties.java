/**
 * 
 */
package br.com.tamandua.indexer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;

/**
 * @author Everton Fernandes Rosario (erosario@gmail.com)
 */
public class IndexerProperties {

	/*-------------------- enumerations --------------------*/
	public static enum Property {
		INDEXER_ARTIST_QUERY("tamandua.indexer.artist"), 
		INDEXER_ARTIST_QUERY_COUNT("tamandua.indexer.artist.count"),
		INDEXER_LYRIC_QUERY("tamandua.indexer.lyric"),
		INDEXER_MUSIC_QUERY("tamandua.indexer.music"),
		INDEXER_MUSIC_QUERY_COUNT("tamandua.indexer.music.count"),
		INDEXER_LETTER_QUERY_COUNT("tamandua.indexer.letter.count"),
		INDEXER_LETTER_QUERY("tamandua.indexer.letter"), 
		INDEXER_DOUBLELETTER_QUERY_COUNT("tamandua.indexer.multiletter.count"),
        INDEXER_DOUBLELETTER_QUERY("tamandua.indexer.multiletter"),
		INDEXER_FULL_QUERY_COUNT("tamandua.indexer.full.count"), 
		INDEXER_FULL_QUERY("tamandua.indexer.full"),
		INDEXER_ARTISTS_QUERY("tamandua.indexer.artists"),
		INDEXER_ARTISTS_QUERY_COUNT("tamandua.indexer.artists.count"),
        INDEXER_ARTIST_IMAGES_QUERY("tamandua.indexer.artist_images"),
        INDEXER_ARTIST_IMAGES_QUERY_COUNT("tamandua.indexer.artist_images.count"),
        INDEXER_INTEGRATION_QUERY("tamandua.indexer.integration"),
        INDEXER_INTEGRATION_QUERY_COUNT("tamandua.indexer.integration.count"),
		
		INDEXER_INDEX_DIRECTORY("tamandua.index.directory"),
		INDEXER_INDEX_ARTISTS_DIRECTORY("tamandua.index.artists.directory"),
		INDEXER_INDEX_INTEGRATION_DIRECTORY("tamandua.index.integration.directory"),
		INDEXER_INDEX_ARTIST_IMAGES_DIRECTORY("tamandua.index.artist_images.directory"),
		INDEXER_INDEX_STOPWORDS("tamandua.index.stopwords.file"),
		INDEXER_MEMORY_RAM_BUF("tamandua.index.memory_ram_buff"),
		
		INDEXER_DB_URL("tamandua.db.url"),
		INDEXER_DB_DRIVER("tamandua.db.driver"),
		INDEXER_DB_USER("tamandua.db.login"),
		INDEXER_DB_PASSWD("tamandua.db.password"), 
		INDEXER_DB_FETCHSIZE("tamandua.db.fetchsize"); 

		/**
		 * property name
		 */
		private String name;

		Property(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	private Properties properties;

	/*-------------------- SINGLETON - START --------------------*/
	private static IndexerProperties instance = new IndexerProperties();

	public static IndexerProperties getInstance() {
		return IndexerProperties.instance;
	}

	/**
	 * private costructor
	 */
	private IndexerProperties() {
		super();
		this.properties = new Properties();
	}

	/*-------------------- SINGLETON - END --------------------*/

	/**
	 * Defines the properties file location and loads it.
	 * 
	 * @param fileResource
	 */
	public static void setFileResource(String fileResource) {
		IndexerProperties.instance.init(fileResource);
	}
	
	/**
	 * Defines the properties file location and loads it.
	 * 
	 * @param fileResource
	 */
	public static void setFileResource(InputStream fileResource) {
		IndexerProperties.instance.init(fileResource);
	}

	public Properties getProperties() {
		return properties;
	}

	/**
	 * Gets the value of the given property.
	 * 
	 * @param property
	 *            The property chosed from {@link Property} enumeration
	 * @return The propertie value, <code>null</code> if it cannot be found
	 */
	public static String getProperty(Property property) {
		return IndexerProperties.instance.getProperties().getProperty(
				property.getName());
	}

	/**
	 * Gets the value of the given property.
	 * 
	 * @param property
	 *            The property chosed from {@link Property} enumeration
	 * @param defaultValue
	 *            the default value to be used if the property cannot be found.
	 * @return The propertie value, <code>defaultValue</code> if it cannot be
	 *         found.
	 */
	public static String getProperty(Property property, String defaultValue) {
		return IndexerProperties.instance.getProperties().getProperty(
				property.getName(), defaultValue);
	}

	public void init(String fileResource) {
		try {
			properties.load(new FileInputStream(fileResource));
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public void init(InputStream fileResource) {
		try {
			properties.load(fileResource);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public boolean isReady() {

		if (properties != null && !properties.isEmpty()) {
			return true;
		}

		return false;
	}
	
	public static void main(String[] args) {
		IndexerProperties.setFileResource(Thread.currentThread().getClass().getResource("/tamandua-indexer.properties").getPath());
		String artistQuery = IndexerProperties.getInstance().getProperty(IndexerProperties.Property.INDEXER_ARTIST_QUERY);
		System.out.println(artistQuery);
	}

	public Store getStore(String key) {
		String storeValue = IndexerProperties.instance.getProperties().getProperty(MessageFormat.format("tamandua.{0}.store", key));
		if ("YES".equals(storeValue)) {
			return Store.YES;
		} else {
			return Store.NO;
		} 
	}

	public Index getIndex(String key) {
		String indexValue = IndexerProperties.instance.getProperties().getProperty(MessageFormat.format("tamandua.{0}.index", key));
		if ("NO".equals(indexValue)) {
			return Index.NO;

		} if ("ANALYZED".equals(indexValue)) {
			return Index.ANALYZED;

		} if ("NOT_ANALYZED".equals(indexValue)) {
			return Index.NOT_ANALYZED;

		} if ("NOT_ANALYZED_NO_NORMS".equals(indexValue)) {
			return Index.NOT_ANALYZED_NO_NORMS;
		
		} if ("ANALYZED_NO_NORMS".equals(indexValue)) {
			return Index.ANALYZED_NO_NORMS;
		
		} else {
			return Index.NO;
		}
	}

	public TermVector getTermVector(String key) {
		String termVectorValue = IndexerProperties.instance.getProperties().getProperty(MessageFormat.format("tamandua.{0}.termvector", key));
		if ("NO".equals(termVectorValue)) {
			return TermVector.NO;

		} if ("YES".equals(termVectorValue)) {
			return TermVector.YES;

		} if ("WITH_POSITIONS".equals(termVectorValue)) {
			return TermVector.WITH_POSITIONS;

		} if ("WITH_OFFSETS".equals(termVectorValue)) {
			return TermVector.WITH_OFFSETS;
		
		} if ("WITH_POSITIONS_OFFSETS".equals(termVectorValue)) {
			return TermVector.WITH_POSITIONS_OFFSETS;
		
		} else {
			return TermVector.NO;
		}
	}
}
