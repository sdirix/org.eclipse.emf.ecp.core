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
		setTableControl(tableControl);
	}

	private TableControl tableControl;
	private TableColumn tableColumn1;
	private TableColumn tableColumn2;

	/**
	 * @param tableColumn1
	 */
	public void addFirstTableColumn(TableColumn tableColumn1) {
		setTableColumn1(tableColumn1);
		getTableControl().getColumns().add(tableColumn1);

	}

	/**
	 * @param tableColumn2
	 */
	public void addSecondTableColumn(TableColumn tableColumn2) {
		setTableColumn2(tableColumn2);
		getTableControl().getColumns().add(tableColumn2);

	}

	/**
	 * @return the tableControl
	 */
	public TableControl getTableControl() {
		return tableControl;
	}

	/**
	 * @param tableControl the tableControl to set
	 */
	public void setTableControl(TableControl tableControl) {
		this.tableControl = tableControl;
	}

	/**
	 * @return the tableColumn1
	 */
	public TableColumn getTableColumn1() {
		return tableColumn1;
	}

	/**
	 * @param tableColumn1 the tableColumn1 to set
	 */
	public void setTableColumn1(TableColumn tableColumn1) {
		this.tableColumn1 = tableColumn1;
	}

	/**
	 * @return the tableColumn2
	 */
	public TableColumn getTableColumn2() {
		return tableColumn2;
	}

	/**
	 * @param tableColumn2 the tableColumn2 to set
	 */
	public void setTableColumn2(TableColumn tableColumn2) {
		this.tableColumn2 = tableColumn2;
	}

}
