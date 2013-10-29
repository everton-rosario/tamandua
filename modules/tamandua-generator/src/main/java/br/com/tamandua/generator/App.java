package br.com.tamandua.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

import twitter4j.Status;
import twitter4j.TwitterException;
import br.com.tamandua.aws.AWSUploader;
import br.com.tamandua.aws.S3Sample;
import br.com.tamandua.generator.threads.ArtistPageGenerator;
import br.com.tamandua.generator.threads.LetterPageGenerator;
import br.com.tamandua.generator.threads.MusicPageGenerator;
import br.com.tamandua.generator.utils.GeneratorProperties;
import br.com.tamandua.generator.utils.PageGenerator;
import br.com.tamandua.generator.utils.ServiceProxy;
import br.com.tamandua.generator.utils.TopFileReader;
import br.com.tamandua.generator.vo.TopArtistVO;
import br.com.tamandua.generator.vo.TopMusicVO;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.MusicVO;
import br.com.tamandua.twitter.TwitterTimeline;

import com.amazonaws.auth.PropertiesCredentials;
import com.google.gson.Gson;


public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    // Opcoes existentes para a aplicacao
    private static Options options = getOptions();

    private static boolean upload = false;
    private static int threads = 0;
    
    private static ExecutorService executor;
	
	public static void main(String[] args) throws IOException {
        // interpretador de comandos
        CommandLineParser parser = new PosixParser();
        try{
        	CommandLine line = parser.parse(options, args);
            // Verifica se nao existe alguma inconsistencia com a chamada de prompt de comando
            if (!isValidLine(line) || line.hasOption("help")) {
                help();
                return;
            }

            if(line.hasOption("upload")){
                AWSUploader.init(new PropertiesCredentials(
                        S3Sample.class.getResourceAsStream("/AwsCredentials.properties")));
            	upload = true;
            }

            if(line.hasOption("threads")){
            	try {
            		threads = Integer.parseInt(line.getOptionValue("threads"));
            		executor = Executors.newFixedThreadPool(threads);
            	} catch (Exception e) {
					help();
				}
            }
            
            if(line.hasOption("top")){
            	String filePath = line.getOptionValue("top");
            	generateTop(filePath);
            }
            
            if(line.hasOption("az")){
            	generateAZ();
            }
            
            if(line.hasOption("letter")){
            	String letter = line.getOptionValue("letter");
            	//if(letter == null){
            		boolean deepArtist = line.hasOption("deep-artist");
            		boolean deepMusic = line.hasOption("deep-music");
            		
            		int indexArtist = 0; 
            		if(line.hasOption("index-artist")){
            			indexArtist = Integer.parseInt(line.getOptionValue("index-artist")) - 1;
            		}
            		
            		generateLetter(letter, deepArtist, deepMusic, indexArtist);
            	/*} else {
            		help();
            	}*/
            }
            
            if(line.hasOption("id-artist")){
            	Long artistId = Long.parseLong(line.getOptionValue("id-artist"));
            	boolean deepMusic = line.hasOption("deep-music");
            	generateArtist(artistId, deepMusic);
            }
            
            if(line.hasOption("artist")){
            	String artist = line.getOptionValue("artist");
            	boolean deepMusic = line.hasOption("deep-music");
            	generateArtist(artist, deepMusic);
            }
            
            if(line.hasOption("id-music")){
            	Long musicId = Long.parseLong(line.getOptionValue("id-music"));
            	generateMusic(musicId);
            }
            
            if(line.hasOption("twitter")){
                generateTwitter();
            }

            if(line.hasOption("music")){
            	String artist = line.getOptionValues("music")[0];
            	if(line.getOptionValues("music").length == 1){
            		generateMusics(artist);
            	} else {
                	String music = line.getOptionValues("music")[1];
                	generateMusic(artist, music);
            	}
            }
            
            if(executor != null){
            	executor.shutdown();
            }
        } catch (ParseException e) {
			help();
		} catch (NumberFormatException e) {
			help();
		}
	}
	
    /**
     * Verificacao se os parametros informados sao validos.
     * @param line Linha parseada do commons-cli
     * @return se a linha esta valida (true) ou invalida(false)
     */
    private static boolean isValidLine(CommandLine line) {
        if ((line.hasOption("help") || 
             line.hasOption("letter") ||
             line.hasOption("artist") || line.hasOption("id-artist") ||
             line.hasOption("music") || line.hasOption("id-music") ||
             line.hasOption("deep-artist") ||
             line.hasOption("deep-music") ||
             line.hasOption("index-artist") ||
             line.hasOption("upload") ||
             line.hasOption("threads") ||
             line.hasOption("az") || 
             line.hasOption("twitter") || 
        	 line.hasOption("top") )) {
            return true;
        } else {
            return false;
        }
    }
	
    /**
     * Mostra como deve ser utilizado a aplicacao.
     */
    private static void help() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("tamandua-generator", options);
    }
	
    /**
     * Obtem as opcoes utilizadas pelo sistema.
     * @return opcoes utilizadas pelo sistema 
     */
    private static Options getOptions() {
        // create the Options
        Options options = new Options();
        
        Option help = new Option("help", "Display help information");
        Option letter = OptionBuilder.withLongOpt("letter")
									  .withDescription("[a-z] - Gera pagina com os artistas que comecam com essa letra.\n" +
														" [0] - Gera pagina com os artistas que comecam com numeros e caracteres especiais.\n" +
														" [ ] - Gera todas as paginas com os artistas que comecam com as letras de A a Z, com numeros e caracteres especiais.")
									  .hasOptionalArg()
									  .withArgName("[a-z0]")
									  .create();
        Option artist = OptionBuilder.withLongOpt("artist")
									  .withDescription("Gera pagina com as musicas de um artista.")
									  .hasArg()
									  .withArgName("URI do artista")
									  .create();
        Option idArtist = OptionBuilder.withLongOpt("id-artist")
									  .withDescription("Gera pagina com as musicas de um artista.")
									  .hasArg()
									  .withArgName("ID do artista")
									  .create();
        Option music = OptionBuilder.withLongOpt("music")
									  .withDescription("Gera pagina com a letra da musica de um artista.")
									  .hasArgs(2)
									  .withArgName("URI do artista e da musica")
									  .create();
        Option idMusic = OptionBuilder.withLongOpt("id-music")
									  .withDescription("Gera pagina com a letra da musica de um artista.")
									  .hasArg()
									  .withArgName("ID da musica")
									  .create();
        Option deepArtist = OptionBuilder.withLongOpt("deep-artist")
									        .withDescription( "Gera as paginas com as musicas dos artistas.")
									        .create();
        Option deepMusic = OptionBuilder.withLongOpt("deep-music")
									        .withDescription( "Gera as paginas com as letras das musicas dos artista.")
									        .create();
        Option indexArtist = OptionBuilder.withLongOpt("index-artist")
									        .withDescription( "Gera as paginas dos artistas a partir desse indice da lista.")
											.hasArg()
											.withArgName("Indice da lista")
									        .create();
        Option upload = OptionBuilder.withLongOpt("upload")
									        .withDescription( "Gera as paginas com as letras das musicas dos artista.")
									        .create();
        Option threads = OptionBuilder.withLongOpt("threads")
									        .withDescription( "Numero de threads que serao utilizadas durante o processo.")
									        .hasArg()
									        .withArgName("Numero de threads. Deve ser maior que zero.")
									        .create();
        Option az = OptionBuilder.withLongOpt("az")
										    .withDescription("Gera a barra com os links das letras AZ.")
										    .create();
        Option twitter = OptionBuilder.withLongOpt("twitter")
                                      .withDescription("Gera o json do twitter.")
                                      .create();
        Option top = OptionBuilder.withLongOpt("top")
        									.withDescription("Gera as paginas home, top artistas, top musicas e nuvem de artistas.")
        									.hasArg()
        									.withArgName("URL do arquivo CSV com o TOP dos artistas e das musicas.")										    
										    .create();
        
        options.addOption(help);
        options.addOption(letter);
        options.addOption(artist);
        options.addOption(idArtist);
        options.addOption(music);
        options.addOption(idMusic);
        options.addOption(deepArtist);
        options.addOption(deepMusic);
        options.addOption(indexArtist);
        options.addOption(upload);
        options.addOption(threads);
        options.addOption(az);
        options.addOption(twitter);
        options.addOption(top);
        
        return options;
    }
  
    private static void generateTop(String filePath) {
    	List<TopArtistVO> topArtists = TopFileReader.getTopArtists(filePath);
    	List<TopMusicVO> topMusics = TopFileReader.getTopMusics(filePath);
    	List<Status> twitterStatus = TwitterTimeline.getMixedFriendsMentionsStatus();
    	
    	PageGenerator.generateHomePage(topArtists, topMusics, twitterStatus, upload);
    	PageGenerator.generateTopArtistsPage(topArtists, upload);
    	PageGenerator.generateTopMusicsPage(topMusics, upload);
    	PageGenerator.generateCloudArtistPage(topArtists, upload);
    	PageGenerator.generateCloudMusicPage(topMusics, upload);
    	PageGenerator.generateTwitter(twitterStatus, upload);
    }
    
    private static void generateAZ(){
    	List<Map<String, String>> lettersMap = new ArrayList<Map<String, String>>();
    	String[] letters = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","0"};    	
    	for(String letter : letters){
    		List<ArtistVO> artists = ServiceProxy.getInstance().findArtistsWithLyricsPublishedByLetter(letter.toUpperCase());    		
    		Map<String, String> letterMap = new HashMap<String, String>();
			letterMap.put("letter", letter);
			letterMap.put("size", String.valueOf(artists.size()));
			lettersMap.add(letterMap);
		}
    	PageGenerator.generateAZPage(lettersMap, upload);
    }
    
    private static void generateLetter(String letter, boolean deepArtist, boolean deepMusic, int indexArtist){
		if(letter == null){
    		String[] letters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","0"};
            List<ArtistVO> artists = null;
    		for(String l : letters){
    			artists = ServiceProxy.getInstance().findArtistsWithLyricsPublishedByLetter(l);
    			generateLetter(l, artists, deepArtist, deepMusic, indexArtist);
    		}
		} else {
		    for (int i = 0; i < letter.length(); i++) {
		        String letterToGenerate = "" + letter.charAt(i);
	            List<ArtistVO> artists = ServiceProxy.getInstance().findArtistsWithLyricsPublishedByLetter(letterToGenerate);
	            generateLetter(letterToGenerate, artists, deepArtist, deepMusic, indexArtist);
            }
		}
    }
    
    private static void  generateLetter(String letter, List<ArtistVO> artists, boolean deepArtist, boolean deepMusic, int indexArtist){
		Collections.sort(artists, new Comparator<ArtistVO>(){										
			public int compare(ArtistVO artist1, ArtistVO artist2) {
				return artist1.getName().compareTo(artist2.getName());
			}										
		});
    	
    	generateLetterPage(letter, artists);

		if(deepArtist || deepMusic){			
			for(ArtistVO artist : artists.subList(indexArtist, artists.size())){
				if(deepArtist){
					generateArtist(artist, artists, deepMusic);
				} else if(deepMusic){
					generateMusics(artist, artists);	
				}	
			}
		}
    }

    private static void generateArtist(Long artistId, boolean deepMusic){
    	ArtistVO artistVO = ServiceProxy.getInstance().findArtistById(artistId);
    	generateArtist(artistVO, deepMusic);
    }
    
    private static void generateArtist(String artistUri, boolean deepMusic){
    	ArtistVO artistVO = ServiceProxy.getInstance().findArtistByURI(artistUri);
    	generateArtist(artistVO, deepMusic);
    }
    
    private static void generateArtist(ArtistVO artistVO, boolean deepMusic){
    	if(artistVO != null){
    		String artistLetter = String.valueOf(artistVO.getLetters().charAt(0));
    		List<ArtistVO> artists = ServiceProxy.getInstance().findArtistsWithLyricsPublishedByLetter(artistLetter);
    		generateArtist(artistVO, artists, deepMusic);
    	}
    }
    
    private static void generateArtist(ArtistVO artistVO, List<ArtistVO> artists, boolean deepMusic){
        if(artistVO != null){
        	List<MusicVO> musics = ServiceProxy.getInstance().findMusicsByArtist(artistVO);
        	if(musics.size() > 0){
        		Collections.sort(musics, new Comparator<MusicVO>(){										
        			public int compare(MusicVO music1, MusicVO music2) {
        				return music1.getTitle().compareTo(music2.getTitle());
        			}										
        		});
        		
	        	generateArtistPage(artistVO, artists, musics);
	        	
	        	if(deepMusic){
	        		generateMusics(musics, artists);
	        	}
        	}
        }
    }
    
    private static void generateMusic(Long musicId){
        MusicVO musicVO = ServiceProxy.getInstance().findMusicById(musicId);
        if(musicVO != null){
        	generateMusic(musicVO);
        }
    }
    
    private static void generateMusic(String artistUri, String musicUri){
        MusicVO musicVO = ServiceProxy.getInstance().findMusicByURI(artistUri, musicUri);
        if(musicVO != null){
        	generateMusic(musicVO);
        }
    }
    
    private static void generateMusics(String artistUri){
		ArtistVO artistVO = ServiceProxy.getInstance().findArtistByURI(artistUri);
        if(artistVO != null){
        	List<MusicVO> musics = ServiceProxy.getInstance().findMusicsByArtist(artistVO);
        	generateMusics(musics);
        }
    }
    
    private static void generateMusics(ArtistVO artistVO, List<ArtistVO> artists){
        if(artistVO != null){
        	List<MusicVO> musics = ServiceProxy.getInstance().findMusicsByArtist(artistVO);
        	generateMusics(musics, artists);
        }
    }
    
    private static void generateMusics(List<MusicVO> musics){
    	if(musics.size() > 0){
    		MusicVO musicVO = musics.get(0);
	    	String artistLetter = String.valueOf(musicVO.getArtist().getLetters().charAt(0));
		    List<ArtistVO> artists = ServiceProxy.getInstance().findArtistsWithLyricsPublishedByLetter(artistLetter);
		    generateMusics(musics, artists);
    	}
    }
    
    private static void generateMusics(List<MusicVO> musics, List<ArtistVO> artists){    	
    	for(MusicVO musicVO : musics){
    		generateMusicPage(musicVO, artists, musics);
    	}
    }
    
    private static void generateMusic(MusicVO musicVO){
    	ArtistVO artist = musicVO.getArtist();
    	String artistLetter = String.valueOf(artist.getLetters().charAt(0));
    	List<ArtistVO> artists = ServiceProxy.getInstance().findArtistsWithLyricsPublishedByLetter(artistLetter);
    	List<MusicVO> musics = ServiceProxy.getInstance().findMusicsByArtist(artist);

    	generateMusicPage(musicVO, artists, musics);
    }
    
    private static void generateLetterPage(String letter, List<ArtistVO> artists){
    	if(threads > 0){
    		executor.execute(new LetterPageGenerator(letter, artists, upload));
    	} else {
    		PageGenerator.generateLetterPage(letter, artists, upload);
    	}
    }
    
    private static void generateArtistPage(ArtistVO artistVO, List<ArtistVO> artists, List<MusicVO> musics){    	
    	if(threads > 0){
    		executor.execute(new ArtistPageGenerator(artistVO, artists, musics, upload));
    	} else {
    		PageGenerator.generateArtistPage(artistVO, artists, musics, upload);
    	}
    }
    
    private static void generateMusicPage(MusicVO musicVO, List<ArtistVO> artists, List<MusicVO> musics){
    	if(threads > 0){
    		executor.execute(new MusicPageGenerator(musicVO, artists, musics, upload));
    	} else {
    		PageGenerator.generateMusicPage(musicVO, artists, musics, upload);
    	}
    }
    
    private static void generateTwitter(){
        List<Status> statuses = TwitterTimeline.getMixedFriendsMentionsStatus();
        PageGenerator.generateTwitter(statuses, upload);
    }
}
