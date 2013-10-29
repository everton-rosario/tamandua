package br.com.tamandua.service.mail;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.tamandua.service.vo.ReportErrorVO;
import br.com.tamandua.service.vo.UserVO;
import br.com.tamandua.ws.utils.MailProperties;

@Service
public class UserMailService {

	@Autowired
	private MailService mailService;
	
	public UserMailService(){}
	
	/**
	 * Construtor padrão com parâmetros.
	 * @param <b>mailService</b> Atributo <i>mailService</i> do tipo <i>MailService</i>.
	 */
	public UserMailService(MailService mailService){
		this.mailService = mailService;
	}
	
	public void sendConfirmMessage(UserVO user) {
		String from = MailProperties.getInstance().getSendMail();
		String to = user.getEmail();
		String subject = MailProperties.getInstance().getRegisterConfirmSubject();
		String template = MailProperties.getInstance().getRegisterConfirmTemplate();
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", user.getName());
		model.put("rg", user.getRg());
		model.put("cpf", user.getCpf());
		model.put("email", user.getEmail());
		model.put("activationCode", user.getActivationCode());
		
		mailService.sendMessage(from, to, subject, template, model);
	}
	
	public void sendResetPassword(UserVO user) {
		String from = MailProperties.getInstance().getSendMail();
		String to = user.getEmail();
		String subject = MailProperties.getInstance().getResetPasswordSubject();
		String template = MailProperties.getInstance().getResetPasswordTemplate();
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("newPassword", user.getPassword());
		
		mailService.sendMessage(from, to, subject, template, model);
	}
	
	public void sendReportError(ReportErrorVO reportError) {
		String from = MailProperties.getInstance().getSendMail();
		String to = MailProperties.getInstance().getReceiveMail();
		String subject = reportError.getAssunto();
		String template = MailProperties.getInstance().getReceiveMailInfoError();
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", reportError.getName());
		model.put("email", reportError.getEmail());
		model.put("url", reportError.getUrl());
		model.put("mensagem", reportError.getMensagem());
		model.put("problema", reportError.getProblema());
		model.put("descricaoProblema", reportError.getDescricaoProblema());
		
		
		mailService.sendMessage(from, to, subject, template, model);
	}
	
	public void sendReportContent(ReportErrorVO reportContent) {
		String from = MailProperties.getInstance().getSendMail();
		String to = MailProperties.getInstance().getReceiveMail();
		String subject = MailProperties.getInstance().getReceiveReportContentSubject();
		String template = MailProperties.getInstance().getReceiveReportContentTemplate();
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", reportContent.getName());
		model.put("email", reportContent.getEmail());
		model.put("message", reportContent.getMensagem());
		
		mailService.sendMessage(from, to, subject, template, model);
	}
	
}
