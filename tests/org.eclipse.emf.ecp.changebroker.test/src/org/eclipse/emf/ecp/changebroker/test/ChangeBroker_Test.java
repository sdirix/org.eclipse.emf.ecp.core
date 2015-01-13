/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * jfaltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.changebroker.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecp.changebroker.internal.ChangeBrokerImpl;
import org.eclipse.emf.ecp.changebroker.spi.EMFObserver;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.BowlingPackage;
import org.eclipse.emf.emfstore.bowling.Matchup;
import org.eclipse.emf.emfstore.bowling.Tournament;
import org.eclipse.emf.emfstore.bowling.TournamentType;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jfaltermeier
 *
 */
public class ChangeBroker_Test {

	private ChangeBrokerImpl broker;
	private Tournament tournament;
	private ContentAdapterNotificationProvider provider;

	@Before
	public void before() {
		broker = new ChangeBrokerImpl();
		tournament = BowlingFactory.eINSTANCE.createTournament();
		provider = new ContentAdapterNotificationProvider(tournament);
		broker.addNotificationProvider(provider);
	}

	@Test
	public void testNotify() throws InterruptedException {
		// setup
		final CountDownLatch latch = new CountDownLatch(1);
		final Matchup matchup = BowlingFactory.eINSTANCE.createMatchup();
		broker.subscribe(new EMFObserver() {

			@Override
			public void handleNotification(Notification notification) {
				assertSame(Notification.ADD, notification.getEventType());
				assertSame(BowlingPackage.eINSTANCE.getTournament_Matchups(), notification.getFeature());
				assertSame(matchup, notification.getNewValue());
				latch.countDown();
			}
		});

		// act
		tournament.getMatchups().add(matchup);

		// assert
		assertTrue("Notify was not called", latch.await(1, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testMultipleReceivers() throws InterruptedException {
		// setup
		final CountDownLatch latch1 = new CountDownLatch(1);
		final CountDownLatch latch2 = new CountDownLatch(1);
		final Matchup matchup = BowlingFactory.eINSTANCE.createMatchup();
		broker.subscribe(new EMFObserver() {

			@Override
			public void handleNotification(Notification notification) {
				latch1.countDown();
			}
		});
		broker.subscribe(new EMFObserver() {

			@Override
			public void handleNotification(Notification notification) {
				latch2.countDown();
			}
		});

		// act
		tournament.getMatchups().add(matchup);

		// assert
		assertTrue("Notify was not called", latch1.await(1, TimeUnit.MILLISECONDS));
		assertTrue("Notify was not called", latch2.await(1, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testRemoveProvider() throws InterruptedException {
		// setup
		final CountDownLatch latch = new CountDownLatch(1);
		final Matchup matchup = BowlingFactory.eINSTANCE.createMatchup();
		broker.subscribe(new EMFObserver() {

			@Override
			public void handleNotification(Notification notification) {
				latch.countDown();
			}
		});

		// act
		broker.removeNotificationProvider(provider);
		tournament.getMatchups().add(matchup);

		// assert
		assertFalse("Notify was called although it shouln't be called", latch.await(1, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testAddSameProviderTwiceAndRemove() throws InterruptedException {
		// setup
		final CountDownLatch latch = new CountDownLatch(1);
		final Matchup matchup = BowlingFactory.eINSTANCE.createMatchup();
		broker.subscribe(new EMFObserver() {

			@Override
			public void handleNotification(Notification notification) {
				latch.countDown();
			}
		});
		broker.addNotificationProvider(provider);
		broker.removeNotificationProvider(provider);

		// act
		tournament.getMatchups().add(matchup);

		// assert
		assertFalse("Notify was called although it shouln't be called", latch.await(1, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testRemoveUnregisteredProvider() throws InterruptedException {
		// setup
		final CountDownLatch latch = new CountDownLatch(1);
		final Matchup matchup = BowlingFactory.eINSTANCE.createMatchup();
		broker.subscribe(new EMFObserver() {

			@Override
			public void handleNotification(Notification notification) {
				latch.countDown();
			}
		});

		// act

		broker.removeNotificationProvider(new ContentAdapterNotificationProvider(tournament));
		tournament.getMatchups().add(matchup);

		// assert
		assertTrue("Notify was not called", latch.await(1, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testEClassStrategy() throws InterruptedException {
		// setup
		final CountDownLatch latch1 = new CountDownLatch(1);
		final CountDownLatch latch2 = new CountDownLatch(1);
		final Matchup matchup = BowlingFactory.eINSTANCE.createMatchup();
		tournament.getMatchups().add(matchup);
		broker.subscribeToEClass(new EMFObserver() {

			@Override
			public void handleNotification(Notification notification) {
				if (notification.getNotifier() == tournament) {
					latch1.countDown();
				} else if (notification.getNotifier() == matchup) {
					latch2.countDown();
				}
			}
		}, BowlingPackage.eINSTANCE.getTournament());

		// act&assert
		tournament.setType(TournamentType.PRO);
		assertTrue(latch1.await(1, TimeUnit.MILLISECONDS));

		// act&assert
		matchup.setNrSpectators(new BigInteger("1"));
		assertFalse("Notify was called although it shouln't be called", latch2.await(1, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testContainingEClassStrategy() throws InterruptedException {
		// setup
		final CountDownLatch latch1 = new CountDownLatch(1);
		final CountDownLatch latch2 = new CountDownLatch(1);
		final Matchup matchup = BowlingFactory.eINSTANCE.createMatchup();
		tournament.getMatchups().add(matchup);
		broker.subscribeToTree(new EMFObserver() {

			@Override
			public void handleNotification(Notification notification) {
				if (notification.getNotifier() == tournament) {
					latch1.countDown();
				} else if (notification.getNotifier() == matchup) {
					latch2.countDown();
				}
			}
		}, BowlingPackage.eINSTANCE.getTournament());

		// act&assert
		tournament.setType(TournamentType.PRO);
		assertTrue("Notify was not called", latch1.await(1, TimeUnit.MILLISECONDS));

		// act&assert
		matchup.setNrSpectators(new BigInteger("1"));
		assertTrue("Notify was not called", latch2.await(1, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testIgnoreNotifactionsByEMFObserver() throws InterruptedException {
		// setup
		final CountDownLatch latch1 = new CountDownLatch(1);
		final CountDownLatch latch2 = new CountDownLatch(1);
		final Matchup matchup = BowlingFactory.eINSTANCE.createMatchup();
		tournament.getMatchups().add(matchup);
		broker.subscribeToEClass(new EMFObserver() {

			@Override
			public void handleNotification(Notification notification) {
				matchup.setNrSpectators(new BigInteger("1"));
				latch1.countDown();

			}
		}, BowlingPackage.eINSTANCE.getTournament());
		broker.subscribeToEClass(new EMFObserver() {

			@Override
			public void handleNotification(Notification notification) {
				latch2.countDown();

			}
		}, BowlingPackage.eINSTANCE.getMatchup());

		// act
		tournament.setType(TournamentType.PRO);

		// assert
		assertTrue("Notify was not called", latch1.await(1, TimeUnit.MILLISECONDS));
		assertFalse("Notify was called although it shouln't be called", latch2.await(1, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testFeatureStrategy() throws InterruptedException {
		// setup
		final CountDownLatch latch1 = new CountDownLatch(1);
		final CountDownLatch latch2 = new CountDownLatch(1);
		final Matchup matchup = BowlingFactory.eINSTANCE.createMatchup();

		broker.subscribeToFeature(new EMFObserver() {

			@Override
			public void handleNotification(Notification notification) {
				if (notification.getFeature() == BowlingPackage.eINSTANCE.getTournament_Type()) {
					latch1.countDown();
				} else if (notification.getFeature() == BowlingPackage.eINSTANCE.getTournament_Matchups()) {
					latch2.countDown();
				}
			}
		}, BowlingPackage.eINSTANCE.getTournament_Type());

		// act&assert
		tournament.setType(TournamentType.PRO);
		assertTrue(latch1.await(1, TimeUnit.MILLISECONDS));

		// act&assert
		tournament.getMatchups().add(matchup);
		assertFalse("Notify was called although it shouln't be called", latch2.await(1, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testRemoveObserverNoStrategy() throws InterruptedException {
		// setup
		final CountDownLatch latch1 = new CountDownLatch(1);
		final Matchup matchup = BowlingFactory.eINSTANCE.createMatchup();
		final EMFObserver receiver = new EMFObserver() {

			@Override
			public void handleNotification(Notification notification) {
				latch1.countDown();
			}
		};
		broker.subscribe(receiver);

		// act
		broker.unsubsribe(receiver);
		tournament.getMatchups().add(matchup);

		// assert
		assertFalse("Notify was called although it shouln't be called", latch1.await(1, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testRemoveObserverEClassStrategy() throws InterruptedException {
		// setup
		final CountDownLatch latch1 = new CountDownLatch(1);
		final Matchup matchup = BowlingFactory.eINSTANCE.createMatchup();
		final EMFObserver receiver = new EMFObserver() {

			@Override
			public void handleNotification(Notification notification) {
				latch1.countDown();
			}
		};
		broker.subscribeToEClass(receiver, BowlingPackage.eINSTANCE.getTournament());

		// act
		broker.unsubsribe(receiver);
		tournament.getMatchups().add(matchup);

		// assert
		assertFalse("Notify was called although it shouln't be called", latch1.await(1, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testRemoveObserverContainingEClassStrategy() throws InterruptedException {
		// setup
		final CountDownLatch latch1 = new CountDownLatch(1);
		final Matchup matchup = BowlingFactory.eINSTANCE.createMatchup();
		final EMFObserver receiver = new EMFObserver() {

			@Override
			public void handleNotification(Notification notification) {
				latch1.countDown();
			}
		};
		broker.subscribeToTree(receiver, BowlingPackage.eINSTANCE.getTournament());

		// act
		broker.unsubsribe(receiver);
		tournament.getMatchups().add(matchup);

		// assert
		assertFalse("Notify was called although it shouln't be called", latch1.await(1, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testRemoveObserverFeatureStrategy() throws InterruptedException {
		// setup
		final CountDownLatch latch1 = new CountDownLatch(1);
		final Matchup matchup = BowlingFactory.eINSTANCE.createMatchup();
		final EMFObserver receiver = new EMFObserver() {

			@Override
			public void handleNotification(Notification notification) {
				latch1.countDown();
			}
		};
		broker.subscribeToFeature(receiver, BowlingPackage.eINSTANCE.getTournament_Matchups());

		// act
		broker.unsubsribe(receiver);
		tournament.getMatchups().add(matchup);

		// assert
		assertFalse("Notify was called although it shouln't be called", latch1.await(1, TimeUnit.MILLISECONDS));
	}
}
