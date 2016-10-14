package gov.nih.nci.cananolab.datamigration.util;

import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.Provider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AESEncryption {

	private Cipher encryptCipher;
	private Cipher decryptCipher;
	private Provider provider;
	private static final String AES_ENCRYPTION_SCHEME = "AES";
	private static final String AES_ALGORITHM = "AES";
	private static final String UNICODE_FORMAT = "UTF-8";
	private static final String SALT = "SECRET INGREDIENT";
	private static final String MD5_HASH = "MD5";
	public static final String PASSWORD_HASH_ALGORITHM = "SHA-256";
	public static final String passphrase = "super secret";


	public AESEncryption() throws Exception
	{
		try
		{
			this.provider = new BouncyCastleProvider();        
			SecretKeySpec skey = getSKeySpec(passphrase, true);
			encryptCipher = Cipher.getInstance(AES_ENCRYPTION_SCHEME,provider);
			decryptCipher = Cipher.getInstance(AES_ENCRYPTION_SCHEME,provider);            
			encryptCipher.init(Cipher.ENCRYPT_MODE, skey);
			AlgorithmParameters ap = encryptCipher.getParameters();
			decryptCipher.init(Cipher.DECRYPT_MODE, skey,ap);
		}        
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public String encrypt(String unencryptedString) throws Exception {
		if (unencryptedString == null || unencryptedString.trim().length() == 0)
			throw new IllegalArgumentException("unencrypted string was null or empty");

		byte[] cleartext = null;
		byte[] ciphertext = null;
		try {
			cleartext = unencryptedString.getBytes(UNICODE_FORMAT);            
			ciphertext = encryptCipher.doFinal(cleartext);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;			
		} 
		return Base64.encodeBase64String(ciphertext);
	}

	public String decrypt(String encryptedString) throws Exception
	{	
		if (encryptedString == null || encryptedString.trim().length() <= 0)
			throw new IllegalArgumentException("encrypted string was null or empty");

		Base64 base64decoder = new Base64();
		byte[] ciphertext;
		try {		
			ciphertext = base64decoder.decode(encryptedString);		
		} catch (Exception e) {
			e.printStackTrace();
			throw e; 
		}
		byte[] cleartext = null;
		try {

			cleartext = decryptCipher.doFinal(ciphertext);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
		return bytes2String(cleartext);
	}

	private SecretKeySpec getSKeySpec(String passphrase, boolean isMD5Hash) {	        
		try 
		{
			MessageDigest md = null;
			md = MessageDigest.getInstance(MD5_HASH, provider);

			md.update((passphrase + SALT).getBytes(UNICODE_FORMAT));
			byte[] thedigest = md.digest();

			SecretKeySpec skeySpec = new SecretKeySpec(thedigest,AES_ALGORITHM);	
			return skeySpec;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;	
	}

	public static String bytes2String(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			stringBuffer.append((char) bytes[i]);
		}
		return stringBuffer.toString();
	}

}
