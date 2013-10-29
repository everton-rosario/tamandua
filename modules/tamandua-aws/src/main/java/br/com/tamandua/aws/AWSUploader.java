package br.com.tamandua.aws;

import java.io.File;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;

public class AWSUploader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AWSUploader.class);
	
	private static AWSUploader instance;
	private AmazonS3 s3;
	
	/**
     * @return the s3
     */
    public AmazonS3 getS3() {
        return s3;
    }

    public static void init(PropertiesCredentials credentials) {
	    instance = new AWSUploader(credentials);
	}
	
	private AWSUploader (PropertiesCredentials credentials) {
	    this.s3 = new AmazonS3Client(credentials);
	}
	
	public static AWSUploader getInstance() {
	    if (instance == null) {
	        throw new IllegalStateException("AWSUploader in invalid state. Must be called init() method before using it!");
	    }
	    return instance;
	}

	public void upload(File file, String bucketName, String keyName){
		String fileName = file.getName();
		String defKeyName = null;
		if(keyName == null){
			defKeyName = fileName;
		} else {
			defKeyName = keyName + fileName;
		}
		LOGGER.debug("Uploading file '"+defKeyName+"' into bucket '"+bucketName+"'...");
		s3.putObject(bucketName, defKeyName, file);
		
	    // Libera o acesso ao arquivo informado
		LOGGER.debug("Granting read permission for all users to file '"+defKeyName+"'...");
	    AccessControlList acl = s3.getObjectAcl(bucketName, defKeyName);
	    acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
	    s3.setObjectAcl(bucketName, defKeyName, acl);
	    LOGGER.debug("Upload finalizado com sucesso!");
	}
	
	public void uploadHtml(String fileName, InputStream is, String bucketName, String keyName){
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setCacheControl("must-revalidate");
        metadata.setContentEncoding("gzip,deflate");
        metadata.setContentType("text/html");

	 	upload(fileName, is, bucketName, keyName, metadata);
	}
	
	public void uploadImage(String fileName, InputStream is, String bucketName, String keyName){
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setCacheControl("must-revalidate");
		metadata.setContentType("image/jpg");
		
	 	upload(fileName, is, bucketName, keyName, metadata);
	}
	
    public void upload(String fileName, InputStream is, String bucketName, String keyName, ObjectMetadata metadata){
        String defKeyName = null;
        if(keyName == null){
            defKeyName = fileName;
        } else {
            defKeyName = keyName + fileName;
        }
        LOGGER.debug("Uploading file '"+defKeyName+"' into bucket '"+bucketName+"'...");

        /* 
         * Faz o tratamento dos headers do AWS para servir os htmls e Incs
         * de uma forma mais eficaz/eficiente
         */
        s3.putObject(bucketName, defKeyName, is, metadata);
        
        // Libera o acesso ao arquivo informado
        LOGGER.debug("Granting read permission for all users to file '"+defKeyName+"'...");
        AccessControlList acl = s3.getObjectAcl(bucketName, defKeyName);
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        s3.setObjectAcl(bucketName, defKeyName, acl);
        LOGGER.debug("Upload finalizado com sucesso!");
    }
}
