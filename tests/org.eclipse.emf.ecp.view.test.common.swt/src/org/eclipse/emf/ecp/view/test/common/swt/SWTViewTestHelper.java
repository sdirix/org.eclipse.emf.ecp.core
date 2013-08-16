/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas Helming - initial API and implementation
 */
package org.eclipse.emf.ecp.view.test.common.swt;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderers;
import org.eclipse.emf.ecp.ui.view.test.ViewTestHelper;
import org.eclipse.emf.ecp.view.context.ViewModelContextImpl;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public final class SWTViewTestHelper {

	private SWTViewTestHelper() {

	}

	private static ViewModelContextImpl viewModelContextImpl;

	public static Shell createShell() {
		final Display display = Display.getDefault();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		return shell;
	}

	public static Control render(Renderable renderable, EObject input, Shell shell) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		final ECPControlContext context = ViewTestHelper.createECPControlContext(
			input, shell);
		final Node<Renderable> node = NodeBuilders.INSTANCE.build(renderable, context);
		final ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		final AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
			composedAdapterFactory);

		viewModelContextImpl = new ViewModelContextImpl((View) renderable, input);
		viewModelContextImpl.registerViewChangeListener(node);
		final List<RenderingResultRow<Control>> resultRows = SWTRenderers.INSTANCE.render(shell, node,
			adapterFactoryItemDelegator);
		// TODO return resultRows
		if (resultRows == null) {
			return null;
		}

		composedAdapterFactory.dispose();

		return resultRows.get(0).getMainControl();

	}

	public static Control render(Renderable renderable, Shell shell) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {
		return render(renderable, ViewFactory.eINSTANCE.createView(), shell);
	}

}
