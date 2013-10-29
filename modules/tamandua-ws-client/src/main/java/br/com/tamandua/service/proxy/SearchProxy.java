/**
 * 
 */
package br.com.tamandua.service.proxy;

import static br.com.tamandua.service.proxy.ProxyHelper.APPLICATION_JSON_WITH_CHARSET_UTF8;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.PageResultVO;

/**
 * @author Everton Rosario (erosario@gmail.com)
 */
@Path("/search")
public interface SearchProxy {

        @GET
        @Path("/open")
        @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
        @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
        public PageResultVO searchByOpenTerms(@QueryParam("q") String terms,
                                              @QueryParam("page") @DefaultValue("1") final Integer pageNumber,
                                              @QueryParam("max") @DefaultValue("1000") final Integer max,
                                              @QueryParam("hitsPerPage") @DefaultValue("50") final Integer hitsPerPage,
                                              @QueryParam("sort") @DefaultValue("music_title") final String sort,
                                              @QueryParam("ascending") @DefaultValue("true") final boolean ascendingSort);
        @GET
        @Path("/uri")
        @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
        @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
        public PageResultVO searchByURI(@QueryParam("q") String terms);
        
        @GET
        @Path("/image")
        @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
        @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
        public ArtistVO searchByImage(@QueryParam("artistUri") String artistUri);

        @GET
        @Path("/example")
        @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
        @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
        public PageResultVO searchByOpenTermsExample(@QueryParam("q") String terms,
                                                     @QueryParam("page") @DefaultValue("1") final Integer pageNumber,
                                                     @QueryParam("max") @DefaultValue("1000") final Integer max,
                                                     @QueryParam("sort") @DefaultValue("music_title") final String sort,
                                                     @QueryParam("ascending") @DefaultValue("true") final String ascendingSort);
}
