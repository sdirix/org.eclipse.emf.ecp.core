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
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.common.dnd;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;

import org.eclipse.jface.viewers.Viewer;

/**
 * @author Eugen Neufeld
 * 
 */
public abstract class ECPDropAdapter extends EditingDomainViewerDropAdapter {

	public ECPDropAdapter() {
		this(null, null);
	}

	/**
	 * @param domain
	 * @param viewer
	 */
	public ECPDropAdapter(EditingDomain domain, Viewer viewer) {
		super(domain, viewer);
	}

	public void setEditingDomain(EditingDomain editingDomain) {
		domain = editingDomain;
	}

	public void setViewer(Viewer viewer) {
		this.viewer = viewer;
	}
}
