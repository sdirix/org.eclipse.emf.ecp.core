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
package org.eclipse.emf.ecp.view.model;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>VDomain Model Reference</b></em>'.
 * <!-- end-user-doc -->
 * 
 * 
 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getVDomainModelReference()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface VDomainModelReference extends EObject
{

	boolean resolve(EObject eObject);

	Iterator<Setting> getIterator();

} // VDomainModelReference
