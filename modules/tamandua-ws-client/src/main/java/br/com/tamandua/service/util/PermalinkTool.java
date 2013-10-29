/**
 * 
 */
package br.com.tamandua.service.util;


/**
 * @author Everton Rosario (erosario@gmail.com)
 *
 */
public class PermalinkTool {
    public static final String URI_ARTISTA = "artista";
    public static final String URI_MUSICA = "musica";
    public static final String URI_LETRA = "letra";
    public static final String URI_SUFIX = ".html";
    
    public PermalinkTool() {
    }
    
    public String linkToArtist(String artistName) {
        return StringNormalizer.normalizeURLPaths(URI_ARTISTA, artistName) + URI_SUFIX;
    }
    public String linkToMusic(String artistName, String musicName) {
        return StringNormalizer.normalizeURLPaths(URI_MUSICA, artistName, musicName) + URI_SUFIX;
    }
    public String linkToArtistUri(String artistUri) {
        return "/" + URI_ARTISTA + threatUri(artistUri) + URI_SUFIX;
    }
    public String linkToMusicUri(String artistMusicUri) {
        return "/" + URI_MUSICA + threatUri(artistMusicUri) + URI_SUFIX;
    }
    private String threatUri(String uri) {
        if (uri == null) {
            return "";
        }
        
        if (!uri.startsWith("/")) {
            uri += "/" + uri.trim();
        }
        if (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() -1);
        }
        return uri;
    }

    public String linkToLetter(String letter) {
        return StringNormalizer.normalizeURLPaths(URI_LETRA, letter) + URI_SUFIX;
    }
    
    public String linkToLetter(char letter) {
        return linkToLetter(""+letter);
    }

    public static void main(String[] args) {
        PermalinkTool tool = new PermalinkTool();
        System.out.println(tool.linkToArtist("Ke$ha"));
        System.out.println(tool.linkToMusic("Ke$ha", "Tik/tak"));
        System.out.println(tool.linkToLetter("ç"));
    }
}
