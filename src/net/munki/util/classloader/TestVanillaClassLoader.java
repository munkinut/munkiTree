/*
 * TestVanillaClassLoader.java - demonstrates custom classloading
 *
 * Created on 08 August 2002, 18:24
 *
 * Copyright (C) 2002 by munki
 *
 */

package net.munki.util.classloader;

import java.io.IOException;
import java.lang.reflect.*;

import net.munki.util.classloader.VanillaClassLoader;
import net.munki.util.io.ByteFileReaderWriter;

@SuppressWarnings("unused")
public class TestVanillaClassLoader {
    
    private String base;
    private byte[] key;
    
    public TestVanillaClassLoader(String base) {
        this.base = base;
    }
    
    @SuppressWarnings("rawtypes")
	public Class load(String className) {
        Class myClass = null;
        try {
            Class thisClass = this.getClass();
            ClassLoader thisClassLoader = thisClass.getClassLoader();
            ClassLoader myClassLoader = new VanillaClassLoader(thisClassLoader, base);
            myClass = myClassLoader.loadClass(className);
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("Class Loader could not find the specified class " + cnfe.getMessage());
        }
        return myClass;
    }

    public static void main (String args[]) {
        
        if (args.length != 2) {
            usage();
            System.exit(1);
        }
        
        String base = args[0];
        String classToLoad = args[1];
        try {
            TestVanillaClassLoader tcl = new TestVanillaClassLoader(base);
            @SuppressWarnings("rawtypes")
			Class myClass = tcl.load(classToLoad);
            @SuppressWarnings("deprecation")
			Object o = myClass.newInstance();
            if (((Loadable)o).start()) System.out.println("Module started");
            else System.err.println("Module failed to start");
        }
        catch (InstantiationException ie) {
            System.err.println("Module could not be instantiated");
        }
        catch (IllegalAccessException iae) {
            System.err.println("Module could not be accessed");
        }
        catch (ClassCastException cce) {
            System.err.println("Module did not implement the Loadable interface");
        }
    }

    private static void usage() {
        System.out.println("Usage: java TestVanillaClassLoader <base dir> <class name>");
    }
    
}
