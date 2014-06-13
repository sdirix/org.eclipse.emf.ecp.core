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
package org.eclipse.emf.ecp.view.spi.group.model;

import org.eclipse.emf.ecp.view.spi.model.VContainer;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.spi.group.model.VGroup#isContainerLayoutEmbedding <em>Container Layout Embedding
 * </em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.spi.group.model.VGroup#getLabelAlignment <em>Label Alignment</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.ecp.view.spi.group.model.VGroupPackage#getGroup()
 * @model
 * @generated
 */
public interface VGroup extends VContainer
{

	/**
	 * Returns the value of the '<em><b>Container Layout Embedding</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Container Layout Embedding</em>' attribute isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Container Layout Embedding</em>' attribute.
	 * @see #setContainerLayoutEmbedding(boolean)
	 * @see org.eclipse.emf.ecp.view.spi.group.model.VGroupPackage#getGroup_ContainerLayoutEmbedding()
	 * @model default="false"
	 * @generated
	 * @since 1.3
	 */
	boolean isContainerLayoutEmbedding();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.spi.group.model.VGroup#isContainerLayoutEmbedding
	 * <em>Container Layout Embedding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Container Layout Embedding</em>' attribute.
	 * @see #isContainerLayoutEmbedding()
	 * @generated
	 * @since 1.3
	 */
	void setContainerLayoutEmbedding(boolean value);

	/**
	 * Returns the value of the '<em><b>Label Alignment</b></em>' attribute.
	 * The default value is <code>"LabelAligned"</code>.
	 * The literals are from the enumeration {@link org.eclipse.emf.ecp.view.spi.group.model.GroupLabelAlignment}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Label Alignment</em>' attribute isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Label Alignment</em>' attribute.
	 * @see org.eclipse.emf.ecp.view.spi.group.model.GroupLabelAlignment
	 * @see #setLabelAlignment(GroupLabelAlignment)
	 * @see org.eclipse.emf.ecp.view.spi.group.model.VGroupPackage#getGroup_LabelAlignment()
	 * @model default="LabelAligned"
	 * @generated
	 * @since 1.3
	 */
	GroupLabelAlignment getLabelAlignment();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.ecp.view.spi.group.model.VGroup#getLabelAlignment
	 * <em>Label Alignment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Label Alignment</em>' attribute.
	 * @see org.eclipse.emf.ecp.view.spi.group.model.GroupLabelAlignment
	 * @see #getLabelAlignment()
	 * @generated
	 * @since 1.3
	 */
	void setLabelAlignment(GroupLabelAlignment value);
} // VGroup
