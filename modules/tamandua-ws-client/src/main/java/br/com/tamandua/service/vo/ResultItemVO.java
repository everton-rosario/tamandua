/**
 * 
 */
package br.com.tamandua.service.vo;

import java.io.Serializable;

/**
 * @author Everton Rosario
 */
public class ResultItemVO implements Serializable {
    private Long idLyric;
    private Long idMusic;
    private String laguage;
    private String text;
    private Integer totalAccess;
    private String lyricUri;
    private String lyricTitle;
    private String lyricType;
    private String musicTitle;
    private String musicUri;
    private String musicUrl;
    private Long idArtist;
    private String artistName;
    private String artistUri;
    private String artistLetters;
    private String musicTags;
    private String idtTrackRadio;
    private String urlVideoClip;

	/**
     * @return the urlVideoClip
     */
    public String getUrlVideoClip() {
		return urlVideoClip;
	}
    /**
     * @param urlVideoClip the urlVideoClip to set
     */    
	public void setUrlVideoClip(String urlVideoClip) {
		this.urlVideoClip = urlVideoClip;
	}
	/**
     * @return the idtTrackRadio
     */
    public String getIdtTrackRadio() {
        return idtTrackRadio;
    }
    /**
     * @param idtTrackRadio the idtTrackRadio to set
     */
    public void setIdtTrackRadio(String idtTrackRadio) {
        this.idtTrackRadio = idtTrackRadio;
    }
    /**
     * @return the idLyric
     */
    public Long getIdLyric() {
        return idLyric;
    }
    /**
     * @param idLyric the idLyric to set
     */
    public void setIdLyric(Long idLyric) {
        this.idLyric = idLyric;
    }
    /**
     * @return the idMusic
     */
    public Long getIdMusic() {
        return idMusic;
    }
    /**
     * @param idMusic the idMusic to set
     */
    public void setIdMusic(Long idMusic) {
        this.idMusic = idMusic;
    }
    /**
     * @return the laguage
     */
    public String getLaguage() {
        return laguage;
    }
    /**
     * @param laguage the laguage to set
     */
    public void setLaguage(String laguage) {
        this.laguage = laguage;
    }
    /**
     * @return the text
     */
    public String getText() {
        return text;
    }
    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }
    /**
     * @return the totalAccess
     */
    public Integer getTotalAccess() {
        return totalAccess;
    }
    /**
     * @param totalAccess the totalAccess to set
     */
    public void setTotalAccess(Integer totalAccess) {
        this.totalAccess = totalAccess;
    }
    /**
     * @return the lyricUri
     */
    public String getLyricUri() {
        return lyricUri;
    }
    /**
     * @param lyricUri the lyricUri to set
     */
    public void setLyricUri(String lyricUri) {
        this.lyricUri = lyricUri;
    }
    /**
     * @return the lyricTitle
     */
    public String getLyricTitle() {
        return lyricTitle;
    }
    /**
     * @param lyricTitle the lyricTitle to set
     */
    public void setLyricTitle(String lyricTitle) {
        this.lyricTitle = lyricTitle;
    }
    /**
     * @return the lyricType
     */
    public String getLyricType() {
        return lyricType;
    }
    /**
     * @param lyricType the lyricType to set
     */
    public void setLyricType(String lyricType) {
        this.lyricType = lyricType;
    }
    /**
     * @return the musicTitle
     */
    public String getMusicTitle() {
        return musicTitle;
    }
    /**
     * @param musicTitle the musicTitle to set
     */
    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }
    /**
     * @return the musicUri
     */
    public String getMusicUri() {
        return musicUri;
    }
    /**
     * @param musicUri the musicUri to set
     */
    public void setMusicUri(String musicUri) {
        this.musicUri = musicUri;
    }
    /**
     * @return the musicUrl
     */
    public String getMusicUrl() {
        return musicUrl;
    }
    /**
     * @param musicUrl the musicUrl to set
     */
    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }
    /**
     * @return the idArtist
     */
    public Long getIdArtist() {
        return idArtist;
    }
    /**
     * @param idArtist the idArtist to set
     */
    public void setIdArtist(Long idArtist) {
        this.idArtist = idArtist;
    }
    /**
     * @return the artistName
     */
    public String getArtistName() {
        return artistName;
    }
    /**
     * @param artistName the artistName to set
     */
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
    /**
     * @return the artistUri
     */
    public String getArtistUri() {
        return artistUri;
    }
    /**
     * @param artistUri the artistUri to set
     */
    public void setArtistUri(String artistUri) {
        this.artistUri = artistUri;
    }
    /**
     * @return the artistLetters
     */
    public String getArtistLetters() {
        return artistLetters;
    }
    /**
     * @param artistLetters the artistLetters to set
     */
    public void setArtistLetters(String artistLetters) {
        this.artistLetters = artistLetters;
    }
    /**
     * @return the musicTags
     */
    public String getMusicTags() {
        return musicTags;
    }
    /**
     * @param musicTags the musicTags to set
     */
    public void setMusicTags(String musicTags) {
        this.musicTags = musicTags;
    }
}
