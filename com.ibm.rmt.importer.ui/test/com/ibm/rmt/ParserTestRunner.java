package com.ibm.rmt;

import com.ibm.rmt.model.DecisionTable;
import com.ibm.rmt.model.ReferenceMode;

public class ParserTestRunner {

	public static void main(String arg[]) {
		RulesCollector collector = new RulesCollector(
				ReferenceMode.RULESET_VARIABLE);
		collector.collect("data/src-rules-test-2.txt");
		for (DecisionTable table : collector.getRuleset()) {
			table.print(System.out);
		}
	}

}
