/*
 * VanillaFilter.java - implements Filter and is a simple pass thru
 *
 * Created on 13 August 2002, 18:24
 *
 * Copyright (C) 2002 by munki
 *
 */

package net.munki.util.classloader;

/** Implements Filter to provide a simple, does nothing,
 * pass thru filter.
 */
public class VanillaFilter implements Filter {

    /** Provides a simple, does nothing, pass thru filter
     * by simply returning the passed byte[].
     * @param bytes The byte[] to transform
     * @return The transformed byte[]
     */    
    public byte[] transformBytes(byte[] bytes) {
        return bytes;
    }
    
}
