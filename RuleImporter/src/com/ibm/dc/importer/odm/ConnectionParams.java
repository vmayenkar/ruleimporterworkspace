/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.dc.importer.odm;

/**
 * The parameters required to connect to the Decision Center
 */
public class ConnectionParams {


	/**
	 * The user used to connect to Decision Center.
	 */
	String user;
	/**
	 * The password used to connect to Decision Center.
	 */
	String password;
	/**
	 * The URL used to connect to Decision Center.
	 */
	String url;
	/**
	 * The Decision Center datasource.
	 */
	String datasource;
	
	public ConnectionParams(String url, String datasource, String user,
			String password) {
		this.user=user;
		this.password=password;
		this.url=url;
		this.datasource=datasource;
	}
	
}
