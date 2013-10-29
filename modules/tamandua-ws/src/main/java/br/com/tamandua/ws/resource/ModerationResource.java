package br.com.tamandua.ws.resource;

import static br.com.tamandua.service.proxy.ProxyHelper.APPLICATION_JSON_WITH_CHARSET_UTF8;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import br.com.tamandua.persistence.app.AppContext;
import br.com.tamandua.service.ModerationService;
import br.com.tamandua.service.vo.ModerationVO;
import br.com.tamandua.service.vo.validator.MessagesVO;

@Path("/moderation")
public class ModerationResource {
	
	@POST
    @Path("/music")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
	public MessagesVO correctMusic(ModerationVO moderation) {
		ModerationService service = (ModerationService) AppContext.getApplicationContext().getBean("moderationService");
		return service.correctMusic(moderation);
	}
}
