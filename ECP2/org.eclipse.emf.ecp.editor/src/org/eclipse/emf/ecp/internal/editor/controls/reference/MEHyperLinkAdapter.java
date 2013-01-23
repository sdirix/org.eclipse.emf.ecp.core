/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
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

package org.eclipse.emf.ecp.internal.editor.controls.reference;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.EditModelElementContext;

import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;

/**
 * A {@link HyperlinkAdapter} to the model elements.
 * 
 * @author helming
 * @author Eugen Neufeld
 */
public class MEHyperLinkAdapter extends HyperlinkAdapter {

	private EObject target;

	private EditModelElementContext modelContext;

	/**
	 * Default constructor.
	 * 
	 * @param target
	 *            the target of the model link
	 * @param modelContext
	 *            the {@link EditModelElementContext}
	 */
	public MEHyperLinkAdapter(EObject target, EditModelElementContext modelContext) {
		super();
		this.target = target;
		this.modelContext = modelContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void linkActivated(HyperlinkEvent event) {
		modelContext.openEditor(target, "org.eclipse.emf.ecp.editor");
		super.linkActivated(event);
	}
}
