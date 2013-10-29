package br.com.tamandua.generator.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.tamandua.service.proxy.ArtistProxy;
import br.com.tamandua.service.proxy.MusicProxy;
import br.com.tamandua.service.proxy.ProxyHelper;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.MusicVO;

public class ServiceProxy {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProxy.class);
	
	private static ServiceProxy instance;
	
	private ArtistProxy artistProxy;
	private MusicProxy musicProxy;
	
	public static ServiceProxy getInstance(){
		if(instance == null){
			instance = new ServiceProxy();
		}
		return instance;
	}
	
	private ServiceProxy(){
		final String WEB_SERVICE_HOST = GeneratorProperties.getInstance().getWebServiceHost();
		this.artistProxy = ProxyHelper.getArtistProxy(WEB_SERVICE_HOST);
		this.musicProxy = ProxyHelper.getMusicProxy(WEB_SERVICE_HOST);
	}
	
	public List<ArtistVO> findArtistsWithLyricsPublishedByLetter(String letter){
	    LOGGER.debug("Buscando artistas com letras publicadas que comecam com a letra '" + letter + "'.");
	    List<ArtistVO> artists = artistProxy.findArtistsWithLyricsPublishedByLetter(letter);
	    List<ArtistVO> publishedArtists = new ArrayList<ArtistVO>(artists.size());
	    for (ArtistVO artistVO : artists) {
            if (ArtistVO.FLAG_PUBLIC.equals(artistVO.getFlag_public())) {
                publishedArtists.add(artistVO);
            }
        }
	    LOGGER.debug("Foram encontrados '" + artists.size() + "' artistas com musicas que comecam com a letra '" + letter + "' e destes '"+publishedArtists.size()+"' estao publicados.");
	    return publishedArtists;
	}
	
	public ArtistVO findArtistById(Long id){
		LOGGER.debug("Buscando o artista '" + id + "'.");
		ArtistVO artistVO = artistProxy.findArtistById(id);
		if(artistVO == null){
			System.err.println("O artista '" + id + "' nao foi encontrado!");
        } else if(ArtistVO.FLAG_NO_PUBLIC.equals(artistVO.getFlag_public())){
            System.err.println("O artista '" + id + "' nao esta publicado!");
            artistVO = null;
        }
		return artistVO;
	}
	
	public ArtistVO findArtistByURI(String artistUri){
		LOGGER.debug("Buscando o artista '" + artistUri + "'.");
		ArtistVO artistVO = artistProxy.findArtistByURI("/" + artistUri + "/");
		if(artistVO == null){
			System.err.println("O artista '" + artistUri + "' nao foi encontrado!");
        } else if(ArtistVO.FLAG_NO_PUBLIC.equals(artistVO.getFlag_public())){
            System.err.println("O artista '" + artistUri + "' nao esta publicado!");
            artistVO = null;
        }
		return artistVO;
	}
	
	public MusicVO findMusicById(Long id){
		LOGGER.debug("Buscando a musica '" + id + "'.");
		MusicVO musicVO = musicProxy.findMusicId(id);
		if(musicVO == null){
			System.err.println("A musica '" + id + "' nao foi encontrada!");
        } else if(MusicVO.FLAG_NO_PUBLIC.equals(musicVO.getFlag_public())){
            System.err.println("A musica '" + id + "' nao esta publicada!");
            musicVO = null;
        }
		return musicVO;
	}
	
	public List<MusicVO> findMusicsByArtist(ArtistVO artistVO){
    	LOGGER.debug("Buscando as musicas do artista '" + artistVO.getSortName() + "'.");
    	List<MusicVO> musics = musicProxy.findMusicsByArtistId(artistVO.getIdArtist());
    	List<MusicVO> publishedMusics = new ArrayList<MusicVO>(musics.size());
        for (MusicVO musicVO : musics) {
            if (MusicVO.FLAG_PUBLIC.equals(musicVO.getFlag_public()) && musicVO.hasModeratedLyric()) {
                publishedMusics.add(musicVO);
            }
        }
    	LOGGER.debug("Foram encontradas '" + musics.size() + "' musicas do artista '" + artistVO.getSortName() + "' e destas '"+publishedMusics.size()+"' estao publicadas.");
    	return publishedMusics;
	}
	
	public MusicVO findMusicByURI(String artistUri, String musicUri){
		LOGGER.debug("Buscando a musica '" + musicUri + "' do artista '" + artistUri + "'.");
		MusicVO musicVO = musicProxy.findMusicByURI("/" + artistUri + "/" + musicUri);
        if(musicVO == null){        
        	LOGGER.error("A musica '" + musicUri + "' do artista '" + artistUri + "' nao foi encontrada!");
        } else if(MusicVO.FLAG_NO_PUBLIC.equals(musicVO.getFlag_public())){
            System.err.println("A musica '" + musicUri + "' nao esta publicada!");
            musicVO = null;
        }
        return musicVO;
	}
}
