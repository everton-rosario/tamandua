/**
 * 
 */
package br.com.tamandua.searcher.ws.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;

import br.com.tamandua.searcher.SearchManager;
import br.com.tamandua.searcher.exception.InvalidIndexException;
import br.com.tamandua.service.util.StringNormalizer;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.ImageVO;
import br.com.tamandua.service.vo.PageResultVO;
import br.com.tamandua.service.vo.ResultItemVO;

/**
 * @author Everton Rosario
 */
public class SearcherAdapter {
    public PageResultVO searchByOpenTerms(String terms,
                                          final Integer pageNumber,
                                          final Integer max,
                                          final String sort,
                                          final Integer hitsPerPage,
                                          final boolean ascendingSort) throws InvalidIndexException {
        System.out.println("terms=" + terms);
        System.out.println(",pageNumber=" + pageNumber);
        System.out.println(",max=" + max);
        System.out.println(",sort=" + sort);
        System.out.println(",hitsPerPage=" + hitsPerPage);
        System.out.println(",ascendingSort=" + ascendingSort);
        
        
        SearchManager searchManager = SearchManager.getInstance();
        List<Document> resultList = searchManager.doPagingSearch(terms, pageNumber, max, sort, hitsPerPage, ascendingSort);

        return getPageResult(terms, pageNumber, max, sort, hitsPerPage, ascendingSort, resultList);
    }

    public PageResultVO searchByArtistAndMusic(String artistName, String musicTitle) throws InvalidIndexException {
        SearchManager searchManager = SearchManager.getInstance();
        searchManager.doCategorySearch(artistName, musicTitle);
        return new PageResultVO();
    }
    
    public PageResultVO searchByArtist(String artistName) throws InvalidIndexException {
        SearchManager searchManager = SearchManager.getInstance();
        String artistNameNormalized = StringNormalizer.normalizeText(artistName).toLowerCase();
        List<Document> resultList = searchManager.searchByArtist(artistNameNormalized);

        List<ResultItemVO> resultItems = new ArrayList<ResultItemVO>(resultList.size());
        for (Document document : resultList) {
            ResultItemVO resultItemVO = new ResultItemVO();
            resultItemVO.setIdArtist(Long.parseLong(document.get("id_artist")));
            resultItemVO.setArtistName(document.get("artist_name"));
            resultItemVO.setArtistUri(document.get("artist_uri"));
            resultItemVO.setArtistLetters(document.get("artist_letters"));
            resultItemVO.setMusicUri(document.get("music_uri"));
            resultItemVO.setIdtTrackRadio(document.get("idt_track_radio"));

            resultItems.add(resultItemVO);
        }
        PageResultVO pageResultVO = new PageResultVO();
        pageResultVO.setResultList(resultItems);
        
        return pageResultVO;
    }

    public PageResultVO searchByMusicURI(String musicUri) throws InvalidIndexException {
        SearchManager searchManager = SearchManager.getInstance();
        List<Document> resultList = searchManager.searchByMusicURI(musicUri);

        List<ResultItemVO> resultItems = new ArrayList<ResultItemVO>(resultList.size());
        for (Document document : resultList) {
            ResultItemVO resultItemVO = new ResultItemVO();
            resultItemVO.setIdMusic(Long.parseLong(document.get("id_music")));
            resultItemVO.setMusicUri(document.get("music_uri"));
            resultItemVO.setMusicTitle(document.get("music_title"));
            resultItemVO.setIdtTrackRadio(document.get("idt_track_radio"));
            resultItemVO.setUrlVideoClip(document.get("url_video_clip"));
            resultItemVO.setArtistName(document.get("artist_name"));
            
            resultItems.add(resultItemVO);
        }
        PageResultVO pageResultVO = new PageResultVO();
        pageResultVO.setResultList(resultItems);
        
        return pageResultVO;
    }

    private PageResultVO getPageResult(String terms, Integer pageNumber, Integer max, String sort, Integer hitsPerPage, boolean ascendingSort, List<Document> resultList) {
        
        PageResultVO pageResultVO = new PageResultVO();
        pageResultVO.setAscending(ascendingSort);
        pageResultVO.setPageSize(hitsPerPage);
        pageResultVO.setSortField(sort);
        pageResultVO.setTotalResults(resultList.size());
        pageResultVO.setStart((pageNumber -1) * hitsPerPage);
        pageResultVO.setResultList(getResultItems(resultList));
        return pageResultVO;
    }

    private List<ResultItemVO> getResultItems(List<Document> resultList) {
        List<ResultItemVO> itens = new ArrayList<ResultItemVO>(resultList.size());
        for (Document document : resultList) {
            ResultItemVO resultItemVO = new ResultItemVO();
            resultItemVO.setIdLyric(Long.parseLong(document.get("id_lyric")));
            resultItemVO.setIdMusic(Long.parseLong(document.get("id_music")));
            resultItemVO.setLaguage(document.get("laguage"));
            resultItemVO.setText(document.get("text"));
            //resultItemVO.setTotalAccess(document.get("total_access"));
            resultItemVO.setTotalAccess(0); // TODO colocar o valor correto quando tiver
            resultItemVO.setLyricUri(document.get("lyric_uri"));
            resultItemVO.setLyricTitle(document.get("lyric_title"));
            resultItemVO.setLyricType(document.get("lyric_type"));
            resultItemVO.setMusicTitle(document.get("music_title"));
            resultItemVO.setMusicUri(document.get("music_uri"));
            resultItemVO.setMusicUrl(document.get("music_url"));
            resultItemVO.setIdArtist(Long.parseLong(document.get("id_artist")));
            resultItemVO.setArtistName(document.get("artist_name"));
            resultItemVO.setArtistUri(document.get("artist_uri"));
            resultItemVO.setArtistLetters(document.get("artist_letters"));
            resultItemVO.setMusicTags(document.get("music_tags"));
            resultItemVO.setIdtTrackRadio(document.get("idt_track_radio"));
            
            itens.add(resultItemVO);
        }
        return itens;
    }

    public ArtistVO searchImagesByArtistUri(String artistUri) throws InvalidIndexException {
        SearchManager searchManager = SearchManager.getInstance();
        List<Document> resultList = searchManager.searchImagesByArtistURI(artistUri);
        
        ArtistVO artistVO = new ArtistVO();
        boolean first = true;
        for (Document document : resultList) {
            if (first) {
                first = false;
                artistVO.setIdArtist(Long.parseLong(document.get("id_artist")));
                artistVO.setName(document.get("artist_name"));
                artistVO.setUri(artistUri);
            }
            ImageVO imageVO = new ImageVO();
            imageVO.setIdImage(Long.parseLong(document.get("id_image")));
            imageVO.setDescription(document.get("image_description"));
            
            String imageHeight = document.get("image_height");
            if(imageHeight != null && !imageHeight.isEmpty()){
            	imageVO.setHeight(Integer.parseInt(imageHeight));
            }
            
            String imageWidth = document.get("image_width");
            if(imageWidth != null && !imageWidth.isEmpty()){
            	imageVO.setWidth(Integer.parseInt(imageWidth));
            }
            imageVO.setUri(document.get("image_uri"));
            imageVO.setUrl(document.get("image_url"));
            imageVO.setProvider(document.get("image_provider"));
            
            artistVO.addImage(imageVO);
        }
        
        return artistVO;
    }

}
