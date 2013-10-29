/**
 * 
 */
package br.com.tamandua.service.proxy;

import static br.com.tamandua.service.proxy.ProxyHelper.APPLICATION_JSON_WITH_CHARSET_UTF8;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;

import br.com.tamandua.service.vo.ImageVO;
import br.com.tamandua.service.vo.validator.MessagesVO;

/**
 * @author BILL
 *
 */
@Path("/images")
public interface ImageProxy {

    @POST
    @Path("/save")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO saveImage(ImageVO imageVO);

    @GET
    @Path("/find/uri")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public ImageVO findImageByURI(@QueryParam("uri") String uri);

    @GET
    @Path("/find/provider")
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Wrapped(element="images")
    public List<ImageVO> findImageByProvider(@QueryParam("provider") String provider, @QueryParam("letter") String letter);

    @GET
    @Path("/example")
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public ImageVO getImageExample();

}

