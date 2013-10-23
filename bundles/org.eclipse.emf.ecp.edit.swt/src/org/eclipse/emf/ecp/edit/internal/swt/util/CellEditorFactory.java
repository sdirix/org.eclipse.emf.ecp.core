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
package org.eclipse.emf.ecp.edit.internal.swt.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.edit.spi.util.ECPApplicableTester;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.osgi.framework.Bundle;

public final class CellEditorFactory {
	private static final String CONTROL_EXTENSION = "org.eclipse.emf.ecp.edit.swt.cellEditor"; //$NON-NLS-1$

	private static final String CLASS_ATTRIBUTE = "class";//$NON-NLS-1$
	private static final String ID = "id";//$NON-NLS-1$
	private static final String TESTER = "tester";//$NON-NLS-1$

	private Set<CellDescriptor> descriptors = new HashSet<CellEditorFactory.CellDescriptor>();

	public static final CellEditorFactory INSTANCE = new CellEditorFactory();

	private CellEditorFactory() {
		parseExtensionPoint();
	}

	private void parseExtensionPoint() {
		IConfigurationElement[] controls = Platform.getExtensionRegistry().getConfigurationElementsFor(
			CONTROL_EXTENSION);
		for (IConfigurationElement e : controls) {
			try {
				String id = e.getAttribute(ID);
				String clazz = e.getAttribute(CLASS_ATTRIBUTE);
				Class<? extends CellEditor> resolvedClass = loadClass(e.getContributor().getName(), clazz);
				ECPApplicableTester tester = (ECPApplicableTester) e.createExecutableExtension(TESTER);
				descriptors.add(new CellDescriptor(id, resolvedClass, tester));
			} catch (ClassNotFoundException e1) {
				Activator.logException(e1);
			} catch (CoreException e1) {
				Activator.logException(e1);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> Class<T> loadClass(String bundleName, String clazz) throws ClassNotFoundException {
		Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			throw new ClassNotFoundException(clazz + UtilMessages.CellEditorFactory_CannotBeLoadedBecauseBundle + bundleName
				+ UtilMessages.CellEditorFactory_CannotBeResolved);
		}
		return (Class<T>) bundle.loadClass(clazz);

	}

	public CellEditor getCellEditor(IItemPropertyDescriptor propertyDescriptor, EObject eObject, Table table,
		ECPControlContext ecpControlContext) {
		int bestPriority = -1;
		CellDescriptor bestCandidate = null;
		for (CellDescriptor descriptor : descriptors) {
			int priority = descriptor.getTester().isApplicable(propertyDescriptor, eObject);
			if (priority > bestPriority) {
				bestCandidate = descriptor;
				bestPriority = priority;
			}
		}
		CellEditor result = null;
		if (bestCandidate != null) {
			try {
				Constructor<? extends CellEditor> constructor = bestCandidate.getCellEditorClass().getConstructor(
					Composite.class);
				result = constructor.newInstance(table);
				ECPCellEditor ecpCellEditor = (ECPCellEditor) result;
				ecpCellEditor.instantiate(propertyDescriptor, ecpControlContext);
			} catch (SecurityException e) {
				Activator.logException(e);
			} catch (NoSuchMethodException e) {
				Activator.logException(e);
			} catch (IllegalArgumentException e) {
				Activator.logException(e);
			} catch (InstantiationException e) {
				Activator.logException(e);
			} catch (IllegalAccessException e) {
				Activator.logException(e);
			} catch (InvocationTargetException e) {
				Activator.logException(e);
			} catch (ClassCastException e) {
				Activator.logException(e);
			}
		}
		if (result == null) {
			result = new TextCellEditor(table);
		}
		return result;
	}

	private class CellDescriptor {
		private final String id;
		private final Class<? extends CellEditor> cellEditorClass;
		private final ECPApplicableTester tester;

		CellDescriptor(String id, Class<? extends CellEditor> cellEditorClass, ECPApplicableTester tester) {
			super();
			this.id = id;
			this.cellEditorClass = cellEditorClass;
			this.tester = tester;
		}

		String getId() {
			return id;
		}

		Class<? extends CellEditor> getCellEditorClass() {
			return cellEditorClass;
		}

		ECPApplicableTester getTester() {
			return tester;
		}
	}
}
