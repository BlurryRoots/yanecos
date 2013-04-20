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
public class NoSuchDataException extends DataException {
    /**
	 * ???
	 */
	private static final long serialVersionUID = 1L;

	/**
     * ???
     * @param someDataType
     * @param someEntityId
     */
    public NoSuchDataException( final Class<? extends Data> someDataType, 
    							final long someEntityId ) {
        super( new StringBuilder()
                   .append( "Data of type " )
                   .append( someDataType.toString() )
                   .append( " is not registered!" )
                   .toString(),
               someDataType, 
        	   someEntityId );
    }
}
