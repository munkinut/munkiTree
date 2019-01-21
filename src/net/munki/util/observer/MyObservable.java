/*
 * MyObservable.java - interface implementing half of a custom observer pattern
 *
 * Copyright (C) 2001 by Warren Milburn
 *
 */

package net.munki.util.observer;

/** Interface describing the observable half of an observer pattern implementation
 */
public interface MyObservable {
    
    /** Observers register their interest in observable events
     * @param observer Observer wishing to be notified
     */    
    public void registerInterest(MyObserver observer);

}

