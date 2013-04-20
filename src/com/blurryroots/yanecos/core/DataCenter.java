/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blurryroots.yanecos.core;

import gnu.trove.list.TLongList;

import gnu.trove.list.array.TLongArrayList;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.procedure.TLongObjectProcedure;

import java.util.List;
import java.util.Map;

import com.blurryroots.yanecos.exception.NoSuchEntityException;

//TODO: make serializeable
/**
 *
 * Combines the data, managing of processors and entities and keep track of the
 * delta time, for accurate updating.
 *
 * @author Sven Freiberg
 */
public 
class DataCenter {
    /**
     * The database holding all entity ids, data, and processors
     */
    private DataCore _dataCore;
    /**
     * Holds a reference to the entity manager of this DataCenter.
     */
    private EntityManager _entityMgr;
    /**
     * Holds a reference to the processor manager of this DataCenter.
     */
    private DataProcessorManager _processorManager;
    /**
     * Time past since the last update
     */
    private double _deltaT;

    /**
     * Creates a new world.
     */
    public 
    DataCenter() {
        //
    }

    /**
     * Initializes the DataCenter and all its subsystems
     */
    public 
    void initialize() {
        //create a new datacore object
        _dataCore = new DataCore();
        //initialize the datacore
        _dataCore.initialize();
        
        //create a new enity manager object
        _entityMgr = new EntityManager( this );
        
        //intantiate the EntityController singleton
        EntityController.instantiate( this );
        
        //create a new processor manager
        _processorManager = new DataProcessorManager( this );
    }

    /**
     * Returns the entity manager attached to this world.
     *
     * @return the _entityMgr
     */
    EntityManager getEntityManager() {
        //
        return _entityMgr;
    }

    /**
     * @return the processor manager
     */
    DataProcessorManager getDataProcessorManager() {
        //
        return _processorManager;
    }

    /**
     * Updates the time difference of the last update
     */
    public 
    void updateDelta( final double someDelta ) {
        //
        _deltaT = someDelta;
    }

    /**
     * @return time difference
     */
    public 
    double getDeltaT() {
        //
        return _deltaT;
    }

    /**
     * @return the database
     */
    public 
    DataCore getDataCore() {
        //
        return _dataCore;
    }

    /**
     * @param someDataCore the databse to set
     */
    public 
    void setDataCore( final DataCore someDataCore ) {
        //
        _dataCore = someDataCore;
    }

    /*DataProcessorManager**/
    /**
     * Adds a new Processor to the database
     * @param someSystem
     */
    public 
    <T extends DataProcessor> void addProcessor( final T someSystem ) {
        //
        _processorManager.addProcessor( someSystem, someSystem.getClass() );
    }

    /**
     * Returns the processor of the given type
     * @param someProcessorClassType
     * @return a processor
     */
    public 
    <T extends DataProcessor> T getProcessor(
            final Class<T> someProcessorClassType ) {
        //
        return _processorManager.getProcessor( someProcessorClassType );
    }

    /**
     * Removes a processor from the database
     * @param someProcessorClassType 
     */
    public
    <T extends DataProcessor> void removeProcessor(
            final Class<T> someProcessorClassType ) {
        //
        _processorManager.removeProcessor( someProcessorClassType );
    }
    /*DataProcessorManager**/
    
    
    /*EntityManager**/
    /**
     * Creates a new entity stores it into the database and returns the id
     * @param someName
     * @return the id of the newly created entity
     */
    public 
    long createEntityId( final String someName ) {
        //
        return _entityMgr.createEntity( someName );
    }

    /**
     * Creates a new entity stores it into the database and returns it
     * @param someName
     * @return an entity
     */
    public 
    IEntity createEntity( final String someName ) {
        //create a new entity
    	final long entityId = _entityMgr.createEntity( someName );
    	//return the entity interface
        return new EntityImplementation( this, entityId );
    }

    /**
     * Returns a list of entities featuring a given set of data
     * @param someDataType
     * @param someFurtherDataTypes
     * @return a list of entity ids
     */
    public 
    TLongList getEntitiesWithData(
            final Class<? extends Data> someDataType,
            final Class<? extends Data>... someFurtherDataTypes ) {
        //
        return _entityMgr.getEntitiesWithData( someDataType, someFurtherDataTypes );
    }

    /**
     * Returns a list of entities featuring a given set of data
     * @param someInterestedDataList
     * @return list of entities
     */
    public 
    TLongList getEntitiesWithData(
            final List<Class<? extends Data>> someInterestedDataList ) {
        //
        return _entityMgr.getEntitiesWithData( someInterestedDataList );
    }

    /**
     * Returns an entity
     * @param someId
     * @return an entity
     */
    public 
    IEntity getEntity( final long someId ) 
    		throws NoSuchEntityException {
        //if there is no entity with the given id, throw an exception
        if ( ! _dataCore.getEntity_Data_Table().containsKey( someId ) ) {
            //
            throw new NoSuchEntityException( someId );
        }
        //
        return new EntityImplementation( this, someId );
    }

    /**
     * Returns an entity 
     * @param someName
     * @return an entity
     */
    public 
    IEntity getEntityByName( final String someName )
            throws NoSuchEntityException {
        //if there is no entity with given name, throw an expetion
        if ( ! _dataCore.getEntityNameTable().containsValue( someName ) ) {
            //
            throw new NoSuchEntityException( -1 );
        }
        //TODO: change this to getEntityByName
        return getEntity( _dataCore.getNameEntityTable().get( someName ) );
    }

    /**
     * Returns the id of an entity
     * @param someName
     * @return an entity id
     */
    public 
    long getEntityIdByName( final String someName ) {
        //TODO: throw exception!!
        return _entityMgr.getEntityByName( someName );
    }

    /**
     * Removes an entity from the database
     * @param someId
     * @throws NoSuchEntityException
     */
    public 
    void removeEntity( final long someId ) 
    		throws NoSuchEntityException {
        //
        _entityMgr.removeEntity( someId );
    }
    
    /**
     * Returns the current number of entities stored in the database 
     */
    public 
    long getEntityCount() {
        //
        return _entityMgr.getEntityCount();
    }
    /*EntityManager**/
    
    
    /**
     * ???
     * @param someProcessorId
     */
    void updateProcessorInterest( final long someProcessorId ) {
        //
        _processorManager.updateProcessorInterest( someProcessorId );
    }

    /**
     * Returns a list of entities the given processor is interested in
     * @param someProcessorId
     * @return a list of entity ids
     */
    TLongList getInterestedEntitiesFor( final long someProcessorId ) {
        //
        return _dataCore.getProcessId_interestedEntity_Table().get( someProcessorId );
    }

    /**
     * Invoked on entity creation
     */
    void onEntityCreated( final long someId ) {
        //nothing to do here...
    	//for now ;)
    }

    /**
     * Invoked on entity removal
     * @param someId
     */
    void onEntityRemoved( final long someId ) {
        //get all processors and the entities they're interested in
        final TLongObjectMap<TLongList> processorIntrestedEntityTable =
                _dataCore.getProcessId_interestedEntity_Table();
        //create an emtpy list, to fill with all processors having interest in the
        //entity to be removed
        final TLongList processorsContainigEntityList = new TLongArrayList();

        //iterate over all processors and determine if they are interested
        //in the entity to be removed
        processorIntrestedEntityTable.forEachEntry(
                new TLongObjectProcedure<TLongList>() {
                    //
                    @Override
                    public boolean execute( long processorId, TLongList interestedEntityList ) {
                        //iterate over all entities in the list
                        for ( int i = 0; i < interestedEntityList.size(); ++i ) {
                            //and find out if the given entity is in it                                
                            if ( interestedEntityList.get( i ) == someId ) {
                                //if so add the processor to the list
                                processorsContainigEntityList.add( processorId );
                            }
                        }
                        return true;
                    }
                } );

        //iterate over the list of processors happen to be interested in
        //the entity which is to be removed
        for ( int i = 0; i < processorsContainigEntityList.size(); ++i ) {
            //and remove the entity from their interest list
            processorIntrestedEntityTable.get( processorsContainigEntityList.get( i ) ).remove( someId );
        }
    }

    /**
     * Invoked on entity changed
     * @param someEntityId
     */
    void onEntityChanged( final long someEntityId ) {
        //setup local variables
        List<Class<? extends Data>> processorInterstedDataList;
        long processorIdBuffer;
        boolean isInterested;
        int interestedProcessorCount;
        
        //retrieve all processors and a list with interested entities
        final TLongObjectMap<TLongList> processorIntrestedEntityTable =
                _dataCore.getProcessId_interestedEntity_Table();
        //retrieve all types of data the given entity holds
        final List<Class<? extends Data>> entityDataList =
                _entityMgr.getAllDataTypesFromEntity( someEntityId );

        
        //iterate over all lists of data types and the processors interested in them 
        for ( Map.Entry<List<Class<? extends Data>>, TLongList> entry :
              _dataCore.getData_InterestedProcessor_Table().entrySet() ) {
        	
            //a data type set
            processorInterstedDataList = entry.getKey();
            
            //number of processors interested in ^^
            interestedProcessorCount = entry.getValue().size();            
            //number of data types
            final int interestedDataCount = processorInterstedDataList.size();

            //iterate over all processors interested in the current data type set
            for ( int i = 0; i < interestedProcessorCount; ++i ) {
                //
                processorIdBuffer = entry.getValue().get( i );

                //determine if the processor is interested in the given entity
                isInterested = true;
                for ( int j = 0; j < interestedDataCount; ++j ) {
                    //if the entity is holding ALL data in which the processor is interested in
                	//isInterested is going to be true
                    isInterested &= entityDataList.contains( processorInterstedDataList.get( j ) );
                }

                //if the processor is interested in this entity
                if ( isInterested ) {
                    //add the id of the entity to the list of interested entites of the processor
                    processorIntrestedEntityTable.get( processorIdBuffer ).add( someEntityId );
                }
                //if the processor is not interested, but was befor
                else if ( processorIntrestedEntityTable.get( processorIdBuffer ).contains( someEntityId ) ) {
                    //remove the entity from the interest list
                    processorIntrestedEntityTable.get( processorIdBuffer ).remove( someEntityId );
                }
            }
        }
    }
}
