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
 *******************************************************************************/
package org.eclipse.emf.ecp.explorereditorbridge.internal;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.model.VElement;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eugen Neufeld
 * 
 */
public class ECPControlContextImpl implements ECPControlContext {

	private final EObject modelElement;
	// private final ViewModelContext viewContext;

	private final ECPProject ecpProject;

	/**
	 * Constructor for the default implementation of the ECPControlContext.
	 * 
	 * @param modelElement the {@link EObject} which will be opened in the editor
	 * @param ecpProject the {@link ECPProject} to which the modelElement belongs
	 * @param shell the {@link Shell} to use for UI elements
	 */
	public ECPControlContextImpl(EObject modelElement, ECPProject ecpProject, Shell shell) {
		super();
		this.modelElement = modelElement;
		this.ecpProject = ecpProject;
		// viewContext = new ViewModelContextImpl(getView(), getModelElement());
	}

	/**
	 * Constructor for the default implementation of the ECPControlContext.
	 * 
	 * @param modelElement the {@link EObject} which will be opened in the editor
	 * @param ecpProject the {@link ECPProject} to which the modelElement belongs
	 * @param shell the {@link Shell} to use for UI elements
	 * @param viewContext the {@link ViewModelContext}
	 */
	public ECPControlContextImpl(EObject modelElement, ECPProject ecpProject, Shell shell, ViewModelContext viewContext) {
		super();
		this.modelElement = modelElement;
		this.ecpProject = ecpProject;
		// this.viewContext = viewContext;
	}

	/**
	 * Constructor for the default implementation of the ECPControlContext.
	 * 
	 * @param domainObject the {@link EObject} which will be opened in the editor
	 * @param ecpProject the {@link ECPProject} to which the modelElement belongs
	 * @param shell the {@link Shell} to use for UI elements
	 * @param view the view
	 */
	public ECPControlContextImpl(EObject domainObject, ECPProject ecpProject, Shell shell, VElement view) {
		super();
		modelElement = domainObject;
		this.ecpProject = ecpProject;
		// viewContext = new ViewModelContextImpl(view, getModelElement());
	}

	/** {@inheritDoc} */
	public EObject getModelElement() {
		return modelElement;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.ECPControlContext#getViewContext()
	 */
	public ViewModelContext getViewContext() {
		return null;
	}

}
