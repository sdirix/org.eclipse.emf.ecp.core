/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.separator.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.exceptions.ECPProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.explorereditorbridge.internal.ECPControlContextImpl;
import org.eclipse.emf.ecp.internal.core.ECPProjectManagerImpl;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderers;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.model.separator.Separator;
import org.eclipse.emf.ecp.view.model.separator.SeparatorFactory;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DatabindingClassRunner.class)
public class SWTSeparatorTest {

	@Test
	public void testSeparator() throws NoRendererFoundException,
			NoPropertyDescriptorFoundExeption,
			ECPProjectWithNameExistsException {

		// setup model
		View view = ViewFactory.eINSTANCE.createView();
		Category category = ViewFactory.eINSTANCE.createCategory();
		Separator separator = SeparatorFactory.eINSTANCE.createSeparator();
		separator.setName("separator");
		category.setComposite(separator);
		view.getCategorizations().add(category);

		// setup ui
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		Composite parent = new Composite(shell, SWT.NONE);
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		parent.setLayout(new GridLayout());

		// setup context
		ECPProvider provider = ECPUtil.getECPProviderRegistry().getProvider(
				EMFStoreProvider.NAME);
		ECPProject project = ECPProjectManagerImpl.INSTANCE.createProject(
				provider, "test");
		project.getContents().add(view);
		ECPControlContextImpl context = new ECPControlContextImpl(view,
				project, shell);

		// test SWTRenderer
		Node<View> node = NodeBuilders.INSTANCE.build(view, context);
		// ModelRenderer<Composite> renderer = ModelRenderer.INSTANCE.
		// .getRenderer(new Object[] { parent });
		// RendererContext<Composite> rendererContext = renderer.render(node);
		// Composite c = rendererContext.getControl();
		// Control control = c.getChildren()[0];

		ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
				composedAdapterFactory);
		Control control = SWTRenderers.INSTANCE.render(parent, node,
				adapterFactoryItemDelegator);
		assertEquals("org_eclipse_emf_ecp_ui_seperator",
				control.getData("org.eclipse.rap.rwt.customVariant"));

	}
}
