package br.com.tamandua.images.utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImagesProperties extends PropertiesConfiguration {

    /* Chaves de propriedades no arquivo de propriedades */	
	public static final String SELECT_IMAGES_QUERY = "select.images.query";
	public static final String UPDATE_IMAGES_FLAG = "update.images.flag";
	public static final String MAX_RESULTS = "max.results";
    
	public static final String DB_USER = "database.user";
	public static final String DB_PASS = "database.pass";
	public static final String DB_URL = "database.url";
	public static final String DB_DRIVER = "database.driver";

	public static final String IMAGES_BUCKET_NAME = "images.bucket.name";
	public static final String ARTIST_DIR_NAME = "artist.dir.name";

    /* Nome do arquivo de propriedades */
    private static final String PROPERTIES_FILE_NAME = "/images.properties";
    
    /* Mecanismo de log */
    private static Logger LOGGER = LoggerFactory.getLogger(ImagesProperties.class);
    
    private static ImagesProperties instance;
    
    static {
        try {
            instance = new ImagesProperties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * @throws ConfigurationException 
     */
    private ImagesProperties() throws ConfigurationException {
        LOGGER.info("Loading config file...");
        setListDelimiter(';');
        load(this.getClass().getResourceAsStream(PROPERTIES_FILE_NAME));        
    }
    
    public String getSelectImagesQuery(){
        return getString(SELECT_IMAGES_QUERY);
    }
    public String getUpdateImagesFlag(){
        return getString(UPDATE_IMAGES_FLAG);
    }
    public int getMaxResults(){
    	return getInt(MAX_RESULTS);
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

    public String getImagesBucketName(){
    	return getString(IMAGES_BUCKET_NAME);
    }
    public String getArtistDirName(){
    	return getString(ARTIST_DIR_NAME);
    }
    
    /**
     * @return the instance
     */
    public static ImagesProperties getInstance() {
        return instance;
    }
}
