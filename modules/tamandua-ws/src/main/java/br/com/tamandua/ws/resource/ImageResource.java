package br.com.tamandua.ws.resource;

import static br.com.tamandua.service.proxy.ProxyHelper.APPLICATION_JSON_WITH_CHARSET_UTF8;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.tamandua.persistence.app.AppContext;
import br.com.tamandua.service.vo.ImageVO;
import br.com.tamandua.service.vo.validator.MessagesVO;
import br.com.tamandua.ws.service.ImageService;

/**
 * @author Everton Rosario (erosario@gmail.com)
 */
@Path("/images")
public class ImageResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageResource.class);

    @POST
    @Path("/save")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO saveImage(ImageVO imageVO) {
        
        LOGGER.debug("/images/save");
        
        ImageService service = (ImageService) AppContext.getApplicationContext().getBean("imageService");
        return service.saveImage(imageVO);
    }
   
    @GET
    @Path("/find/uri")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public ImageVO findImageByURI(@QueryParam("uri") String uri) {
        
        LOGGER.debug("/images/find/uri");

        ImageService service = (ImageService) AppContext.getApplicationContext().getBean("imageService");
        return service.findByUri(uri);
    }

    @GET
    @Path("/find/provider")
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Wrapped(element = "images")
    public List<ImageVO> findImageByProvider(@QueryParam("provider") String provider) {
        
        LOGGER.debug("/images/find/provider");

        ImageService service = (ImageService) AppContext.getApplicationContext().getBean("imageService");
        return service.findByProvider(provider);
    }

    @GET
    @Path("/example")
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public ImageVO getImageExample() {
        
        LOGGER.debug("/images/example");

        ImageService service = (ImageService) AppContext.getApplicationContext().getBean("imageService");
        return service.getImageExample();
    }
}
