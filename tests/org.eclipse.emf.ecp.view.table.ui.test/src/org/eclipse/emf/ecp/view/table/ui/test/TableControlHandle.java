/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.table.ui.test;

import org.eclipse.emf.ecp.view.model.TableColumn;
import org.eclipse.emf.ecp.view.model.TableControl;

/**
 * @author Jonas
 * 
 */
public class TableControlHandle {

	public TableControlHandle(TableControl tableControl) {
		this.tableControl = tableControl;
		// TODO Auto-generated constructor stub
	}

	public TableControl tableControl;
	public TableColumn tableColumn1;
	public TableColumn tableColumn2;

	/**
	 * @param tableColumn1
	 */
	public void addFirstTableColumn(TableColumn tableColumn1) {
		this.tableColumn1 = tableColumn1;
		tableControl.getColumns().add(tableColumn1);

	}

	/**
	 * @param tableColumn2
	 */
	public void addSecondTableColumn(TableColumn tableColumn2) {
		this.tableColumn2 = tableColumn2;
		tableControl.getColumns().add(tableColumn2);

	}

}
