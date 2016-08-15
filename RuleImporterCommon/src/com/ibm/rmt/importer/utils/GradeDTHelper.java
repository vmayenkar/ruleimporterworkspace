/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.rmt.importer.utils;

import ilog.rules.dt.IlrDTExpressionManager;
import ilog.rules.dt.model.IlrDTActionDefinition;
import ilog.rules.dt.model.IlrDTActionSet;
import ilog.rules.dt.model.IlrDTModel;
import ilog.rules.dt.model.IlrDTPartition;
import ilog.rules.dt.model.IlrDTPartitionDefinition;
import ilog.rules.dt.model.IlrDTPartitionItem;
import ilog.rules.dt.model.expression.IlrDTExpressionDefinition;
import ilog.rules.dt.model.expression.IlrDTExpressionInstance;
import ilog.rules.dt.model.helper.IlrDTPropertyHelper;

import java.util.Arrays;
import java.util.List;

/**
 * Helper class to facilitate creation of decision table and decision 
 * tree.
 */
public class GradeDTHelper {

	private IlrDTModel dtModel;
	
	public GradeDTHelper(IlrDTModel dtModel) {
		super();
		this.dtModel = dtModel;
	}

	/**
	 * Adds the first condition column/nodes, based on the yearly repayments
	 * 
	 * <p>
	 * Creates 4 bands:
	 *     <ol>
	 *         <li>0 to 10000</li>
	 *         <li>10000 to 30000</li>
	 *         <li>30000 to 60000</li>
	 *         <li>>60000</li>
	 *     </ol> 
	 * </p>
	 */
	public void addFirstCondition(){
		
		IlrDTExpressionManager expressionManager = dtModel.getExpressionManager();
		
		String repaymentDefinitionText = 
			    "the yearly repayment of 'the loan' is at least <min> and less than <max>";
		
		//Create the base expression for the first column/node
		IlrDTExpressionDefinition repaymentExpression = expressionManager
				.newExpressionDefinition(repaymentDefinitionText);
		
		//The first Partition and Partition Definition already exist and are retrieved 
		//from the model and not created
		IlrDTPartitionDefinition repaymentPartitionDefinition = dtModel.getPartitionDefinition(0);
		
		//Associate the Expression Definition with the Partition Definition
		repaymentPartitionDefinition.setExpression(repaymentExpression);
		
		//Set a Title for the column/node, we need to set it on the Partition Definition
		//using helper class 
		IlrDTPropertyHelper.setDefinitionTitle(repaymentPartitionDefinition, "Yearly repayment");
		
		
		 //Once the column/node  (Partition Definition) is defined, the rows / branches
		 //(Partition Items) are created. The first column's entries are stored
		 //in the 'root' Partition.		
		IlrDTPartition yearlyPaymentPartition = dtModel.getRoot();
		
		// the root node of an empty tree contains a single partition item
		IlrDTPartitionItem piyearlyPaymentBetweenZeroTo10K = yearlyPaymentPartition
				.getPartitionItem(0);
		

		//To create the expression instance, we need to provide values for placeholders
		//for example, 0 for <min> and 10000 for <max> in case of first partition.
		List<String> parameters = Arrays.asList(new String[]{"0", "10000"});
		
		
		//The values along with the definition are passed to the Expression
		//Manager to generate the Expression Instance for this Partition Item
		IlrDTExpressionInstance yearlyPaymentExpressionInstance = expressionManager
				.newExpressionInstance(repaymentExpression, parameters);
		piyearlyPaymentBetweenZeroTo10K.setExpression(yearlyPaymentExpressionInstance);

		
		//Create a second partition item. This one will go after the first one
		//(0 to 10000) in position 1, and will cover the range of 10000 to 30000. 
		//This Partition Item doesn't already exist, so we use 
		//DTModel.addPartitionItem to create it.
		yearlyPaymentExpressionInstance = expressionManager.newExpressionInstance(
				repaymentExpression, Arrays.asList(new String[]{"10000", "30000"}));
		dtModel.addPartitionItem(yearlyPaymentPartition, 1, yearlyPaymentExpressionInstance);

		yearlyPaymentExpressionInstance = expressionManager.newExpressionInstance(
				repaymentExpression, Arrays.asList(new String[]{"30000", "60000"}));
		dtModel.addPartitionItem(yearlyPaymentPartition, 2, yearlyPaymentExpressionInstance);

		//use helper method to create 4th partition
		createPartitionItem(
				expressionManager, yearlyPaymentPartition, 3 , repaymentExpression,
				"<a number> is at least <a number>", "60000");
	}
	
	/**
	 * Adds a second condition column, based on corporate score.
	 * 
	 * <p>
	 * Creates 3 bands of corporate score for each of the 4 bands created for yearly repayment:
	 *     <ol>
	 *         <li>>=900</li>
	 *         <li>600 to 900</li>
	 *         <li>300 to 600</li>
	 *     </ol> 
	 * </p>
	 */
	public void addSecondCondition(){
		
		final IlrDTPartition rootPartition = dtModel.getRoot();
		IlrDTPartitionItem piRepaymentZeroTo10K = rootPartition.getPartitionItem(0);
		IlrDTPartitionItem piRepayment10KTo30K = rootPartition.getPartitionItem(1);
		IlrDTPartitionItem piRepayment30Kto60K = rootPartition.getPartitionItem(2);
		IlrDTPartitionItem piRepaymentMoreThan60K = rootPartition.getPartitionItem(3);

		IlrDTExpressionManager expressionManager = dtModel
				.getExpressionManager();
		
		// Create the base expression for the “Corporate score” condition column.
		String scoreDefinitionText = 
			    "the corporate score in 'the loan report' is at least <min> and less than <max>";
		
		IlrDTExpressionDefinition scoreExpression = expressionManager
				.newExpressionDefinition(scoreDefinitionText);
		IlrDTPartitionDefinition scorePartitionDefinition = dtModel
				.newPartitionDefinition(scoreExpression);
		IlrDTPropertyHelper.setDefinitionTitle(scorePartitionDefinition,
				"Corporate score");

		dtModel.addPartitionDefinition(1, scorePartitionDefinition);
		
		IlrDTExpressionInstance csConditionExpression = expressionManager
				.newOverriddenExpressionInstance("<a number> is at least <a number>", Arrays.asList(new String[]{"900"}),scoreExpression);
		
		
		IlrDTPartition csPartitionRepaymentZeroTo10K = dtModel.addPartition(
				piRepaymentZeroTo10K, scorePartitionDefinition,
				csConditionExpression);
		
		IlrDTPartition csPartitionRepayment10KTo30K = dtModel.addPartition(
				piRepayment10KTo30K, scorePartitionDefinition,
				csConditionExpression);

		IlrDTPartition csPartitionRepayment30KTo60K = dtModel.addPartition(
				piRepayment30Kto60K, scorePartitionDefinition,
				csConditionExpression);
		
		IlrDTPartition csPartitionRepaymentMoreThan60K = dtModel.addPartition(
				piRepaymentMoreThan60K, scorePartitionDefinition,
				csConditionExpression);
	
		//Add some more PartitionItems for a range of corporate score using the helper method.
		createPartitionItem(expressionManager, csPartitionRepaymentZeroTo10K, 1, scoreExpression, null, "600", "900");
		createPartitionItem(expressionManager, csPartitionRepaymentZeroTo10K, 2, scoreExpression, null, "300", "600");
		
		createPartitionItem(expressionManager, csPartitionRepayment10KTo30K, 1, scoreExpression, null, "600", "900");
		createPartitionItem(expressionManager, csPartitionRepayment10KTo30K, 2, scoreExpression, null, "300", "600");
		
		createPartitionItem(expressionManager, csPartitionRepayment30KTo60K, 1, scoreExpression, null, "600", "900");
		createPartitionItem(expressionManager, csPartitionRepayment30KTo60K, 2, scoreExpression, null, "300","600");
		
		createPartitionItem(expressionManager, csPartitionRepaymentMoreThan60K, 1, scoreExpression, null, "600", "900");
		createPartitionItem(expressionManager, csPartitionRepaymentMoreThan60K, 2, scoreExpression, null, "300", "600");
	}
	
	/**
	 * The third column in the table is an action column
	 * that sets the Grade in the loan report
	 */
	public void addFirstActionColumn() throws Exception{
			
		//Action columns are represented as IlrDTActionDefinitions. As with 
		//condition columns (Partition Definitions), these take an Expression 
		//for their definition, and are titled in the same way.
		IlrDTExpressionManager expressionManager = dtModel
				.getExpressionManager();
		
		String gradeDefinitionText = 
			    "set the grade of 'the loan report' to <a string>";
		
		IlrDTExpressionDefinition gradeExpression= expressionManager
				.newExpressionDefinition(gradeDefinitionText, null);
		IlrDTActionDefinition gradeActionDefinition = dtModel.newActionDefinition(gradeExpression);
		IlrDTPropertyHelper.setDefinitionTitle(gradeActionDefinition, "Grade");
		
		dtModel.addActionDefinition(0, gradeActionDefinition);
		
		//Actions are the leaf nodes in the tree model. To assign actions, we 
		//need to retrieve the Partition Items from the second column. Here we use the 
		//helper method to action part to the corporate score partitions
		addActionToPartitionItem(0, 0, 0, gradeActionDefinition, expressionManager, "\"A\"");
		addActionToPartitionItem(0, 1, 0, gradeActionDefinition, expressionManager, "\"A\"");
		addActionToPartitionItem(0, 2, 0, gradeActionDefinition, expressionManager, "\"B\"");
		
		addActionToPartitionItem(1, 0, 0, gradeActionDefinition, expressionManager, "\"A\"");
		addActionToPartitionItem(1, 1, 0, gradeActionDefinition, expressionManager, "\"B\"");
		addActionToPartitionItem(1, 2, 0, gradeActionDefinition, expressionManager, "\"C\"");
		
		addActionToPartitionItem(2, 0, 0, gradeActionDefinition, expressionManager, "\"B\"");
		addActionToPartitionItem(2, 1, 0, gradeActionDefinition, expressionManager, "\"C\"");
		addActionToPartitionItem(2, 2, 0, gradeActionDefinition, expressionManager, "\"D\"");
		
		addActionToPartitionItem(3, 0, 0, gradeActionDefinition, expressionManager, "\"C\"");
		addActionToPartitionItem(3, 1, 0, gradeActionDefinition, expressionManager, "\"D\"");
		addActionToPartitionItem(3, 2, 0, gradeActionDefinition, expressionManager, "\"E\"");
	}
	
	/**
	 * The fourth column in the table is an action column
	 * that sets the message in the loan report. This method
	 * follows the same pattern as the addFirstActionColumn method.
	 */
	public void addSecondActionColumn() throws Exception{
			
		IlrDTExpressionManager expressionManager = dtModel
				.getExpressionManager();
		IlrDTExpressionDefinition actionDefinitionExpression= expressionManager
				.newExpressionDefinition(
						"in 'the loan report', add the message <a string>",
						null);
		IlrDTActionDefinition actionDefinition = dtModel.newActionDefinition(actionDefinitionExpression);
		IlrDTPropertyHelper.setDefinitionTitle(actionDefinition, "Message");
		
		dtModel.addActionDefinition(1, actionDefinition);
		
		addActionToPartitionItem(0, 0, 1, actionDefinition, expressionManager, "\"Very low risk loan\"");
		addActionToPartitionItem(0, 1, 1, actionDefinition, expressionManager, "\"Very low risk loan\"");
		addActionToPartitionItem(0, 2, 1, actionDefinition, expressionManager, "\"Low risk loan\"");
		
		addActionToPartitionItem(1, 0, 1, actionDefinition, expressionManager, "\"Very low risk loan\"");
		addActionToPartitionItem(1, 1, 1, actionDefinition, expressionManager, "\"Low risk loan\"");
		addActionToPartitionItem(1, 2, 1, actionDefinition, expressionManager, "\"Average risk loan\"");
		
		addActionToPartitionItem(2, 0, 1, actionDefinition, expressionManager, "\"Low risk loan\"");
		addActionToPartitionItem(2, 1, 1, actionDefinition, expressionManager, "\"Average risk loan\"");
		addActionToPartitionItem(2, 2, 1, actionDefinition, expressionManager, "\"Risky loan\"");
		
		addActionToPartitionItem(3, 0, 1, actionDefinition, expressionManager, "\"Average risk loan\"");
		addActionToPartitionItem(3, 1, 1, actionDefinition, expressionManager, "\"Risky loan\"");
		addActionToPartitionItem(3, 2, 1, actionDefinition, expressionManager, "\"Very risky loan\"");
	}
	
	/**
	 * Helper method to add action to a partition item
	 * 
	 * @param yearlyPaymentPartitionItem
	 * @param corporateScorePartitionItem
	 * @param actionItem
	 * @param actionDefinition
	 * @param expressionManager
	 * @param actionValue
	 */
	private void addActionToPartitionItem(int yearlyRepaymentPartitionItem, 
			int corporateScorePartitionItem, int actionItem,
			IlrDTActionDefinition actionDefinition,
			IlrDTExpressionManager expressionManager,
			String actionValue) {
		
		IlrDTExpressionInstance actionExpression = expressionManager
				.newExpressionInstance(actionDefinition.getExpressionDefinition(), Arrays.asList(new String[]{actionValue}));
		
		addActionToPartitionItem(yearlyRepaymentPartitionItem, corporateScorePartitionItem, actionItem, actionDefinition, actionExpression);
	}
	
	/**
	 *  Helper method to add action to a partition item
	 *  
	 * @param yearlyRepaymentPartitionItem
	 * @param corporateScorePartitionItem
	 * @param actionItem
	 * @param actionDefinition
	 * @param actionExpression
	 */
	private void addActionToPartitionItem(int yearlyRepaymentPartitionItem, 
			int corporateScorePartitionItem,int actionItem,
			IlrDTActionDefinition actionDefinition,
			IlrDTExpressionInstance actionExpression) {
		
		//getStatement returns the next branch in the tree. If there is another
		//condition (Partition) after this Partition Item, that will be 
		//returned. If there are no further Partitions, Action Sets contain the
		//actions to be performed if this set of conditions have been met.
		//
		//First retrieve the specified Partition Item.
		IlrDTPartition partition = (IlrDTPartition) dtModel.getRoot().getPartitionItem(yearlyRepaymentPartitionItem).getNextStatement();
		IlrDTPartitionItem partitionItem = partition.getPartitionItem(corporateScorePartitionItem);

		
		//Next, get the Action Set (again using getStatement) and create a new 
		//Action
		IlrDTActionSet actionSet = (IlrDTActionSet) partitionItem.getStatement();
		actionSet.addAction(actionItem, actionDefinition, actionExpression);
	}

	/**
	 * Creates a new Partition Item within the supplied Partition
	 * 
	 * <p>
	 * If <em>overriddenDefinition</em> is specified, an overridden expression
	 * instance is created. If <em>null</em>, a normal instance is created 
	 * instead.
	 * </p>
	 * 
	 * @param expressionManager used to create Expression Instances
	 * @param partition the parent Partition for the new Item
	 * @param position the position of the new Item in the parent Partition
	 * @param expressionDefinition the Expression Definition from the Partition Definition
	 * @param overriddenDefinition an overridden definition for the instance, if required.
	 * @param parameters values for the expression instance
	 * 
	 * @return the newly created Partition Item
	 */
	private IlrDTPartitionItem createPartitionItem(
			IlrDTExpressionManager expressionManager, IlrDTPartition partition,
			int position, IlrDTExpressionDefinition expressionDefinition,
			String overriddenDefinition, String... parameters) {

		IlrDTExpressionInstance expressionInstance = null;


		//If the expression is to be overridden, a new definition must be supplied
		if (overriddenDefinition == null) {
			expressionInstance = expressionManager.newExpressionInstance(
					expressionDefinition, Arrays.asList(parameters));
		} else {
			expressionInstance = expressionManager
					.newOverriddenExpressionInstance(overriddenDefinition,
							Arrays.asList(parameters), expressionDefinition);
		}

		//Once we have an expression instance, use it to create the new partition item 
		IlrDTPartitionItem partitionItem = dtModel.addPartitionItem(partition,
				position, expressionInstance);

		return partitionItem;
	}
	
}
