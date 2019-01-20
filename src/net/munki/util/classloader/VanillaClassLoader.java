/*
 * VanillaClassLoader.java - loads .mod classes with no transformation
 *
 * Created on 08 August 2002, 18:24
 *
 * Copyright (C) 2002 by munki
 *
 */

package net.munki.util.classloader;

/** Loads vanilla custom classes with the .mod extension.
 */
public class VanillaClassLoader extends CustomClassLoader {

    /** Creates a new VanillaClassLoader.
     * @param parent The parent class loader.
     * @param base The base directory from which to search for classes to load.
     */    
    public VanillaClassLoader(ClassLoader parent, String base) {
        super(parent, base);
    }
    
    /** Creates a new VanillaClassLoader.
     * @param parent The parent class loader.
     */    
    public VanillaClassLoader(ClassLoader parent) {
        super(parent);
    }
    
    /** Creates a new VanillaClassLoader.
     */    
    public VanillaClassLoader() {
        super();
    }

    protected byte[] transformBytes(byte[] classBytes) {
        return classBytes;
    }
    
}
