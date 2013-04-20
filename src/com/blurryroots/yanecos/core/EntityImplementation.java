/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blurryroots.yanecos.core;

import java.util.ArrayList;
import java.util.List;

import com.blurryroots.yanecos.exception.EntityAlreadHasDataException;
import com.blurryroots.yanecos.exception.NoSuchDataOnEntityException;
import com.blurryroots.yanecos.exception.NoSuchEntityException;

//TODO: make serializeable?
/**
 * A hidden (package level) implemantion of the IEntity interface.
 * Used when user want to use entities, as a 'whole'.
 * 
 * @author Sven Freiberg
 */
class EntityImplementation implements IEntity {
    /**
     * Holds a reference to the central control unit for the data/processor
     * system.
     */
    private DataCenter _dataCenter;
    /**
     * Holds the id of the currently controlled entity.
     */
    private long _id;
    /**
     * Indicates whether the entity has changed, in therms of
     * data added, or data removed. 
     */
    private boolean _hasChanged;

    /**
     * EntityImplementation constructor
     * @param someDataCenter
     * @param someId
     */
    EntityImplementation( DataCenter someDataCenter, long someId ) {
        //
        _dataCenter = someDataCenter;
        _id = someId;
    }

    /**
     * The id of an entity.
     *
     * @return Long
     */
    @Override
    public 
    long getId() {
        //
        return _id;
    }

    /**
     * The name of an entity.
     *
     * @return String
     */
    @Override
    public 
    String getName() {
        //
        return _dataCenter.getEntityManager().getEntityName( _id );
    }

    /**
     * Returns the given data of an entity.
     *
     * @param <T>
     * @param someDataClassType
     * @return Data Component
     */
    @Override
    public 
    <T extends Data> T getData( final Class<T> someDataClassType )
            throws NoSuchEntityException,
                   NoSuchDataOnEntityException {
        //
        return _dataCenter.getEntityManager().getDataFromEntity(
                someDataClassType, _id );
    }

    /**
     * Adds the given data to an entity.
     *
     * @param <T> T extends Data.
     * @param someData Some data to add.
     */
    @Override
    public 
    <T extends Data> void addData( final T someData )
            throws NoSuchEntityException, EntityAlreadHasDataException {
        //
        _dataCenter.getEntityManager().addDataToEntity(
                _id, someData );
        //
        _hasChanged = true;
    }

    /**
     * Removes the data of given type from an entity.
     *
     * @param <T> T extends Data.
     * @param someDataClassType Some type of data to be removed.
     */
    @Override
    public 
    <T extends Data> void removeData(
            final Class<T> someDataClassType ) throws Exception {
        //
        _dataCenter.getEntityManager().removeDataFromEntity(
                _id, someDataClassType );
        //
        _hasChanged = true;
    }

    /**
     * Returns all data, this entity holds.
     * @return list of data
     */
    @Override
    public 
    List<? extends Data> getAllData() {
    	//
    	List<Class<? extends Data>> typeList = 
    			_dataCenter.getEntityManager().getAllDataTypesFromEntity( _id );
    	//
    	List<Data> dataList = new ArrayList<Data>();
    	
    	//
    	for( Class<? extends Data> type : typeList ){
    		try {
				dataList.add( _dataCenter.getEntityManager().getDataFromEntity( type, _id ) );
			}
			catch( NoSuchEntityException ex ) {
				ex.printStackTrace();
			}
			catch( NoSuchDataOnEntityException ex ) {
				ex.printStackTrace();
			}
    	}
    	
    	//
    	return dataList;
    }
    
    /**
     * Tells the DataCenter to update all things concerning this entity.
     * Use this method, if you have added or removed data.
     */
    @Override
    public 
    void update() {
        //
        if( _hasChanged ) {
            //
            _dataCenter.onEntityChanged( _id );
            _hasChanged = false;
        }
    }
}
