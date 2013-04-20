/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blurryroots.yanecos.core;

/**
 * Base Class for every Data added to an entity.
 *
 * @author Sven Freiberg
 */
public 
abstract class Data implements Cloneable {
    /**
     * ???
     */
    boolean _isActive;

    /**
     * Standard constructor.
     */
    public 
    Data() {
        //
        _isActive = true;
    }

    /**
     * Activates the component.
     */
    public 
    void activate() {
        //
        _isActive = true;
    }

    /**
     * Deactivates the component.
     */
    public
    void deactivate() {
        //
        _isActive = false;
    }

    /**
     * Toggles the status of the component.
     */
    public 
    void toggleActive() {
        //
        _isActive = !_isActive;
    }

    /**
     * Returns true if active, false if not.
     *
     * @return boolean
     */
    public
    boolean isActive() {
        //
        return _isActive;
    }

    /**
     * Returns a clone (deep copy) of the data.
     *
     * @return Object
     */
    @Override
    public 
    abstract Object clone();    
}
