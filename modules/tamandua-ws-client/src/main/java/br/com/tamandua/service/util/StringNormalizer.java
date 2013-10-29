package br.com.tamandua.service.util;

import java.text.Normalizer;
import java.text.Normalizer.Form;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Classe StringNormalizer
 * 
 * @author Everton Fernandes Rosario (sma_erosario@uolinc.com)
 */
public class StringNormalizer {

    public static String normalizeURL(String s) {
        if (s == null) 
            return null;
        s = StringEscapeUtils.unescapeHtml(s);
        // Realiza trim para remover os espacos no comeco e fim
        s = s.trim();
        // Realiza a substituicao de "espacos" por tracos
        s = s.replaceAll("\\s","-");
        // Troca & por e
        s = s.replaceAll("&","e");
        // Quebra os caracteres acentuados e extendidos.
        s = Normalizer.normalize(s, Form.NFD);
        // Remove todos os caracteres, exceto:
        // A-Z maiusculos e a-z minusculos
        // Numericos de 0-9
        // o caractere "-" (traco)
        // o caractere "/" (barra)
        // o caractere "(" (abre parentesis)
        // o caractere ")" (fecha parentesis)
        // o Caractere ";" (ponto-e-virgula)
        // o caractere "$" (cifrao)
        // o Caractere "~" (til)
        // o Caractere "*" (asterisco)
        // o Caractere "_" (underscore)
        // o caractere "@" (arroba)
        // o caractere "|" (pipe)
        s = s.replaceAll("[^a-z0-9A-Z\\-/\\(\\);\\$~\\*\\_@]","");
        s = s.toLowerCase();
        return s;
    }

    public static String normalizeURLPaths(String ...paths) {
        StringBuffer sbuff = new StringBuffer();
        for (String path : paths) {
            sbuff.append("/").append(normalizeURL(path != null ? path.replace('/', '-') : ""));
        }
        return sbuff.toString();
    }
    
    /**
     * @param Nome do arquivo a ser armazenado
     * @return O nome sem os caracteres que nao sao permitidos para utilizacao no FileSystem.
     */
    public static String normalizeFileName(String s) {
        s = StringEscapeUtils.unescapeHtml(s);
        // Realiza trim para remover os espacos no comeco e fim
        s = s.trim();
        // Realiza a substituicao de "espacos" por tracos
        s = s.replaceAll("\\s","-");
        // Troca & por e
        s = s.replaceAll("&","e");
        // Quebra os caracteres acentuados e extendidos.
        s = Normalizer.normalize(s, Form.NFD);
        // Remove todos os caracteres, exceto:
        // A-Z maiusculos e a-z minusculos
        // Numericos de 0-9
        // o caractere "-" (traco)
        // o caractere "\" (barra invertida)
        // o caractere "/" (barra)
        // o caractere "(" (abre parentesis)
        // o caractere ")" (fecha parentesis)
        // o Caractere ";" (ponto-e-virgula)
        // o caractere "$" (cifrao)
        // o Caractere "~" (til)
        // o Caractere "*" (asterisco)
        // o Caractere "_" (underscore)
        // o caractere "@" (arroba)
        // o caractere "|" (pipe)
        s = s.replaceAll("[^a-z0-9A-Z\\.\\-/\\(\\);\\$~\\_@]","");
        s = s.toLowerCase();
        return s;
    }

    /**
     * @param Nome do artista a ser normalizado
     * @return O nome sem os caracteres que nao sao permitidos para utilizacao no FileSystem.
     */
    public static String normalizeArtistName(String s) {
        s = StringEscapeUtils.unescapeHtml(s);
        // Realiza trim para remover os espacos no comeco e fim
        s = s.trim();
        // Realiza a substituicao de "espacos" por tracos
        s = s.replaceAll("\\s","-");
        // Troca & por e
        s = s.replaceAll("&","e");
        // Quebra os caracteres acentuados e extendidos.
        s = Normalizer.normalize(s, Form.NFD);
        // Remove todos os caracteres, exceto:
        // A-Z maiusculos e a-z minusculos
        // Numericos de 0-9
        // o caractere "-" (traco)
        // o Caractere "_" (underscore)
        s = s.replaceAll("[^a-z0-9A-Z\\-\\_]","");
        s = s.toLowerCase();
        return s;
    }

    /**
     * @param s String a ser normalizada
     * @return Uma string sem acentos e semcaracteres que nao sejam letras e numeros
     */
    public static String normalizeText(String s) {
        s = StringEscapeUtils.unescapeHtml(s);
        // Realiza trim para remover os espacos no comeco e fim
        s = s.trim();
        // Troca & por e
        s = s.replaceAll("&","e");
        s = s.replaceAll("\\<.*?\\>", "");
        // Quebra os caracteres acentuados e extendidos.
        s = Normalizer.normalize(s, Form.NFD);
        // Remove todas html entities
        // Remove todos os caracteres, exceto:
        // A-Z maiusculos e a-z minusculos
        // Numericos de 0-9
        // o caractere "-" (traco)
        // o Caractere "_" (underscore)
        // o caractere "\" (barra invertida)
        // o caractere "/" (barra)
        // o caractere "." (ponto)
        s = s.replaceAll("[^a-z0-9A-Z\\-\\_\\s/\\.\\\\]","");
        return s;
    }
}
