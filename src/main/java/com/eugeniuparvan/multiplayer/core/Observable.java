package com.eugeniuparvan.multiplayer.core;

import java.io.Serializable;
import java.util.Vector;

/**
 * @see java.util.Observable
 */
public class Observable implements Serializable
{
    private static final long serialVersionUID = -2273128155493268686L;
    
    private transient Boolean changed = false;
    private transient Vector<Observer> obs;
    
    public Observable()
    {
	obs = new Vector<>();
    }
    
    public synchronized void addObserver(Observer o)
    {
	if (o == null)
	    throw new NullPointerException();
	if (!obs.contains(o))
	{
	    obs.addElement(o);
	}
    }
    
    public synchronized void deleteObserver(Observer o)
    {
	obs.removeElement(o);
    }
    
    public void notifyObservers()
    {
	notifyObservers(null);
    }
    
    public void notifyObservers(Object arg)
    {
	Object[] arrLocal;
	
	synchronized (this)
	{
	    if (!changed)
		return;
	    arrLocal = obs.toArray();
	    clearChanged();
	}
	
	for (int i = arrLocal.length - 1; i >= 0; i--)
	    ((Observer) arrLocal[i]).update(this, arg);
    }
    
    public synchronized void deleteObservers()
    {
	obs.removeAllElements();
    }
    
    protected synchronized void setChanged()
    {
	changed = true;
    }
    
    protected synchronized void clearChanged()
    {
	changed = false;
    }
    
    public synchronized boolean hasChanged()
    {
	return changed;
    }
    
    public synchronized int countObservers()
    {
	return obs.size();
    }
}
