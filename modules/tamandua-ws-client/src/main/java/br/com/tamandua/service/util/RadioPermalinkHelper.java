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
 * Everton Rosario                   07/10/2009     Criaçao da Classe
 */
package br.com.tamandua.service.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * PermalinkHelper
 * 
 * 
 * @author Everton Rosario (sma_erosario@uolinc.com)
 */
public class RadioPermalinkHelper {
    private static final Logger LOGGER = Logger.getLogger(RadioPermalinkHelper.class);
    private static final boolean IS_DEBUG_ENABLED = LOGGER.isDebugEnabled();

    public static final String RADIO_URL = "http://www.radio.uol.com.br";
    public static final String ACTION_PLAY = "play";
    public static final String ACTION_SEARCH = "search";
    public static final String ACTION_ADD = "add";

    public static enum PermalinkAction {
        PLAY(ACTION_PLAY, "action=" + ACTION_PLAY), SEARCH(ACTION_SEARCH, "action=" + ACTION_SEARCH), ADD(ACTION_ADD, "action=" + ACTION_ADD);

        private String actionName;
        private String actionParam;

        private PermalinkAction(String actionName, String actionParam) {
            this.actionName = actionName;
            this.actionParam = actionParam;
        }

        public String actionParam() {
            return this.actionParam;
        }

        public String actionName() {
            return this.actionName;
        }
    }

    public static enum PermalinkBase {
        RELEASE  (RADIO_URL + "/album",     "/album"), 
        VOLUME   (RADIO_URL + "/volume",    "/volume"), 
        TRACK    (RADIO_URL + "/musica",    "/musica"),
        ARTIST   (RADIO_URL + "/artista",   "/artista"),
        SEARCH   (RADIO_URL + "/busca",     "/busca"),
        AZ       (RADIO_URL + "/az",        "/az"),
        EDITORIAL(RADIO_URL + "/editorial", "/editorial"),
        STYLE    (RADIO_URL + "/estilo",    "/estilo"),
        PROGRAM  (RADIO_URL + "/programa",  "/programa");

        private String url;
        private String relativeUrl;

        private PermalinkBase(String url, String relativeUrl) {
            this.url = url;
            this.relativeUrl = relativeUrl;
        }

        public String url() {
            return this.url;
        }

        public String relativeUrl() {
            return this.relativeUrl;
        }

    }

    // ======================================================
    // HELPERS PARA FAIXAS
    //

    /**
     * return /album/namArtist/namRelease/idtRelease?action="namAction"
     * 
     * @param namArtist
     * @param namTrack
     * @param namAction
     * @param idtTrack
     * @return
     */
    public static String getPermalinkForTrack(String namArtist, String namTrack, String idtTrack, String namAction) {
        return getLink(
                PermalinkBase.TRACK.relativeUrl()
              + "/" + getNormalized(namArtist)
              + "/" + getNormalized(namTrack)
              + (StringUtils.isNotEmpty(idtTrack) ? "/" + idtTrack : "")
              + (StringUtils.isNotEmpty(namAction) ? "?action=" + namAction : ""));
    }

    /**
     * return http://www.radio.uol.com.br/musica/namArtist/namTrack/idtTrack?action=
     * "namAction"
     *
     * @param namArtist
     * @param namTrack
     * @param idtTrack
     * @param namAction
     * @return
     */
    public static String getCompletePermalinkForTrack(String namArtist, String namTrack, String idtTrack, String namAction) {
        return getLink(
                PermalinkBase.TRACK.url()
              + "/" + getNormalized(namArtist)
              + "/" + getNormalized(namTrack)
              + (StringUtils.isNotEmpty(idtTrack) ? "/" + idtTrack : "")
              + (StringUtils.isNotEmpty(namAction) ? "?action=" + namAction : ""));
    }


    
    

    // ======================================================
    // HELPERS PARA ALBUM
    //

    /**
     * return /album/namArtist/namRelease/idtRelease?action="namAction"
     * 
     * @param namArtist
     * @param idtRelease
     * @param namRelease
     * @param namAction
     * @return
     */
    public static String getPermalinkForRelease(String namArtist, String namRelease, String idtRelease, String namAction) {
        return getLink(
                PermalinkBase.RELEASE.relativeUrl()
              + "/" + getNormalized(namArtist)
              + "/" + getNormalized(namRelease)
              + (StringUtils.isNotEmpty(idtRelease) ? "/" + idtRelease : "")
              + (StringUtils.isNotEmpty(namAction) ? "?action=" + namAction : ""));
    }

    /**
     * return
     * http://www.radio.uol.com.br/album/namArtist/namRelease/idtRelease?action
     * ="namAction"
     *
     * @param namArtist
     * @param namRelease
     * @param idtRelease
     * @param namAction
     * @return
     */
    public static String getCompletePermalinkForRelease(String namArtist, String namRelease, String idtRelease, String namAction) {
        return getLink(
                PermalinkBase.RELEASE.url()
              + "/" + getNormalized(namArtist)
              + "/" + getNormalized(namRelease)
              + (StringUtils.isNotEmpty(idtRelease) ? "/" + idtRelease : "")
              + (StringUtils.isNotEmpty(namAction) ? "?action=" + namAction : ""));
    }

    

    // ======================================================
    // HELPERS PARA VOLUME
    //

    /**
     * return /volume/namArtist/namVolume/idtVolume?action="namAction"
     * 
     * @param namArtist
     * @param idtVolume
     * @param namVolume
     * @param namAction
     * @return
     */
    public static String getPermalinkForVolume(String namArtist, String namVolume, String idtVolume, String namAction) {
        return getLink(
                PermalinkBase.VOLUME.relativeUrl()
              + "/" + getNormalized(namArtist)
              + "/" + getNormalized(namVolume)
              + (StringUtils.isNotEmpty(idtVolume) ? "/" + idtVolume : "")
              + (StringUtils.isNotEmpty(namAction) ? "?action=" + namAction : ""));
    }

    /**
     * return
     * http://www.radio.uol.com.br/volume/namArtist/namVolume/idtVolume?action
     * ="namAction"
     *
     * @param namArtist
     * @param namVolume
     * @param idtVolume
     * @param namAction
     * @return
     */
    public static String getCompletePermalinkForVolume(String namArtist, String namVolume, String idtVolume, String namAction) {
        return getLink(
                PermalinkBase.VOLUME.url()
              + "/" + getNormalized(namArtist)
              + "/" + getNormalized(namVolume)
              + (StringUtils.isNotEmpty(idtVolume) ? "/" + idtVolume : "")
              + (StringUtils.isNotEmpty(namAction) ? "?action=" + namAction : ""));
    }


    // ======================================================
    // HELPERS PARA ARTISTA
    //

    /**
     * return /album/namArtist/idtArtist?action="namAction"
     * 
     * @param namArtist
     * @param idtArtist
     * @param namAction
     * @return
     */
    public static String getPermalinkForArtist(String namArtist, String idtArtist, String namAction) {
        if (StringUtils.isEmpty(idtArtist)) {
            LOGGER.warn("Trying to create an ARTIST PERMALINK with empty idtArtist: namArtist="+namArtist+", idtArtist="+idtArtist+", namAction="+namAction);
        }
        return getLink(
                PermalinkBase.ARTIST.relativeUrl()
              + "/" + getNormalized(namArtist)
              + (StringUtils.isNotEmpty(idtArtist) ? "/" + idtArtist : "")
              + (StringUtils.isNotEmpty(namAction) ? "?action=" + namAction : ""));
    }

    /**
     * return
     * http://www.radio.uol.com.br/album/namArtist/idtArtist?action="namAction"
     *
     * @param namArtist
     * @param idtArtist
     * @param namAction
     * @return
     */
    public static String getCompletePermalinkForArtist(String namArtist, String idtArtist, String namAction) {
        return getLink(
                PermalinkBase.ARTIST.url()
              + "/" + getNormalized(namArtist)
              + (StringUtils.isNotEmpty(idtArtist) ? "/" + idtArtist : "")
              + (StringUtils.isNotEmpty(namAction) ? "?action=" + namAction : ""));
    }
    
    
    // ======================================================
    // HELPERS PARA BUSCA
    //

    public static String getPermalinkForSearch(String searchTerm) {
        return getLink(PermalinkBase.SEARCH.relativeUrl() + "/" + getNormalized(searchTerm));
    }

    public static String getPermalinkForCategorySearch(String category, String searchTerm) {
        return getLink(PermalinkBase.SEARCH.relativeUrl() + "/" + category + "/" + getNormalized(searchTerm));
    }

    public static String getCompletePermalinkForSearch(String searchTerm) {
        return getLink(PermalinkBase.SEARCH.url() + "/" + getNormalized(searchTerm));
    }

    public static String getCompletePermalinkForCategorySearch(String category, String searchTerm) {
        return getLink(PermalinkBase.SEARCH.url() + "/" + category + "/" + getNormalized(searchTerm));
    }


    
    
    
    // ======================================================
    // HELPERS PARA AZ
    //

    public static String getPermalinkForAZ() {
        return getLink(PermalinkBase.AZ.relativeUrl());
    }

    public static String getPermalinkForAZ(String letter) {
        return getLink(PermalinkBase.AZ.relativeUrl() + "/" + getNormalized(letter) + "/1");
    }

    public static String getPermalinkForAZ(String letter, String pageNumber) {
        return getLink(PermalinkBase.AZ.relativeUrl() + "/" + getNormalized(letter) + "/" + pageNumber);
    }

    public static String getCompletePermalinkForAZ() {
        return getLink(PermalinkBase.AZ.url());
    }

    public static String getCompletePermalinkForAZ(String letter) {
        return getLink(PermalinkBase.AZ.url() + "/" + getNormalized(letter) + "/1");
    }

    public String getCompletePermalinkForAZ(String letter, String pageNumber) {
        return getLink(PermalinkBase.AZ.url() + "/" + getNormalized(letter) + "/" + pageNumber);
    }


    
    
    
    // ======================================================
    // HELPERS PARA EDITORIAL
    //

    public static String getPermalinkForEditorial() {
        return getLink(PermalinkBase.EDITORIAL.relativeUrl());
    }

    public static String getPermalinkForEditorial(String playlistName) {
        return getLink(PermalinkBase.EDITORIAL.relativeUrl() + "/" + getNormalized(playlistName));
    }

    public static String getCompletePermalinkForEditorial() {
        return getLink(PermalinkBase.EDITORIAL.url());
    }

    public static String getCompletePermalinkForEditorial(String playlistName) {
        return getLink(PermalinkBase.EDITORIAL.url() + "/" + getNormalized(playlistName));
    }


    
    
    
    // ======================================================
    // HELPERS PARA ESTILOS
    //

    public static String getPermalinkForStyle() {
        return getLink(PermalinkBase.STYLE.relativeUrl());
    }

    public static String getPermalinkForStyle(String styleName) {
        return getLink(PermalinkBase.STYLE.relativeUrl() + "/" + getNormalized(styleName));
    }

    public static String getPermalinkForStyle(String styleName, String selectedTab) {
        return getLink(PermalinkBase.STYLE.relativeUrl() + "/" + getNormalized(styleName) + "/" + selectedTab);
    }

    public static String getPermalinkForStyle(String styleName, String selectedTab, int page) {
        return getLink(PermalinkBase.STYLE.relativeUrl() + "/" + getNormalized(styleName) + "/" + selectedTab + "?page=" + page);
    }
    
    public static String getCompletePermalinkForStyle() {
        return getLink(PermalinkBase.STYLE.url());
    }

    public static String getCompletePermalinkForStyle(String styleName) {
        return getLink(PermalinkBase.STYLE.url() + "/" + getNormalized(styleName));
    }

    public static String getCompletePermalinkForStyle(String styleName, String selectedTab) {
        return getLink(PermalinkBase.STYLE.url() + "/" + getNormalized(styleName) + "/" + selectedTab);
    }

    public static String getCompletePermalinkForStyle(String styleName, String selectedTab, int page) {
        return getLink(PermalinkBase.STYLE.url() + "/" + getNormalized(styleName) + "/" + selectedTab + "?page=" + page);
    }


    
    
    
    // ======================================================
    // HELPERS PARA PROGRAMAS
    //

    public static String getPermalinkForProgram() {
        return getLink(PermalinkBase.PROGRAM.relativeUrl());
    }
    
    public static String getPermalinkForProgram(String programName) {
        return getLink(PermalinkBase.PROGRAM.relativeUrl() + "/" + getNormalized(programName));
    }    

    public static String getPermalinkForProgram(String programName, int page) {
        return getLink(PermalinkBase.PROGRAM.relativeUrl() + "/" + getNormalized(programName) + "/" + page);
    }

    public static String getPermalinkForProgramEdition(String programName, long idtEdition) {
        return getLink(PermalinkBase.PROGRAM.relativeUrl() + "/" + getNormalized(programName) + "/edicao/" + idtEdition);
    }

    public static String getCompletePermalinkForProgram() {
        return getLink(PermalinkBase.PROGRAM.url());
    }
    
    public static String getCompletePermalinkForProgram(String programName) {
        return getLink(PermalinkBase.PROGRAM.url() + "/" + getNormalized(programName));
    }    

    public static String getCompletePermalinkForProgram(String programName, int page) {
        return getLink(PermalinkBase.PROGRAM.url() + "/" + getNormalized(programName) + "/" + page);
    }

    public static String getCompletePermalinkForProgramEdition(String programName, long idtEdition) {
        return getLink(PermalinkBase.PROGRAM.url() + "/" + getNormalized(programName) + "/edicao/" + idtEdition);
    }
    
    /// METODOS UTILITARIOS
    /**
     * @param textToNormalize Texto a ser normalizado
     * @return Obtem o conteúdo segundo as regras do @see {@link StringNormalizer#normalize(String)}, e em seguida o conteúdo é encodado como URL.
     */
    public static String getNormalized(String textToNormalize) {
        String normalized = StringNormalizer.normalizeURL(textToNormalize);
        String encoded = "";
        try {
            encoded = URLEncoder.encode(normalized, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            encoded = normalized;
        }
        return encoded;
    }

    /**
     * Não realiza nenhuma operação no momento.
     * @param url
     * @return
     */
    private static String getLink(String url) {
        return url;
    }

}
