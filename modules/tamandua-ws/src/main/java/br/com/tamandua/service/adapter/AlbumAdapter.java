package br.com.tamandua.service.adapter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.tamandua.persistence.Album;
import br.com.tamandua.persistence.MusicAlbum;
import br.com.tamandua.persistence.MusicAlbumId;
import br.com.tamandua.persistence.app.AppContext;
import br.com.tamandua.persistence.dao.AlbumHome;
import br.com.tamandua.persistence.dao.MusicAlbumHome;
import br.com.tamandua.service.vo.AlbumVO;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.ImageVO;
import br.com.tamandua.service.vo.MusicVO;
import br.com.tamandua.service.vo.validator.ActionMessageVO;
import br.com.tamandua.service.vo.validator.Level;
import br.com.tamandua.service.vo.validator.LevelMessageVO;
import br.com.tamandua.service.vo.validator.MessagesVO;
import br.com.tamandua.service.vo.validator.ActionMessageVO.Action;

/**
 * @author Everton Rosario (erosario@gmail.com)
 */
@Component
public class AlbumAdapter {
    
    @Transactional
    public AlbumVO getAlbumExample() {
        
        AlbumVO vo = new AlbumVO();
        vo.setName("Album Exemplo da Aplicacao");
        vo.setUri("/album-exemplo/");
        ArtistAdapter artistAdapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
        vo.setArtist(artistAdapter.getArtistExample());

        ImageVO imageBackCover = new ImageVO();
        imageBackCover.setUri("/uri/imagem-back");
        vo.setImageByIdBackCover(imageBackCover);
        ImageVO imageCover = new ImageVO();
        imageCover.setUri("/uri/imagem-cover");
        vo.setImageByIdCover(imageCover);
        
        return vo;
    }

	public AlbumVO findAlbumByUri(String uri) {
	    AlbumHome dao = (AlbumHome) AppContext.getApplicationContext().getBean("albumHome");
		return parse(dao.findByUri(uri), true);
	}

    @Transactional
	public MessagesVO saveAlbum(AlbumVO albumVO, MusicVO musicVO) {
        MessagesVO messages = new MessagesVO();
        if (albumVO == null) {
            messages.addError("albumVO cannot be null");
            return messages;
        }

        // Faz a persistencia das imagens do album
        ImageAdapter imageAdapter = (ImageAdapter) AppContext.getApplicationContext().getBean("imageAdapter");
        if (albumVO.getImageByIdCover() != null) {
            ImageVO imageVO = albumVO.getImageByIdCover();
            messages.merge(imageAdapter.saveImage(imageVO));
            ImageVO persistedImageVO = imageAdapter.findByUri(imageVO.getUri());
            albumVO.setImageByIdCover(persistedImageVO);
        }
        if (albumVO.getImageByIdBackCover() != null) {
            ImageVO imageVO = albumVO.getImageByIdBackCover();
            messages.merge(imageAdapter.saveImage(imageVO));
            ImageVO persistedImageVO = imageAdapter.findByUri(imageVO.getUri());
            albumVO.setImageByIdBackCover(persistedImageVO);
        }

        AlbumHome albumHome = (AlbumHome) AppContext.getApplicationContext().getBean("albumHome");
	    
        Album album = albumHome.findByUri(albumVO.getUri());
        if (album == null) {
            album = parse(albumVO, true);
            albumHome.persist(album);
            albumHome.flush();
            messages.addAction(new ActionMessageVO(Action.CREATE, new LevelMessageVO(Level.INFO, "album ["+album.getIdAlbum()+"] created")));
        } else {
            Album parsed = parse(albumVO, true);
            parsed.setIdAlbum(album.getIdAlbum());
            albumHome.merge(parsed);
            albumHome.flush();
            album = parsed;
            messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "album ["+album.getIdAlbum()+"] updated")));
        }
        
        // Liga com a musica
        MusicAlbumHome musicAlbumHome = (MusicAlbumHome) AppContext.getApplicationContext().getBean("musicAlbumHome");
        MusicAlbumId id = new MusicAlbumId(musicVO.getIdMusic(), album.getIdAlbum());
        MusicAlbum found = musicAlbumHome.findById(id);
        if (found == null) {
            MusicAlbum musicAlbum = new MusicAlbum(id, 0);
            musicAlbumHome.persist(musicAlbum);
            musicAlbumHome.flush();
            messages.addAction(new ActionMessageVO(Action.CREATE, new LevelMessageVO(Level.INFO, "musicAlbum ["+musicAlbum.getId()+"] created")));
        }
        
        return messages;
	}

    /* =====================================
     * 
     * PARSER's ENTITY -->> VO
     * 
     */
    /**
	 * Realiza a transofrmacao de Entity para VO
	 * @param entity
	 * @return
	 */
	public static AlbumVO parse(Album entity, boolean isToDigg) {
		if (entity == null) {
			return null;
		}

		AlbumVO vo = new AlbumVO();
		vo.setIdAlbum(entity.getIdAlbum());
		vo.setIdCountry(entity.getIdCountry());
		vo.setName(entity.getName());
		vo.setUri(entity.getUri());
		vo.setUrl(entity.getUrl());
		vo.setYear(entity.getYear());

		if (isToDigg) {
		    vo.setArtist(ArtistAdapter.parse(entity.getArtist(), false));
		    vo.setImageByIdBackCover(ImageAdapter.parse(entity.getImageByIdBackCover()));
		    vo.setImageByIdCover(ImageAdapter.parse(entity.getImageByIdCover()));
		    //vo.setArtistDiscographies(artistDiscographies)
		}
		return vo;
	}
	
	/**
	 * Realiza a transofrmacao de Entity para VO de uma colecao de objetos
	 * @param entity
	 * @return
	 */
	public static List<AlbumVO> parse(List<Album> entities) {
		if (entities == null) {
			return null;
		}
		
		List<AlbumVO> voList = new ArrayList<AlbumVO>(entities.size());
		for (Album entity : entities) {
			voList.add(parse(entity, true));
		}

		return voList;
	}

    /* =====================================
     * 
     * PARSER's VO -->> ENTITY
     * 
     */
    public static Album parse(AlbumVO vo, boolean isToDigg) {
        if (vo == null) {
            return null;
        }

        Album entity = new Album();
        entity.setIdAlbum(vo.getIdAlbum());
        entity.setIdCountry(vo.getIdCountry());
        entity.setName(vo.getName());
        entity.setUri(vo.getUri());
        entity.setUrl(vo.getUrl());
        entity.setYear(vo.getYear());

        if (isToDigg) {
            entity.setArtist(ArtistAdapter.parse(vo.getArtist(), false));
            entity.setImageByIdBackCover(ImageAdapter.parse(vo.getImageByIdBackCover()));
            entity.setImageByIdCover(ImageAdapter.parse(vo.getImageByIdCover()));
            //vo.setArtistDiscographies(artistDiscographies)
        }
        return entity;

    }
}
