package br.com.tamandua.service;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import br.com.tamandua.generator.utils.GeneratorProperties;
import br.com.tamandua.generator.utils.MediaHash;
import br.com.tamandua.service.vo.ImageVO;
import br.com.tamandua.ws.service.ImageService;

@Service
public class GeneratorService {
	
	private static final int THREADS = 10;
	
	public class UploadException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public UploadException(Throwable cause){
			super("An error ocurred during generate and uploading page.", cause);
		}
	}

    public void uploadArtistByLetter(String letter){
		String[] args = new String[]{
				"--letter=" + letter
				,"--threads=" + THREADS
				,"--upload" 
		};
		
		generator(args);
	}
    
    public void uploadArtistsByLetter(String letter){
		String[] args = new String[]{
				"--letter=" + letter
				,"--deep-artist"
				,"--threads=" + THREADS
				,"--upload" 
		};

		generator(args);
	}
    
    public void uploadArtist(Long idArtist){
		String[] args = new String[]{
				"--id-artist=" + idArtist
				,"--threads=" + THREADS
				,"--upload"
		};

		generator(args);
	}
    
    public void uploadMusicsByArtist(Long artistId){
		String[] args = new String[]{
				"--id-artist=" + artistId
				,"--deep-music"
				,"--threads=" + THREADS
				,"--upload"
		};

		generator(args);
	}
    
    public void uploadMusic(Long musicId){
		String[] args = new String[]{
				"--id-music=" + musicId
				,"--threads=" + THREADS
				,"--upload"
		};

		generator(args);
	}
   
    public void removeLetterPage(String letter){
    	String dirName = GeneratorProperties.getInstance().getLetterDir();
    	String fileName = letter;
    	removePage(dirName, fileName);
    }
    
    public void removeArtistPage(String artistUri){
    	String dirName = GeneratorProperties.getInstance().getArtistDir();
    	String fileName = artistUri.replaceAll("/", "");
    	removePage(dirName, fileName);
    }
    
    public void removeMusicPage(String musicUri){
    	String dirName = GeneratorProperties.getInstance().getMusicDir();
    	String[] musicUriSplitted = musicUri.split("/");
    	if(musicUriSplitted.length == 3){
    		removePage(dirName+"/"+musicUriSplitted[1], musicUriSplitted[2]);
    	}
    }
    
    public void removePage(String dirName, String fileName){
    	removeFile(dirName, fileName + ".html");
    	removeFile(dirName, fileName + ".inc");
    }
    
    public void removeFile(String dirName, String fileName){
    	String bucketName = GeneratorProperties.getInstance().getBucketName();    	
    	String folderName = MediaHash.getMediaFolder(dirName+"/"+fileName).toString().replace('\\', '/');
    	String keyName = GeneratorProperties.getInstance().getRootPages() + folderName + fileName;
    	
    	String[] args = new String[]{
    			"--bucket=" + bucketName
    			,"--keyname=" + keyName
    			,"--remove"
				,"--threads=" + THREADS
    	};

    	aws(args);
    }
    
    public void removeImage(ImageVO image){
    	String bucketName = ImageService.ARTIST_BUCKET_NAME;
    	String keyName = image.getLocation() + image.getFile();
    	keyName = keyName.replace("/"+bucketName+"/", "");
    	
    	String[] args = new String[]{
    			"--bucket=" + bucketName
    			,"--keyname=" + keyName
    			,"--remove"
				,"--threads=" + THREADS
    	};

    	aws(args);
    }
    
    private void generator(String[] args){
		try{
			System.out.println("ARGS: "+Arrays.asList(args));
			br.com.tamandua.generator.App.main(args);
		} catch (Exception e) {
			throw new UploadException(e);
		}
    }
    
    private void aws(String[] args){
		try{
			System.out.println("ARGS: "+Arrays.asList(args));
			br.com.tamandua.aws.App.main(args);
		} catch (Exception e) {
			throw new UploadException(e);
		}
    }
}
