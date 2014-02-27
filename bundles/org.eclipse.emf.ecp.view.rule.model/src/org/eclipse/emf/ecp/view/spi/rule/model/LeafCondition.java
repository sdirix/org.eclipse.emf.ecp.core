/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * EclipseSource Munich GmbH - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.rule.model;

import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Leaf Condition</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.spi.rule.model.LeafCondition#getExpectedValue <em>Expected Value</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.spi.rule.model.LeafCondition#getDomainModelReference <em>Domain Model Reference
 * </em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.ecp.view.spi.rule.model.RulePackage#getLeafCondition()
 * @model
 * @generated
 * @since 1.2
 */
public interface LeafCondition extends Condition {
	/**
	 * Returns the value of the '<em><b>Expected Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expected Value</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Expected Value</em>' attribute.
	 * @see #setExpectedValue(Object)
	 * @see org.eclipse.emf.ecp.view.spi.rule.model.RulePackage#getLeafCondition_ExpectedValue()
	 * @model required="true"
	 * @generated
	 */
	Object getExpectedValue();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.spi.rule.model.LeafCondition#getExpectedValue
	 * <em>Expected Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Expected Value</em>' attribute.
	 * @see #getExpectedValue()
	 * @generated
	 */
	void setExpectedValue(Object value);

	/**
	 * Returns the value of the '<em><b>Domain Model Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Domain Model Reference</em>' containment reference isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Domain Model Reference</em>' containment reference.
	 * @see #setDomainModelReference(VDomainModelReference)
	 * @see org.eclipse.emf.ecp.view.spi.rule.model.RulePackage#getLeafCondition_DomainModelReference()
	 * @model containment="true" required="true"
	 * @generated
	 */
	VDomainModelReference getDomainModelReference();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.spi.rule.model.LeafCondition#getDomainModelReference
	 * <em>Domain Model Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Domain Model Reference</em>' containment reference.
	 * @see #getDomainModelReference()
	 * @generated
	 */
	void setDomainModelReference(VDomainModelReference value);

} // LeafCondition
