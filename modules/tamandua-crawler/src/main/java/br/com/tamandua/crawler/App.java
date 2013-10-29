package br.com.tamandua.crawler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.tamandua.crawler.domain.Artist;
import br.com.tamandua.crawler.domain.ArtistList;
import br.com.tamandua.crawler.domain.Track;
import br.com.tamandua.crawler.domain.TrackList;
import br.com.tamandua.crawler.domain.xml.IOIndex;
import br.com.tamandua.crawler.domain.xml.TrackPageParser;
import br.com.tamandua.crawler.parser.TrackReport;
import br.com.tamandua.crawler.util.ImageDownloader;
import br.com.tamandua.service.proxy.ArtistProxy;
import br.com.tamandua.service.proxy.ImageProxy;
import br.com.tamandua.service.proxy.MusicProxy;
import br.com.tamandua.service.proxy.ProxyHelper;
import br.com.tamandua.service.util.StringNormalizer;
import br.com.tamandua.service.vo.AlbumVO;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.ImageVO;
import br.com.tamandua.service.vo.LyricVO;
import br.com.tamandua.service.vo.MusicVO;

/**
 * Class App
 *
 * @author Everton Rosario (erosario@gmail.com)
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    // Opcoes existentes para a aplicacao
    private static Options options = getOptions();
    
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
        //args = new String[] { "--load-track-content", "--provider=terra" ,"--letters=a" };
        
        // interpretador de comandos
        CommandLineParser parser = new PosixParser();

        try {
            // Realiza o parse da linha de comando
            CommandLine line = parser.parse( options, args );
            String lettersToUse = ""; 
            String provider = "";
            String threads = "1";

            // Verifica se nao existe alguma inconsistencia com a chamada de prompt de comando
            if (!isValidLine(line) || line.hasOption("help") || line.hasOption("projecthelp")) {
                showUsage();
                return;
            }
            
            // Define que letras utilizar
            if (line.hasOption("letters")) {
                lettersToUse = line.getOptionValue("letters");
                System.out.println("usando letters: " + line.getOptionValue("letters"));
            }

            // Define que letras utilizar
            if (line.hasOption("provider")) {
                provider = line.getOptionValue("provider");
                System.out.println("usando provider: " + line.getOptionValue("provider"));
            }

            // Define que letras utilizar
            if (line.hasOption("threads")) {
                threads = line.getOptionValue("threads");
            }
            System.out.println("usando threads: " + threads);

            // Imprime versao
            if (line.hasOption("version")) {
                showVersion();
            }

            /*
             * Lista de operacoes possiveis para o crawler 
             */
            if (line.hasOption("--crawl-artist-index")) {
                crawlArtistLetterIndexes(lettersToUse, provider);
            }
            if (line.hasOption("--summarize-artist-index")) {
                summarizeArtistIndex(lettersToUse, provider, ArtistList.TYPE_DEFAULT);
            }
            if (line.hasOption("--summarize-artist-index-merged")) {
                summarizeArtistIndex(lettersToUse, provider, ArtistList.TYPE_DEFAULT);
            }
            if (line.hasOption("--crawl-track-index")) {
                crawlTrackIndex(lettersToUse, provider);
            }
            if (line.hasOption("--count-tracks")) {
                countTracks(lettersToUse, provider, Integer.parseInt(threads), ArtistList.TYPE_DEFAULT);
            }
            if (line.hasOption("--merge-track-content")) {
                mergeTrackContents(lettersToUse, provider);
            }
            if (line.hasOption("--crawl-track-content-merge")) {
                crawlTrackContents(lettersToUse, provider, Integer.parseInt(threads), ArtistList.TYPE_MERGE);
            }
            if (line.hasOption("--load-track-content-merge")) {
                loadTrackContents(lettersToUse, provider, ArtistList.TYPE_MERGE);
            }
            if (line.hasOption("--crawl-track-content")) {
                crawlTrackContents(lettersToUse, provider, Integer.parseInt(threads), ArtistList.TYPE_DEFAULT);
            }
            if (line.hasOption("--load-track-content")) {
                loadTrackContents(lettersToUse, provider, ArtistList.TYPE_DEFAULT);
            }
            if (line.hasOption("--load-artist-content")) {
                loadArtistContents(lettersToUse, provider, ArtistList.TYPE_DEFAULT);
            }
            if (line.hasOption("--crawl-load-images")) {
                crawlLoadImages(lettersToUse, provider, ArtistList.TYPE_DEFAULT);
            }
        }
        catch(ParseException exp) {
            showUsage();
        }	
    }

    private static void crawlLoadImages(String lettersToUse, String provider, String type) throws InstantiationException, IllegalAccessException, ClassNotFoundException, JAXBException, IOException {
        // Servico que sera requisitado pelo RESTClient.
        ArtistProxy artistProxy = ProxyHelper.getArtistProxy(CrawlerProperties.getInstance().getWebServiceHost());
        ImageProxy imageProxy = ProxyHelper.getImageProxy(CrawlerProperties.getInstance().getWebServiceHost());

        // Obtem o manipulador de acervo correto de acordo com o provider informado
        IProviderArtistIndex artistIndex = CrawlerProperties.getInstance().getCrawlerProviderArtistIndex(provider);
        ArtistList artistList = artistIndex.loadSummarizedLetters(lettersToUse, type);
        LOGGER.info("Carregado indice de artistas com total de [{}] artistas.", artistList.getTotalArtists());

        // Percorre os artistas
        for (Artist artist : artistList.getArtists()) {
            // Trata artista
            ArtistVO artistVO = artistProxy.findArtistByURI(artist.getUrl());
            
            if (artistVO != null && artistVO.getAllImages() != null) {
                for (ImageVO imageVO : artistVO.getAllImages()) {
                    if (StringUtils.isBlank(imageVO.getFile())) {
                        String path = CrawlerProperties.getInstance().getCrawlerImageFiles();
                        String uri = "";
                        if (artistVO != null) {
                            path += artistVO.getUri(); 
                            uri += artistVO.getUri();
                        } else {
                            path += File.separator + "images" + File.separator;
                            uri += File.separator + "images" + File.separator;
                        }
        
                        String url = imageVO.getUrl();
                        if (!url.startsWith("http://")) {
                            url = CrawlerProperties.getInstance().getCrawlerProviderImageHost(provider) + url;
                        }
        
                        File pathToStore = new File(path);
                        path = path.replace("/", File.separator);
                        pathToStore.mkdirs();
                        path += imageVO.getIdImage() + url.substring(url.lastIndexOf('.'));
                        uri += imageVO.getIdImage() + url.substring(url.lastIndexOf('.'));
                        File fileToStore = new File(path);
                        
                        if (!fileToStore.exists()) {
                            try {
                                LOGGER.info("Getting image[{}] with id_image [{}] to store in file[{}]", new String[] { url, "" + imageVO.getIdImage(), path } );
                                ImageDownloader.saveImageFromUrlToFile(url, fileToStore);
                                
                                imageVO.setFile(path);
                                imageVO.setUrl(uri);
                                BufferedImage image = ImageIO.read(fileToStore);
                                imageVO.setWidth(image.getWidth());
                                imageVO.setHeight(image.getHeight());
            
                                imageProxy.saveImage(imageVO);
                            } catch (Exception ex) {
                                LOGGER.error("Error getting image[{}] with id_image [{}] to store in file[{}]", new String[] { url, "" + imageVO.getIdImage(), path } );
                            }
                        }
                    }
                }
            }
        }
    }


    private static void countTracks(String lettersToUse, String provider, int numberOfThreads, String type) throws InstantiationException, IllegalAccessException, ClassNotFoundException, JAXBException, InterruptedException {
        IProviderArtistIndex artistIndex = CrawlerProperties.getInstance().getCrawlerProviderArtistIndex(provider);
        ArtistList fullArtistList = artistIndex.loadSummarizedLetters(lettersToUse, type);
        System.out.println("Indice FULL de artistas com total de [" + fullArtistList.getTotalArtists() + "] artistas.");
        TrackReport report = artistIndex.countTracks(fullArtistList, numberOfThreads, type);
        System.out.println("Relatorio de faixas: " + report);
    }

    private static void mergeTrackContents(String lettersToUse, String provider) throws InstantiationException, IllegalAccessException, ClassNotFoundException, JAXBException, InterruptedException {
        IProviderArtistIndex artistIndex = CrawlerProperties.getInstance().getCrawlerProviderArtistIndex(provider);
        ArtistList fullArtistList = artistIndex.loadSummarizedLetters(lettersToUse, ArtistList.TYPE_DEFAULT);
        System.out.println("Indice FULL de artistas com total de [" + fullArtistList.getTotalArtists() + "] artistas.");
        
        ArtistList merged = new ArtistList();
        merged.setProvider(provider);
        
        // Percorre todos os artistas crawleados atualmente
        int totalNewTracks = 0;
        for (Artist artist : fullArtistList.getArtists()) {
            TrackList trackList = TrackList.loadFromArtistFile(artist, provider);
            TrackList oldTrackList = TrackList.loadFromArtistOldFile(artist, provider);
            trackList.removeExistingTracks(oldTrackList);
            if (!trackList.getTracks().isEmpty()) {
                totalNewTracks += trackList.getTotalTracks();
                // Nao ficou vazio, entao loga e salva o novo (merged)
                System.out.println("Artista " + artist + " com " + trackList.getTotalTracks() + " musicas novas!");
                IOIndex.writeTrackIndexFromArtist(trackList, TrackList.TYPE_MERGE);
            }
        }
        System.out.println("Total de musicas novas: " + totalNewTracks);
    }

    /**
     * Carrega o conteúdo dos artistas de uma forma mais leve do que percorrer todas as faixas
     * @param lettersToUse
     * @param provider
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws JAXBException 
     * @throws InvocationTargetException 
     * @throws NoSuchMethodException 
     * @throws IllegalArgumentException 
     * @throws SecurityException 
     */
    private static void loadArtistContents(String lettersToUse, String provider, String type) throws InstantiationException, IllegalAccessException, ClassNotFoundException, JAXBException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
        // Servico que sera requisitado pelo RESTClient.
        ArtistProxy artistProxy = ProxyHelper.getArtistProxy(CrawlerProperties.getInstance().getWebServiceHost());

        // Obtem o manipulador de acervo correto de acordo com o provider informado
        IProviderArtistIndex artistIndex = CrawlerProperties.getInstance().getCrawlerProviderArtistIndex(provider);
        ArtistList artistList = artistIndex.loadSummarizedLetters(lettersToUse, type);
        LOGGER.info("Carregado indice de artistas com total de [{}] artistas.", artistList.getTotalArtists());

        // Percorre os artistas
        for (Artist artist : artistList.getArtists()) {
            TrackList trackList = null;
            try {
                trackList = TrackList.loadFromArtistFile(artist, provider);
            } catch (Exception ex) {
                System.err.println("Erro processando arquivo do artista " + artist + " do provider " + provider + " ["+artist.getUrl()+"]");
                continue;
            }
            
            // Carrega uma musica/pagina que contenha dados do artista.
            if (trackList.getTracks() != null && !trackList.getTracks().isEmpty()) {
                
                // Obtem apenas a primeira faixa, para trabalhar com o conteudo do artista apenas.
                Track track = trackList.getTracks().get(0);
                
                TrackPageParser trackParser = CrawlerProperties.getInstance().getCrawlerProviderTrackParser(provider, trackList.getArtist(), track);

                // Trata artista
                ArtistVO artistVO = parseArtistVO(provider, artist, trackParser);
                
                // Realiza o POST para o servico
                long start = System.currentTimeMillis();
                try {
                    artistProxy.saveArtist(artistVO);
                } catch (Exception ex) {
                    LOGGER.error("Erro salvando artist!", ex);
                }
                long total = System.currentTimeMillis() - start;
                LOGGER.info("Carregado artista: [{}] in [{}] miliseconds", artistVO.getUri(), total);
                
            }
        }
        LOGGER.info("Conteudo de [{}] artistas carregados com sucesso!", artistList.getArtists().size());    
    }

    /**
     * Carrega o conteudo das faixas com suas meta-informacoes para o Banco
     * @param lettersToUse
     * @param provider 
     * @throws JAXBException
     * @throws IOException Ao carregar/parsear o conteudo da faixa
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws InvocationTargetException 
     * @throws NoSuchMethodException 
     * @throws IllegalArgumentException 
     * @throws SecurityException 
     */
    private static void loadTrackContents(String lettersToUse, String provider, String type) throws JAXBException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
        
    	// Servico que sera requisitado pelo RESTClient.
        MusicProxy musicProxy = ProxyHelper.getMusicProxy(CrawlerProperties.getInstance().getWebServiceHost());

        // Obtem o manipulador de acervo correto de acordo com o provider informado
        IProviderArtistIndex artistIndex = CrawlerProperties.getInstance().getCrawlerProviderArtistIndex(provider);
        ArtistList artistList = artistIndex.loadSummarizedLetters(lettersToUse, type);
        LOGGER.info("Carregado indice de artistas com total de [{}] artistas.", artistList.getTotalArtists());
        int totalTracks = 0;
        int startTrack = 0;
        int currentTrack = 0;
        
        // Percorre os artistas
        for (Artist artist : artistList.getArtists()) {
            TrackList trackList = null;
            try {
                trackList = TrackList.loadFromArtistFile(artist, provider, type);
            } catch (Exception ex) {
                System.err.println("Erro processando arquivo do artista " + artist + " do provider " + provider + " ["+artist.getUrl()+"]");
                continue;
            }
            totalTracks += trackList.getTotalTracks();
            
            // Carrega cada musica de cada artista
            for (Track track : trackList.getTracks()) {
                currentTrack++;
                
                // Carrega as faixas a partir do indice atual que foi conseguido
                if (currentTrack >= startTrack) {
                    TrackPageParser trackParser = CrawlerProperties.getInstance().getCrawlerProviderTrackParser(provider, trackList.getArtist(), track);
                    
                    ArtistVO artistVO = parseArtistVO(provider, artist, trackParser);
                    
                    // Trata Faixa + Letras
                    MusicVO musicVO = new MusicVO();
                    LyricVO lyricVO = new LyricVO();
                    Set<LyricVO> lyricsVO = new HashSet<LyricVO>();
                    lyricsVO.add(lyricVO);
                    musicVO.setLyrics(lyricsVO);
                    
                    // Adiciona os estilos nas faixas
                    if (trackParser.getStyles() != null) {
    	                for (String styleName : trackParser.getStyles()) {
    						musicVO.addStyle(styleName);
    					}
                    }
    
                    // Adiciona os estilos dos usuarios nas faixas
                    if (trackParser.getFreeStyles() != null) {
                        for (String styleName : trackParser.getFreeStyles()) {
                            musicVO.addFreeStyle(styleName);
                        }
                    }

                    // Adiciona os compositores
                    if (trackParser.getComposers() != null) {
                    	for (String composerName : trackParser.getComposers()) {
                    		musicVO.addComposer(composerName);
    					}
                    }
                    
                    // Dados pertinentes a musica
                    musicVO.setArtist(artistVO);
                    musicVO.setTitle(trackParser.getTrack().getName());
                    if (trackParser.isCypher()) {
                    	musicVO.setUrl(trackParser.getTrack().getCypher());
                    	lyricVO.setLyricType(LyricVO.LYRIC_TYPE_TABLATURE);
                    }
                    if (trackParser.isTraduction()) {
                    	musicVO.setUrl(trackParser.getTrack().getTraduction());
                    	lyricVO.setLyricType(LyricVO.LYRIC_TYPE_TRADUCTION);
                    }
                    if (trackParser.isLyric()) {
                    	musicVO.setUrl(trackParser.getTrack().getLyric());
                    	lyricVO.setLyricType(LyricVO.LYRIC_TYPE_ORIGINAL);
                    }
                    lyricVO.setMusic(musicVO);
                    lyricVO.setUri(musicVO.getUrl());
                    lyricVO.setUrl(musicVO.getUrl());
                    lyricVO.setText(trackParser.getMusicContent());
                    lyricVO.setLyricTitle(musicVO.getTitle());
                    lyricVO.setTotalAccess(0L);
                    lyricVO.setProvider(provider);
                    
                    // Trata Album
                    String albumURI = trackParser.getAlbumURI();
                    String[] albumCovers = trackParser.getAlbumCover();
                    String albumName = trackParser.getAlbumName();
                    AlbumVO albumVO = new AlbumVO();
                    albumVO.setArtist(artistVO);
                    albumVO.setName(albumName);
                    albumVO.setUri(albumURI);
                    if (albumCovers != null && albumCovers.length == 3) {
    	                ImageVO imageCover = new ImageVO();
    	                imageCover.setUri(albumCovers[2]);
    	                imageCover.setProvider(provider);
    					albumVO.setImageByIdCover(imageCover);
                    }
                    // Adiciona o album somente se existir
                    if (StringUtils.isNotBlank(albumURI)) {
                    	musicVO.setAlbum(albumVO);
                    }
                    
                    // Realiza o POST para o servico
                    long start = System.currentTimeMillis();
                    try {
                        musicProxy.saveMusic(musicVO);
                    } catch (Exception ex) {
                        LOGGER.error("Erro salvando faixa!", ex);
                        IOIndex.writeErrorMusic(musicVO, CrawlerProperties.getInstance().getErrorLoadDirectory());
                    }
                    long total = System.currentTimeMillis() - start;
                    LOGGER.info("Carregada faixa: [{}] in [{}] miliseconds", musicVO.getUrl(), total);
                    
//                    JAXBContext context = JAXBContext.newInstance(MusicVO.class);
//                    Marshaller marshaller = context.createMarshaller();
//                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//                    StringWriter strWriter = new StringWriter();
//                    marshaller.marshal(musicVO, strWriter);
//                    System.out.println(strWriter.toString());
                }
            }
        }
        LOGGER.info("Conteudo de faixas com total de [{}] musicas carregada com sucesso!", totalTracks);
    }


    private static ArtistVO parseArtistVO(String provider, Artist artist, TrackPageParser trackParser) {
        Set<ImageVO> allImages = new HashSet<ImageVO>();
        
        // Trata Imagem do Artista
        List<String> artistImages = trackParser.getArtistImages();
        if (artistImages != null && !artistImages.isEmpty()) {
            for (String uri : artistImages) {
                if (!uri.endsWith(trackParser.getNoImageToIgnore())) {
                    if (!uri.startsWith("http://")) {
                        uri = trackParser.getImageHost() + uri;
                    }
                    ImageVO img = new ImageVO();
                    img.setUri(uri);
                    img.setUrl(uri);
                    img.setProvider(provider);
                    allImages.add(img);
                }
            }
        }
        
        // Trata Artista
        ArtistVO artistVO = new ArtistVO();
        artistVO.setLetters(artist.getLetter());
        artistVO.setUri(trackParser.getArtist().getUrl());
        artistVO.setName(trackParser.getArtist().getName());
        artistVO.setSortName(StringNormalizer.normalizeArtistName(artistVO.getName()));
        artistVO.setFlag_moderate(ArtistVO.FLAG_NO_MODERATE);
        artistVO.setFlag_public(ArtistVO.FLAG_PUBLIC);
        if (allImages != null && !allImages.isEmpty()) {
            artistVO.setAllImages(allImages);
        }
        return artistVO;
    }

    /**
     * Faz o crawl do conteudo das faixas
     * @param lettersToUse Letras utilizadas para realizacao da operacao
     * @param provider 
     * @param threads 
     * @throws JAXBException
     * @throws InterruptedException
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    private static void crawlTrackContents(String lettersToUse, String provider, int threads, String type) throws JAXBException, InterruptedException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        IProviderArtistIndex artistIndex = CrawlerProperties.getInstance().getCrawlerProviderArtistIndex(provider);
        ArtistList fullArtistList = artistIndex.loadSummarizedLetters(lettersToUse, type);
        System.out.println("Indice FULL de artistas com total de [" + fullArtistList.getTotalArtists() + "] artistas.");
        TrackReport report = artistIndex.crawlTrackContents(fullArtistList, threads, type);
        System.out.println("Relatorio de faixas: " + report);
    }

    /**
     * Faz o cralwing dos artistas, obtendo um indice de faixas 
     * @param lettersToUse Letras utilizadas para realizacao da operacao
     * @param provider 
     * @throws JAXBException
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws InterruptedException 
     * @throws IOException 
     */
    private static void crawlTrackIndex(String lettersToUse, String provider) throws JAXBException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, InterruptedException {
        // 
        IProviderArtistIndex artistIndex = CrawlerProperties.getInstance().getCrawlerProviderArtistIndex(provider);
        ArtistList fullArtistList = artistIndex.loadSummarizedLetters(lettersToUse, ArtistList.TYPE_DEFAULT);
        System.out.println("Indice FULL de artistas com total de [" + fullArtistList.getTotalArtists() + "] artistas.");
        artistIndex.crawlTracksIndexFromArtists(fullArtistList);
    }

    /**
     * Cria a o indice sumarizado
     * @param lettersToUse Letras utilizadas para realizacao da operacao
     * @param provider 
     * @throws JAXBException
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    private static void summarizeArtistIndex(String lettersToUse, String provider, String type) throws JAXBException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        IProviderArtistIndex artistIndex = CrawlerProperties.getInstance().getCrawlerProviderArtistIndex(provider);
        ArtistList fullArtistList = artistIndex.summarizeLetters(type);
        System.out.println("Indice FULL de artistas com total de [" + fullArtistList.getTotalArtists() + "] artistas.");
        Set<String> uniqueUrls = fullArtistList.getUniqueUrls();
        System.out.println("Indice FULL de artistas com total de [" + uniqueUrls.size() + "] URLs.");
    }

    /**
     * Exibe a versao do sistema
     */
    private static void showVersion() {
        System.out.println("tamandua version: 1.0.0-SNAPSHOT.");
    }

    /**
     * Faz o crawling de todas as letras(Alfabeto), buscando os artistas existentes.
     * @param provider 
     * @throws JAXBException
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws IOException 
     */
    private static void crawlArtistLetterIndexes(String letters, String provider) throws JAXBException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
        IProviderArtistIndex artistIndex = CrawlerProperties.getInstance().getCrawlerProviderArtistIndex(provider);
        int totalArtists = artistIndex.crawlAllArtistLetters(letters, new CrawlerHTTP(provider));
        System.out.println("Indice de artistas com um total de [" + totalArtists + "] artistas.");
    }

    /**
     * Verificacao se os parametros informados sao validos individualmente utilizando obrigatoriamente o "PROVIDER"
     * @param line Linha parseada do commons-cli
     * @return Se a linha esta valida (true) ou invalida(false)
     */
    private static boolean isValidLine(CommandLine line) {
        if ((line.hasOption("help") || 
             line.hasOption("projecthelp") ||
             line.hasOption("version") ||
             line.hasOption("crawl-artist-index") ||
             line.hasOption("summarize-artist-index") ||
             line.hasOption("crawl-track-index") ||
             line.hasOption("count-tracks") ||
             line.hasOption("crawl-track-content") ||
             line.hasOption("load-track-content") ||
             line.hasOption("crawl-track-content-merge") ||
             line.hasOption("load-track-content-merge") ||
             line.hasOption("load-artist-content") ||
             line.hasOption("merge-track-content") ||
             line.hasOption("crawl-load-images")) && line.hasOption("provider")) {
            
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
        formatter.printHelp( "tamandua", options );
    }

    /**
     * @return opcoes utilizadas pelo sistema 
     */
    private static Options getOptions() {
        // create the Options
        Options options = new Options();

        Option help        = new Option( "help", "print this message" );
        Option projecthelp = new Option( "projecthelp", "print project help information" );
        Option version     = new Option( "version", "print the version information and exit" );
        
//        Option quiet       = new Option( "quiet", "be extra quiet" );
//        Option verbose     = new Option( "verbose", "be extra verbose" );
//        Option debug       = new Option( "debug", "print debugging information" );
//        Option emacs       = new Option( "emacs", "produce logging information without adornments" );
//
//        // Opcoes 
//        Option logfile = OptionBuilder.withArgName("file").hasArg()
//                                      .withDescription("use given file for log")
//                                      .create("logfile");
//        Option logger = OptionBuilder.withArgName("classname")
//                                     .hasArg()
//                                     .withDescription("the class which it to perform " + "logging")
//                                     .create("logger");
//        Option listener = OptionBuilder.withArgName("classname")
//                                       .hasArg()
//                                       .withDescription("add an instance of class as a project listener")
//                                       .create("listener");
//        Option buildfile = OptionBuilder.withArgName("file")
//                                        .hasArg()
//                                        .withDescription("use given buildfile")
//                                        .create("buildfile");
//        Option find = OptionBuilder.withArgName("file")
//                                   .hasArg()
//                                   .withDescription("search for buildfile towards the root of the filesystem and use it")
//                                   .create("find");
//
//        Option property = OptionBuilder.withArgName("property=value")
//                                       .hasArgs(2)
//                                       .withValueSeparator()
//                                       .withDescription("use value for given property")
//                                       .create("D");

        Option usedletters = OptionBuilder.withLongOpt( "letters" )
                                          .withDescription( "Letras a serem utilizadas" )
                                          .hasArg()
                                          .withArgName("[a-z0]")
                                          .create();
        Option provider = OptionBuilder.withLongOpt( "provider" )
                                       .withDescription( "Provider a ser utilizado" )
                                       .hasArg()
                                       .withArgName("PROVIDER")
                                       .create();
        Option threads = OptionBuilder.withLongOpt( "threads" )
                                      .withDescription( "Numero de threads do processo. Quanto mais aguentar mais rápido o processamento e mais memória." )
                                      .hasArg()
                                      .withArgName("NUMBER_OF_THREADS")
                                      .create();
        Option crawlArtistIndex = OptionBuilder.withLongOpt( "crawl-artist-index" )
                                          .withDescription( "crawl the artist index letters from web")
                                          .create();
        Option summarizeArtistIndex = OptionBuilder.withLongOpt( "summarize-artist-index" )
                                                   .withDescription( "summarizes the artist index letters from disc")
                                                   .create();
        Option crawlTrackIndex = OptionBuilder.withLongOpt( "crawl-track-index" )
                                              .withDescription( "crawl all track index from web")
                                              .create();
        Option countTracks = OptionBuilder.withLongOpt( "count-tracks" )
                                          .withDescription( "Show the total number of tracks")
                                          .create();
        Option crawlTrackContent = OptionBuilder.withLongOpt( "crawl-track-content" )
                                                .withDescription( "crawl the track content from web")
                                                .create();
        Option loadTrackContent = OptionBuilder.withLongOpt( "load-track-content" )
                                               .withDescription( "loads the track contents to the database")
                                               .create();
        Option crawlTrackContentMerge = OptionBuilder.withLongOpt( "crawl-track-content-merge" )
                                                     .withDescription( "crawl the track content from web")
                                                     .create();
        Option loadTrackContentMerge = OptionBuilder.withLongOpt( "load-track-content-merge" )
                                                    .withDescription( "loads the track contents to the database")
                                                    .create();
        Option loadArtistContent = OptionBuilder.withLongOpt( "load-artist-content" )
                                                .withDescription( "loads the artist contents to the database")
                                                .create();
        Option mergeTrackContent = OptionBuilder.withLongOpt( "merge-track-content" )
                                                .withDescription( "merges two crawled indexes to a merged configured folder")
                                                .create();
        
        Option crawlLoadImages = OptionBuilder.withLongOpt( "crawl-load-images" )
                                              .withDescription( "loads the artist contents to the database")
                                              .create();
        
        // Faz o bind das opcoes criadas ao conjunto de instrucoes aceitas
        options.addOption(help);
        options.addOption(projecthelp);
        options.addOption(version);
        options.addOption(crawlArtistIndex);
        options.addOption(summarizeArtistIndex);
        options.addOption(crawlTrackIndex);
        options.addOption(countTracks);
        options.addOption(crawlTrackContent);
        options.addOption(loadTrackContent);
        options.addOption(loadArtistContent);
        options.addOption(crawlLoadImages);
        options.addOption(mergeTrackContent);
        options.addOption(crawlTrackContentMerge);
        options.addOption(loadTrackContentMerge);

        options.addOption(usedletters);
        options.addOption(provider);
        options.addOption(threads);
        
//        options.addOption(quiet);
//        options.addOption(verbose);
//        options.addOption(debug);
//        options.addOption(emacs);
//        options.addOption(logfile);
//        options.addOption(logger);
//        options.addOption(listener);
//        options.addOption(buildfile);
//        options.addOption(find);
//        options.addOption(property);
        
        return options;
    }
    
    

}
