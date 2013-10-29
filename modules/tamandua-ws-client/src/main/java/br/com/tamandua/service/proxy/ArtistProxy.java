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

import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.validator.MessagesVO;

/**
 * @author Everton Rosario (erosario@gmail.com)
 * ArtistProxy
 */
@Path("/artists")
public interface ArtistProxy {

    @POST
    @Path("/save")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO saveArtist(ArtistVO artist);

    @POST
    @Path("/moderate")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO moderateArtist(ArtistVO artist);
    
    @POST
    @Path("/public")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO publicArtist(ArtistVO artist);
    
    @POST
    @Path("/update/fill-sort-name")
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO fillArtistSortName();

    @GET
    @Path("/find/id")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public ArtistVO findArtistById(@QueryParam("id") Long id);
    
    @GET
    @Path("/find/uri")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public ArtistVO findArtistByURI(@QueryParam("uri") String uri);

    @GET
    @Path("/find/letter")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Wrapped(element = "artists")
    public List<ArtistVO> findArtistsByLetter(@QueryParam("letter") String letter);
    
    @GET
    @Path("/find/with-lyrics-published/by-letter")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Wrapped(element = "artists")
    public List<ArtistVO> findArtistsWithLyricsPublishedByLetter(@QueryParam("letter") String letter);
    
    @GET
    @Path("/find/image-id")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public ArtistVO findArtistByImageId(Long idImage);

    @GET
    @Path("/example")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public ArtistVO getArtistExample();
    
    @POST
    @Path("/moderateArtists")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public void moderateArtistsSelected(ArtistVO artistVO);

    
    @POST
    @Path("/upload")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public void uploadImage(ArtistVO artistVO);
}
