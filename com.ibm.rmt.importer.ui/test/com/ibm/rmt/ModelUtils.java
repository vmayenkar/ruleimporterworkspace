package com.ibm.rmt;

import com.ibm.rmt.model.Attribute;
import com.ibm.rmt.model.DecisionTable;
import com.ibm.rmt.model.ReferenceMode;

public class ModelUtils {

	private RulesCollector collector;

	public ModelUtils(RulesCollector collector) {
		this.collector = collector;
	}

	protected void printXOMElementsFor(String var) {
		System.out.println(var);
		String format = "	public %s %s;";
		for (DecisionTable table : collector.getRuleset()) {
			for (Attribute attr : table.getConditionAttributes()) {
				if (attr.getInstanceName().equals(var)) {
					String result = String.format(format, attr.getJavaType(),
							attr.getFieldName());
					System.out.println(result);
				}
			}
		}
	}

	public void printXOMElements() {
		printXOMElementsFor("input");
		printXOMElementsFor("var");
		printXOMElementsFor("output");
	}

	// <variables name="name" type="type" initialValue="" verbalization="name"/>
	public void printRulesetVars() {
		String format = "<variables name=\"%s\" type=\"%s\" initialValue=\"\" verbalization=\"%s\"/>";
		for (DecisionTable table : collector.getRuleset()) {
			for (Attribute attr : table.getConditionAttributes()) {
				String result = String.format(format, attr.getFieldName(),
						attr.getJavaType(), attr.getFieldName());
				System.out.println(result);
			}
			Attribute attr = table.getActionAttribute();
			String result = String.format(format, attr.getFieldName(),
					attr.getJavaType(), attr.getFieldName());
			System.out.println(result);
		}
	}

	public static void main(String arg[]) {
		RulesCollector collector = new RulesCollector(
				ReferenceMode.RULESET_VARIABLE);
		collector.collect("data/src-rules-test-2.txt");
		ModelUtils utils = new ModelUtils(collector);
		utils.printRulesetVars();
	}

}
