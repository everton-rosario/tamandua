package br.com.tamandua.service;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import br.com.tamandua.indexer.App;
import br.com.tamandua.indexer.IndexerProperties;

@Service
public class IndexerService {
	
    static {    	
    	IndexerProperties.setFileResource(IndexerService.class.getResourceAsStream("/tamandua-indexer.properties"));
    }
	
	private static final int THREADS = 10;
	
	public class IndexerException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public IndexerException(Throwable cause){
			super("An error ocurred during indexing pages.", cause);
		}
	}
	
	public class CleanerException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public CleanerException(Throwable cause){
			super("An error ocurred during cleaning pages.", cause);
		}
	}

    public void indexByLetter(String letter){
		String[] args = new String[]{
				"--index"
				,"--letters=" + letter 
				,"--threads=" + THREADS
		};
		
		indexer(args);
	}
    
    public void cleanByLetter(String letter){
		String[] args = new String[]{
				"--clean"
				,"--letters=" + letter 
				,"--threads=" + THREADS
		};
		
		cleaner(args);
	}
    
    public void indexArtist(Long artistId){
		String[] args = new String[]{
				"--index"
				,"--artistId=" + artistId
				,"--threads=" + THREADS
		};

		indexer(args);
	}
    
    public void cleanArtist(Long artistId){
		String[] args = new String[]{
				"--clean"
				,"--artistId=" + artistId
				,"--threads=" + THREADS
		};

		cleaner(args);
	}
    
    public void indexMusic(Long musicId){
		String[] args = new String[]{
				"--index"
				,"--musicId=" + musicId
				,"--threads=" + THREADS
		};

		indexer(args);
	}
    
    public void cleanMusic(Long musicId){
		String[] args = new String[]{
				"--clean"
				,"--musicId=" + musicId
				,"--threads=" + THREADS
		};

		cleaner(args);
	}
    
    private void indexer(String[] args){
		try{
			System.out.println("[index] ARGS: "+Arrays.asList(args));
			App.main(args);
		} catch (Exception e) {
			throw new IndexerException(e);
		}
    }
    
    private void cleaner(String[] args){
		try{
			System.out.println("[clean] ARGS: "+Arrays.asList(args));
			App.main(args);
		} catch (Exception e) {
			throw new CleanerException(e);
		}
    }
}
