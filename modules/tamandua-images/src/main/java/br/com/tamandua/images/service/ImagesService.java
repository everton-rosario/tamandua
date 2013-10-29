package br.com.tamandua.images.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.tamandua.aws.AWSUploader;
import br.com.tamandua.aws.S3Sample;
import br.com.tamandua.images.dao.ImagesDao;
import br.com.tamandua.images.utils.ImagesProperties;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.ImageVO;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class ImagesService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImagesService.class);
	
	public static void updateImagesNotFound() throws Exception {
    	LOGGER.info("Updating images not found...");
    	
		try{
			AWSUploader.init(new PropertiesCredentials(
                S3Sample.class.getResourceAsStream("/AwsCredentials.properties")));
			AmazonS3 s3 = AWSUploader.getInstance().getS3();

	        ImagesDao imageDAO = new ImagesDao(ImagesProperties.getInstance().getDBUser(),
	                                         ImagesProperties.getInstance().getDBPass(),
	                                         ImagesProperties.getInstance().getDBURL(),
	                                         ImagesProperties.getInstance().getDBDriver());
	        
	        String bucketName = ImagesProperties.getInstance().getImagesBucketName();
	        String dirName = ImagesProperties.getInstance().getArtistDirName();

	        Collection<ArtistVO> artists = imageDAO.getArtistImages();
	        for(ArtistVO artist : artists){
    			if(artist != null){
    				String artistUri = artist.getUri();
    				LOGGER.info("Verificando as "+artist.getAllImages().size()+" imagens do artista '"+artistUri+"'.");
    				List<String> imagesS3 = new ArrayList<String>();
    				String keyName = dirName + artistUri;
    				ObjectListing objectList = s3.listObjects(bucketName, keyName);
    				for(S3ObjectSummary objectSummary : objectList.getObjectSummaries()){
    					String fileName = objectSummary.getKey().replaceAll(keyName, "");
    					String[] fileNames = fileName.split("\\.");
    					if(fileNames.length == 2){
    						imagesS3.add(fileNames[0]);
    					}
    				}
    				
    				int notFound = 0;
    				for(ImageVO image : artist.getAllImages()){
    					Long idImage = image.getIdImage();
						if(!imagesS3.contains(String.valueOf(idImage))){
							LOGGER.warn("A imagem '"+idImage+"' do artista '"+artistUri+"' nao foi encontrada.");
				        	imageDAO.updateImageFlag(idImage, false);
							notFound++;
						} else {
							imageDAO.updateImageFlag(idImage, true);
						}
					}
    				if(notFound == 0){
    					LOGGER.info("Todas as imagens do artista '"+artistUri+"' foram encontradas.");
    				} else {
    					LOGGER.info("Nao foram encontradas "+notFound+" imagens do artista '"+artistUri+"'.");	
    				}
    			}
	        }
		} catch (Exception e) {
			LOGGER.error("Ocorreu um erro durante a atualizacao da flag das imagens nao encontradas.", e);
			throw e;
		}
		
		 LOGGER.info("Finished updating images not found!");
	}
}
