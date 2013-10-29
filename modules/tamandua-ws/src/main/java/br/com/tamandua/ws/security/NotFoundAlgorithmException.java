package br.com.tamandua.ws.security;

/**
 * 
 * @author SmartSW
 * </br></br>
 * Exceção Lançada quando não encontrado o Algorítmo de Criptografia.
 * <table border = 1>
 * <tr><td >Extends :</td> <td>RuntimeException</td></tr>
 * </table>
 *
 */
public class NotFoundAlgorithmException extends RuntimeException {

	private static final long serialVersionUID = 5014414645784190012L;

	/**
	 * Construtor padrão para Exceção com mensagem do Algorítmo e a Causa da Exceção.
	 * @param <b>algorithm</b> atributo <i>algorithm</i> do Tipo <i>String</i> para montagem da Exceção.
	 * @param <b>cause</b> causa da Exceção gerada.
	 */
	public NotFoundAlgorithmException(String algorithm, Throwable cause){
		super("The algorithm '" + algorithm + "' was not found.", cause);
	}	
}
