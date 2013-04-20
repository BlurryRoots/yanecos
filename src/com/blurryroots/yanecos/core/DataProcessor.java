/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blurryroots.yanecos.core;

import gnu.trove.list.TLongList;

import java.util.ArrayList;
import java.util.List;

//TODO: make serializeable
/**
 * Base class for every processor considered to perform any action on a
 * component from an entity.
 *
 * @author Sven Freiberg
 */
public 
abstract class DataProcessor {
    /**
     * Holds a reference to the data center this processor is attached to.
     */
    private DataCenter _dataCenter;
    /**
     * Holds the id of this processor.
     */
    private Long _id;
    /**
     * Holds the state of activeness.
     */
    private boolean _isActive;
    /**
     * A list of data types this processor is interested in.
     */
    private List<Class<? extends Data>> _interestedDataList;

    /**
     * Creates a new processor.
     */
    public
    DataProcessor() {
        //       
        _isActive = true;
        _interestedDataList = new ArrayList<Class<? extends Data>>();
    }

    /**
     * Tells the DataCenter, which entites this processor is interested in.
     * @param someInterestedData
     */
    public
    void registerInterestIn( Class<? extends Data>... someInterestedData ) {
        //iterate over the array of data types 
        for( int i = 0; i < someInterestedData.length; ++i ) {
            //and add them to the interest list
            _interestedDataList.add( someInterestedData[i] );
        }
        //if there are actually things to be interested in,
        //tell the DataCenter to update the interest
        if( ! _interestedDataList.isEmpty() ) {
            //
            _dataCenter.updateProcessorInterest( _id );
        }
    }

    /**
     * Event invoked on being attached to a DataCenter.
     *
     * @param someId An id for the new processor.
     * @param someDataCenter The data center to be attach to.
     */
    void onAttach( Long someId, DataCenter someDataCenter ) {
        //give this processor an id
        _id = someId;
        //give this processor a refernece to the DataCenter
        _dataCenter = someDataCenter;
        
        //invoke the initialize method
        this.onInitialize();
    }

    /**
     * Processes all the things this processor is ought to process.
     */
    public 
    void process() {
        //if this processor is active
        if( _isActive ) {
            //let him process
            this.onProcessing();
        }
    }

    /**
     * Houses all the initializations for the processor, such as registering
     * interest in a specific group of entities.
     */
    protected 
    abstract void onInitialize();

    /**
     * Processes the processor's logic.
     */
    protected 
    abstract void onProcessing();

    /**
     * Returns the world on which this system is attached to.
     *
     * @return the data center
     */
    protected 
    DataCenter getDataCenter() {
        //
        return _dataCenter;
    }

    /**
     * Returns a list of entity ids, this processor is interested in.
     * @return a list of interested entity ids
     */
    public 
    TLongList getInterestedEntityIds() {
        //
        return _dataCenter.getInterestedEntitiesFor( _id );
    }

    /**
     * Returns a list of entities, this processor is interested in.
     * @return a list of interested entities
     */
    public 
    List<IEntity> getInterestedEntities() {
        //create an empty list to hold the entities
        final List<IEntity> entities = new ArrayList<IEntity>();
        //retrieve the list of interested entity ids
        final TLongList entityIds = _dataCenter.getInterestedEntitiesFor( _id );
        //get the number of interested entity ids
        final int size = entityIds.size();

        //iterate over the list of interested entity ids
        for( int i = 0; i < size; ++i ) {
            //add a new entity interface implementation to the return list
            entities.add( new EntityImplementation( _dataCenter,
                                                    entityIds.get( i ) ) );
        }

        //return the entity list
        return entities;
    }

    /**
     * Check if this processor is active.
     * @return true if active
     */
    public 
    boolean isActive() {
        //
        return _isActive;
    }

    /**
     * Activates this processor.
     */
    public 
    void activate() {
        //
        _isActive = true;
    }

    /**
     * Deactivates this processor.
     */
    public 
    void deactivate() {
        //
        _isActive = false;
    }

    /**
     * Toggles the activeness of this processor.
     */
    public 
    void toggelActiveness() {
        //
        _isActive = !_isActive;
    }

    /**
     * @return the list of interested data types
     */
    List<Class<? extends Data>> getInterestedDataList() {
        //
        return _interestedDataList;
    }

    /**
     * @return the processor specific id
     */
    Long getId() {
        //
        return _id;
    }
}
