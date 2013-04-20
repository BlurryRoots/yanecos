/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blurryroots.yanecos.core;

//import java.util.*;
//import gnu.trove.map.hash.
import gnu.trove.list.TLongList;
import gnu.trove.list.array.TLongArrayList;
import gnu.trove.map.hash.THashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.blurryroots.yanecos.exception.EntityAlreadHasDataException;
import com.blurryroots.yanecos.exception.NoSuchDataException;
import com.blurryroots.yanecos.exception.NoSuchDataOnEntityException;
import com.blurryroots.yanecos.exception.NoSuchEntityException;

//TODO: implement convenient use of the active/inactive state of data
/**
 * Manages all tasks w concerning entities.
 *
 * @author Sven Freiberg
 * @version 0.3
 */
class EntityManager {
    /**
     * Reference to the DataCenter, this EntityManager is working in.
     */
    private DataCenter _dataCenter;

    /**
     * Constructs a new EntityManager object.
     *
     * @param someDataCenter The world for the EntityManager to live in.
     */
    EntityManager( DataCenter someDataCenter ) {
        //
        _dataCenter = someDataCenter;
    }

    /**
     * Frees up the give id, in order to reuse it.
     * @param someId
     */
    private
    void freeId( Long someId ) {
        //set the id flag to not taken
        _dataCenter.getDataCore()
                   .getEntityId_setStatus_Table()
                   .put( someId, Boolean.FALSE );
        //add the id to the group of non taken ids
        _dataCenter.getDataCore()
        	       .getSetStatus_entityId_Table()
        	       .get( Boolean.FALSE )
        	       .add( someId );
    }

    /**
     * Check if there are free ids to use.
     * @return true if there is at least on free id.
     */
    public boolean hasFreeIds(){
        //checks whether there are free ids available
        return ! _dataCenter.getDataCore()
        		            .getSetStatus_entityId_Table()
        		            .get( Boolean.FALSE )
        		            .isEmpty();
    }
    
    /**
     * Creates a new id.
     */
    private
    void createNewId() {
        //
        DataCore db;
        long currentId;
        long newId;

        //
        db = _dataCenter.getDataCore();
        
        //get the highest used id
        currentId = db.getEntityIdCounter();
        //check possibility of overflowing
        if( currentId == Long.MAX_VALUE ) {
            //
            throw new ArithmeticException( "Long overflow! Out of ids!" );
        }

        //increment id counter
        db.setEntityIdCounter( currentId + 1 );
        //get the next id
        newId = db.getEntityIdCounter();
        
        //set its status to not taken
        freeId( newId );
    }

    /**
     * Returns the next free id to use.
     * @return a free id.
     */
    private 
    long getNextFreeId() {
        //
        DataCore db;
        long freeId;

        //
        db = _dataCenter.getDataCore();
        
        //create a new id if there are no free ones available
        if( !hasFreeIds() ) {
            //
            createNewId();
        }

        //'pop' the next free id from the list        
        freeId = db.getSetStatus_entityId_Table()
        		   .get( Boolean.FALSE )
        		   .removeAt( 0 );
        //and add it to the used id list
        db.getEntityId_setStatus_Table()
          .put( freeId, Boolean.TRUE );
        
        //
        return freeId;
    }

    /**
     * Creates a new entity id and returns it.
     *
     * @param someName The name of the new entity.
     * @return entity id
     */
    public 
    long createEntity( String someName ) {
        //        
        DataCore db;
        Long eId;

        //
        db = _dataCenter.getDataCore();
        
        //grab the next free id
        eId = getNextFreeId();

        //register entity name
        db.getEntityNameTable().put( eId, someName );
        db.getNameEntityTable().put( someName, eId );

        //prepare data list
        db.getEntity_Data_Table().put(
                eId,
                new THashMap<Class<? extends Data>, Data>() );        

        //increment the number of currently registered entites
        db.setEntityCounter( db.getEntityCounter() + 1 );        

        //invoke on created event
        _dataCenter.onEntityCreated( eId );        

        //
        return eId;
    }

    /**
     * Returns the entity assign to the given name.
     *
     * @param someName The name of the entity.
     * @return EntityId
     */
    public 
    long getEntityByName( String someName ) {
        //
        return _dataCenter.getDataCore()
        			      .getNameEntityTable()
        			      .get( someName );
    }

    /**
     * Removes an entity from the database and recycles its id.
     *
     * @param someId The id of the entity to be removed.
     */
    public 
    void removeEntity( final long someId ) 
    		throws NoSuchEntityException {
        //
        DataCore db = _dataCenter.getDataCore();

        //if there is no entity with the given id
        if( ! db.getEntity_Data_Table().containsKey( someId ) ) {
            //throw an exception
            throw new NoSuchEntityException( someId );
        }

        //iterate over the list of data types registered for this entity 
        for( Map.Entry<Class<? extends Data>, Data> entry :
                db.getEntity_Data_Table().get( someId ).entrySet() ) {
            //remove all the the entity in all data type lists
            db.getData_Entity_Table()
              .get( entry.getKey() )
              .remove( someId );
        }

        //remove all registered data of the entity
        db.getEntity_Data_Table().remove( someId );

        //remove the name of the entity
        db.getNameEntityTable().remove( db.getEntityNameTable().get( someId ) );
        db.getEntityNameTable().remove( someId );

        //free the id used bys the entity
        freeId( someId );        
        
         //decrement the number of entities registered
        db.setEntityCounter( db.getEntityCounter() - 1 );

        //invoke on removed event
        _dataCenter.onEntityRemoved( someId );
    }

    /**
     * Get the current number of entities populating the database.
     *
     * @return The current number of entities.
     */
    public 
    long getEntityCount() {
        //
        return _dataCenter.getDataCore().getEntityCounter();
    }

    /**
     * Adds data to a specific entity.
     *
     * @param <T> T extends Data.
     * @param someEntityId The id of the entity.
     * @param someData Component to be added.
     */
    public
    <T extends Data> void addDataToEntity( long someEntityId, T someData )
            throws EntityAlreadHasDataException, NoSuchEntityException {
        //local vars
        DataCore db = _dataCenter.getDataCore();
        Map<Class<? extends Data>, Data> entityData = db.getEntity_Data_Table().get( someEntityId );

        //check whether there is no entry, there is no entity with the given id
        if( entityData == null ) {
            //if so throw an exception
            throw new NoSuchEntityException( someEntityId );
        }

        //check whether this entity already holds data of this type
        if( entityData.containsKey( someData.getClass() ) ) {
            //if so throw an exception
            throw new EntityAlreadHasDataException( someData.getClass(), someEntityId );
        }

        //store the data in the storage for this entity
        entityData.put( someData.getClass(), someData );
        
        //if this type of data hasnt been used before
        if( db.getData_Entity_Table().get( someData.getClass() ) == null ) {
            //create a new entry for it
            db.getData_Entity_Table().put( someData.getClass(), new TLongArrayList() );
        }
        
        //add this entity to the list of entities that use this type of data
        db.getData_Entity_Table().get( someData.getClass() ).add( someEntityId );
    }

    /**
     * Returns a specific data of a given entity.
     *
     * @param <T> T extends Data.
     * @param someDataType Class-Type of Data
     * @param someEntityId The id of an entity.
     * @return Data of given type.
     * @throws Exception.
     */
    public
    <T extends Data> T getDataFromEntity(
            final Class<T> someDataType, final long someEntityId )
            throws NoSuchEntityException,
                   NoSuchDataOnEntityException {
        //local vars
        T data;
        THashMap<Class<? extends Data>, Data> entiyDataMap;
        //
        DataCore db = _dataCenter.getDataCore();

        //check if there is an entity registered under the given id
        if( ! db.getEntity_Data_Table().containsKey( someEntityId ) ) {
            //if not throw an exception
            throw new NoSuchEntityException( someEntityId );
        }

        //retrieve the data for this entity
        entiyDataMap = db.getEntity_Data_Table().get( someEntityId );
        //check whether this entity holds the given type of data 
        if( ! entiyDataMap.containsKey( someDataType ) ) {
            //if not throw an exception
            throw new NoSuchDataOnEntityException( someDataType, someEntityId );
        }

        //retrieve the data
        data = (T)entiyDataMap.get( someDataType );

        //return it
        return data;
    }

    /**
     * Removes data from an Entity.
     *
     * @param someEntityId Id of an entity.
     * @param someDataType Type of data to be removed.
     */
    public 
    void removeDataFromEntity(
            final long someEntityId, Class<? extends Data> someDataType )
            throws NoSuchEntityException,
                   NoSuchDataException,
                   NoSuchDataOnEntityException {
        //
        DataCore db = _dataCenter.getDataCore();

        //check whether there is an entity registered with the given id
        if( ! db.getEntity_Data_Table().containsKey( someEntityId ) ) {
            //if not throw an exception
            throw new NoSuchEntityException( someEntityId );
        }

        //check whether this entity holds the given type of data
        if( ! db.getData_Entity_Table().containsKey( someDataType ) ) {
            //if not throw an exception
            throw new NoSuchDataException( someDataType, someEntityId );
        }

        //checks whether the given type of data is properly wired with the
        //given entity
        if( ! db.getData_Entity_Table()
        		.get( someDataType )
        		.contains( someEntityId ) ) {
            //if not, throw an exception
            throw new NoSuchDataOnEntityException( someDataType, someEntityId );
        }

        //remove the entity from the data table
        db.getData_Entity_Table().get( someDataType ).remove( someEntityId );
        //remove the data from the entity table
        db.getEntity_Data_Table().get( someEntityId ).remove( someDataType );

    }

    //
    /**
     * Returns a list of entites holding a set of data.
     *
     * @param someDataType Class-Type of data (? extends data).
     * @return List of entity ids holding the given type of data.
     */
    public
    TLongList getEntitiesWithData(
            Class<? extends Data> someDataType,
            Class<? extends Data>... someFurtherDataTypes ) {
        //local vars
        List<Class<? extends Data>> dataTypeList;
        
        //condense the parameter to a list
        dataTypeList = new ArrayList<Class<? extends Data>>();
        dataTypeList.add( someDataType );
        for( int i = 0; i < someFurtherDataTypes.length; ++i ) {
            //
            dataTypeList.add( someFurtherDataTypes[i] );
        }

        //call the function to retrieve entities with a list of data
        return getEntitiesWithData( dataTypeList );
    }

    /**
     * Returns a list of entities holding a given set of data.
     * @param someDataTypeList A list of data.
     * @return A list of entity ids.
     */
    public 
    TLongList getEntitiesWithData( 
    		List<Class<? extends Data>> someDataTypeList ) {
        //local vars
        DataCore db;
        List<TLongList> possibleResultEntityLists;
        TLongList comparerList;
        long currentId;
        TLongList resultEntityList;

        //get current datacore
        db = _dataCenter.getDataCore();

        //if the given list of data is empty, return null
        if( someDataTypeList.isEmpty() ) {
            //
            return null;
        }

        //check whether the list holds exactly one item
        if( someDataTypeList.size() == 1 ) {
            //if there is already a set of interesting entities
            //read them from the table and save them in result
            //otherwise create an empty list
            resultEntityList = 
            		( db.getData_Entity_Table()
            		    .containsKey( someDataTypeList.get( 0 ) ) )
                    ? db.getData_Entity_Table()
                        .get( someDataTypeList.get( 0 ) )
                    : new TLongArrayList();

        }
        else {
            //create a new list for possbile results
            possibleResultEntityLists = new ArrayList<TLongList>();
            //create a new list for actual results
            resultEntityList = new TLongArrayList();

            //iterater over the given list of data types
            for( Class<? extends Data> dataType : someDataTypeList ) {
                //add all entities holding the current data
            	//in form of a list
                possibleResultEntityLists.add( 
                		db.getData_Entity_Table().get( dataType ) );
            }

            //pop the first of the lists
            comparerList = possibleResultEntityLists.remove( 0 );
            //check whether there is a list to compare
            if( comparerList != null ) {
            	
                //if so iterate over the list
                for( int i = 0; i < comparerList.size(); ++i ) {
                	
                	//store the current id
                    currentId = comparerList.get( i );
                    
                    //iterate over the residual entity lists
                    for( int j = 0; j < possibleResultEntityLists.size(); ++j ) {
                    	
                        //retrieve a list
                        TLongList entries = possibleResultEntityLists.get( j );
                        //check whether there is a list
                        if( entries != null ) {
                            //if so iterate over the entities in this list
                            for( int k = 0; k < entries.size(); ++k ) {
                                //if the current id is also in this list
                                if( currentId == entries.get( k ) ) {
                                    //add it to the result list
                                    resultEntityList.add( currentId );
                                	//TODO: is there a faster algorithm?
                                }
                            }
                        }
                        
                    }
                    
                }
                
            }
        }

        return resultEntityList;
    }

    //
    /**
     * Returns the name of an entity.
     *
     * @param someId The id of an entity.
     * @return The entity name.
     */
    public
    String getEntityName( long someId ) {
        //
        return ( _dataCenter.getDataCore().getEntityNameTable().get( someId ) );
    }

    //
    public
    List<Class<? extends Data>> getAllDataTypesFromEntity( long someId ) {
        //
        return new ArrayList<Class<? extends Data>>(
                _dataCenter.getDataCore().getEntity_Data_Table().get( someId ).keySet() );
    }
}
