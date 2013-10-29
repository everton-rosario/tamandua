/**
 * 
 */
package br.com.tamandua.service.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Everton Rosario (erosario@gmail.com)
 *
 */
@XmlRootElement(name = "music_album_id")
public class MusicAlbumIdVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long musicId;
    private Long albumId;

    public MusicAlbumIdVO() {
        super();
    }
    public MusicAlbumIdVO(Long musicId, Long albumId) {
        super();
        this.musicId = musicId;
        this.albumId = albumId;
    }

    /**
     * @return the musicId
     */
    public Long getMusicId() {
        return musicId;
    }
    /**
     * @param musicId the musicId to set
     */
    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }
    /**
     * @return the albumId
     */
    public Long getAlbumId() {
        return albumId;
    }
    /**
     * @param albumId the albumId to set
     */
    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((albumId == null) ? 0 : albumId.hashCode());
        result = prime * result + ((musicId == null) ? 0 : musicId.hashCode());
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
        MusicAlbumIdVO other = (MusicAlbumIdVO) obj;
        if (albumId == null) {
            if (other.albumId != null)
                return false;
        } else if (!albumId.equals(other.albumId))
            return false;
        if (musicId == null) {
            if (other.musicId != null)
                return false;
        } else if (!musicId.equals(other.musicId))
            return false;
        return true;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MusicAlbumIdVO [" + (albumId != null ? "albumId=" + albumId + ", " : "") + (musicId != null ? "musicId=" + musicId : "") + "]";
    }
}
