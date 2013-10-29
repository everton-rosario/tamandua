package br.com.tamandua.service.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.tamandua.persistence.Artist;
import br.com.tamandua.persistence.Image;
import br.com.tamandua.persistence.app.AppContext;
import br.com.tamandua.persistence.dao.ArtistHome;
import br.com.tamandua.persistence.dao.ImageHome;
import br.com.tamandua.service.util.StringNormalizer;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.ImageVO;
import br.com.tamandua.service.vo.validator.ActionMessageVO;
import br.com.tamandua.service.vo.validator.Level;
import br.com.tamandua.service.vo.validator.LevelMessageVO;
import br.com.tamandua.service.vo.validator.MessagesVO;
import br.com.tamandua.service.vo.validator.ActionMessageVO.Action;

/**
 * @author Everton Rosario (erosario@gmail.com)
 */
@Component
public class ArtistAdapter {
    
    @Transactional
    public ArtistVO getArtistExample() {
        ArtistVO vo = new ArtistVO();
        vo.setName("Artista Exemplo da Aplicacao");
        vo.setUri("/artista-exemplo/");

        ImageVO image = new ImageVO();
        image.setUri("/uri/imagem.jpg");
        vo.setImage(image);
        ImageVO imageHuge = new ImageVO();
        imageHuge.setUri("/uri/imagemHuge.jpg");
        vo.setImageHuge(imageHuge);
        ImageVO imageTyny = new ImageVO();
        imageTyny.setUri("/uri/imagemTiny.jpg");
        vo.setImageTiny(imageTyny);
        vo.getAllImages().add(imageTyny);
        vo.getAllImages().add(image);
        vo.getAllImages().add(imageHuge);
        vo.setFlag_moderate(ArtistVO.FLAG_MODERATE);
        vo.setFlag_public(ArtistVO.FLAG_PUBLIC);
        
        return vo;
    }

	public ArtistVO findArtistById(Long id) {
		ArtistHome dao = (ArtistHome) AppContext.getApplicationContext().getBean("artistHome");
		return parse(dao.findById(id), true);
	}
    
	public ArtistVO findArtistByUri(String uri) {
		ArtistHome dao = (ArtistHome) AppContext.getApplicationContext().getBean("artistHome");
		return parse(dao.findByUri(uri), true);
	}

	public ArtistVO findArtistByImageId(Long idImage) {
        ArtistHome dao = (ArtistHome) AppContext.getApplicationContext().getBean("artistHome");
        return parse(dao.findByImageId(idImage), true);
    }

	public List<ArtistVO> findArtistsByLetter(String letter) {
        ArtistHome dao = (ArtistHome) AppContext.getApplicationContext().getBean("artistHome");
        return parse(dao.findByLetter(letter));
    }
	
	public List<ArtistVO> findArtistsWithLyricsPublishedByLetter(String letter) {
        ArtistHome dao = (ArtistHome) AppContext.getApplicationContext().getBean("artistHome");
        return parse(dao.findWithLyricsPublishedByLetter(letter));
    }

	@Transactional
	public MessagesVO fillArtistSortName() {
        ArtistHome dao = (ArtistHome) AppContext.getApplicationContext().getBean("artistHome");
        List<Artist> allArtists = dao.findAll();
        for (Artist artist : allArtists) {
            artist.setSortName(StringNormalizer.normalizeArtistName(artist.getName()));
            dao.merge(artist);
        }
        dao.flush();
        MessagesVO messagesVO = new MessagesVO();
        messagesVO.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "atualizados " + allArtists.size()+ " artistas")));
        return messagesVO;
    }

    @Transactional
	public MessagesVO saveArtist(ArtistVO artistVO) {
        MessagesVO messages = new MessagesVO();
        if (artistVO == null) {
            messages.addError("artistVO cannot be null");
            return messages;
        }

        // Faz a persistencia das imagens do artista
        ImageHome imageHome = (ImageHome) AppContext.getApplicationContext().getBean("imageHome");
        ImageAdapter imageAdapter = (ImageAdapter) AppContext.getApplicationContext().getBean("imageAdapter");
        if (artistVO.getImage() != null) {
            ImageVO imageVO = artistVO.getImage();
            messages.merge(imageAdapter.saveImage(imageVO));
            ImageVO persistedImageVO = imageAdapter.findByUri(imageVO.getUri());
            artistVO.setImage(persistedImageVO);
        }
        if (artistVO.getImageHuge() != null) {
            ImageVO imageVO = artistVO.getImageHuge();
            messages.merge(imageAdapter.saveImage(imageVO));
            ImageVO persistedImageVO = imageAdapter.findByUri(imageVO.getUri());
            artistVO.setImageHuge(persistedImageVO);
        }
        if (artistVO.getImageTiny() != null) {
            ImageVO imageVO = artistVO.getImageTiny();
            messages.merge(imageAdapter.saveImage(imageVO));
            ImageVO persistedImageVO = imageAdapter.findByUri(imageVO.getUri());
            artistVO.setImageTiny(persistedImageVO);
        }
        Set<Image> allImages = new HashSet<Image>();
        if (artistVO.getAllImages() != null && !artistVO.getAllImages().isEmpty()) {
            Set<ImageVO> persistedImagesVO = new HashSet<ImageVO>();
            for (ImageVO imageVO : artistVO.getAllImages()) {
                messages.merge(imageAdapter.saveImage(imageVO));
                ImageVO persistedImageVO = imageAdapter.findByUri(imageVO.getUri());
                persistedImagesVO.add(persistedImageVO);
                allImages.add(imageHome.findById(persistedImageVO.getIdImage()));
            }
            artistVO.setAllImages(persistedImagesVO);
        }

        ArtistHome artistHome = (ArtistHome) AppContext.getApplicationContext().getBean("artistHome");
	    
        Artist artist = artistHome.findByUri(artistVO.getUri());
        if (artist == null) {
            artist = parse(artistVO, false);
            artist.setSortName(StringNormalizer.normalizeArtistName(artist.getName()));
            artist.setUri("/"+StringNormalizer.normalizeArtistName(artist.getName())+"/");
            artist.setFlag_moderate(ArtistVO.FLAG_NO_MODERATE);
            artist.setFlag_public(ArtistVO.FLAG_NO_PUBLIC);            
            artistHome.persist(artist);
            artistHome.flush();
            artist.getAllImages().addAll(allImages);
            artistHome.merge(artist);
            artistHome.flush();
            messages.addAction(new ActionMessageVO(Action.CREATE, new LevelMessageVO(Level.INFO, "artist ["+artist.getIdArtist()+"] created")));
        } else {
        	Artist parsed = parse(artistVO, true);
            mergeLetters(artist, parsed.getLetters());
            artist.getAllImages().addAll(parsed.getAllImages());
            artist.setSortName(StringNormalizer.normalizeArtistName(parsed.getName()));
            artist.setName(parsed.getName());
            artist.setDtBirth(parsed.getDtBirth());
            artist.setDtEnd(parsed.getDtEnd());
            artist.setImage(parsed.getImage());
            artist.setImageHuge(parsed.getImageHuge());
            artist.setImageTiny(parsed.getImageTiny());
            artist.setFlag_moderate(ArtistVO.FLAG_NO_MODERATE);
            artist.setFlag_public(ArtistVO.FLAG_NO_PUBLIC);
            artistHome.merge(artist);
            artistHome.flush();
            messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "artist ["+artist.getIdArtist()+"] updated")));
        }
        
        artistVO.setIdArtist(artist.getIdArtist());
        return messages;
	}
    
    @Transactional
	public MessagesVO moderateById(Boolean moderate, Long id) {
    	MessagesVO messages = new MessagesVO();
    	ArtistHome artistHome = (ArtistHome) AppContext.getApplicationContext().getBean("artistHome");
    	Artist artist = artistHome.findById(id);
    	
    	if(moderate){
    		artist.setFlag_moderate(ArtistVO.FLAG_MODERATE);
    	}else{
    		artist.setFlag_moderate(ArtistVO.FLAG_NO_MODERATE);
    	}    	    	
    	artistHome.merge(artist);
        artistHome.flush();
        
        messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "artist ["+artist.getIdArtist()+"] moderated")));
    	return messages;    	
    }
    
    @Transactional
	public MessagesVO moderateAllByLetters(Boolean moderate, String letters) {
    	MessagesVO messages = new MessagesVO();    	
    	ArtistHome artistHome = (ArtistHome) AppContext.getApplicationContext().getBean("artistHome");    	
    	artistHome.moderateAllByLetters(moderate, letters);    	    	
        artistHome.flush();
        
        messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "artists with letters ["+ letters +"] moderated")));
    	return messages;
    	
    }
    
    @Transactional
	public MessagesVO moderateAllByIds(Boolean moderate, List<Long> ids){
    	MessagesVO messages = new MessagesVO();

    	ArtistHome artistHome = (ArtistHome) AppContext.getApplicationContext().getBean("artistHome");    	
    	artistHome.moderateAllByIds(moderate, ids);    	
    	artistHome.flush();
    	
        messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "artists ["+ids+"] moderated")));
    	return messages;    	
    }

    @Transactional
	public MessagesVO publishById(Boolean publish, Long id) {
    	MessagesVO messages = new MessagesVO();
    	ArtistHome artistHome = (ArtistHome) AppContext.getApplicationContext().getBean("artistHome");
    	Artist artist = artistHome.findById(id);
    	
    	if(publish){
    		artist.setFlag_public(ArtistVO.FLAG_PUBLIC);
    	}else{
    		artist.setFlag_public(ArtistVO.FLAG_NO_PUBLIC);
    	}    	    	
    	artistHome.merge(artist);
        artistHome.flush();
        
        messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "artist ["+artist.getIdArtist()+"] updated")));
    	return messages;
    }
    
    @Transactional
	public MessagesVO publishAllByLetters(Boolean publish, String letters) {
    	MessagesVO messages = new MessagesVO();    	
    	ArtistHome artistHome = (ArtistHome) AppContext.getApplicationContext().getBean("artistHome");    	
    	artistHome.publishAllByLetters(publish, letters);    	    	
        artistHome.flush();
        
        messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "artists with letters ["+ letters +"] updated")));
    	return messages;
    }

    @Transactional
	public MessagesVO publishAllByIds(Boolean publish, List<Long> ids){
    	MessagesVO messages = new MessagesVO();

    	ArtistHome artistHome = (ArtistHome) AppContext.getApplicationContext().getBean("artistHome");    	
    	artistHome.publishAllByIds(publish, ids);    	
    	artistHome.flush();
    	
        messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "artists ["+ids+"] updated")));
    	return messages;    	
    }
    
	private void mergeLetters(Artist artist, String letters) {
		if (artist.getLetters() == null) {
			artist.setLetters("");
		}
	    for (int i = 0; i < letters.length(); i++) {
	        if (artist.getLetters().indexOf(letters.charAt(i)) < 0) {
	            artist.setLetters(artist.getLetters() + letters.charAt(i)); 
	        }
            
        }
    }

    /**
	 * Realiza a transofrmacao de Entity para VO
	 * @param entity
	 * @return
	 */
	public static ArtistVO parse(Artist entity, boolean isToDigg) {
		if (entity == null) {
			return null;
		}

		ArtistVO vo = new ArtistVO();
		//vo.setAlbums(entity.getAlbums());
		//vo.setArtistAliases(entity.getArtistAliases());
		//vo.setArtistDiscographies(entity.getArtistDiscographies());
		vo.setDtBirth(entity.getDtBirth());
		vo.setDtEnd(entity.getDtEnd());
		vo.setIdArtist(entity.getIdArtist());
		vo.setIdCountry(entity.getIdCountry());
		if (isToDigg) {
			vo.setImage(ImageAdapter.parse(entity.getImage()));
			vo.setImageHuge(ImageAdapter.parse(entity.getImageHuge()));
			vo.setImageTiny(ImageAdapter.parse(entity.getImageTiny()));
			vo.setAllImages(ImageAdapter.parseEntities(entity.getAllImages()));
		}
		vo.setLetters(entity.getLetters());
		//vo.setMusicArtists(entity.getMusicArtists());
		//vo.setMusics(entity.getMusics());
		vo.setName(entity.getName());
		vo.setSortName(entity.getSortName());
		vo.setTotalAccess(entity.getTotalAccess());
		vo.setType(entity.getType());
		vo.setUri(entity.getUri());
		vo.setUrl(entity.getUrl());
		vo.setFlag_moderate(entity.getFlag_moderate());
		vo.setFlag_public(entity.getFlag_public());
		return vo;
	}
	
	/**
	 * Realiza a transofrmacao de Entity para VO de uma colecao de objetos
	 * @param entity
	 * @return
	 */
	public static List<ArtistVO> parse(List<Artist> entities) {
		if (entities == null) {
			return null;
		}
		
		List<ArtistVO> voList = new ArrayList<ArtistVO>(entities.size());
		for (Artist entity : entities) {
			voList.add(parse(entity, false));
		}

		return voList;
	}


    public static Artist parse(ArtistVO vo, boolean isToDigg) {
        if (vo == null) {
            return null;
        }

        Artist entity = new Artist();
        //entity.setAlbums(vo.getAlbums());
        //entity.setArtistAliases(vo.getArtistAliases());
        //entity.setArtistDiscographies(vo.getArtistDiscographies());
        entity.setDtBirth(vo.getDtBirth());
        entity.setDtEnd(vo.getDtEnd());
        entity.setIdArtist(vo.getIdArtist());
        entity.setIdCountry(vo.getIdCountry());
        if (isToDigg) {
	        entity.setImage(ImageAdapter.parse(vo.getImage()));
	        entity.setImageHuge(ImageAdapter.parse(vo.getImageHuge()));
	        entity.setImageTiny(ImageAdapter.parse(vo.getImageTiny()));
	        entity.setAllImages(ImageAdapter.parseVOs(vo.getAllImages()));
        }
        entity.setLetters(vo.getLetters());
        //entity.setMusicArtists(vo.getMusicArtists());
        //entity.setMusics(vo.getMusics());
        entity.setName(vo.getName());
        entity.setSortName(vo.getSortName());
        entity.setTotalAccess(vo.getTotalAccess());
        entity.setType(vo.getType());
        entity.setUri(vo.getUri());
        entity.setUrl(vo.getUrl());
        entity.setFlag_moderate(vo.getFlag_moderate());
        entity.setFlag_public(vo.getFlag_public());
        return entity;
    }

}
