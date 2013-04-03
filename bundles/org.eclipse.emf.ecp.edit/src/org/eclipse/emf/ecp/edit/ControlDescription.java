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

import java.util.Set;

import org.eclipse.emf.ecp.edit.util.ECPApplicableTester;

/**
 * This class describes the control extensionpoint. It is used to provide the possibility to access all known controls without reading the extension point.
 * @author Eugen Neufeld
 */
public class ControlDescription {

	private final String id;
	private final Class<? extends AbstractControl<?>> controlClass;
	private final Class<?> supportedCompositeClass;
	private final boolean showLabel;
	private final Set<ECPApplicableTester> tester;
	
	/**
	 * The constructor of the ControlDescription.
	 * @param controlClass the class implementing the control
	 * @param supportedCompositeClass the composite this class is supporting
	 * @param showLabel whether to show a label for this control or not
	 * @param tester the class testing whether the control is applicable for the current feature of the current eobject
	 */
	public ControlDescription(String id,Class<? extends AbstractControl<?>> controlClass, Class<?> supportedCompositeClass, boolean showLabel, Set<ECPApplicableTester> tester) {
		super();
		this.id=id;
		this.controlClass = controlClass;
		this.supportedCompositeClass=supportedCompositeClass;
		this.showLabel=showLabel;
		this.tester=tester;
	}
	public String getId() {
		return id;
	}
	/**
	 * The class implementing the Control. It extends the {@link AbstractControl}.
	 * @return the class implementing this control
	 */
	public Class<? extends AbstractControl<?>> getControlClass() {
		return controlClass;
	}
	/**
	 * The class of the supported composite. For SWT this is for example the org.eclipse.swt.Composite class.
	 * @return the class of the composite
	 */
	public Class<?> getSupportedCompositeClass() {
		return supportedCompositeClass;
	}
	/**
	 * Whether to show a label for this control or not.
	 * @return true if a label should be shown
	 */
	public boolean isShowLabel() {
		return showLabel;
	}
	/**
	 * The tester for this control. The tester is used to check whether this control is usable on a specific feature of a specific eobject.
	 * @return the {@link ECPApplicableTester} implementation
	 */
	public Set<ECPApplicableTester> getTester() {
		return tester;
	}
}
