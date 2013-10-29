/**
 * 
 */
package br.com.tamandua.service.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Everton Rosario (erosario@gmail.com)
 */
@XmlRootElement(name = "music_album")
public class MusicAlbumVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private MusicAlbumIdVO id;
    private Integer numIndex;
    /**
     * @return the id
     */
    public MusicAlbumIdVO getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(MusicAlbumIdVO id) {
        this.id = id;
    }
    /**
     * @return the numIndex
     */
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
        MusicAlbumVO other = (MusicAlbumVO) obj;
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
        return "MusicAlbumVO [" + (id != null ? "id=" + id + ", " : "") + (numIndex != null ? "numIndex=" + numIndex : "") + "]";
    }
    
    
}
