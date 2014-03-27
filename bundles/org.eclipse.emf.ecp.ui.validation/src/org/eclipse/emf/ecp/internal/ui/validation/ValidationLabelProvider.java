/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.internal.ui.validation;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * {@link ITableLabelProvider} for displaying {@link org.eclipse.emf.common.util.Diagnostic Diagnostics} in a Tree with
 * columns.
 * 
 * @author jfaltermeier
 * 
 */
public class ValidationLabelProvider implements ITableLabelProvider {

	private final ComposedAdapterFactory composedAdapterFactory;
	private final AdapterFactoryLabelProvider labelProvider;

	/**
	 * Default constructor.
	 */
	public ValidationLabelProvider() {
		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		labelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener listener) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose() {
		composedAdapterFactory.dispose();
		labelProvider.dispose();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
	 */
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener listener) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		final Diagnostic diagnostic = (Diagnostic) element;
		switch (columnIndex) {
		case 0:
			return getImageForSeverity(diagnostic.getSeverity());
		default:
			return null;
		}
	}

	private Image getImageForSeverity(int severity) {
		// TODO add images
		switch (severity) {
		case Diagnostic.OK:
		case Diagnostic.INFO:
		case Diagnostic.WARNING:
		case Diagnostic.ERROR:
		case Diagnostic.CANCEL:
		default:
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		final Diagnostic diagnostic = (Diagnostic) element;
		final List<?> data = diagnostic.getData();
		switch (columnIndex) {
		case 0:
			return diagnostic.getMessage();
		case 1:
			if (data.size() > 0) {
				return labelProvider.getText(data.get(0));
			}
			return null;
		case 2:
			if (data.size() > 1) {
				return ((EStructuralFeature) data.get(1)).getName();
			}
			return null;
		default:
			return null;
		}
	}
}
