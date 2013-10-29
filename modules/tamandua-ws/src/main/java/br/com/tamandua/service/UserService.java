package br.com.tamandua.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.tamandua.persistence.app.AppContext;
import br.com.tamandua.service.adapter.UserAdapter;
import br.com.tamandua.service.mail.UserMailService;
import br.com.tamandua.service.vo.ImageVO;
import br.com.tamandua.service.vo.ReportErrorVO;
import br.com.tamandua.service.vo.UserVO;
import br.com.tamandua.service.vo.validator.ActionMessageVO;
import br.com.tamandua.service.vo.validator.Level;
import br.com.tamandua.service.vo.validator.LevelMessageVO;
import br.com.tamandua.service.vo.validator.MessagesVO;
import br.com.tamandua.service.vo.validator.ActionMessageVO.Action;
import br.com.tamandua.ws.service.ImageService;

@Service
public class UserService {
	@Transactional
	public MessagesVO saveUser(UserVO user){
		UserAdapter adapter = (UserAdapter) AppContext.getApplicationContext().getBean("userAdapter");
		MessagesVO messages = adapter.saveUser(user);
		
		byte[] file = user.getFile();
		user = adapter.findByEmail(user.getEmail());
		
		if(file != null){
			user.setFile(file);			
			ImageService imageService = (ImageService) AppContext.getApplicationContext().getBean("imageService");
			ImageVO image = imageService.uploadRescaleImage(user);		
			user.setImage(image);
			adapter.saveUser(user);
		}

		return messages;
	}
	
	public UserVO getUserById(Long id){
		UserAdapter adapter = (UserAdapter) AppContext.getApplicationContext().getBean("userAdapter");
		return adapter.findById(id);
	}
	
	public UserVO getUserByEmail(String email){
		UserAdapter adapter = (UserAdapter) AppContext.getApplicationContext().getBean("userAdapter");
		return adapter.findByEmail(email);
	}
	
	@Transactional
	public UserVO confirmRegister(UserVO user){
		UserAdapter adapter = (UserAdapter) AppContext.getApplicationContext().getBean("userAdapter");
		return adapter.confirmRegister(user);
	}
	
	@Transactional
	public UserVO resetPassword(UserVO user){
		UserAdapter adapter = (UserAdapter) AppContext.getApplicationContext().getBean("userAdapter");
		String newPassword = adapter.resetPassword(user);
		user.setPassword(newPassword);
		return user;
	}

	public boolean verifyLogin(UserVO userVO){
		UserAdapter adapter = (UserAdapter) AppContext.getApplicationContext().getBean("userAdapter");
		return adapter.verifyLogin(userVO);
	}
	
	@Transactional
	public MessagesVO editUser(UserVO userVO){
		UserAdapter adapter = (UserAdapter) AppContext.getApplicationContext().getBean("userAdapter");
		if(userVO.getFile() != null){
			ImageService imageService = (ImageService) AppContext.getApplicationContext().getBean("imageService");
			ImageVO image = imageService.uploadRescaleImage(userVO);		
			userVO.setImage(image);
		}
		MessagesVO messages = adapter.saveUser(userVO);
		return messages;
	}
	
	public MessagesVO reportError(ReportErrorVO reportErrorVO){
		UserMailService userMailService = (UserMailService) AppContext.getApplicationContext().getBean("userMailService");
		userMailService.sendReportError(reportErrorVO);
		
		MessagesVO messages = new MessagesVO();
		messages.addAction(new ActionMessageVO(Action.CREATE, new LevelMessageVO(Level.INFO, "Report error was sent successful")));
		
		return messages;
	}
	
	public MessagesVO reportContent(ReportErrorVO reportContentVO){
		UserMailService userMailService = (UserMailService) AppContext.getApplicationContext().getBean("userMailService");
		userMailService.sendReportContent(reportContentVO);
		
		MessagesVO messages = new MessagesVO();
		messages.addAction(new ActionMessageVO(Action.CREATE, new LevelMessageVO(Level.INFO, "Report content was sent successful")));
		
		return messages;
	}
}
