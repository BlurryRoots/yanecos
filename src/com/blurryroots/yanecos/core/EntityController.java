/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blurryroots.yanecos.core;

import com.blurryroots.yanecos.exception.EntityAlreadHasDataException;
import com.blurryroots.yanecos.exception.NoSuchDataOnEntityException;
import com.blurryroots.yanecos.exception.NoSuchEntityException;

//TODO: remove in favour for IEntity interface and its hidden impl
/**
 *
 * @author Sven Freiberg
 */
public 
class EntityController {
    //
    private static EntityController _instance;
    /**
     * Holds a reference to the central control unit for the data/processor
     * system.
     */
    private DataCenter _dataCenter;
    /**
     * Holds the id of the currently controlled entity.
     */
    private Long _id;
    private boolean _hasChanged;

    //
    private 
    EntityController( DataCenter someDataCenter ) {
        //
        _dataCenter = someDataCenter;
    }

    //
    /**
     * Creates a new instance of an EntityControler.
     *
     * @param someDataCenter The data center this EntityControler is attachted
     * to.
     */
    static void instantiate( DataCenter someDataCenter ) {
        //
        _instance = new EntityController( someDataCenter );
    }

    //
    /**
     * Sets the id of an entity to peform actions on it.
     *
     * @param someId Entity id to pefrom actions on.
     */
    public 
    static void setEntity( Long someId ) {
        //
        _instance._id = someId;
    }

    //
    /**
     * The id of an entity.
     *
     * @return Long
     */
    public 
    static Long getId() {
        //
        return _instance._id;
    }

    //
    /**
     * The name of an entity.
     *
     * @return String
     */
    public 
    static String getName() {
        //
        return _instance._dataCenter.getEntityManager().getEntityName(
                _instance._id );
    }

    //
    /**
     * Returns the given data of an entity.
     *
     * @param <T>
     * @param someDataClassType
     * @return Data Component
     */
    public 
    static <T extends Data> T getData( Class<T> someDataClassType )
            throws NoSuchEntityException,
                   NoSuchDataOnEntityException {
        //
        return _instance._dataCenter.getEntityManager().getDataFromEntity(
                someDataClassType, _instance._id );
    }

    //
    /**
     * Adds the given data to an entity.
     *
     * @param <T> T extends Data.
     * @param someData Some data to add.
     */
    public 
    static <T extends Data> void addData( T someData )
            throws NoSuchEntityException, EntityAlreadHasDataException {
        //
        _instance._dataCenter.getEntityManager().addDataToEntity(
                _instance._id, someData );
        //
        _instance._hasChanged = true;
    }

    //
    /**
     * Removes the data of given type from an entity.
     *
     * @param <T> T extends Data.
     * @param someDataClassType Some type of data to be removed.
     */
    public 
    static <T extends Data> void removeData(
            Class<T> someDataClassType ) throws Exception {
        //
        _instance._dataCenter.getEntityManager().removeDataFromEntity(
                _instance._id, someDataClassType );
        //
        _instance._hasChanged = true;
    }

    //
    /**
     * Unsets the id reference.
     */
    public 
    static void unset() {
        //
        if( _instance._hasChanged ) {
            //
            _instance._dataCenter.onEntityChanged( _instance._id );
            _instance._hasChanged = false;
        }

        //                
        _instance._id = null;
    }
}
