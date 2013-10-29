package br.com.tamandua.service.vo;

// Generated 21/02/2010 23:04:42 by Hibernate Tools 3.2.4.GA

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * LyricVO generated by hbm2java
 */
@XmlRootElement(name = "lyric")
public class LyricVO implements java.io.Serializable {
	
	public static final String LYRIC_TYPE_ORIGINAL = "ORIGINAL";
	public static final String LYRIC_TYPE_TRADUCTION = "TRADUCTION";
	public static final String LYRIC_TYPE_ADAPTATION = "ADAPTATION";
	public static final String LYRIC_TYPE_TABLATURE = "TABLATURE";
	public static final String FLAG_DEFAULT = "S";
	public static final String FLAG_NO_DEFAULT = "N";
	
    private static final long serialVersionUID = 1L;
    private Long idLyric;
    private MusicVO music;
    private String laguage;
    private String text;
    private Long totalAccess;
    private String uri;
    private String url;
    private String lyricTitle;
    private String lyricType;
    private String provider;
    private String flag;


	public LyricVO() {
    }

    public LyricVO(MusicVO music) {
        this.music = music;
    }

    public LyricVO(MusicVO music, String laguage, String text, Long totalAccess, String uri, String url, String lyricTitle,
            String lyricType, String flag) {
        this.music = music;
        this.laguage = laguage;
        this.text = text;
        this.totalAccess = totalAccess;
        this.uri = uri;
        this.url = url;
        this.lyricTitle = lyricTitle;
        this.lyricType = lyricType;
        this.flag = flag;
    }

    public Long getIdLyric() {
        return this.idLyric;
    }

    public void setIdLyric(Long idLyric) {
        this.idLyric = idLyric;
    }

    @XmlTransient
    public MusicVO getMusic() {
        return this.music;
    }

    public void setMusic(MusicVO music) {
        this.music = music;
    }

    public String getLaguage() {
        return this.laguage;
    }

    public void setLaguage(String laguage) {
        this.laguage = laguage;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTotalAccess() {
        return this.totalAccess;
    }

    public void setTotalAccess(Long totalAccess) {
        this.totalAccess = totalAccess;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLyricTitle() {
        return this.lyricTitle;
    }

    public void setLyricTitle(String lyricTitle) {
        this.lyricTitle = lyricTitle;
    }

    public String getLyricType() {
        return this.lyricType;
    }

    public void setLyricType(String lyricType) {
        this.lyricType = lyricType;
    }

    public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "LyricVO [idMusic="
				+ idLyric
				+ ", "
				+ (laguage != null ? "laguage=" + laguage + ", " : "")
				+ (lyricTitle != null ? "lyricTitle=" + lyricTitle + ", " : "")
				+ (lyricType != null ? "lyricType=" + lyricType + ", " : "")
				+ (provider != null ? "provider=" + provider + ", " : "")
				+ (totalAccess != null ? "totalAccess=" + totalAccess + ", "
						: "") + (uri != null ? "uri=" + uri + ", " : "")
				+ (url != null ? "url=" + url : "") 
				+ (flag != null ? "flag=" + flag : "")
				+ "]";
				
	}
}
