package br.com.tamandua.searcher.ws;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.tamandua.indexer.IndexerProperties;
import br.com.tamandua.searcher.SearchManager;
import br.com.tamandua.searcher.exception.InvalidIndexException;

/**
 * @author Everton Rosario (erosario@gmail.com)
 *
 */
public class SearcherListener implements ServletContextListener {
    private static final Logger logger =  LoggerFactory.getLogger(SearcherListener.class);

    public void contextDestroyed(ServletContextEvent event) {
        logger.info("Closing SearchManager for [tamandua-searcher-ws]...");
        SearchManager.close();
        logger.info("SearchManager Closed successfuly for [tamandua-searcher-ws]!");
        logger.info("Unloaded SearcherListener for [tamandua-searcher-ws]!");
    }

    public void contextInitialized(ServletContextEvent event) {
        IndexerProperties.setFileResource(event.getServletContext().getRealPath("/WEB-INF/tamandua-indexer.properties"));
        try {
            logger.info("Loading SearchManager for [tamandua-searcher-ws]...");
            SearchManager.open();
            logger.info("SearchManager Loaded successfuly for [tamandua-searcher-ws]!");
        } catch (InvalidIndexException e) {
            e.printStackTrace();
        }
        logger.info("Loaded SearcherListener for [tamandua-searcher-ws]!");
    }

}
