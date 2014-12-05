/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.model.common;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;

/**
 * Tester for Text Renderer.
 *
 * @author Eugen Neufeld
 *
 */
public class XMLDateControlRendererTester extends SimpleControlRendererTester {

	private static final String XML_TYPE_DATE = "http://www.eclipse.org/emf/2003/XMLType#date"; //$NON-NLS-1$
	private static final String BASE_TYPE = "baseType"; //$NON-NLS-1$
	private static final String EXTENDED_META_DATA = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.model.common.SimpleControlRendererTester#isSingleValue()
	 */
	@Override
	protected boolean isSingleValue() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.model.common.SimpleControlRendererTester#getPriority()
	 */
	@Override
	protected int getPriority() {
		return 3;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.model.common.SimpleControlRendererTester#getSupportedClassType()
	 */
	@Override
	protected Class<?> getSupportedClassType() {
		return XMLGregorianCalendar.class;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.model.common.SimpleControlRendererTester#checkFeatureETypeAnnotations(org.eclipse.emf.common.util.EList)
	 */
	@Override
	protected boolean checkFeatureETypeAnnotations(EList<EAnnotation> eAnnotations) {
		for (final EAnnotation annotation : eAnnotations) {
			if (!annotation.getSource().equals(EXTENDED_META_DATA)) {
				continue;
			}
			if (!annotation.getDetails().containsKey(BASE_TYPE)) {
				continue;
			}
			if (annotation.getDetails().get(BASE_TYPE).equals(XML_TYPE_DATE)) {
				return true;
			}
		}
		return false;
	}
}
