package net.munki.util.security;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.spec.*;

import java.io.*;

@SuppressWarnings("unused")
public class Blowfish {

    public static String encrypt(String toEncrypt, String key) throws Exception {
        return crypt(toEncrypt, key, Cipher.ENCRYPT_MODE);    
    }

    public static String decrypt(String toDecrypt, String key) throws Exception {
        return crypt(toDecrypt, key, Cipher.DECRYPT_MODE);    
    }

    public static byte[] encrypt(byte[] toEncrypt, byte[] key)
            throws Exception {
        return crypt(toEncrypt, key, Cipher.ENCRYPT_MODE);    
    }

    public static byte[] decrypt(byte[] toDecrypt, byte[] key) throws Exception {
        return crypt(toDecrypt, key, Cipher.DECRYPT_MODE);    
    }

    private static String crypt(String toCrypt, String key, int mode) throws Exception {
        
        byte[] bytesToCrypt = toCrypt.getBytes();
        byte[] bytesKey = key.getBytes();
        byte[] result = crypt(bytesToCrypt, bytesKey, mode);
        return new String(result);
    }

    private static byte[] crypt(byte[] input, byte[] key, int mode) throws Exception {

    	// TODO : rewrite this next line using javax.crypto
        Provider sunJce = new net.munki.util.security.SunJCE();
        Security.addProvider(sunJce);
         
        javax.crypto.KeyGenerator kgen = javax.crypto.KeyGenerator.getInstance("Blowfish");

        kgen.init(448);
        SecretKey skey = kgen.generateKey();

        byte[] raw = key;
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "Blowfish");
            
        Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        cipher.init(mode, skeySpec);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayInputStream bis = new ByteArrayInputStream(input);
        CipherOutputStream cos = new CipherOutputStream(bos, cipher);

        int length = 0;
        byte[] buffer =  new byte[8192];

        while ((length = bis.read(buffer)) != -1) {
           cos.write(buffer, 0, length);
        }

        bis.close();
        cos.close();

        return bos.toByteArray();
    }

}
