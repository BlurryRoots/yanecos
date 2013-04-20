/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blurryroots.yanecos.exception;

/**
 * ???
 * @author Sven Freiberg
 */
public class NoSuchEntityException extends EntityException {
    /**
	 * ???
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ???
	 * @param someEntityId
	 */
    public NoSuchEntityException( long someEntityId ) {
        //
        super( "No entity with id:"
               + String.valueOf( someEntityId )
               + " registerd!",
               someEntityId );
    }
}
