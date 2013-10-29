package br.com.tamandua.crawler;

import br.com.tamandua.crawler.domain.Track;

/**
 * @author Everton
 *
 */
public class TrackCrawler {
	
	public Track crawlTrack(Track track, CrawlerHTTP http) {

		// Faz a leitura da cifra
		String cypher = null;
		boolean ok = false;
		while (!ok) {
			try {
				cypher = http.getResponseBody(track.getCypher());
				ok = true;
			} catch (Exception ex) {
				System.err.println("Error getting Cypher Track: " + track.getCypher());
				//ex.printStackTrace();
			}
		}

		// Faz a leitura da letra
		String lyric = null;
		ok = false;
		while (!ok) {
			try {
				lyric = http.getResponseBody(track.getLyric());
				ok = true;
			} catch (Exception ex) {
				System.err.println("Error getting lyric Track: " + track.getLyric());
				//ex.printStackTrace();
			}
		}

		// Faz a leitura da traducao
		String traduction = null;
		ok = false;
		while (!ok) {
			try {
				traduction = http.getResponseBody(track.getTraduction());
				ok = true;
			} catch (Exception ex) {
				System.err.println("Error getting traduction Track: " + track.getTraduction());
				//ex.printStackTrace();
			}
		}

		track.setCypherHtmlBody(cypher);
		track.setLyricHtmlBody(lyric);
		track.setTraductionHtmlBody(traduction);
		return track;
	}

}
