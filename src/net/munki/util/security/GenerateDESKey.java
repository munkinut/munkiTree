/*
 * GenerateKey.java
 *
 * Created on 08 August 2002, 19:59
 */

package net.munki.util.security;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

import net.munki.util.io.ByteFileReaderWriter;

@SuppressWarnings("unused")
public class GenerateDESKey {
    
    static public void main( String args[] ) throws Exception {
        
        String keyFilename = args[0];
        String algorithm = "DES";
        
        SecureRandom sr = new SecureRandom();
        KeyGenerator kg = KeyGenerator.getInstance( algorithm );
        kg.init( sr );
        SecretKey key = kg.generateKey();
        
        ByteFileReaderWriter.writeFile( keyFilename, key.getEncoded() );
    }
}


