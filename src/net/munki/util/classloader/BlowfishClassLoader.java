/*
 * BlowfishClassLoader.java - a custom classloader that decrypts blowfish
 * encrypted classes on the fly.
 *
 * Implements the abstract CustomClassLoader by providing the 
 * transformBytes() method.  This allows whatever transformation
 * might be required on a custom class file before it gets loaded.
 *
 * Created on 08 August 2002, 18:24
 *
 * Copyright (C) 2002 by munki
 *
 */

package net.munki.util.classloader;

import net.munki.util.security.Blowfish;

/** Loads Blowfish encrypted files.
 */
public class BlowfishClassLoader extends CustomClassLoader {
    
    private byte[] key;
    
    /** Creates a new BlowfishClassLoader.
     * @param parent The parent class loader.
     * @param base The base directory from which to search for classes to load.
     * @param key The key to decrypt the encrpyted files.
     */    
    public BlowfishClassLoader(ClassLoader parent, String base, byte[] key) {
        super(parent, base);
        this.key = key;
    }

    /** Creates a new BlowfishClassLoader.
     * @param parent The parent class loader.
     * @param base The base directory from which to search for classes to load.
     */    
    public BlowfishClassLoader(ClassLoader parent, String base) {
        super(parent, base);
        this.key = null;
    }
    
    /** Creates a new BlowfishClassLoader.
     * @param parent The parent class loader.
     */    
    public BlowfishClassLoader(ClassLoader parent) {
        super(parent);
        this.key = null;
    }
    
    /** Creates a new BlowfishClassLoader
     */    
    public BlowfishClassLoader() {
        super();
        this.key = null;
    }
    
    /** Sets the key used to decrypt the encrypted classes.
     * @param key The key.
     */    
    public void setKey(byte[] key) {
        this.key = key;
    }
    
    protected byte[] transformBytes(byte[] classBytes) {
        byte[] newClassBytes = null;
        try {
            newClassBytes = Blowfish.decrypt(classBytes, key);
        }
        catch (Exception e) {
            System.err.println("Warning: failed to decrypt class");
        }
        return newClassBytes;
    }    

}
