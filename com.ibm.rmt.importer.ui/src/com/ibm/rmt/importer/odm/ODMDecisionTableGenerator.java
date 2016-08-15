/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.rmt.importer.odm;

import ilog.rules.dt.IlrDTController;
import ilog.rules.dt.model.IlrDTActionDefinition;
import ilog.rules.dt.model.IlrDTModel;
import ilog.rules.dt.model.IlrDTPartition;
import ilog.rules.dt.model.IlrDTPartitionDefinition;
import ilog.rules.dt.model.helper.IlrDTHelper;
import ilog.rules.studio.model.IlrStudioModelPlugin;
import ilog.rules.studio.model.base.IlrRulePackage;
import ilog.rules.studio.model.base.IlrRuleProject;
import ilog.rules.studio.model.dt.IlrDTService;
import ilog.rules.studio.model.dt.IlrDecisionTable;
import ilog.rules.studio.model.dt.IlrDtFactory;
import ilog.rules.studio.model.resource.IlrResourceManager;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.rmt.importer.utils.GradeDTHelper;

/**
 * This class illustrates use of Rule Model API for Rule Designer to create
 * Decision table.
 */
public class ODMDecisionTableGenerator extends ODMRulesGenerator {
	
	private static Logger logger = Logger.getLogger(ODMDecisionTableGenerator.class
			.getSimpleName());
	
	@Override
	public boolean createRule(IlrRuleProject ruleProject,
			IlrRulePackage rulePackage, String ruleName)  {
		
		try {
			//Get handle to the factory
			IlrDtFactory factory = IlrDtFactory.eINSTANCE;
			
			// Create the decision table object using IlrDtFactory
			IlrDecisionTable dt = factory.createDecisionTable();
			
			//Set basic artifact properties.
			dt.setName(ruleName);
			dt.setLocale(Locale.US);

			// Create a controller associated with the decision table to manage the
			// DT content creation.
			IlrDTService dtService = (IlrDTService) ruleProject
					.getService(IlrDTService.SERVICE_ID);
			IlrDTController dtController = IlrDTHelper.createDTController(dt,
					dtService);

			IlrDTModel dtModel = dtController.getDTModel();
			
			//Remove default table
			int partitions = dtModel.getPartitionDefinitionCount();
			for(int i=1; i<partitions; i++){
				IlrDTPartitionDefinition def = dtModel.getPartitionDefinition(1);
				dtModel.removePartitionDefinition(def);
			}
			
			IlrDTActionDefinition actionDefinition = dtModel.getActionDefinition(0);
			dtModel.removeActionDefinition(actionDefinition);
			
			IlrDTPartition rootPartition = dtModel.getRoot();
			int partitionItems = rootPartition.getPartitionItemCount();
			for(int i=1; i<partitionItems; i++){
				rootPartition.removePartitionItem(1);
			}
			
			//Create a helper class to create Decision Table
			GradeDTHelper gradeDTHelper = new GradeDTHelper(dtModel);
			
			//Add condition columns
			gradeDTHelper.addFirstCondition();
			gradeDTHelper.addSecondCondition();
			
			//Add action columns
			gradeDTHelper.addFirstActionColumn();
			gradeDTHelper.addSecondActionColumn();

			//Save the Decision Table.
			dtService.commitDTController(dtController);
			rulePackage.getFolderElements().add(dt);
			IlrResourceManager resourceManager = IlrStudioModelPlugin
						.getResourceManager();
			resourceManager.saveElement(dt);
			
			return true;
			
		} catch (Exception e){
			logger.log(Level.SEVERE, "Error occured during creation of decision table " + ruleName , e);
			return false;
		}
	}

}
