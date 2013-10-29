package br.com.tamandua.redirect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.tamandua.generator.utils.GeneratorProperties;
import br.com.tamandua.generator.utils.MediaHash;
import br.com.tamandua.generator.utils.VelocityGenerator;
import br.com.tamandua.redirect.utils.PagesSource;
import br.com.tamandua.redirect.utils.RedirectProperties;
import br.com.tamandua.service.proxy.ProxyHelper;
import br.com.tamandua.service.proxy.SearchProxy;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.ImageVO;
import br.com.tamandua.service.vo.PageResultVO;
import br.com.tamandua.service.vo.ResultItemVO;

public class PageRedirectServlet extends HttpServlet {

	private static final long serialVersionUID = -8164026321836379373L;
    private static final long LAST_MODIFIED = System.currentTimeMillis();
    private static Logger LOGGER = LoggerFactory.getLogger(PageRedirectServlet.class);
    private static final int FAIL_COUNTER = 100;
    private static long tries = 0;
    private RedirectProperties redirectProperties = RedirectProperties.getInstance();
    
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	    
		String contextURI = req.getRequestURI().replace(req.getContextPath(), "");
		String contextURIOriginal = contextURI;
		String noExtensionURI = contextURI;
        //if(redirectProperties.isUsingTemplate()){
		noExtensionURI = noExtensionURI.replaceAll("\\.inc", "");
        noExtensionURI = noExtensionURI.replaceAll("\\.html", "");
		//}

        boolean isLetterPage = noExtensionURI.startsWith("/letra");
        boolean isArtistPage = noExtensionURI.startsWith("/artista");
		boolean isMusicPage = noExtensionURI.startsWith("/musica");
		boolean isSearchPage = noExtensionURI.startsWith("/busca");
		boolean isPage = noExtensionURI.startsWith("/pages");
		boolean isContentPage = noExtensionURI.startsWith("/pages/conteudo");

		PrintWriter out = null;
		BufferedReader in = null;
		try {
			resp.setContentType("text/html");
			resp.setCharacterEncoding("UTF-8");
			resp.setDateHeader("Last-Modified", LAST_MODIFIED);
			out = resp.getWriter();
			
			String pagesSource = redirectProperties.getPagesSource();
			StringBuilder response = new StringBuilder();
			String idtRadioTrack = null;
			ArtistVO artistVO = null;
			String artistName = null;
			String artistNameNormalized = null;
			String musicNameNormalized = null;
			
			if(!isSearchPage){		        
				String[] contextURISplited = contextURI.split("/");
				String fileName = contextURISplited[contextURISplited.length-1];
				if(isLetterPage || isArtistPage || isMusicPage || isContentPage){
		        	contextURI = contextURI.replaceAll("\\.html", ".inc");
		        	fileName = fileName.replaceAll("\\.html", ".inc");
				}

				StringBuffer mediaFolder = new StringBuffer("/");
		        if(isLetterPage || isArtistPage || isMusicPage){
		        	mediaFolder = MediaHash.getMediaFolder(contextURI);
		        } else if(isPage) {
					String[] noExtensionURISplited = noExtensionURI.split("/");
		        	String folderName = noExtensionURI.replace("/pages", "").replace(noExtensionURISplited[noExtensionURISplited.length-1], "");
		        	mediaFolder = new StringBuffer(folderName);
		        }
			
				if(pagesSource.equals(PagesSource.LOCAL.name())){
				    //LOGGER.info("Using LOCAL served files...");
					String filePath = redirectProperties.getPathFiles()+mediaFolder+fileName;
					File file = new File(filePath);
	                FileInputStream fis = new FileInputStream(file);
	                resp.setDateHeader("Last-Modified", file.lastModified());
					in = new BufferedReader(new InputStreamReader(fis, "UTF-8"));				
	                String inputLine;
	                while ((inputLine = in.readLine()) != null){
	                    response.append(inputLine);
	                }
				} else if(pagesSource.equals(PagesSource.S3.name())){
					String spec = redirectProperties.getUrlRedirect()+mediaFolder.toString().replace('\\', '/')+fileName;
					HttpClient httpClient = new HttpClient();
					HttpMethod getMethod = new GetMethod(spec);
	                int responseNumber = httpClient.executeMethod(getMethod); // TODO Fazer tratamento para 404 (Pagina despublicada)
	                String lastMod = getMethod.getResponseHeader("Last-Modified") != null ? getMethod.getResponseHeader("Last-Modified").getValue() : (new Date()).toString();
	                resp.setHeader("Last-Modified", lastMod);
	                response.append(getMethod.getResponseBodyAsString());
				}
				
				// Caso o sistema de buscas esteja fora do ar, comeca a fazer 1 request a cada FAIL_COUNTER recebidos
			    if (tries-- <= 0 ) {
    			    try {
    			    	SearchProxy searchProxy = ProxyHelper.getSearchProxy("http://tocaletra.com.br/search");
    			    	
						if (isArtistPage) {
							noExtensionURI = noExtensionURI.replace("/artista", "");
							String[] artistUri = noExtensionURI.split("/");
 		   		        	artistVO = searchProxy.searchByImage("/"+artistUri[1]+"/");
						}				
						if (isMusicPage) {
						    noExtensionURI = noExtensionURI.replace("/musica", "");		
		    	    	    String[] musicUri = noExtensionURI.split("/");
	    		        	artistNameNormalized = musicUri[1];
	    		        	musicNameNormalized = musicUri[2].replace(".html", "");
 		   		        	artistVO = searchProxy.searchByImage("/"+artistNameNormalized+"/");
		    	        
	 		   		        PageResultVO pageResultVO = searchProxy.searchByURI(noExtensionURI);
		    		        ResultItemVO firstItem = pageResultVO.getFirstItem();
	    		    	    if (firstItem != null && noExtensionURI.equals(firstItem.getMusicUri())) {
	    		        	    idtRadioTrack = firstItem.getIdtTrackRadio();
		    	        	    artistName = firstItem.getArtistName();
		    		        }
						}
    			    } catch (Exception ex) {
    			    	LOGGER.error("ERRO AO BUSCAR AS MEDIAS REFERENTES A MUSICA " + noExtensionURI, ex);
    			        LOGGER.warn("SISTEMA DE BUSCA POSSIVELMENTE FORA DO AR DEPOIS DE " + tries + " SUCESSOS");
    			        tries = FAIL_COUNTER;
    			    }
				}

				//if(redirectProperties.isUsingTemplate() && !isSearchPage && !isTopPage){
				if(isLetterPage || isArtistPage || isMusicPage || isContentPage){
					String VELOCITY_TEMPLATE_FRONT = GeneratorProperties.getInstance().getVelocityTemplateFront();
			    	
					StringBuilder head = new StringBuilder();
					StringBuilder body = new StringBuilder();
					int cutIndex = response.toString().indexOf("<!-- X -->");
					if(cutIndex > -1){
						head = new StringBuilder(response.substring(0, cutIndex));
						body = new StringBuilder(response.substring(cutIndex+10));
					} else {
						body = response;
					}
					
					Map<String, Object> context = new HashMap<String, Object>();
			        context.put("type", "include");
			        context.put("head", head);
			        context.put("body", body);
			        if (!isLetterPage) {
			            context.put("contextURI", URLEncoder.encode(contextURIOriginal, "UTF-8"));
			        }

			        if(isArtistPage){
			        	context.put("artistPage", true);
				        if(artistVO != null && artistVO.getAllImages() != null){
				        	List<ImageVO> artistImages = getArtistImages(artistVO);
				        	context.put("artistImages", artistImages);
				        }
			        }
			        
			        if(isMusicPage){
			        	context.put("musicPage", true);
				        if(idtRadioTrack != null && !idtRadioTrack.isEmpty()){
				        	context.put("idtRadioTrack", idtRadioTrack);
				        }
				        if(artistVO != null && artistVO.getAllImages() != null){
				        	List<ImageVO> artistImages = getArtistImages(artistVO);
				        	context.put("artistImages", artistImages);
				        }
				        context.put("artistName", artistName);
				        context.put("artistNameNormalized", artistNameNormalized);
				        context.put("musicNameNormalized", musicNameNormalized);
			        }
					
					StringWriter writer = VelocityGenerator.getInstance().getHtml(VELOCITY_TEMPLATE_FRONT, context);
					out.print(writer.toString());
				} else {
					out.print(response.toString());
				}
			} else {
                String queryParameter = new String(req.getParameter("q").getBytes("ISO8859_1"),"UTF8");

                String VELOCITY_TEMPLATE_FRONT = GeneratorProperties.getInstance().getVelocityTemplateFront();
                Map<String, Object> context = new HashMap<String, Object>();
                context.put("type", "googleResult");
                context.put("category", req.getParameter("category"));
                context.put("q", queryParameter);
                context.put("escapeTool", new org.apache.velocity.tools.generic.EscapeTool());
                if (!isLetterPage) {
                    context.put("contextURI", URLEncoder.encode(contextURIOriginal, "UTF-8"));
                }
                
                StringWriter writer = VelocityGenerator.getInstance().getHtml(VELOCITY_TEMPLATE_FRONT, context);
                out.print(writer.toString());
            }
            	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(out != null){
				out.flush();
				out.close();
			}
			if(in != null){
				in.close();
			}
		}
	}
	
	public List<ImageVO> getArtistImages(ArtistVO artist){
		List<ImageVO> artistImages = new ArrayList<ImageVO>();
    	for(ImageVO imageVO : artist.getAllImages()){
    		if(imageVO != null && imageVO.getUri() != null){
    			String[] imageUriSplited = imageVO.getUri().split("\\/");
    			String imageFileName = imageUriSplited[imageUriSplited.length-1];

    			boolean isThumbnail = false;
    			if(imageVO.getProvider() != null && imageVO.getProvider().equals("vagalume")){
    				isThumbnail = imageFileName.startsWith("gt");
    			}

    			if(!isThumbnail && imageUriSplited.length > 1){
        			String[] imageFileNameSplited = imageFileName.split("\\.");
        			String imageExtension = imageFileNameSplited[imageFileNameSplited.length-1];
        			String imageUrl = redirectProperties.getUrlArtistImages() + artist.getUri() + imageVO.getIdImage() + "." + imageExtension;
        			imageVO.setUrl(imageUrl);				        			
        			artistImages.add(imageVO);
    			}
    		}
    	}
    	
    	Collections.sort(artistImages, new Comparator<ImageVO>(){
			public int compare(ImageVO image1, ImageVO image2) {
				return image1.getIdImage().compareTo(image2.getIdImage());
			}
    		
    	});
    	
    	return artistImages;
	}
}
