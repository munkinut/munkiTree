/*
 * ByteFileReaderWriter.java - reads and writes byte streams to and from files.
 *
 * Created on 08 August 2002, 18:24
 *
 * Copyright (C) 2002 by munki
 *
 */

package net.munki.util.io;

import java.io.*;

public class ByteFileReaderWriter {

    public static byte[] readFile( String filename ) throws IOException {
        File file = new File( filename );
        long len = file.length();
        byte data[] = new byte[(int)len];
        FileInputStream fin = new FileInputStream( file );
        int r = fin.read( data );
        if (r != len) {
        	fin.close();
            throw new IOException( "Only read "+r+" of "+len+" for "+file );
        }
        fin.close();
        return data;
    }
    
    public static void writeFile( String filename, byte data[] ) throws IOException {
        FileOutputStream fout = new FileOutputStream( filename );
        fout.write( data );
        fout.close();
    }
}


