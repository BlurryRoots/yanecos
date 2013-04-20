/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blurryroots.yanecos.core;

import java.util.List;

import com.blurryroots.yanecos.exception.EntityAlreadHasDataException;
import com.blurryroots.yanecos.exception.NoSuchDataOnEntityException;
import com.blurryroots.yanecos.exception.NoSuchEntityException;

/**
 * An interface to use an entity as a 'whole'.
 * 
 * @author Sven Freiberg
 */
public 
interface IEntity {

    /**
     * Adds the given data to an entity.
     *
     * @param <T> T extends Data.
     * @param someData Some data to add.
     */
    <T extends Data> void addData( final T someData ) 
    		throws NoSuchEntityException, EntityAlreadHasDataException;

    /**
     * Returns the given data of an entity.
     *
     * @param <T>
     * @param someDataClassType
     * @return Data Component
     */
    <T extends Data> T getData( final Class<T> someDataClassType ) 
    		throws NoSuchEntityException, NoSuchDataOnEntityException;

    /**
     * The id of an entity.
     *
     * @return Long
     */
    long getId();

    /**
     * The name of an entity.
     *
     * @return String
     */
    String getName();

    /**
     * Removes the data of given type from an entity.
     *
     * @param <T> T extends Data.
     * @param someDataClassType Some type of data to be removed.
     */
    <T extends Data> void removeData( final Class<T> someDataClassType )
    		throws Exception;
    
    /**
     * Return a list containing all data attached to the entity.
     * 
     * @return List of data
     */
    List<? extends Data> getAllData();

    /**
     * Propagate all changes made to this entity.
     */
    void update();
    
}
