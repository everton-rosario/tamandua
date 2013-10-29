package br.com.tamandua.generator.threads;

import java.util.List;

import br.com.tamandua.generator.utils.PageGenerator;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.MusicVO;

public class ArtistPageGenerator implements Runnable  {
	
	private ArtistVO artistVO;
	private List<ArtistVO> artists;
	private List<MusicVO> musics;
	private boolean upload;
	
	public ArtistPageGenerator(ArtistVO artistVO, List<ArtistVO> artists, List<MusicVO> musics, boolean upload){
		this.artistVO = artistVO;
		this.artists = artists;
		this.musics = musics;
		this.upload = upload;
	}

	public void run() {
    	PageGenerator.generateArtistPage(artistVO, artists, musics, upload);
	}

}
