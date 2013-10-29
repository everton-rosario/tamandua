package br.com.tamandua.crawler.terra;

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
public class TerraTrackPageParser extends TrackPageParser {

    public TerraTrackPageParser(Artist artist, Track track) throws IOException {
        super(artist, track);
    }
    
    public TerraTrackPageParser(Track track) throws IOException {
        super(track);
    }

    /**
     * @return Obtem a letra/cifra/traducao da musica de dentro do html crawleado
     */
    @Override
    public String getMusicContent() {
        String divId = "div_letra";
        if (isTraduction()) {
        	divId = "box_traducao";
        }
		Element lyricElement = source.getFirstElement("id",divId,false);
		if (!isTraduction() && lyricElement != null) {
		    return lyricElement.getContent().toString();
		}
		
		StringBuffer lyricContent = new StringBuffer();
		if (lyricElement != null) {
		    List<Element> linhas = lyricElement.getAllElements("tr");
		    int index = 0;
		    for (Element linha : linhas) {
                if (index >= 4) {
                    List<Element> colunas = linha.getAllElements("td");
                    lyricContent.append(colunas.get(1).getContent().toString().replaceAll("<em></em>", "\n"));
                }
                index++;
            }
		}
        return lyricContent.toString();
    }

    /**
     * @return Lista dos estilos existentes na musica crawleada
     */
    @Override
    public List<String> getStyles() {
        return Collections.EMPTY_LIST;
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
        return new String[]{ };
    }
    
    /**
     * @return Obtem os compositores da letra crawleada.
     */
    @Override
    public List<String> getComposers() {
        List<String> extractedAuthors = new ArrayList<String>();

        List<Element> authorElements = source.getAllElements("small");
        for (Element authorElement : authorElements) {
            String authorText = authorElement.getContent().toString();
            if (authorText.contains("Composição:")) {
                String composers = authorElement != null ? authorText : "";
                String[] splitedComposers = composers.split(" / ");
                for (String composer : splitedComposers) {
                    String strComposer = composer.trim();
                    if (strComposer.endsWith(".")) {
                        strComposer = strComposer.substring(0, strComposer.length()-1);
                    }
                    extractedAuthors.add(strComposer.replace("Composição: ", ""));
                }
                
            }
            
        }

        return extractedAuthors;
    }
    
    /**
     * @return Obtem a lista de url/uri de imagens do artista da musica crawleada
     */
    @Override
    public List<String> getArtistImages() {
        List<String> images = new ArrayList<String>();
        
        Element avatar = source.getFirstElement("class", "avatar", false);
        if (avatar != null) {
            try {
                String styleContent = avatar.getAttributeValue("style").toString();
                int open = styleContent.indexOf('(');
                int close = styleContent.indexOf(')', open+1);
                String imageURL = styleContent.substring(open+1, close);
                images.add(imageURL);
                if (imageURL.endsWith("-tb.jpg")) {
                    images.add(imageURL.replace("-tb.jpg", ".jpg"));
                }
                return images;
            } catch (Exception ex) {
                // Do nothing
            }
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<String> getFreeStyles() {
        List<String> extractedStyles = new ArrayList<String>();
        Element stylesElement = source.getFirstElement("class","tags",false);
        List<Element> styles = stylesElement != null ? stylesElement.getAllElements("class", "pp", false) : Collections.EMPTY_LIST;
        if (styles == null) {
            styles = Collections.EMPTY_LIST;
        }
            
        for (Element style : styles) {
            if (style.getAttributeValue("class") == null) {
                extractedStyles.add(style.getContent().toString());
            }
        }
        return extractedStyles;
    }

    public String getImageHost() {
        return "http://letras.terra.com.br";
    }

    @Override
    public String getNoImageToIgnore() {
        return "noavatar-artista_78x78.gif";
    }
}
