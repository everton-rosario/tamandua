package br.com.tamandua.sitemap.generator;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.tamandua.sitemap.database.LoaderDB;
import br.com.tamandua.sitemap.utils.SitemapProperties;

/**
 *
 * @author Gabriel Palacio
 */
public class SitemapGenerator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SitemapGenerator.class);
	
    public static void generate() throws IOException {
    	LOGGER.info("Generating sitemap...");

        LoaderDB loaderDB = new LoaderDB(SitemapProperties.getInstance().getDBUser(),
                                         SitemapProperties.getInstance().getDBPass(),
                                         SitemapProperties.getInstance().getDBURL(),
                                         SitemapProperties.getInstance().getDBDriver());

        Sitemap sitemap = new Sitemap(SitemapProperties.getInstance().getBaseURL(),
                                      SitemapProperties.getInstance().getDirName(),
                                      loaderDB);

        sitemap.create(SitemapProperties.getInstance().getArtistPrefix(),
                       SitemapProperties.getInstance().getMaxURLs());

        sitemap.create(SitemapProperties.getInstance().getMusicPrefix(),
                	   SitemapProperties.getInstance().getMaxURLs());

        sitemap.createLetter(SitemapProperties.getInstance().getLetterPrefix(),
        					 SitemapProperties.getInstance().getLetterList());
        
        sitemap.createIndex();

        LOGGER.info("Finished generating sitemap!");
    }

}
