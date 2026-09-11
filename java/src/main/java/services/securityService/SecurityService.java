package services.securityService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;

/**
 * Security service.
 */
public class SecurityService {
	private MessageDigest digest;
	private Encoder base64;

	public SecurityService() {
		// Initialise MessageDigest
		try {
			digest = MessageDigest.getInstance("SHA-256");
		}
		catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("SecurityService:init: SHA-256 algorithm not available.", e.getCause());
	    }
		
		// Initialise base64 encoder
		base64 = Base64.getEncoder();
	}
	
	/**
	 * Hashes an array of characters using SHA-256.
	 * @param in	A char array.
	 * @return		A byte array.
	 */
	public byte[] hash(char[] in) {
        byte[] inBytes = new byte[in.length * 2];
        for (int i = 0; i < in.length; i++) {
        	inBytes[i * 2] = (byte) (in[i] >> 8);
        	inBytes[(i * 2) + 1] = (byte) in[i];
        }
        
        byte[] hash = digest.digest(inBytes);

        Arrays.fill(inBytes, (byte) 0);
        
        return hash;
    }
	
	/**
	 * Hashes an array of characters using SHA-256
	 * and returns a base 64-encoded string representation
	 * of that hash. 
	 * @param in	The char array.
	 * @return		The string.
	 */
	public String hashToBase64(char[] in) {
		return base64.encodeToString(hash(in));
	}
	
	/**
	 * Securely erase a byte array.
	 * @param in	The byte array.
	 */
	public void clean(byte[] in) {
		for (int i = 0; i < in.length; i++)
			in[i] = (byte)0;
	}
	
	/**
	 * Securely erase a char array.
	 * @param in	The char array.
	 */
	public void clean(char[] in) {
		for (int i = 0; i < in.length; i++)
			in[i] = (byte)0;
	}
}
