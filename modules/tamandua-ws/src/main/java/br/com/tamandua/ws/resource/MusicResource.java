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
import br.com.tamandua.service.GeneratorService;
import br.com.tamandua.service.IndexerService;
import br.com.tamandua.service.MusicService;
import br.com.tamandua.service.vo.MusicVO;
import br.com.tamandua.service.vo.validator.MessagesVO;

/**
 * @author Everton Rosario (erosario@gmail.com)
 *
 */
@Path("/musics")
public class MusicResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MusicResource.class);

	@POST
    @Path("/save")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO saveMusic(MusicVO music) {
        MusicService service = (MusicService) AppContext.getApplicationContext().getBean("musicService");
        return service.saveMusic(music);
    }
	@POST
    @Path("/saveDetailed")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO saveMusicDetailed(MusicVO music) {
        MusicService service = (MusicService) AppContext.getApplicationContext().getBean("musicService");
        return service.saveMusicDetailed(music);
    }
	@POST
    @Path("/saveLyric")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO saveMusicLyric(MusicVO music) {
		MusicService service = (MusicService) AppContext.getApplicationContext().getBean("musicService");
        return service.saveMusicLyric(music);
    }
	@GET
    @Path("/find/uri")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MusicVO findMusicByURI(@QueryParam("uri") String uri) {
        
        LOGGER.debug("/musics/find/uri");

        MusicService service = (MusicService) AppContext.getApplicationContext().getBean("musicService");
        MusicVO musicVO = service.findMusicByUri(uri);
        return musicVO;
    }

    @GET
    @Path("/find/artist-id")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Wrapped(element = "musics")
    public List<MusicVO> findMusicsByArtistId(@QueryParam("artist-id") Long artistId) {
        
        LOGGER.debug("/musics/find/artist-id");

        MusicService service = (MusicService) AppContext.getApplicationContext().getBean("musicService");
        List<MusicVO> musicVOs = service.findMusicByArtistId(artistId);
        return musicVOs;
    }

    @GET
    @Path("/find/id")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MusicVO findMusicId(@QueryParam("id") Long id) {
        LOGGER.debug("/musics/find/id");

        MusicService service = (MusicService) AppContext.getApplicationContext().getBean("musicService");
        MusicVO musicVO = service.findMusicById(id);
        return musicVO;
	}

    @GET
    @Path("/example")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MusicVO getMusicExample() {
        
        LOGGER.debug("/musics/example");

        MusicService service = (MusicService) AppContext.getApplicationContext().getBean("musicService");
        MusicVO musicVO = service.getMusicExample();
        return musicVO;
    }

	@POST
    @Path("/moderate")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO moderate(@QueryParam("moderate") Boolean moderate,@QueryParam("id") Long id) {
		LOGGER.debug("/musics/moderate");
		MusicService service = (MusicService) AppContext.getApplicationContext().getBean("musicService");
		return service.moderateById(moderate, id);
	}
	
	@POST
    @Path("/moderate/all")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO moderateAll(@QueryParam("moderate") Boolean moderate, @QueryParam("artistId") Long artistId) {
		LOGGER.debug("/musics/moderate/all");
		MusicService service = (MusicService) AppContext.getApplicationContext().getBean("musicService");
		return service.moderateAllByArtist(moderate, artistId);
	}
	
    @POST
    @Path("/moderate/selected")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO moderateSelected(@QueryParam("moderate") Boolean moderate, @QueryParam("ids") List<Long> ids){
    	LOGGER.debug("/musics/moderate/selected");
    	MusicService service = (MusicService) AppContext.getApplicationContext().getBean("musicService");
    	return service.moderateAllByIds(moderate, ids);
    }
	
	@POST
    @Path("/publish")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO publish(@QueryParam("publish") Boolean publish, @QueryParam("id") Long id) {
		LOGGER.debug("/musics/publish");
		MusicService service = (MusicService) AppContext.getApplicationContext().getBean("musicService");
		MessagesVO messages = service.publishById(publish, id);

		GeneratorService generatorService = (GeneratorService) AppContext.getApplicationContext().getBean("generatorService");
    	IndexerService indexerService = (IndexerService) AppContext.getApplicationContext().getBean("indexerService");
    	if(publish){	    	    	
	    	generatorService.uploadMusic(id);
	    	indexerService.indexMusic(id);
    	} else {
    		MusicVO music = service.findMusicById(id);
    		generatorService.removeMusicPage(music.getUri());
    		indexerService.cleanMusic(id);
    	}
    	return messages;
	}
	
	@POST
    @Path("/publish/all")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO publishAll(@QueryParam("publish") Boolean publish, @QueryParam("artistId") Long artistId) {
		LOGGER.debug("/musics/publish/all");
		MusicService service = (MusicService) AppContext.getApplicationContext().getBean("musicService");
		MessagesVO messages = service.publishAllByArtist(publish, artistId);

		GeneratorService generatorService = (GeneratorService) AppContext.getApplicationContext().getBean("generatorService");
    	IndexerService indexerService = (IndexerService) AppContext.getApplicationContext().getBean("indexerService");
    	if(publish){	    	    	
	    	generatorService.uploadMusicsByArtist(artistId);
	    	indexerService.indexArtist(artistId);
    	} else {
    		List<MusicVO> musics = service.findMusicByArtistId(artistId);
    		for(MusicVO music : musics){
    			generatorService.removeMusicPage(music.getUri());
    		}
    		indexerService.cleanArtist(artistId);
    	}
    	return messages;
	}
    
    @POST
    @Path("/publish/selected")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO publishSelected(@QueryParam("publish") Boolean publish, @QueryParam("ids") List<Long> ids){
    	LOGGER.debug("/musics/publish/selected");
    	MusicService service = (MusicService) AppContext.getApplicationContext().getBean("musicService");
    	MessagesVO messages = service.publishAllByIds(publish, ids);
    	
    	GeneratorService generatorService = (GeneratorService) AppContext.getApplicationContext().getBean("generatorService");
    	IndexerService indexerService = (IndexerService) AppContext.getApplicationContext().getBean("indexerService");
    	for(Long id : ids){
    		if(publish){       	
        		generatorService.uploadMusic(id);
        		indexerService.indexMusic(id);
    		} else {
    			MusicVO music = service.findMusicById(id);
        		generatorService.removeMusicPage(music.getUri());
        		indexerService.cleanMusic(id);
    		}
    	}
    	return messages;
    }
}
