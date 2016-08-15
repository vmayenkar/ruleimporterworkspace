/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.rmt.importer.exception;

public class RuleImporterException extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Throwable originalException;
	
	public RuleImporterException (Throwable e) {
		super(e);
		this.originalException=e;
	}

	public Throwable getOriginalException() {
		return originalException;
	}
	
}
