package br.com.tamandua.generator.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringBufferInputStream;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileGenerator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileGenerator.class);

	private static final String PAGES_DIR = GeneratorProperties.getInstance().getPagesDir();
	
	public static void createHtmlFile(String dirName, String fileName, String text, boolean useHash, boolean upload){
		fileName = fileName + ".html";
		createFile(dirName, fileName, text, useHash, upload);
	}

	public static void createIncludeFile(String dirName, String fileName, String text, boolean useHash, boolean upload){
		fileName = fileName + ".inc";
		createFile(dirName, fileName, text, useHash, upload);
	}
	
	public static void createFile(String dirName, String fileName, String text, boolean useHash, boolean upload){
		if (upload) {
		    uploadFile(dirName, fileName, text, useHash);
		} else {
		    storeFile(dirName, fileName, text, useHash);
		}
	}

	private static void storeFile(String dirName, String fileName, String text, boolean useHash){
        try {
            StringBuffer folderName = getFolderName(dirName, fileName, useHash);
            String folderPath = PAGES_DIR+folderName;

        	File dir = new File(folderPath);
        	if(!dir.exists()){
        		dir.mkdirs();
        	}

        	String filePath = dir.getPath()+"/"+fileName;
        	OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8");
        	out.write(text);
        	out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private static void uploadFile(String dirName, String fileName, String text, boolean useHash){
        try {
            InputStream is = new StringBufferInputStream(text);
            
            StringBuffer folderName = getFolderName(dirName, fileName, useHash);
            FileUploader.upload(fileName, folderName.toString().replaceAll(Pattern.quote("\\"), "/"), is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static StringBuffer getFolderName(String dirName, String fileName, boolean useHash){
    	StringBuffer folderName = null;
    	if(useHash){
            LOGGER.debug("Gerando o nome do diretorio onde o arquivo sera salvo.");
            folderName = MediaHash.getMediaFolder(dirName+"/"+fileName);
            LOGGER.debug("Nome do diretorio '"+folderName+"' onde o arquivo sera salvo gerado com sucesso.");
    	} else {
    		folderName = new StringBuffer(dirName+"/");
    	}
    	return folderName;
    }
}
