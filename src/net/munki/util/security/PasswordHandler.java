/*
 * PasswordHandler.java
 *
 * Created on 23 June 2001, 15:02
 */

package net.munki.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/** Provides an interface to MessageDigest for providing and verifying one way hashes.
 *
 * @author warrenm
 * @version 0.1
 */
public final class PasswordHandler {
    
    private MessageDigest md;
    
    /** Creates new PasswordHandler using MD5 algorithm */
    public PasswordHandler() {
        try {
            md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException nsae) {
            System.out.println(nsae);
        }
    }
    
    /** Creates new PasswordHandler using specified algorithm
     * @param algorithm Algorithm to use
     */
    public PasswordHandler(String algorithm) {
        try {
            md = MessageDigest.getInstance(algorithm);
        }
        catch (NoSuchAlgorithmException nsae) {
            System.out.println(nsae);
        }
    }
    
    /** Generates a hash based on a supplied password string.
     * @param password Password string
     * @return Hashed password
     */
    public String generateHash(String password) {
        md.reset();
        byte[] buf = password.getBytes();
        String hash = new String(md.digest(buf));
        return hash;
    }
    
    /** Generates a base64 encoded hash based on a supplied password string.
     * @param password Password string
     * @return Hashed password
     */
    public String generateHashBase64(String password) {
        md.reset();
        byte[] buf = password.getBytes();
        String hash = new String(md.digest(buf));
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(hash.getBytes());
    }
    
    /** Verifies a password against a hashed password.
     * @param password Supplied password
     * @param hash1 Hash against which to verify the password
     * @return True or False
     */
    public boolean authenticate(String password, String hash1) {
        boolean returnValue = true;
        String hash2 = this.generateHash(password);
        byte[] hashBuffer1 = hash1.getBytes();
        byte[] hashBuffer2 = hash2.getBytes();
        if (hashBuffer1.length != hashBuffer2.length) {
            returnValue = false;
        }
        else {
            for (int i = 0; i < hashBuffer1.length; i++) {
                if (hashBuffer1[i] != hashBuffer2[i]) {
                    returnValue = false;
                    break;
                }
            }
        }
        return returnValue;
    }

    /** Verifies a password against a base64 encoded hashed password.
     * @return True or False
     * @param b64hash1 The base64 encoded hashed password
     * @param password Supplied password
     */
    public boolean authenticateBase64(String password, String b64hash1) {
        boolean returnValue = true;
        String b64hash2 = this.generateHashBase64(password);
        byte[] hashBuffer1 = b64hash1.getBytes();
        byte[] hashBuffer2 = b64hash2.getBytes();
        if (hashBuffer1.length != hashBuffer2.length) {
            returnValue = false;
        }
        else {
            for (int i = 0; i < hashBuffer1.length; i++) {
                if (hashBuffer1[i] != hashBuffer2[i]) {
                    returnValue = false;
                    break;
                }
            }
        }
        return returnValue;
    }
}
