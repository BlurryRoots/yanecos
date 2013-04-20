/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blurryroots.yanecos.exception;

/**
 * ???
 * @author Sven Freiberg
 */
public class EntityException extends YanecosException{
	/**
	 * ???
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ???
	 */
    private long _entityId;
    
    /**
     * ???
     * @param someMessage
     * @param someEntityId
     */
    public EntityException( final String someMessage, 
    						final long someEntityId ) {
        super( someMessage );        
        //
        _entityId = someEntityId;
    }
    
    /**
     * ???
     * @param someThrowable
     * @param someEntityId
     */
    public EntityException( final Throwable someThrowable, 
    						long someEntityId ) {
        super( someThrowable );        
        //
        _entityId = someEntityId;
    }
    
    /**
     * ???
     * @param someMessage
     * @param someThrowable
     * @param someEntityId
     */
    public EntityException( final String someMessage, 
    						final Throwable someThrowable, 
    						final long someEntityId ) {
        super( someMessage, someThrowable );        
        //
        _entityId = someEntityId;
    }
    

    /**
     * @return entity id
     */
    public long getEntityId() {
        //
        return _entityId;
    }
}
