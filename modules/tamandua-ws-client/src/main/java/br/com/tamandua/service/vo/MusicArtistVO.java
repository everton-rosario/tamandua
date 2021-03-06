package br.com.tamandua.service.vo;

// Generated 21/02/2010 23:04:42 by Hibernate Tools 3.2.4.GA

import javax.xml.bind.annotation.XmlRootElement;

/**
 * MusicArtistVO generated by hbm2java
 */
@XmlRootElement(name = "music_artist")
public class MusicArtistVO implements java.io.Serializable {

	public static final String PARTICIPATION_COMPOSER = "COMPOSER";
	public static final String PARTICIPATION_INTERPRETER = "INTERPRETER";
	
    private static final long serialVersionUID = 1L;
    private MusicArtistId id;
    private MusicVO music;
    private ArtistVO artist;
    private String participation;

    public MusicArtistVO() {
    }

    public MusicArtistVO(MusicArtistId id, MusicVO music, ArtistVO artist) {
        this.id = id;
        this.music = music;
        this.artist = artist;
    }

    public MusicArtistId getId() {
        return this.id;
    }

    public void setId(MusicArtistId id) {
        this.id = id;
    }

    public MusicVO getMusic() {
        return this.music;
    }

    public void setMusic(MusicVO music) {
        this.music = music;
    }

    public ArtistVO getArtist() {
        return this.artist;
    }

    public void setArtist(ArtistVO artist) {
        this.artist = artist;
    }

	public String getParticipation() {
		return participation;
	}

	public void setParticipation(String participation) {
		this.participation = participation;
	}

	@Override
	public String toString() {
		return "MusicArtistVO ["
				+ (artist != null ? "artist=" + artist + ", " : "")
				+ (id != null ? "id=" + id + ", " : "")
				+ (music != null ? "music=" + music + ", " : "")
				+ (participation != null ? "participation=" + participation
						: "") + "]";
	}

}
