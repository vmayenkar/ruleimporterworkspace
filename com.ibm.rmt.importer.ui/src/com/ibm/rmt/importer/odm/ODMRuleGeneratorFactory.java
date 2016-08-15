/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.rmt.importer.odm;

import com.ibm.rmt.importer.enums.RuleType;

/**
 * A Factory that determines Rule Generator to be returned
 * based on rule type.
 *
 */
public class ODMRuleGeneratorFactory {
	
	public static ODMRulesGenerator getODMRuleGenerator(RuleType ruleType) {

		ODMRulesGenerator ruleGenerator = null;

		switch (ruleType) {
		case AR:
			ruleGenerator = new ODMActionRuleGenerator();
			break;
		case DT:
			ruleGenerator = new ODMDecisionTableGenerator();
			break;
		}

		return ruleGenerator;
	}
	
	
	
}
