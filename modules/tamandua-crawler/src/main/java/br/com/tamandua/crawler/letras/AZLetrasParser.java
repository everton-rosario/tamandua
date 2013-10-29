package br.com.tamandua.crawler.letras;

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
public class AZLetrasParser extends AbstractParser {

    public AZLetrasParser(InputStream is) throws IOException {
        super(is);
    }

    public AZLetrasParser(Reader r) throws IOException {
        super(r);
    }

    public AZLetrasParser(Source source) {
        super(source);
    }

    public AZLetrasParser(String htmlBody) throws IOException {
        super(htmlBody);
    }

    public void fillArtistIndex(ArtistList letterArtists) {
        Element bloco = getSource().getFirstElement("class", "bloco", false);
        List<Element> colunas = bloco.getAllElements("ul");
        for (Element coluna : colunas) {
            List<Element> links = coluna.getAllElements("a");
            for (Element link : links) {
                if (link != null) {
                    String url = "/" + link.getAttributeValue("href") + "/";
                    String name = link.getFirstElement("a").getTextExtractor().toString();
                    Artist artist = new Artist(name, letterArtists.getLetter(), url);
                    letterArtists.addArtist(artist);
                }
            }
        }
    }

    public void fillTrackIndex(TrackList trackList) {
        Element bloco = getSource().getFirstElement("class", "bloco", false);
        if (bloco != null) {
            Element coluna = bloco.getFirstElement("ol");
            if (coluna != null) {
                List<Element> links = coluna.getAllElements("a");
                for (Element link : links) {
                    if (link != null) {
                        String url = "/" + link.getAttributeValue("href") + "/";
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
