package br.com.tamandua.ws.resource;

import static br.com.tamandua.service.proxy.ProxyHelper.APPLICATION_JSON_WITH_CHARSET_UTF8;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.tamandua.persistence.app.AppContext;
import br.com.tamandua.service.UserService;
import br.com.tamandua.service.vo.ReportErrorVO;
import br.com.tamandua.service.mail.UserMailService;
import br.com.tamandua.service.vo.UserVO;
import br.com.tamandua.service.vo.validator.MessagesVO;

@Path("/user")
public class UserResource {

	@POST
    @Path("/save")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO saveUser(UserVO user){
		UserService userService = (UserService) AppContext.getApplicationContext().getBean("userService");
		MessagesVO messages = userService.saveUser(user);
		
		user = userService.getUserByEmail(user.getEmail());		
		UserMailService userMailService = (UserMailService) AppContext.getApplicationContext().getBean("userMailService");
		userMailService.sendConfirmMessage(user);
		
		return messages;
	}
	
	@POST
    @Path("/confirmRegister")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public UserVO confirmRegister(UserVO user){
		UserService userService = (UserService) AppContext.getApplicationContext().getBean("userService");
		return userService.confirmRegister(user);
	}
	
	@POST
    @Path("/resetPassword")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public void resetPassword(UserVO user){
		UserService userService = (UserService) AppContext.getApplicationContext().getBean("userService");
		userService.resetPassword(user);
		
		UserMailService userMailService = (UserMailService) AppContext.getApplicationContext().getBean("userMailService");
		userMailService.sendResetPassword(user);
	}

	@POST
	@Path("/login")
	@Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public Boolean verifyLogin(UserVO userVO){
		UserService userService = (UserService) AppContext.getApplicationContext().getBean("userService");
		return userService.verifyLogin(userVO);
	}
	
	@POST
	@Path("/findByEmail")
	@Consumes( { MediaType.TEXT_PLAIN } )
	@Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public UserVO findByEmail(String email){
		UserService userService = (UserService) AppContext.getApplicationContext().getBean("userService");
		return userService.getUserByEmail(email);
	}
	
	@POST
	@Path("/findById")
	@Consumes( { MediaType.TEXT_PLAIN } )
	@Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public UserVO findById(Long id){
		UserService userService = (UserService) AppContext.getApplicationContext().getBean("userService");
		return userService.getUserById(id);
	}
	
	@POST
	@Path("/edit")
	@Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
	public MessagesVO editUser(UserVO userVO){
		UserService userService = (UserService) AppContext.getApplicationContext().getBean("userService");
		return userService.editUser(userVO);
	}
	
	@POST
	@Path("/reportError")
	@Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
	@Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
	public MessagesVO reportError(ReportErrorVO reportErrorVO){	
		UserService userService = (UserService) AppContext.getApplicationContext().getBean("userService");
		return userService.reportError(reportErrorVO);
	}
	
	@POST
	@Path("/reportContent")
	@Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
	@Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
	public MessagesVO reportContent(ReportErrorVO reportContentVO){	
		UserService userService = (UserService) AppContext.getApplicationContext().getBean("userService");
		return userService.reportContent(reportContentVO);

	}
	
	
}
