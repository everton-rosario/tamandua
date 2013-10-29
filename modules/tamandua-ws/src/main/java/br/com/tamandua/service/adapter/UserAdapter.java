package br.com.tamandua.service.adapter;

import java.util.Random;

import org.springframework.stereotype.Component;

import br.com.tamandua.persistence.User;
import br.com.tamandua.persistence.app.AppContext;
import br.com.tamandua.persistence.dao.UserHome;
import br.com.tamandua.service.vo.UserVO;
import br.com.tamandua.service.vo.validator.ActionMessageVO;
import br.com.tamandua.service.vo.validator.Level;
import br.com.tamandua.service.vo.validator.LevelMessageVO;
import br.com.tamandua.service.vo.validator.MessagesVO;
import br.com.tamandua.service.vo.validator.ActionMessageVO.Action;
import br.com.tamandua.ws.security.HashGenerator;
import br.com.tamandua.ws.security.PasswordSignature;

@Component
public class UserAdapter {
	public MessagesVO saveUser(UserVO userVO){
		MessagesVO messages = new MessagesVO();
		
		UserHome userHome = (UserHome) AppContext.getApplicationContext().getBean("userHome");
		User user = null;
		if(userVO.getIdUser() != null){
			user = userHome.findById(userVO.getIdUser());
		}

		if(user == null){
			//New User
			user = parse(userVO);
			user.setPassword(PasswordSignature.getInstance().sign(userVO.getPassword()));
			user.setRole("INTERNET");
			user.setStatus("I");
			user.setActivationCode(HashGenerator.getInstance().generate(user.getEmail()));
			userHome.persist(user);
			userHome.flush();
			messages.addAction(new ActionMessageVO(Action.CREATE, new LevelMessageVO(Level.INFO, "user ["+user.getIdUser()+"] created")));
		}else{
			//Update User
			User parsed = parse(userVO);
			user.setEmail(parsed.getEmail());
			user.setName(parsed.getName());
			user.setCpf(parsed.getCpf());
			user.setRg(parsed.getRg());
			if(parsed.getImage() != null){
				user.setImage(parsed.getImage());
			}
			if(userVO.getPassword() != null && !userVO.getPassword().equals("")){
				user.setPassword(PasswordSignature.getInstance().sign(userVO.getPassword()));
			}
			userHome.merge(user);
			messages.addAction(new ActionMessageVO(Action.UPDATE, new LevelMessageVO(Level.INFO, "user ["+user.getIdUser()+"] updated")));
		}
		
		return messages;
	}
	public UserVO findById(Long id){
		UserHome dao = (UserHome) AppContext.getApplicationContext().getBean("userHome");
		return parse(dao.findById(id));
	}
	public UserVO findByEmail(String email){
		UserHome dao = (UserHome) AppContext.getApplicationContext().getBean("userHome");
		User user = dao.findByEmail(email);
		if(user != null){
			return parse(user);
		} else {
			return null;
		}
	}
	public UserVO confirmRegister(UserVO userVO){
		UserHome userHome = (UserHome) AppContext.getApplicationContext().getBean("userHome");
		User user = userHome.findByEmail(userVO.getEmail());
		
		if(user != null){
			String activationCode = HashGenerator.getInstance().toString(user.getActivationCode());
			if(userVO.getActivationCode().equals(activationCode)){
				user.setStatus("A");
				userHome.merge(user);
			}
			return parse(user);
		}
		
		return null;
	}
	public String resetPassword(UserVO userVO){
		UserHome userHome = (UserHome) AppContext.getApplicationContext().getBean("userHome");
		User user = userHome.findByEmail(userVO.getEmail());
		
		String newPassword = null;
		if(user != null){
			newPassword = "";
			Random random = new Random();
			for(int i=0; i<8; i++){
				newPassword += random.nextInt(10);
			}
			
			user.setPassword(PasswordSignature.getInstance().sign(newPassword));
			userHome.merge(user);
		}
		return newPassword;
	}
	public boolean verifyLogin(UserVO userVO){
		UserHome userHome = (UserHome) AppContext.getApplicationContext().getBean("userHome");
		User user = userHome.findByEmail(userVO.getEmail());
		if(user != null){
			if(PasswordSignature.getInstance().verify(user.getPassword(), userVO.getPassword())){
				return true;
			}
		}
		return false;		
	}
	public static UserVO parse(User entity) {
		UserVO vo = new UserVO();
		vo.setIdUser(entity.getIdUser());
		vo.setName(entity.getName());
		vo.setEmail(entity.getEmail());
		vo.setRole(entity.getRole());
		vo.setCpf(entity.getCpf());
		vo.setRg(entity.getRg());
		vo.setStreet(entity.getStreet());
		vo.setCity(entity.getCity());
		vo.setStreetNumber(entity.getStreetNumber());
		vo.setStreetComplement(entity.getStreetComplement());
		vo.setState(entity.getState());
		vo.setCountry(entity.getCountry());
		vo.setStatus(entity.getStatus());
		vo.setImage(ImageAdapter.parse(entity.getImage()));
		vo.setActivationCode(HashGenerator.getInstance().toString(entity.getActivationCode()));
		return vo;
	}
	public static User parse(UserVO vo) {
		User entity = new User();
		entity.setIdUser(vo.getIdUser());
		entity.setName(vo.getName());
		entity.setEmail(vo.getEmail());
		entity.setRole(vo.getRole());
		entity.setCpf(vo.getCpf());
		entity.setRg(vo.getRg());
		entity.setStreet(vo.getStreet());
		entity.setCity(vo.getCity());
		entity.setStreetNumber(vo.getStreetNumber());
		entity.setStreetComplement(vo.getStreetComplement());
		entity.setState(vo.getState());
		entity.setCountry(vo.getCountry());
		entity.setStatus(vo.getStatus());
		entity.setImage(ImageAdapter.parse(vo.getImage()));
		return entity;
	}
}
