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
 ******************************************************************************/
package org.eclipse.emf.ecp.view.rule;

import java.util.Collections;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;

// TODO: Auto-generated Javadoc
/**
 * The Class RuleService.
 * 
 * @author Eugen Neufeld
 */
public class RuleService {

	/**
	 * Instantiates a new rule service.
	 * 
	 * @param view the view
	 * @param domainModel the domain model
	 */
	public RuleService(View view, EObject domainModel) {
		// FIXME
		// register content adapters, do stuff
	}

	/**
	 * Gets the involved {@link EObject EObjects}.
	 * 
	 * @param setting the setting
	 * @param newValue the new value
	 * @return the involved e object
	 */
	public Set<Renderable> getInvolvedEObject(Setting setting, Object newValue) {
		// FIXME do something usefull
		return Collections.emptySet();
	}

	/**
	 * Dispose.
	 */
	public void dispose() {
		// dispose stuff
	}
}
