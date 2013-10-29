package br.com.tamandua.crawler;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.tamandua.crawler.domain.Artist;
import br.com.tamandua.crawler.domain.Track;
import br.com.tamandua.crawler.domain.xml.TrackPageParser;
import br.com.tamandua.crawler.letras.LetrasArtistIndex;
import br.com.tamandua.crawler.letras.LetrasTrackPageParser;
import br.com.tamandua.crawler.musicacom.MusicaComArtistIndex;
import br.com.tamandua.crawler.terra.TerraArtistIndex;
import br.com.tamandua.crawler.terra.TerraTrackPageParser;
import br.com.tamandua.crawler.traducidas.TraducidasArtistIndex;
import br.com.tamandua.crawler.vagalume.VagalumeArtistIndex;
import br.com.tamandua.crawler.vagalume.VagalumeTrackPageParser;

/**
 * @author Everton Rosario (erosario@gmail.com)
 */
public class CrawlerProperties extends PropertiesConfiguration {

    /* Chaves de propriedades no arquivo de propriedades */
    private static final String WEB_SERVICE_HOST = "tamandua.ws.host";
    private static final String CRAWLER_INDEX_FILES = "crawler.index.files";
    private static final String CRAWLER_INDEX_BKP_FILES = "crawler.index-bkp.files";
    private static final String CRAWLER_INDEX_OLD_FILES = "crawler.index.old.files";
    private static final String CRAWLER_INDEX_MERGE_FILES = "crawler.index.merge.files";
    private static final String CRAWLER_ERROR_LOADER_FILES = "crawler.error.loader.files";
    private static final String CRAWLER_IMAGE_FILES = "crawler.image.files";

    private static final String CRAWLER_PROVIDER_HOST = "crawler.provider.{0}.host";
    private static final String CRAWLER_PROVIDER_IMAGE_HOST = "crawler.provider.{0}.image.host";
    private static final String CRAWLER_PROVIDER_ARTIST_INDEX = "crawler.provider.{0}.artist.index.class";
    private static final String CRAWLER_PROVIDER_TRACK_PARSER = "crawler.provider.{0}.track.parser.class";

    /* Nome do arquivo de propriedades */
    private static final String PROPERTIES_FILE_NAME = "crawler.properties";
    
    /* Mecanismo de log */
    private static Logger LOGGER = LoggerFactory.getLogger(CrawlerProperties.class);
    
    private static CrawlerProperties instance;
    
    static {
        try {
            instance = new CrawlerProperties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * @throws ConfigurationException 
     */
    private CrawlerProperties() throws ConfigurationException {
        String crawlerHome = System.getenv("CRAWLER_HOME");
        if (StringUtils.isEmpty(crawlerHome)) {
            throw new ConfigurationException("Variable CRAWLER_HOME was not set. Set it to the home dir where is or will be the index and letters.");
        }
        File crawlerHomePath = new File(crawlerHome);
        if (!crawlerHomePath.exists()) {
            LOGGER.warn("Path from environment variable CRAWLER_HOME does not exists. Trying to create {}...", crawlerHome);
            if (!crawlerHomePath.mkdirs()) {
                throw new ConfigurationException("Could not create the CRAWLER_HOME, try creating yourself and check writing permissions for the user running the application.");
            }
            LOGGER.warn("Path from environment variable CRAWLER_HOME created SUCCESSFULLY!");
        }
            
        File crawlerPropertiesFile = new File(crawlerHome + System.getProperty("file.separator") + PROPERTIES_FILE_NAME);
        if (!crawlerPropertiesFile.exists()) {
            LOGGER.warn("File configuration 'crawler.properties' does not exists in CRAWLER_HOME, creating the default one...");
            createDefaultConfigFile(crawlerPropertiesFile);
        }

        LOGGER.info("Loading config file...");
        load(crawlerPropertiesFile);
    }

    private void createDefaultConfigFile(File crawlerPropertiesFile) throws ConfigurationException {
    	
    	setProperty(WEB_SERVICE_HOST, "http://ws.tocaletra.com.br/tamandua-ws");
    	
        String fileSeparator = System.getProperty("file.separator");
        setProperty(CRAWLER_INDEX_FILES, fileSeparator + "export" + fileSeparator + "crawled" + fileSeparator + "index");
        setProperty(CRAWLER_INDEX_BKP_FILES, fileSeparator + "export" + fileSeparator + "crawled" + fileSeparator + "index-bkp");
        setProperty(CRAWLER_INDEX_OLD_FILES, fileSeparator + "export" + fileSeparator + "crawled_old" + fileSeparator + "index");
        setProperty(CRAWLER_INDEX_MERGE_FILES, fileSeparator + "export" + fileSeparator + "crawled_merge" + fileSeparator + "index");
        setProperty(CRAWLER_ERROR_LOADER_FILES, fileSeparator + "export" + fileSeparator + "crawled" + fileSeparator + "loader-error");
        setProperty(CRAWLER_IMAGE_FILES, fileSeparator + "export" + fileSeparator + "htdocs" + fileSeparator + "static.img.com");

        setProperty(MessageFormat.format(CRAWLER_PROVIDER_HOST, "vagalume"), "www.vagalume.com.br");
        setProperty(MessageFormat.format(CRAWLER_PROVIDER_IMAGE_HOST, "vagalume"), "http://static4.vagalume.uol.com.br");
        setProperty(MessageFormat.format(CRAWLER_PROVIDER_ARTIST_INDEX, "vagalume"), VagalumeArtistIndex.class.getCanonicalName());
        setProperty(MessageFormat.format(CRAWLER_PROVIDER_TRACK_PARSER, "vagalume"), VagalumeTrackPageParser.class.getCanonicalName());

        setProperty(MessageFormat.format(CRAWLER_PROVIDER_HOST, "terra"), "letras.terra.com.br");
        setProperty(MessageFormat.format(CRAWLER_PROVIDER_IMAGE_HOST, "terra"), "http://letras.terra.com.br");
        setProperty(MessageFormat.format(CRAWLER_PROVIDER_ARTIST_INDEX, "terra"), TerraArtistIndex.class.getCanonicalName());
        setProperty(MessageFormat.format(CRAWLER_PROVIDER_TRACK_PARSER, "terra"), TerraTrackPageParser.class.getCanonicalName());

        setProperty(MessageFormat.format(CRAWLER_PROVIDER_HOST, "letras"), "www.letras.com.br");
        setProperty(MessageFormat.format(CRAWLER_PROVIDER_ARTIST_INDEX, "letras"), LetrasArtistIndex.class.getCanonicalName());
        setProperty(MessageFormat.format(CRAWLER_PROVIDER_TRACK_PARSER, "letras"), LetrasTrackPageParser.class.getCanonicalName());
        
        setProperty(MessageFormat.format(CRAWLER_PROVIDER_HOST, "traducidas"), "www.traducidas.com.ar");
        setProperty(MessageFormat.format(CRAWLER_PROVIDER_ARTIST_INDEX, "traducidas"), TraducidasArtistIndex.class.getCanonicalName());

        setProperty(MessageFormat.format(CRAWLER_PROVIDER_HOST, "musicacom"), "www.musica.com");
        setProperty(MessageFormat.format(CRAWLER_PROVIDER_ARTIST_INDEX, "musicacom"), MusicaComArtistIndex.class.getCanonicalName());

        save(crawlerPropertiesFile);
    }
    

    public String getCrawlerIndexBkpFiles() {
        return getString(CRAWLER_INDEX_BKP_FILES);
    }

    public String getCrawlerIndexFiles() {
        return getString(CRAWLER_INDEX_FILES);
    }

    public String getCrawlerIndexOldFiles() {
        return getString(CRAWLER_INDEX_OLD_FILES);
    }

    public String getCrawlerIndexMergeFiles() {
        return getString(CRAWLER_INDEX_MERGE_FILES);
    }

    public String getCrawlerImageFiles() {
        return getString(CRAWLER_IMAGE_FILES);
    }
    
    public String getWebServiceHost() {
    	return getString(WEB_SERVICE_HOST);
    }

    public IProviderArtistIndex getCrawlerProviderArtistIndex(String provider) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class clazz = Class.forName(getString(MessageFormat.format(CRAWLER_PROVIDER_ARTIST_INDEX, provider)));
        return (IProviderArtistIndex) clazz.newInstance();
    }

    public TrackPageParser getCrawlerProviderTrackParser(String provider, Artist artist, Track track) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
    	Class[] paramTypes = new Class[] { Artist.class, Track.class };
        Class clazz = Class.forName(getString(MessageFormat.format(CRAWLER_PROVIDER_TRACK_PARSER, provider)));
        Constructor constructor = clazz.getConstructor(paramTypes);
        return (TrackPageParser) constructor.newInstance(artist, track);
    }

    public String getCrawlerProviderHost(String provider) {
        return getString(MessageFormat.format(CRAWLER_PROVIDER_HOST, provider));
    }
    
    public String getCrawlerProviderImageHost(String provider) {
        return getString(MessageFormat.format(CRAWLER_PROVIDER_IMAGE_HOST, provider));
    }

    /**
     * @return the instance
     */
    public static CrawlerProperties getInstance() {
        return instance;
    }

    public File getErrorLoadDirectory() {
        return new File(getString(CRAWLER_ERROR_LOADER_FILES));
    }



//    public static void main(String[] args) {
//        CrawlerProperties crawlerProperties = CrawlerProperties.getInstance();
//        String crawlerIndexProperty = crawlerProperties.getCrawlerIndexFiles();
//        String crawlerIndexBkpProperty = crawlerProperties.getCrawlerIndexBkpFiles();
//        System.out.println(crawlerIndexProperty + " " + crawlerIndexBkpProperty);
//    }
}
