/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.rmt.importer.odm;

import ilog.rules.studio.model.IlrStudioModelPlugin;
import ilog.rules.studio.model.base.IlrRulePackage;
import ilog.rules.studio.model.base.IlrRuleProject;

import java.util.logging.Logger;

import com.ibm.rmt.importer.enums.RuleType;

/**
 * An abstract class that all specific types of rule generators
 * inherit.
 */
public abstract class ODMRulesGenerator {
	
	private static Logger logger = Logger.getLogger(ODMRulesGenerator.class.getName());
	
	public boolean createRule(String projectName,
			String packageName, String ruleName, RuleType ruleType) {
				
		// Retrieve the target rule project where the rules will be created
		IlrRuleProject ruleProject = IlrStudioModelPlugin.getRuleModel()
				.getRuleProject(projectName);
		if (ruleProject == null) {
			logger.severe("Cannot find rule project in workspace: "
					+ projectName);
			return false;
		}
		
		// Retrieve the target rule package where the rules will be created
		IlrRulePackage rulePackage = ruleProject.getRulePackage(packageName);
		if (rulePackage == null) {
			logger.severe("Cannot find rule package in project: " + packageName);
			return false;
		}

		return this.createRule(ruleProject,rulePackage,ruleName);
		
	}
	
	public abstract boolean createRule(IlrRuleProject ruleProject,
			IlrRulePackage rulePackage, String ruleName);
	
}
