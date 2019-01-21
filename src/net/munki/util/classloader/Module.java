/*
 * Module.java - implements the Loadable interface defining a loadable module class
 *
 * Created on 08 August 2002, 18:24
 *
 * Copyright (C) 2002 by munki
 *
 */

package net.munki.util.classloader;

public class Module implements Loadable {

    public Module() {
    }

    public boolean start() {
        return true;
    }
    
}
