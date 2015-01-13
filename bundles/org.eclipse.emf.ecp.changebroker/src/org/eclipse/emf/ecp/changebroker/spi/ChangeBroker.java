/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jonas - initial API and implementation
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.changebroker.spi;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * A Change Broker maintains sets of {@link EMFObserver EMFObservers}. Based on incoming notifications it forwards the
 * notification to the receivers based on strategies. All notifications that arrive while a receiver is notified will be
 * ignored.
 *
 * @author Jonas
 * @author jfaltermeier
 *
 */
public interface ChangeBroker {

	/**
	 * Adds an {@link EMFObserver} that will receive all notifications. Does nothing if already registered.
	 *
	 * @param observer the observer
	 */
	void subscribe(EMFObserver observer);

	/**
	 * Adds an {@link EMFObserver} that will receive notifications of notifiers with the given EClass. Does nothing if
	 * already registered.
	 *
	 * @param observer the observer
	 * @param eClass the required EClass of the notifier
	 */
	void subscribeToEClass(EMFObserver observer, EClass eClass);

	/**
	 * Adds an {@link EMFObserver} that will receive notifications of notifiers with the given EClass or which are
	 * contained (either direct or indirect) in an EObject of the given EClass. Does nothing if
	 * already registered.
	 *
	 * @param observer the observer
	 * @param eClass the required EClass of the notifier or of one of its parents
	 */
	void subscribeToTree(EMFObserver observer, EClass eClass);

	/**
	 * Adds an {@link EMFObserver} that will receive notifications when the given EStructuralFeautre is effected. Does
	 * nothing if
	 * already registered.
	 *
	 * @param observer the observer
	 * @param feature the feature to receive changes for
	 */
	void subscribeToFeature(EMFObserver observer, EStructuralFeature feature);

	/**
	 * Removes a {@link EMFObserver}. Does nothing if receiver is not registered.
	 *
	 * @param observer the receiver
	 */
	void unsubsribe(EMFObserver observer);

	void stopNotification();

	void continueNotification();

}
