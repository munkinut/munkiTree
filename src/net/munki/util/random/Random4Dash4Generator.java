/*
 * Random4Dash4Generator.java - generates 4-4 char strings randomly
 *
 * Created on 08 August 2002, 18:24
 *
 * Copyright (C) 2002 by munki
 *
 */

package net.munki.util.random;

import net.munki.util.string.StringTool;

/** Generates random 4-4 character Strings
 */
public class Random4Dash4Generator extends RandomGenerator {
    
    /** Creates a new random String generator
     */    
    public Random4Dash4Generator() {
        super();
    }
    
    /** Generate the random String
     * @return Random String
     */    
    public String generate() {
        return StringTool.cat(new String[] {super.generateCode(4), "-", super.generateCode(4)});
    }
}