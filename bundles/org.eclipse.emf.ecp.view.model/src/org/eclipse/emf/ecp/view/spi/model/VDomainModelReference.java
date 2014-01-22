/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 */
package org.eclipse.emf.ecp.view.spi.model;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>VDomain Model Reference</b></em>'.
 * <!-- end-user-doc -->
 * 
 * 
 * @see org.eclipse.emf.ecp.view.spi.model.VViewPackage#getDomainModelReference()
 * @model interface="true" abstract="true"
 * @generated
 * @since 1.2
 */
public interface VDomainModelReference extends EObject
{
	/**
	 * Resolve attempts to resolve the domain model reference. If it fails existing results will be kept. Thus even
	 * after
	 * a failed resolution the iterator might not be empty.
	 * 
	 * @param eObject the root domain model to use to resolve the path
	 * @return true if resolution succeeded and false otherwise
	 */
	boolean resolve(EObject eObject);

	/**
	 * Returns an iterator allowing to iterate over found settings for this domain model reference.
	 * Will return an empty Iterator if resolve was not successfully completed.
	 * 
	 * @return the {@link Iterator} for this domain model reference
	 */
	Iterator<Setting> getIterator();

	/**
	 * Returns an iterator allowing to iterate overall {@link EStructuralFeature EStructuralFeatures} of this domain
	 * model reference.
	 * 
	 * @return the {@link Iterator} over all {@link EStructuralFeature EStructuralFeatures} for this domain model
	 *         reference
	 */
	Iterator<EStructuralFeature> getEStructuralFeatureIterator();

} // VDomainModelReference
