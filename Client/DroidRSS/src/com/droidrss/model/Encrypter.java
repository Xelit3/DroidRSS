/* Encrypter.java
* @author: Xavi Rueda
* @date: 5-10-2013
* @description: This class provides a method to return a hash in SHA1 of a String
*/

package com.droidrss.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// TODO: Auto-generated Javadoc
/**
 * The Class Encrypter.
 */
public class Encrypter {
	
	/**
	 * Gets the ecnrypted in SHA1 message
	 *
	 * @param message the message
	 * @return the hash
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	public static String getHash(String message) throws NoSuchAlgorithmException {
		MessageDigest md;
		byte[] buffer, digest;
		String hash;
		
		hash = new String();
		buffer = message.getBytes();
		md = MessageDigest.getInstance("SHA1");
		md.update(buffer);
		digest = md.digest();

		for(byte aux : digest) {
			int b = aux & 0xff;
			if (Integer.toHexString(b).length() == 1) hash += "0";
			hash += Integer.toHexString(b);
		}
		
		return hash;}
}