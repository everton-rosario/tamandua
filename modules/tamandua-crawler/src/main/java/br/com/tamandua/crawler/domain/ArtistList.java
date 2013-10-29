/**
 * 
 */
package br.com.tamandua.crawler.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.com.tamandua.crawler.CrawlerProperties;

/**
 * Class ArtistList
 *
 * @author Everton Rosario (erosario@gmail.com)
 */
@XmlRootElement(name="artist-index")
public class ArtistList {
    public static final String TYPE_DEFAULT = "default";
    public static final String TYPE_BKP = "bkp";
    public static final String TYPE_OLD = "old";
    public static final String TYPE_MERGE = "merge";
    
    private String letter;
    private String provider;
    private List<Artist> artists = new ArrayList<Artist>();
    
    public ArtistList() {
        this(null);
    }
    
    /**
     * @param letter
     */
    public ArtistList(String letter) {
        this(letter, null);
    }

    /**
     * @param letter
     */
    public ArtistList(String letter, String provider) {
        super();
        this.letter = letter;
        this.provider = provider;
    }

    /** @return Obtem o campo letter do ArtistList. */
    @XmlAttribute
    public String getLetter() {
        return letter;
    }
    /** @param letter Ajusta o campo letter do ArtistList. */
    public void setLetter(String letter) {
        this.letter = letter;
    }
    /** @return Obtem o campo totalArtists do ArtistList. */
    @XmlAttribute
    public int getTotalArtists() {
        return artists.size();
    }
    /** @return Obtem o campo provider do ArtistList. */
    @XmlAttribute
    public String getProvider() {
        return provider;
    }
    /** @param provider Ajusta o campo provider do ArtistList. */
    public void setProvider(String provider) {
        this.provider = provider;
    }
    /** @param totalArtists Ajusta o campo totalArtists do ArtistList. */
    public void setTotalArtists(int totalArtists) {
        // do nothing
    }
    /** @return Obtem o campo artists do ArtistList. */
    @XmlElement(name="artist")
    public List<Artist> getArtists() {
        return artists;
    }
    /** @param artists Ajusta o campo artists do ArtistList. */
    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
    
    public File getAppropriateFile(String type) {
        if (TYPE_BKP.equals(type)) {
            return getBkpFile();
        } else if (TYPE_OLD.equals(type)) {
            return getOldFile();
        } else if (TYPE_MERGE.equals(type)) {
            return getMergeFile();
        } else {
            return getFile();
        }
    }
    public File getFile() {
        return new File(CrawlerProperties.getInstance().getCrawlerIndexFiles() + System.getProperty("file.separator") + provider + System.getProperty("file.separator") + this.letter.toLowerCase()+ ".xml");
    }
    public File getBkpFile() {
        return new File(CrawlerProperties.getInstance().getCrawlerIndexBkpFiles() + System.getProperty("file.separator") + provider + System.getProperty("file.separator") + this.letter.toLowerCase()+ ".xml");
    }
    public File getOldFile() {
        return new File(CrawlerProperties.getInstance().getCrawlerIndexOldFiles() + System.getProperty("file.separator") + provider + System.getProperty("file.separator") + this.letter.toLowerCase()+ ".xml");
    }
    public File getMergeFile() {
        return new File(CrawlerProperties.getInstance().getCrawlerIndexMergeFiles() + System.getProperty("file.separator") + provider + System.getProperty("file.separator") + this.letter.toLowerCase()+ ".xml");
    }
    public void addArtist(Artist artist) {
        artists.add(artist);
    }

    public Set<String> getUniqueUrls() {
        Set<String> urls = new TreeSet<String>();
        for (Artist artist : this.artists) {
            urls.add(artist.getUrl());
        }
        return urls;
    }
}
