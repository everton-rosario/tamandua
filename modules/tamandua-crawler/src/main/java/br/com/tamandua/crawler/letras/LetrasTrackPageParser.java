package br.com.tamandua.crawler.letras;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.StartTag;
import br.com.tamandua.crawler.domain.Artist;
import br.com.tamandua.crawler.domain.Track;
import br.com.tamandua.crawler.domain.xml.TrackPageParser;

/**
 * @author Everton Rosario (erosario@gmail.com)
 */
public class LetrasTrackPageParser extends TrackPageParser {

    public LetrasTrackPageParser(Artist artist, Track track) throws IOException {
        super(artist, track);
    }
    
    public LetrasTrackPageParser(Track track) throws IOException {
        super(track);
    }

    /**
     * @return Obtem a letra/cifra/traducao da musica de dentro do html crawleado
     */
    @Override
    public String getMusicContent() {
        String divId = "lletra";
        if (isTraduction()) {
        	divId = "divletra";
        }
		Element lyricElement = source.getFirstElement("id",divId,false);
        if (!isTraduction() && lyricElement != null) {
            return lyricElement.getContent().toString();
        }

        if (lyricElement == null) {
		    divId = "lletra";
		    lyricElement = source.getFirstElement("class",divId,false);
		}
		
	    StringBuffer lyricContent = new StringBuffer();
        if (lyricElement != null) {
            List<Element> linhas = lyricElement.getAllElements("class","trad1",false);
            for (Element linha : linhas) {
                lyricContent.append(linha.getContent().toString() + "\n");
            }
        }
        return lyricContent.toString();
    }

    /**
     * @return Lista dos estilos existentes na musica crawleada
     */
    @Override
    public List<String> getStyles() {
        return null;
    }
    
    /**
     * @return O nome do album exibido na pagina crawleada
     */
    @Override
    public String getAlbumName() {
        return null;
    }
    
    /**
     * @return O nome do album exibido na pagina crawleada
     */
    @Override
    public String getAlbumURI() {
        return null;
    }
    
    /**
     * @return As imagens do album, 100x100, 200x200 e a original. Os valores podem ser vazios ""
     */
    @Override
    public String[] getAlbumCover() {
        return null;
    }
    
    /**
     * @return Obtem os compositores da letra crawleada.
     */
    @Override
    public List<String> getComposers() {
        List<String> extractedAuthors = new ArrayList<String>();

        Element authorElement = source.getFirstElement("id","autor",false);
        List<Element> authors = authorElement != null ? authorElement.getAllElements("a") : Collections.EMPTY_LIST;
        for (Element author : authors) {
            String nomeAutor = author.getContent().toString();
            if (!nomeAutor.contains("informe o(s) compositor(es)")) {
                extractedAuthors.add(nomeAutor);
            }
        }

        return extractedAuthors;
    }
    
    /**
     * @return Obtem a lista de url/uri de imagens do artista da musica crawleada
     */
    @Override
    public List<String> getArtistImages() {
        return null;
    }

    @Override
    public List<String> getFreeStyles() {
        return null;
    }

    @Override
    public String getImageHost() {
        return null;
    }

    @Override
    public String getNoImageToIgnore() {
        return null;
    }

}
