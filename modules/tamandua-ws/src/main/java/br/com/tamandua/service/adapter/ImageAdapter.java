/**
 * 
 */
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
public class ImageAdapter {
    
    public ImageVO findById(Long id) {
        ImageHome imageHome = (ImageHome) AppContext.getApplicationContext().getBean("imageHome");
        Image entity = imageHome.findById(id);
        return parse(entity);
    }
	
    public ImageVO findByUri(String uri) {
        ImageHome imageHome = (ImageHome) AppContext.getApplicationContext().getBean("imageHome");
        Image entity = imageHome.findByUri(uri);
        return parse(entity);
    }

    public List<ImageVO> findByProvider(String provider) {
        ImageHome imageHome = (ImageHome) AppContext.getApplicationContext().getBean("imageHome");
        List<Image> entities = imageHome.findByProvider(provider);
        return parse(entities);
    }

    @Transactional
    public MessagesVO saveImage(ImageVO imageVO) {
        MessagesVO messages = new MessagesVO();
        if (imageVO == null) {
            messages.addError("imageVO cannot be null");
            return messages;
        }

        ImageHome imageHome = (ImageHome) AppContext.getApplicationContext().getBean("imageHome");
        
        Image image = imageHome.findByUri(imageVO.getUri());
        if (image == null) {
            image = parse(imageVO);
            imageHome.persist(image);
            imageHome.flush();
            messages.addAction(new ActionMessageVO(Action.CREATE, new LevelMessageVO(Level.INFO, "image ["+image.getIdImage()+"] created")));
        } else {
            Image parsed = parse(imageVO);
            image.setDescription(parsed.getDescription());
            image.setFile(parsed.getFile());
            image.setHeight(parsed.getHeight());
            image.setWidth(parsed.getWidth());
            image.setLocation(parsed.getLocation());
            image.setProvider(parsed.getProvider());
            image.setUrl(parsed.getUrl());
            imageHome.merge(image);
            imageHome.flush();
            messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "image ["+parsed.getIdImage()+"] updated")));
        }
        
        return messages;
    }
    
    @Transactional
    public MessagesVO editImage(ImageVO imageVO) {
    	MessagesVO messages = new MessagesVO();
        if (imageVO == null) {
            messages.addError("imageVO cannot be null");
            return messages;
        }

        Image image = parse(imageVO);
        ImageHome imageHome = (ImageHome) AppContext.getApplicationContext().getBean("imageHome");
        imageHome.merge(image);
        imageHome.flush();
        
    	messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "image ["+image.getIdImage()+"] updated")));
    	return messages;
    }
    
    @Transactional
    public MessagesVO removeImage(ArtistVO artistVO){
    	
    	MessagesVO messages = new MessagesVO();
    	ImageHome imageHome = (ImageHome) AppContext.getApplicationContext().getBean("imageHome");
    	ArtistHome artistHome = (ArtistHome) AppContext.getApplicationContext().getBean("artistHome");
    	    	
    	Artist artist = artistHome.findById(artistVO.getIdArtist());
		Image image = artist.getImage();
		Image imageHuge = artist.getImageHuge();
		Image imageTiny = artist.getImageTiny();
    	
		for(ImageVO imgVO : artistVO.getAllImages()){
			Long idImage = imgVO.getIdImage();
			
    		if((image != null && idImage.equals(image.getIdImage())) ||
    			(imageHuge != null && idImage.equals(imageHuge.getIdImage())) ||
    			(imageTiny != null && idImage.equals(imageTiny.getIdImage())) ){
    			messages.addError("Essa imagem não pode ser removida. Ela está selecionada como uma das imagens padrões do artista. Escolha outra imagem padrão antes de removê-la.");
    		}else{
    			Image img = imageHome.findById(imgVO.getIdImage());
    			artist.getAllImages().remove(img);
    			artistHome.merge(artist);
    			artistHome.flush();
    			imageHome.remove(img);
    			messages.addAction(new ActionMessageVO(Action.DELETE, new LevelMessageVO(Level.INFO, "image ["+ img.getIdImage() +"] deleted")));
    		}
    	}
    	return messages;
    }

    /* =====================================
     * 
     * PARSER's ENTITY -->> VO
     * 
     */
    public static ImageVO parse(Image entity) {
        if (entity == null) {
            return null;
        }

        ImageVO vo = new ImageVO();
        vo.setDescription(entity.getDescription());
        vo.setFile(entity.getFile());
        vo.setHeight(entity.getHeight());
        vo.setIdImage(entity.getIdImage());
        vo.setLocation(entity.getLocation());
        vo.setUri(entity.getUri());
        vo.setUrl(entity.getUrl());
        vo.setWidth(entity.getWidth());
        vo.setProvider(entity.getProvider());
        return vo;
    }

    public static List<ImageVO> parse(List<Image> entities) {
        if (entities == null) {
            return null;
        }

        List<ImageVO> vos = new ArrayList<ImageVO>(entities.size());
        for (Image entity : entities) {
            vos.add(parse(entity));
        }

        return vos;
    }

    /* =====================================
     * 
     * PARSER's VO -->> ENTITY
     * 
     */
    public static Image parse(ImageVO vo) {
        if (vo == null) {
            return null;
        }

        Image entity = new Image();
        entity.setDescription(vo.getDescription());
        entity.setFile(vo.getFile());
        entity.setHeight(vo.getHeight());
        entity.setIdImage(vo.getIdImage());
        entity.setLocation(vo.getLocation());
        entity.setUri(vo.getUri());
        entity.setUrl(vo.getUrl());
        entity.setWidth(vo.getWidth());
        entity.setProvider(vo.getProvider());
        return entity;
    }

    public static Set<Image> parseVOs(Set<ImageVO> vos) {
        if (vos == null) {
            return null;
        }

        Set<Image> entities = new HashSet<Image>(vos.size());
        for (ImageVO vo : vos) {
            entities.add(parse(vo));
        }

        return entities;
    }

    public static Set<ImageVO> parseEntities(Set<Image> entities) {
        if (entities == null) {
            return null;
        }

        Set<ImageVO> vos = new HashSet<ImageVO>(entities.size());
        for (Image entity : entities) {
            vos.add(parse(entity));
        }

        return vos;
    }

}
