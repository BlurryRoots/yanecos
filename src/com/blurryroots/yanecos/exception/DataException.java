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
public class DataException extends YanecosException {
    /**
	 * ???
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ???
	 */
    private Class<? extends Data> _dataType;
    /**
     * ???
     */
    private long _entityId;

    /**
     * ???
     * @param someMsg
     * @param someDataType
     * @param someEntityId
     */
    public DataException(
            String someMsg,
            Class<? extends Data> someDataType,
            long someEntityId ) {
        //
        super( someMsg );

        //
        _dataType = someDataType;
        _entityId = someEntityId;
    }

    /**
     * @return classtype of data
     */
    public Class<? extends Data> getDataType() {
        //
        return _dataType;
    }

    /**
     * @return entity id
     */
    public long getEntityId() {
        //
        return _entityId;
    }

}
