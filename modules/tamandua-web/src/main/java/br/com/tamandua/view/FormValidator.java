package br.com.tamandua.view;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.fileupload.FileItem;

import br.com.tamandua.service.proxy.ProxyHelper;
import br.com.tamandua.service.proxy.UserProxy;
import br.com.tamandua.service.vo.UserVO;

public class FormValidator {
	
	private static final int AVATAR_MAX_SIZE = 2000000;
	private static final Pattern pattern = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");

	public static List<String> validateRegisterForm(String email, String cemail,String password, String cpassword, String name, String cpf, FileItem avatar){
		UserProxy userProxy = ProxyHelper.getUserProxy("http://tocaletra.com.br/ws");
		
		List<String> messages = new ArrayList<String>();
		if(email.isEmpty()){
			messages.add("Campo e-mail deve ser preenchido.");
		} else if(!validateEmail(email)) {
			messages.add("Campo e-mail inválido.");
		} else if(!email.equals(cemail)){
			messages.add("Confirmação de e-mail não confere!");
		} else if(userProxy.findByEmail(email) != null){
			messages.add("O e-mail <b>"+email+"</b> já está cadastrado!");
		}
		if(password.isEmpty()){
			messages.add("Campo senha deve ser preenchido.");
		} else if(!password.equals(cpassword)){
			messages.add("Confirmação de senha não confere!");
		}
		if(name.isEmpty()){
			messages.add("Campo nome deve ser preenchido.");
		}
		messages.addAll(validateAvatar(avatar));
		if(!cpf.isEmpty()){
			messages.addAll(validateCPF(cpf));
		}
		return messages;
	}
	
	public static List<String> validateEditForm(UserVO user, String newEmail, String confirmNewEmail, String password, String newPassword, String confirmNewPassword, String name, String cpf, FileItem avatar){
		UserProxy userProxy = ProxyHelper.getUserProxy("http://tocaletra.com.br/ws");
		
		List<String> messages = new ArrayList<String>();
		if(!newEmail.isEmpty()){
			if(!validateEmail(newEmail)) {
				messages.add("Campo novo e-mail inválido.");
			} else if(!newEmail.equals(confirmNewEmail)){
				messages.add("Confirmação de novo e-mail não confere!");
			} else if(userProxy.findByEmail(newEmail) != null){
				messages.add("Novo e-mail <b>"+ newEmail +"</b> já está cadastrado!");
			}
		}
		if(!password.isEmpty()){
			UserVO verifyUser = new UserVO();
			verifyUser.setEmail(user.getEmail());
			verifyUser.setPassword(password);
			if(!userProxy.verifyLogin(verifyUser)){
				messages.add("Senha atual inválida!");
			}
			if(newPassword.isEmpty() || confirmNewPassword.isEmpty()){
				messages.add("Campo nova senha e confirmar nova senha devem ser preenchidos.");
			} else if(!newPassword.equals(confirmNewPassword)){
				messages.add("Confirmação de nova senha não confere!");
			}
		} else if(!newPassword.isEmpty() || !confirmNewPassword.isEmpty()){
			messages.add("Campo senha atual deve ser preenchido.");
		}
		if(name.isEmpty()){
			messages.add("Campo nome deve ser preenchido.");
		}
		messages.addAll(validateAvatar(avatar));
		if(!cpf.isEmpty()){
			messages.addAll(validateCPF(cpf));
		}

		return messages;
	}
	
	public static List<String> validateMusicForm(String music_name, String artist_uri, String lyric_text, String translate, String translateMusic){
		List<String> messages = new ArrayList<String>();

		if(music_name == null || music_name.isEmpty()){
			messages.add("Nome da música deve ser preenchido.");
		}
		if(artist_uri == null || artist_uri.isEmpty()){
			messages.add("Nome do interprete não foi preenchido corretamente.");
		}
		if(lyric_text == null || lyric_text.isEmpty()){
			messages.add("Letra da música deve ser preenchida.");
		}
		if(translate.equals("y") && (translateMusic == null || translateMusic.isEmpty())){
			messages.add("Tradução da música deve ser preenchida.");
		}
		
		return messages;
	}
	
	public static List<String> validateAvatar(FileItem avatar){
		List<String> messages = new ArrayList<String>();
		if(!avatar.getName().isEmpty()){
			if(!avatar.getContentType().equals("image/jpg") && !avatar.getContentType().equals("image/jpeg") && !avatar.getContentType().equals("image/pjpeg")){
				messages.add("Formato do arquivo do avatar inválido! É permitido apenas imagens no formato <b>.jpg</b> ou <b>.jpeg</b>");
			}
			if(avatar.getSize() > AVATAR_MAX_SIZE){
				messages.add("O arquivo do avatar deve ter no máximo 2MB.");
			}
		}
		return messages;
	}
	
	public static boolean validateEmail(String email){  
		Matcher matcher = pattern.matcher(email);
		return matcher.find();
	}
	
	public static List<String> validateCPF(String cpf){
		List<String> messages = new ArrayList<String>();
		try{
			NumberFormat.getInstance().parse(cpf);			
		} catch (ParseException e) {
			messages.add("O CPF deve ter apenas números.");
			return messages;
		}
		if(cpf.length() != 11){
			messages.add("O CPF deve ter 11 dígitos.");
		} else if (cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222") || cpf.equals("33333333333") ||
			cpf.equals("44444444444") || cpf.equals("55555555555") || cpf.equals("66666666666") || cpf.equals("77777777777") ||
			cpf.equals("88888888888") || cpf.equals("99999999999")){
			messages.add("O número de CPF inválido!");
		} else {		
			int firstDigit = Integer.parseInt(String.valueOf(cpf.charAt(9)));
			int secondDigit = Integer.parseInt(String.valueOf(cpf.charAt(10)));		
			if(firstDigit != calculateCPFFirstDigit(cpf) || secondDigit != calculateCPFSecondDigit(cpf)){
				messages.add("O número de CPF inválido!");
			}
		}
		return messages;

	}
	
	public static int calculateCPFFirstDigit(String cpf){
		return calculateCPFDigit(cpf.substring(0, 9));
	}
	
	public static int calculateCPFSecondDigit(String cpf){
		return calculateCPFDigit(cpf.substring(0, 10));
	}
	
	public static int calculateCPFDigit(String cpf){
		int val = 0;
		int dig = cpf.length() + 1;
		for(int i=0; i<cpf.length(); i++){
			val += Integer.parseInt(String.valueOf(cpf.charAt(i))) * dig;
			dig--;
		}
		int calc = val % 11;
		if(calc < 2){
			return 0;
		} else {
			return 11 - calc;
		}
	}
}
