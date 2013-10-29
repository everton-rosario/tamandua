package br.com.tamandua.ws.security;

/**
 * 
 * @author SmartSW
 * </br></br>
 * Exceção Lançada para Chave Privada Inválida.
 * <table border = 1>
 * <tr><td >Extends :</td> <td>RuntimeException</td></tr>
 * </table>
 *
 */
public class InvalidPrivateKeyException extends RuntimeException {

	private static final long serialVersionUID = -7831441180259217209L;

	/**
	 * Construtor Padrão com a Causa da Exceção.
	 * @param <b>cause</b> causa da Exceção gerada.
	 */
	public InvalidPrivateKeyException(Throwable cause){
		super("Private key is not a valid.", cause);
	}
}
