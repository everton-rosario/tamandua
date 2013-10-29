package br.com.tamandua.ws.utils;

import java.io.IOException;
import java.io.InputStream;

import br.com.tamandua.aws.AWSUploader;
import br.com.tamandua.aws.S3Sample;

import com.amazonaws.auth.PropertiesCredentials;

public class ImageUploader implements Runnable {
	
	private String fileName;
	private InputStream in;
	private String bucketName;
	private String folderName;
	
	static {
    	try{
    		AWSUploader.init(new PropertiesCredentials(
		        S3Sample.class.getResourceAsStream("/AwsCredentials.properties")));
    	} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public ImageUploader(String fileName, InputStream in, String bucketName, String folderName){
		this.fileName = fileName;
		this.in = in;
		this.bucketName = bucketName;
		this.folderName = folderName;
	}
	
	public void run(){
		update();
	}
	
	public void update(){
		AWSUploader.getInstance().uploadImage(fileName, in, bucketName, folderName);
	}
	
}
