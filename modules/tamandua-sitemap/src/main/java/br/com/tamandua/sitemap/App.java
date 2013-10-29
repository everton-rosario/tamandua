package br.com.tamandua.sitemap;

import java.io.IOException;

import br.com.tamandua.sitemap.generator.SitemapGenerator;

public class App {
	
    public static void main( String[] args ) throws IOException {
        //------------------- sitemap -------------------------
        SitemapGenerator.generate();
    }
}
