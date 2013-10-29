package br.com.tamandua.aws;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Class App
 *
 * @author Everton Rosario (erosario@gmail.com)
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    // Opcoes existentes para a aplicacao
    private static Options options = getOptions();
    
    private static int threads = 0;
    
    private static ExecutorService executor;
    
    /** @param args 
     * @throws JAXBException 
     * @throws InterruptedException 
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws InvocationTargetException 
     * @throws NoSuchMethodException 
     * @throws IllegalArgumentException 
     * @throws SecurityException 
     */
    public static void main(String[] args) throws JAXBException, InterruptedException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
        // interpretador de comandos
        CommandLineParser parser = new PosixParser();
        try {
            // Realiza o parse da linha de comando
            CommandLine line = parser.parse( options, args );

            // Verifica se nao existe alguma inconsistencia com a chamada de prompt de comando
            if (!isValidLine(line) || line.hasOption("help")) {
                showUsage();
                return;
            }
            
            AWSUploader.init(new PropertiesCredentials(
                    S3Sample.class.getResourceAsStream("/AwsCredentials.properties")));
            
            boolean isRecursive = false;
            // Define se as operacoes serao recursivas, ou no nivel do diretorio informado
            if (line.hasOption("recursive")) {
                isRecursive = true;
            }

            // Define que bucket vai utilizar na operacao. Campo OBRIGATORIO
            String bucketName = null;
            if (line.hasOption("--bucket")) {
                bucketName = line.getOptionValue("bucket");
            } else {
                showUsage();
                return;
            }
            
            // Define qual sera o keyName usado para salvar o(s) arquivo(s).
            String keyName = null;
            if (line.hasOption("--keyname")) {
            	keyName = line.getOptionValue("keyname");
            }

            // Define quantas threads utilizara
            if (line.hasOption("threads")) {
            	try {
            		threads = Integer.parseInt(line.getOptionValue("threads"));
            		executor = Executors.newFixedThreadPool(threads);
            	} catch (Exception e) {
            		showUsage();
            	}
            }

            AmazonS3 s3 = AWSUploader.getInstance().getS3();
            
            /*
             * Mostra estatisticas do bucket em questao 
             */
            if (line.hasOption("--stats")) {
                getStatistics(s3, bucketName, threads, isRecursive);
            }

            if (line.hasOption("--upload") && line.hasOption("--remove")) {
            	showUsage();
            } else if (line.hasOption("--upload")) {
                String strFile = line.getOptionValue("upload");
                File file = new File(strFile);
                if (!file.exists()) {
                    LOGGER.error("to upload, the file or directory must exist!");
                    return;
                }

                // Procura a existencia do bucket
                boolean alreadyExistBucket = checkExistenceOfBucket(s3, bucketName);
                
                // Cria o bucket caso nao exista e ajusta a permissao de acesso
                if (!alreadyExistBucket) {
                	LOGGER.debug("Bucket not found...");                    	
                	LOGGER.debug("Creating bucket '"+bucketName+"'...");
                    s3.createBucket(bucketName);
                    AccessControlList bucketAcl = s3.getBucketAcl(bucketName);
                    LOGGER.debug("Granting read permissions for all users to bucket '"+bucketName+"'...");
                    bucketAcl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
                    s3.setBucketAcl(bucketName, bucketAcl);
                }
                
                uploadFileOrDirectory(s3, bucketName, keyName, isRecursive, file);
            } else if (line.hasOption("--remove")) {
                // Procura a existencia do bucket
                boolean alreadyExistBucket = checkExistenceOfBucket(s3, bucketName);
                
                if (alreadyExistBucket) {
	        		LOGGER.debug("Removing file '"+keyName+"' into bucket '"+bucketName+"'...");
	        		s3.deleteObject(bucketName, keyName);
	        	    LOGGER.debug("Remove finalizado com sucesso!");
                }
            }
            
            if(executor != null){
            	executor.shutdown();
            }
        }
        catch(ParseException exp) {
            showUsage();
        }	
    }

    private static void uploadFileOrDirectory(AmazonS3 s3, String bucketName, String keyName, boolean isRecursive, File startFile)
    	throws IOException {
        // Faz o upload de um arquivo simples
        if (startFile.isFile()) {
        	uploadFile(s3, startFile, bucketName, keyName);
        // Faz o tratamento de diretorios  
        } else if (startFile.isDirectory()) {
            File[] files = startFile.listFiles();
            for (File file : files) {
            	if (file.isFile()) {
            		uploadFile(s3, file, bucketName, keyName);
            	} else if (file.isDirectory() && isRecursive) {
            		uploadFileOrDirectory(s3, bucketName, keyName+file.getName()+"/", isRecursive, file);
            	}
            }
        }        
    }
    
    private static void uploadFile(AmazonS3 s3, File file, String bucketName, String keyName){
    	if(threads > 0){
    		executor.execute(new UploaderThread(s3, file, bucketName, keyName));
    	} else {
    		AWSUploader.getInstance().upload(file, bucketName, keyName);
    	}
    }

    private static boolean checkExistenceOfBucket(AmazonS3 s3, String bucketName) {
        boolean alreadyExistBucket = false;
        List<Bucket> listBuckets = s3.listBuckets();
        for (Bucket bucket : listBuckets) {
            if (bucket.getName().equals(bucketName)) {
                alreadyExistBucket = true;
            }
        }
        return alreadyExistBucket;
    }

    /*
     * List objects in your bucket by prefix - There are many options for
     * listing the objects in your bucket.  Keep in mind that buckets with
     * many objects might truncate their results when listing their objects,
     * so be sure to check if the returned object listing is truncated, and
     * use the AmazonS3.listNextBatchOfObjects(...) operation to retrieve
     * additional results.
     */
    private static void getStatistics(AmazonS3 s3, String bucket, int threads, boolean isRecursive) {
        LOGGER.debug("Listing objects");
        ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
        .withBucketName(bucket));
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            LOGGER.debug(" - " + objectSummary.getKey() + "  " +
                    "(size = " + objectSummary.getSize() + ")");
        }
                
    }

    /**
     * Verificacao se os parametros informados sao validos individualmente utilizando obrigatoriamente o "PROVIDER"
     * @param line Linha parseada do commons-cli
     * @return Se a linha esta valida (true) ou invalida(false)
     */
    private static boolean isValidLine(CommandLine line) {
        if ((line.hasOption("help") || 
             line.hasOption("stats") ||
             line.hasOption("upload") ||
             line.hasOption("keyname") ||
             line.hasOption("threads") ||
             line.hasOption("remove") ||
             line.hasOption("download")) && line.hasOption("bucket")) {
            
            return true; 
        } else {
            return false;
        }
    }

    /**
     * Mostra como deve ser utilizado a aplicacao.
     */
    private static void showUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "aws", options );
    }

    /**
     * @return opcoes utilizadas pelo sistema 
     */
    private static Options getOptions() {
        // create the Options
        Options options = new Options();
        
        Option help        = new Option( "help", "print this message" );

        Option recursive = new Option( "recursive", "scans all subfolders to upload files and subfolders" );

        Option stats = OptionBuilder.withLongOpt( "stats" )
                                    .withDescription( "get the statistics from the specified bucket" )
                                    .create();

        Option threads = OptionBuilder.withLongOpt( "threads" )
                                      .withDescription( "Number of threads to process the operations" )
                                      .hasArg()
                                      .withArgName("NUMBER_OF_THREADS")
                                      .create();

        Option upload = OptionBuilder.withLongOpt( "upload" )
                                     .withDescription( "uploads all subfiles if the indicated FILE is a Directory, or uploads only the file if it is just a file. if you choose this option, you cannot use remove option" )
                                     .hasArg()
                                     .withArgName("FILE")
                                     .create();
        Option download = OptionBuilder.withLongOpt( "download" )
                                       .withDescription( "download the entire bucket" )
                                       .hasArg()
                                       .create();
        Option bucket = OptionBuilder.withLongOpt( "bucket" )
                                     .withDescription( "The bucket to be used" )
                                     .hasArg()
                                     .withArgName("BUCKET")
                                     .create();
        Option keyName = OptionBuilder.withLongOpt( "keyname" )
							         .withDescription( "The key name used to save files." )
							         .hasArg()
							         .withArgName("KEYNAME")
							         .create();
        Option acl = OptionBuilder.withLongOpt( "acl" )
                                  .withDescription( "The permissions to be applied when uploading")
                                  .hasArg()
                                  .withArgName("ACLs")
                                  .create();
        Option remove = OptionBuilder.withLongOpt( "remove" )
							        .withDescription( "removes all subfiles if the indicated FILE is a Directory, or removes only the file if it is just a file. if you choose this option, you cannot use upload option" )
							        .create();
        
        // Faz o bind das opcoes criadas ao conjunto de instrucoes aceitas
        options.addOption(help);
        options.addOption(stats);
        options.addOption(recursive);
        options.addOption(upload);
        options.addOption(download);
        options.addOption(bucket);
        options.addOption(keyName);
        options.addOption(acl);
        options.addOption(threads);
        options.addOption(remove);
        
        return options;
    }
    
    

}
