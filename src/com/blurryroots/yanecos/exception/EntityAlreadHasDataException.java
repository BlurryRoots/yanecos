/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blurryroots.yanecos.exception;

import com.blurryroots.yanecos.core.Data;


/**
 * ???
 * @author Sven Freiberg
 */
public class EntityAlreadHasDataException  extends DataException {
    /**
	 * ???
	 */
	private static final long serialVersionUID = 1L;

	/**
     * ???
     * @param someDataType
     * @param someEntityId
     */
    public EntityAlreadHasDataException( final Class<? extends Data> someDataType, 
    									 final long someEntityId ) {
        super( new StringBuilder()
        		   .append( "Cannot add data of type: " )
        		   .append( someDataType.toString() )
        		   .append( " to entity(" )
        		   .append( String.valueOf( someEntityId ) )
        		   .append( " ) it already has data of this type!"  )
        		   .toString(),
               someDataType,
               someEntityId );
    }
}