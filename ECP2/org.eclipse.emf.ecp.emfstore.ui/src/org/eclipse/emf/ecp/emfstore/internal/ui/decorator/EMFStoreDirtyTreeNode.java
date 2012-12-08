/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.emfstore.internal.ui.decorator;

/**
 * @author Tobias Verhoeven
 */
public class EMFStoreDirtyTreeNode {

	private int changeCount;
	private boolean childChanges;

	public EMFStoreDirtyTreeNode(int changeCount, boolean childChanges) {
		this.changeCount = changeCount;
		this.childChanges = childChanges;
	}

	public boolean shouldDisplayDirtyIndicator() {
		return childChanges || changeCount > 0;
	}

	public int getChangeCount() {
		return changeCount;
	}

	public void setChangeCount(int changeCount) {
		this.changeCount = changeCount;
	}

	public boolean isChildChanges() {
		return childChanges;
	}

	public void setChildChanges(boolean childChanges) {
		this.childChanges = childChanges;
	}
}