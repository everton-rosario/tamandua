/**
 * 
 */
package br.com.tamandua.service.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author BILL
 *
 */
public class CloneUtil {

	public static Serializable clone(Serializable object) {
		try {
		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
			
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bis);
			return (Serializable) ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException("Impossible to clone object", e);
		}
	}
	
}
