/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.common;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eugen Neufeld
 * 
 */
public class SelectModelElementComposite extends AbstractFilteredSelectionComposite<TableViewer> {

	private final ECPViewerFilter filter = new ECPViewerFilter() {

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

			String eObjectName = eObjectToFilter.toString();
			String searchString = getSearchTerm();
			if (!searchString.startsWith("*")) {
				searchString = "*" + searchString + "*";
			}
			Pattern pattern = Pattern.compile(wildcardToRegex(searchString), Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(eObjectName);
			return matcher.matches();
		}

		public String wildcardToRegex(String wildcard) {
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
	};
	private final ILabelProvider labelProvider;
	private final Object input;

	/**
	 * Default Constructor for the SelectModelElementComposite.
	 * 
	 * @param labelProvider the {@link ILabelProvider} for the selection
	 * @param input the input for the selection
	 */
	public SelectModelElementComposite(ILabelProvider labelProvider, Object input) {
		super();
		this.labelProvider = labelProvider;
		this.input = input;
	}

	private ILabelProvider getLabelProvider() {
		return labelProvider;
	}

	private Object getInput() {
		return input;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.ui.common.AbstractFilteredSelectionComposite#getFilter()
	 */
	@Override
	protected ECPViewerFilter getFilter() {
		return filter;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.ecp.ui.common.AbstractFilteredSelectionComposite#createViewer(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected TableViewer createViewer(Composite composite) {
		TableViewer lv = new TableViewer(composite);
		lv.setLabelProvider(getLabelProvider());
		lv.setContentProvider(ArrayContentProvider.getInstance());
		lv.setInput(getInput());
		return lv;
	}

}
