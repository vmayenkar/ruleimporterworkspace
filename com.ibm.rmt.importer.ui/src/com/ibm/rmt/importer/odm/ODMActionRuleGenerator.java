/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.rmt.importer.odm;

import ilog.rules.engine.IlrPriorityValues;
import ilog.rules.studio.model.IlrStudioModelPlugin;
import ilog.rules.studio.model.base.IlrRulePackage;
import ilog.rules.studio.model.base.IlrRuleProject;
import ilog.rules.studio.model.brl.IlrActionRule;
import ilog.rules.studio.model.brl.IlrBrlFactory;
import ilog.rules.studio.model.resource.IlrResourceManager;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.CoreException;

/**
 * This class illustrates use of Rule Model API for Rule Designer to create
 * Action rule.
 */
public class ODMActionRuleGenerator extends ODMRulesGenerator {
	
	private static Logger logger = Logger.getLogger(ODMActionRuleGenerator.class
			.getSimpleName());
	
	private static final String RULE_DEFINITION = 
					"if \n" +
					"  the last name of 'the borrower' is empty \n" +
					"then \n" +
					"  in 'the loan report' , reject the data with the message \"Name is empty\";";

	
	@Override
	public boolean createRule(IlrRuleProject ruleProject, IlrRulePackage rulePackage,
			String ruleName) {
		
		try {
			
			//Get handle to the factory
			IlrBrlFactory factory = IlrBrlFactory.eINSTANCE;
						
			//Create an instance of action rule using IlrBrlFactory instance
			IlrActionRule actionRule = factory.createActionRule();
			
			//Populate attributes of the model
			actionRule.setName(ruleName);
			actionRule.setLocale(Locale.US);
			actionRule.setPriority(Integer.toString(IlrPriorityValues.maximum));
			actionRule.setRuleProject(ruleProject);
			actionRule.setPackage(rulePackage);
			actionRule.setDefinition(RULE_DEFINITION);

			//Persist the action rule to the workspace
			rulePackage.getFolderElements().add(actionRule);
			IlrResourceManager resourceManager = IlrStudioModelPlugin
					.getResourceManager();
			resourceManager.saveElement(actionRule);

			return true;
		} catch (CoreException e) {
			logger.log(Level.SEVERE, "Error occured during creation of action rule " + ruleName , e);
			return false;
		}
	}

}
