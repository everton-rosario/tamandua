package br.com.tamandua.generator.threads;

import java.util.List;

import br.com.tamandua.generator.utils.PageGenerator;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.MusicVO;

public class MusicPageGenerator implements Runnable  {

	private MusicVO musicVO;
	private List<ArtistVO> artists;
	private List<MusicVO> musics;
	private boolean upload;
	
	public MusicPageGenerator(MusicVO musicVO, List<ArtistVO> artists, List<MusicVO> musics, boolean upload){
		this.musicVO = musicVO;
		this.artists = artists;
		this.musics = musics;
		this.upload = upload;
	}

	public void run() {
    	PageGenerator.generateMusicPage(musicVO, artists, musics, upload);
	}

}
