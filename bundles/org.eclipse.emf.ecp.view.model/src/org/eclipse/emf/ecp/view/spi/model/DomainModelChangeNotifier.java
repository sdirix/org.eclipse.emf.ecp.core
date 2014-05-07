/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Alexandra Buzila - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.model;

import org.eclipse.emf.common.notify.Notifier;

/**
 * @author Alexandra Buzila
 * 
 */
public interface DomainModelChangeNotifier {

	/**
	 * The listener interface for receiving modelChange events.
	 * The class that is interested in processing a modelChange
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>registerViewChangeListener</code> or <code>registerDomainChangeListener</code> method. When
	 * the modelChange event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @since 1.2
	 * 
	 */
	public interface DomainModelChangeListener {

		/**
		 * Notify about a change.
		 * 
		 * @param notification the {@link ModelChangeNotification}
		 */
		void notifyChange(ModelChangeNotification notification);

		/**
		 * @param notifier the notifier
		 */
		void notifyAdd(Notifier notifier);

		/**
		 * @param notifier the notifier
		 */
		void notifyRemove(Notifier notifier);
	}

	/**
	 * Register domain change listener.
	 * 
	 * @param modelChangeListener the model change listener
	 */
	void registerDomainChangeListener(DomainModelChangeListener modelChangeListener);

	/**
	 * Unregister domain change listener.
	 * 
	 * @param modelChangeListener the model change listener
	 */
	void unregisterDomainChangeListener(DomainModelChangeListener modelChangeListener);
}
