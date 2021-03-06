package br.com.tamandua.persistence;

// Generated 21/02/2010 23:04:42 by Hibernate Tools 3.2.4.GA

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Music generated by hbm2java
 */
@Entity
@Table(name = "music", uniqueConstraints = @UniqueConstraint(columnNames = "uri"))
public class Music implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long idMusic;
    private Artist artist;
    private String title;
    private Date dtRelease;
    private Long idCountry;
    private String uri;
    private String url;
    private Set<MusicArtist> musicArtists = new HashSet<MusicArtist>(0);
    private Set<Lyric> lyrics = new HashSet<Lyric>(0);
    
    private String flag_moderate = "N";
    private String flag_public = "S";

    public Music() {
    }

    public Music(Artist artist) {
        this.artist = artist;
    }

    public Music(Artist artist, String title, Date dtRelease, Long idCountry, String uri, String url,
            Set<MusicArtist> musicArtists, Set<Lyric> lyrics, String flag_moderate, String flag_public) {
        this.artist = artist;
        this.title = title;
        this.dtRelease = dtRelease;
        this.idCountry = idCountry;
        this.uri = uri;
        this.url = url;
        this.musicArtists = musicArtists;
        this.lyrics = lyrics;
        this.flag_moderate = flag_moderate;
        this.flag_public = flag_public;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_music", unique = true, nullable = false)
    public Long getIdMusic() {
        return this.idMusic;
    }

    public void setIdMusic(Long idMusic) {
        this.idMusic = idMusic;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_artist")
    public Artist getArtist() {
        return this.artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    @Column(name = "title", length = 500)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "dt_release", length = 10)
    public Date getDtRelease() {
        return this.dtRelease;
    }

    public void setDtRelease(Date dtRelease) {
        this.dtRelease = dtRelease;
    }

    @Column(name = "id_country")
    public Long getIdCountry() {
        return this.idCountry;
    }

    public void setIdCountry(Long idCountry) {
        this.idCountry = idCountry;
    }

    @Column(name = "uri", unique = true, length = 500)
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "music")
    public Set<MusicArtist> getMusicArtists() {
        return this.musicArtists;
    }

    public void setMusicArtists(Set<MusicArtist> musicArtists) {
        this.musicArtists = musicArtists;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "music")
    public Set<Lyric> getLyrics() {
        return this.lyrics;
    }

    public void setLyrics(Set<Lyric> lyrics) {
        this.lyrics = lyrics;
    }
    
    public void addLyric(Lyric lyric) {
        this.lyrics.add(lyric);
    }
    
    @Column(name = "flag_moderate")       
    public String getFlag_moderate() {
		return flag_moderate;
	}

	public void setFlag_moderate(String flagModerate) {
		flag_moderate = flagModerate;
	}

	@Column(name = "flag_publication")	
	public String getFlag_public() {
		return flag_public;
	}

	public void setFlag_public(String flagPublic) {
		flag_public = flagPublic;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idMusic == null) ? 0 : idMusic.hashCode());
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
        Music other = (Music) obj;
        if (idMusic == null) {
            if (other.idMusic != null)
                return false;
        } else if (!idMusic.equals(other.idMusic))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Music [" + (idMusic != null ? "idMusic=" + idMusic + ", " : "") + (title != null ? "title=" + title + ", " : "")
                + (uri != null ? "uri=" + uri : "") + "]";
    }

}
