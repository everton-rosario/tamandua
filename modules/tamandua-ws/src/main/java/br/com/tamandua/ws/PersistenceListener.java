package br.com.tamandua.ws;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Everton Rosario (erosario@gmail.com)
 *
 */
public class PersistenceListener implements ServletContextListener {
    private static final Logger logger =  LoggerFactory.getLogger(PersistenceListener.class);

    public void contextDestroyed(ServletContextEvent event) {
        logger.info("Unloaded PersistenceListener for [tamandua-persistence-ws]!");
    }

    public void contextInitialized(ServletContextEvent event) {
        logger.info("Loaded PersistenceListener for [tamandua-persistence-ws]!");
    }

}
