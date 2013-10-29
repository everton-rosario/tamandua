package br.com.tamandua.generator.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import twitter4j.Status;
import br.com.tamandua.generator.vo.TopArtistVO;
import br.com.tamandua.generator.vo.TopMusicVO;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.MusicVO;

public class PageGenerator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PageGenerator.class);

	private static final String VELOCITY_TEMPLATE_HOME = GeneratorProperties.getInstance().getVelocityTemplateHome();
	private static final String VELOCITY_TEMPLATE_TOP_ARTISTS = GeneratorProperties.getInstance().getVelocityTemplateTopArtists();
	private static final String VELOCITY_TEMPLATE_TOP_MUSICS = GeneratorProperties.getInstance().getVelocityTemplateTopMusics();
	private static final String VELOCITY_TEMPLATE_CLOUD_ARTIST = GeneratorProperties.getInstance().getVelocityTemplateCloudArtist();
	private static final String VELOCITY_TEMPLATE_CLOUD_MUSIC = GeneratorProperties.getInstance().getVelocityTemplateCloudMusic();
	//private static final String VELOCITY_TEMPLATE_FRONT = GeneratorProperties.getInstance().getVelocityTemplateFront();
	private static final String VELOCITY_TEMPLATE_AZ = GeneratorProperties.getInstance().getVelocityTemplateAZ();
	private static final String VELOCITY_TEMPLATE_LETTER = GeneratorProperties.getInstance().getVelocityTemplateLetter();
	private static final String VELOCITY_TEMPLATE_ARTIST = GeneratorProperties.getInstance().getVelocityTemplateArtist();
	private static final String VELOCITY_TEMPLATE_LYRIC = GeneratorProperties.getInstance().getVelocityTemplateLyric();
	private static final String VELOCITY_TEMPLATE_TWITTER = GeneratorProperties.getInstance().getVelocityTemplateTwitter();

	private static final String LETTER_DIR = GeneratorProperties.getInstance().getLetterDir();
	private static final String ARTIST_DIR = GeneratorProperties.getInstance().getArtistDir();
	private static final String MUSIC_DIR = GeneratorProperties.getInstance().getMusicDir();
	private static final String INCLUDE_DIR = GeneratorProperties.getInstance().getIncludeDir();
	private static final String TOP_CONTENT_DIR = GeneratorProperties.getInstance().getTopContentDir();
	
	private static final int TOP_HOME_COUNT = GeneratorProperties.getInstance().getTopHomeCount();
	private static final int TOP_COUNT = GeneratorProperties.getInstance().getTopCount();
	private static final int CLOUD_COUNT = GeneratorProperties.getInstance().getCloudCount();
	
	private static final String HOME_FILE = GeneratorProperties.getInstance().getHomeFile();
	private static final String TOP_ARTISTS_FILE = GeneratorProperties.getInstance().getTopArtistFile();
	private static final String TOP_MUSICS_FILE = GeneratorProperties.getInstance().getTopMusicsFile();
	private static final String CLOUD_ARTIST_FILE = GeneratorProperties.getInstance().getCloudArtistFile();
	private static final String CLOUD_MUSIC_FILE = GeneratorProperties.getInstance().getCloudMusicFile();
	private static final String AZ_FILE = GeneratorProperties.getInstance().getAZFile();
	
	public static void generateHomePage(List<TopArtistVO> topArtists, List<TopMusicVO> topMusics, List<Status> twitterStatus, boolean upload){
		LOGGER.debug("Gerando a pagina home...");
		Map<String, Object> context = new HashMap<String, Object>();
        context.put("topArtists", (topArtists.size()<=TOP_HOME_COUNT) ? topArtists : topArtists.subList(0, TOP_HOME_COUNT));
        context.put("topMusics", (topMusics.size()<=TOP_HOME_COUNT) ? topMusics : topMusics.subList(0, TOP_HOME_COUNT));
        context.put("tweets", twitterStatus);

        StringWriter html = VelocityGenerator.getInstance().getHtml(VELOCITY_TEMPLATE_HOME, context);
        FileGenerator.createHtmlFile("", HOME_FILE, html.toString(), false, upload);
        LOGGER.debug("Pagina home gerada com sucesso...");
	}
	
	public static void generateTopArtistsPage(List<TopArtistVO> topArtists, boolean upload){
		LOGGER.debug("Gerando a pagina com o top de artistas...");
		Map<String, Object> context = new HashMap<String, Object>();
        context.put("topArtists", (topArtists.size()<=TOP_COUNT) ? topArtists : topArtists.subList(0, TOP_COUNT));
        
        StringWriter html = VelocityGenerator.getInstance().getHtml(VELOCITY_TEMPLATE_TOP_ARTISTS, context);
        FileGenerator.createIncludeFile(TOP_CONTENT_DIR, TOP_ARTISTS_FILE, html.toString(), false, upload);
        LOGGER.debug("Pagina com o top de artistas gerada com sucesso...");
	}
	
	public static void generateTopMusicsPage(List<TopMusicVO> topMusics, boolean upload){
		LOGGER.debug("Gerando a pagina com o top de musicas...");
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("topMusics", (topMusics.size()<=TOP_COUNT) ? topMusics : topMusics.subList(0, TOP_COUNT));
        
        StringWriter html = VelocityGenerator.getInstance().getHtml(VELOCITY_TEMPLATE_TOP_MUSICS, context);
        FileGenerator.createIncludeFile(TOP_CONTENT_DIR, TOP_MUSICS_FILE, html.toString(), false, upload);
        LOGGER.debug("Pagina com o top de musicas gerada com sucesso...");
	}
	
	public static void generateCloudArtistPage(List<TopArtistVO> topArtists, boolean upload){
		LOGGER.debug("Gerando o trecho html com a nuvem de artistas...");
		topArtists = (topArtists.size()<=CLOUD_COUNT) ? topArtists : topArtists.subList(0, CLOUD_COUNT);
		Map<String, Object> context = new HashMap<String, Object>();
		Collections.sort(topArtists, new Comparator<TopArtistVO>(){										
										public int compare(TopArtistVO topArtist1, TopArtistVO topArtist2) {
											return topArtist1.getName().compareTo(topArtist2.getName());
										}										
									});		
        context.put("topArtists", topArtists);
        
        StringWriter html = VelocityGenerator.getInstance().getHtml(VELOCITY_TEMPLATE_CLOUD_ARTIST, context);
        FileGenerator.createIncludeFile(INCLUDE_DIR, CLOUD_ARTIST_FILE, html.toString(), false, upload);
        LOGGER.debug("Trecho html com a nuvem de artistas gerado com sucesso...");
	}
	
	public static void generateCloudMusicPage(List<TopMusicVO> topMusics, boolean upload){
		LOGGER.debug("Gerando o trecho html com a nuvem de musicas...");
		topMusics = (topMusics.size()<=CLOUD_COUNT) ? topMusics : topMusics.subList(0, CLOUD_COUNT);
		Map<String, Object> context = new HashMap<String, Object>();
		Collections.sort(topMusics, new Comparator<TopMusicVO>(){										
										public int compare(TopMusicVO topMusic1, TopMusicVO topMusic2) {
											return topMusic1.getTitle().compareTo(topMusic2.getTitle());
										}										
									});		
        context.put("topMusics", topMusics);
        
        StringWriter html = VelocityGenerator.getInstance().getHtml(VELOCITY_TEMPLATE_CLOUD_MUSIC, context);
        FileGenerator.createIncludeFile(INCLUDE_DIR, CLOUD_MUSIC_FILE, html.toString(), false, upload);
        LOGGER.debug("Trecho html com a nuvem de musicas gerado com sucesso...");
	}
	
	public static void generateAZPage(List<Map<String, String>> lettersMap, boolean upload){
		LOGGER.debug("Gerando o trecho html com a barra dos links das letras...");
    	Map<String, Object> context = new HashMap<String, Object>();
        context.put("lettersMap", lettersMap);

        StringWriter azHtml = VelocityGenerator.getInstance().getHtml(VELOCITY_TEMPLATE_AZ, context);
        FileGenerator.createIncludeFile(INCLUDE_DIR, AZ_FILE, azHtml.toString(), false, upload);
        LOGGER.debug("Trecho html com a barra dos links das letras gerado com sucesso...");
	}
	
	public static void generateLetterPage(String letter, List<ArtistVO> artists, boolean upload){		
    	Map<String, Object> context = new HashMap<String, Object>();
        context.put("type", "letter");
        context.put("letter", letter.toUpperCase());
        context.put("artists", artists);
        
        LOGGER.debug("Gerando a pagina html dos artistas que comecam com a letra '"+letter+"'...");
        //StringWriter frontHtml = VelocityGenerator.getInstance().getHtml(VELOCITY_TEMPLATE_FRONT, context);
        StringWriter letterHtml = VelocityGenerator.getInstance().getHtml(VELOCITY_TEMPLATE_LETTER, context);
        LOGGER.debug("Pagina html dos artistas que comecam com a letra '"+letter+"' gerada com sucesso!");
        
        LOGGER.debug("Salvando o arquivo html dos artistas que comecam com a letra '"+letter+"'...");
        //FileGenerator.createHtmlFile(LETTER_DIR, letter.toLowerCase(), frontHtml.toString(), upload);
        FileGenerator.createIncludeFile(LETTER_DIR, letter.toLowerCase(), letterHtml.toString(), true, upload);
        LOGGER.debug("Arquivo html dos artistas que comecam com a letra '"+letter+"' salvo com sucesso!");
	}
	
	public static void generateArtistPage(ArtistVO artistVO, List<ArtistVO> artists, List<MusicVO> musics, boolean upload){		
    	Map<String, Object> context = new HashMap<String, Object>();
        context.put("type", "artist");
        context.put("artist", artistVO);
        context.put("artists", artists);
        context.put("musics", musics);
        context.put("letter", ""+artistVO.getLetters().charAt(0));

        String artist = artistVO.getUri().replaceAll("/", "");
        
        LOGGER.debug("Gerando a pagina html das musicas do artista '"+artist+"'...");
        //StringWriter frontHtml = VelocityGenerator.getInstance().getHtml(VELOCITY_TEMPLATE_FRONT, context);
        StringWriter artistHtml = VelocityGenerator.getInstance().getHtml(VELOCITY_TEMPLATE_ARTIST, context);
        LOGGER.debug("Pagina html das musicas do artista '"+artist+"' gerada com sucesso!");
        
        LOGGER.debug("Salvando o arquivo html das musicas do artista '"+artist+"'...");
        //FileGenerator.createHtmlFile(ARTIST_DIR, artist, frontHtml.toString(), upload);
        FileGenerator.createIncludeFile(ARTIST_DIR, artist, artistHtml.toString(), true, upload);
        LOGGER.debug("Arquivo html das musicas do artista '"+artist+"' salvo com sucesso!");
	}
	
	public static void generateMusicPage(MusicVO musicVO, List<ArtistVO> artists, List<MusicVO> musics, boolean upload){		
    	Map<String, Object> context = new HashMap<String, Object>();
        context.put("type", "lyric");
        context.put("music", musicVO);
        context.put("artists", artists);
        context.put("musics", musics);
        context.put("letter", ""+musicVO.getArtist().getLetters().charAt(0));

        String[] musicUri = musicVO.getUri().split("/");
        if(musicUri.length == 3){
            String artist = musicUri[1];
            String music = musicUri[2];

	        LOGGER.debug("Gerando a pagina html da musica '"+music+"'...");
	        //StringWriter frontHtml = VelocityGenerator.getInstance().getHtml(VELOCITY_TEMPLATE_FRONT, context);
	        StringWriter lyricHtml = VelocityGenerator.getInstance().getHtml(VELOCITY_TEMPLATE_LYRIC, context);
	        LOGGER.debug("Pagina html da musica '"+music+"' gerada com sucesso!");
	        
	        LOGGER.debug("Salvando o arquivo html da musica '"+music+"'...");
	        //FileGenerator.createHtmlFile(MUSIC_DIR + "/" + artist, music, frontHtml.toString(), upload);
	        FileGenerator.createIncludeFile(MUSIC_DIR + "/" + artist, music, lyricHtml.toString(), true, upload);
	        LOGGER.debug("Arquivo html da musica '"+music+"' salvo com sucesso!");
        } else {
        	LOGGER.error("O Uri da musica de id:"+musicVO.getIdMusic()+" esta em um formato invalido!");
        }
	}

    public static void generateTwitter(List<Status> twitterStatus, boolean upload) {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("tweets", twitterStatus);

        LOGGER.debug("Gerando a pagina do twitter...");
        StringWriter twitterContent = VelocityGenerator.getInstance().getHtml(VELOCITY_TEMPLATE_TWITTER, context);
        LOGGER.debug("Pagina do twitter gerada com sucesso!");
            
        LOGGER.debug("Salvando o arquivo do twitter...");
        FileGenerator.createIncludeFile("", "twitter", twitterContent.toString(), true, upload);
        LOGGER.debug("Arquivo do twitter salvo com sucesso!");
        
        Gson gson = new Gson();
        String json = gson.toJson(twitterStatus);
        FileGenerator.createFile("", "twitter.js", json, false, upload);

    }
}
