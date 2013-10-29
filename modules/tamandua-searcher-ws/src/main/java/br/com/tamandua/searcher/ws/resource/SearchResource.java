package br.com.tamandua.searcher.ws.resource;

import static br.com.tamandua.service.proxy.ProxyHelper.APPLICATION_JSON_WITH_CHARSET_UTF8;
import static br.com.tamandua.service.proxy.ProxyHelper.APPLICATION_XML_WITH_CHARSET_UTF8;
import static br.com.tamandua.service.proxy.ProxyHelper.TEXT_PLAIN_WITH_CHARSET_UTF8;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.tamandua.searcher.exception.InvalidIndexException;
import br.com.tamandua.searcher.ws.adapter.SearcherAdapter;
import br.com.tamandua.service.util.StringNormalizer;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.PageResultVO;
import br.com.tamandua.service.vo.ResultItemVO;

/**
 * @author Everton Rosario (erosario@gmail.com)
 *
 */
@Path("/search")
public class SearchResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResource.class);

    @GET
    @Path("/open")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public PageResultVO searchByOpenTerms(@QueryParam("q") String terms,
                                          @QueryParam("page") @DefaultValue("1") final Integer pageNumber,
                                          @QueryParam("max") @DefaultValue("1000") final Integer max,
                                          @QueryParam("hitsPerPage") @DefaultValue("50") final Integer hitsPerPage,
                                          @QueryParam("sort") @DefaultValue("music_title") final String sort,
                                          @QueryParam("ascending") @DefaultValue("true") final boolean ascendingSort) throws InvalidIndexException {
        System.out.println("type=query");
        System.out.println(",q=" + terms);
        System.out.println(",page=" + pageNumber);
        System.out.println(",max=" + max);
        System.out.println(",hitsPerPage=" + hitsPerPage);
        System.out.println(",sort=" + sort);
        System.out.println(",ascending=" + ascendingSort);

        LOGGER.debug("/search/open");
        
        SearcherAdapter adapter = new SearcherAdapter();
        PageResultVO resultList = adapter.searchByOpenTerms(StringNormalizer.normalizeText(terms), pageNumber, max, sort, hitsPerPage, ascendingSort);
        
        return resultList;
    }

    @GET
    @Path("/uri")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public PageResultVO searchByURI(@QueryParam("q") String terms) throws InvalidIndexException {
        System.out.println("type=query");
        System.out.println(",q=" + terms);

        LOGGER.debug("/search/uri");
        
        SearcherAdapter adapter = new SearcherAdapter();
        PageResultVO resultList = adapter.searchByMusicURI(terms);
        
        return resultList;
    }

    @GET
    @Path("/image")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public ArtistVO searchByImage(@QueryParam("artistUri") String artistUri) throws InvalidIndexException {
        System.out.println("type=query");
        System.out.println(",artistUri=" + artistUri);

        LOGGER.debug("/search/image");
        
        SearcherAdapter adapter = new SearcherAdapter();
        ArtistVO artistResult = adapter.searchImagesByArtistUri(artistUri);
        
        return artistResult;
    }

    @GET
    @Path("/category")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public PageResultVO searchByCategories(@QueryParam("artistName") String artistName,
                                           @QueryParam("musicTitle") String musicTitle) throws InvalidIndexException {
        System.out.println("artistName=" + artistName);
        System.out.println(",musicTitle=" + musicTitle);

        LOGGER.debug("/search/category");
        
        SearcherAdapter adapter = new SearcherAdapter();
        PageResultVO resultList = adapter.searchByArtistAndMusic(artistName, musicTitle);
        
        return resultList;
    }
    
    @GET
    @Path("/artist")
    @Consumes( { TEXT_PLAIN_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_XML_WITH_CHARSET_UTF8 } )
    public PageResultVO searchByArtist(@QueryParam("artistName") String artistName) throws InvalidIndexException {
        System.out.println("artistName=" + artistName);

        LOGGER.debug("/search/artist");
        
        SearcherAdapter adapter = new SearcherAdapter();
        PageResultVO pageResultVO = adapter.searchByArtist(artistName);
        
        return pageResultVO;
    }

    @GET
    @Path("/example")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public PageResultVO searchByOpenTermsExample(@QueryParam("q") String terms,
                                                 @QueryParam("page") @DefaultValue("1") final Integer pageNumber,
                                                 @QueryParam("max") @DefaultValue("1000") final Integer max,
                                                 @QueryParam("sort") @DefaultValue("music_title") final String sort,
                                                 @QueryParam("ascending") @DefaultValue("true") final String ascendingSort) {
        System.out.println("type=query");
        System.out.println(",q=" + terms);
        System.out.println(",page=" + pageNumber);
        System.out.println(",max=" + max);
        System.out.println(",sort=" + sort);
        System.out.println(",ascending=" + ascendingSort);

		LOGGER.debug("/search/example");
		
        return getPageResultVOExample(3, 147, 50);
    }

    private PageResultVO getPageResultVOExample(int numPages, int numResults, int resultsPerPage) {
        PageResultVO pageResultVO = new PageResultVO();
        pageResultVO.setPageSize(resultsPerPage);
        pageResultVO.setTotalPages(numPages);
        pageResultVO.setTotalResults(numResults);

        for (int i = 0; i < resultsPerPage; i++) {
	        ResultItemVO itemVO = getResultItemExample("Numero " + i);
	        pageResultVO.getResultList().add(itemVO);
        }
        
        return pageResultVO;
    }

	private ResultItemVO getResultItemExample(String label) {
		ResultItemVO resultItemVO = new ResultItemVO();
		resultItemVO.setArtistLetters("a");
		resultItemVO.setArtistName(label + " Artist Name");
		resultItemVO.setArtistUri(StringNormalizer.normalizeURLPaths(label.split(" ")));
		resultItemVO.setIdArtist((long)label.hashCode());
		resultItemVO.setIdLyric((long)label.hashCode());
		resultItemVO.setIdMusic((long)label.hashCode());
		resultItemVO.setLaguage(label);
		resultItemVO.setLyricTitle(label + " Lyric Title");
		resultItemVO.setLyricType("ORIGINAL");
		resultItemVO.setLyricUri(StringNormalizer.normalizeURLPaths(label.split(" ")));
		resultItemVO.setMusicTags("TESTE|ROCK|POP");
		resultItemVO.setMusicTitle(label + " Music Title");
		resultItemVO.setMusicUri(StringNormalizer.normalizeURLPaths(label.split(" ")));
		resultItemVO.setMusicUrl("http://tocaletra.com.br/" + StringNormalizer.normalizeURLPaths(label.split(" ")));
		resultItemVO.setTotalAccess(1000);
		resultItemVO.setText("Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed \n" +
				             "diam nonummy nibh euismod tincidunt ut laoreet dolore magna \n" + 
				             "aliquam erat volutpat. Ut wisi enim ad minim veniam, quis \n" + 
				             "nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip \n" + 
				             " \n" + 
				             "ex ea commodo consequat. Duis autem vel eum iriure dolor in \n" + 
				             "hendrerit in vulputate velit esse molestie consequat, vel illum \n" + 
				             "lius quod ii legunt saepius. Claritas est etiam processus \n" + 
				             "lius quod ii legunt saepius. Claritas est etiam processus \n" + 
				             " \n" + 
				             "hendrerit in vulputate velit esse molestie consequat, vel illum \n" + 
				             "olore eu feugiat nulla facilisis at vero eros et accumsan et \n" + 
				             "lius quod ii legunt saepius. Claritas est etiam processus \n" + 
				             "lius quod ii legunt saepius. Claritas est etiam processus \n" + 
				             " \n" + 
				             "hendrerit in vulputate velit esse molestie consequat, vel illum \n" + 
				             "hendrerit in vulputate velit esse molestie consequat, vel illum \n" 
				             );
		
		return resultItemVO;
	}
	
}
