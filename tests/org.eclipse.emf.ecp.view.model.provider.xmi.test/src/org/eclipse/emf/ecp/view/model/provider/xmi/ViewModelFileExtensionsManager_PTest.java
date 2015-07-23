/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jonas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.model.provider.xmi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.view.model.provider.xmi.ViewModelFileExtensionsManager.ExtensionDescription;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.model.VViewModelLoadingProperties;
import org.eclipse.emf.ecp.view.spi.provider.ViewProviderHelper;
import org.eclipse.emf.emfstore.bowling.BowlingPackage;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jonas
 *
 */
public class ViewModelFileExtensionsManager_PTest {

	private static final String FILEPATH = "viewmodel.view";
	private static final String FILEPATH2 = "viewmodel2.view";

	private static final String VIEWNAME = "the view name";
	private ViewModelFileExtensionsManager manager;
	private final EClass eClass1 = BowlingPackage.eINSTANCE.getLeague();

	private static URI uri(String filepath) {
		return URI.createPlatformPluginURI("org.eclipse.emf.ecp.view.model.provider.xmi"
			+ "/" + filepath, false);
	}

	@Before
	public void init() throws IOException {
		ViewModelFileExtensionsManager.dispose();
		manager = ViewModelFileExtensionsManager.getInstance();
	}

	@Test
	public void testGetExtensionURIs() {
		final Map<URI, ExtensionDescription> extensionURIS = ViewModelFileExtensionsManager.getExtensionURIS();
		assertEquals(2, extensionURIS.size());
		final Set<URI> keySet = extensionURIS.keySet();
		assertTrue(keySet.contains(uri(FILEPATH)));
		assertTrue(keySet.contains(uri(FILEPATH2)));

	}

	@Test
	public void testHasViewModelFor() {
		final EObject eObject = EcoreUtil.create(eClass1);
		assertTrue(manager.hasViewModelFor(eObject, null));
	}

	@Test
	public void testCreateViewModel() {
		final EObject eObject = EcoreUtil.create(eClass1);
		final VView view = manager.createView(eObject, null);
		assertNull(view.getLoadingProperties());
		assertEquals(VIEWNAME, view.getName());
	}

	@Test
	public void testCreateViewModel2() {
		final EObject eObject = EcoreUtil.create(eClass1);
		final VViewModelLoadingProperties properties = VViewFactory.eINSTANCE.createViewModelLoadingProperties();
		properties.addInheritableProperty("key", "value");
		final VView view = manager.createView(eObject, properties);
		assertNotNull(view.getLoadingProperties());
		assertEquals("value", view.getLoadingProperties().get("key"));
		assertEquals("the view name 2", view.getName());
	}

	@Test
	public void testIntegrationWithViewProvider() {
		final EObject eObject = EcoreUtil.create(eClass1);
		final VView view = ViewProviderHelper.getView(eObject, null);
		assertEquals(VIEWNAME, view.getName());
	}
}
