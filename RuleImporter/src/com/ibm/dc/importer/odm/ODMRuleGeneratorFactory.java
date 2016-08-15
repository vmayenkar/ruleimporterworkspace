/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.dc.importer.odm;

import com.ibm.rmt.importer.enums.RuleType;

/**
 * A Factory that determines Rule Generator to be returned
 * based on rule type enum.
 *
 */
public class ODMRuleGeneratorFactory {
	
	public static ODMRuleGenerator getODMRuleGenerator(RuleType ruleType) {

		ODMRuleGenerator ruleGenerator = null;

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
