package br.com.tamandua.aws;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

import com.amazonaws.services.s3.AmazonS3;

public class UploaderThread implements Runnable {

	private AmazonS3 s3;
	private File file;
	private String bucketName;
	private String keyName;
	
	private String fileName;
	private InputStream is;
	
	public UploaderThread(AmazonS3 s3, File file, String bucketName, String keyName){
		this.s3 = s3;
		this.file = file;
		this.bucketName = bucketName;
		this.keyName = keyName;
	}
	
    public UploaderThread(AmazonS3 s3, String fileName, InputStream is, String bucketName, String keyName){
        this.s3 = s3;
        this.fileName = fileName;
        this.is = is;
        this.bucketName = bucketName;
        this.keyName = keyName;
    }

    /**
     * Verifica qual forma de envio do arquivo, se lido diretamente do InputStream
     * ou se lera do File (provavelmente armazenado em disco)
     */
    public void run() {
        if (StringUtils.isNotBlank(fileName)) {
            AWSUploader.getInstance().uploadHtml(fileName, is, bucketName, keyName);
        } else {
            AWSUploader.getInstance().upload(file, bucketName, keyName);
        }
	}
}
