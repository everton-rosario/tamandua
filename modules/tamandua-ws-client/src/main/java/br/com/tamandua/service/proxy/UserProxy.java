package br.com.tamandua.service.proxy;

import static br.com.tamandua.service.proxy.ProxyHelper.APPLICATION_JSON_WITH_CHARSET_UTF8;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.tamandua.service.vo.ReportErrorVO;
import br.com.tamandua.service.vo.UserVO;
import br.com.tamandua.service.vo.validator.MessagesVO;

@Path("/user")
public interface UserProxy {
	@POST
    @Path("/save")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO saveUser(UserVO userVO);
	
	@POST
    @Path("/confirmRegister")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public UserVO confirmRegister(UserVO userVO);
	
	@POST
    @Path("/resetPassword")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public void resetPassword(UserVO userVO);
	
	@POST
	@Path("/login")
	@Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public Boolean verifyLogin(UserVO userVO);
	
	@POST
    @Path("/findByEmail")
    @Consumes( { MediaType.TEXT_PLAIN } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public UserVO findByEmail(String email);
	
	@POST
    @Path("/findById")
    @Consumes( { MediaType.TEXT_PLAIN } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public UserVO findById(Long id);
	
	@POST
    @Path("/edit")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO editUser(UserVO userVO);	
	
	@POST
    @Path("/reportError")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO reportError(ReportErrorVO reportErrorVO);
	
	@POST
    @Path("/reportContent")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    public MessagesVO reportContent(ReportErrorVO reportContentVO);
	
}
