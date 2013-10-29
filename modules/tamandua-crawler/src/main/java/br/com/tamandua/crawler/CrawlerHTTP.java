package br.com.tamandua.crawler;

import java.io.IOException;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;

import br.com.tamandua.service.util.StringNormalizer;

/**
 * @author Everton Rosario (erosario@gmail.com)
 *
 */
public class CrawlerHTTP {
    private String provider;
    private HttpClient httpclient;
    private HostConfiguration hostconfig;
    
    public CrawlerHTTP(String provider) {
        this.provider = provider;

        // Cria o httpclient
        httpclient = new HttpClient();
        httpclient.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
        httpclient.getParams().setParameter("http.socket.timeout", new Integer(60000));
        httpclient.getParams().setConnectionManagerTimeout(60000);
        //httpclient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        httpclient.getParams().setParameter("http.useragent", "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
        httpclient.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.IGNORE_COOKIES);

        // Configura o host utilizado
        hostconfig = new HostConfiguration();
        hostconfig.setHost(CrawlerProperties.getInstance().getCrawlerProviderHost(provider));
        hostconfig.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
    }

    public String getResponseBody(String uri) throws IOException {
        String responseBody = null;
        if (StringUtils.isNotEmpty(uri)) {

            GetMethod httpget = null;
            try {
                httpget = new GetMethod(uri);
            } catch (Exception ex) {
                uri = StringNormalizer.normalizeURL(uri);
                httpget = new GetMethod(uri);
            }
            try {
                int responseCode = httpclient.executeMethod(hostconfig, httpget);
                if (responseCode != 200) {
                    responseBody = "";
                } else {
                    responseBody = httpget.getResponseBodyAsString();
                }
            } finally {
                httpget.releaseConnection();
            }
        }
        return responseBody;
    }

    /**
     * @return the provider
     */
    public String getProvider() {
        return provider;
    }

    /**
     * @param provider the provider to set
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }
}
