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
package org.eclipse.emf.ecp.ui.view.custom.swt;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.swt.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.custom.model.CustomControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.widgets.Control;
import org.osgi.framework.Bundle;

public class CustomControlSWTRenderer extends
	AbstractSWTRenderer<CustomControl> {

	@Override
	protected List<RenderingResultRow<Control>> renderSWT(Node<CustomControl> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final CustomControl customControl = node.getRenderable();

		// TODO: handle exceptions
		try {
			final Class<?> clazz = getClass(customControl.getBundle(),
				customControl.getClassName());
			final Constructor<?> constructor = clazz.getConstructor();
			final Object obj = constructor.newInstance();
			final ECPAbstractCustomControlSWT categoryComposite = (ECPAbstractCustomControlSWT) obj;
			categoryComposite.init(node.getControlContext());

			final List<RenderingResultRow<Control>> renderingResultRows = categoryComposite
				.createControls(getParentFromInitData(initData));
			node.addRenderingResultDelegator(new SWTRenderingResultCustomControl(categoryComposite, customControl,
				getParentFromInitData(initData)));

			return renderingResultRows;
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

	private Class<?> getClass(String pluginID, String className)
		throws ClassNotFoundException {
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
