/* 
 * Copyright (C) 2016 IBM Corporation
 */
package com.ibm.rmt.importer.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.ibm.rmt.importer.ImportBatcher;

/**
 * This class behavior is connected to the import button and menu item in Rule
 * Designer to select the configuration file to import.
 */
public class ImportAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	public ImportAction() {
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		Shell shell = window.getShell();
		shell.open();
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);

		String[] filterNames = new String[] { "Import Config Files",
				"All Files (*.*)" };
		String[] filterExtensions = new String[] { "*.properties", "*.*" };
		String filterPath = "C:\\";

		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(filterPath);

		String selectedFile = dialog.open();
		ImportBatcher batcher = new ImportBatcher();
		batcher.startImport(selectedFile);
	}

	/**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}