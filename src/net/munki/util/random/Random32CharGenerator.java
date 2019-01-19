/*
 * Random32CharGenerator.java - generates 32 char strings randomly
 *
 * Created on 08 August 2002, 18:24
 *
 * Copyright (C) 2002 by munki
 *
 */

package net.munki.util.random;

/** Generates random 32 character Strings
 */
public class Random32CharGenerator extends RandomGenerator {

    /** Creates new KeyGenerator */
    public Random32CharGenerator() {
        super();
    }

    /** Generates the random String
     * @return Random 32 character String
     */    
    public String generate() {
        return super.generateCode(32);
    }
}
