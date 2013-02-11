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
package org.eclipse.emf.ecp.edit;

import org.eclipse.emf.ecp.editor.util.ECPApplicableTester;

public class ControlDescription {

	private final Class<? extends AbstractControl<?>> controlClass;
	private final Class<?> supportedCompositeClass;
	private final boolean showLabel;
	private final ECPApplicableTester tester;
	
	
	public ControlDescription(Class<? extends AbstractControl<?>> controlClass, Class<?> supportedCompositeClass, boolean showLabel, ECPApplicableTester tester) {
		super();
		this.controlClass = controlClass;
		this.supportedCompositeClass=supportedCompositeClass;
		this.showLabel=showLabel;
		this.tester=tester;
	}
	public Class<? extends AbstractControl<?>> getControlClass() {
		return controlClass;
	}
	
	public Class<?> getSupportedCompositeClass() {
		return supportedCompositeClass;
	}
	public boolean isShowLabel() {
		return showLabel;
	}
	public ECPApplicableTester getTester() {
		return tester;
	}
}
