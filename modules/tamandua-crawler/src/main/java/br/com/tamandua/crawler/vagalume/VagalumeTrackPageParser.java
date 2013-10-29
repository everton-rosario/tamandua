package br.com.tamandua.crawler.vagalume;

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
public class VagalumeTrackPageParser extends TrackPageParser {

    public VagalumeTrackPageParser(Artist artist, Track track) throws IOException {
        super(artist, track);
    }
    
    public VagalumeTrackPageParser(Track track) throws IOException {
        super(track);
    }

    /**
     * @return Obtem a letra/cifra/traducao da musica de dentro do html crawleado
     */
    @Override
    public String getMusicContent() {
        String divClass = "tab_original";
        if (isTraduction()) {
        	divClass = "tab_tra_pt";
        }
        if (isCypher()) {
        	divClass = "cifratxt";
        }
		Element lyricElement = source.getFirstElement("class",divClass,false);
		if (lyricElement == null && "tab_tra_pt".equals(divClass)) {
		    divClass = "tab_original";
		    lyricElement = source.getFirstElement("class",divClass,false);
		}
        return lyricElement != null ? lyricElement.getContent().toString() : "";
    }

    /**
     * @return Lista dos estilos existentes na musica crawleada
     */
    @Override
    public List<String> getStyles() {
        List<String> extractedStyles = new ArrayList<String>();
        Element stylesElement = source.getFirstElement("class","songstyle",false);
        List<StartTag> styles = stylesElement != null ? stylesElement.getAllStartTags("a") : Collections.EMPTY_LIST;
        if (styles == null) {
            styles = Collections.EMPTY_LIST;
        }
            
        for (StartTag style : styles) {
            if (style.getAttributeValue("class") == null) {
                extractedStyles.add(style.getElement().getContent().toString());
            }
        }
        return extractedStyles;
    }
    
    /**
     * @return O nome do album exibido na pagina crawleada
     */
    @Override
    public String getAlbumName() {
        String albumName = "";
        Element albunsElement = source.getFirstElement("class","lyricAlbuns iP",false);
        StartTag album = albunsElement != null ? albunsElement.getFirstStartTag("strong") : null;
        albumName = album != null ? album.getElement().getContent().toString() : "";
        return albumName;
    }
    
    /**
     * @return O nome do album exibido na pagina crawleada
     */
    @Override
    public String getAlbumURI() {
        String albumURI = "";
        Element albunsElement = source.getFirstElement("class","lyricAlbuns iP",false);
        albumURI = albunsElement != null ? albunsElement.getAttributeValue("href").toString() : null;
        return albumURI;
    }
    
    /**
     * @return As imagens do album, 100x100, 200x200 e a original. Os valores podem ser vazios ""
     */
    @Override
    public String[] getAlbumCover() {
        Element coverElement = source.getFirstElement("class","albumImg",false);
        String albumCover100x100 = "";
        String albumCover200x200 = "";
        String albumCoverOriginal = "";

        if (coverElement != null) {
            albumCover100x100 = coverElement.getAttributeValue("src") == null ? "" : coverElement.getAttributeValue("src");
            albumCover200x200 = albumCover100x100.replaceAll("-W100\\.jpg", "-W200.jpg");
            albumCoverOriginal = albumCover100x100.replaceAll("-W100\\.jpg", ".jpg");
        }

        return new String[]{albumCover100x100, albumCover200x200, albumCoverOriginal};
    }
    
    /**
     * @return Obtem os compositores da letra crawleada.
     */
    @Override
    public List<String> getComposers() {
        List<String> extractedAuthors = new ArrayList<String>();

        Element authorElement = source.getFirstElement("class","author",false);
        List<StartTag> authors = authorElement != null ? authorElement.getAllStartTags("strong") : Collections.EMPTY_LIST;
        for (StartTag author : authors) {
            extractedAuthors.add(author.getElement().getContent().toString());
        }

        return extractedAuthors;
    }
    
    /**
     * @return Obtem a lista de url/uri de imagens do artista da musica crawleada
     */
    @Override
    public List<String> getArtistImages() {
        List<String> extractedImages = new ArrayList<String>();
        
        Element artistImageElementDiv = source.getFirstElement("class","pics",false);
        if (artistImageElementDiv != null) {
            List<Element> artistImages = artistImageElementDiv.getAllElements("img");
            for (Element artistImage : artistImages) {
                String imageUri = artistImage.getAttributeValue("src");
                extractedImages.add(imageUri);
                String image2Uri = imageUri.replace("/images/gt", "/images/g");
                if (!imageUri.equals(image2Uri)) {
                    extractedImages.add(image2Uri);
                }
            }
        }

        // Imagens adicionais da avril
        artistImageElementDiv = source.getFirstElement("class","artPicsGallery",false);
        if (artistImageElementDiv != null) {
            List<Element> artistImages = artistImageElementDiv.getAllElements("abbr");
            for (Element artistImage : artistImages) {
                String imageUri = artistImage.getAttributeValue("title");
                extractedImages.add(imageUri);
                String image2Uri = imageUri.replace("/images/gt", "/images/g");
                if (!imageUri.equals(image2Uri)) {
                    extractedImages.add(image2Uri);
                }
            }
        }
        
        return extractedImages;
    }

    @Override
    public List<String> getFreeStyles() {
        return null;
    }

    @Override
    public String getImageHost() {
        return "http://static4.vagalume.uol.com.br";
    }

    @Override
    public String getNoImageToIgnore() {
        return "no_foto_artista_send.gif";
    }
}
