package br.com.tamandua.redirect.utils;

import java.io.InputStream;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedirectProperties extends PropertiesConfiguration {

    /* Chaves de propriedades no arquivo de propriedades */
	private static final String URL_REDIRECT = "url.redirect";
	private static final String URL_ARTIST_IMAGES = "url.artist.images";
	private static final String PATH_FILES = "path.files";
	private static final String PAGES_SOURCE = "pages.source";
	private static final String USING_TEMPLATE = "using.template";

    /* Nome do arquivo de propriedades */
    private static final String PROPERTIES_FILE_NAME = "redirect.properties";
    
    /* Mecanismo de log */
    private static Logger LOGGER = LoggerFactory.getLogger(RedirectProperties.class);
    
    private static RedirectProperties instance;
    
    static {
        try {
            instance = new RedirectProperties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * @throws ConfigurationException 
     */
    private RedirectProperties() throws ConfigurationException {
        LOGGER.info("Loading config file...");
        InputStream resourceAsStream = this.getClass().getResourceAsStream(PROPERTIES_FILE_NAME);
        if (resourceAsStream == null) {
            resourceAsStream = this.getClass().getResourceAsStream("/"+PROPERTIES_FILE_NAME);
        }
        load(resourceAsStream);
    }    

    public String getUrlRedirect() {
        return getString(URL_REDIRECT);
    }
    
    public String getUrlArtistImages() {
        return getString(URL_ARTIST_IMAGES);
    }

    /**
     * @return the instance
     */
    public static RedirectProperties getInstance() {
        return instance;
    }

	public String getPathFiles() {
		return getString(PATH_FILES);
	}
	
	public String getPagesSource(){
		return getString(PAGES_SOURCE);
	}
	
	public boolean isUsingTemplate(){
		return new Boolean(getString(USING_TEMPLATE));
	}
}
