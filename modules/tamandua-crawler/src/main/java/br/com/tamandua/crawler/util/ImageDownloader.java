/**
 * 
 */
package br.com.tamandua.crawler.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

/**
 * @author sma_erosario
 * 
 */
public class ImageDownloader {
    public static void saveImageFromUrlToFile(String strURL, File file) throws IOException {
        // Get the image
        URL url = new URL(strURL);
        FileUtils.copyURLToFile(url, file);
    }
    
    public static void main(String[] args) throws IOException {
        ImageDownloader.saveImageFromUrlToFile("http://bolao.esporte.uol.com.br/web/app/imgs/skins/basic/topo.png", new File("c:\\export\\images\\image.png"));
    }

}
