/*******************************************************************************
 * Copyright (c) 2011-2016 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.rulerepository.tooling;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emfforms.internal.editor.toolbaractions.LoadEcoreAction;
import org.eclipse.emfforms.spi.editor.GenericEditor;
import org.eclipse.jface.action.Action;

/**
 * RuleRepositoryEditor.
 * 
 * @author Eugen Neufeld
 *
 */
@SuppressWarnings("restriction")
public class RuleRepositoryEditor extends GenericEditor {

	/**
	 * Returns the toolbar actions for this editor.
	 *
	 * @return A list of actions to show in the Editor's Toolbar
	 */
	@Override
	protected List<Action> getToolbarActions() {
		final List<Action> result = new LinkedList<Action>();

		result.add(new LoadEcoreAction(getResourceSet(), "Load ViewModel")); //$NON-NLS-1$

		// result.addAll(readToolbarActions());
		return result;
	}
}
