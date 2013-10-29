package br.com.tamandua.ws.security;

/**
 * 
 * @author SmartSW
 * </br></br>
 * Exceção Lançada para Chave Pública Inválida.
 * <table border = 1>
 * <tr><td >Extends :</td> <td>RuntimeException</td></tr>
 * </table>
 *
 */
public class InvalidPublicKeyException extends RuntimeException {

	private static final long serialVersionUID = -1974112511122211699L;

	/**
	 * Construtor Padrão com a Causa da Exceção.
	 * @param <b>cause</b> causa da Exceção gerada.
	 */
	public InvalidPublicKeyException(Throwable cause){
		super("Public key is not a valid.", cause);
	}
}
