/**
 * 
 */
package br.com.tamandua.crawler.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.com.tamandua.crawler.CrawlerProperties;
import br.com.tamandua.crawler.domain.xml.IOIndex;
import br.com.tamandua.service.util.StringNormalizer;

/**
 * Class ArtistList
 *
 * @author Everton Rosario (erosario@gmail.com)
 */
@XmlRootElement(name="track-index")
public class TrackList {
    
    public static final String TYPE_DEFAULT = "default";
    public static final String TYPE_BKP = "bkp";
    public static final String TYPE_OLD = "old";
    public static final String TYPE_MERGE = "merge";
    
    private Artist artist;
    private String provider;
    private List<Track> tracks = new ArrayList<Track>();
    
    
    public TrackList() {
    }

    public TrackList(Artist artist) {
        this.artist = artist;
    }

    public void addTrack(Track track) {
    	tracks.add(track);
    }

    @XmlElement(name="artist")
	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	@XmlElement(name="tracks")
	public List<Track> getTracks() {
		return tracks;
	}

	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}
	
	@XmlAttribute(name="totalTracks")
	public int getTotalTracks() {
		return this.tracks.size();
	}

	/**
	 * @return O nome onde o arquivo deve ser persistido/foi persistido
	 */
	@XmlTransient
	public File getFile() {
		String path = CrawlerProperties.getInstance().getCrawlerIndexFiles() + System.getProperty("file.separator") + provider + System.getProperty("file.separator") + artist.getLetter();
		File filePath = new File(path);
		filePath.mkdirs();
		String fileName = (this.artist.getUrl().substring(0, this.artist.getUrl().length() -1)).toLowerCase();
		fileName = StringNormalizer.normalizeFileName(fileName);
		return new File(path + fileName + ".xml");
	}

    /**
     * @return O nome onde o arquivo deve ser persistido/foi persistido como BACKUP
     */
    @XmlTransient
	public File getBackupFile() {
		String path = CrawlerProperties.getInstance().getCrawlerIndexFiles() + System.getProperty("file.separator") + provider + System.getProperty("file.separator") + artist.getLetter();
		File filePath = new File(path);
		filePath.mkdirs();
		String fileName = (this.artist.getUrl().substring(0, this.artist.getUrl().length() -1)).toLowerCase();
        fileName = StringNormalizer.normalizeFileName(fileName);
		return new File(path + fileName + ".xml");
	}

    /**
     * @return O nome onde o arquivo deve ser persistido/foi persistido como BACKUP
     */
    @XmlTransient
    public File getOldFile() {
        String path = CrawlerProperties.getInstance().getCrawlerIndexOldFiles() + System.getProperty("file.separator") + provider + System.getProperty("file.separator") + artist.getLetter();
        File filePath = new File(path);
        filePath.mkdirs();
        String fileName = (this.artist.getUrl().substring(0, this.artist.getUrl().length() -1)).toLowerCase();
        fileName = StringNormalizer.normalizeFileName(fileName);
        return new File(path + fileName + ".xml");
    }

    /**
     * @return O nome onde o arquivo deve ser persistido/foi persistido como BACKUP
     */
    @XmlTransient
    public File getMergeFile() {
        String path = CrawlerProperties.getInstance().getCrawlerIndexMergeFiles() + System.getProperty("file.separator") + provider + System.getProperty("file.separator") + artist.getLetter();
        File filePath = new File(path);
        filePath.mkdirs();
        String fileName = (this.artist.getUrl().substring(0, this.artist.getUrl().length() -1)).toLowerCase();
        fileName = StringNormalizer.normalizeFileName(fileName);
        return new File(path + fileName + ".xml");
    }

    /**
	 * Facilitador para carregar um TrackList de um arquivo em disco
	 * @param artist Artista do qual sera carregada a lista de faixas
	 * @return
	 * @throws JAXBException
	 */
    public static TrackList loadFromArtistFile(Artist artist, String provider, String type) throws JAXBException {
        TrackList trackList = new TrackList(artist);
        trackList.setProvider(provider);
		trackList = IOIndex.readTrackIndexFromArtist(artist, provider, type);
        return trackList;
    }

    /**
     * Facilitador para carregar um TrackList de um arquivo em disco
     * @param artist Artista do qual sera carregada a lista de faixas
     * @return
     * @throws JAXBException
     */
    public static TrackList loadFromArtistFile(Artist artist, String provider) throws JAXBException {
        return loadFromArtistFile(artist, provider, TrackList.TYPE_DEFAULT);
    }

    /**
     * Facilitador para carregar um TrackList de um arquivo em disco
     * @param artist Artista do qual sera carregada a lista de faixas
     * @return
     * @throws JAXBException
     */
    public static TrackList loadFromArtistOldFile(Artist artist, String provider) {
        TrackList trackList = new TrackList(artist);
        trackList.setProvider(provider);
        try {
            trackList = IOIndex.readTrackIndexFromArtist(artist, provider, TrackList.TYPE_OLD);
        } catch (JAXBException e) {
            // Nao faz nada
            
        }
        return trackList;
    }

    /**
     * @return the provider
     */
    public String getProvider() {
        return provider;
    }

    /**
     * @param provider the provider to set
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * @param anotherTrackList
     */
    public void removeExistingTracks(TrackList anotherTrackList) {
        this.tracks.removeAll(anotherTrackList.getTracks());
    }

    public File getAppropriateFile(String type) {
        if (TYPE_BKP.equals(type)) {
            return getBackupFile();
        } else if (TYPE_OLD.equals(type)) {
            return getOldFile();
        } else if (TYPE_MERGE.equals(type)) {
            return getMergeFile();
        } else {
            return getFile();
        }
    }
}
