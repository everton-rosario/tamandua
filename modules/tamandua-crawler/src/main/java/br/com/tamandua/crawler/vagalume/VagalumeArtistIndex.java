package br.com.tamandua.crawler.vagalume;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;

import br.com.tamandua.crawler.CrawlerHTTP;
import br.com.tamandua.crawler.IProviderArtistIndex;
import br.com.tamandua.crawler.TrackCrawlerProcessController;
import br.com.tamandua.crawler.domain.Artist;
import br.com.tamandua.crawler.domain.ArtistList;
import br.com.tamandua.crawler.domain.TrackList;
import br.com.tamandua.crawler.domain.xml.IOIndex;
import br.com.tamandua.crawler.parser.TrackReport;

/**
 * Class VagalumeArtistIndex
 *
 * @author Everton Rosario (erosario@gmail.com)
 */
public class VagalumeArtistIndex implements IProviderArtistIndex {
    private static final String PROVIDER = "vagalume";
    private static String letters = "abcdefghijklmnopqrstuvwxyz0";

    /* (non-Javadoc)
     * @see br.com.tamandua.crawler.vagalume.IProviderArtistIndex#crawlLetter(java.lang.String)
     */
    public ArtistList crawlLetter(String letter, CrawlerHTTP http) throws IOException {
        ArtistList letterArtists = new ArtistList(letter, PROVIDER);
        String responseBody = http.getResponseBody("/browse/"+letter.toLowerCase()+".html");
        AZVagalumeParser parser = new AZVagalumeParser(responseBody);
        parser.fillArtistIndex(letterArtists);
        return letterArtists;
    }
    
    /* (non-Javadoc)
     * @see br.com.tamandua.crawler.vagalume.IProviderArtistIndex#crawlAllArtistLetters(java.lang.String)
     */
    public int crawlAllArtistLetters(String indicatedLettersToCrawl, CrawlerHTTP http) throws JAXBException, IOException {
        long start = System.currentTimeMillis();
        String lettersToCrawl = StringUtils.isEmpty(indicatedLettersToCrawl) ? letters : indicatedLettersToCrawl;

        int totalArtists = 0;
        for (int i = 0; i < lettersToCrawl.length(); i++) {
            String letter = "" + lettersToCrawl.charAt(i);
            ArtistList artistList = crawlLetter(letter, http);
            IOIndex.writeLetter(artistList);
            totalArtists += artistList.getTotalArtists();
        }
        long end = System.currentTimeMillis();
        System.out.println("Vagalume.crawlAllLetters took ["+((end - start)/1000)+"] seconds.");
        return totalArtists;
    }

    /* (non-Javadoc)
     * @see br.com.tamandua.crawler.vagalume.IProviderArtistIndex#summarizeLetters()
     */
    public ArtistList summarizeLetters(String type) throws JAXBException {
        long start = System.currentTimeMillis();
        ArtistList fullList = new ArtistList("FULL", PROVIDER);
        for (int i = 0; i < letters.length(); i++) {
            ArtistList artistList = IOIndex.readLetter("" + letters.charAt(i), PROVIDER, type);
            fullList.getArtists().addAll(artistList.getArtists());
        }
        IOIndex.writeLetter(fullList);
        long end = System.currentTimeMillis();
        System.out.println("Vagalume.summarizeLetters took ["+(end - start)+"] miliseconds.");
        return fullList;
    }

    /* (non-Javadoc)
     * @see br.com.tamandua.crawler.vagalume.IProviderArtistIndex#loadSummarizedLetters(java.lang.String)
     */
    public ArtistList loadSummarizedLetters(String lettersToUse, String type) throws JAXBException {
        long start = System.currentTimeMillis();

        ArtistList artistList = null;
        // Realiza a operacao padrao de obter todas as letras
        if (StringUtils.isEmpty(lettersToUse)) {
            artistList = IOIndex.readLetter("full", PROVIDER, type);

        // Caso contrario realiza a leitura de cada uma das letras informadas e as carrega em memoria
        } else {
            artistList = new ArtistList();
            for (int i = 0; i < lettersToUse.length(); i++) {
                ArtistList letterList = IOIndex.readLetter("" + lettersToUse.charAt(i), PROVIDER, type);
                artistList.getArtists().addAll(letterList.getArtists());
            }
        }
        
        long end = System.currentTimeMillis();
        System.out.println("Vagalume.loadSummarizedLetters took ["+(end - start)+"] miliseconds.");
        return artistList;
    }

    /* (non-Javadoc)
     * @see br.com.tamandua.crawler.vagalume.IProviderArtistIndex#crawlTracksIndexFromArtists(br.com.tamandua.crawler.domain.ArtistList)
     */
    public void crawlTracksIndexFromArtists(ArtistList artistList) throws JAXBException, IOException, InterruptedException {
        CrawlerHTTP http = new CrawlerHTTP(PROVIDER);
    	long start = System.currentTimeMillis();
    	int totalTracks = 0;
    	int artistCount = 0;
    	int artistStartCount = 0;
    	for (Artist artist : artistList.getArtists()) {
    		System.out.println("Crawling tracks from artist [" + artist.getUrl() + "]");
    		TrackList trackList = crawlTracksIndexFromArtist(artist, http);
    		IOIndex.writeTrackIndexFromArtist(trackList);
    		System.out.println("Crawled [" + trackList.getTotalTracks() + "]tracks from artist [" + artist.getUrl() + "]");
    		totalTracks += trackList.getTotalTracks();
    		artistStartCount++;
    	    artistCount++;
		}
    	long end = System.currentTimeMillis();
    	System.out.println("Vagalume.crawlTracksIndexFromArtists took ["+(end - start)+"] miliseconds.");
    	System.out.println("Indices de faixas com total de [" + totalTracks + "] faixas.");
    	System.out.println("Crawleado ["+artistStartCount+"] artistas");
    }

    /**
     * Faz o parse do conte�do das musicas de um artista 
     * @param artist Artista que ser� "crawleado"
     * @return
     * @throws IOException 
     * @throws InterruptedException 
     */
	private TrackList crawlTracksIndexFromArtist(Artist artist, CrawlerHTTP http) throws IOException, InterruptedException {
        TrackList trackList = new TrackList();
        trackList.setArtist(artist);
        trackList.setProvider(PROVIDER);
        boolean alreadyCrawled = false;
        // Verifica se o arquivo ja existe
        if (trackList.getFile().exists()) {
            try {
                // Tenta carrega-lo, se carregar, considera que j� foi crawleado
                trackList = IOIndex.readTrackIndexFromArtist(artist, PROVIDER);
                alreadyCrawled = true;
                System.out.println("Already crawled tracks from artist [" + artist.getUrl() + "]!");
            } catch (JAXBException e) {
                alreadyCrawled = false;
            }
        }
        
        // Caso n�o tenha sido crawleado, executa o GET.
        if (!alreadyCrawled) {
            boolean ok = false;
            String responseBody = null;
            while (!ok) {
                try {
                    responseBody = http.getResponseBody(artist.getUrl());
                    ok = true;
                } catch (Exception ex) {
                    while(!ok) {
                        try {
                            responseBody = http.getResponseBody(artist.getUrl());
                            ok = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            Thread.currentThread().sleep(1000);
                        }
                    }
                }
            }
            if (StringUtils.isNotBlank(responseBody)) {
                AZVagalumeParser parser = new AZVagalumeParser(responseBody);
                parser.fillTrackIndex(trackList);
            }
        }
        return trackList;
	}

	/* (non-Javadoc)
     * @see br.com.tamandua.crawler.vagalume.IProviderArtistIndex#crawlTrackContents(br.com.tamandua.crawler.domain.ArtistList)
     */
	public TrackReport crawlTrackContents(ArtistList fullArtistList, int numberThreads, String type) throws JAXBException, InterruptedException {
		long start = System.currentTimeMillis();
		List<TrackCrawlerProcessController> processes = TrackCrawlerProcessController.getThreads(numberThreads, fullArtistList.getArtists(), PROVIDER, type, false);
		List<Thread> threads = new ArrayList<Thread>();
		for (TrackCrawlerProcessController process : processes) {
			Thread thread = new Thread(process);
			threads.add(thread);
			thread.start();
		}
		for (Thread thread : threads) {
			thread.join();
		}

        TrackReport report = new TrackReport();
        for (TrackCrawlerProcessController process : processes) {
            report.incTotalTracks(process.getTotalProcessedTracks());
            report.incTotalLyrics(process.getTotalLyrics());
            report.incTotalCyphers(process.getTotalCyphers());
            report.incTotalTraductions(process.getTotalTraductions());
        }

		long end = System.currentTimeMillis();
		System.out.println("VagalumeArtistIndex.crawlTrackContents took [" + (end - start) + "] milliseconds.");
		return report;
	}

    @Override
    public TrackReport countTracks(ArtistList fullArtistList, int numberThreads, String type) throws JAXBException, InterruptedException {
        long start = System.currentTimeMillis();
        List<TrackCrawlerProcessController> processes = TrackCrawlerProcessController.getThreads(numberThreads, fullArtistList.getArtists(), PROVIDER, type, true);
        List<Thread> threads = new ArrayList<Thread>();
        for (TrackCrawlerProcessController process : processes) {
            Thread thread = new Thread(process);
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        TrackReport report = new TrackReport();
        for (TrackCrawlerProcessController process : processes) {
            report.incTotalTracks(process.getTotalProcessedTracks());
            report.incTotalLyrics(process.getTotalLyrics());
            report.incTotalCyphers(process.getTotalCyphers());
            report.incTotalTraductions(process.getTotalTraductions());
        }

        long end = System.currentTimeMillis();
        System.out.println("VagalumeArtistIndex.countTracks took [" + (end - start) + "] milliseconds.");
        return report;
    }

    @Override
    public String getAllLetters() {
        return letters;
    }
	
}
