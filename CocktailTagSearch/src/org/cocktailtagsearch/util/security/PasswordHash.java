package org.cocktailtagsearch.util.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHash {
	public static String Hashing(String raw, String salt) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(salt.getBytes());
		md.update(raw.getBytes());
		String hex = String.format("%064x", new BigInteger(1, md.digest()));
		
		return hex;
	}
}