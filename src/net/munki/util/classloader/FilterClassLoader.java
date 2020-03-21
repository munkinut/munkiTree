/*
 * FilterClassLoader.java - loads classes
 *
 * Inspired by the example produced by Ken McCrary in this article
 * http://www.javaworld.com/javaworld/jw-03-2000/jw-03-classload.html
 *
 * This class provides the bulk of the class loading mechanism
 * leaving the custom transformation of the loaded byte array to a
 * class implementing the Filter interface.  This allows a modified class file 
 * to be transformed into a regular class file before being loaded.
 *
 * Created on 13 August 2002, 18:24
 *
 * Copyright (C) 2002 by munki
 *
 */

package net.munki.util.classloader;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;

/** Class providing the majority of functionality
 * required to load custom classes with a .mod extension.
 *
 * Developers should provide a byte[] filter implementing the Filter
 * interface to the constructor.
 *
 */
public class FilterClassLoader extends ClassLoader {
    
    private static final String DEFAULT_BASE = System.getProperty("user.dir");
    private static final String FS = System.getProperty("file.separator");
    private static final String MANIFEST = "META-INF" + FS + "MANIFEST.MF";
    
    private Manifest manifest;
    
    private String base;
    private Filter filter;
    
    /** Creates a new CustomClassLoader.
     * @param parent The parent class loader.
     * @param base The base directory from which to search for classes to load.
     * @param filter The filter to use for transforming the custom byte[].
     */    
    public FilterClassLoader(ClassLoader parent, String base, Filter filter) {
        super(parent);
        this.base = base;
        this.filter = filter;
        init();
    }
    
    /** Creates a new CustomClassLoader.
     * @param parent The parent class loader.
     * @param base The base directory from which to search for classes to load.
     */    
    public FilterClassLoader(ClassLoader parent, String base) {
        super(parent);
        this.base = base;
        init();
    }
    
    /** Creates a new CustomClassLoader.
     * @param parent The parent class loader.
     */    
    public FilterClassLoader(ClassLoader parent) {
        super(parent);
        this.base = DEFAULT_BASE;
        init();
    }
    
    /** Creates a new CustomClassLoader.
     */    
    public FilterClassLoader() {
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
    
    private byte[] transformBytes(byte[] classBytes) {
        byte[] bytes;
        if (filter != null) {
            bytes = filter.transformBytes(classBytes);
        }
        else {
            bytes = classBytes;
        }
        return bytes;
    }
    
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
