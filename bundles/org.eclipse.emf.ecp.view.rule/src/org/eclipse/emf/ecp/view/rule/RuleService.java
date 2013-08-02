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

import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.context.AbstractViewService;
import org.eclipse.emf.ecp.view.context.ModelChangeNotification;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener;
import org.eclipse.emf.ecp.view.model.Renderable;

/**
 * The Class RuleService.
 * 
 * @author Eugen Neufeld
 */
public class RuleService extends AbstractViewService {

	private ViewModelContext context;
	private ModelChangeListener domainChangeListener;
	private ModelChangeListener viewChangeListener;

	/**
	 * Instantiates a new rule service.
	 */
	public RuleService() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.AbstractViewService#instantiate(org.eclipse.emf.ecp.view.context.ViewModelContext)
	 */
	@Override
	public void instantiate(ViewModelContext context) {
		this.context = context;
		domainChangeListener = new ModelChangeListener() {

			public void notifyChange(ModelChangeNotification notification) {
				// TODO Auto-generated method stub

			}
		};
		context.registerDomainChangeListener(domainChangeListener);
		viewChangeListener = new ModelChangeListener() {

			public void notifyChange(ModelChangeNotification notification) {
				// TODO Auto-generated method stub

			}
		};
		context.registerViewChangeListener(viewChangeListener);
	}

	/**
	 * Gets the involved {@link org.eclipse.emf.ecore.EObject EObjects}.
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
	@Override
	public void dispose() {
		// dispose stuff
		context.unregisterDomainChangeListener(domainChangeListener);
		context.unregisterViewChangeListener(viewChangeListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener#notifyChange(org.eclipse.emf.ecp.view.context.ModelChangeNotification)
	 */
	public void notifyChange(ModelChangeNotification notification) {
		// TODO Auto-generated method stub

	}

}
