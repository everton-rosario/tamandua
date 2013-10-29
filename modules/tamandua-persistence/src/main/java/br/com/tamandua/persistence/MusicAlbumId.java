/**
 * 
 */
package br.com.tamandua.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Everton Rosario (erosario@gmail.com)
 *
 */
@Embeddable
public class MusicAlbumId implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idMusic;
    private Long idAlbum;

    public MusicAlbumId() {
        super();
    }
    public MusicAlbumId(Long idMusic, Long idAlbum) {
        super();
        this.idMusic = idMusic;
        this.idAlbum = idAlbum;
    }

    /**
     * @return the idMusic
     */
    @Column(name="id_music")
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
     * @return the idAlbum
     */
    @Column(name="id_album")
    public Long getidAlbum() {
        return idAlbum;
    }
    /**
     * @param idAlbum the idAlbum to set
     */
    public void setidAlbum(Long idAlbum) {
        this.idAlbum = idAlbum;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idAlbum == null) ? 0 : idAlbum.hashCode());
        result = prime * result + ((idMusic == null) ? 0 : idMusic.hashCode());
        return result;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MusicAlbumId other = (MusicAlbumId) obj;
        if (idAlbum == null) {
            if (other.idAlbum != null)
                return false;
        } else if (!idAlbum.equals(other.idAlbum))
            return false;
        if (idMusic == null) {
            if (other.idMusic != null)
                return false;
        } else if (!idMusic.equals(other.idMusic))
            return false;
        return true;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MusicidAlbum [" + (idAlbum != null ? "idAlbum=" + idAlbum + ", " : "") + (idMusic != null ? "idMusic=" + idMusic : "") + "]";
    }
}
