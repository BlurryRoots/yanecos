/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blurryroots.yanecos.core;

import java.util.List;

//TODO: make serializeable
/**
 * Convenience implementation to use the interest system.
 * 
 * @author Sven Freiberg
 */
public 
abstract class InterestListDataProcessor extends DataProcessor {
    /**
     * Buffer for interested data, filled in constructor
     */
    private Class<? extends Data>[] _interestedData;
    
    /**
     * Constructs a InterestListDataProcessor
     * @param someInterestedData A set of data this processor is interested in processing.
     */
    public
    InterestListDataProcessor( Class<? extends Data>... someInterestedData ){
        //
        _interestedData = someInterestedData;
    }
    
    /**
     * Initialize this processor.
     */
    @Override
    protected
    void onInitialize() {
        //
        this.registerInterestIn( _interestedData );
    }

    /**
     * 
     */
    @Override
    protected 
    void onProcessing() {
        //
        this.onProcessingIntrestList( getInterestedEntities() );
        
    }
    
    /**
     * Process all entities holding data this processor is interested in.
     * @param someEntityList A list of entites
     */
    public
    abstract void onProcessingIntrestList( List<IEntity> someEntityList );    
}
