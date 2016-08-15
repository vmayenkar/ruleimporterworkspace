This repository contains resources related to IBM developerWorks tutorial "Programmatic Creation of Rule Artifacts using ODM APIs" showing you how the ODM  APIs can be used to create rule artifacts programmatically. 

For license information, see license.txt file in the root directory of this project.

This code sample is based on IBM ODM version 8.7.1

The workspace and steps required to compile, build and run the sample code is described below.

Workspace Description
=====================

The workspace consists of three projects as described below

1) RuleImporterCommon - 	This project contains classes that are used in the other two projects.
							Include jars in <ODM install directory>/teamserver/lib and the jars 
							in <ODM install directory>/teamserver/lib/eclipse_plugin in the build path
							of this project.
						
2) RuleImporter 	-		This project contains classes that connect to the Decision Center instance 
							and create rule artifacts. The classes in this project illustrate use of ODM
							Decision Center API to connect and create rule artifacts in the Decision Center. 
							
							RuleGenerator class contains main method that is used to launch the 
							program to create rule artifacts. The class expects URL of the Decision Center instance,
							the user name and the password to connect to the Decision Center, the datasource for the 
							Decision Center and location of the configuration file that describes rule project 
							and rule package under that contain the generated rule artifacts. The configuration file is 
							located in the config folder of the RuleImportCommon project.
							
							Include jars in <ODM install directory>/teamserver/lib 
							and the jars in <ODM install directory>/teamserver/lib/eclipse_plugin in the build path
							of this project. 
						
3) com.ibm.rmt.importer.ui-	This project contains classes that implement plug-in for Rule Designer and illustrate use of
							ODM Rule Designer API to create ODM rule artifacts. 
							
							The rule importer component is implemented as a plug-in of the ODM Rule Designer. The plug-in
							requires the following plug-ins for the operation of rule importer plug-in.
							
							- org.eclipse.ui 
							- org.eclipse.core.runtime
							- ilog.rules.studio.model
							- ilog.rules.studio.model.brl
							- ilog.rules.studio.model.dt
													
							The plug-in uses classes from RuleImporterCommon project that is packages as Common.jar.
							
							The plug-in can be generated using the "Export Wizard" available on the 
							overview tab of the plug-in configuration. The plug-in configuration can be opened using
							plugin.xml found in the com.ibm.rmt.importer.ui folder. Once the plug-in has been generated, 
							you install it by just copying the generated jar file in the “plugins” folder, under the ODM 
							install root. After installing the plug-in, (re)launch Rule Designer. You will now see additional 
							button in Eclipse to start the rules import.
							
							Once the plug-in is installed, you need a configuration file for performing the import. 
							The configuration file describes where to place the ODM rules that are created. The configuration file is 
							located in the config folder of the RuleImportCommon project.
							
Create loan-validation rules workspace
--------------------------------------
TBD

Starting the Import for Decision Center
---------------------------------------
To start the import, follow these steps:

Launch RuleGenerator Java class while specifying URL of the Decision Center instance, the user name and the password 
to connect to the Decision Center and location of the configuration file as command line parameter.
This will start the import process and insert the newly created rules in the proper location in the Decision Center. 
*The RuleGenerator.launch file in the RuleImporter project stores the run configuration that you could use to launch the RuleGenerator.*
**Note that you need to delete all the rules in the target rule package that may remain from a previous import before starting a new one.**
							
Starting the Import using Rule Designer Plug-in
-----------------------------------------------
To start the import, follow these steps:

Click the Import button, which will display a file chooser for the configuration file.
Select the configuration file located in the config folder of the RuleImportCommon project and click open.
This will start the import process and insert the newly created rules in the proper location.
**Note that you need to delete all the rules in the target rule package that may remain from a previous import before starting a new one.**

Configuration File
------------------
The configuration file specifies the properties needed to create rule artifacts such rule project, 
rule package and rule type. The file is located in config folder of the RuleImportCommon project.