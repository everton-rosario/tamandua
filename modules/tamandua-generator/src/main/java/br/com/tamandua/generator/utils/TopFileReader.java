package br.com.tamandua.generator.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import br.com.tamandua.generator.vo.TopArtistVO;
import br.com.tamandua.generator.vo.TopMusicVO;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.MusicVO;

public class TopFileReader {	
	
	public static List<TopArtistVO> getTopArtists(String filePath){
		List<Map<String, Object>> topMap = TopFileReader.getTopMap(filePath);
		List<TopArtistVO> topArtists = new ArrayList<TopArtistVO>();
		
		for(int i =0; i< topMap.size(); i++){
			String artistUrl= (String)topMap.get(i).get("Page");
			Long pageViews = Long.parseLong(topMap.get(i).get("Pageviews") + "");
			
			String[] uri = artistUrl.split("/");			
			if(uri.length > 2 && uri[1].equals("artista")){
				String artistUri = uri[2].replace(".html", "");

				ArtistVO artist = ServiceProxy.getInstance().findArtistByURI(artistUri);
				if(artist != null){
					TopArtistVO topArtist = new TopArtistVO();
					topArtist.setIdArtist(artist.getIdArtist());
					topArtist.setName(artist.getName());
					topArtist.setUri(artist.getUri());				
					topArtist.setPageViews(pageViews);
					topArtists.add(topArtist);
				}
			}
		}
		
		List<TopMusicVO> topMusics = TopFileReader.getTopMusics(filePath);
		for(TopMusicVO topMusic : topMusics){
			Long pageViews = topMusic.getPageViews();
			String artistUri = topMusic.getArtist().getUri();
			
			TopArtistVO topArtist = new TopArtistVO();
			topArtist.setUri(artistUri);

			int index = topArtists.indexOf(topArtist);
			if(index > -1){				
				topArtist = topArtists.get(index);
				topArtist.addPageViews(pageViews);
			} else {
				ArtistVO artist = ServiceProxy.getInstance().findArtistByURI(artistUri.replace("/", ""));
				if(artist != null){
					topArtist = new TopArtistVO();
					topArtist.setIdArtist(artist.getIdArtist());
					topArtist.setName(artist.getName());
					topArtist.setUri(artist.getUri());				
					topArtist.setPageViews(pageViews);
					topArtists.add(topArtist);
				}
			}
		}
		
		Collections.sort(topArtists);
		int position = 1;
		for(TopArtistVO topArtist : topArtists){
			topArtist.setPosition(position);
			position++;
		}

		return topArtists;
	}
	
	public static List<TopMusicVO> getTopMusics(String filePath){
		List<Map<String, Object>> topMap = TopFileReader.getTopMap(filePath);
		List<TopMusicVO> topMusics = new ArrayList<TopMusicVO>();
		int position = 1;
		
		for(int i =0; i< topMap.size(); i++){
			String musictUrl= (String)topMap.get(i).get("Page");
			Long pageViews = Long.parseLong(topMap.get(i).get("Pageviews") + "");
			
			String[] uri = musictUrl.split("/");
									
			if(uri.length > 3 && uri[1].equals("musica")){
				String musicUri = uri[3].replace(".html", "");
				String artistUri = uri[2];
				
				MusicVO music = ServiceProxy.getInstance().findMusicByURI(artistUri, musicUri);
				if(music != null){
					TopMusicVO topMusic = new TopMusicVO();
					topMusic.setUri(music.getUri());
					topMusic.setTitle(music.getTitle());
					topMusic.setArtist(music.getArtist());
					topMusic.setPageViews(pageViews);
					topMusic.setPosition(position);
					position++;
					topMusics.add(topMusic);
				}
			}
		}
		
		Collections.sort(topMusics);
		return topMusics;
	}
	
	
	
	public static List<Map<String, Object>> getTopMap(String filePath){
		File file = null;
		BufferedReader bufRdr = null;
		Map<String, Object> topMap = null;
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		
		try {
			file = new File(filePath);
			bufRdr  = new BufferedReader(new FileReader(file));
			String line = null;
			
			String[][] numbers = new String[500][7];
			
			int row = 0;
			int col = 0;
			while((line = bufRdr.readLine()) != null){
				StringTokenizer st = new StringTokenizer(line,"\t");
				
				while (st.hasMoreTokens()){
					numbers[row][col] = st.nextToken();
					
					switch (col) {
						case 0:{
							topMap = new HashMap<String, Object>();
							topMap.put("Page", numbers[row][col]);
							break;
						}
						case 1:{
							topMap.put("Pageviews", numbers[row][col]);
							break;
						}
						case 2:{
							topMap.put("Unique Pageviews", numbers[row][col]);
							break;
						}
						case 3:{
							topMap.put("Avg Time", numbers[row][col]);
							break;
						}
						case 4:{
							topMap.put("Bounce Rate", numbers[row][col]);
							break;
						}
						case 5:{
							topMap.put("Exit", numbers[row][col]);
							break;
						}
						default:{
							topMap.put("Index", numbers[row][col]);
							listMap.add(topMap);
							break;
						}						
					}					
					col++;
				}
				col = 0;
				row++;
			}
			bufRdr.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listMap;
	}
	
}
