package br.com.tamandua.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.tamandua.persistence.app.AppContext;
import br.com.tamandua.service.adapter.ArtistAdapter;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.validator.MessagesVO;

/**
 * @author Everton Rosario (erosario@gmail.com)
 *
 */
@Service
public class ArtistService {
    
    @Transactional
    public ArtistVO getArtistExample() {
        ArtistAdapter adapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
        return adapter.getArtistExample();
    }

    @Transactional
	public ArtistVO findArtistById(Long id) {
        ArtistAdapter adapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
        return adapter.findArtistById(id);
	}
    
    @Transactional
	public ArtistVO findArtistByUri(String uri) {
        ArtistAdapter adapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
        return adapter.findArtistByUri(uri);
	}
    
    @Transactional
    public MessagesVO saveArtist(ArtistVO artist) {
        ArtistAdapter adapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
        return adapter.saveArtist(artist);
    }
    
    @Transactional
    public MessagesVO fillArtistSortName() {
        ArtistAdapter adapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
        return adapter.fillArtistSortName();
    }

    public ArtistVO findArtistByImageId(Long idImage) {
        ArtistAdapter adapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
        return adapter.findArtistByImageId(idImage);
    }

    public List<ArtistVO> findArtistsByLetter(String letter) {
        ArtistAdapter adapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
        return adapter.findArtistsByLetter(letter);
    }
    
    public List<ArtistVO> findArtistsWithLyricsPublishedByLetter(String letter) {
        ArtistAdapter adapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
        return adapter.findArtistsWithLyricsPublishedByLetter(letter);
    }
    
    @Transactional
    public MessagesVO moderateById(Boolean moderate, Long id) {
    	ArtistAdapter adapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
        return adapter.moderateById(moderate, id);
    }
    
    @Transactional
    public MessagesVO moderateAllByLetters(Boolean moderate, String letters) {
    	ArtistAdapter adapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
        return adapter.moderateAllByLetters(moderate, letters);
    }
    
    @Transactional
    public MessagesVO moderateAllByIds(Boolean moderate, List<Long> ids){
    	ArtistAdapter adapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
    	return adapter.moderateAllByIds(moderate, ids);
    }
    
    @Transactional
    public MessagesVO publishById(Boolean publish, Long id) {
    	ArtistAdapter adapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
        return adapter.publishById(publish, id);
    }
    
    @Transactional
    public MessagesVO publishAllByLetters(Boolean publish, String letters) {
    	ArtistAdapter adapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
        return adapter.publishAllByLetters(publish, letters);
    }
    
    @Transactional
    public MessagesVO publishAllByIds(Boolean publish, List<Long> ids){
    	ArtistAdapter adapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
    	return adapter.publishAllByIds(publish, ids);
    }
    
}
