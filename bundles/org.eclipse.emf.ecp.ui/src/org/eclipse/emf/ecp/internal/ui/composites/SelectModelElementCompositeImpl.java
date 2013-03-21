/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.internal.ui.composites;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.internal.ui.ECPViewerFilter;
import org.eclipse.emf.ecp.ui.common.SelectionComposite;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a composite which displays available EObejcts to the user. The user can
 * filter the items by typing in filter text.
 * 
 * @author Eugen Neufeld
 * 
 */
public class SelectModelElementCompositeImpl extends AbstractFilteredSelectionComposite<TableViewer> implements
	SelectionComposite<TableViewer> {

	private final ECPViewerFilter filter;

	private final Object input;

	private ComposedAdapterFactory composedAdapterFactory;

	private AdapterFactoryLabelProvider adapterFactoryLabelProvider;

	/**
	 * Default Constructor for the SelectModelElementCompositeImpl.
	 * 
	 * @param input the input for the selection
	 */
	public SelectModelElementCompositeImpl(Object input) {
		super();
		composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);
		filter = new ModelElementViewerFilter(adapterFactoryLabelProvider);
		this.input = input;
	}

	private ILabelProvider getLabelProvider() {
		return adapterFactoryLabelProvider;
	}

	private Object getInput() {
		return input;
	}

	/** {@inheritDoc} **/
	@Override
	protected ECPViewerFilter getFilter() {
		return filter;
	}

	/** {@inheritDoc} **/
	@Override
	protected TableViewer createViewer(Composite composite) {
		TableViewer lv = new TableViewer(composite);
		lv.setLabelProvider(getLabelProvider());
		lv.setContentProvider(ArrayContentProvider.getInstance());
		lv.setInput(getInput());
		return lv;
	}

	/** {@inheritDoc} **/
	public void dispose() {
		composedAdapterFactory.dispose();
		adapterFactoryLabelProvider.dispose();
	}

	/**
	 * Private Implementation of a {@link ECPViewerFilter} for ModelElements.
	 * 
	 * @author Eugen Neufeld
	 * 
	 */
	private final class ModelElementViewerFilter extends ECPViewerFilter {

		private ILabelProvider labelProvider;

		public ModelElementViewerFilter(ILabelProvider labelProvider) {
			this.labelProvider = labelProvider;
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (getSearchTerm() == null || getSearchTerm().length() == 0) {
				return true;
			}
			if (!(element instanceof EObject)) {
				return false;
			}
			// TODO is this ok so?
			EObject eObjectToFilter = (EObject) element;

			String eObjectName = labelProvider.getText(eObjectToFilter);

			String searchString = getSearchTerm();
			if (!searchString.startsWith("*")) {
				searchString = "*" + searchString + "*";
			}
			Pattern pattern = Pattern.compile(wildcardToRegex(searchString), Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(eObjectName);
			return matcher.matches();
		}

		private String wildcardToRegex(String wildcard) {
			StringBuffer s = new StringBuffer(wildcard.length());
			s.append('^');
			for (int i = 0, is = wildcard.length(); i < is; i++) {
				char c = wildcard.charAt(i);
				switch (c) {
				case '*':
					s.append(".*");
					break;
				case '?':
					s.append(".");
					break;
				case ' ':
					s.append("\\s");
					break;
				// escape special regexp-characters
				case '(':
				case ')':
				case '[':
				case ']':
				case '$':
				case '^':
				case '.':
				case '{':
				case '}':
				case '|':
				case '\\':
					s.append("\\");
					s.append(c);
					break;
				default:
					s.append(c);
					break;
				}
			}
			s.append('$');
			return s.toString();
		}
	}
}
