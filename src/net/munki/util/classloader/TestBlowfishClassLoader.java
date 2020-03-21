/*
 * TestBlowfishClassLoader.java - demonstrates custom classloading with encrypted classes
 *
 * Created on 08 August 2002, 18:24
 *
 * Copyright (C) 2002 by munki
 *
 */

package net.munki.util.classloader;

import java.io.IOException;
import java.lang.reflect.*;

import net.munki.util.classloader.BlowfishClassLoader;
import net.munki.util.io.ByteFileReaderWriter;

@SuppressWarnings("unused")
public class TestBlowfishClassLoader {
    
    private String base;
    private byte[] key;
    
    public TestBlowfishClassLoader(String base, byte[] key) {
        this.base = base;
        this.key = key;
    }
    
    @SuppressWarnings("rawtypes")
	public Class load(String className) {
        Class myClass = null;
        try {
            Class thisClass = this.getClass();
            ClassLoader thisClassLoader = thisClass.getClassLoader();
            ClassLoader myClassLoader = new BlowfishClassLoader(thisClassLoader, base, key);
            myClass = myClassLoader.loadClass(className);
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("Class Loader could not find the specified class " + cnfe.getMessage());
        }
        return myClass;
    }

    public static void main (String args[]) {
        
        if (args.length != 3) {
            usage();
            System.exit(1);
        }
        
        String base = args[0];
        String keyFile = args[1];
        String classToLoad = args[2];
        byte[] key = null;
        
        try {
            key = ByteFileReaderWriter.readFile(keyFile);
        }
        catch (IOException ioe) {
            System.err.println("Error: could not read the key file");
            System.exit(1);
        }

        if (key != null) {
            try {
                TestBlowfishClassLoader tcl = new TestBlowfishClassLoader(base, key);
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
        else {
            System.out.println("Error: key was null, class loading aborted");
            System.exit(1);
        }
    }

    private static void usage() {
        System.out.println("Usage: java TestBlowfishClassLoader <base dir> <key file> <class name>");
    }
    
}
