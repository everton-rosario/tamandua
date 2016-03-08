package br.com.tamandua.persistence;

// Generated 21/02/2010 23:04:42 by Hibernate Tools 3.2.4.GA

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * ArtistDiscography generated by hbm2java
 */
@Entity
@Table(name = "artist_discography")
public class ArtistDiscography implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private ArtistDiscographyId id;
    private Artist artist;
    private Album album;

    public ArtistDiscography() {
    }

    public ArtistDiscography(ArtistDiscographyId id, Artist artist, Album album) {
        this.id = id;
        this.artist = artist;
        this.album = album;
    }

    @EmbeddedId
    @AttributeOverrides( { @AttributeOverride(name = "idAlbum", column = @Column(name = "id_album", nullable = false)),
            @AttributeOverride(name = "idArtist", column = @Column(name = "id_artist", nullable = false)) })
    public ArtistDiscographyId getId() {
        return this.id;
    }

    public void setId(ArtistDiscographyId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_artist", nullable = false, insertable = false, updatable = false)
    public Artist getArtist() {
        return this.artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_album", nullable = false, insertable = false, updatable = false)
    public Album getAlbum() {
        return this.album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

}