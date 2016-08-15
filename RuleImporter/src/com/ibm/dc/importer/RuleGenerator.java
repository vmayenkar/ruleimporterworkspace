/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.dc.importer;

import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.dc.importer.odm.ConnectionParams;
import com.ibm.dc.importer.odm.ODMRuleGenerator;
import com.ibm.dc.importer.odm.ODMRuleGeneratorFactory;
import com.ibm.rmt.importer.enums.RuleType;
import com.ibm.rmt.importer.utils.RuleImporterConstants;
import com.ibm.rmt.importer.utils.RuleImporterUtils;

/**
 * This class allows the generation of rules artifacts
 * resulting from the import of a source rules file.
 */
public class RuleGenerator {

	private static Logger logger = Logger.getLogger(RuleGenerator.class
			.getSimpleName());
	
	private static Properties imports;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * Process the program arguments
		 */
		String uri = "http://localhost:9081/teamserver";
		String datasource = "jdbc/ilogDataSource";
		String username = "rtsAdmin";
		String password = "passw0rd";
		String importFile = "../RuleImporterCommon/config/import.properties";

		for (int i = 0; i < args.length - 1;) {

			if (args[i].equals("-dcurl")) {
				uri = args[++i];
			} else if (args[i].equals("-dcdatasource")) {
				datasource = args[++i];
			} else if (args[i].equals("-dcuser")) {
				username = args[++i];
			} else if (args[i].equals("-dcpassword")) {
				password = args[++i];
			} else if (args[i].equals("-configfile")) {
				importFile = args[++i];
			}

			i++;
		}
		
		ConnectionParams connParams = new ConnectionParams(uri, datasource, username, password);
		
		startImport(connParams,importFile);

	
	}
	
	public static void startImport(ConnectionParams connParams, String importFile) {
		logger.info("Import started");
		imports = RuleImporterUtils.getImportConfig(importFile);
		if (imports != null) {
			for (String element : RuleImporterUtils.getImportList(imports)) {
				importRules(connParams,element);
			}
		}
	}

	/**
	 * Imports the configuration corresponding to the given property element.
	 * 
	 * @param importElement
	 * @param generator
	 */
	private static void importRules(ConnectionParams connParams, String importElement) {
		
		String projectName = RuleImporterUtils.getConfigValue(imports,importElement, RuleImporterConstants.PROJECT_NAME);
		String packageName = RuleImporterUtils.getConfigValue(imports,importElement, RuleImporterConstants.PACKAGE_NAME);
		String ruleName = RuleImporterUtils.getConfigValue(imports,importElement, RuleImporterConstants.RULE_NAME);
		String rType = RuleImporterUtils.getConfigValue(imports,importElement, RuleImporterConstants.RULE_TYPE);
		
		if ( projectName == null || packageName == null || ruleName == null || rType == null ) {
			logger.severe("Incomplete configuration for import "
					+ importElement);
		} else {
			
			RuleType ruleType = Enum.valueOf(RuleType.class, rType);
			createRule(connParams,projectName, packageName,ruleName,ruleType);
		}
	}
	
	/**
	 * Read the rule definitions from the given imported file, and create the
	 * corresponding rules in the given project, under the given package.
	 * 
	 * @param projectName
	 * @param packageName
	 * @param importedFile
	 * @return
	 */
	public static boolean createRule(ConnectionParams connParams, String projectName, String packageName,
			String ruleName, RuleType ruleType)  {
		
		ODMRuleGenerator ruleGenerator = ODMRuleGeneratorFactory.getODMRuleGenerator(ruleType);
		if (null == ruleGenerator) {
			logger.severe("Cannot find rule generator for : " + ruleType);
			return false;
		}
		
		return ruleGenerator.createRule(connParams,projectName, packageName, ruleName, ruleType);

	}

}
