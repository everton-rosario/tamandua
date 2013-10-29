package br.com.tamandua.sitemap.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SitemapProperties extends PropertiesConfiguration {

    /* Chaves de propriedades no arquivo de propriedades */
	public static final String DIR_NAME = "sitemap.dir.name";
	
	public static final String MAX_URLS = "sitemap.xml.maxurls";
	
	public static final String BASE_URL = "sitemap.base.url";
    
	public static final String LETTER_PREFIX = "sitemap.letter.prefix";
	public static final String ARTIST_PREFIX = "sitemap.artist.prefix";
	public static final String MUSIC_PREFIX = "sitemap.music.prefix";
	
	public static final String ARTIST_QUERY = "sitemap.artist.query";
	public static final String MUSIC_QUERY = "sitemap.music.query";
    
	public static final String DB_USER = "sitemap.database.user";
	public static final String DB_PASS = "sitemap.database.pass";
	public static final String DB_URL = "sitemap.database.url";
	public static final String DB_DRIVER = "sitemap.database.driver";
    
	public static final String LETTER_LIST = "sitemap.letter.list";

    /* Nome do arquivo de propriedades */
    private static final String PROPERTIES_FILE_NAME = "/sitemap.properties";
    
    /* Mecanismo de log */
    private static Logger LOGGER = LoggerFactory.getLogger(SitemapProperties.class);
    
    private static SitemapProperties instance;
    
    static {
        try {
            instance = new SitemapProperties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * @throws ConfigurationException 
     */
    private SitemapProperties() throws ConfigurationException {
        LOGGER.info("Loading config file...");
        load(this.getClass().getResourceAsStream(PROPERTIES_FILE_NAME));
    }    

	public String getDirName(){
		return getString(DIR_NAME);
	}	
	public Integer getMaxURLs(){
		return Integer.parseInt(getString(MAX_URLS));
	}	
    public String getBaseURL(){
    	return getString(BASE_URL);
    }    
    public String getLetterPrefix(){
    	return getString(LETTER_PREFIX);
    }
    public String getArtistPrefix(){
    	return getString(ARTIST_PREFIX);
    }
    public String getMusicPrefix(){
    	return getString(MUSIC_PREFIX);
    }    
    public String getArtistQuery(){
        return getString(ARTIST_QUERY);
    }
    public String getMusicQuery(){
        return getString(MUSIC_QUERY);
    }    
    public String getDBUser(){
    	return getString(DB_USER);
    }
    public String getDBPass(){
    	return getString(DB_PASS);
    }
    public String getDBURL(){
    	return getString(DB_URL);
    }
    public String getDBDriver(){
    	return getString(DB_DRIVER);
    }
    public List<String> getLetterList(){
    	return Arrays.asList(getString(LETTER_LIST).split(";"));
    }
    
    /**
     * @return the instance
     */
    public static SitemapProperties getInstance() {
        return instance;
    }
}
