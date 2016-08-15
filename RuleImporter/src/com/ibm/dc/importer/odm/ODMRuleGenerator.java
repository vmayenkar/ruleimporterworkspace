/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.dc.importer.odm;

import ilog.rules.teamserver.brm.IlrBaseline;
import ilog.rules.teamserver.brm.IlrBrmPackage;
import ilog.rules.teamserver.brm.IlrRulePackage;
import ilog.rules.teamserver.brm.IlrRuleProject;
import ilog.rules.teamserver.model.IlrApplicationException;
import ilog.rules.teamserver.model.IlrConnectException;
import ilog.rules.teamserver.model.IlrDefaultSearchCriteria;
import ilog.rules.teamserver.model.IlrModelConstants;
import ilog.rules.teamserver.model.IlrObjectNotFoundException;
import ilog.rules.teamserver.model.IlrSession;
import ilog.rules.teamserver.model.IlrSessionHelper;
import ilog.rules.teamserver.model.permissions.IlrRoleRestrictedPermissionException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;

import com.ibm.rmt.importer.enums.RuleType;
import com.ibm.rmt.importer.exception.RuleImporterException;

/**
 * An abstract class that all specific types of rule generators
 * inherit.
 */
public abstract class ODMRuleGenerator {
	
	private static Logger logger = Logger.getLogger(ODMRuleGenerator.class
			.getSimpleName());
	
	protected Connection conn=null;
	
	public boolean createRule(ConnectionParams connParams,
			String projectName, String packageName, String ruleName,
			RuleType ruleType) {
			
		try {
					
			//Connect to Decision Center
			conn = new Connection(connParams);
			conn.connect();
			IlrSession session=conn.getSession();
			
			//Load Project
			IlrRuleProject ruleProject = loadProject(session,projectName);
			
			//Get handle to rulepackage
			IlrRulePackage rulePackage=null;
			if (packageName.contains(".")) {
				rulePackage = getPackageForPath(session, packageName.split("\\."));
			} else {
				rulePackage = getRulePackage(session,packageName);
			}
			 	
			//Create rule
			this.createRule(session,  ruleProject, rulePackage, ruleName);
			
			return true;
			
		} catch (IlrConnectException | IlrApplicationException | RuleImporterException e) {
			logger.log(Level.SEVERE, "Error occured during creation of action rule " + ruleName , e);
			return false;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error occured during creation of action rule " + ruleName , e);
			return false;
		} finally {
			if (null!=conn){
				try {
					conn.disconnect();
				} catch (IlrConnectException e) {
					//ignore
				}
			}
		}
		
	}
	
	/**
	 * Retrieves the project from Decision Center. 
	 * 
	 * @return the existing project
	 */
	private IlrRuleProject loadProject(IlrSession session,String projectName) throws IlrApplicationException{
		
		IlrRuleProject project = IlrSessionHelper.getProjectNamed(session,
				projectName);
		
		List<IlrBaseline> baselines = IlrSessionHelper.getBaselines(
				session, project);
		
		session.setWorkingBaseline(baselines.get(0));
		
		return project;
	}
	
	/**
	 * Returns handle to the rule package from . 
	 * 
	 * 
	 * @return the rule package
	 */
	@SuppressWarnings("unchecked")
	private IlrRulePackage getRulePackage(IlrSession session,String packageName) throws IlrApplicationException{
		
		IlrBrmPackage brm = session.getBrmPackage();
		
		IlrRulePackage rulePackage = null;
		
		List<EAttribute> features = new ArrayList<EAttribute>();
		features.add(brm.getModelElement_Name());
		
		List<String> values = new ArrayList<String>();
		values.add(packageName);
		
		IlrDefaultSearchCriteria searchCriteria = new IlrDefaultSearchCriteria(
		brm.getRulePackage(), features, values);
		
		// Execute the query
		List<IlrRulePackage> list = (List<IlrRulePackage>) session.findElements(searchCriteria);
		// Retrieve the first element. Should have only one element.
		if (list.size() == 1) {
			rulePackage = list.get(0);
		}
		
		return rulePackage;
	}
	
	/**
	 * Returns a package identified by a path.
	 *
	 * @param session The Rule Team Server session.
	 * @param paths The path.
	 * @return The package.
	 * @throws IlrRoleRestrictedPermissionException
	 * @throws IlrObjectNotFoundException
	 */
	public static IlrRulePackage getPackageForPath (IlrSession session, String [] paths) throws IlrRoleRestrictedPermissionException, IlrObjectNotFoundException {
		session.beginUsage();
		IlrBrmPackage brm = session.getBrmPackage();
		// Check the path
		IlrRulePackage current = null;
		if ( (paths==null)  ||
			 (paths.length==0) ||
			 ((paths.length==1)&& (paths[0]==null)) ||
			 ((paths.length==1)&& (paths[0].trim().length()==0)) ) {
			// Return the root package
			return null;
		}
		StringBuffer sb = new StringBuffer ();
		for (int i = 0; i < paths.length; i++) {
			String path = paths[i];
			sb.append(path + "/");
			// Get the sub-packages within the current package
			List<IlrRulePackage> folders = getFoldersInFolder (session, current);
			boolean found = false;
			for (int j = 0; j < folders.size(); j++) {
				IlrRulePackage packag = folders.get(j);
				// If a package with the right name is found, it becomes the current package
				String packname = ((String) packag.getRawValue(brm.getModelElement_Name()));
				if ((packname!=null) && (packname.equalsIgnoreCase(path))) {
					current = packag;
					found = true;
					break;
				}
			}
			// An element in the path is unknown
			if (!found) throw new RuntimeException (sb.toString() + " is an unknow package in the project " + session.getWorkingBaseline().getName());
		}
		// Found it
		return current;
	}

	/**
	 * Returns the subpackages (1st level) of the package.
	 *
	 * @param session The Rule Team Server session.
	 * @param folder The path.
	 * @return The list of subpackages.
	 * @throws IlrRoleRestrictedPermissionException
	 * @throws IlrObjectNotFoundException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<IlrRulePackage> getFoldersInFolder  (IlrSession session, IlrRulePackage folder) throws IlrRoleRestrictedPermissionException,IlrObjectNotFoundException {
		// Get the package for the model. It contains accessors for the meta objects to represent
		// each class, feature of each class, and so on.
		IlrBrmPackage brm = session.getBrmPackage();
		// Create the filters criteria
		List features = new ArrayList ();
		// The criteria is the parent package
		EReference name = brm.getRulePackage_Parent();
		features.add(name);
		//	The criteria is the package
		List values = new ArrayList ();
		values.add(folder);
		// Look for a package having the parent package equal to the passed package
		IlrDefaultSearchCriteria searchCriteria = new IlrDefaultSearchCriteria(brm.getRulePackage(),features,values, null, IlrModelConstants.SCOPE_PROJECT, null, true);
		// Execute the query
		java.util.List list = session.findElements(searchCriteria);
		// Retrieve the result
		java.util.List<IlrRulePackage> ret = new ArrayList<IlrRulePackage> ();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			IlrRulePackage subfolder = (IlrRulePackage) iterator.next();
			// Root package requested?
			if ((subfolder.getParent()==null) && (folder==null)) {
					ret.add(subfolder);
			} else {
				// Retrieve only the first child level
				if ((subfolder.getParent()!=null) && (folder!=null)) {
					if (subfolder.getParent().equals(folder, true))  {
							ret.add(subfolder);
					}
				}
			}
		}
		return ret;
	}

	
	
	
	public abstract boolean createRule(IlrSession session, IlrRuleProject ruleProject,
			IlrRulePackage rulePackage, String ruleName) throws RuleImporterException;

}
