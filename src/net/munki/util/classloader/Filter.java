/*
 * Filter.java - interface defining methods of a classloader filter
 *
 * Created on 13 August 2002, 18:24
 *
 * Copyright (C) 2002 by munki
 *
 */

package net.munki.util.classloader;

/** Interface defining methods required for a filter for the
 * custom class loader.
 */
public interface Filter {

    /** Transform one byte array into another by means
     * of some custom filtering mechanism.
     * @param bytes The byte[] to transform.
     * @return The transformed byte[].
     */    
    public byte[] transformBytes(byte[] bytes);
    
}

