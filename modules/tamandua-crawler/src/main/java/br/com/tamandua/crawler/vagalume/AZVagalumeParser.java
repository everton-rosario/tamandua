package br.com.tamandua.crawler.vagalume;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import br.com.tamandua.crawler.domain.Artist;
import br.com.tamandua.crawler.domain.ArtistList;
import br.com.tamandua.crawler.domain.Track;
import br.com.tamandua.crawler.domain.TrackList;
import br.com.tamandua.crawler.parser.AbstractParser;

/**
 * @author Everton Rosario (erosario@gmail.com)
 *
 */
public class AZVagalumeParser extends AbstractParser {

    public AZVagalumeParser(InputStream is) throws IOException {
        super(is);
    }

    public AZVagalumeParser(Reader r) throws IOException {
        super(r);
    }

    public AZVagalumeParser(Source source) {
        super(source);
    }

    public AZVagalumeParser(String htmlBody) throws IOException {
        super(htmlBody);
    }

    public void fillArtistIndex(ArtistList letterArtists) {
        Element browselist = getSource().getFirstElement("class", "ordemAlfabetica", false);
        List<Element> liArtists = browselist.getAllElements("li");
        for (Element li : liArtists) {
            if (li != null) {
                Element link = li.getFirstElement("a");
                if (link != null) {
                    String url = link.getAttributeValue("href");
                    String name = link.getFirstElement("a").getTextExtractor().toString();
                    name = name.substring(2);
                    Artist artist = new Artist(name, letterArtists.getLetter(), url);
                    letterArtists.addArtist(artist);
                }
            }
        }
    }

    public void fillTrackIndex(TrackList trackList) {
        // Todas as musicas do artista (separadas por a-z)
        Element artistSonglist = getSource().getFirstElement("id", "artist-songlist", false);
        
        if (artistSonglist != null) {
        List<Element> liTracks = artistSonglist.getAllElements("li");
            for (Element li : liTracks) {
                if (li != null) {
                    Element link = li.getFirstElement("a");
                    if (link != null) {
                        String url = link.getAttributeValue("href");
                        if (StringUtils.isNotBlank(url)) {
                            String name = link.getFirstElement("a").getTextExtractor().toString();
                            Track track = new Track();
                            track.setName(name);
                            if (url.contains("(cifrada)")) {
                                track.setCypher(url);
                            } else if (url.contains("traducao")) {
                                track.setTraduction(url);
                            } else {
                                track.setLyric(url);
                            }
                            trackList.addTrack(track);
                        }
                    }
                }
            }
        }
    }

}
