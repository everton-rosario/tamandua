package br.com.tamandua.generator.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.tools.ToolManager;

public class VelocityGenerator {
	
	private static VelocityGenerator instance;
	
	private VelocityEngine engine;
	private ToolManager manager;
	
	public static VelocityGenerator getInstance(){
		if(instance == null){
			instance = new VelocityGenerator();
		}
		return instance;
	}
	
	private VelocityGenerator(){
		try {
			final String VELOCITY_PROPERTIES_FILE = GeneratorProperties.getInstance().getVelocityPropertiesFile();
			final String TOOLBOX_XML_FILE = GeneratorProperties.getInstance().getToolboxXmlFile();
			
			Properties properties = new Properties();    	
			properties.load(this.getClass().getResourceAsStream(VELOCITY_PROPERTIES_FILE));        	
    	
	        this.engine = new VelocityEngine(properties);
	        engine.init();
	        
	    	this.manager = new ToolManager();
	    	manager.configure(TOOLBOX_XML_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public StringWriter getHtml(String templateName, Map<String, Object> contextMap){
		StringWriter writer = new StringWriter();
		try {
			VelocityContext context = new VelocityContext(manager.createContext());			
			for(String key : contextMap.keySet()){
				context.put(key, contextMap.get(key));
			}

			Template template = engine.getTemplate(templateName, "UTF-8");
	        template.merge(context, writer);
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return writer;
	}
}
