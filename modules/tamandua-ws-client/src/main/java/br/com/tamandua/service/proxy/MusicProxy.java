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

import br.com.tamandua.service.vo.MusicVO;
import br.com.tamandua.service.vo.validator.MessagesVO;

/**
 * @author BILL
 *
 */
@Path("/musics")
public interface MusicProxy {
    


	@POST
    @Path("/save")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO saveMusic(MusicVO music);

	@GET
    @Path("/find/uri")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MusicVO findMusicByURI(@QueryParam("uri") String uri);

    @GET
    @Path("/find/artist-id")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Wrapped(element = "musics")
    public List<MusicVO> findMusicsByArtistId(@QueryParam("artist-id") Long artistId);

    @GET
    @Path("/find/id")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MusicVO findMusicId(@QueryParam("id") Long id);

    @GET
    @Path("/example")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MusicVO getMusicExample();
    
    @POST
    @Path("/saveDetailed")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO saveMusicDetailed(MusicVO music);
    
    @POST
    @Path("/moderateMusics")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public void moderateMusicsSelected(MusicVO musicVO);
    
    
    

}
