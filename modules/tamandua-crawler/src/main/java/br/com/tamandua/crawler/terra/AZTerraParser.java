package br.com.tamandua.crawler.terra;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.regex.Pattern;

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
public class AZTerraParser extends AbstractParser {

    public AZTerraParser(InputStream is) throws IOException {
        super(is);
    }

    public AZTerraParser(Reader r) throws IOException {
        super(r);
    }

    public AZTerraParser(Source source) {
        super(source);
    }

    public AZTerraParser(String htmlBody) throws IOException {
        super(htmlBody);
    }

    public void fillArtistIndex(ArtistList letterArtists) {
        List<Element> allColumns = getSource().getAllElements("id", Pattern.compile("listagem(.*)"));
        for (Element column : allColumns) {
            
            List<Element> links = column.getAllElements("a");
            for (Element link : links) {
                if (link != null) {
                    String url = link.getAttributeValue("href");
                    String name = link.getFirstElement("a").getTextExtractor().toString();
                    Artist artist = new Artist(name, letterArtists.getLetter(), url);
                    letterArtists.addArtist(artist);
                }
            }
        }
    }

    public void fillTrackIndex(TrackList trackList) {
        Element musicografia = getSource().getFirstElement("id", "musicografia", false);
        if (musicografia != null) {
            List<Element> colunas = musicografia.getAllElements("ol");
            if (colunas != null) {
                for (Element coluna : colunas) {
                    List<Element> links = coluna.getAllElements("a");
                    if (links != null) {
                        for (Element link : links) {
                            if (link != null) {
                                String url = link.getAttributeValue("href");
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

}
