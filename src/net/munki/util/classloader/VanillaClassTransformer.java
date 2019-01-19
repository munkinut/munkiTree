/*
 * VanillaClassTransformer.java - converts classes to module format
 *
 * This class simply renames .class files to .mod files for use with the 
 * VanillaClassLoader custom classloader.
 *
 * Created on 08 August 2002, 18:24
 *
 * Copyright (C) 2002 by munki
 *
 */

package net.munki.util.classloader;

import java.io.IOException;

import net.munki.util.io.ByteFileReaderWriter;

/** Transforms class files to vanilla modules.
 *
 * Usage: <code>java VanillaClassTransformer <classfile></code>
 */
public class VanillaClassTransformer {

    /** Runs the BlowfishClassTransformer.
     * @param args The commandline arguments.
     */    
    public static void main (String args[]) {
    
        if (args.length != 1) {
            usage();
            System.exit(1);
        }
        
        String classFilename = args[0];
        
        if (!classFilename.endsWith(".class")) {
            usage();
            System.exit(1);
        }
        
        String modFilename = modify(classFilename);
        
        byte[] uncrypted = null;
        byte[] encrypted = null;

        try {
            uncrypted = ByteFileReaderWriter.readFile(classFilename);
        }
        catch (IOException ioe) {
            System.err.println("Failed to read class or key file");
            System.exit(1);
        }
        
        try {
            if (uncrypted != null) encrypted = uncrypted;
        }
        catch (Exception e) {
            System.err.println("Failed to encrypt class");
            System.exit(1);
        }
        
        try {
            if (encrypted != null) ByteFileReaderWriter.writeFile(modFilename, encrypted);
        }
        catch (IOException ioe) {
            System.err.println("Failed to write encrypted class");
            System.exit(1);
        }
        
    }
    
    private static void usage() {
        System.out.println("java VanillaClassTransformer <class file>");
    }
    
    private static String modify(String filename) {
        String temp = filename.substring(0,filename.indexOf(".class"));
        StringBuffer buf = new StringBuffer(temp);
        buf.append(".mod");
        return buf.toString();
    }

}
