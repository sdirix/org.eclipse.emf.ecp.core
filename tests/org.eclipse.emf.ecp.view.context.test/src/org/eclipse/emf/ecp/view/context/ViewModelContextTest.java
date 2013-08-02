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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.model.ViewPackage;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.BowlingPackage;
import org.eclipse.emf.emfstore.bowling.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class ViewModelContextTest.
 * 
 * @author Eugen Neufeld
 */
public class ViewModelContextTest {

	private Player player;
	private View view;
	private ViewModelContextImpl viewModelContext;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		player = BowlingFactory.eINSTANCE.createPlayer();
		view = ViewFactory.eINSTANCE.createView();
		numPlayerAdapters = player.eAdapters().size();
		numViewAdapters = view.eAdapters().size();
		viewModelContext = new ViewModelContextImpl(view, player);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.eclipse.emf.ecp.view.context.ViewModelContextImpl#getViewModel()}.
	 */
	@Test
	public void testGetViewModel() {
		// TODO return copy?
		assertEquals(view, viewModelContext.getViewModel());
	}

	/**
	 * Test method for {@link org.eclipse.emf.ecp.view.context.ViewModelContextImpl#getDomainModel()}.
	 */
	@Test
	public void testGetDomainModel() {
		// TODO return copy?
		assertEquals(player, viewModelContext.getDomainModel());
	}

	/**
	 * Test method for {@link org.eclipse.emf.ecp.view.context.ViewModelContextImpl#dispose()}.
	 */
	@Test()
	public void testDisposeAdapters() {
		viewModelContext.dispose();
		// assert adapters not there anymore
		assertEquals(numPlayerAdapters, player.eAdapters().size());
		assertEquals(numViewAdapters, view.eAdapters().size());
	}

	/**
	 * Test dispose get domain model.
	 */
	@Test(expected = IllegalStateException.class)
	public void testDisposeGetDomainModel() {
		viewModelContext.dispose();
		viewModelContext.getDomainModel();
		fail("No exception throw!");
	}

	/**
	 * Test dispose get view model.
	 */
	@Test(expected = IllegalStateException.class)
	public void testDisposeGetViewModel() {
		viewModelContext.dispose();
		viewModelContext.getViewModel();
		fail("No exception throw!");
	}

	/**
	 * Test dispose register domain listner.
	 */
	@Test(expected = IllegalStateException.class)
	public void testDisposeRegisterDomainListner() {
		viewModelContext.dispose();
		viewModelContext.registerDomainChangeListener(new ModelChangeListener() {

			public void notifyChange(ModelChangeNotification notification) {
				// TODO Auto-generated method stub

			}
		});
		fail("No exception throw!");
	}

	/**
	 * Test dispose register view listner.
	 */
	@Test(expected = IllegalStateException.class)
	public void testDisposeRegisterViewListner() {
		viewModelContext.dispose();
		viewModelContext.registerViewChangeListener(new ModelChangeListener() {

			public void notifyChange(ModelChangeNotification notification) {
				// TODO Auto-generated method stub

			}
		});
		fail("No exception throw!");
	}

	/**
	 * Test dispose un register domain listner.
	 */
	@Test(expected = IllegalStateException.class)
	public void testDisposeUnRegisterDomainListner() {
		viewModelContext.dispose();
		viewModelContext.unregisterDomainChangeListener(new ModelChangeListener() {

			public void notifyChange(ModelChangeNotification notification) {
				// TODO Auto-generated method stub

			}
		});
		fail("No exception throw!");
	}

	/**
	 * Test dispose un register view listner.
	 */
	@Test(expected = IllegalStateException.class)
	public void testDisposeUnRegisterViewListner() {
		viewModelContext.dispose();
		viewModelContext.unregisterViewChangeListener(new ModelChangeListener() {

			public void notifyChange(ModelChangeNotification notification) {
				// TODO Auto-generated method stub

			}
		});
		fail("No exception throw!");
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.view.context.ViewModelContextImpl#registerViewChangeListener(org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener)}
	 * .
	 */
	private boolean correctNotificationArrived;
	private int numPlayerAdapters;
	private int numViewAdapters;

	/**
	 * Test register view change listener.
	 */
	@Test
	public void testRegisterViewChangeListener() {
		viewModelContext.registerViewChangeListener(new ModelChangeListener() {

			public void notifyChange(ModelChangeNotification notification) {
				correctNotificationArrived = notification.getNotifier() == view
					&& notification.getStructuralFeature() == ViewPackage.eINSTANCE.getAbstractCategorization_Name();
			}
		});
		view.setName("Test");
		assertTrue(correctNotificationArrived);
	}

	/**
	 * Test register view change listener null.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterViewChangeListenerNull() {
		viewModelContext.registerViewChangeListener(null);
		fail("No exception thrown.");
	}

	/**
	 * Test register view change listener change on domain.
	 */
	@Test
	public void testRegisterViewChangeListenerChangeOnDomain() {
		viewModelContext.registerViewChangeListener(new ModelChangeListener() {

			public void notifyChange(ModelChangeNotification notification) {
				correctNotificationArrived = true;
			}
		});
		player.setName("Test");
		assertFalse(correctNotificationArrived);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.view.context.ViewModelContextImpl#unregisterViewChangeListener(org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener)}
	 * .
	 */
	@Test
	public void testUnregisterViewChangeListener() {
		final ModelChangeListener modelChangeListener = new ModelChangeListener() {

			public void notifyChange(ModelChangeNotification notification) {
				correctNotificationArrived = true;
			}
		};
		viewModelContext.registerViewChangeListener(modelChangeListener);
		viewModelContext.unregisterViewChangeListener(modelChangeListener);
		view.setName("Test");
		assertFalse(correctNotificationArrived);
	}

	/**
	 * Test unregister view change listener change on domain.
	 */
	@Test
	public void testUnregisterViewChangeListenerChangeOnDomain() {
		final ModelChangeListener modelChangeListener = new ModelChangeListener() {

			public void notifyChange(ModelChangeNotification notification) {
				correctNotificationArrived = true;
			}
		};
		viewModelContext.registerViewChangeListener(modelChangeListener);
		viewModelContext.unregisterViewChangeListener(modelChangeListener);
		player.setName("Test");
		assertFalse(correctNotificationArrived);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.view.context.ViewModelContextImpl#registerDomainChangeListener(org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener)}
	 * .
	 */
	@Test
	public void testRegisterDomainChangeListener() {
		viewModelContext.registerDomainChangeListener(new ModelChangeListener() {

			public void notifyChange(ModelChangeNotification notification) {
				correctNotificationArrived = notification.getNotifier() == player
					&& notification.getStructuralFeature() == BowlingPackage.eINSTANCE.getPlayer_Name();
			}
		});
		player.setName("Test");
		assertTrue(correctNotificationArrived);
	}

	/**
	 * Test register domain change listener null.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterDomainChangeListenerNull() {
		viewModelContext.registerDomainChangeListener(null);
		fail("No exception thrown.");
	}

	/**
	 * Test register domain change listener change on view.
	 */
	@Test
	public void testRegisterDomainChangeListenerChangeOnView() {
		viewModelContext.registerDomainChangeListener(new ModelChangeListener() {

			public void notifyChange(ModelChangeNotification notification) {
				correctNotificationArrived = true;
			}
		});
		view.setName("Test");
		assertFalse(correctNotificationArrived);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.emf.ecp.view.context.ViewModelContextImpl#unregisterDomainChangeListener(org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener)}
	 * .
	 */
	@Test
	public void testUnregisterDomainChangeListener() {
		final ModelChangeListener modelChangeListener = new ModelChangeListener() {

			public void notifyChange(ModelChangeNotification notification) {
				correctNotificationArrived = true;
			}
		};
		viewModelContext.registerDomainChangeListener(modelChangeListener);
		viewModelContext.unregisterDomainChangeListener(modelChangeListener);
		player.setName("Test");
		assertFalse(correctNotificationArrived);
	}

	/**
	 * Test unregister domain change listener change on view.
	 */
	@Test
	public void testUnregisterDomainChangeListenerChangeOnView() {
		final ModelChangeListener modelChangeListener = new ModelChangeListener() {

			public void notifyChange(ModelChangeNotification notification) {
				correctNotificationArrived = true;
			}
		};
		viewModelContext.registerDomainChangeListener(modelChangeListener);
		viewModelContext.unregisterDomainChangeListener(modelChangeListener);
		view.setName("Test");
		assertFalse(correctNotificationArrived);
	}

}
