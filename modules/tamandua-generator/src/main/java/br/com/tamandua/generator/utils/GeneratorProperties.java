package br.com.tamandua.generator.utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneratorProperties extends PropertiesConfiguration {

    /* Chaves de propriedades no arquivo de propriedades */
	private static final String WEB_SERVICE_HOST = "web.service.host";
	
	private static final String VELOCITY_PROPERTIES_FILE = "file.velocity.properties";
    private static final String TOOLBOX_XML_FILE = "file.toolbox.xml";
    
    private static final String VELOCITY_TEMPLATE_HOME = "velocity.tamplate.home";
    private static final String VELOCITY_TEMPLATE_TOP_ARTISTS = "velocity.tamplate.top.artists";
    private static final String VELOCITY_TEMPLATE_TOP_MUSICS = "velocity.tamplate.top.musics";
    private static final String VELOCITY_TEMPLATE_CLOUD_ARTIST = "velocity.tamplate.cloud.artist";
    private static final String VELOCITY_TEMPLATE_CLOUD_MUSIC = "velocity.tamplate.cloud.music";
    private static final String VELOCITY_TEMPLATE_FRONT = "velocity.tamplate.front";
    private static final String VELOCITY_TEMPLATE_AZ = "velocity.tamplate.az";
    private static final String VELOCITY_TEMPLATE_LETTER = "velocity.tamplate.letter";
    private static final String VELOCITY_TEMPLATE_ARTIST = "velocity.tamplate.artist";
    private static final String VELOCITY_TEMPLATE_LYRIC = "velocity.tamplate.lyric";
    private static final String VELOCITY_TEMPLATE_TWITTER = "velocity.template.twitter";

    private static final String PAGES_DIR = "dir.pages";
    private static final String LETTER_DIR = "dir.letter";
    private static final String ARTIST_DIR = "dir.artist";
    private static final String MUSIC_DIR = "dir.music";
    private static final String INCLUDE_DIR = "dir.include";
    private static final String CONTENT_DIR = "dir.content";
    private static final String TOP_CONTENT_DIR = "dir.content.top";
    
    private static final String TOP_HOME_COUNT = "top.home.count";
    private static final String TOP_COUNT = "top.count";
    private static final String CLOUD_COUNT = "cloud.count";
    
    private static final String HOME_FILE = "file.home";
    private static final String TOP_ARTISTS_FILE = "file.top.artists";
    private static final String TOP_MUSICS_FILE = "file.top.musics";
    private static final String CLOUD_ARTIST_FILE = "file.cloud.artist";
    private static final String CLOUD_MUSIC_FILE = "file.cloud.music";
    private static final String AZ_FILE = "file.az";
    
    private static final String BUCKET_NAME = "bucket.name";
    private static final String ROOT_PAGES = "root.pages";

    /* Nome do arquivo de propriedades */
    private static final String PROPERTIES_FILE_NAME = "/generator.properties";
    
    /* Mecanismo de log */
    private static Logger LOGGER = LoggerFactory.getLogger(GeneratorProperties.class);
    
    private static GeneratorProperties instance;
    
    static {
        try {
            instance = new GeneratorProperties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * @throws ConfigurationException 
     */
    private GeneratorProperties() throws ConfigurationException {
        LOGGER.info("Loading config file...");
        load(this.getClass().getResourceAsStream(PROPERTIES_FILE_NAME));
    }    

    public String getWebServiceHost() {
        return getString(WEB_SERVICE_HOST);
    }

    public String getVelocityPropertiesFile() {
        return getString(VELOCITY_PROPERTIES_FILE);
    }

    public String getToolboxXmlFile() {
        return getString(TOOLBOX_XML_FILE);
    }
    
    public String getVelocityTemplateFront() {
        return getString(VELOCITY_TEMPLATE_FRONT);
    }

    public String getVelocityTemplateHome() {
        return getString(VELOCITY_TEMPLATE_HOME);
    }
    
    public String getVelocityTemplateTopArtists() {
        return getString(VELOCITY_TEMPLATE_TOP_ARTISTS);
    }
    
    public String getVelocityTemplateTopMusics() {
        return getString(VELOCITY_TEMPLATE_TOP_MUSICS);
    }
    
    public String getVelocityTemplateCloudArtist() {
        return getString(VELOCITY_TEMPLATE_CLOUD_ARTIST);
    }
    
    public String getVelocityTemplateCloudMusic() {
        return getString(VELOCITY_TEMPLATE_CLOUD_MUSIC);
    }
    
    public String getVelocityTemplateAZ() {
        return getString(VELOCITY_TEMPLATE_AZ);
    }
    
    public String getVelocityTemplateLetter() {
        return getString(VELOCITY_TEMPLATE_LETTER);
    }
    
    public String getVelocityTemplateArtist() {
        return getString(VELOCITY_TEMPLATE_ARTIST);
    }
    
    public String getVelocityTemplateLyric() {
        return getString(VELOCITY_TEMPLATE_LYRIC);
    }
    
    public String getPagesDir(){
    	return getString(PAGES_DIR);
    }
    
    public String getLetterDir() {
    	return getString(LETTER_DIR);
    }
    
    public String getArtistDir() {
    	return getString(ARTIST_DIR);
    }
    
    public String getMusicDir() {
    	return getString(MUSIC_DIR);
    }
    
    public String getIncludeDir() {
    	return getString(INCLUDE_DIR);
    }
    
    public String getContentDir() {
    	return getString(CONTENT_DIR);
    }
    
    public String getTopContentDir() {
    	return getString(TOP_CONTENT_DIR);
    }
    
    public int getTopHomeCount() {
    	return getInt(TOP_HOME_COUNT);
    }
    
    public int getTopCount() {
    	return getInt(TOP_COUNT);
    }

    public int getCloudCount() {
    	return getInt(CLOUD_COUNT);
    }
    
    public String getAZFile() {
    	return getString(AZ_FILE);
    }
    
    public String getHomeFile() {
    	return getString(HOME_FILE);
    }
    
    public String getTopArtistFile() {
    	return getString(TOP_ARTISTS_FILE);
    }
    
    public String getTopMusicsFile() {
    	return getString(TOP_MUSICS_FILE);
    }
    
    public String getCloudArtistFile() {
    	return getString(CLOUD_ARTIST_FILE);
    }
    
    public String getCloudMusicFile() {
    	return getString(CLOUD_MUSIC_FILE);
    }
    
    public String getBucketName() {
    	return getString(BUCKET_NAME);
    }

    public String getRootPages() {
    	return getString(ROOT_PAGES);
    }
    
    public String getVelocityTemplateTwitter() {
        return getString(VELOCITY_TEMPLATE_TWITTER);
    }

    /**
     * @return the instance
     */
    public static GeneratorProperties getInstance() {
        return instance;
    }
}
