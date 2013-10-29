package br.com.tamandua.ws.security;

/**
 * 
 * @author SmartSW
 * </br></br>
 * Exceção para Erro de Assinatura de Password.
 * <table border = 1>
 * <tr><td >Extends :</td> <td>RuntimeException</td></tr>
 * </table>
 *
 */
public class PasswordSignatureException extends RuntimeException {

	private static final long serialVersionUID = 6245931481360438862L;

	/**
	 * Construtor padrão para Exceção com mensagem e a Causa da Exceção.
	 * @param <b>message</b> atributo <i>message</i> do Tipo <i>String</i> para montagem da Exceção.
	 * @param <b>cause</b> causa da Exceção gerada.
	 */
	public PasswordSignatureException(String message, Throwable cause){
		super(message, cause);
	}
}
