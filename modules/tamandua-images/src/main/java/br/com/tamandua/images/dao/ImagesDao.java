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
package br.com.tamandua.images.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.tamandua.images.utils.ImagesProperties;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.ImageVO;

/**
 * Suggestion loader that retrieves information from a given database
 * @author Gabriel Palacio
 *
 */
public class ImagesDao
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImagesDao.class);
	
    //------------------ attributes -----------------------
    private String username;
    private String password;
    private String connectionString;
    private String dbDriver;

    public ImagesDao(String username, String password, String connectionString, String dbDriver) {
        this.username = username;
        this.password = password;
        this.connectionString = connectionString;
        this.dbDriver = dbDriver;
    }
    
    public Collection<ArtistVO> getArtistImages() {
        Map<String, ArtistVO> artistMap = new HashMap<String, ArtistVO>();
        String query = ImagesProperties.getInstance().getSelectImagesQuery();
        try {
            Connection connection = getConnection();
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            while (rs.next()) {
            	ArtistVO artist;
            	String artistUri = rs.getString("artist_uri");
            	if(artistMap.containsKey(artistUri)){
            		artist = artistMap.get(artistUri);
            	} else {
            		artist = new ArtistVO();
                	artist.setIdArtist(rs.getLong("id_artist"));
                	artist.setUri(artistUri);
                	artistMap.put(artistUri, artist);
            	}
            	
            	ImageVO image = new ImageVO();
            	image.setIdImage(rs.getLong("id_image"));
            	image.setUri(rs.getString("image_uri"));
            	artist.getAllImages().add(image);
            }
            rs.close();
            connection.close();
        }
        catch (SQLException ex) {
        	LOGGER.error("Error loading data from DB.", ex);
        }

        return artistMap.values();
    }
    
    public void updateImageFlag(Long idImage, boolean isPublic) {
        String command = ImagesProperties.getInstance().getUpdateImagesFlag();
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(command);
            ps.setString(1, isPublic ? "S" : "N");
            ps.setLong(2, idImage);
            ps.execute();
            connection.close();
        }
        catch (SQLException ex) {
        	LOGGER.error("Error loading data from DB.", ex);
        }
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
