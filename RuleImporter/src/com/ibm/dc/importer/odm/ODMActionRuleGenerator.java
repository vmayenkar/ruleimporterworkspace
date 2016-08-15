/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.dc.importer.odm;

import ilog.rules.teamserver.brm.IlrActionRule;
import ilog.rules.teamserver.brm.IlrBrmPackage;
import ilog.rules.teamserver.brm.IlrRulePackage;
import ilog.rules.teamserver.brm.IlrRuleProject;
import ilog.rules.teamserver.model.IlrApplicationException;
import ilog.rules.teamserver.model.IlrSession;
import ilog.rules.teamserver.model.IlrSessionHelper;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class illustrates use of Decision Center APIs
 * to create Action Rule
 *
 */
public class ODMActionRuleGenerator extends ODMRuleGenerator {
	
	private static Logger logger = Logger.getLogger(ODMActionRuleGenerator.class
			.getSimpleName());
	
	private static final String RULE_DEFINITION = 
			"if \n" +
			"  the last name of 'the borrower' is empty \n" +
			"then \n" +
			"  in 'the loan report' , reject the data with the message \"Name is empty\";";
	@Override
	public boolean createRule(IlrSession session, IlrRuleProject ruleProject, 
			IlrRulePackage rulePackage,
			String ruleName)  {

			try {
								
				//Get handle to the package for the Decision Centre rule model
				IlrBrmPackage model = session.getBrmPackage();
				//Create a rule and commit it to the working baseline using create rule method
				IlrActionRule actionRule= (IlrActionRule) IlrSessionHelper.createRule(session, model.getActionRule(), rulePackage,ruleName, RULE_DEFINITION);

				//Another convenient method to create an action rule
				//IlrSessionHelper.createActionRule(session, rulePackage, ruleName,body);
				
				//Set target value of a feature that is part of base model
				String priorityValue = Integer.toString(50);
				actionRule.setRawValue(model.getRule_Priority(), priorityValue);

				
				return true;
			} catch (IlrApplicationException e) {
				logger.log(Level.SEVERE, "Error occured during creation of action rule " + ruleName , e);
				return false;
			}
	}

}
