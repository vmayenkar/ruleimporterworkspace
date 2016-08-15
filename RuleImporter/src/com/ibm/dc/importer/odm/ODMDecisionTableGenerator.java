/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.dc.importer.odm;

import ilog.rules.dt.IlrDTController;
import ilog.rules.dt.model.IlrDTActionDefinition;
import ilog.rules.dt.model.IlrDTModel;
import ilog.rules.dt.model.IlrDTPartition;
import ilog.rules.dt.model.IlrDTPartitionDefinition;
import ilog.rules.teamserver.brm.IlrBrmPackage;
import ilog.rules.teamserver.brm.IlrDecisionTable;
import ilog.rules.teamserver.brm.IlrRulePackage;
import ilog.rules.teamserver.brm.IlrRuleProject;
import ilog.rules.teamserver.model.IlrSession;
import ilog.rules.teamserver.model.IlrSessionHelper;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.rmt.importer.utils.GradeDTHelper;

/**
 * This class illustrates use of Decision Center APIs
 * to create Decision Table
 */
public class ODMDecisionTableGenerator extends ODMRuleGenerator {
	
	private static Logger logger = Logger.getLogger(ODMDecisionTableGenerator.class
			.getSimpleName());
	
	private IlrDecisionTable dTable;
	private IlrDTController dtController;
	
	@Override
	public boolean createRule(IlrSession session, IlrRuleProject ruleProject, 
			IlrRulePackage rulePackage,
			String ruleName)  {
	
		try {
			IlrBrmPackage model = session.getBrmPackage();
			
			dTable = (IlrDecisionTable) IlrSessionHelper.createRule(session, model.getDecisionTable(),
							rulePackage,ruleName, null);

			dtController = IlrSessionHelper.getDTController(session, dTable, Locale.US);

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
			
			GradeDTHelper gradeDTHelper = new GradeDTHelper(dtModel);
			
			//Add condition columns
			gradeDTHelper.addFirstCondition();
			gradeDTHelper.addSecondCondition();
			
			//Add action columns
			gradeDTHelper.addFirstActionColumn();
			gradeDTHelper.addSecondActionColumn();
			
			String dtBody = IlrSessionHelper
					.dtControllerToStorableString(session, dtController);
			IlrSessionHelper.setDefinition(session, dTable, dtBody);
			
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error occured during creation of Decision table " + ruleName , e);
			return false;
		}
	}
	
	

	
	
}
