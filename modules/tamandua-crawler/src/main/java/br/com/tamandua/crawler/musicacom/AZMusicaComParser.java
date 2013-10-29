package br.com.tamandua.crawler.musicacom;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

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
public class AZMusicaComParser extends AbstractParser {

    public AZMusicaComParser(InputStream is) throws IOException {
        super(is);
    }

    public AZMusicaComParser(Reader r) throws IOException {
        super(r);
    }

    public AZMusicaComParser(Source source) {
        super(source);
    }

    public AZMusicaComParser(String htmlBody) throws IOException {
        super(htmlBody);
    }

    public void fillArtistIndex(ArtistList letterArtists) {
        Element font = getSource().getFirstElement("cellspacing", "5", false);
        List<Element> links = font.getAllElements("a");
        for (Element link : links) {
            String nameStr = link.getTextExtractor().toString();
            if (link != null) {
                if (!"[Ver los más visitados]".equals(nameStr)) {
                    String url = "/" + link.getAttributeValue("href") + "/";
                    String name = nameStr.substring(nameStr.indexOf(":: ") + 3);
                    Artist artist = new Artist(name, letterArtists.getLetter(), url);
                    letterArtists.addArtist(artist);
                }
            }
        }
    }

    public void fillTrackIndex(TrackList trackList) {
        Element table = getSource().getFirstElement("cellspacing", "5", false);
        if (table != null) {
            List<Element> links = table.getAllElements("a");
            if (links != null) {
                for (Element link : links) {
                    if (link != null) {
                        String nameStr = link.getTextExtractor().toString();
                        if (!"[Ordenar letras por visitas]".equals(nameStr)) {
                            String url = "/" + link.getAttributeValue("href");
                            if (url.contains("letra=")) {
                                String name = nameStr.substring(nameStr.indexOf("-")+1).trim();
                                Track track = new Track();
                                track.setName(name);
                                if (url.contains("(cifrada)")) {
                                    track.setCypher(url);
                                } else if (url.contains("(en portugues)") || url.contains("(en español)")) {
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

}
