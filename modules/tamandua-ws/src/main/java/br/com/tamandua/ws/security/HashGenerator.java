package br.com.tamandua.ws.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashGenerator {

	private static final String ALGORITHM = "MD5";
	
	private static HashGenerator instance;
	private static final Logger LOGGER = LoggerFactory.getLogger(HashGenerator.class);
	
	private HashGenerator(){ }
	
	public static HashGenerator getInstance(){
		synchronized(HashGenerator.class) {
			if(instance == null){
				instance = new HashGenerator();
			}			
		}
		return instance;
	}
	
	public byte[] generate(String value) {
		try{
			MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
			messageDigest.update(value.getBytes());
			return messageDigest.digest();
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Erro ao Montar a Gerador de Hash ", e);
			throw new NotFoundAlgorithmException(ALGORITHM, e);
		}		
	}
	
	public String toString(byte[] hash){
		StringBuilder value = new StringBuilder();
		for (int i = 0; i < hash.length; i++) {
			int parteAlta = ((hash[i] >> 4) & 0xf) << 4;
			int parteBaixa = hash[i] & 0xf;
			if (parteAlta == 0){
				value.append('0');
			}
			value.append(Integer.toHexString(parteAlta | parteBaixa));
		}
		return value.toString();
	}
}
