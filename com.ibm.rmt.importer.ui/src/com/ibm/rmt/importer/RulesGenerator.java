/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.rmt.importer;

import java.util.logging.Logger;

import com.ibm.rmt.importer.enums.RuleType;
import com.ibm.rmt.importer.odm.ODMRuleGeneratorFactory;
import com.ibm.rmt.importer.odm.ODMRulesGenerator;

/**
 * This class allows the generation of rules artifacts
 */
public class RulesGenerator {

	private static Logger logger = Logger.getLogger(RulesGenerator.class
			.getSimpleName());
	
	
	/**
	 * Read the rule definitions from the given imported file, and create the
	 * corresponding rules in the given project, under the given package.
	 * 
	 * @param projectName
	 * @param packageName
	 * @param ruleName
	 * @param ruleType
	 * @return
	 */
	public boolean createRule(String projectName, String packageName,
			String ruleName, RuleType ruleType)  {
		
		ODMRulesGenerator ruleGenerator = ODMRuleGeneratorFactory.getODMRuleGenerator(ruleType);
		if (null == ruleGenerator) {
			logger.severe("Cannot find rule generator for : " + ruleType);
			return false;
		}
		
		logger.info("Importing "+ruleName+" of type "+ruleType);
		return ruleGenerator.createRule(projectName, packageName, ruleName, ruleType);
	}
}
