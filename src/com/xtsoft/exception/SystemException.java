package com.xtsoft.exception;

/**
 * 
 * @author x61
 * 
 */
public class SystemException extends Exception {
	public SystemException() {
		super();
	}

	public SystemException(String msg) {
		super(msg);
	}

	public SystemException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public SystemException(Throwable cause) {
		super(cause);
	}
}
