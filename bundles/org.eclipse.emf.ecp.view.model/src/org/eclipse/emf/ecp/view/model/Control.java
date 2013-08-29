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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Control</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.model.Control#getTargetFeature <em>Target Feature</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.model.Control#getHint <em>Hint</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.model.Control#isMandatory <em>Mandatory</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.model.Control#getPathToFeature <em>Path To Feature</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.model.Control#getLabelAlignment <em>Label Alignment</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getControl()
 * @model
 * @generated
 */
public interface Control extends AbstractControl {
	/**
	 * Returns the value of the '<em><b>Target Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target Feature</em>' reference isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Target Feature</em>' reference.
	 * @see #setTargetFeature(EStructuralFeature)
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getControl_TargetFeature()
	 * @model required="true"
	 * @generated
	 */
	EStructuralFeature getTargetFeature();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.model.Control#getTargetFeature <em>Target Feature</em>}'
	 * reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Target Feature</em>' reference.
	 * @see #getTargetFeature()
	 * @generated
	 */
	void setTargetFeature(EStructuralFeature value);

	/**
	 * Returns the value of the '<em><b>Hint</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hint</em>' attribute list isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Hint</em>' attribute list.
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getControl_Hint()
	 * @model
	 * @generated
	 */
	EList<String> getHint();

	/**
	 * Returns the value of the '<em><b>Mandatory</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mandatory</em>' attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Mandatory</em>' attribute.
	 * @see #setMandatory(boolean)
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getControl_Mandatory()
	 * @model default="false"
	 * @generated
	 */
	boolean isMandatory();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.model.Control#isMandatory <em>Mandatory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Mandatory</em>' attribute.
	 * @see #isMandatory()
	 * @generated
	 */
	void setMandatory(boolean value);

	/**
	 * Returns the value of the '<em><b>Path To Feature</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EReference}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Path To Feature</em>' reference list isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Path To Feature</em>' reference list.
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getControl_PathToFeature()
	 * @model
	 * @generated
	 */
	EList<EReference> getPathToFeature();

	/**
	 * Returns the value of the '<em><b>Label Alignment</b></em>' attribute.
	 * The default value is <code>"Left"</code>.
	 * The literals are from the enumeration {@link org.eclipse.emf.ecp.view.model.Alignment}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Label Alignment</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Label Alignment</em>' attribute.
	 * @see org.eclipse.emf.ecp.view.model.Alignment
	 * @see #setLabelAlignment(Alignment)
	 * @see org.eclipse.emf.ecp.view.model.ViewPackage#getControl_LabelAlignment()
	 * @model default="Left"
	 * @generated
	 */
	Alignment getLabelAlignment();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.model.Control#getLabelAlignment <em>Label Alignment</em>}'
	 * attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Label Alignment</em>' attribute.
	 * @see org.eclipse.emf.ecp.view.model.Alignment
	 * @see #getLabelAlignment()
	 * @generated
	 */
	void setLabelAlignment(Alignment value);

} // Control
