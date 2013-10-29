/**
 * 
 */
package br.com.tamandua.service.proxy;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.httpclient.HttpClient;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClientExecutor;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

/**
 * @author Everton Rosário
 *
 */
public class ProxyHelper {
    public final static String APPLICATION_JSON_WITH_CHARSET_UTF8 = "application/json; charset=UTF-8";
    public final static String APPLICATION_XML_WITH_CHARSET_UTF8 = "application/xml; charset=UTF-8";
    public final static String MULTIPART_FORM_DATA_WITH_CHARSET_UTF8 = "multipart/form-data; charset=UTF-8";
    public final static String TEXT_PLAIN_WITH_CHARSET_UTF8 = "text/plain; charset=UTF-8";
    public final static String APPLICATION_JSON_WITH_CHARSET_ISO88591 = "application/json; charset=ISO-8859-1";
    public final static String APPLICATION_XML_WITH_CHARSET_ISO88591 = "application/xml; charset=ISO-8859-1";
    public final static String MULTIPART_FORM_DATA_WITH_CHARSET_ISO88591 = "multipart/form-data; charset=ISO-8859-1";
    public final static String TEXT_PLAIN_WITH_CHARSET_ISO88591 = "text/plain; charset=ISO-8859-1";

    public static MusicProxy getMusicProxy(String webServiceHost) {
        MusicProxy musicProxy = (MusicProxy) ProxyFactory.create(MusicProxy.class, createUri(webServiceHost), createClientExecutor(), ResteasyProviderFactory.getInstance());
        return musicProxy;
    }

    public static ArtistProxy getArtistProxy(String webServiceHost) {
        ArtistProxy artistProxy = (ArtistProxy) ProxyFactory.create(ArtistProxy.class, createUri(webServiceHost), createClientExecutor(), ResteasyProviderFactory.getInstance());
        return artistProxy;
    }

    public static ImageProxy getImageProxy(String webServiceHost) {
        ImageProxy imageProxy = (ImageProxy) ProxyFactory.create(ImageProxy.class, createUri(webServiceHost), createClientExecutor(), ResteasyProviderFactory.getInstance());
        return imageProxy;
    }
    public static UserProxy getUserProxy(String webServiceHost){
    	UserProxy userProxy = (UserProxy) ProxyFactory.create(UserProxy.class, createUri(webServiceHost), createClientExecutor(), ResteasyProviderFactory.getInstance());
    	return userProxy;
    }
    public static ModerationProxy getModerationProxy(String webServiceHost){
    	ModerationProxy moderationProxy = (ModerationProxy) ProxyFactory.create(ModerationProxy.class, createUri(webServiceHost), createClientExecutor(), ResteasyProviderFactory.getInstance());
    	return moderationProxy;
    }

    public static SearchProxy getSearchProxy(String webServiceHost){
        SearchProxy searchProxy = (SearchProxy) ProxyFactory.create(SearchProxy.class, createUri(webServiceHost), createClientExecutor(), ResteasyProviderFactory.getInstance());
        return searchProxy;
    }
    
    private static URI createUri(String base) {
        try {
            return new URI(base);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static ApacheHttpClientExecutor createClientExecutor() {
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setParameter("http.socket.timeout", new Integer(60000));
        httpClient.getParams().setConnectionManagerTimeout(60000);
        httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        httpClient.getParams().setParameter("http.protocol.uri-charset", "UTF-8");
        httpClient.getParams().setParameter("http.protocol.element-charset", "UTF-8");
        httpClient.getParams().setParameter("http.protocol.credential-charset", "UTF-8");
        httpClient.getParams().setContentCharset("UTF-8");

        return new ApacheHttpClientExecutor(httpClient);
    }
}
