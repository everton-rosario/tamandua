/**
 * 
 */
package br.com.tamandua.view;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.tamandua.service.proxy.ArtistProxy;
import br.com.tamandua.service.proxy.ModerationProxy;
import br.com.tamandua.service.proxy.MusicProxy;
import br.com.tamandua.service.proxy.ProxyHelper;
import br.com.tamandua.service.proxy.SearchProxy;
import br.com.tamandua.service.proxy.UserProxy;
import br.com.tamandua.service.util.StringNormalizer;
import br.com.tamandua.service.vo.ArtistVO;
import br.com.tamandua.service.vo.LyricVO;
import br.com.tamandua.service.vo.ModerationVO;
import br.com.tamandua.service.vo.MusicVO;
import br.com.tamandua.service.vo.PageResultVO;
import br.com.tamandua.service.vo.ReportErrorVO;
import br.com.tamandua.service.vo.UserVO;
import br.com.tamandua.service.vo.json.UserJSON;

/**
 * @author Everton Rosario
 */
@Controller
public class FrontController {
	private static final int AVATAR_MAX_SIZE = 2000000;
	
	public FrontController() {
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
			model.addAttribute("letter", ""+artistVO.getLetters().charAt(0));
		}
		return "front";
	}

	@RequestMapping("/musica/{artistUri}/{musicUri}.html")
	public String doGetMusic(@PathVariable String artistUri,
			@PathVariable String musicUri, Model model) {
		model.addAttribute("type", "lyric");
		MusicProxy musicProxy = ProxyHelper
				.getMusicProxy("http://tocaletra.com.br/ws");
		MusicVO musicVO = musicProxy.findMusicByURI("/" + artistUri + "/"
				+ musicUri);
		model.addAttribute("music", musicVO);
		model.addAttribute("letter", ""+musicVO.getArtist().getLetters().charAt(0));

		return "front";
	}

	@RequestMapping("/letra/{letter}.html")
	public String doGetLetter(@PathVariable String letter, Model model) {
		model.addAttribute("type", "letter");
		ArtistProxy artistProxy = ProxyHelper
				.getArtistProxy("http://tocaletra.com.br/ws");
		model.addAttribute("letter", letter);
		List<ArtistVO> artistsFromLetter = artistProxy
				.findArtistsByLetter(letter);
		model.addAttribute("artists", artistsFromLetter);
		return "front";
	}

	@RequestMapping("/usuario/form/{form}")
	public String formUser(@PathVariable String form, Model model) {
		model.addAttribute("type", form);
		return "front";
	}
	
	@RequestMapping("/usuario/confirmRegister")
	public String confirmRegister(@QueryParam("email") String email, @QueryParam("activationCode") String activationCode, Model model){
		UserVO user = new UserVO();
		user.setEmail(email);
		user.setActivationCode(activationCode);
		
		UserProxy userProxy = ProxyHelper.getUserProxy("http://tocaletra.com.br/ws");
		user = userProxy.confirmRegister(user);
		
		model.addAttribute("type", "message");
		if(user != null && user.getStatus().equals("A")){
			model.addAttribute("message", "Confirmação de cadastro efetuada com sucesso!");
			model.addAttribute("subMessage", "Agora é só acessar a sua conta através do seu e-mail e senha.");
		} else {
			model.addAttribute("message", "Erro ao confirmar seu cadastro!");
			model.addAttribute("subMessage", "E-mail ou código de ativação inválidos.");
		}

		return "front";
	}
		
	@RequestMapping("/usuario/resetPassword")
	public String resetPassword(@QueryParam("email") String email, Model model){
		
		UserProxy userProxy = ProxyHelper.getUserProxy("http://tocaletra.com.br/ws");
	
		UserVO user = userProxy.findByEmail(email);		
		
		if(user != null){
			userProxy.resetPassword(user);
			model.addAttribute("type","message");
			model.addAttribute("message","Senha alterada com sucesso!");
			model.addAttribute("subMessage","Entre agora mesmo no seu email para verificar a nova senha! ");
			
		} else {
			user = new UserVO();
			user.setEmail(email);
			model.addAttribute("type","forgotPassword");
			model.addAttribute("user",user);
			model.addAttribute("emailNotExist","Este e-mail não está cadastrado!");
		}		
		
		
	
		
		return "front";
	}

	@RequestMapping("/usuario/save")
	@Consumes({ProxyHelper.MULTIPART_FORM_DATA_WITH_CHARSET_UTF8})
	public String saveUser(@Context HttpServletRequest request, Model model) {
		UserProxy userProxy = ProxyHelper.getUserProxy("http://tocaletra.com.br/ws");
		
		Map<String, Object> parameters = getParameters(request);		
		String name = (String) parameters.get("name");
		String email = (String) parameters.get("email");
		String password = (String) parameters.get("password");
		String cpf = (String) parameters.get("cpf");
		String rg = (String) parameters.get("rg");		
		FileItem avatar = (FileItem) parameters.get("avatar");
		
		UserVO userVo = new UserVO();
		userVo.setName(name);
		userVo.setEmail(email);
		
		String messageAvatar = validateAvatar(avatar);
		
		
		if(!messageAvatar.equals("")){
			model.addAttribute("type", "register");
			model.addAttribute("invalidFile", messageAvatar);				
			model.addAttribute("user" , userVo);
			return "front";
		}
		
		if(userProxy.findByEmail(email) != null){
			model.addAttribute("type", "register");
			model.addAttribute("emailExist", "Este e-mail já está cadastrado!");				
			model.addAttribute("user" , userVo);

			return "front";
		}
		
		UserVO user = new UserVO();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setCpf(cpf);
		user.setRg(rg);
		if(!avatar.getName().isEmpty()){
			user.setFile(avatar.get());
		}
		userProxy.saveUser(user);
	
		model.addAttribute("type", "message");
		model.addAttribute("message", "Cadastro efetuado com sucesso!");
		model.addAttribute("subMessage", "Entre agora mesmo no seu email para confirmar o cadastro!");
		return "front";
	}

	@RequestMapping("/usuario/login")
	public ModelAndView login(@Context HttpServletResponse response,
						@FormParam("email") String email,
						@FormParam("password") String password,
						@FormParam("keepConnected") String keepConnected){
		Map<String, Object> model = new HashMap<String, Object>();
			
		UserProxy userProxy = ProxyHelper.getUserProxy("http://tocaletra.com.br/ws");
		UserVO user = userProxy.findByEmail(email);

		if(user != null){
			user.setPassword(password);
			Boolean isValid = userProxy.verifyLogin(user);			
			if(isValid){
				if(user.getStatus().equals("A")){
					String cookieValue = getCookieValue(user);
					Cookie cookie = new Cookie("session", cookieValue);
					if(keepConnected != null){
						cookie.setMaxAge(15*24*60*60);
					} else {
						cookie.setMaxAge(-1);
					}
					cookie.setPath("/");
					//cookie.setSecure(true);
					response.addCookie(cookie);
					
					model.put("success", true);
					return new ModelAndView("jsonView", model);
				} else {
					model.put("success", false);
					model.put("error", "Seu cadatro não foi confirmado. Entre agora em seu e-mail para confirmar seu cadastro.");
					return new ModelAndView("jsonView", model);
				}
			}
		}
		model.put("success", false);
		model.put("error", "E-mail e/ou senha incorretos, tente novamente.");
		return new ModelAndView("jsonView", model);
	}
	
	@RequestMapping("/usuario/logout")
	public ModelAndView logout(@Context HttpServletRequest request, @Context HttpServletResponse response){
		Cookie cookie = getSessionCookie(request);
		cookie.setValue("");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		return new ModelAndView("jsonView", new HashMap<String, Object>());
	}
	
	@SuppressWarnings("finally")
	@RequestMapping("/usuario/form/edit")
	public String formEdit(@Context HttpServletRequest request, Model model, UserVO userVO) {
		try{
			Long idUser = getSessionUser(request).getIdUser();			
			
			UserProxy userProxy = ProxyHelper.getUserProxy("http://tocaletra.com.br/ws");
			userVO = userProxy.findById(idUser);
			
			model.addAttribute("type", "edit");
			model.addAttribute("user", userVO);
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			return "front";
		}	
	}
	
	@RequestMapping("/usuario/edit")
	@Consumes({ProxyHelper.MULTIPART_FORM_DATA_WITH_CHARSET_UTF8})
	public String editUser(@Context HttpServletRequest request, @Context HttpServletResponse response, Model model) {
		UserProxy userProxy = ProxyHelper.getUserProxy("http://tocaletra.com.br/ws");
		
		Map<String, Object> parameters = getParameters(request);
		String name = (String) parameters.get("name");
		String newEmail = (String) parameters.get("newEmail");
		String password = (String) parameters.get("password");
		String newPassword = (String) parameters.get("newPassword");
		String confirmNewPassword = (String) parameters.get("confirmNewPassword");
		String cpf = (String) parameters.get("cpf");
		String rg = (String) parameters.get("rg");
		FileItem avatar = (FileItem) parameters.get("avatar");
		
		Long idUser = getSessionUser(request).getIdUser();		
		UserVO user = userProxy.findById(idUser);
		user.setName(name);
		user.setCpf(cpf);
		user.setRg(rg);
		user.setPassword(password);
		
		String messageAvatar = validateAvatar(avatar);
		
		boolean hasError = false;
		
		if(!messageAvatar.equals("")){
			model.addAttribute("invalidFile", messageAvatar);
			hasError = true;
		}		
		if(userProxy.findByEmail(newEmail) != null){
			model.addAttribute("emailExist", "Novo e-mail já está cadastrado!");
			hasError = true;
		}		
		if(!password.isEmpty() && !userProxy.verifyLogin(user)){;
			model.addAttribute("invalidPassword", "Senha atual incorreta!");
			hasError = true;
		}
		
		if(!hasError){
			if(!avatar.getName().isEmpty()){
				user.setFile(avatar.get());
			}
			if(!newEmail.isEmpty()){
				user.setEmail(newEmail);
			}
			if(!newPassword.isEmpty()){
				user.setPassword(newPassword);
			}
			userProxy.editUser(user);
			
			Cookie cookie = getSessionCookie(request);
			String cookieValue = getCookieValue(user);
			cookie.setValue(cookieValue);
			cookie.setPath("/");
			response.addCookie(cookie);
			
			model.addAttribute("type", "message");
			model.addAttribute("message", "Cadastro atualizado com sucesso!");
			model.addAttribute("subMessage","Você pode alterar seus dados a qualquer momento.");
		} else {
			UserVO newUser = new UserVO();
			newUser.setEmail(newEmail);
			
			model.addAttribute("type", "edit");
			model.addAttribute("user" , user);
			model.addAttribute("newUser" , newUser);
		}
		
		return "front";
	}
	
	@RequestMapping("/usuario/correctMusic")
	public String correctMusic(@Context HttpServletRequest request,
			@FormParam("idEntity") Long idEntity, @FormParam("newText") String newText, Model model) {
		Long idUser = getSessionUser(request).getIdUser();
		
		UserProxy userProxy = ProxyHelper.getUserProxy("http://tocaletra.com.br/ws");
		UserVO user = userProxy.findById(idUser);
		
		ModerationVO moderation = new ModerationVO();
		moderation.setUser(user);
		moderation.setIdEntity(idEntity);
		moderation.setNewValue(newText);
		
		ModerationProxy moderationProxy = ProxyHelper.getModerationProxy("http://tocaletra.com.br/ws");
		moderationProxy.correctMusic(moderation);
		
		return "front";
	}
	
	@RequestMapping("/usuario/saveMusic")
	public String saveMusic(
			@FormParam("music_id") Long music_id,
			@FormParam("music_name") String music_name,
			@FormParam("lyric_text") String lyric_text,
			@FormParam("artistUri") String artistUri,
			@FormParam("artistName") String artistName,
			@FormParam("translate") String translate,
			@FormParam("translateMusic") String translateMusic,
			@FormParam("language") String language, Model model){
		
		MusicProxy musicProxy = ProxyHelper.getMusicProxy("http://tocaletra.com.br/ws");
		
		ArtistVO artistVO = new ArtistVO();
		artistVO.setUri(artistUri);
		artistVO.setName(artistName);
		
		Set<LyricVO> lyrics = new HashSet<LyricVO>();		
	
		LyricVO lyricVO = new LyricVO();
		lyricVO.setLyricTitle(music_name);
		lyricVO.setText(lyric_text);
		lyricVO.setProvider("x");
		lyricVO.setLaguage(language);
		lyricVO.setLyricType(LyricVO.LYRIC_TYPE_ORIGINAL);
		lyricVO.setFlag(LyricVO.FLAG_DEFAULT);
		lyrics.add(lyricVO);
		
		LyricVO lyricVOTraduction = null;
		if(translate.equals("y")){
			lyricVOTraduction = new LyricVO();
			lyricVOTraduction.setLyricTitle(music_name + " (tradução)");
			lyricVOTraduction.setText(translateMusic);
			lyricVOTraduction.setProvider("x");
			lyricVOTraduction.setLaguage("pt-br");
			lyricVOTraduction.setLyricType(LyricVO.LYRIC_TYPE_TRADUCTION);
			lyricVOTraduction.setFlag(LyricVO.FLAG_NO_DEFAULT);
			lyrics.add(lyricVOTraduction);			
		}
		
		String music_uri = artistUri + StringNormalizer.normalizeText(music_name);
		MusicVO music = musicProxy.findMusicByURI(music_uri);
		
		MusicVO musicVO = new MusicVO();
		musicVO.setIdMusic(music_id);
		musicVO.setTitle(music_name);
		musicVO.setUri(music_uri);
		musicVO.setLyrics(lyrics);
		musicVO.setArtist(artistVO);
		
		if(music != null){
			model.addAttribute("type","newMusic");
			model.addAttribute("musicExist" , "Essa música já existe.");
			model.addAttribute("lyric",lyricVO);
			model.addAttribute("music", musicVO);
			model.addAttribute("artist", artistVO);
			
			if(lyricVOTraduction != null){
				model.addAttribute("translateMusic",translateMusic);
			}
					
		}else{
			model.addAttribute("type", "message");
			musicProxy.saveMusicDetailed(musicVO);
			model.addAttribute("message" , "Nova música criada com sucesso.");
		}
		
		return "front";
	}
	
	@RequestMapping("/usuario/reportError")
	public String reportError(
			@QueryParam("name") String name,
			@QueryParam("email") String email,
			@QueryParam("url") String url,
			@QueryParam("assunto") String assunto,
			@QueryParam("mensagem") String mensagem,
			@QueryParam("problema") String problema,
			@QueryParam("descricaoProblema") String descricaoProblema,
			Model model
			
	){
		
		ReportErrorVO reportErrorVO = new ReportErrorVO();
		reportErrorVO.setName(name);
		reportErrorVO.setEmail(email);
		reportErrorVO.setUrl(url);
		reportErrorVO.setAssunto(assunto);
		reportErrorVO.setMensagem(mensagem);
		reportErrorVO.setProblema(problema);
		reportErrorVO.setDescricaoProblema(descricaoProblema);
		
		UserProxy userProxy = ProxyHelper.getUserProxy("http://tocaletra.com.br/ws");
		userProxy.reportError(reportErrorVO);
		
		model.addAttribute("type", "message");
		model.addAttribute("message", "Enviado com sucesso!");
		model.addAttribute("subMessage", "Obrigado por nos ajudar a manter o site.");
		
		return "front";	
	}
	
	@RequestMapping("/usuario/reportContent")
	public String reportContent(
			@QueryParam("name") String name,
			@QueryParam("email") String email,
			@QueryParam("description") String description,
			Model model
	){
		ReportErrorVO reportContent = new ReportErrorVO();
		reportContent.setName(name);
		reportContent.setEmail(email);
		reportContent.setMensagem(description);
		
		UserProxy userProxy = ProxyHelper.getUserProxy("http://tocaletra.com.br/ws");
		userProxy.reportContent(reportContent);
		
		model.addAttribute("type", "message");
		model.addAttribute("message", "Denúncia enviada com sucesso!");
		model.addAttribute("subMessage", "Em breve estaremos verificando o conteúdo denúnciado.");
	
		return "front";
	}

	@RequestMapping("/home/politica-de-privacidade")
    public String privacyPolicies(Model model){
	    model.addAttribute("type", "privacyPolicies");
        return "front";
    }

    @RequestMapping(value="/busca", params={"q"/*, "pageNumber", "max", "sort", "ascendingSort"*/})
    public String doGetOpenSearch(@RequestParam(required=true) String q,/*
                                  @RequestParam(required=false) final Integer pageNumber,
                                  @RequestParam(required=false) final Integer max,
                                  @RequestParam(required=false) final String sort,
                                  @RequestParam(required=false) final String ascendingSort,*/
                                  Model model) {
        model.addAttribute("type", "query");
        model.addAttribute("q", q);
//        model.addAttribute("page", pageNumber);
//        model.addAttribute("max", max);
//        model.addAttribute("sort", sort);
//        model.addAttribute("ascending", ascendingSort);

        SearchProxy searchProxy = ProxyHelper.getSearchProxy("http://tocaletra.com.br/search");
        PageResultVO pageResultVO = searchProxy.searchByOpenTerms(StringNormalizer.normalizeText(q), 1, 50, 50, "music_title", true);
        model.addAttribute("pageResult", pageResultVO);

        return "front";
    }
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public Cookie getSessionCookie(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		for(int i=0; i < cookies.length; i++){
			if(cookies[i].getName().equals("session")){
				return cookies[i];
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public String getCookieValue(UserVO user){
		String cookieValue = null;
		try{
			String userJSON = UserJSON.getString(user);
			cookieValue = URLEncoder.encode(userJSON, "UTF-8");
			cookieValue = cookieValue.replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return cookieValue;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public UserVO getSessionUser(HttpServletRequest request){
		UserVO userVO = null;
		try{
			Cookie cookie = getSessionCookie(request);
			if(cookie != null){
				String userJSON = URLDecoder.decode(cookie.getValue(), "UTF-8");
				userVO = UserJSON.getObject(userJSON);
			}
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return userVO;
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
	
	public String validateAvatar(FileItem avatar){
		String message = "";
		if(!avatar.getName().isEmpty()){
			if(!avatar.getContentType().equals("image/jpg") && !avatar.getContentType().equals("image/jpeg") && !avatar.getContentType().equals("image/pjpeg")){
				message = "É permitido apenas imagens no formato jpg ou jpeg";
			}else{
				if(avatar.getSize() > AVATAR_MAX_SIZE){
					message = "O arquivo do avatar deve ter no máximo 2MB";
				}
			}
			
		}
		return message;
	}
	
}
