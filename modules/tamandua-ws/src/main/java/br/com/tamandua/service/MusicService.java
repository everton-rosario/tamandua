package br.com.tamandua.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.tamandua.persistence.app.AppContext;
import br.com.tamandua.service.adapter.AlbumAdapter;
import br.com.tamandua.service.adapter.ArtistAdapter;
import br.com.tamandua.service.adapter.MusicAdapter;
import br.com.tamandua.service.util.StringNormalizer;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.MusicVO;
import br.com.tamandua.service.vo.validator.MessagesVO;

/**
 * @author BILL
 *
 */
@Service
public class MusicService {

	@Transactional
	public MessagesVO saveMusic(MusicVO musicVO) {
		MessagesVO messages = new MessagesVO();
		
		// Salva(cria ou atualiza) o Artista
		ArtistAdapter artistAdapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
		messages.merge( artistAdapter.saveArtist(musicVO.getArtist()) );
		if (musicVO.getArtist() == null || musicVO.getArtist().getIdArtist() == null) {
		    System.err.println("Não persistiu o artista: " + musicVO.getArtist() + ". Mensagens de erro: " + messages.toString());
		}
		// Busca o artista persistido e utiliza o id
		//ArtistVO artistPersisted = artistAdapter.findArtistByUri(musicVO.getArtist().getUri());

		// Salva ou cria a musica no banco
		MusicAdapter musicAdapter = (MusicAdapter) AppContext.getApplicationContext().getBean("musicAdapter");

		// Trata a URI para ser uma URI mais localizavel e independente do provider
		musicVO.setUri(StringNormalizer.normalizeURLPaths(musicVO.getArtist().getName(), musicVO.getTitle()));
		// Salva a musica e a letra da musica
		messages.merge(musicAdapter.saveMusic(musicVO));

		MusicVO musicPersisted = musicVO;
		//MusicVO musicPersisted = musicAdapter.findByUri(musicVO.getUri());
		if (musicPersisted == null || musicPersisted.getIdMusic() == null) {
		    System.err.println("Não persistiu a música ou não gerou um ID ["+musicPersisted+"]" + messages.toString());
		    throw new RuntimeException("Não persistiu a música ou não gerou um ID ["+musicPersisted+"]");
		}
		
		// Trata compositores
		messages.merge(musicAdapter.updateMusicArtists(musicPersisted, musicVO.getMusicArtists()));
		
		// Trata Tags (no caso estilos)
		messages.merge(musicAdapter.updateMusicTags(musicPersisted, musicVO.getStyles()));

        // Trata Tags (no caso freetags)
        messages.merge(musicAdapter.updateMusicFreeTags(musicPersisted, musicVO.getFreeStyles()));

        // Trata Albuns
		AlbumAdapter albumAdapter = (AlbumAdapter) AppContext.getApplicationContext().getBean("albumAdapter");
        messages.merge(albumAdapter.saveAlbum(musicVO.getAlbum(), musicPersisted));
		return messages;
	}
	@Transactional
	public MessagesVO saveMusicDetailed(MusicVO musicVO){
		MusicAdapter musicAdapter = (MusicAdapter) AppContext.getApplicationContext().getBean("musicAdapter");
		return musicAdapter.saveMusicDetailed(musicVO);
	}
	@Transactional
	public MessagesVO saveMusicLyric(MusicVO musicVO){
		MusicAdapter musicAdapter = (MusicAdapter) AppContext.getApplicationContext().getBean("musicAdapter");
		return musicAdapter.saveMusicLyric(musicVO);
	}

	@Transactional
	public MusicVO findMusicByUri(String uri) {
		MusicAdapter adapter = (MusicAdapter) AppContext.getApplicationContext().getBean("musicAdapter");
		return adapter.findByUri(uri);
	}

    @Transactional
	public List<MusicVO> findMusicByArtistId(Long artistId) {
        MusicAdapter adapter = (MusicAdapter) AppContext.getApplicationContext().getBean("musicAdapter");
        return adapter.findByArtistId(artistId);
    }

	@Transactional
	public MusicVO getMusicExample() {
		MusicAdapter adapter = (MusicAdapter) AppContext.getApplicationContext().getBean("musicAdapter");
		return adapter.getMusicExample();
	}

    @Transactional
    public MusicVO findMusicById(Long id) {
        MusicAdapter adapter = (MusicAdapter) AppContext.getApplicationContext().getBean("musicAdapter");
        return adapter.findById(id);
    }
    
    @Transactional
    public MessagesVO moderateById(Boolean moderate, Long id) {
    	MusicAdapter adapter = (MusicAdapter) AppContext.getApplicationContext().getBean("musicAdapter");
        return adapter.moderateById(moderate, id);
    }
    
    @Transactional
    public MessagesVO moderateAllByArtist(Boolean moderate, Long artistId) {
    	MusicAdapter adapter = (MusicAdapter) AppContext.getApplicationContext().getBean("musicAdapter");
        return adapter.moderateAllByArtist(moderate, artistId);
    }
    
    @Transactional
    public MessagesVO moderateAllByIds(Boolean moderate, List<Long> ids){
    	MusicAdapter adapter = (MusicAdapter) AppContext.getApplicationContext().getBean("musicAdapter");
    	return adapter.moderateAllByIds(moderate, ids);
    }
    
    @Transactional
    public MessagesVO publishById(Boolean publish, Long id) {
    	MusicAdapter adapter = (MusicAdapter) AppContext.getApplicationContext().getBean("musicAdapter");
        return adapter.publishById(publish, id);
    }
    
    @Transactional
    public MessagesVO publishAllByArtist(Boolean publish, Long artistId) {
    	MusicAdapter adapter = (MusicAdapter) AppContext.getApplicationContext().getBean("musicAdapter");
        return adapter.publishAllByArtist(publish, artistId);
    }
    
    @Transactional
    public MessagesVO publishAllByIds(Boolean publish, List<Long> ids){
    	MusicAdapter adapter = (MusicAdapter) AppContext.getApplicationContext().getBean("musicAdapter");
    	return adapter.publishAllByIds(publish, ids);
    }
}
