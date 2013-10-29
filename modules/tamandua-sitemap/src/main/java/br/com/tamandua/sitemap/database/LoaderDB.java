/**
 * Copyright
/**
 * Copyright
 * Copyright (c) - UOL Inc,
 * Todos os direitos reservados
 *
 * Este arquivo e uma propriedade confidencial do Universo Online Inc.
 * Nenhuma parte do mesmo pode ser copiada, reproduzida, impressa ou
 * transmitida por qualquer meio sem autorizacao expressa e por escrito
 * de um representante legal do Universo Online Inc.
 *
 * All rights reserved
 *
 * This file is a confidential property of Universo Online Inc.
 * No part of this file may be reproduced or copied in any form or by
 * any means without written permisson from an authorized person from
 * Universo Online Inc.
 *
 * Historico de revisoes
 * Autor                             Data        Motivo
 * --------------------------------- ----------- -----------------------
 * Fabio Dickfeldt                   21/01/2009  Class creation
 */
package br.com.tamandua.sitemap.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.tamandua.sitemap.utils.SitemapProperties;

/**
 * Suggestion loader that retrieves information from a given database
 * @author Gabriel Palacio
 *
 */
public class LoaderDB
{
	private static final Logger LOGGER = LoggerFactory.getLogger(LoaderDB.class);
	
    //------------------ attributes -----------------------
    private String username;
    private String password;
    private String connectionString;
    private String dbDriver;

    private static final String LIMIT = " LIMIT {0},{1}";

    public LoaderDB(String username, String password, String connectionString, String dbDriver) {
        this.username = username;
        this.password = password;
        this.connectionString = connectionString;
        this.dbDriver = dbDriver;
    }
    
    public List<String> getArtistUris(Integer start, Integer maxResults) {
        String limit = MessageFormat.format(LIMIT, start.toString(), maxResults.toString());
        List<String> uris = new ArrayList<String>();
        String query = SitemapProperties.getInstance().getArtistQuery();
        try {
            Connection connection = getConnection();
            ResultSet rs = connection.prepareStatement(query.concat(limit)).executeQuery();
            while (rs.next()) {
            	uris.add(rs.getString("uri"));
            }
            rs.close();
            connection.close();
        }
        catch (SQLException ex) {
        	LOGGER.error("Error loading data from DB.", ex);
        }

        return uris;
    }
    
    public List<String> getMusicUris(Integer start, Integer maxResults) {
        String limit = MessageFormat.format(LIMIT, start.toString(), maxResults.toString());
        List<String> uris = new ArrayList<String>();
        String query = SitemapProperties.getInstance().getMusicQuery();
        try {
            Connection connection = getConnection();
            ResultSet rs = connection.prepareStatement(query.concat(limit)).executeQuery();
            while (rs.next()) {
            	uris.add(rs.getString("uri"));
            }
            rs.close();
            connection.close();
        }
        catch (SQLException ex) {
        	LOGGER.error("Error loading data from DB.", ex);
        }

        return uris;
    }

    //---------------- utility methods -----------------------

    /**
     * Connects to database
     *
     * @return a connection
     */
	private Connection getConnection() {
		try {
			Class.forName(dbDriver);
			return  DriverManager.getConnection(connectionString, username, password);
		}
		catch(Exception ex) {
			LOGGER.error("Error while creating database connection." + " user: " + username + "password: " + password + " connection: " + connectionString , ex);
			return null;
		}
	}
}
