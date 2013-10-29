package br.com.tamandua.crawler.domain.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import net.htmlparser.jericho.MasonTagTypes;
import net.htmlparser.jericho.MicrosoftTagTypes;
import net.htmlparser.jericho.PHPTagTypes;
import net.htmlparser.jericho.Source;

import org.apache.commons.lang.StringUtils;

import br.com.tamandua.crawler.domain.Artist;
import br.com.tamandua.crawler.domain.Track;

/**
 * @author Everton Rosario (erosario@gmail.com)
 */
public abstract class TrackPageParser {
    protected Artist artist;
    protected Track track;
    protected boolean lyric;
    protected boolean cypher;
    protected boolean traduction;
    protected Source source;

    /* Bloco static com os registros do Jericho */
    static {
        MicrosoftTagTypes.register();
        PHPTagTypes.register();
        PHPTagTypes.PHP_SHORT.deregister(); // remove PHP short tags otherwise they override processing instructions
        MasonTagTypes.register();
    }

    public TrackPageParser(Artist artist, Track track) throws IOException {
        this(track);
        this.artist = artist;
    }
    
    public TrackPageParser(Track track) throws IOException {
        super();
        this.track = track;
        if (StringUtils.isNotEmpty(track.getLyricHtmlBody())) {
            lyric = true;
        }
        if (StringUtils.isNotEmpty(track.getCypherHtmlBody())) {
            cypher = true;
        }
        if (StringUtils.isNotEmpty(track.getTraductionHtmlBody())) {
            traduction = true;
        }

        source = new Source(new StringReader(getHtmlBody()));

    }

    public Track getTrack() {
        return track;
    }

    /**
     * @return the lyric
     */
    public boolean isLyric() {
        return lyric;
    }

    /**
     * @return the cypher
     */
    public boolean isCypher() {
        return cypher;
    }

    /**
     * @return the traduction
     */
    public boolean isTraduction() {
        return traduction;
    }
    
    /**
     * @return O body da pagina html crawleada
     */
    public String getHtmlBody() {
        if (isLyric()) {
            return getTrack().getLyricHtmlBody();
        } else if (isTraduction()) { 
            return getTrack().getTraductionHtmlBody();
        } else if (isCypher()) { 
            return getTrack().getCypherHtmlBody();
        }
        return "";
    }


    public void getEntityTrack() {
        String artistName = getArtist().getName();
        String artistURI = getArtist().getUrl();
        String[] albumCover = getAlbumCover();
        String albumName = getAlbumName();
        String albumURI = getAlbumURI();
        List<String> artistImages = getArtistImages();
        List<String> composers = getComposers();
        String musicContent = getMusicContent();
        List<String> styles = getStyles();
        
        System.out.println("###############################");
        System.out.println("String artistName = getArtist().getName(); " + artistName);
        System.out.println("String artistURI = getArtist().getUrl(); " + artistURI);
        System.out.println("String[] albumCover = getAlbumCover(); " + albumCover);
        System.out.println("String albumName = getAlbumName(); " + albumName);
        System.out.println("String albumURI = getAlbumURI(); " + albumURI);
        System.out.println("List<String> artistImages = getArtistImages(); " + artistImages);
        System.out.println("List<String> composers = getComposers(); " + composers);
        System.out.println("String musicContent = getMusicContent(); " + musicContent);
        System.out.println("List<String> styles = getStyles(); " + styles);
        System.out.println();
        
    }

    /**
     * @return the artist
     */
    public Artist getArtist() {
        return artist;
    }

    /**
     * @param artist the artist to set
     */
    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    /**
     * @return Obtem a letra/cifra/traducao da musica de dentro do html crawleado
     */
    public abstract String getMusicContent();
    
    /**
     * @return Lista dos estilos existentes na musica crawleada
     */
    public abstract List<String> getStyles();
    
    /**
     * @return O nome do album exibido na pagina crawleada
     */
    public abstract String getAlbumName();
    
    /**
     * @return O nome do album exibido na pagina crawleada
     */
    public abstract String getAlbumURI();
    
    /**
     * @return As imagens do album, 100x100, 200x200 e a original. Os valores podem ser vazios ""
     */
    public abstract String[] getAlbumCover();
    
    /**
     * @return Obtem os compositores da letra crawleada.
     */
    public abstract List<String> getComposers();
    
    /**
     * @return Obtem a lista de url/uri de imagens do artista da musica crawleada
     */
    public abstract List<String> getArtistImages();

    /**
     * @return Lista dos tags existentes na musica crawleada que provavelmente foi colocada por usuarios
     */
    public abstract List<String> getFreeStyles();

    /**
     * @return Url do host de imagens a ser utilizado
     */
    public abstract String getImageHost();

    public abstract String getNoImageToIgnore();

}
