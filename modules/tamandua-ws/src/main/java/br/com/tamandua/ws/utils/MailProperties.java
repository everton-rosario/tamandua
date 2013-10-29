package br.com.tamandua.ws.utils;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailProperties {

	/* Propriedades */
	private static Properties properties = new Properties();
	
    /* Chaves de propriedades no arquivo de propriedades */
	private static final String SEND_MAIL = "send.mail";
	private static final String RECEIVE_MAIL = "receive.mail";
	
	private static final String RECEIVE_MAIL_INFO_ERROR = "receive.mail.info.error";

	private static final String RECEIVE_REPORT_CONTENT_SUBJECT = "receive.reportContent.subject";
	private static final String RECEIVE_REPORT_CONTENT_TEMPLATE = "receive.reportContent.template";
	
	private static final String REGISTER_CONFIRM_TEMPLATE = "register.confirm.template";
    private static final String REGISTER_CONFIRM_SUBJECT = "register.confirm.subject";

	private static final String RESET_PASSWORD_TEMPLATE = "reset.password.template";
    private static final String RESET_PASSWORD_SUBJECT = "reset.password.subject";

    /* Nome do arquivo de propriedades */
    private static final String PROPERTIES_FILE_NAME = "/mail.properties";
    
    /* Mecanismo de log */
    private static Logger LOGGER = LoggerFactory.getLogger(MailProperties.class);
    
    private static MailProperties instance;
    
    static {
        try {
        	instance = new MailProperties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private MailProperties(){
    	LOGGER.info("Loading config file...");
    	try {
			properties.load(MailProperties.class.getResourceAsStream(PROPERTIES_FILE_NAME));
		} catch (IOException e) {
			LOGGER.error("Erro on loading config file...", e);
		}
    }

    public String getSendMail() {
        return properties.getProperty(SEND_MAIL);
    }

    public String getReceiveMail() {
        return properties.getProperty(RECEIVE_MAIL);
    }
    
    public String getReceiveMailInfoError(){
    	return properties.getProperty(RECEIVE_MAIL_INFO_ERROR);
    }
    
    public String getRegisterConfirmTemplate() {
        return properties.getProperty(REGISTER_CONFIRM_TEMPLATE);
    }
    
    public String getRegisterConfirmSubject() {
        return properties.getProperty(REGISTER_CONFIRM_SUBJECT);
    }
    
    public String getResetPasswordTemplate(){
    	return properties.getProperty(RESET_PASSWORD_TEMPLATE);
    }
    
    public String getResetPasswordSubject() {
    	return properties.getProperty(RESET_PASSWORD_SUBJECT);
    }
        
	public String getReceiveReportContentSubject() {
		return properties.getProperty(RECEIVE_REPORT_CONTENT_SUBJECT);
	}
	
	
	public String getReceiveReportContentTemplate() {
		return properties.getProperty(RECEIVE_REPORT_CONTENT_TEMPLATE);
	}

	/**
     * @return the instance
     */
    public static MailProperties getInstance() {
        return instance;
    }
}
