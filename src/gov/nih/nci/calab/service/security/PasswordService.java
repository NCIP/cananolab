package gov.nih.nci.calab.service.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

/**
 * This class will provide a utility to provide one way encryption of the user's passwords
 * for authentication purposes.
 * 
 * @author shinohaa
 * @author doswellj
 *
 */
public final class PasswordService 
{
    
    private static final String ENCRYPTION_TYPE = "SHA";
    private static PasswordService instance;

    private static Logger logger = Logger.getLogger(PasswordService.class);
    
   
    private PasswordService() 
    {

    }

    /**
     * Uses SHA to encrypt a password.
     * @param strplaintext Accepts a password as a string
     * @return encrypted password
     */
    public synchronized String encrypt(String strplaintext) {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(ENCRYPTION_TYPE); 
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error with password algorithm type", e);
        }
        try {
            md.update(strplaintext.getBytes("UTF-8")); 
        } catch (UnsupportedEncodingException e) {
            logger.error("Problem with encoding type", e);
        }

        byte raw[] = md.digest(); 
        String hash = (new BASE64Encoder()).encode(raw); 
        return hash; 
    }

    /**
     * Returns an instance of the PasswordService class.
     * @return  instance of PasswordService
     */
    public static synchronized PasswordService getInstance() // step 1
    {

        if (instance == null) {
            instance = new PasswordService();
        }
        return instance;
    }
}
