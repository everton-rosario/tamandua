package br.com.tamandua.service.mail;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * 
 * @author SmartSW
 * </br></br>
 * Classe de Serviço para Envio de Email.
 * <table border = 1>
 * <tr><td colspan =2>Implements :</td></tr>
 * <tr><td>MailService</td></tr>
 * <tr><td>InitializingBean</td></tr>
 * </table>
 */
@Service
public class MailService implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private VelocityEngine velocityEngine;
	
	private ExecutorService pool;
	
	public MailService(){}
		
	/**
	 * Construtor padrão com parâmetros.
	 * @param <b>mailSender</b> Atributo <i>mailSender</i> do tipo <i>JavaMailSender</i>.
	 * @param <b>velocityEngine</b> Atributo <i>velocityEngine</i> do tipo <i>VelocityEngine</i>.
	 */
	public MailService(JavaMailSender mailSender, VelocityEngine velocityEngine){
		this.mailSender = mailSender;
		this.velocityEngine = velocityEngine;
	}

	/**
	 * Método de inicialização do Serviço para envio de e-mail.	 
	 */
	@PostConstruct
	public void init(){
		pool = Executors.newCachedThreadPool();
		LOGGER.info("Java Mail Sender successful initialization!");
	}

	/**
	 * Método para envio de um e-mail.
	 * @param <b>from</b> atributo do tipo <i>String</i>.
	 * @param <b>to</b> atributo do tipo <i>String</i>.
	 * @param <b>subject</b> atributo do tipo <i>String</i>.
	 * @param <b>template</b> atributo do tipo <i>String</i>.
	 * @param <b>model</b> atributo do tipo <i>Mapeamento</i>.
	 */
	public void sendMessage(String from, String to, String subject, String template, Map<String, Object> model) {
		sendMessage(from, new String[]{to}, null, null, subject, template, model);
	}

	/**
	 * Método sobrecarregado para envio de vários e-mails.
	 * @param <b>from</b> atributo do tipo <i>String</i>.
	 * @param <b>to</b> atributo do tipo <i>Vetor de Strings</i>.
	 * @param <b>subject</b> atributo do tipo <i>String</i>.
	 * @param <b>template</b> atributo do tipo <i>String</i>.
	 * @param <b>model</b> atributo do tipo <i>Mapeamento</i>.
	 */
	public void sendMessage(String from, String[] to, String subject, String template, Map<String, Object> model) {		
		sendMessage(from, to, null, null, subject, template, model);
	}

	/**
	 * Método sobrecarregado para envio de vários e-mails com cópia.
	 * @param <b>from</b> atributo do tipo <i>String</i>.
	 * @param <b>to</b> atributo do tipo <i>Vetor de Strings</i>.
	 * @param <b>cc</b> atributo do tipo <i>Vetor de Strings</i>.
	 * @param <b>bcc</b> atributo do tipo <i>Vetor de Strings</i>.
	 * @param <b>subject</b> atributo do tipo <i>String</i>.
	 * @param <b>template</b> atributo do tipo <i>String</i>.
	 * @param <b>model</b> atributo do tipo <i>Mapeamento</i>.
	 */
	public void sendMessage(final String from, final String[] to, final String[] cc, final String[] bcc, final String subject, String template, Map<String, Object> model) {
		try {
			final String body = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, model);
			MimeMessagePreparator preparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
					message.setFrom(from);
					message.setTo(to);
					if(cc != null && cc.length > 0) message.setCc(cc);
				    if(bcc != null && bcc.length > 0) message.setBcc(bcc);
				    message.setSubject(subject);
					message.setText(body, true);
			 	}
			};			
		  	PendingMail pendingMail = new PendingMail(preparator);
			pool.submit(pendingMail);	
		}
		catch(Throwable e){
			LOGGER.error("Submit email message exception!", e);
		}
	}
	
		
	/**
	 * Método sobrecarregado para envio de um e-mail recebendo como parâmetro uma <i>String</i> já contendo o corpo da mensagem.
	 * @param <b>from</b> atributo do tipo <i>String</i>.
	 * @param <b>to</b> atributo do tipo <i>String</i>.
	 * @param <b>subject</b> atributo do tipo <i>String</i>.
	 * @param <b>body</b> atributo do tipo <i>String</i>.
	 */
	public void sendMessage(String from, String to, String subject, String body) {
		sendMessage(from, new String[]{to}, null, null, subject, body);
	}
	
	/**
	 * Método sobrecarregado para envio de vários e-mails, recebendo como parâmetro uma <i>String</i> já contendo o corpo da mensagem.
	 * @param <b>from</b> atributo do tipo <i>String</i>.
	 * @param <b>to</b> atributo do tipo <i>String</i>.
	 * @param <b>subject</b> atributo do tipo <i>String</i>.
	 * @param <b>body</b> atributo do tipo <i>String</i>.
	 */
	public void sendMessage(String from, String[] to, String subject, String body) {
		sendMessage(from, to, null, null, subject, body);
		
	}
	
	/**
	 * Método sobrecarregado para envio de vários e-mails com cópia, recebendo como parâmetro uma <i>String</i> já contendo o corpo da mensagem.
	 * @param <b>from</b> atributo do tipo <i>String</i>.
	 * @param <b>to</b> atributo do tipo <i>String</i>.
	 * @param <b>cc</b> atributo do tipo <i>Vetor de Strings</i>.
	 * @param <b>bcc</b> atributo do tipo <i>Vetor de Strings</i>.
	 * @param <b>subject</b> atributo do tipo <i>String</i>.
	 * @param <b>body</b> atributo do tipo <i>String</i>.
	 */
	public void sendMessage(final String from, final String[] to, final String[] cc, final String[] bcc, final String subject, final String body) {
		try{
			MimeMessagePreparator preparator = new MimeMessagePreparator(){
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
					message.setFrom(from);
					message.setTo(to);
					if(cc != null && cc.length > 0) message.setCc(cc);
				    if(bcc != null && bcc.length > 0) message.setBcc(bcc);
				    message.setSubject(subject);
					message.setText(body, true);
			 	}
			};
			PendingMail pendingMail = new PendingMail(preparator);
			pool.submit(pendingMail);
		}catch(Throwable e){
			LOGGER.error("Submit email scheduling message exception!", e);
		}
	}
		
	/**
	 * @author SmartSW
	 * </br></br>
	 * Classe de preparação para envio da mensagem. 
	 * <table border = 1>
	 * <tr><td colspan =2>Implements :</td></tr>
	 * <tr><td>Callable - MimeMessagePreparator</td></tr>
	 * </table>
	 */
	 private class PendingMail implements Callable<MimeMessagePreparator> {
		MimeMessagePreparator preparator;
		
		/**
		 * Construtor padrão com parâmetros.
		 * @param <b>preparator</b> Atributo <i>preparator</i> do tipo <i>MimeMessagePreparator</i>.
		 */
		public PendingMail(MimeMessagePreparator preparator){
			this.preparator = preparator;			
		}
		
		/**
		 * Método de chamada para o envio da mensagem.
		 * @return <b>MimeMessagePreparator</b> Retorna um <i>MimeMessagePreparator</i> com a chamada de envio da mensagem.
		 */
		public MimeMessagePreparator call() throws Exception {
			try {				
				mailSender.send(preparator);
				LOGGER.info("Email message sent successfully!");
			} catch(Exception e){
				LOGGER.error("Sending email message exception!", e);
				throw e;
			}
			return preparator;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub		
	}

}
