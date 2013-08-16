/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.rule.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.model.View;

/**
 * @author Edgar
 * 
 */
public class TestViewModelContext implements ViewModelContext {

	private final View view;
	private final EObject domainModel;
	private EContentAdapter viewContentAdapter;
	private EContentAdapter domainContentAdapter;
	private final List<ModelChangeListener> domainChangeListeners = new ArrayList<ViewModelContext.ModelChangeListener>();
	private final List<ModelChangeListener> viewChangeListeners = new ArrayList<ViewModelContext.ModelChangeListener>();

	public TestViewModelContext(View view, EObject domainModel) {
		this.view = view;
		this.domainModel = domainModel;
	}

	public void unregisterViewChangeListener(ModelChangeListener modelChangeListener) {
		viewChangeListeners.remove(modelChangeListener);
		view.eAdapters().remove(viewContentAdapter);
	}

	public void unregisterDomainChangeListener(ModelChangeListener modelChangeListener) {
		domainChangeListeners.remove(modelChangeListener);
		domainModel.eAdapters().remove(domainContentAdapter);
	}

	public void registerViewChangeListener(ModelChangeListener modelChangeListener) {
		viewContentAdapter = new TestViewContentAdapter(viewChangeListeners);
		view.eAdapters().add(viewContentAdapter);
	}

	public void registerDomainChangeListener(ModelChangeListener modelChangeListener) {
		domainChangeListeners.add(modelChangeListener);
		domainContentAdapter = new TestDomainContentAdapter(domainChangeListeners);
		domainModel.eAdapters().add(domainContentAdapter);
	}

	public View getViewModel() {
		return view;
	}

	public EObject getDomainModel() {
		return domainModel;
	}

	public void dispose() {
		domainModel.eAdapters().remove(domainContentAdapter);
		domainModel.eAdapters().remove(viewContentAdapter);
	}
}
