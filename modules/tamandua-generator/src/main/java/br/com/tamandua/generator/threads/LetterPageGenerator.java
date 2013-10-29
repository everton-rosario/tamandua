package br.com.tamandua.generator.threads;

import java.util.List;

import br.com.tamandua.generator.utils.PageGenerator;
import br.com.tamandua.service.vo.ArtistVO;

public class LetterPageGenerator implements Runnable {
		
	private String letter;
	private List<ArtistVO> artists;
	private boolean upload;
	
	public LetterPageGenerator(String letter, List<ArtistVO> artists, boolean upload){
		this.letter = letter;
		this.artists = artists;
		this.upload = upload;
	}

	public void run() {
		PageGenerator.generateLetterPage(letter, artists, upload);
	}
	
}
