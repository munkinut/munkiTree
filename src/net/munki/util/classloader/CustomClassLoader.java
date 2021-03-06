/*
 * CustomClassLoader.java - loads classes
 *
 * Inspired by the example produced by Ken McCrary in this article
 * http://www.javaworld.com/javaworld/jw-03-2000/jw-03-classload.html
 *
 * This abstract class provides the bulk of the class loading mechanism
 * leaving the custom transformation of the loaded byte array to an
 * abstract method transformBytes().  This allows a modified class file 
 * to be transformed into a regular class file before being loaded.
 *
 * Created on 08 August 2002, 18:24
 *
 * Copyright (C) 2002 by munki
 *
 */

package net.munki.util.classloader;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;

/** Abstract class providing the majority of functionality
 * required to load custom classes with a .mod extension.
 *
 * Developers should subclass this class and implement
 * the following method signature:
 *
 * <code>protected byte[] transformBytes (byte[] classBytes)</code>
 */
public abstract class CustomClassLoader extends ClassLoader {
    
    private static final String DEFAULT_BASE = System.getProperty("user.dir");
    private static final String FS = System.getProperty("file.separator");
    private static final String MANIFEST = "META-INF" + FS + "MANIFEST.MF";
    
    private Manifest manifest;
    
    protected String base;
    
    /** Creates a new CustomClassLoader.
     * @param parent The parent class loader.
     * @param base The base directory from which to search for classes to load.
     */    
    public CustomClassLoader(ClassLoader parent, String base) {
        super(parent);
        this.base = base;
        init();
    }
    
    /** Creates a new CustomClassLoader.
     * @param parent The parent class loader.
     */    
    public CustomClassLoader(ClassLoader parent) {
        super(parent);
        this.base = DEFAULT_BASE;
        init();
    }
    
    /** Creates a new CustomClassLoader.
     */    
    public CustomClassLoader() {
        super();
        this.base = DEFAULT_BASE;
        init();
    }
    
    private void init() {
        
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(base + FS + MANIFEST);
            manifest = new Manifest(fis);
        }
        catch (IOException ioe) {
            // System.err.println("Warning: Unable to read manifest");
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                }
                catch (IOException ioe){
                    System.err.println("Warning: Unable to close manifest");
                }
            }
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected Class findClass(String name) throws ClassNotFoundException {
        FileInputStream fis = null;
        if (!base.endsWith(FS)) base = base.concat(FS);
        String path = name.replace('.', FS.charAt(0));
        try {
            fis = new FileInputStream(base + path + ".mod");
            byte[] classBytes = new byte[fis.available()];
            fis.read(classBytes);
            byte[] transformedBytes = transformBytes(classBytes);
            definePackage(name);
            return defineClass(name, transformedBytes, 0, transformedBytes.length);
        }
        catch (IOException ioe) {
            throw new ClassNotFoundException(name);
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                }
                catch (IOException ioe){
                    System.err.println("Warning: Unable to close class file");
                }
            }
        }
    }
    
    protected abstract byte[] transformBytes(byte[] classBytes);
    
    @SuppressWarnings("deprecation")
	protected URL findResource(String name) {
        
        File searchResource = new File(base + FS + name);
        URL result = null;
        
        if (searchResource.exists()) {
            try {
                return searchResource.toURL();
            }
            catch (MalformedURLException mfe) {
                System.err.println("Warning: Could not find resource called " + name);
            }
        }
        else {
            System.err.println("Warning: Could not find resource called " + name);
        }
        
        return result;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected Enumeration findResources(final String name) throws IOException {
        
        return new Enumeration() {
            
            URL resource = findResource(name);
            
            public boolean hasMoreElements() {
                return (resource != null);
            }
            
            public Object nextElement() {
                if (!hasMoreElements()) {
                    throw new NoSuchElementException();
                }
                else {
                    URL result = resource;
                    resource = null;
                    return result;
                }
            }
        };
    }
    
    @SuppressWarnings("deprecation")
	private void definePackage(String className) {
        
        String packageName = className;
        int index = className.lastIndexOf('.');
        if (index != -1) {
            packageName = className.substring(0, index);
        }
        
        if (manifest == null ||
            getPackage(packageName) != null) {
                return;
        }
        
        String specificationTitle, specificationVersion, specificationVendor,
               implementationTitle, implementationVersion, implementationVendor;
        
        Attributes attr = manifest.getMainAttributes();
        
        if (attr != null) {
            
            specificationTitle   = attr.getValue(Attributes.Name.SPECIFICATION_TITLE);
            specificationVersion = attr.getValue(Attributes.Name.SPECIFICATION_VERSION);
            specificationVendor  = attr.getValue(Attributes.Name.SPECIFICATION_VENDOR);
            implementationTitle   = attr.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            implementationVersion = attr.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            implementationVendor  = attr.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
            
            definePackage(packageName, specificationTitle, specificationVersion, specificationVendor,
                          implementationTitle, implementationVersion, implementationVendor, null);
        }
    }
}
