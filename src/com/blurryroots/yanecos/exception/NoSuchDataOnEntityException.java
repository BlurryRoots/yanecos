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
public class NoSuchDataOnEntityException extends DataException {
    /**
	 * ???
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ???
	 * @param someDataType
	 * @param someEntityId
	 */
    public NoSuchDataOnEntityException( final Class<? extends Data> someDataType, 
    									final long someEntityId ) {
        super(
                "Cannot get data of type: "
                + someDataType.toString()
                + " entity(" + String.valueOf( someEntityId ) + " )"
                + " does not hold data of this type!",
                someDataType,
                someEntityId );
    }
}
