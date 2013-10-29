package br.com.tamandua.ws.resource;

import static br.com.tamandua.service.proxy.ProxyHelper.APPLICATION_JSON_WITH_CHARSET_UTF8;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.tamandua.persistence.app.AppContext;
import br.com.tamandua.service.ArtistService;
import br.com.tamandua.service.GeneratorService;
import br.com.tamandua.service.IndexerService;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.ImageVO;
import br.com.tamandua.service.vo.validator.MessagesVO;
import br.com.tamandua.ws.service.ImageService;

/**
 * @author Everton Rosario (erosario@gmail.com)
 *
 */
@Path("/artists")
public class ArtistResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtistResource.class);

	@POST
    @Path("/save")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO saveArtist(ArtistVO artist) {
		LOGGER.debug("/artists/save");
		
        ArtistService service = (ArtistService) AppContext.getApplicationContext().getBean("artistService");
        return service.saveArtist(artist);
    }
	
    @POST
    @Path("/update/fill-sort-name")
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO fillArtistSortName() {
        
        LOGGER.debug("/artists/update/fill-sort-name");
        
        ArtistService service = (ArtistService) AppContext.getApplicationContext().getBean("artistService");
        return service.fillArtistSortName();
    }
    
    @GET
    @Path("/find/id")
    @Consumes( { MediaType.APPLICATION_JSON } )
    @Produces( { MediaType.APPLICATION_JSON } )
    public ArtistVO findArtistById(@QueryParam("id") Long id) {
        
        LOGGER.debug("/artists/find/id");

        ArtistService service = (ArtistService) AppContext.getApplicationContext().getBean("artistService");
        ArtistVO artistVO = service.findArtistById(id);
        return artistVO;
    }

    @GET
    @Path("/find/uri")
    @Consumes( { MediaType.APPLICATION_JSON } )
    @Produces( { MediaType.APPLICATION_JSON } )
    public ArtistVO findArtistByURI(@QueryParam("uri") String uri) {
        
        LOGGER.debug("/artists/find/uri");

        ArtistService service = (ArtistService) AppContext.getApplicationContext().getBean("artistService");
        ArtistVO artistVO = service.findArtistByUri(uri);
        return artistVO;
    }

    @GET
    @Path("/find/image-id")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public ArtistVO findArtistByImageId(Long idImage) {
        LOGGER.debug("/artists/find/image-id");

        ArtistService service = (ArtistService) AppContext.getApplicationContext().getBean("artistService");
        ArtistVO artistVO = service.findArtistByImageId(idImage);
        return artistVO;
    }
    
    @GET
    @Path("/find/letter")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Wrapped(element = "artists")
    public List<ArtistVO> findArtistsByLetter(@QueryParam("letter") String letter) {
        LOGGER.debug("/artists/find/letter");

        ArtistService service = (ArtistService) AppContext.getApplicationContext().getBean("artistService");
        List<ArtistVO> artistVOs = service.findArtistsByLetter(letter);
        return artistVOs;
    }
    
    @GET
    @Path("/find/with-lyrics-published/by-letter")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Wrapped(element = "artists")
    public List<ArtistVO> findArtistsWithLyricsPublishedByLetter(@QueryParam("letter") String letter) {
        LOGGER.debug("/artists/find/lyricsPublished/letter");

        ArtistService service = (ArtistService) AppContext.getApplicationContext().getBean("artistService");
        List<ArtistVO> artistVOs = service.findArtistsWithLyricsPublishedByLetter(letter);
        return artistVOs;
    }
    
    @POST
    @Path("/upload")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public void uploadImage(ArtistVO artist){
		if(artist.getFile() != null && artist.getFile().length > 0){
    		ImageService imageService = (ImageService) AppContext.getApplicationContext().getBean("imageService");
    		ImageVO image = imageService.uploadImage(artist);
    		
    		ArtistService service = (ArtistService) AppContext.getApplicationContext().getBean("artistService");
    		artist = service.findArtistByUri(artist.getUri());
    		artist.getAllImages().add(image);
    		service.saveArtist(artist);
		}
    }
    
    @POST
    @Path("/remove/image")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO removeImage(@QueryParam("idArtist") Long idArtist, @QueryParam("ids") List<Long> ids){
    	LOGGER.debug("/artists/remove/image");
    	
    	ArtistVO artistVO = new ArtistVO();
    	Set<ImageVO> allImages = new HashSet<ImageVO>();
    	
    	for (Long idsImage : ids) {
    		ImageVO imageVO = new ImageVO();
    		imageVO.setIdImage(idsImage);
    		allImages.add(imageVO);
		}
    	
    	artistVO.setIdArtist(idArtist);
    	artistVO.setAllImages(allImages);
    	
    	ImageService service = (ImageService) AppContext.getApplicationContext().getBean("imageService");
    	
		return service.removeImage(artistVO);
    	
    }

    @GET
    @Path("/example")
    //@Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public ArtistVO getArtistExample() {
        
        LOGGER.debug("/artists/example");

        ArtistService service = (ArtistService) AppContext.getApplicationContext().getBean("artistService");
        ArtistVO artistVO = service.getArtistExample();
        return artistVO;
    }
    
	@POST
    @Path("/moderate")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO moderate(@QueryParam("moderate") Boolean moderate,@QueryParam("id") Long id) {
		LOGGER.debug("/artists/moderate");
		 ArtistService service = (ArtistService) AppContext.getApplicationContext().getBean("artistService");
		return service.moderateById(moderate, id);
	}
	
	@POST
    @Path("/moderate/all")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO moderateAll(@QueryParam("moderate") Boolean moderate, @QueryParam("letters") String letters) {
		LOGGER.debug("/artists/moderate/all");
		ArtistService service = (ArtistService) AppContext.getApplicationContext().getBean("artistService");
		return service.moderateAllByLetters(moderate, letters);
	}
	
    @POST
    @Path("/moderate/selected")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO moderateSelected(@QueryParam("moderate") Boolean moderate, @QueryParam("ids") List<Long> ids){
    	LOGGER.debug("/artists/moderate/selected");
    	ArtistService service = (ArtistService) AppContext.getApplicationContext().getBean("artistService");
    	return service.moderateAllByIds(moderate, ids);
    }
	
	@POST
    @Path("/publish")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO publish(@QueryParam("publish") Boolean publish, @QueryParam("id") Long id) {
		LOGGER.debug("/artists/publish");
		ArtistService service = (ArtistService) AppContext.getApplicationContext().getBean("artistService");
		MessagesVO messages = service.publishById(publish, id);

		GeneratorService generatorService = (GeneratorService) AppContext.getApplicationContext().getBean("generatorService");
    	IndexerService indexerService = (IndexerService) AppContext.getApplicationContext().getBean("indexerService");
    	if(publish){    	
	    	generatorService.uploadArtist(id);
	    	indexerService.indexArtist(id);
    	} else {
    		ArtistVO artist = service.findArtistById(id);
    		generatorService.removeArtistPage(artist.getUri());
    		indexerService.cleanArtist(id);
    	}
    	return messages;
	}
	
	@POST
    @Path("/publish/all")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO publishAll(@QueryParam("publish") Boolean publish, @QueryParam("letters") String letters) {
		LOGGER.debug("/artists/publish/all");
		ArtistService service = (ArtistService) AppContext.getApplicationContext().getBean("artistService");
		MessagesVO messages = service.publishAllByLetters(publish, letters);

		GeneratorService generatorService = (GeneratorService) AppContext.getApplicationContext().getBean("generatorService");
    	IndexerService indexerService = (IndexerService) AppContext.getApplicationContext().getBean("indexerService");
    	if(publish){    	
	    	generatorService.uploadArtistsByLetter(letters);
	    	indexerService.indexByLetter(letters);
    	} else {
    		List<ArtistVO> artists = service.findArtistsByLetter(letters);
    		for(ArtistVO artist : artists){
    			generatorService.removeArtistPage(artist.getUri());
    		}
    		indexerService.cleanByLetter(letters);
    	}
    	return messages;
	}
    
    @POST
    @Path("/publish/selected")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO publishSelected(@QueryParam("publish") Boolean publish, @QueryParam("ids") List<Long> ids){
    	LOGGER.debug("/artists/publish/selected");
    	ArtistService service = (ArtistService) AppContext.getApplicationContext().getBean("artistService");
    	MessagesVO messages = service.publishAllByIds(publish, ids);
    	
    	GeneratorService generatorService = (GeneratorService) AppContext.getApplicationContext().getBean("generatorService");
    	IndexerService indexerService = (IndexerService) AppContext.getApplicationContext().getBean("indexerService");
    	for(Long id : ids){
    		if(publish){        	
        		generatorService.uploadArtist(id);
        		indexerService.indexArtist(id);
        	} else {
	    		ArtistVO artist = service.findArtistById(id);
	    		generatorService.removeArtistPage(artist.getUri());
	    		indexerService.cleanArtist(id);
        	} 
    	}
    	return messages;
    }
 
	@POST
    @Path("/publish/letter")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public void publishLetter(@QueryParam("publish") Boolean publish, @QueryParam("letters") String letters) {
		LOGGER.debug("/artists/public/letter");
		
		GeneratorService generatorService = (GeneratorService) AppContext.getApplicationContext().getBean("generatorService");
		if(publish){    	
	    	generatorService.uploadArtistByLetter(letters);
		} else {
			generatorService.removeLetterPage(letters);
		}
	}
}
