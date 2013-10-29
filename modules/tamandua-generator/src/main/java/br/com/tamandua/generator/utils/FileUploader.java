package br.com.tamandua.generator.utils;

import java.io.File;
import java.io.InputStream;

import br.com.tamandua.aws.AWSUploader;
import br.com.tamandua.aws.App;

public class FileUploader {
	
	public static void upload(String filePath, String keyName) throws Exception{
		File file = new File(filePath);
		upload(file, keyName);
	}
	
    public static void upload(String fileName, String keyName, InputStream is) throws Exception{
        AWSUploader.getInstance().uploadHtml(fileName, is, GeneratorProperties.getInstance().getBucketName(), GeneratorProperties.getInstance().getRootPages() + keyName);
    }

    public static void upload(File file, String keyName) throws Exception{
		String[] args = new String[4];
		args[0] = "--bucket=" + GeneratorProperties.getInstance().getBucketName();
		args[1] = "--recursive";
		args[2] = "--upload=" + file.getAbsolutePath();
		args[3] = "--keyname=" + GeneratorProperties.getInstance().getRootPages() + keyName;

		App.main(args);
	}
}
