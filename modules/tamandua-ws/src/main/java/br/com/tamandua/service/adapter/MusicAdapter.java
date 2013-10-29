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
import br.com.tamandua.persistence.EntityDescriptor;
import br.com.tamandua.persistence.Lyric;
import br.com.tamandua.persistence.Music;
import br.com.tamandua.persistence.MusicArtist;
import br.com.tamandua.persistence.MusicArtistId;
import br.com.tamandua.persistence.Tag;
import br.com.tamandua.persistence.app.AppContext;
import br.com.tamandua.persistence.dao.ArtistHome;
import br.com.tamandua.persistence.dao.LyricHome;
import br.com.tamandua.persistence.dao.MusicArtistHome;
import br.com.tamandua.persistence.dao.MusicHome;
import br.com.tamandua.persistence.dao.TagHome;
import br.com.tamandua.service.ArtistService;
import br.com.tamandua.service.util.StringNormalizer;
import br.com.tamandua.service.vo.AlbumVO;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.ImageVO;
import br.com.tamandua.service.vo.LyricVO;
import br.com.tamandua.service.vo.MusicArtistVO;
import br.com.tamandua.service.vo.MusicVO;
import br.com.tamandua.service.vo.TagVO;
import br.com.tamandua.service.vo.validator.ActionMessageVO;
import br.com.tamandua.service.vo.validator.Level;
import br.com.tamandua.service.vo.validator.LevelMessageVO;
import br.com.tamandua.service.vo.validator.MessagesVO;
import br.com.tamandua.service.vo.validator.ActionMessageVO.Action;

/**
 * @author Everton Rosario (erosario@gmail.com)
 */
@Component
public class MusicAdapter {

	/**
	 * @return Uma musica de exemplo para o servico
	 */
	public MusicVO getMusicExample() {
		ArtistAdapter artistAdapter = (ArtistAdapter) AppContext.getApplicationContext().getBean("artistAdapter");
		ArtistVO artistVO = artistAdapter.getArtistExample();
		
		// Trata Faixa + Letras
        MusicVO musicVO = new MusicVO();
        LyricVO lyric = new LyricVO();
        LyricVO lyric2 = new LyricVO();
        HashSet<LyricVO> lyrics = new HashSet<LyricVO>();
        lyrics.add(lyric);
        lyrics.add(lyric2);
        musicVO.setLyrics(lyrics);

        musicVO.setArtist(artistVO);
        musicVO.setTitle("Music Title");
    	musicVO.setUri("/kacamba/saia-e-bicicletinha/");
    	musicVO.setFlag_moderate(MusicVO.FLAG_MODERATE);
    	musicVO.setFlag_public(MusicVO.FLAG_PUBLIC);
    	lyric.setLyricType(LyricVO.LYRIC_TYPE_ORIGINAL);
    	lyric2.setLyricType(LyricVO.LYRIC_TYPE_ORIGINAL);

    	lyric.setMusic(musicVO);
        lyric.setUri(musicVO.getUri());
        lyric.setText("Bota a saia e vem pra rua com sua bicicletinha\nEu quero ver a cor da sua calcinha\nEu disse ela ta de saia, de bicicletinha\numa mao vai no guidao\na outra tapando a calcinha");
        lyric.setLyricTitle("Saia e Bicicletinha");
        lyric.setTotalAccess(0L);
        lyric.setProvider("provider1");
        
        lyric2.setMusic(musicVO);
        lyric2.setUri(musicVO.getUri());
        lyric2.setText("Bota a saia e vem pra rua com sua bicicletinha\nEu quero ver a cor da sua calcinha\nEu disse ela ta de saia, de bicicletinha\numa mao vai no guidao\na outra tapando a calcinha");
        lyric2.setLyricTitle("Saia e Bicicletinha");
        lyric2.setTotalAccess(0L);
        lyric2.setProvider("provider2");

        // Trata Album
        AlbumVO albumVO = new AlbumVO();
        musicVO.setAlbum(albumVO);
        albumVO.setArtist(artistVO);
        albumVO.setName("Bahia 2010");
        albumVO.setUri("/album/bahia-2010");
        ImageVO imageCover = new ImageVO();
        imageCover.setUri("/album/bahia-2010/cover.jpg");
		albumVO.setImageByIdCover(imageCover);
		
		return musicVO;
	}

	public MessagesVO saveMusic(MusicVO musicVO) {
		MessagesVO messages = new MessagesVO();
        if (musicVO == null || musicVO.getLyrics() == null || musicVO.getLyrics().isEmpty()) {
            messages.addError("musicVO and musicVO.getLyric() cannot be null nor empty!");
            return messages;
        }

		MusicHome musicHome = (MusicHome) AppContext.getApplicationContext().getBean("musicHome");
		LyricHome lyricHome = (LyricHome) AppContext.getApplicationContext().getBean("lyricHome");
		ArtistHome artistHome = (ArtistHome) AppContext.getApplicationContext().getBean("artistHome");
	    
        Music music = musicHome.findByUri(musicVO.getUri());
        if (music == null) {
        	// Carrega o vo recebido
            music = parse(musicVO, true);

            // Faz o tratamento dos artistas ja existentes (descobre o ID)
            Artist artist = artistHome.findById(musicVO.getArtist().getIdArtist());
            if (artist == null) {
                messages.addError("artist not persisted! Persist before calling saveMusic!");
                return messages;
            }
			music.setArtist(artist);
			music.setFlag_moderate(MusicVO.FLAG_NO_MODERATE);
			music.setFlag_public(MusicVO.FLAG_NO_PUBLIC);

			for (Lyric lyric : music.getLyrics()) {
			    lyric.setMusic(music);
			    //lyricHome.persist(lyric);
			}
			musicHome.persist(music);

			musicHome.flush();
			messages.addAction(new ActionMessageVO(Action.CREATE, new LevelMessageVO(Level.INFO, "music ["+music.getIdMusic()+"] created")));

        } else {
            
        	Music parsed = parse(musicVO, true);

        	// Faz o tratamento dos artistas ja existentes (descobre o ID)
            Artist artist = artistHome.findByUri(musicVO.getArtist().getUri());
            if (artist == null) {
                messages.addError("artist not persisted! Persist before calling saveMusic!");
                return messages;
            }
            music.setArtist(artist);
            
            music.setDtRelease(parsed.getDtRelease());
            music.setIdCountry(parsed.getIdCountry());
            music.setTitle(parsed.getTitle());
            music.setFlag_moderate(MusicVO.FLAG_NO_MODERATE);
			music.setFlag_public(MusicVO.FLAG_NO_PUBLIC);
            // Trata a letra da musica
            if (parsed.getLyrics() != null && !parsed.getLyrics().isEmpty()) {
                for (Lyric lyric : parsed.getLyrics()) {
                    Lyric lyricFound = lyricHome.findByUri(lyric.getUri(), lyric.getProvider());
                    if (lyricFound != null) {
                        // TODO lyric.setTotalAccess(lyricFound.getTotalAccess());
                        lyricFound.setLaguage(lyric.getLaguage());
                        lyricFound.setLyricTitle(lyric.getLyricTitle());
                        lyricFound.setMusic(lyric.getMusic());
                        lyricFound.setLyricType(lyric.getLyricType());
                        lyricFound.setProvider(lyric.getProvider());
                        lyricFound.setText(lyric.getText());
                        lyricFound.setUri(lyric.getUri());
                        lyricFound.setUrl(lyric.getUrl());
                        lyricFound.setMusic(music);
                        music.getLyrics().remove(lyric);
                        music.getLyrics().add(lyricFound);
                    } else {
                        lyric.setMusic(music);
                        music.addLyric(lyric);
                    }
                }
            }
            
            musicHome.merge(music);
			//lyricHome.merge(music.getLyric());
            musicHome.flush();
            messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "music ["+music.getIdMusic()+"] updated")));
            
        }
        music = musicHome.findByUri(musicVO.getUri());
        musicVO.setIdMusic(music.getIdMusic());
        return messages;
	}
	
	public MessagesVO saveMusicDetailed(MusicVO musicVO){
		MessagesVO messages = new MessagesVO();
		
		MusicHome musicHome = (MusicHome) AppContext.getApplicationContext().getBean("musicHome");
		LyricHome lyricHome = (LyricHome) AppContext.getApplicationContext().getBean("lyricHome");
		
		Music music = musicHome.findByUri(musicVO.getUri());
        if (music == null) {
			// Carrega o vo recebido
        	ArtistService service = (ArtistService) AppContext.getApplicationContext().getBean("artistService");
            ArtistVO artistVO = service.findArtistByUri(musicVO.getArtist().getUri());
            
            music = parse(musicVO, true);
            music.getArtist().setIdArtist(artistVO.getIdArtist());
            music.setUri(artistVO.getUri() + StringNormalizer.normalizeArtistName(music.getTitle()));
            music.setUrl(music.getUri()+".html");
            music.setFlag_moderate(MusicVO.FLAG_NO_MODERATE);
			music.setFlag_public(MusicVO.FLAG_NO_PUBLIC);

            if (music.getLyrics() != null && !music.getLyrics().isEmpty()) {
            	for (Lyric lyric : music.getLyrics()) {
                    Lyric lyricFound = lyricHome.findByUri(lyric.getUri(), lyric.getProvider());
                    if (lyricFound != null) {
                        // TODO lyric.setTotalAccess(lyricFound.getTotalAccess());
                        lyricFound.setLaguage(lyric.getLaguage());
                        lyricFound.setLyricTitle(lyric.getLyricTitle());
                        lyricFound.setMusic(lyric.getMusic());
                        lyricFound.setLyricType(lyric.getLyricType());
                        lyricFound.setProvider(lyric.getProvider());
                        lyricFound.setText(lyric.getText());
                        lyricFound.setUri(lyric.getUri());
                        lyricFound.setUrl(lyric.getUrl());
                        lyricFound.setMusic(music);
                        lyricFound.setFlag(lyric.getFlag());
                        music.getLyrics().remove(lyric);
                        music.getLyrics().add(lyricFound);
                    } else {
                   		lyric.setUri(artistVO.getUri() + StringNormalizer.normalizeArtistName(lyric.getLyricTitle()));
                       	lyric.setUrl(lyric.getUri()+".html");
                    	lyric.setMusic(music);
                        music.addLyric(lyric);
                    }
                }
            }
            
            musicHome.persist(music);
			musicHome.flush();
			messages.addAction(new ActionMessageVO(Action.CREATE, new LevelMessageVO(Level.INFO, "music ["+music.getIdMusic()+"] created")));
        } else {
        	Music parsed = parse(musicVO, true);
        	music.setTitle(parsed.getTitle());
        	music.setDtRelease(parsed.getDtRelease());
            music.setIdCountry(parsed.getIdCountry());
            
            for (Lyric lyric : music.getLyrics()) {
                if(lyric.getIdLyric().equals(musicVO.getIdLyricOriginal())){
                	lyric.setFlag(LyricVO.FLAG_DEFAULT);
                }else{
                	lyric.setFlag(LyricVO.FLAG_NO_DEFAULT);
                }
            }
            
            music.setFlag_moderate(MusicVO.FLAG_NO_MODERATE);
			music.setFlag_public(MusicVO.FLAG_NO_PUBLIC);
            musicHome.merge(music);
            musicHome.flush();
            messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "music ["+music.getIdMusic()+"] updated")));
            
        }
		
		return messages;
	}
	
	public MessagesVO saveMusicLyric(MusicVO musicVO){
		MessagesVO messages = new MessagesVO();
		
		LyricHome lyricHome = (LyricHome) AppContext.getApplicationContext().getBean("lyricHome");
		MusicHome musicHome = (MusicHome) AppContext.getApplicationContext().getBean("musicHome");

		Music music = musicHome.findByUri(musicVO.getUri());
    	Music parsed = parse(musicVO, true);
    	if (parsed.getLyrics() != null && !parsed.getLyrics().isEmpty()) {
    		for (Lyric lyric : parsed.getLyrics()) {
                Lyric lyricFound = lyricHome.findByUri(lyric.getUri(), lyric.getProvider());
                if (lyricFound != null) {
                    // TODO lyric.setTotalAccess(lyricFound.getTotalAccess());
                    lyricFound.setLaguage(lyric.getLaguage());
                    lyricFound.setLyricTitle(lyric.getLyricTitle());
                    lyricFound.setMusic(lyric.getMusic());
                    lyricFound.setLyricType(lyric.getLyricType());
                    lyricFound.setProvider(lyric.getProvider());
                    lyricFound.setText(lyric.getText());
                    lyricFound.setUri(lyric.getUri());
                    lyricFound.setUrl(lyric.getUrl());
                    
                    music.setFlag_moderate(MusicVO.FLAG_NO_MODERATE);
                    music.setFlag_public(MusicVO.FLAG_NO_PUBLIC);
                    
                    lyricFound.setMusic(music);
                    music.getLyrics().remove(lyric);
                    music.getLyrics().add(lyricFound);
                } else {
                    lyric.setMusic(music);
                    music.addLyric(lyric);
                }
            }
        }
		return messages;
	}

    public MusicVO findById(Long id) {
        MusicHome dao = (MusicHome) AppContext.getApplicationContext().getBean("musicHome");
        return parse(dao.findById(id), true);
    }

	public MusicVO findByUri(String uri) {
		MusicHome dao = (MusicHome) AppContext.getApplicationContext().getBean("musicHome");
		return parse(dao.findByUri(uri), true);
	}

	public List<MusicVO> findByArtistId(Long artistId) {
        MusicHome dao = (MusicHome) AppContext.getApplicationContext().getBean("musicHome");
        return parse(dao.findByArtistId(artistId));
    }

    public MessagesVO updateMusicArtists(MusicVO musicVO, Set<MusicArtistVO> musicArtistsVO) {
        ArtistHome artistHome = (ArtistHome) AppContext.getApplicationContext().getBean("artistHome");
        MusicHome musicHome = (MusicHome) AppContext.getApplicationContext().getBean("musicHome");
        MusicArtistHome musicArtistHome = (MusicArtistHome) AppContext.getApplicationContext().getBean("musicArtistHome");
        // Verifica as mensagens a serem retornadas sobre as operacoes efetuadas
        MessagesVO messages = new MessagesVO();

        // Encontra a musica informada (DEVE ja existir)
        if (musicVO.getIdMusic() == null) {
            messages.addError("music with empty id ["+musicVO+"]!");
            return messages;
        }
        Music musicEntity = musicHome.findById(musicVO.getIdMusic()); 

        if (musicEntity == null) {
            messages.addError("music from id["+musicVO.getIdMusic()+"] doesnt exist!");
            return messages;
        }

        // Encontra os existentes e remove-os
//        List<MusicArtist> musicArtistsBefore = musicArtistHome.findByMusicId(musicEntity.getIdMusic());
//        if (musicArtistsBefore != null && musicArtistsBefore.size() > 0) {
//            messages.addAction(
//                    new ActionMessageVO(Action.DELETE, 
//                                        new LevelMessageVO(Level.INFO, 
//                                                           "removed [" + musicArtistsBefore.size()+ "] " +
//                                                           "MusicArtist from music [" + musicVO.getIdMusic() + "]")));
//            for (MusicArtist musicArtist : musicArtistsBefore) {
//                musicArtistHome.remove(musicArtist);
//            }
//            musicArtistHome.flush();
//        }

        Set<MusicArtist> musicArtistsEntities = musicEntity.getMusicArtists();
        int added = 0;
        
        // Trata os "participantes" da musica (compositor etc)
        Set<MusicArtist> musicArtists = parseVOs(musicArtistsVO);
        if (musicArtists != null && !musicArtists.isEmpty()) {
            for (MusicArtist musicArtist : musicArtists) {
                // Faz o tratamento dos artistas ja existentes (descobre o ID)
                Artist participant = artistHome.findByName(musicArtist.getArtist().getName());
                // Caso nao exista o "artista" cria um.
                if (participant == null) {
                    participant = musicArtist.getArtist();
                    artistHome.persist(participant);
                    artistHome.flush();
                }
                musicArtist.setArtist(participant);
                musicArtist.setMusic(musicEntity);

                MusicArtistId id = new MusicArtistId();
                id.setIdArtist(participant.getIdArtist());
                id.setIdMusic(musicEntity.getIdMusic());
                musicArtist.setId(id);
                
                if (!musicArtistsEntities.contains(musicArtist)) {
                    musicArtistsEntities.add(musicArtist);
                    musicArtistHome.persist(musicArtist);
                    musicArtistHome.flush();
                    added++;
                }
            }
            messages.addAction(
                    new ActionMessageVO(Action.CREATE, 
                                        new LevelMessageVO(Level.INFO, 
                                                           "added [" + added + "] " +
                                                           "MusicArtist to music [" + musicVO.getIdMusic() + "]")));
        }
        return messages;
    }

    public MessagesVO updateMusicTags(MusicVO musicVO, List<TagVO> tagsVO) {
        MusicHome musicHome = (MusicHome) AppContext.getApplicationContext().getBean("musicHome");
        TagHome tagHome = (TagHome) AppContext.getApplicationContext().getBean("tagHome");

        // Encontra a musica informada (DEVE ja existir)
        Music musicEntity = musicHome.findById(musicVO.getIdMusic()); 

        // Verifica as mensagens a serem retornadas sobre as operacoes efetuadas
        MessagesVO messages = new MessagesVO();
        if (musicEntity == null) {
            messages.addError("music from id["+musicVO.getIdMusic()+"] doesnt exist!");
            return messages;
        }

        // Encontra os existentes e remove-os
        List<Tag> tagsBefore = tagHome.findByEntity(EntityDescriptor.ENTITY_MUSIC.getIdEntityDescriptor(), musicEntity.getIdMusic(), Tag.TYPE_STYLE);
//        if (tagsBefore != null && tagsBefore.size() > 0) {
//            messages.addAction(
//                    new ActionMessageVO(Action.DELETE, 
//                                        new LevelMessageVO(Level.INFO, 
//                                                           "removed [" + tagsBefore.size()+ "] " +
//                                                           "Tag from music [" + musicVO.getIdMusic() + "]")));
//            for (Tag tag : tagsBefore) {
//                tagHome.remove(tag);
//            }
//            tagHome.flush();
//        }
        
        // Insere as tags selecionadas para a faixa
        List<Tag> parsedTags = TagAdapter.parseVOs(tagsVO);
        if (parsedTags != null && parsedTags.size() > 0) {
            for (Tag tag : parsedTags) {
                tag.setEntityDescriptor(EntityDescriptor.ENTITY_MUSIC);
                tag.setIdEntity(musicEntity.getIdMusic());
                
                if (tagsBefore != null && !tagsBefore.contains(tag)) {
                    tagHome.persist(tag);
                    tagHome.flush();
                }
            }
            messages.addAction(
            new ActionMessageVO(Action.CREATE, 
                                new LevelMessageVO(Level.INFO, 
                                                   "added [" + parsedTags.size()+ "] " +
                                                   "Tag to music [" + musicEntity.getIdMusic() + "]")));
        }
        
        return messages;
    }

    public MessagesVO updateMusicFreeTags(MusicVO musicVO, List<TagVO> tagsVO) {
        MusicHome musicHome = (MusicHome) AppContext.getApplicationContext().getBean("musicHome");
        TagHome tagHome = (TagHome) AppContext.getApplicationContext().getBean("tagHome");

        // Encontra a musica informada (DEVE ja existir)
        Music musicEntity = musicHome.findById(musicVO.getIdMusic()); 

        // Verifica as mensagens a serem retornadas sobre as operacoes efetuadas
        MessagesVO messages = new MessagesVO();
        if (musicEntity == null) {
            messages.addError("music from id["+musicVO.getIdMusic()+"] doesnt exist!");
            return messages;
        }

        // Encontra os existentes e remove-os
        List<Tag> tagsBefore = tagHome.findByEntity(EntityDescriptor.ENTITY_MUSIC.getIdEntityDescriptor(), musicEntity.getIdMusic(), Tag.TYPE_FREESTYLE);
        
        // Insere as tags selecionadas para a faixa
        List<Tag> parsedTags = TagAdapter.parseVOs(tagsVO);
        if (parsedTags != null && parsedTags.size() > 0) {
            for (Tag tag : parsedTags) {
                tag.setEntityDescriptor(EntityDescriptor.ENTITY_MUSIC);
                tag.setIdEntity(musicEntity.getIdMusic());
                
                if (tagsBefore != null && !tagsBefore.contains(tag)) {
                    tagHome.persist(tag);
                    tagHome.flush();
                }
            }
            messages.addAction(
            new ActionMessageVO(Action.CREATE, 
                                new LevelMessageVO(Level.INFO, 
                                                   "added [" + parsedTags.size()+ "] " +
                                                   "Tag to music [" + musicEntity.getIdMusic() + "]")));
        }
        
        return messages;
    }

    /* =====================================
	 * 
	 * PARSER's VO -->> ENTITY
	 * 
	 */
	public static Music parse(MusicVO vo, boolean isToDigg) {
		if (vo == null) {
			return null;
		}
		Music entity = new Music();
		// Campos que so sao parseados caso deva "cavar". Evita recursao infinita
		if (isToDigg) {
			entity.setArtist(ArtistAdapter.parse(vo.getArtist(), false));
			entity.setLyrics(parseLyricVOs(vo.getLyrics()));
			entity.setMusicArtists(parseVOs(vo.getMusicArtists()));
		}
		entity.setIdMusic(vo.getIdMusic());
		entity.setDtRelease(vo.getDtRelease());
		entity.setIdCountry(vo.getIdCountry());
		entity.setTitle(vo.getTitle());
		entity.setUri(vo.getUri());
		entity.setUrl(vo.getUrl());
		entity.setFlag_moderate(vo.getFlag_moderate());
		entity.setFlag_public(vo.getFlag_public());
		return entity;
	}

	private static Set<Lyric> parseLyricVOs(Set<LyricVO> vos) {
        if (vos == null) {
            return null;
        }
        
        Set<Lyric> entities = new HashSet<Lyric>();
        for (LyricVO lyricVO : vos) {
            entities.add(parse(lyricVO));
        }
        return entities;
    }

    public static Set<MusicArtist> parseVOs(
			Set<MusicArtistVO> vos) {
		if (vos == null) {
			return null;
		}
		
		Set<MusicArtist> entities = new HashSet<MusicArtist>();
		for (MusicArtistVO musicArtistVO : vos) {
			entities.add(parse(musicArtistVO));
		}
		return entities;
	}

	public static MusicArtist parse(MusicArtistVO vo) {
		if (vo == null) {
			return null;
		}
		
		MusicArtist entity = new MusicArtist();
		MusicArtistId id = new MusicArtistId();
		entity.setId(id);
		entity.setArtist(ArtistAdapter.parse(vo.getArtist(), false));
		entity.setMusic(parse(vo.getMusic(), false));
		entity.setParticipation(vo.getParticipation());
		return entity;
	}

	public static Lyric parse(LyricVO vo) {
		if (vo == null) {
			return null;
		}

		Lyric entity = new Lyric();
		entity.setIdLyric(vo.getIdLyric());
		entity.setLaguage(vo.getLaguage());
		entity.setLyricTitle(vo.getLyricTitle());
		entity.setLyricType(vo.getLyricType());
		entity.setMusic(parse(vo.getMusic(), false));
		entity.setProvider(vo.getProvider());
		entity.setText(vo.getText());
		entity.setTotalAccess(vo.getTotalAccess());
		entity.setUri(vo.getUri());
		entity.setUrl(vo.getUrl());
		entity.setFlag(vo.getFlag());
		return entity;
	}	
	/* =====================================
	 * 
	 * PARSER's ENTITY -->> VO
	 * 
	 */
    private static List<MusicVO> parse(List<Music> entities) {
        if (entities == null) {
            return null;
        }
        
        List<MusicVO> vos = new ArrayList<MusicVO>();
        for (Music entity : entities) {
            vos.add(parse(entity, true));
        }
        return vos;
    }

    public static MusicVO parse(Music entity, boolean isToDigg) {
		if (entity == null) {
			return null;
		}
		MusicVO vo = new MusicVO();
		// Campos que so sao parseados caso deva "cavar". Evita recursao infinita
		if (isToDigg) {
			vo.setArtist(ArtistAdapter.parse(entity.getArtist(), false));
			vo.setLyrics(parseLyrics(entity.getLyrics()));
			vo.setMusicArtists(parse(entity.getMusicArtists()));
		}
		vo.setIdMusic(entity.getIdMusic());
		vo.setDtRelease(entity.getDtRelease());
		vo.setIdCountry(entity.getIdCountry());
		vo.setTitle(entity.getTitle());
		vo.setUri(entity.getUri());
		vo.setUrl(entity.getUrl());
		vo.setFlag_moderate(entity.getFlag_moderate());
		vo.setFlag_public(entity.getFlag_public());
		return vo;
	}

	private static Set<LyricVO> parseLyrics(Set<Lyric> entities) {
        if (entities == null) {
            return null;
        }
        
        Set<LyricVO> vos = new HashSet<LyricVO>();
        for (Lyric entity : entities) {
            vos.add(parse(entity));
        }
        return vos;
    }

    public static Set<MusicArtistVO> parse(
			Set<MusicArtist> entities) {
		if (entities == null) {
			return null;
		}
		
		Set<MusicArtistVO> vos = new HashSet<MusicArtistVO>();
		for (MusicArtist musicArtist : entities) {
			vos.add(parse(musicArtist));
		}
		return vos;
	}

	public static MusicArtistVO parse(MusicArtist entity) {
		if (entity == null) {
			return null;
		}
		
		MusicArtistVO vo = new MusicArtistVO();
		vo.setArtist(ArtistAdapter.parse(entity.getArtist(), false));
		vo.setMusic(parse(entity.getMusic(), false));
		vo.setParticipation(entity.getParticipation());
		return vo;
	}

	public static LyricVO parse(Lyric entity) {
		if (entity == null) {
			return null;
		}

		LyricVO vo = new LyricVO();
		vo.setIdLyric(entity.getIdLyric());
		vo.setLaguage(entity.getLaguage());
		vo.setLyricTitle(entity.getLyricTitle());
		vo.setLyricType(entity.getLyricType());
		vo.setMusic(parse(entity.getMusic(), false));
		vo.setProvider(entity.getProvider());
		vo.setText(entity.getText());
		vo.setTotalAccess(entity.getTotalAccess());
		vo.setUri(entity.getUri());
		vo.setUrl(entity.getUrl());
		vo.setFlag(entity.getFlag());
		return vo;
	}
    
    @Transactional
	public MessagesVO moderateById(Boolean moderate, Long id) {
    	MessagesVO messages = new MessagesVO();
    	MusicHome musicHome = (MusicHome) AppContext.getApplicationContext().getBean("musicHome");
    	Music music = musicHome.findById(id);
    	
    	if(moderate){
    		music.setFlag_moderate(MusicVO.FLAG_MODERATE);
    	}else{
    		music.setFlag_moderate(MusicVO.FLAG_NO_MODERATE);
    	}    	    	
    	musicHome.merge(music);
    	musicHome.flush();
        
        messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "music ["+music.getIdMusic()+"] moderated")));
    	return messages;    	
    }
    
    @Transactional
	public MessagesVO moderateAllByArtist(Boolean moderate, Long artistId) {
    	MessagesVO messages = new MessagesVO();    	
    	MusicHome musicHome = (MusicHome) AppContext.getApplicationContext().getBean("musicHome");
    	musicHome.moderateAllByArtist(moderate, artistId);    	    	
    	musicHome.flush();
        
        messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "musics from artist ["+ artistId +"] moderated")));
    	return messages;
    	
    }
    
    @Transactional
	public MessagesVO moderateAllByIds(Boolean moderate, List<Long> ids){
    	MessagesVO messages = new MessagesVO();

    	MusicHome musicHome = (MusicHome) AppContext.getApplicationContext().getBean("musicHome");
    	musicHome.moderateAllByIds(moderate, ids);    	
    	musicHome.flush();
    	
        messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "musics ["+ids+"] moderated")));
    	return messages;    	
    }

    @Transactional
	public MessagesVO publishById(Boolean publish, Long id) {
    	MessagesVO messages = new MessagesVO();
    	MusicHome musicHome = (MusicHome) AppContext.getApplicationContext().getBean("musicHome");
    	Music music = musicHome.findById(id);
    	
    	if(publish){
    		music.setFlag_public(MusicVO.FLAG_PUBLIC);
    	}else{
    		music.setFlag_public(MusicVO.FLAG_NO_PUBLIC);
    	}    	    	
    	musicHome.merge(music);
    	musicHome.flush();
        
        messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "music ["+music.getIdMusic()+"] updated")));
    	return messages;
    }
    
    @Transactional
	public MessagesVO publishAllByArtist(Boolean publish, Long artistId) {
    	MessagesVO messages = new MessagesVO();    	
    	MusicHome musicHome = (MusicHome) AppContext.getApplicationContext().getBean("musicHome");
    	musicHome.publishAllByArtist(publish, artistId);    	    	
    	musicHome.flush();
        
        messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "musics from artist ["+ artistId +"] updated")));
    	return messages;
    }

    @Transactional
	public MessagesVO publishAllByIds(Boolean publish, List<Long> ids){
    	MessagesVO messages = new MessagesVO();

    	MusicHome musicHome = (MusicHome) AppContext.getApplicationContext().getBean("musicHome");    	
    	musicHome.publishAllByIds(publish, ids);    	
    	musicHome.flush();
    	
        messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "musics ["+ids+"] updated")));
    	return messages;    	
    }
}
