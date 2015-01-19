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
 * <p>
 * A Change Broker maintains sets of {@link EMFObserver observers}. Based on incoming notifications it forwards the
 * notification to the observers based on strategies.
 * </p>
 * <p>
 * There are two kinds of observers: regular {@link EMFObserver EMFObservers} and {@link ReadOnlyEMFObserver
 * ReadOnlyEMFObservers}.
 * </p>
 * <p>
 * EMFObservers may change the EMF model and therefore trigger further notifications. To prevent circular updates
 * between EMFObservers, notifications that arrive while the
 * {@link EMFObserver#handleNotification(org.eclipse.emf.common.notify.Notification) handleNotification} method is
 * called, will not be forwarded to non ReadOnlyEMFObservers.
 * </p>
 * <p>
 * ReadOnlyEMFObservers are not allowed to change the EMF model. They will receive all notifications at any time.
 * </p>
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
	 * Removes an observer. Does nothing if receiver is not registered.
	 *
	 * @param observer the receiver
	 */
	void unsubsribe(EMFObserver observer);

	/**
	 * Stops notifying all {@link EMFObserver EMFObservers}. {@link ReadOnlyEMFObserver ReadOnlyEMFObservers} will still
	 * be notified.
	 */
	void stopNotification();

	/**
	 * Notifying the {@link EMFObserver EMFObservers} is started again.
	 */
	void continueNotification();

}
