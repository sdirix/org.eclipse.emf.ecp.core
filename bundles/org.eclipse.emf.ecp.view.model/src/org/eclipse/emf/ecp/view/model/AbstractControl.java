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

import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Control</b></em>'.
 * <!-- end-user-doc -->
 * 
 * 
 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getAbstractControl()
 * @model abstract="true"
 * @generated
 */
public interface AbstractControl extends Composite {

	Set<VDomainModelReference> getDomainModelReferences();

} // AbstractControl
