/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.dc.importer.utils;

import ilog.rules.teamserver.brm.IlrBaseline;
import ilog.rules.teamserver.brm.IlrRuleProject;
import ilog.rules.teamserver.model.IlrApplicationException;
import ilog.rules.teamserver.model.IlrSession;
import ilog.rules.teamserver.model.IlrSessionHelper;

import java.util.List;

/**
 * A Collection of utility methods 
 */
public class DecisionCenterUtils {

	
	/**
	 * Retrieves project from the Decision Center
	 * 
	 * @param session
	 * @param projectName
	 * @return
	 * @throws IlrApplicationException
	 */
	public static IlrRuleProject loadProject(IlrSession session, String projectName) throws IlrApplicationException{
		
		IlrRuleProject project = IlrSessionHelper.getProjectNamed(session, projectName);
		
		List<IlrBaseline> baselines = IlrSessionHelper.getBaselines(session, project);
		session.setWorkingBaseline(baselines.get(0));
	
		return project;
	}
}
