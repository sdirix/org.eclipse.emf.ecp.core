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
 * 
 *******************************************************************************/

package org.eclipse.emf.ecp.internal.editor.descriptor;

/**
 * A {@link AbstractAttributeDescriptor} using the annotation in the genmodel.
 * 
 * @author Shterev
 * @author Eugen Neufeld
 */
public class AnnotationPriorityDescriptor extends AbstractAttributeDescriptor<Double> {
	@Override
	protected String getAnnotationName() {
		return "priority";
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.internal.editor.descriptor.AbstractAttributeDescriptor#getDefaultValue()
	 */
	@Override
	protected Double getDefaultValue() {
		return 100d;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.internal.editor.descriptor.AbstractAttributeDescriptor#getTypedValue(java.lang.String)
	 */
	@Override
	protected Double getTypedValue(String value) {
		return Double.parseDouble(value);
	}
}
