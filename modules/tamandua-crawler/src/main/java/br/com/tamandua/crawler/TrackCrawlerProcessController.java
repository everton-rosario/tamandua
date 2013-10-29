package br.com.tamandua.crawler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;

import br.com.tamandua.crawler.domain.Artist;
import br.com.tamandua.crawler.domain.Track;
import br.com.tamandua.crawler.domain.TrackList;
import br.com.tamandua.crawler.domain.xml.IOIndex;

/**
 * @author Everton Rosario (erosario@gmail.com)
 */
public class TrackCrawlerProcessController implements Runnable {
	private String provider;
	private List<Artist> artists;
	private int totalProcessedTracks = 0;
	private int totalLyrics = 0;
	private int totalCyphers = 0;
	private int totalTraductions = 0;
	private TrackCrawler trackCrawler;
    private boolean justCount;
	private static Set<String> processed = new TreeSet<String>();
	private String type;
	
	// Para tratamento do que ja foi processado
	static {
//		processed.add("a");
//		processed.add("b");
//		processed.add("c");
//		processed.add("e");
//		processed.add("f");
//		processed.add("g");
//		processed.add("h");
//		processed.add("l");
//		processed.add("m");
//		processed.add("n");
//		processed.add("o");
//		processed.add("s");
//		processed.add("x");
	}
	
	/**
	 * @param artists Lista de artistas que será crawleada
	 * @param provider Provider a ser crawleado
	 * @param justCount Indica se somente será feita uma contagem de faixas
	 */
	public TrackCrawlerProcessController(List<Artist> artists, String provider, String type, boolean justCount) {
		this.artists = artists;
		this.trackCrawler = new TrackCrawler();
		this.provider = provider;
		this.justCount = justCount;
		this.type = type;
	}

	@Override
	public void run() {
		int totalTracks = 0;
		CrawlerHTTP http = new CrawlerHTTP(provider);
		for (Artist artist : artists) {
			if (!alreadyProcessed(artist.getLetter())) {
				long startArtist = System.currentTimeMillis();
				try {
					TrackList tracksFromArtist = null;
					try {
						tracksFromArtist = IOIndex.readTrackIndexFromArtist(artist, provider, type);
					} catch (Exception ex) {
						System.out.println(Thread.currentThread().getName() + " Artist CORROMPIDO [" + artist.getUrl() + "] trying to get from BACKUP");
						tracksFromArtist = IOIndex.readTrackIndexFromArtist(artist, provider, TrackList.TYPE_BKP);
					}
					
					boolean hasAlteredSomeTrack = false;
					for (Track track : tracksFromArtist.getTracks()) {
						
						// Faz uma verificacao antes de realizar o get para nao ter pegado antes este conteudo
						if (StringUtils.isEmpty(track.getCypherHtmlBody()) 
						 && StringUtils.isEmpty(track.getLyricHtmlBody()) 
						 && StringUtils.isEmpty(track.getTraductionHtmlBody())
						 && !justCount) {
							
							// Realiza o get, caso nunca tenha obtido o conteudo
							trackCrawler.crawlTrack(track, http);
							hasAlteredSomeTrack = true;
						}
						
						if (StringUtils.isNotEmpty(track.getCypher())) {
						    totalCyphers++;
						}
                        if (StringUtils.isNotEmpty(track.getTraduction())) {
                            totalTraductions++;
                        }
                        if (StringUtils.isNotEmpty(track.getLyric())) {
                            totalLyrics++;
                        }
					}
					totalTracks += tracksFromArtist.getTotalTracks();
					
					// Verifica se foi alterada alguma faixa, caso sim, grava o arquivo senao, nao e necessario reescrever
					if (!hasAlteredSomeTrack) {
						System.out.println(Thread.currentThread().getName() + " Artist [" + artist.getUrl() + "] already processed!" );
					}
					else if (hasAlteredSomeTrack) {
						// Faz uma repeti�ao para nunca ficar sem gravar o arquivo em disco
						boolean recordedFile = false;
						while (!recordedFile) {
							try {
								IOIndex.writeTrackIndexFromArtist(tracksFromArtist, type);
								recordedFile = true;
							} catch (Exception ex) {
								System.err.println("ATENCAO, parece que acabou o espaco em disco, verificar!");
								try {
									Thread.sleep(30000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
						long endArtist = System.currentTimeMillis();
						System.out.println(Thread.currentThread().getName() + " Artist [" + artist.getUrl() + "] with ["+tracksFromArtist.getTotalTracks()+"] tracks processed in [" + ((long) endArtist - startArtist) + "] milliseconds." );
					}
				} catch (JAXBException e) {
					e.printStackTrace();
				}
			}
		}
		this.totalProcessedTracks = totalTracks;
		
	}

	public int getTotalLyrics() {
        return totalLyrics;
    }

    public int getTotalCyphers() {
        return totalCyphers;
    }

    public int getTotalTraductions() {
        return totalTraductions;
    }

    private boolean alreadyProcessed(String letter) {
		return processed.contains(letter);
	}

	public int getTotalProcessedTracks() {
		return totalProcessedTracks;
	}
	
	public static List<TrackCrawlerProcessController> getThreads(int numberOfProcesses, List<Artist> totalArtists, String provider, String type, boolean justCount) {
		List<TrackCrawlerProcessController> processess = new ArrayList<TrackCrawlerProcessController>(numberOfProcesses);
		int totalPerThread = (int) Math.ceil(totalArtists.size() / numberOfProcesses + totalArtists.size() % numberOfProcesses);
		
		Iterator<Artist> it = totalArtists.iterator();
		boolean isToContinue = true;
		List<Artist> slice = new ArrayList<Artist>(totalPerThread);
		int totalInThisThread = 0;
		while(it.hasNext() && isToContinue) {
			slice.add(it.next());
			totalInThisThread++;
			if (totalInThisThread >= totalPerThread || !it.hasNext()) {
				processess.add(new TrackCrawlerProcessController(slice, provider, type, justCount));
				slice = new ArrayList<Artist>(totalPerThread);
				totalInThisThread = 0;
			}
		}
		
		return processess;
	}
}
