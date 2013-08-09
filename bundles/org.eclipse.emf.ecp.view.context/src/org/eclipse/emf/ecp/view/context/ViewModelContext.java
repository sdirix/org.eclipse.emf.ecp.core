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
package org.eclipse.emf.ecp.view.context;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.model.View;

// TODO: Auto-generated Javadoc
/**
 * The Interface ViewModelContext.
 * 
 * @author Eugen Neufeld
 */
public interface ViewModelContext {
	/**
	 * The listener interface for receiving modelChange events.
	 * The class that is interested in processing a modelChange
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>registerViewChangeListener</code> or <code>registerDomainChangeListener</code> method. When
	 * the modelChange event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 */
	public interface ModelChangeListener {

		/**
		 * Notify about a change.
		 * 
		 * @param notification the {@link ModelChangeNotification}
		 */
		void notifyChange(ModelChangeNotification notification);
	}

	/**
	 * Gets the view model.
	 * 
	 * @return the view model
	 */
	View getViewModel();

	/**
	 * Gets the domain model.
	 * 
	 * @return the domain model
	 */
	EObject getDomainModel();

	/**
	 * Register view change listener.
	 * 
	 * @param modelChangeListener the model change listener
	 */
	void registerViewChangeListener(ModelChangeListener modelChangeListener);

	/**
	 * Unregister view change listener.
	 * 
	 * @param modelChangeListener the model change listener
	 */
	void unregisterViewChangeListener(ModelChangeListener modelChangeListener);

	/**
	 * Register domain change listener.
	 * 
	 * @param modelChangeListener the model change listener
	 */
	void registerDomainChangeListener(ModelChangeListener modelChangeListener);

	/**
	 * Unregister domain change listener.
	 * 
	 * @param modelChangeListener the model change listener
	 */
	void unregisterDomainChangeListener(ModelChangeListener modelChangeListener);

	void dispose();
}
