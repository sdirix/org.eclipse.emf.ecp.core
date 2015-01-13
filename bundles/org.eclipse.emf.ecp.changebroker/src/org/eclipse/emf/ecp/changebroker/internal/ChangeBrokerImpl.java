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
 * Johannes Faltermeier - - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.changebroker.internal;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.changebroker.spi.ChangeBroker;
import org.eclipse.emf.ecp.changebroker.spi.EMFObserver;
import org.eclipse.emf.ecp.changebroker.spi.NotificationProvider;
import org.eclipse.emf.ecp.changebroker.spi.NotificationReceiver;

/**
 *
 * Implementation of a {@link ChangeBroker}.
 * Registers itself as a {@link NotificationReceiver} at all {@link NotificationProvider providers}. Once it
 * gets notified it forwards the notification to all of the brokers notification receivers, as applicable.
 *
 * @author Jonas
 * @author jfaltermeier
 *
 */
public class ChangeBrokerImpl implements ChangeBroker, NotificationReceiver {

	private final Set<NotificationProvider> providers = new CopyOnWriteArraySet<NotificationProvider>();
	private boolean stopNotification;
	private boolean notificationRunning;

	private final NoStrategy noStrategy = new NoStrategy();
	private final EClassStrategy eClassStrategy = new EClassStrategy();
	private final ContainingEClassStrategy containingEClassStrategy = new ContainingEClassStrategy();
	private final FeatureStrategy featureStrategy = new FeatureStrategy();
	private final Strategy[] strategies;

	/**
	 * Default constructor.
	 */
	public ChangeBrokerImpl() {
		strategies = new Strategy[] { noStrategy, eClassStrategy, containingEClassStrategy, featureStrategy };
	}

	/**
	 * Adds a notification provider.
	 *
	 * @param provider the provider
	 */
	public void addNotificationProvider(NotificationProvider provider) {
		if (providers.add(provider)) {
			provider.addReceiver(this);
		}
	}

	/**
	 * Removes a notification provider.
	 *
	 * @param provider the provider
	 */
	public void removeNotificationProvider(NotificationProvider provider) {
		if (providers.remove(provider)) {
			provider.removeReceiver(this);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.changebroker.spi.EMFObserver#handleNotification(org.eclipse.emf.common.notify.Notification)
	 */
	@Override
	public void notify(Notification notification) {
		if (stopNotification) {
			return;
		}
		if (notificationRunning) {
			return;
		}
		notificationRunning = true;
		final Set<EMFObserver> receivers = new LinkedHashSet<EMFObserver>();
		for (final Strategy strategy : strategies) {
			receivers.addAll(strategy.getObservers(notification));
		}
		for (final EMFObserver receiver : receivers) {
			receiver.handleNotification(notification);
		}
		notificationRunning = false;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.changebroker.spi.ChangeBroker#subscribe(org.eclipse.emf.ecp.changebroker.spi.EMFObserver)
	 */
	@Override
	public void subscribe(EMFObserver receiver) {
		noStrategy.register(receiver);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.changebroker.spi.ChangeBroker#subscribeToEClass(org.eclipse.emf.ecp.changebroker.spi.EMFObserver,
	 *      org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public void subscribeToEClass(EMFObserver observer, EClass eClass) {
		eClassStrategy.register(eClass, observer);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.changebroker.spi.ChangeBroker#subscribeToTree(org.eclipse.emf.ecp.changebroker.spi.EMFObserver,
	 *      org.eclipse.emf.ecore.EClass)
	 */
	@Override
	public void subscribeToTree(EMFObserver observer, EClass eClass) {
		containingEClassStrategy.register(eClass, observer);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.changebroker.spi.ChangeBroker#subscribeToFeature(org.eclipse.emf.ecp.changebroker.spi.EMFObserver,
	 *      org.eclipse.emf.ecore.EStructuralFeature)
	 */
	@Override
	public void subscribeToFeature(EMFObserver observer, EStructuralFeature feature) {
		featureStrategy.register(feature, observer);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.changebroker.spi.ChangeBroker#unsubsribe(org.eclipse.emf.ecp.changebroker.spi.EMFObserver)
	 */
	@Override
	public void unsubsribe(EMFObserver receiver) {
		for (final Strategy strategy : strategies) {
			strategy.deregister(receiver);
		}
	}

	/**
	 * For testing purposes.
	 *
	 * @return all registered providers
	 */
	public Set<NotificationProvider> getNotificationProviders() {
		return new LinkedHashSet<NotificationProvider>(providers);
	}

	public Set<EMFObserver> getRegisteredObservers() {
		final LinkedHashSet<EMFObserver> hashSet = new LinkedHashSet<EMFObserver>();
		for (final Strategy strategy : strategies) {
			hashSet.addAll(strategy.getAllObservers());
		}
		return hashSet;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.changebroker.spi.ChangeBroker#stopNotification()
	 */
	@Override
	public void stopNotification() {
		stopNotification = true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.changebroker.spi.ChangeBroker#continueNotification()
	 */
	@Override
	public void continueNotification() {
		stopNotification = false;

	}

}
