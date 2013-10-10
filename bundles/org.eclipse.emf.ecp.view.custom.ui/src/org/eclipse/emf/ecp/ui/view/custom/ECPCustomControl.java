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
 * Edagr Mueller - initial API and implementation
 * Johannes Faltermeier - ECPCustomControl API changes
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.custom;

import java.util.Set;

import org.eclipse.emf.ecp.edit.ECPControl;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;

/**
 * {@link ECPControl} is the interface describing a custom control.
 * 
 * @author eneufeld
 * @author emueller
 * @author jfaltermeier
 * 
 */
public interface ECPCustomControl extends ECPControl {

	/**
	 * Returns all registered {@link ECPCustomControlFeature}s of this {@link ECPCustomControl}.
	 * 
	 * @return all features
	 */
	// Set<ECPCustomControlFeature> getECPCustomControlFeatures();
	/**
	 * Returns a list of all {@link VDomainModelReference VDomainModelReferences} which will be used by this control.
	 * 
	 * @return a {@link Set} of {@link VDomainModelReference VDomainModelReferences} to be used by the control
	 */
	Set<VDomainModelReference> getNeededDomainModelReferences();

}
