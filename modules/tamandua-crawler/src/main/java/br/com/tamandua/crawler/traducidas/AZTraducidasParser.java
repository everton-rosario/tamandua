package br.com.tamandua.crawler.traducidas;

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
public class AZTraducidasParser extends AbstractParser {

    public AZTraducidasParser(InputStream is) throws IOException {
        super(is);
    }

    public AZTraducidasParser(Reader r) throws IOException {
        super(r);
    }

    public AZTraducidasParser(Source source) {
        super(source);
    }

    public AZTraducidasParser(String htmlBody) throws IOException {
        super(htmlBody);
    }

    public void fillArtistIndex(ArtistList letterArtists) {
        Element bloco = getSource().getFirstElement("class", "lista", false);
        List<Element> links = bloco.getAllElements("a");
        for (Element link : links) {
            if (link != null) {
                String url = link.getAttributeValue("href");
                url = url.replace("http://www.traducidas.com.ar/letras", "") + "/";
                String name = link.getFirstElement("a").getTextExtractor().toString();
                if (StringUtils.isNotBlank(name)) {
                    Artist artist = new Artist(name, letterArtists.getLetter(), url);
                    if (!letterArtists.getArtists().contains(artist)) {
                        letterArtists.addArtist(artist);
                    }
                }
            }
        }
    }

    public void fillTrackIndex(TrackList trackList) {
        Element bloco = getSource().getFirstElement("class", "lista", false);
        List<Element> links = bloco.getAllElements("a");
        for (Element link : links) {
            if (link != null) {
                String url = link.getAttributeValue("href");
                String name = link.getFirstElement("a").getTextExtractor().toString();
                if (StringUtils.isNotBlank(name)) {
                    Track track = new Track();
                    track.setName(name);
                    track.setLyric(url);
                    if (!trackList.getTracks().contains(track)) {
                        trackList.addTrack(track);
                    }
                }
            }
        }
    }

}
