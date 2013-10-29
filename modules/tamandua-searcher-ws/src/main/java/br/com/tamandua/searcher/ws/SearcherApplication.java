package br.com.tamandua.searcher.ws;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import br.com.tamandua.searcher.ws.resource.SearchResource;

/**
 * @author Everton Rosario
 */
public class SearcherApplication extends Application {

	public Set<Class<?>> getClasses() {
		Set<Class<?>> s = new HashSet<Class<?>>();
		s.add(SearchResource.class);
		return s;
	}
}
