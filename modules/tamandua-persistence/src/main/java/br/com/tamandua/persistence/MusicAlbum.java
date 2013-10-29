/**
 * 
 */
package br.com.tamandua.persistence;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Everton Rosario (erosario@gmail.com)
 */
@Entity
@Table(name = "music_album")
public class MusicAlbum implements Serializable {
    private static final long serialVersionUID = 1L;

    private MusicAlbumId id;
    private Integer numIndex;

    /**
     * 
     */
    public MusicAlbum() {
        super();
    }

    public MusicAlbum(MusicAlbumId id, Integer numIndex) {
        super();
        this.id = id;
        this.numIndex = numIndex;
    }

    /**
     * @return the id
     */
    @EmbeddedId
    @AttributeOverrides( { @AttributeOverride(name = "idMusic", column = @Column(name = "id_music", nullable = false)),
            @AttributeOverride(name = "idAlbum", column = @Column(name = "id_album", nullable = false)) })
    public MusicAlbumId getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(MusicAlbumId id) {
        this.id = id;
    }
    /**
     * @return the numIndex
     */
    @Column(name = "num_index")
    public Integer getNumIndex() {
        return numIndex;
    }
    /**
     * @param numIndex the numIndex to set
     */
    public void setNumIndex(Integer numIndex) {
        this.numIndex = numIndex;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((numIndex == null) ? 0 : numIndex.hashCode());
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
        MusicAlbum other = (MusicAlbum) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (numIndex == null) {
            if (other.numIndex != null)
                return false;
        } else if (!numIndex.equals(other.numIndex))
            return false;
        return true;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MusicAlbum [" + (id != null ? "id=" + id + ", " : "") + (numIndex != null ? "numIndex=" + numIndex : "") + "]";
    }
    
    
}
