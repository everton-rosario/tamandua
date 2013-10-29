/**
 * 
 */
package br.com.tamandua.persistence;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Everton Rosario (erosario@gmail.com)
 */
@Entity
@Table(name = "music_artist")
public class ArtistImage {
    private Long idArtistImage;
    private Long idArtist;
    private Long idImage;

    /**
     * @return the idArtistImage
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id_artist_image", unique = true, nullable = false)
    public Long getIdArtistImage() {
        return idArtistImage;
    }
    /**
     * @param idArtistImage the idArtistImage to set
     */
    public void setIdArtistImage(Long idArtistImage) {
        this.idArtistImage = idArtistImage;
    }
    /**
     * @return the idArtist
     */
    @Column(name="")
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
     * @return the idImage
     */
    public Long getIdImage() {
        return idImage;
    }
    /**
     * @param idImage the idImage to set
     */
    public void setIdImage(Long idImage) {
        this.idImage = idImage;
    }
    
    
}
