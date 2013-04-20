package com.blurryroots.yanecos.exception;

/**
 * ???
 * @author Sven Freiberg
 *
 */
public class YanecosException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ???
	 * @param someMsg
	 */
	public YanecosException( final String someMsg ) {
		super( someMsg );
		//
	}
	
	/**
	 * ???
	 * @param someThrowable
	 */
	public YanecosException( final Throwable someThrowable ) {
		super( someThrowable );
		//
	}
	
	/**
	 * ???
	 * @param someMsg
	 * @param someThrowable
	 */
	public YanecosException( final String someMsg, final Throwable someThrowable  ) {
		super( someMsg, someThrowable );
		//
	}
}
