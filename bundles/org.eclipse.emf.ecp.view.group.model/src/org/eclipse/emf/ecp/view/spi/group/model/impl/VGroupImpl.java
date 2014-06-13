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
package org.eclipse.emf.ecp.view.spi.group.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecp.view.spi.group.model.GroupLabelAlignment;
import org.eclipse.emf.ecp.view.spi.group.model.VGroup;
import org.eclipse.emf.ecp.view.spi.group.model.VGroupPackage;
import org.eclipse.emf.ecp.view.spi.model.impl.VContainerImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.ecp.view.spi.group.model.impl.VGroupImpl#isContainerLayoutEmbedding <em>Container Layout
 * Embedding</em>}</li>
 * <li>{@link org.eclipse.emf.ecp.view.spi.group.model.impl.VGroupImpl#getLabelAlignment <em>Label Alignment</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 * @since 1.3
 */
public class VGroupImpl extends VContainerImpl implements VGroup
{
	/**
	 * The default value of the '{@link #isContainerLayoutEmbedding() <em>Container Layout Embedding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #isContainerLayoutEmbedding()
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	protected static final boolean CONTAINER_LAYOUT_EMBEDDING_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isContainerLayoutEmbedding() <em>Container Layout Embedding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #isContainerLayoutEmbedding()
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	protected boolean containerLayoutEmbedding = CONTAINER_LAYOUT_EMBEDDING_EDEFAULT;
	/**
	 * The default value of the '{@link #getLabelAlignment() <em>Label Alignment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getLabelAlignment()
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	protected static final GroupLabelAlignment LABEL_ALIGNMENT_EDEFAULT = GroupLabelAlignment.LABEL_ALIGNED;
	/**
	 * The cached value of the '{@link #getLabelAlignment() <em>Label Alignment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getLabelAlignment()
	 * @generated
	 * @ordered
	 * @since 1.3
	 */
	protected GroupLabelAlignment labelAlignment = LABEL_ALIGNMENT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VGroupImpl()
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
		return VGroupPackage.Literals.GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @since 1.3
	 */
	@Override
	public boolean isContainerLayoutEmbedding()
	{
		return containerLayoutEmbedding;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @since 1.3
	 */
	@Override
	public void setContainerLayoutEmbedding(boolean newContainerLayoutEmbedding)
	{
		final boolean oldContainerLayoutEmbedding = containerLayoutEmbedding;
		containerLayoutEmbedding = newContainerLayoutEmbedding;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, VGroupPackage.GROUP__CONTAINER_LAYOUT_EMBEDDING,
				oldContainerLayoutEmbedding, containerLayoutEmbedding));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @since 1.3
	 */
	@Override
	public GroupLabelAlignment getLabelAlignment()
	{
		return labelAlignment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @since 1.3
	 */
	@Override
	public void setLabelAlignment(GroupLabelAlignment newLabelAlignment)
	{
		final GroupLabelAlignment oldLabelAlignment = labelAlignment;
		labelAlignment = newLabelAlignment == null ? LABEL_ALIGNMENT_EDEFAULT : newLabelAlignment;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, VGroupPackage.GROUP__LABEL_ALIGNMENT,
				oldLabelAlignment, labelAlignment));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType)
	{
		switch (featureID)
		{
		case VGroupPackage.GROUP__CONTAINER_LAYOUT_EMBEDDING:
			return isContainerLayoutEmbedding();
		case VGroupPackage.GROUP__LABEL_ALIGNMENT:
			return getLabelAlignment();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
		case VGroupPackage.GROUP__CONTAINER_LAYOUT_EMBEDDING:
			setContainerLayoutEmbedding((Boolean) newValue);
			return;
		case VGroupPackage.GROUP__LABEL_ALIGNMENT:
			setLabelAlignment((GroupLabelAlignment) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID)
	{
		switch (featureID)
		{
		case VGroupPackage.GROUP__CONTAINER_LAYOUT_EMBEDDING:
			setContainerLayoutEmbedding(CONTAINER_LAYOUT_EMBEDDING_EDEFAULT);
			return;
		case VGroupPackage.GROUP__LABEL_ALIGNMENT:
			setLabelAlignment(LABEL_ALIGNMENT_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID)
	{
		switch (featureID)
		{
		case VGroupPackage.GROUP__CONTAINER_LAYOUT_EMBEDDING:
			return containerLayoutEmbedding != CONTAINER_LAYOUT_EMBEDDING_EDEFAULT;
		case VGroupPackage.GROUP__LABEL_ALIGNMENT:
			return labelAlignment != LABEL_ALIGNMENT_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString()
	{
		if (eIsProxy()) {
			return super.toString();
		}

		final StringBuffer result = new StringBuffer(super.toString());
		result.append(" (containerLayoutEmbedding: "); //$NON-NLS-1$
		result.append(containerLayoutEmbedding);
		result.append(", labelAlignment: "); //$NON-NLS-1$
		result.append(labelAlignment);
		result.append(')');
		return result.toString();
	}

} // VGroupImpl
