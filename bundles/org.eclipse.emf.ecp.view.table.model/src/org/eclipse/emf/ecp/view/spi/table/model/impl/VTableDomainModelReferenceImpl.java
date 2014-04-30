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
package org.eclipse.emf.ecp.view.spi.table.model.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecp.view.spi.model.impl.VFeaturePathDomainModelReferenceImpl;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emf.ecp.view.spi.table.model.VTablePackage;

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
		final VTableControl control = findTableControl();
		if (!EReference.class.isInstance(getDomainModelEFeature())) {
			final Set<Setting> settings = Collections.emptySet();
			return settings.iterator();
		}
		final EList<EStructuralFeature> allFeatures = EReference.class.cast(getDomainModelEFeature())
			.getEReferenceType().getEAllStructuralFeatures();
		int numColumns = 1;
		if (control != null) {
			numColumns = control.getColumns().size();
		}
		else {
			numColumns = allFeatures.size();
		}
		if (!lastResolvedEObject.eClass().getEAllStructuralFeatures().contains(getDomainModelEFeature())) {
			final List<Setting> settings = Collections.emptyList();
			return settings.iterator();
		}
		final int numElems = 1 + ((EList<?>) lastResolvedEObject.eGet(getDomainModelEFeature())).size()
			* numColumns;
		return new Iterator<EStructuralFeature.Setting>() {
			// 1 for the tablereference + numElements*numColumns

			int currentObject = -1;
			int currentAttribute = 0;

			int returnedElements = 0;

			@Override
			public boolean hasNext() {
				if (control != null) {
					return numElems > returnedElements
						&& ((EList<?>) lastResolvedEObject.eGet(getDomainModelEFeature())).size()
							* control.getColumns().size() + 1 >= numElems - returnedElements;
				}
				return numElems > returnedElements
					&& ((EList<?>) lastResolvedEObject.eGet(getDomainModelEFeature())).size()
						* allFeatures.size() + 1 >= numElems - returnedElements;
			}

			@Override
			public Setting next() {
				Setting result = null;
				if (currentObject == -1) {
					result = ((InternalEObject) lastResolvedEObject).eSetting(getDomainModelEFeature());
					currentObject++;
				}
				else if (control != null) {

					result = ((InternalEObject) ((EList<?>) lastResolvedEObject.eGet(getDomainModelEFeature()))
						.get(currentObject)).eSetting(control.getColumns().get(currentAttribute++).getAttribute());
					if (currentAttribute == control.getColumns().size()) {
						currentAttribute = 0;
						currentObject++;
					}
				}
				else {

					result = ((InternalEObject) ((EList<?>) lastResolvedEObject.eGet(getDomainModelEFeature()))
						.get(currentObject)).eSetting(allFeatures.get(currentAttribute++));
					if (currentAttribute == allFeatures.size()) {
						currentAttribute = 0;
						currentObject++;
					}
				}
				returnedElements++;
				return result;
			}

			@Override
			public void remove() {
				// TODO Auto-generated method stub

			}

		};
	}

	/**
	 * @return
	 */
	private VTableControl findTableControl() {
		EObject parent = eContainer();
		while (!VTableControl.class.isInstance(parent) && parent != null) {
			parent = parent.eContainer();
		}
		return (VTableControl) parent;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.model.impl.VFeaturePathDomainModelReferenceImpl#getEStructuralFeatureIterator()
	 */
	@Override
	public Iterator<EStructuralFeature> getEStructuralFeatureIterator() {
		final VTableControl control = findTableControl();
		final EList<EStructuralFeature> allFeatures = EReference.class.cast(getDomainModelEFeature())
			.getEReferenceType().getEAllStructuralFeatures();
		return new Iterator<EStructuralFeature>() {
			private int counter = 0;

			@Override
			public boolean hasNext() {
				if (control != null) {
					return control.getColumns().size() + 1 > counter;
				}
				return allFeatures.size() + 1 > counter;
			}

			@Override
			public EStructuralFeature next() {
				EStructuralFeature result = null;
				if (0 == counter) {
					result = getDomainModelEFeature();
				}
				else {
					if (control != null) {
						result = control.getColumns().get(counter - 1).getAttribute();
					} else {
						result = allFeatures.get(counter - 1);
					}
				}
				counter++;
				return result;
			}

			@Override
			public void remove() {
			}
		};
	}

} // VTableDomainModelReferenceImpl
