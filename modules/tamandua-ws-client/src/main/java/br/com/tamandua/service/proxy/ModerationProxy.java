package br.com.tamandua.service.proxy;

import static br.com.tamandua.service.proxy.ProxyHelper.APPLICATION_JSON_WITH_CHARSET_UTF8;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import br.com.tamandua.service.vo.ModerationVO;
import br.com.tamandua.service.vo.validator.MessagesVO;

@Path("/moderation")
public interface ModerationProxy {

	@POST
    @Path("/music")
    @Consumes( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
    @Produces( { APPLICATION_JSON_WITH_CHARSET_UTF8 } )
	public MessagesVO correctMusic(ModerationVO moderation);
	
}
