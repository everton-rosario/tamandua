/**
 * 
 */
package br.com.tamandua.ws.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.tamandua.generator.utils.MediaHash;
import br.com.tamandua.persistence.app.AppContext;
import br.com.tamandua.service.GeneratorService;
import br.com.tamandua.service.adapter.ImageAdapter;
import br.com.tamandua.service.util.StringNormalizer;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.ImageVO;
import br.com.tamandua.service.vo.UserVO;
import br.com.tamandua.service.vo.validator.MessagesVO;
import br.com.tamandua.ws.utils.ImageRescaleTransformation;
import br.com.tamandua.ws.utils.ImageUploader;

/**
 * @author Everton Rosario (erosario@gmail.com)
 */
@Service
public class ImageService {
	
	public static final int WIDTH = 100;
	public static final int HEIGHT = 120;
	
	public static final String USER_BUCKET_NAME = "tocaletrausrimg";
	public static final String USER_URI = "/"+USER_BUCKET_NAME+"/";
	public static final String USER_URL = "http://"+USER_BUCKET_NAME+".s3.amazonaws.com/";

	public static final String ARTIST_BUCKET_NAME = "tocaletraimg";
	public static final String ARTIST_URI = "/"+ARTIST_BUCKET_NAME+"/";
	public static final String ARTIST_URL = "http://"+ARTIST_BUCKET_NAME+".s3.amazonaws.com/";
	
	public static final String PROVIDER = "S3";
	public static final String FILE_EXTENSION = ".jpg";

	public String normalizaImage(String imageName){
		//Normaliza o nome da imagem (remove até o ponto);
		String fileNameNormalize = StringNormalizer.normalizeFileName(imageName);
		int tam = fileNameNormalize.length() - 3;
		String fileNameAux = fileNameNormalize.substring(0,tam);
		return fileNameAux + ".jpg";
	}
	
	@Transactional
	public ImageVO uploadImage(ArtistVO artistVO){
		String artistUri = artistVO.getUri();
		byte[] byteArray = artistVO.getFile();
		Iterator<ImageVO> images = artistVO.getAllImages().iterator();
		String description = (images.hasNext()) ? images.next().getDescription() : "";
		
		ImageVO image = new ImageVO();
    	ImageIcon imageIcon = new ImageIcon(byteArray);
    	int height = imageIcon.getIconHeight();
    	int width = imageIcon.getIconWidth();

    	image.setUri(artistUri);	    	
    	saveImage(image);
    	image = findByUri(artistUri);

		String fileName = image.getIdImage().toString();
		String folderName = "artist" + artistUri;
		folderName = folderName.replace('\\', '/');
		
		image.setFile(fileName+FILE_EXTENSION);
		image.setUri(ARTIST_URI+folderName+fileName);
		image.setUrl(ARTIST_URL+folderName+image.getFile());
		image.setLocation(ARTIST_URI+folderName);
		image.setProvider(PROVIDER);
		image.setDescription(description);
    	image.setHeight(height);
    	image.setWidth(width);

		InputStream stream = new ByteArrayInputStream(byteArray);
		ImageUploader uploader = new ImageUploader(image.getFile(), stream, ARTIST_BUCKET_NAME, folderName);
		//new Thread(uploader).start();
		uploader.update();

		editImage(image);
    	
    	return image;
	}

	public ImageVO uploadImage(UserVO userVO) {
		byte[] byteArray = userVO.getFile();
		String fileName = "avatar_ori";
		String folderName = MediaHash.getMediaFolder(userVO.getIdUser().toString()).toString();
		folderName = folderName.substring(1);
		folderName = folderName.replace('\\', '/');
		folderName += userVO.getIdUser() + "/";

		ImageVO image = new ImageVO();
		image.setFile(fileName+FILE_EXTENSION);
		image.setUri(USER_URI+folderName+fileName);
		image.setUrl(USER_URL+folderName+image.getFile());
		image.setLocation(USER_URI+folderName);
		image.setProvider(PROVIDER);
		image.setDescription("Imagem do tamanho original do avatar do usuário.");

		InputStream stream = new ByteArrayInputStream(byteArray);
		ImageUploader uploader = new ImageUploader(image.getFile(), stream, USER_BUCKET_NAME, folderName);
		new Thread(uploader).start();
		
    	ImageIcon imageIcon = new ImageIcon(byteArray);
    	image.setHeight(imageIcon.getIconHeight());
    	image.setWidth(imageIcon.getIconWidth());
    	saveImage(image);
    	
    	return findByUri(image.getUri());
	}
	
	public ImageVO uploadRescaleImage(UserVO userVO){
		uploadImage(userVO);
		
		byte[] byteArray = userVO.getFile();
		String fileName = "avatar_"+WIDTH+"_"+HEIGHT;
		String folderName = MediaHash.getMediaFolder(userVO.getIdUser().toString()).toString();
		folderName = folderName.substring(1);
		folderName = folderName.replace('\\', '/');
		folderName += userVO.getIdUser() + "/";
		
		ImageVO image = new ImageVO();
		image.setFile(fileName+FILE_EXTENSION);
		image.setUri(USER_URI+folderName+fileName);
		image.setUrl(USER_URL+folderName+image.getFile());
		image.setLocation(USER_URI+folderName);
		image.setProvider(PROVIDER);
		image.setDescription("Imagem redimensinada ("+WIDTH+"x"+HEIGHT+") do avatar do usuário.");
		image.setWidth(WIDTH);
		image.setHeight(HEIGHT);

		InputStream stream = new ByteArrayInputStream(byteArray);
		InputStream rescaleStream = ImageRescaleTransformation.getRescaleStream(stream, HEIGHT, WIDTH);

		ImageUploader uploader = new ImageUploader(image.getFile(), rescaleStream, USER_BUCKET_NAME, folderName);
		uploader.update();
		
		saveImage(image);		
		return findByUri(image.getUri());
	}
	
    @Transactional
    public MessagesVO saveImage(ImageVO imageVO) {
        ImageAdapter adapter = (ImageAdapter) AppContext.getApplicationContext().getBean("imageAdapter");
        return adapter.saveImage(imageVO);
    }
    
    @Transactional
    public MessagesVO editImage(ImageVO imageVO) {
        ImageAdapter adapter = (ImageAdapter) AppContext.getApplicationContext().getBean("imageAdapter");
        return adapter.editImage(imageVO);
    }

    public ImageVO findByUri(String uri) {
        ImageAdapter adapter = (ImageAdapter) AppContext.getApplicationContext().getBean("imageAdapter");
        return adapter.findByUri(uri);
    }

    public List<ImageVO> findByProvider(String provider) {
        ImageAdapter adapter = (ImageAdapter) AppContext.getApplicationContext().getBean("imageAdapter");
        return adapter.findByProvider(provider);
    }

    public ImageVO getImageExample() {
        ImageVO image = new ImageVO();
        image.setUri("/uri/imagem.jpg");
        image.setDescription("Image description.");
        image.setFile("/path/to/some/file.jpg");
        image.setHeight(100);
        image.setWidth(100);
        image.setLocation("some location");
        image.setProvider("where we found this foto");
        image.setUrl("http://www.somedomain.ext");
        return image;
    }
    
    @Transactional
    public MessagesVO removeImage(ArtistVO artistVO) {
    	ImageAdapter adapter = (ImageAdapter) AppContext.getApplicationContext().getBean("imageAdapter");
        GeneratorService generatorService = (GeneratorService) AppContext.getApplicationContext().getBean("generatorService");
        
		for(ImageVO imgVO : artistVO.getAllImages()){
			ImageVO image = adapter.findById(imgVO.getIdImage());
			generatorService.removeImage(image);
    	}
        
        return adapter.removeImage(artistVO);
    }
    
    
    
}
