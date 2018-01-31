/*******************************************************************************
 * Copyright (c) 2011-2018 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.editor.controls.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.eclipse.emf.ecp.view.internal.editor.handler.GenerateControlsHandler;
import org.eclipse.emf.ecp.view.spi.model.VContainedContainer;
import org.eclipse.emf.ecp.view.spi.model.VContainer;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for {@link GenerateControlsHandler}.
 *
 * @author Lucas Koehler
 *
 */
public class GenerateControlsHandler_Test {
	private GenerateControlsHandler controlsHandler;

	@Before
	public void setUp() {
		controlsHandler = new GenerateControlsHandler();
	}

	@Test
	public void testShouldShow() {
		final VElement element = mock(VElement.class);
		final VControl control = mock(VControl.class);
		final VContainer container = mock(VContainer.class);
		final VContainedContainer containedContainer = mock(VContainedContainer.class);
		final VView view = mock(VView.class);

		assertFalse(controlsHandler.shouldShow(element));
		assertFalse(controlsHandler.shouldShow(control));
		assertTrue(controlsHandler.shouldShow(container));
		assertTrue(controlsHandler.shouldShow(containedContainer));
		assertTrue(controlsHandler.shouldShow(view));
	}
}
