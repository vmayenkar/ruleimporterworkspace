This repository contains resources related to IBM developerWorks tutorial "Generate rule artifacts with ODM APIs" showing you how the ODM  APIs can be used to create rule artifacts programmatically. 

For license information, see license.txt file in the root directory of this project.

This code sample is based on IBM ODM version 8.7.1.

The workspace and steps required to compile, build and run the sample code are described below.

Workspace
=========
The workspace consists of three projects as described below

**RuleImporterCommon**
This project contains classes that are used in the other two projects.
Include jars in <ODM install directory>/teamserver/lib and the jars in 
<ODM install directory>/teamserver/lib/eclipse_plugin in the build path of this project.
						
**RuleImporter**
This project contains classes that connect to the Decision Center instance and 
create rule artifacts in the Decision Center. The classes in this project illustrate 
use of ODM Decision Center API to connect and create rule artifacts in the Decision Center. 
							
RuleGenerator class contains main method that is used to launch the program to 
create rule artifacts. The class expects URL of the Decision Center instance,
the user name and the password to connect to the Decision Center, the datasource for the 
Decision Center and location of the configuration file that describes rule project 
and rule package under that contain the generated rule artifacts. The configuration file is 
located in the *config* folder of the *RuleImportCommon* project.
							
Include jars in <ODM install directory>/teamserver/lib 
and the jars in <ODM install directory>/teamserver/lib/eclipse_plugin in the build path
of this project. 
						
**com.ibm.rmt.importer.ui**	
This project contains classes that implement plug-in for Rule Designer and illustrate use of
ODM Rule Designer API to create ODM rule artifacts. 
							
The rule importer component is implemented as a plug-in in the ODM Rule Designer. The plug-in
requires the following plug-ins for the operation of rule importer plug-in.
							
- org.eclipse.ui 
- org.eclipse.core.runtime
- ilog.rules.studio.model
- ilog.rules.studio.model.brl
- ilog.rules.studio.model.dt
													
The plug-in uses classes from RuleImporterCommon project that is packaged as Common.jar.
							
You need a configuration file for performing the import. The configuration file describes where 
to place the rule artifacts that are created during the import. The configuration file is located 
in the *config* folder of the *RuleImportCommon* project.
							
Configuration File
==================
The configuration file specifies the properties needed to create rule artifacts such rule project, 
rule package and rule type. The file is located in config folder of the RuleImportCommon project.
							
Getting started
===============
The steps below assume that you are familiar with the use of IBM ODM Rule Designer tool. Refer to [IBM ODM documentation] (http://www.ibm.com/support/knowledgecenter/SSQP76_8.7.1/welcome/kc_welcome_odmV.html) if you are not familiar with IBM ODM Rule Designer tool.

1. Create a new workspace called *ruleimporterworkspace* and import **RuleImporterCommon**, **RuleImporter** and **com.ibm.rmt.importer.ui** projects into the newly created workspace using Rule Designer. Update Java build path for the projects as described in the section on Workspace description and ensure that there are no errors in your workspace.

2. Create another workspace called *sampleworkspace* and follow the directions at [Setting up the Custom report generation sample] ( https://www.ibm.com/support/knowledgecenter/SSQP76_8.7.1/com.ibm.odm.dserver.rules.samples/designer_smp_topics/smp_rd_brmcustgen_set.html) to setup sample workspace. The rule importer uses **loanvalidation-rules** sample rule project to illustrate creation of rule artifacts using IBM ODM APIs. Delete the decision table *grade* in the *eligibility* rule package and action rule *checkName* in the *validation.borrower* rule package in rule project loanvalidation-rules. These rules are created during the rule import process.

Running Rule Designer plug-in sample
-------------------------------------
Open *ruleimporterworkspace* in the Rule Designer. The project *com.ibm.rmt.importer.ui* contains a run configuration *RuleImporter.launch* that should be used to run the Rule Designer plug-in sample. Edit this run configuration to update the location of **Workspace Data** on the **Main** tab of the run configuration. The location should be updated to point to location of the *sampleworkspace* created in step 2 e.g. *${workspace_loc}/../sampleworkspace*.

Launch *RuleImporter* from run configurations in *ruleimporterworkspace*. This action causes another instance of the Rule Designer to start with *sampleworkspace* as the workspace. You would notice that the newly started instance of Rule Designer contains an addition menu item called *Import Rules*. Click *Import* menu option under *Import Rules* menu to display file chooser. Select the configuration file located in the *config* folder of the *RuleImportCommon* project and click open button to select the file. The rule import process starts and creates decision table *grade* in the *eligibility* rule package and action rule *checkName* in the *validation.borrower* rule package in  the rule project loanvalidation-rules.

You can run Rule Designer plug-in sample multiple times. Please remember to delete the decision table *grade* in the *eligibility* rule package and action rule *checkName* in the *validation.borrower* rule package in rule project loanvalidation-rules before you run the sample.

Running Decision Center APIs sample
-----------------------------------
You need access to Decision Center instance to run Decision Center APIs sample contained in the project *RuleImporter*.  

Open the *sampleworkspace* created in the step 2 in Rule Designer and publish *loanvalidation-rules* project to the Decision Center. Refer to [IBM ODM documentation](http://www.ibm.com/support/knowledgecenter/SSQP76_8.7.1/com.ibm.odm.dcenter.synchro/topics/tsk_synch_pubdc.html) if you are not familiar with the process of publishing rule projects to Decision Center.

Open *ruleimporterworkspace* in the Rule Designer. Edit *RuleGenerator* Java class in the *RuleImporter* project to specify uri of the Decision Center instance, the username and password that you use to connect to the Decision Center. 

The project *RuleImporter* contains a run configuration *RuleGenerator.launch* that should be used to run the Decision Center APIs sample. Launch *RuleGenerator* from run configurations in *ruleimporterworkspace* to start the import and create decision table *grade* in the *eligibility* rule package and action rule *checkName* in the *validation.borrower* rule package in the rule project loanvalidation-rules on the Decision Center. 

You can run Decision Center APIs sample multiple times. Please remember to delete the decision table *grade* in the *eligibility* rule package and action rule *checkName* in the *validation.borrower* rule package in rule project loanvalidation-rules on the Decision Center before you run the sample.
						