package br.com.tamandua.view;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.tamandua.service.proxy.ArtistProxy;
import br.com.tamandua.service.proxy.MusicProxy;
import br.com.tamandua.service.proxy.ProxyHelper;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.ImageVO;
import br.com.tamandua.service.vo.LyricVO;
import br.com.tamandua.service.vo.MusicVO;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	public AdminController() {
	}

	@RequestMapping("/artista/{artistUri}.html")
	public String doGetArtist(@PathVariable String artistUri, Model model) {
		model.addAttribute("type", "artist");
		MusicProxy musicProxy = ProxyHelper.getMusicProxy("http://tocaletra.com.br/ws");
		ArtistProxy artistProxy = ProxyHelper.getArtistProxy("http://tocaletra.com.br/ws");
		ArtistVO artistVO = artistProxy.findArtistByURI("/" + artistUri + "/");
		if (artistVO != null) {
			List<MusicVO> musics = musicProxy.findMusicsByArtistId(artistVO.getIdArtist());
			model.addAttribute("artist", artistVO);
			model.addAttribute("musics", musics);
			model.addAttribute("letterArtist", +artistVO.getLetters().charAt(0));
		}
		return "admin/admFront";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/musica/{artistUri}/{musicUri}.html")	
	public String doGetMusic(@PathVariable String artistUri,
			@PathVariable String musicUri, Model model) {
		model.addAttribute("type", "lyric");
		MusicProxy musicProxy = ProxyHelper.getMusicProxy("http://tocaletra.com.br/ws");
		MusicVO musicVO = musicProxy.findMusicByURI("/" + artistUri + "/"+ musicUri);
		
		Set<LyricVO> sortedLyrics = new TreeSet(new Comparator<LyricVO>() {			
			public int compare(LyricVO lyric1, LyricVO lyric2) {
				return lyric2.getIdLyric().compareTo(lyric1.getIdLyric());
			}
		});
		sortedLyrics.addAll(musicVO.getLyrics());
		musicVO.setLyrics(sortedLyrics);		
		
		model.addAttribute("music", musicVO);
		model.addAttribute("letter", + musicVO.getArtist().getLetters().charAt(0));

		return "admin/admFront";
	}

	@RequestMapping("/letra/{letter}.html")
	public String doGetLetter(@PathVariable String letter, Model model) {
		model.addAttribute("type", "letter");
		ArtistProxy artistProxy = ProxyHelper.getArtistProxy("http://tocaletra.com.br/ws");
		model.addAttribute("letter", letter);
		List<ArtistVO> artistsFromLetter = artistProxy.findArtistsByLetter(letter);
		model.addAttribute("artists", artistsFromLetter);
		return "admin/admFront";
	}
	
	@RequestMapping("/artista/upload")
	@Consumes({ProxyHelper.MULTIPART_FORM_DATA_WITH_CHARSET_UTF8})
	public void uploadArtist(@Context HttpServletRequest request, @Context HttpServletResponse response, Model model) throws IOException {
		ArtistProxy artistProxy = ProxyHelper.getArtistProxy("http://tocaletra.com.br/ws");
		
		Map<String, Object> parameters = getParameters(request);		
		String artistUri = (String) parameters.get("artistUri");
		String description = (String) parameters.get("description");	
		FileItem upload = (FileItem) parameters.get("upload");

		ArtistVO artist = new ArtistVO();
		artist.setUri(artistUri);
		artist.setFile(upload.get());
		
		ImageVO image = new ImageVO();
		image.setDescription(description);
		artist.getAllImages().add(image);

		artistProxy.uploadImage(artist);
		
		response.sendRedirect(artistUri.replaceAll("/","")+".html");
	}
	
	public Map<String, Object> getParameters(HttpServletRequest request){
		Map<String, Object> parameters = new HashMap<String, Object>();
		if (ServletFileUpload.isMultipartContent(request)) {
   	    	ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
   	    	List items = null;
   	    	try {
   	    		items = upload.parseRequest(request);
   	    		
   	    		if(items!=null){   	   	    		
   	   	    		Iterator<FileItem> iterator = items.iterator();
   	   	    		while (iterator.hasNext()) {
   	   	    			FileItem item = iterator.next();
   	   	    			if(item.isFormField()) {   	    				
   	   	    				parameters.put(item.getFieldName(), item.getString("UTF-8"));
   	   	    			} else {
   	   	    				parameters.put(item.getFieldName(), item);
   	   	    			}
   	   	    		}
   	   	    	}
   	    	} catch (FileUploadException e) {
   	    		e.printStackTrace();
   	    	} catch (UnsupportedEncodingException e) {
   	    		e.printStackTrace();
			}
   	    }
		return parameters;
	}
}
