package br.com.tamandua.sitemap.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.tamandua.service.util.PermalinkTool;
import br.com.tamandua.sitemap.database.LoaderDB;
import br.com.tamandua.sitemap.utils.SitemapProperties;

/**
 *
 * @author Gabriel Palacio
 */
public class Sitemap {

	private static final Logger LOGGER = LoggerFactory.getLogger(Sitemap.class);
	
    //------------------ constants ---------------------------
    static final String SITEMAP_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><urlset xmlns=\"http://www.google.com/schemas/sitemap/0.84\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.google.com/schemas/sitemap/0.84 http://www.google.com/schemas/sitemap/0.84/sitemap.xsd\">\n";
    static final String SITEMAP_FOOTER = "</urlset>\n";
    static final String URL_TEMPLATE = "\t<url><loc>{0}</loc></url>\n";
    static final String SITEMAP_INDEX_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sitemapindex xmlns=\"http://www.google.com/schemas/sitemap/0.84\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.google.com/schemas/sitemap/0.84 http://www.google.com/schemas/sitemap/0.84/siteindex.xsd\">\n";
    static final String SITEMAP_INDEX_FOOTER = "</sitemapindex>\n";
    static final String SITEMAP_TEMPLATE = "\t<sitemap><loc>{0}</loc></sitemap>\n";    
    static final PermalinkTool permalinkTool = new PermalinkTool();

    //------------------- attributes ----------------------------
    private String targetPath;
    private LoaderDB loaderDB;
    private String urlBase;

    //------------------- constructor ---------------------------

    public Sitemap(String urlBase, String targetPath, LoaderDB loaderDB){
        this.targetPath = targetPath;
        this.loaderDB = loaderDB;
        this.urlBase = urlBase;
    }

    //-------------------- public methods -------------------------

    public void create(String prefix, Integer maxResults) throws IOException {
    	List<String> uris = getUris(prefix, 1, maxResults);
        Integer count = 0;
        LOGGER.info("Generating '"+prefix+"' sitemap file(s)...");
        while (!uris.isEmpty()) {
        	String fileName = targetPath.concat("/sitemap-").concat(prefix).concat("-").concat(count.toString().concat(".xml"));
        	LOGGER.info("Generating '"+fileName+"' file with '"+uris.size()+"' urls...");
        	File file = new File(fileName);
        	file.getParentFile().mkdirs();
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            OutputStreamWriter output = new OutputStreamWriter(fileOutputStream, "UTF-8");
            output.write(SITEMAP_HEADER);
            for (String uri : uris) {
                String url = null;
                if (prefix.equals(SitemapProperties.getInstance().getArtistPrefix())) {
                    url = urlBase + permalinkTool.linkToArtistUri(uri);
                } else if (prefix.equals(SitemapProperties.getInstance().getMusicPrefix())) {
                    url = urlBase + permalinkTool.linkToMusicUri(uri);
                }  else {
                    continue;
                }
                String result = MessageFormat.format(URL_TEMPLATE, url);
                output.write(result);
            }
            count++;
            uris = getUris(prefix, (maxResults * count), maxResults);
            output.write(SITEMAP_FOOTER);
            output.close();
        }
        LOGGER.info("Finished generating '"+prefix+"' sitemap file(s)!");
    }
    
    public List<String> getUris(String prefix, Integer start, Integer maxResults){
    	List<String> uris;
        if (prefix.equals(SitemapProperties.getInstance().getArtistPrefix())) {
        	uris = loaderDB.getArtistUris(start, maxResults);
        } else if (prefix.equals(SitemapProperties.getInstance().getMusicPrefix())) {
        	uris = loaderDB.getMusicUris(start, maxResults);
        }  else {
        	uris = new ArrayList<String>();
        }
        return uris;
    }
    
    public void createLetter(String prefix, List<String> letters) throws IOException {
    	LOGGER.info("Generating letter sitemap file...");
    	String fileName = targetPath.concat("/sitemap-").concat(prefix).concat(".xml");
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        OutputStreamWriter output = new OutputStreamWriter(fileOutputStream, "UTF-8");
        output.write(SITEMAP_HEADER);
        for (String letter : letters) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(urlBase);
            stringBuffer.append("/");
            stringBuffer.append(prefix);
            stringBuffer.append("/");
            stringBuffer.append(letter);
            stringBuffer.append(".html");
            String url = stringBuffer.toString();
            String result = MessageFormat.format(URL_TEMPLATE, url);
            output.write(result);
        }
        output.write(SITEMAP_FOOTER);
        output.close();
        LOGGER.info("Finished generating letter sitemap file!");
    }
    
    public void createIndex() throws IOException {
    	LOGGER.info("Generating index sitemap file...");
        File dir = new File(targetPath);
        FilenameFilter fileNameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return (name.startsWith("sitemap") && !name.equals("sitemap.xml"));
            }
        };
        String[] files = dir.list(fileNameFilter);
        FileOutputStream fileOutputStream = new FileOutputStream(targetPath.concat("/sitemap.xml"));
        OutputStreamWriter output = new OutputStreamWriter(fileOutputStream, "UTF-8");
        output.write(SITEMAP_INDEX_HEADER);

        for (String file : files)
        {
        	LOGGER.info("Generating file location '"+file+"'...");
            String result = MessageFormat.format(SITEMAP_TEMPLATE, urlBase+"/sitemap/"+file);
            output.write(result);
        }

        output.write(SITEMAP_INDEX_FOOTER);
        output.close();
        LOGGER.info("Finished generating index sitemap file!");
    }
}
