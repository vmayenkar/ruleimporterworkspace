/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.rmt.importer;

import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.rmt.importer.enums.RuleType;
import com.ibm.rmt.importer.utils.RuleImporterConstants;
import com.ibm.rmt.importer.utils.RuleImporterUtils;

/**
 * This class is used to process a batch of rule imports from a configuration
 * file (import.properties)
 */
public class ImportBatcher {

	private Properties imports;

	private static Logger logger = Logger.getLogger(ImportBatcher.class
			.getSimpleName());

	public void startImport(String importFile) {
		logger.info("Import started");
		imports = RuleImporterUtils.getImportConfig(importFile);
		if (imports != null) {
			RulesGenerator generator = new RulesGenerator();
			for (String element : RuleImporterUtils.getImportList(imports)) {
				importRules(element, generator);
			}
		}
	}

	/**
	 * Imports the configuration corresponding to the given property element.
	 * 
	 * @param importElement
	 * @param generator
	 */
	private void importRules(String importElement, RulesGenerator generator) {
		
		String projectName = RuleImporterUtils.getConfigValue(imports,importElement, RuleImporterConstants.PROJECT_NAME);
		String packageName = RuleImporterUtils.getConfigValue(imports,importElement, RuleImporterConstants.PACKAGE_NAME);
		String ruleName = RuleImporterUtils.getConfigValue(imports,importElement, RuleImporterConstants.RULE_NAME);
		String rType = RuleImporterUtils.getConfigValue(imports,importElement, RuleImporterConstants.RULE_TYPE);
		
		if (projectName == null || packageName == null || ruleName == null || rType == null ) {
			logger.severe("Incomplete configuration for import "
					+ importElement);
		} else {
			RuleType ruleType = Enum.valueOf(RuleType.class, rType);
			generator.createRule(projectName, packageName,ruleName,ruleType);
		}
	}
	
	

}
