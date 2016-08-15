/* 
 * Copyright (C) 2016 IBM Corporation
 */

package com.ibm.dc.importer.odm;

import ilog.rules.teamserver.client.IlrRemoteSessionFactory;
import ilog.rules.teamserver.model.IlrConnectException;
import ilog.rules.teamserver.model.IlrObjectNotFoundException;
import ilog.rules.teamserver.model.IlrSession;
import ilog.rules.teamserver.model.permissions.IlrPermissionException;

/**
 * Represents a connection to Decision Center on an application server.
 *
 */
public class Connection   {
	/**
	 * The factory that gets the session.
	 */
	IlrRemoteSessionFactory factory ;
	/**
	 * The main entry point to Decision Center.
	 */
	IlrSession session = null;
	
	ConnectionParams connParams = null;

	/**
	 * Whether a user is connected or not.
	 */
	boolean connected = false;

	/**
	 * 
	 * @param connParams
	 */
	public Connection(ConnectionParams connParams) {
		super();
		this.connParams = connParams;
		this.factory = new IlrRemoteSessionFactory();
	}

    public void finalize() {
	if (connected)
	    session.endUsage();
    }

	/**
	 * Connects to the Decision Center project.
	 *
	 * @param name The name of the project.
	 * @throws IlrConnectException
	 * @throws IlrObjectNotFoundException
	 * @throws IlrPermissionException
	 */
	protected void connect ()  {
		try {
			factory.connect(connParams.user, connParams.password,connParams.url, connParams.datasource) ;
			session = factory.getSession();
			session.beginUsage();
			connected = true;
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Disconnects from Decision Center.
	 *
	 * @throws IlrConnectException
	 */
	protected void disconnect () throws IlrConnectException {
		if (null != session){
			session.endUsage();
			session.close();
		}  
	}

	/**
	 * @return The current session.
	 * @throws IlrConnectException
	 * @throws IlrPermissionException
	 * @throws IlrObjectNotFoundException
	 */
	protected IlrSession getSession() throws IlrObjectNotFoundException, IlrPermissionException, IlrConnectException {
		if (!connected) connect ();
		return session;
	}


	/**
	 * @return The factory used to connect to Decision Center.
	 */
	protected IlrRemoteSessionFactory getFactory() {
		return factory;
	}

	/**
	 * Sets the factory to connect to Decision Center.
	 * @param factory The factory.
	 */
	protected void setFactory(IlrRemoteSessionFactory factory) {
		this.factory = factory;
	}

}
