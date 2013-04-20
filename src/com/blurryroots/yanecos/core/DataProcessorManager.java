/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blurryroots.yanecos.core;

import gnu.trove.list.TLongList;
import gnu.trove.list.array.TLongArrayList;

import java.util.List;

//TODO: make serializeable
/**
 *
 * Manages all important functionallity concerning data processors, like adding
 * and removing to/from the data core.
 *
 * @author Sven Freiberg
 */
class DataProcessorManager {
    /**
     * 
     */
    DataCenter _dataCenter;

    //
    DataProcessorManager( DataCenter someDataCenter ) {
        //
        _dataCenter = someDataCenter;
    }

    //
    /**
     * Attaches a new processor to the data center.
     *
     * @param someName Name of the processor.
     * @param someProcessor The processor ought to be attached.
     * @param someProcessorClassType Class-type of the processor.
     */
    public 
    void addProcessor(
            DataProcessor someProcessor,
            Class<? extends DataProcessor> someProcessorClassType ) {
        //local vars
        DataCore db;
        long id;


        db = _dataCenter.getDataCore();        	//get the current DataCore
        id = db.getDataProcessorIdCounter();   	//get the current processoridcounter

        //assign the current id to the new processor
        db.getId_Processor_Table().put( id, someProcessor );
        //assign the current id to the classtype of the new processor
        db.getId_ClassType_Table().put( id, someProcessorClassType );
        //assign the new processor to his id
        db.getClassType_Id_Table().put( someProcessorClassType, id );

        //invoke onAttach event
        someProcessor.onAttach( id, _dataCenter );

        //increment the processoridcounter
        db.setDataProcessorIdCounter( db.getDataProcessorIdCounter() + 1 );
    }

    /**
     * Assembles interested entities for a given processor
     * @param someProcessorId
     */
    public 
    void updateProcessorInterest( long someProcessorId ) {
        //local vars
        DataCore db;
        List<Class<? extends Data>> interestList;

        //get the current DataCore
        db = _dataCenter.getDataCore();
        //get the list of data this processor is interested in
        interestList = db.getId_Processor_Table()
        				 .get( someProcessorId )
        				 .getInterestedDataList();

        //check if there is an entry for this interest combination
        //and if not
        if( ! db.getData_InterestedProcessor_Table()
        	    .containsKey( interestList ) ) {
            //create one
            db.getData_InterestedProcessor_Table().put(
                    interestList,
                    new TLongArrayList() );
        }
        
        //check if this interest combination already contains 
        //the given processor, and if not
        if( ! db.getData_InterestedProcessor_Table()
        		.get( interestList )
        		.contains( someProcessorId ) ) {
            //add its id
            db.getData_InterestedProcessor_Table()
              .get( interestList )
              .add( someProcessorId );
        }

        //assemble a list of entities holding the interested data
        final TLongList interestedEntities = 
        		_dataCenter.getEntitiesWithData( interestList );
        //assign the processor the list of entities its interested in
        db.getProcessId_interestedEntity_Table().put(
                someProcessorId, interestedEntities );
    }

    //
    /**
     * Returns the attached system of given type.
     *
     * @param <T> T extends System.
     * @param someSystemClassType Class-Type of the system to be returned.
     * @return T
     */
    public 
    <T extends DataProcessor> T getProcessor( Class<T> someSystemClassType ) {
        //
        DataCore db = _dataCenter.getDataCore();
        //
        return (T)db.getId_Processor_Table()
        		    .get( db.getClassType_Id_Table().get( someSystemClassType ) );
    }

    //
    /**
     * Removes a DataProcessor from the database. 
     *
     * @param <T> Template (T extends DataProcessor).
     * @param someSystemClassType The ClassType of the DataProcessor to be
     * removed.
     */
    public 
    <T extends DataProcessor> void removeProcessor( Class<T> someSystemClassType ) {
        //
        DataCore db;
        long id;

        //
        db = _dataCenter.getDataCore();
        id = db.getClassType_Id_Table().remove( someSystemClassType );

        //
        db.getId_ClassType_Table().remove( id );
        db.getId_Processor_Table().remove( id );
    }

}
