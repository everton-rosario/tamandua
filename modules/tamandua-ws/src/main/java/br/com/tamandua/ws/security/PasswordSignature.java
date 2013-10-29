package br.com.tamandua.ws.security;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.security.provider.DSAPrivateKey;
import sun.security.provider.DSAPublicKey;

/**
 * 
 * @author SmartSW
 * </br></br>
 * Classe Utilitária para Assinaturas de Password.
 */
public class PasswordSignature  {
	
	private final byte[] PRIVATE_KEY = {48,-127,-58,2,1,0,48,-127,-88,6,7,42,-122,72,-50,56,4,1,48,-127,-100,2,65,0,-4,-90,-126,-50,-114,18,-54,-70,38,-17,-52,-9,17,14,82,109,-80,120,-80,94,-34,-53,-51,30,-76,-94,8,-13,-82,22,23,-82,1,-13,91,-111,-92,126,109,-10,52,19,-59,-31,46,-48,-119,-101,-51,19,42,-51,80,-39,-111,81,-67,-60,62,-25,55,89,46,23,2,21,0,-106,46,-35,-52,54,-100,-70,-114,-69,38,14,-26,-74,-95,38,-39,52,110,56,-59,2,64,103,-124,113,-78,122,-100,-12,78,-23,26,73,-59,20,125,-79,-87,-86,-14,68,-16,90,67,77,100,-122,-109,29,45,20,39,27,-98,53,3,11,113,-3,115,-38,23,-112,105,-77,46,41,53,99,14,28,32,98,53,77,13,-94,10,108,65,110,80,-66,121,76,-92,4,22,2,20,44,-30,79,74,96,-82,-91,-63,5,-48,84,18,-67,-86,115,-57,20,-126,-104,-108};
	private final byte[] PUBLIC_KEY = {48,-127,-16,48,-127,-88,6,7,42,-122,72,-50,56,4,1,48,-127,-100,2,65,0,-4,-90,-126,-50,-114,18,-54,-70,38,-17,-52,-9,17,14,82,109,-80,120,-80,94,-34,-53,-51,30,-76,-94,8,-13,-82,22,23,-82,1,-13,91,-111,-92,126,109,-10,52,19,-59,-31,46,-48,-119,-101,-51,19,42,-51,80,-39,-111,81,-67,-60,62,-25,55,89,46,23,2,21,0,-106,46,-35,-52,54,-100,-70,-114,-69,38,14,-26,-74,-95,38,-39,52,110,56,-59,2,64,103,-124,113,-78,122,-100,-12,78,-23,26,73,-59,20,125,-79,-87,-86,-14,68,-16,90,67,77,100,-122,-109,29,45,20,39,27,-98,53,3,11,113,-3,115,-38,23,-112,105,-77,46,41,53,99,14,28,32,98,53,77,13,-94,10,108,65,110,80,-66,121,76,-92,3,67,0,2,64,90,-56,-43,45,-15,6,-125,85,94,-53,-68,2,67,91,29,-24,116,-97,-25,33,7,-68,-93,-102,-37,121,-59,-112,38,-69,33,-33,-111,59,-79,16,-85,-22,-23,23,1,-16,97,87,-79,-49,-82,-8,-99,24,3,30,-50,-128,88,33,102,-85,-86,-66,36,57,44,-4};
	
	private static final String ALGORITHM = "SHAwithDSA";
	
	private static PasswordSignature instance;
	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordSignature.class);
	
	private PasswordSignature(){ }
	
	/**
	 * Método Estático que retorna uma instância Singleton de <i>PasswordSignature</i>.
	 * @return <b>PasswordSignature</b> Instância da Classe <i>PasswordSignature</i>.
	 */
	public static PasswordSignature getInstance(){
		synchronized(PasswordSignature.class) {
			if(instance == null){
				instance = new PasswordSignature();
			}			
		}
		return instance;
	}
	
	/**
	 * Método que Retorna o Assinador.
	 * @return <b>Signature</b> Atributo do Tipo <i>Signature</i> para o uso com o password.
	 */
	private Signature getSignature() {
		try{
			return Signature.getInstance(ALGORITHM);	
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Erro ao Montar a Assinatura de Password ", e);
			throw new NotFoundAlgorithmException(ALGORITHM, e);
		}		
	}
	
	/**
	 * Método que faz uma Assinatura digital a partir do parâmetro passado.
	 * @param <b>password</b> Atributo <i>password</i> do Tipo <i>String</i> para gerar a Assinatura Digital.
	 * @return <b>Array de bytes</b> Array de <i>bite</i> contendo a Assinatura Digital.
	 */
	public byte[] sign(String password){
		try {
			Signature signature = getSignature();
			signature.initSign(new DSAPrivateKey(PRIVATE_KEY));
			signature.update(password.getBytes());
			return signature.sign();
		} catch (InvalidKeyException e) {
			LOGGER.error("Erro de Chave Privada Inválida ", e);
			throw new InvalidPrivateKeyException(e);
		} catch (SignatureException e) {
			LOGGER.error("Erro de Assinatura de Password ", e);
			throw new PasswordSignatureException("Occured a problem during signature proccess.", e);
		}
	}
	
	/**
	 * Método que verifica a validade do Password com a Assinatura.
	 * @param <b>sign</b> Array de Bites contendo a Assinatura Digital.
	 * @param <b>password</b> Atributo <i>password</i> do Tipo <i>String</i> para a verificação. 
	 * @return <b>boolean</b> Retorna <i>True</i> caso o Password seja válido e <i>False</i> caso contrário.
	 */
	public boolean verify(byte[] sign, String password){
		try {
			Signature signature = getSignature();
			signature.initVerify(new DSAPublicKey(PUBLIC_KEY));
			signature.update(password.getBytes());
			return signature.verify(sign);
		} catch (InvalidKeyException e) {
			LOGGER.error("Erro de Chave Publica Inválida  ", e);
			throw new InvalidPublicKeyException(e);
		} catch (SignatureException e) {
			LOGGER.error("Erro de Assinatura de Password ", e);
			throw new PasswordSignatureException("Occured a problem during verifing proccess.", e);
		}
	}
}
