package br.com.tamandua.crawler.domain.xml.vagalume;
import java.io.File;
import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.MasonTagTypes;
import net.htmlparser.jericho.MicrosoftTagTypes;
import net.htmlparser.jericho.PHPTagTypes;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import br.com.tamandua.crawler.domain.Track;
import br.com.tamandua.crawler.domain.TrackList;
import br.com.tamandua.crawler.domain.xml.TrackPageParser;
import br.com.tamandua.crawler.vagalume.VagalumeTrackPageParser;

public class TestJericho {
    
    public static void main(String[] args) throws Exception {

        // FAZ A CARGA DO ARQUIVO PARA TESTES
        TrackList trackList = new TrackList();
        JAXBContext jc = JAXBContext.newInstance(TrackList.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        trackList = (TrackList) unmarshaller.unmarshal(new File("c:/crawled/index/vagalume/d/djavan.xml"));
        System.out.println("Total de faixas de DJAVAN: " + trackList.getTotalTracks());
        Track track = trackList.getTracks().get(0);
//        trackList = (TrackList) unmarshaller.unmarshal(new File("c:/crawled/index/l/lady-gaga.xml"));
//        System.out.println("Total de faixas de LADY-GAGA: " + trackList.getTotalTracks());
//        Track track = trackList.getTracks().get(4);
        System.out.println("Track sendo parseada: " + track.getName());
        TrackPageParser parser = new VagalumeTrackPageParser(track);
        
        MicrosoftTagTypes.register();
        PHPTagTypes.register();
        PHPTagTypes.PHP_SHORT.deregister(); // remove PHP short tags for this example otherwise they override processing instructions
        MasonTagTypes.register();


        Source source = new Source(new StringReader(parser.getHtmlBody()));
        System.out.println("############################################################");
        System.out.println("Letra da musica: ");
        System.out.println(source.getFirstElement("class","tab_original",false).getContent());
        System.out.println("############################################################");
        System.out.println("Estilos da musica: ");
        List<StartTag> styles = source.getFirstElement("class","songstyle",false).getAllStartTags("a");
        int stylecount = 1;
        for (StartTag style : styles) {
            if (style.getAttributeValue("class") == null) {
                System.out.println("Estilo encontrado [" + (stylecount++) + "] " + style.getElement().getContent());
            }
        }
        System.out.println("############################################################");
        System.out.println("Autores: ");
        List<StartTag> autores = source.getFirstElement("class","author",false).getAllStartTags("strong");
        int autorcount = 1;
        for (StartTag autor : autores) {
            System.out.println("Autor encontrado [" + (autorcount++) + "] " + autor.getElement().getContent());
        }

        System.out.println("############################################################");
        System.out.println("Album: ");
        Element albumElement = source.getFirstElement("class","lyricAlbuns iP",false);
        albumElement.getAttributeValue("href");
        List<StartTag> albuns = albumElement.getAllStartTags("strong");
        int albumcount = 1;
        for (StartTag album : albuns) {
            System.out.println("Album encontrado [" + (albumcount++) + "] " + album.getElement().getContent());
        }
        
        System.out.println("############################################################");
        System.out.println("Album Cover: ");
        Element coverElement = source.getFirstElement("class","albumImg",false);
        String albumCover100x100 = coverElement.getAttributeValue("src");
        String albumCover200x200 = albumCover100x100.replaceAll("-W100\\.jpg", "-W200.jpg");
        String albumCoverOriginal = albumCover100x100.replaceAll("-W100\\.jpg", ".jpg");
        System.out.println("Capa do album encontrada [100x100]: " + albumCover100x100);
        System.out.println("Capa do album encontrada [200x200]: " + albumCover200x200);
        System.out.println("Capa do album encontrada [Original]: " + albumCoverOriginal);
        
        Element artistImageElementDiv = source.getFirstElement("class","artPicsGallery",false);
        if (artistImageElementDiv != null) {
            System.out.println("############################################################");
            System.out.println("Artist Images: ");
            List<Element> artistImages = artistImageElementDiv.getAllElements("class", "divulgacao", false);
            for (Element artistImage : artistImages) {
                String imageUri = artistImage.getAttributeValue("title");
                System.out.println("Imagem do artista encontrada: " + imageUri);
            }
        }
        
    }
}
