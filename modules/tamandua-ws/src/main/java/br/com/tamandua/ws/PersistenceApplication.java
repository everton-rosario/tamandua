package br.com.tamandua.ws;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import br.com.tamandua.ws.resource.ArtistResource;
import br.com.tamandua.ws.resource.ImageResource;
import br.com.tamandua.ws.resource.ModerationResource;
import br.com.tamandua.ws.resource.MusicResource;
import br.com.tamandua.ws.resource.UserResource;

/**
 * @author sotsuki
 */
public class PersistenceApplication extends Application {

	public Set<Class<?>> getClasses() {
		Set<Class<?>> s = new HashSet<Class<?>>();
		s.add(ArtistResource.class);
		s.add(MusicResource.class);
		s.add(ImageResource.class);
		s.add(UserResource.class);
		s.add(ModerationResource.class);
//		// ExceptionHandlers
//		s.add(ClassNotFoundExceptionHandler.class);
//        s.add(SQLExceptionHandler.class);
//        s.add(LocalRankingCacheExceptionHandler.class);
//        s.add(UserForumNotFoundExceptionHandler.class);
//        s.add(InvalidParameterExceptionHandler.class);
		return s;
	}
}
