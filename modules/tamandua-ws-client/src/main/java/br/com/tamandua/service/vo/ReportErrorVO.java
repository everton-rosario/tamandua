package br.com.tamandua.service.vo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="mail")
public class ReportErrorVO implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4822589557717941364L;

	private String name;
	
	private String email;
	
	private String url;
	
	private String assunto;
	
	private String mensagem;
	
	private String problema;
	

	private String descricaoProblema;
	
	
	public ReportErrorVO(){
		
	}
	
	public ReportErrorVO(String name, String email,String url, String assunto, String mensagem, String problema, String descricaoProblema){
		this.name = name;
		this.email = email;
		this.url = url;
		this.assunto = assunto;
		this.mensagem = mensagem;
		this.problema = problema;
		this.descricaoProblema = descricaoProblema;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}
	
	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	
	public String getProblema() {
		return problema;
	}

	public void setProblema(String problema) {
		this.problema = problema;
	}
	
	public String getDescricaoProblema() {
		return descricaoProblema;
	}

	public void setDescricaoProblema(String descricaoProblema) {
		this.descricaoProblema = descricaoProblema;
	}

	public String toString() {
		return "ReportErrorVO [name=" + name + "," +
				"email"	+ email + ", assunto=" + assunto +
				", url=" + url +
				", mensagem=" + mensagem +
				", problema=" + problema +
				", descricaoProblema=" + descricaoProblema +
				"]";
	}
	
	
}
