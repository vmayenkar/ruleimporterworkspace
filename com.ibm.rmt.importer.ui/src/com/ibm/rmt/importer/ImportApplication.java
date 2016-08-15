/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.rmt.importer;

import java.util.logging.Logger;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;


/**
 * 
 */
public class ImportApplication implements IApplication {

	private static Logger logger = Logger.getLogger(ImportApplication.class
			.getSimpleName());

	@Override
	public Object start(IApplicationContext context) throws Exception {
		String[] args = (String[]) context.getArguments().get(
				IApplicationContext.APPLICATION_ARGS);
		String importFile = getArgumentValue(args, "-import");
		if (importFile == null) {
			logger.severe("Import configuration file is not defined");
		} else {
			ImportBatcher batcher = new ImportBatcher();
			batcher.startImport(importFile);
		}
		return IApplication.EXIT_OK;
	}

	private String getArgumentValue(String[] args, String option) {
		int i = 0;
		while (!args[i].equals(option) && i++ < args.length)
			;
		return (i != args.length) ? args[i + 1] : null;
	}

	@Override
	public void stop() {
	}

}
