/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.rmt.importer.utils;

import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Collection of Utility methods
 */
public class RuleImporterUtils {
	
	private static Logger logger = Logger.getLogger(RuleImporterUtils.class
			.getSimpleName());
	
	/**
	 * Returns the list of import elements from the configuration file.
	 * 
	 * @return
	 */
	public static String[] getImportList(Properties imports) {
		return imports.getProperty(RuleImporterConstants.IMPORT_LIST).split(" ");
	}

	/**
	 * Returns a given facet of the given import element (e.g. file, package
	 * name, ...)
	 * 
	 * @param element
	 * @param facet
	 * @return
	 */
	public static String getConfigValue(Properties imports,String element, String facet) {
		return imports.getProperty(element + "." + facet);
	}

	/**
	 * Reads the import configuration file.
	 * 
	 * @param importFile
	 * @return
	 */
	public static Properties getImportConfig(String importFile) {
		try {
			FileReader is = new FileReader(importFile);
			Properties props = new Properties();
			props.load(is);
			String importListString = props.getProperty(RuleImporterConstants.IMPORT_LIST);
			if (importListString == null) {
				throw new Exception(
						"Import list not defined in import configuration file");
			}
			return props;
		} catch (Exception e) {
			logger.severe(e.getMessage());
			return null;
		}
	}
}
