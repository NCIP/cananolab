package src.gov.nih.nci.calab.ui.login;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

/**
 * This class will provide a utility to provide one way encryption of the user's passwords
 * for authentication purposes.  This is a REUSABLE class.
 * 
 * @author shinohaa
 *
 */
public class PasswordService 
{

	private static final String ENCRYPTION_TYPE = "SHA";
    private static PasswordService instance;

    private static Logger logger = Logger.getLogger(PasswordService.class);
    
    private PasswordService() {

    }

    public synchronized String encrypt(String plaintext) {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(ENCRYPTION_TYPE); 
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error with password algorithm type", e);
        }
        try {
            md.update(plaintext.getBytes("UTF-8")); 
        } catch (UnsupportedEncodingException e) {
            logger.error("Problem with encoding type", e);
        }

        byte raw[] = md.digest(); 
        String hash = (new BASE64Encoder()).encode(raw); 
        return hash; 
    }

    public static synchronized PasswordService getInstance() // step 1
    {

        if (instance == null) {
            instance = new PasswordService();
        }
        return instance;
    }
	
}
