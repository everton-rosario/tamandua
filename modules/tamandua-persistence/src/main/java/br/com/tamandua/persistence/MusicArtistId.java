package br.com.tamandua.persistence;

// Generated 21/02/2010 23:04:42 by Hibernate Tools 3.2.4.GA

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MusicArtistId generated by hbm2java
 */
@Embeddable
public class MusicArtistId implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Long idMusic;
    private Long idArtist;

    public MusicArtistId() {
    }

    public MusicArtistId(long idMusic, long idArtist) {
        this.idMusic = idMusic;
        this.idArtist = idArtist;
    }

    @Column(name = "id_music", nullable = false)
    public Long getIdMusic() {
        return this.idMusic;
    }

    public void setIdMusic(Long idMusic) {
        this.idMusic = idMusic;
    }

    @Column(name = "id_artist", nullable = false)
    public Long getIdArtist() {
        return this.idArtist;
    }

    public void setIdArtist(Long idArtist) {
        this.idArtist = idArtist;
    }


    @Override
    public String toString() {
        return "MusicArtistId [idArtist=" + idArtist + ", idMusic=" + idMusic + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idArtist == null) ? 0 : idArtist.hashCode());
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
        MusicArtistId other = (MusicArtistId) obj;
        if (idArtist == null) {
            if (other.idArtist != null)
                return false;
        } else if (!idArtist.equals(other.idArtist))
            return false;
        if (idMusic == null) {
            if (other.idMusic != null)
                return false;
        } else if (!idMusic.equals(other.idMusic))
            return false;
        return true;
    }

}
