/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edagr Mueller - initial API and implementation
 * Eugen Neufeld - Refactoring
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.view.model.CustomComposite;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.osgi.framework.Bundle;

public class SWTCustomCompositeRenderer extends AbstractSWTRenderer<CustomComposite> {
	public static final SWTCustomCompositeRenderer INSTANCE = new SWTCustomCompositeRenderer();

	@Override
	public List<RenderingResultRow<Control>> renderSWT(Node<CustomComposite> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData) {

		final CustomComposite customComposite = node.getRenderable();

		// TODO: handle exceptions
		try {
			final Class<?> clazz = getClass(customComposite.getBundle(), customComposite.getClassName());
			final Constructor<?> constructor = clazz.getConstructor(Composite.class, EObject.class);
			final Composite parent = getParentFromInitData(initData);
			final Object obj = constructor.newInstance(parent, node.getControlContext().getModelElement());
			final Composite categoryComposite = (Composite) obj;

			node.addRenderingResultDelegator(withSWT(categoryComposite));

			return createResult(categoryComposite);

		} catch (final NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private Class<?> getClass(String pluginID, String className) throws ClassNotFoundException {
		final Bundle bundle = Platform.getBundle(pluginID);
		if (bundle == null) {
			throw new ClassNotFoundException(className
				+ " cannot be loaded because because bundle " + pluginID
				+ " cannot be resolved");
		} else {
			return bundle.loadClass(className);
		}
	}

}
