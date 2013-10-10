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
package org.eclipse.emf.ecp.view.table.model.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecp.view.model.impl.VFeaturePathDomainModelReferenceImpl;
import org.eclipse.emf.ecp.view.table.model.VTableControl;
import org.eclipse.emf.ecp.view.table.model.VTableDomainModelReference;
import org.eclipse.emf.ecp.view.table.model.VTablePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Domain Model Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 * 
 * @generated
 */
public class VTableDomainModelReferenceImpl extends VFeaturePathDomainModelReferenceImpl implements
	VTableDomainModelReference
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VTableDomainModelReferenceImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass()
	{
		return VTablePackage.Literals.TABLE_DOMAIN_MODEL_REFERENCE;
	}

	@Override
	public Iterator<Setting> getIterator() {
		if (lastResolvedEObject == null || leftReferences == null) {
			final Set<Setting> settings = Collections.emptySet();
			return settings.iterator();
		}
		final VTableControl control = (VTableControl) eContainer();

		return new Iterator<EStructuralFeature.Setting>() {
			// 1 for the tablereference + numElements*numColumns
			int numElems = 1 + ((EList<?>) lastResolvedEObject.eGet(getDomainModelEFeature())).size()
				* control.getColumns().size();
			int currentObject = -1;
			int currentAttribute = 0;

			public boolean hasNext() {
				return numElems > 0;
			}

			public Setting next() {
				Setting result;
				if (currentObject == -1) {
					result = ((InternalEObject) lastResolvedEObject).eSetting(getDomainModelEFeature());
					currentObject++;
				}
				else {

					result = ((InternalEObject) ((EList<?>) lastResolvedEObject.eGet(getDomainModelEFeature()))
						.get(currentObject)).eSetting(control.getColumns().get(currentAttribute++).getAttribute());
					if (currentAttribute == control.getColumns().size()) {
						currentAttribute = 0;
						currentObject++;
					}
				}
				numElems--;
				return result;
			}

			public void remove() {
				// TODO Auto-generated method stub

			}

		};
	}

} // VTableDomainModelReferenceImpl
