package br.com.tamandua.persistence;

// Generated 21/02/2010 23:04:42 by Hibernate Tools 3.2.4.GA

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Lyric generated by hbm2java
 */
@Entity
@Table(name = "lyric")
public class Lyric implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long idLyric;
    private Music music;
    private String laguage;
    private String text;
    private Long totalAccess;
    private String uri;
    private String url;
    private String lyricTitle;
    private String lyricType;
    private String provider;
    private String flag;

    public Lyric() {
    }

    public Lyric(Music music) {
        this.music = music;
    }

    public Lyric(Music music, String laguage, String text, Long totalAccess, String uri, String url, String lyricTitle,
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

    /**
     * @return the idLyric
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_lyric", unique = true, nullable = false)
    public Long getIdLyric() {
        return idLyric;
    }

    /**
     * @param idLyric the idLyric to set
     */
    public void setIdLyric(Long idLyric) {
        this.idLyric = idLyric;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_music")
    public Music getMusic() {
        return this.music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    @Column(name = "laguage", length = 50)
    public String getLaguage() {
        return this.laguage;
    }

    public void setLaguage(String laguage) {
        this.laguage = laguage;
    }

    @Column(name = "text")
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Column(name = "total_access")
    public Long getTotalAccess() {
        return this.totalAccess;
    }

    public void setTotalAccess(Long totalAccess) {
        this.totalAccess = totalAccess;
    }

    @Column(name = "uri", length = 1000)
    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Column(name = "url", length = 1500)
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "lyric_title", length = 500)
    public String getLyricTitle() {
        return this.lyricTitle;
    }

    public void setLyricTitle(String lyricTitle) {
        this.lyricTitle = lyricTitle;
    }

    @Column(name = "lyric_type", length = 10)
    public String getLyricType() {
        return this.lyricType;
    }


	public void setLyricType(String lyricType) {
        this.lyricType = lyricType;
    }

	@Column(name = "provider", length = 50)
	public String getProvider() {
		return provider;
	}
	
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	@Column(name = "original_flag")
    public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idLyric == null) ? 0 : idLyric.hashCode());
        return result;
    }

	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Lyric other = (Lyric) obj;
        if (idLyric == null) {
        	return false;
        } else if (!idLyric.equals(other.idLyric))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Lyric [" + (idLyric != null ? "idLyric=" + idLyric + ", " : "") + (lyricTitle != null ? "lyricTitle=" + lyricTitle + ", " : "")
                + (lyricType != null ? "lyricType=" + lyricType + ", " : "") + (uri != null ? "uri=" + uri : "") + "]";
    }

}
