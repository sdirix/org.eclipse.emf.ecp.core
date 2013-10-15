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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see org.eclipse.emf.ecp.view.model.ViewFactory
 * @model kind="package"
 * @generated
 */
public interface ViewPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://org/eclipse/emf/ecp/view/model";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.emf.ecp.view.model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	ViewPackage eINSTANCE = org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.AbstractCategorizationImpl
	 * <em>Abstract Categorization</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.AbstractCategorizationImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getAbstractCategorization()
	 * @generated
	 */
	int ABSTRACT_CATEGORIZATION = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.CategorizationImpl
	 * <em>Categorization</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.CategorizationImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getCategorization()
	 * @generated
	 */
	int CATEGORIZATION = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.ViewImpl <em>View</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getView()
	 * @generated
	 */
	int VIEW = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.CategoryImpl <em>Category</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.CategoryImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getCategory()
	 * @generated
	 */
	int CATEGORY = 6;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.RenderableImpl <em>Renderable</em>}'
	 * class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.RenderableImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getRenderable()
	 * @generated
	 */
	int RENDERABLE = 0;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RENDERABLE__VISIBLE = 0;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RENDERABLE__ENABLED = 1;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RENDERABLE__READONLY = 2;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RENDERABLE__DIAGNOSTIC = 3;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RENDERABLE__ATTACHMENTS = 4;

	/**
	 * The number of structural features of the '<em>Renderable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RENDERABLE_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.CompositeImpl <em>Composite</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.CompositeImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getComposite()
	 * @generated
	 */
	int COMPOSITE = 8;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.ControlImpl <em>Control</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.ControlImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getControl()
	 * @generated
	 */
	int CONTROL = 11;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.CustomCompositeImpl
	 * <em>Custom Composite</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.CustomCompositeImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getCustomComposite()
	 * @generated
	 */
	int CUSTOM_COMPOSITE = 12;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.CompositeCollectionImpl
	 * <em>Composite Collection</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.CompositeCollectionImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getCompositeCollection()
	 * @generated
	 */
	int COMPOSITE_COLLECTION = 13;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.ColumnCompositeImpl
	 * <em>Column Composite</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.ColumnCompositeImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getColumnComposite()
	 * @generated
	 */
	int COLUMN_COMPOSITE = 14;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.ColumnImpl <em>Column</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.ColumnImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getColumn()
	 * @generated
	 */
	int COLUMN = 15;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.ActionImpl <em>Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.ActionImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getAction()
	 * @generated
	 */
	int ACTION = 7;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.VDomainModelReference
	 * <em>VDomain Model Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.VDomainModelReference
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getVDomainModelReference()
	 * @generated
	 */
	int VDOMAIN_MODEL_REFERENCE = 9;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.VFeaturePathDomainModelReferenceImpl
	 * <em>VFeature Path Domain Model Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.VFeaturePathDomainModelReferenceImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getVFeaturePathDomainModelReference()
	 * @generated
	 */
	int VFEATURE_PATH_DOMAIN_MODEL_REFERENCE = 10;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.AttachmentImpl <em>Attachment</em>}'
	 * class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.AttachmentImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getAttachment()
	 * @generated
	 */
	int ATTACHMENT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.VDiagnosticImpl <em>VDiagnostic</em>}'
	 * class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.VDiagnosticImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getVDiagnostic()
	 * @generated
	 */
	int VDIAGNOSTIC = 1;

	/**
	 * The feature id for the '<em><b>Diagnostics</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VDIAGNOSTIC__DIAGNOSTICS = 0;

	/**
	 * The number of structural features of the '<em>VDiagnostic</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VDIAGNOSTIC_FEATURE_COUNT = 1;

	/**
	 * The number of structural features of the '<em>Attachment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTACHMENT_FEATURE_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CATEGORIZATION__VISIBLE = RENDERABLE__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CATEGORIZATION__ENABLED = RENDERABLE__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CATEGORIZATION__READONLY = RENDERABLE__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CATEGORIZATION__DIAGNOSTIC = RENDERABLE__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CATEGORIZATION__ATTACHMENTS = RENDERABLE__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CATEGORIZATION__NAME = RENDERABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Actions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CATEGORIZATION__ACTIONS = RENDERABLE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Abstract Categorization</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CATEGORIZATION_FEATURE_COUNT = RENDERABLE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORIZATION__VISIBLE = ABSTRACT_CATEGORIZATION__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORIZATION__ENABLED = ABSTRACT_CATEGORIZATION__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORIZATION__READONLY = ABSTRACT_CATEGORIZATION__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORIZATION__DIAGNOSTIC = ABSTRACT_CATEGORIZATION__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORIZATION__ATTACHMENTS = ABSTRACT_CATEGORIZATION__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORIZATION__NAME = ABSTRACT_CATEGORIZATION__NAME;

	/**
	 * The feature id for the '<em><b>Actions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORIZATION__ACTIONS = ABSTRACT_CATEGORIZATION__ACTIONS;

	/**
	 * The feature id for the '<em><b>Categorizations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORIZATION__CATEGORIZATIONS = ABSTRACT_CATEGORIZATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Categorization</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORIZATION_FEATURE_COUNT = ABSTRACT_CATEGORIZATION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__VISIBLE = CATEGORIZATION__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__ENABLED = CATEGORIZATION__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__READONLY = CATEGORIZATION__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__DIAGNOSTIC = CATEGORIZATION__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__ATTACHMENTS = CATEGORIZATION__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__NAME = CATEGORIZATION__NAME;

	/**
	 * The feature id for the '<em><b>Actions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__ACTIONS = CATEGORIZATION__ACTIONS;

	/**
	 * The feature id for the '<em><b>Categorizations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__CATEGORIZATIONS = CATEGORIZATION__CATEGORIZATIONS;

	/**
	 * The feature id for the '<em><b>Root EClass</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__ROOT_ECLASS = CATEGORIZATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__CHILDREN = CATEGORIZATION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>View</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW_FEATURE_COUNT = CATEGORIZATION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORY__VISIBLE = ABSTRACT_CATEGORIZATION__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORY__ENABLED = ABSTRACT_CATEGORIZATION__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORY__READONLY = ABSTRACT_CATEGORIZATION__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORY__DIAGNOSTIC = ABSTRACT_CATEGORIZATION__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORY__ATTACHMENTS = ABSTRACT_CATEGORIZATION__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORY__NAME = ABSTRACT_CATEGORIZATION__NAME;

	/**
	 * The feature id for the '<em><b>Actions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORY__ACTIONS = ABSTRACT_CATEGORIZATION__ACTIONS;

	/**
	 * The feature id for the '<em><b>Composite</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORY__COMPOSITE = ABSTRACT_CATEGORIZATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Category</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORY_FEATURE_COUNT = ABSTRACT_CATEGORIZATION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Bundle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACTION__BUNDLE = 0;

	/**
	 * The feature id for the '<em><b>Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACTION__CLASS_NAME = 1;

	/**
	 * The number of structural features of the '<em>Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACTION_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSITE__VISIBLE = RENDERABLE__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSITE__ENABLED = RENDERABLE__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSITE__READONLY = RENDERABLE__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSITE__DIAGNOSTIC = RENDERABLE__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSITE__ATTACHMENTS = RENDERABLE__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSITE__NAME = RENDERABLE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Composite</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_FEATURE_COUNT = RENDERABLE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>VDomain Model Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VDOMAIN_MODEL_REFERENCE_FEATURE_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Domain Model EFeature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VFEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EFEATURE = VDOMAIN_MODEL_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Domain Model EReference Path</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VFEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EREFERENCE_PATH = VDOMAIN_MODEL_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>VFeature Path Domain Model Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VFEATURE_PATH_DOMAIN_MODEL_REFERENCE_FEATURE_COUNT = VDOMAIN_MODEL_REFERENCE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL__VISIBLE = COMPOSITE__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL__ENABLED = COMPOSITE__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL__READONLY = COMPOSITE__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL__DIAGNOSTIC = COMPOSITE__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL__ATTACHMENTS = COMPOSITE__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL__NAME = COMPOSITE__NAME;

	/**
	 * The feature id for the '<em><b>Label Alignment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL__LABEL_ALIGNMENT = COMPOSITE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Domain Model Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL__DOMAIN_MODEL_REFERENCE = COMPOSITE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Control</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL_FEATURE_COUNT = COMPOSITE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_COMPOSITE__VISIBLE = COMPOSITE__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_COMPOSITE__ENABLED = COMPOSITE__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_COMPOSITE__READONLY = COMPOSITE__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_COMPOSITE__DIAGNOSTIC = COMPOSITE__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_COMPOSITE__ATTACHMENTS = COMPOSITE__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_COMPOSITE__NAME = COMPOSITE__NAME;

	/**
	 * The feature id for the '<em><b>Bundle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_COMPOSITE__BUNDLE = COMPOSITE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_COMPOSITE__CLASS_NAME = COMPOSITE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Custom Composite</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CUSTOM_COMPOSITE_FEATURE_COUNT = COMPOSITE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_COLLECTION__VISIBLE = COMPOSITE__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_COLLECTION__ENABLED = COMPOSITE__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_COLLECTION__READONLY = COMPOSITE__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_COLLECTION__DIAGNOSTIC = COMPOSITE__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_COLLECTION__ATTACHMENTS = COMPOSITE__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_COLLECTION__NAME = COMPOSITE__NAME;

	/**
	 * The feature id for the '<em><b>Composites</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_COLLECTION__COMPOSITES = COMPOSITE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Composite Collection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COMPOSITE_COLLECTION_FEATURE_COUNT = COMPOSITE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLUMN_COMPOSITE__VISIBLE = COMPOSITE_COLLECTION__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLUMN_COMPOSITE__ENABLED = COMPOSITE_COLLECTION__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLUMN_COMPOSITE__READONLY = COMPOSITE_COLLECTION__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLUMN_COMPOSITE__DIAGNOSTIC = COMPOSITE_COLLECTION__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLUMN_COMPOSITE__ATTACHMENTS = COMPOSITE_COLLECTION__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLUMN_COMPOSITE__NAME = COMPOSITE_COLLECTION__NAME;

	/**
	 * The feature id for the '<em><b>Composites</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLUMN_COMPOSITE__COMPOSITES = COMPOSITE_COLLECTION__COMPOSITES;

	/**
	 * The number of structural features of the '<em>Column Composite</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLUMN_COMPOSITE_FEATURE_COUNT = COMPOSITE_COLLECTION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLUMN__VISIBLE = COMPOSITE_COLLECTION__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLUMN__ENABLED = COMPOSITE_COLLECTION__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLUMN__READONLY = COMPOSITE_COLLECTION__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLUMN__DIAGNOSTIC = COMPOSITE_COLLECTION__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLUMN__ATTACHMENTS = COMPOSITE_COLLECTION__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLUMN__NAME = COMPOSITE_COLLECTION__NAME;

	/**
	 * The feature id for the '<em><b>Composites</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLUMN__COMPOSITES = COMPOSITE_COLLECTION__COMPOSITES;

	/**
	 * The number of structural features of the '<em>Column</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLUMN_FEATURE_COUNT = COMPOSITE_COLLECTION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.Alignment <em>Alignment</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.Alignment
	 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getAlignment()
	 * @generated
	 */
	int ALIGNMENT = 16;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.View <em>View</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>View</em>'.
	 * @see org.eclipse.emf.ecp.view.model.View
	 * @generated
	 */
	EClass getView();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.ecp.view.model.View#getRootEClass
	 * <em>Root EClass</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Root EClass</em>'.
	 * @see org.eclipse.emf.ecp.view.model.View#getRootEClass()
	 * @see #getView()
	 * @generated
	 */
	EReference getView_RootEClass();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.ecp.view.model.View#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.eclipse.emf.ecp.view.model.View#getChildren()
	 * @see #getView()
	 * @generated
	 */
	EReference getView_Children();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.AbstractCategorization
	 * <em>Abstract Categorization</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Abstract Categorization</em>'.
	 * @see org.eclipse.emf.ecp.view.model.AbstractCategorization
	 * @generated
	 */
	EClass getAbstractCategorization();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.AbstractCategorization#getName
	 * <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.emf.ecp.view.model.AbstractCategorization#getName()
	 * @see #getAbstractCategorization()
	 * @generated
	 */
	EAttribute getAbstractCategorization_Name();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.ecp.view.model.AbstractCategorization#getActions <em>Actions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Actions</em>'.
	 * @see org.eclipse.emf.ecp.view.model.AbstractCategorization#getActions()
	 * @see #getAbstractCategorization()
	 * @generated
	 */
	EReference getAbstractCategorization_Actions();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.Categorization <em>Categorization</em>}
	 * '.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Categorization</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Categorization
	 * @generated
	 */
	EClass getCategorization();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.ecp.view.model.Categorization#getCategorizations <em>Categorizations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Categorizations</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Categorization#getCategorizations()
	 * @see #getCategorization()
	 * @generated
	 */
	EReference getCategorization_Categorizations();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.Category <em>Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Category</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Category
	 * @generated
	 */
	EClass getCategory();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link org.eclipse.emf.ecp.view.model.Category#getComposite <em>Composite</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Composite</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Category#getComposite()
	 * @see #getCategory()
	 * @generated
	 */
	EReference getCategory_Composite();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.Composite <em>Composite</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Composite</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Composite
	 * @generated
	 */
	EClass getComposite();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.Composite#getName <em>Name</em>}
	 * '.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Composite#getName()
	 * @see #getComposite()
	 * @generated
	 */
	EAttribute getComposite_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.Control <em>Control</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Control</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Control
	 * @generated
	 */
	EClass getControl();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.Control#getLabelAlignment
	 * <em>Label Alignment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Label Alignment</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Control#getLabelAlignment()
	 * @see #getControl()
	 * @generated
	 */
	EAttribute getControl_LabelAlignment();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link org.eclipse.emf.ecp.view.model.Control#getDomainModelReference <em>Domain Model Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Domain Model Reference</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Control#getDomainModelReference()
	 * @see #getControl()
	 * @generated
	 */
	EReference getControl_DomainModelReference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.CustomComposite
	 * <em>Custom Composite</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Custom Composite</em>'.
	 * @see org.eclipse.emf.ecp.view.model.CustomComposite
	 * @generated
	 */
	EClass getCustomComposite();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.CustomComposite#getBundle
	 * <em>Bundle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Bundle</em>'.
	 * @see org.eclipse.emf.ecp.view.model.CustomComposite#getBundle()
	 * @see #getCustomComposite()
	 * @generated
	 */
	EAttribute getCustomComposite_Bundle();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.CustomComposite#getClassName
	 * <em>Class Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Class Name</em>'.
	 * @see org.eclipse.emf.ecp.view.model.CustomComposite#getClassName()
	 * @see #getCustomComposite()
	 * @generated
	 */
	EAttribute getCustomComposite_ClassName();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.CompositeCollection
	 * <em>Composite Collection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Composite Collection</em>'.
	 * @see org.eclipse.emf.ecp.view.model.CompositeCollection
	 * @generated
	 */
	EClass getCompositeCollection();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.ecp.view.model.CompositeCollection#getComposites <em>Composites</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Composites</em>'.
	 * @see org.eclipse.emf.ecp.view.model.CompositeCollection#getComposites()
	 * @see #getCompositeCollection()
	 * @generated
	 */
	EReference getCompositeCollection_Composites();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.ColumnComposite
	 * <em>Column Composite</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Column Composite</em>'.
	 * @see org.eclipse.emf.ecp.view.model.ColumnComposite
	 * @generated
	 */
	EClass getColumnComposite();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.Column <em>Column</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Column</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Column
	 * @generated
	 */
	EClass getColumn();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.Renderable <em>Renderable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Renderable</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Renderable
	 * @generated
	 */
	EClass getRenderable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.Renderable#isVisible
	 * <em>Visible</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Visible</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Renderable#isVisible()
	 * @see #getRenderable()
	 * @generated
	 */
	EAttribute getRenderable_Visible();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.Renderable#isEnabled
	 * <em>Enabled</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Enabled</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Renderable#isEnabled()
	 * @see #getRenderable()
	 * @generated
	 */
	EAttribute getRenderable_Enabled();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.Renderable#isReadonly
	 * <em>Readonly</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Readonly</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Renderable#isReadonly()
	 * @see #getRenderable()
	 * @generated
	 */
	EAttribute getRenderable_Readonly();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link org.eclipse.emf.ecp.view.model.Renderable#getDiagnostic <em>Diagnostic</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Diagnostic</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Renderable#getDiagnostic()
	 * @see #getRenderable()
	 * @generated
	 */
	EReference getRenderable_Diagnostic();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.ecp.view.model.Renderable#getAttachments <em>Attachments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Attachments</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Renderable#getAttachments()
	 * @see #getRenderable()
	 * @generated
	 */
	EReference getRenderable_Attachments();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.Action <em>Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Action</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Action
	 * @generated
	 */
	EClass getAction();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.Action#getBundle
	 * <em>Bundle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Bundle</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Action#getBundle()
	 * @see #getAction()
	 * @generated
	 */
	EAttribute getAction_Bundle();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.Action#getClassName
	 * <em>Class Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Class Name</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Action#getClassName()
	 * @see #getAction()
	 * @generated
	 */
	EAttribute getAction_ClassName();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.VDomainModelReference
	 * <em>VDomain Model Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>VDomain Model Reference</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VDomainModelReference
	 * @generated
	 */
	EClass getVDomainModelReference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference
	 * <em>VFeature Path Domain Model Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>VFeature Path Domain Model Reference</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference
	 * @generated
	 */
	EClass getVFeaturePathDomainModelReference();

	/**
	 * Returns the meta object for the reference '
	 * {@link org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference#getDomainModelEFeature
	 * <em>Domain Model EFeature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Domain Model EFeature</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference#getDomainModelEFeature()
	 * @see #getVFeaturePathDomainModelReference()
	 * @generated
	 */
	EReference getVFeaturePathDomainModelReference_DomainModelEFeature();

	/**
	 * Returns the meta object for the reference list '
	 * {@link org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference#getDomainModelEReferencePath
	 * <em>Domain Model EReference Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Domain Model EReference Path</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference#getDomainModelEReferencePath()
	 * @see #getVFeaturePathDomainModelReference()
	 * @generated
	 */
	EReference getVFeaturePathDomainModelReference_DomainModelEReferencePath();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.Attachment <em>Attachment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Attachment</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Attachment
	 * @generated
	 */
	EClass getAttachment();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.VDiagnostic <em>VDiagnostic</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>VDiagnostic</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VDiagnostic
	 * @generated
	 */
	EClass getVDiagnostic();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.emf.ecp.view.model.VDiagnostic#getDiagnostics
	 * <em>Diagnostics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute list '<em>Diagnostics</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VDiagnostic#getDiagnostics()
	 * @see #getVDiagnostic()
	 * @generated
	 */
	EAttribute getVDiagnostic_Diagnostics();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.emf.ecp.view.model.Alignment <em>Alignment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Alignment</em>'.
	 * @see org.eclipse.emf.ecp.view.model.Alignment
	 * @generated
	 */
	EEnum getAlignment();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ViewFactory getViewFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.ViewImpl <em>View</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getView()
		 * @generated
		 */
		EClass VIEW = eINSTANCE.getView();

		/**
		 * The meta object literal for the '<em><b>Root EClass</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VIEW__ROOT_ECLASS = eINSTANCE.getView_RootEClass();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VIEW__CHILDREN = eINSTANCE.getView_Children();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.AbstractCategorizationImpl
		 * <em>Abstract Categorization</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.AbstractCategorizationImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getAbstractCategorization()
		 * @generated
		 */
		EClass ABSTRACT_CATEGORIZATION = eINSTANCE.getAbstractCategorization();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ABSTRACT_CATEGORIZATION__NAME = eINSTANCE.getAbstractCategorization_Name();

		/**
		 * The meta object literal for the '<em><b>Actions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ABSTRACT_CATEGORIZATION__ACTIONS = eINSTANCE.getAbstractCategorization_Actions();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.CategorizationImpl
		 * <em>Categorization</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.CategorizationImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getCategorization()
		 * @generated
		 */
		EClass CATEGORIZATION = eINSTANCE.getCategorization();

		/**
		 * The meta object literal for the '<em><b>Categorizations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CATEGORIZATION__CATEGORIZATIONS = eINSTANCE.getCategorization_Categorizations();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.CategoryImpl <em>Category</em>}'
		 * class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.CategoryImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getCategory()
		 * @generated
		 */
		EClass CATEGORY = eINSTANCE.getCategory();

		/**
		 * The meta object literal for the '<em><b>Composite</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CATEGORY__COMPOSITE = eINSTANCE.getCategory_Composite();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.CompositeImpl <em>Composite</em>}
		 * ' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.CompositeImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getComposite()
		 * @generated
		 */
		EClass COMPOSITE = eINSTANCE.getComposite();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute COMPOSITE__NAME = eINSTANCE.getComposite_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.ControlImpl <em>Control</em>}'
		 * class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.ControlImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getControl()
		 * @generated
		 */
		EClass CONTROL = eINSTANCE.getControl();

		/**
		 * The meta object literal for the '<em><b>Label Alignment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CONTROL__LABEL_ALIGNMENT = eINSTANCE.getControl_LabelAlignment();

		/**
		 * The meta object literal for the '<em><b>Domain Model Reference</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CONTROL__DOMAIN_MODEL_REFERENCE = eINSTANCE.getControl_DomainModelReference();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.CustomCompositeImpl
		 * <em>Custom Composite</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.CustomCompositeImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getCustomComposite()
		 * @generated
		 */
		EClass CUSTOM_COMPOSITE = eINSTANCE.getCustomComposite();

		/**
		 * The meta object literal for the '<em><b>Bundle</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CUSTOM_COMPOSITE__BUNDLE = eINSTANCE.getCustomComposite_Bundle();

		/**
		 * The meta object literal for the '<em><b>Class Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CUSTOM_COMPOSITE__CLASS_NAME = eINSTANCE.getCustomComposite_ClassName();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.CompositeCollectionImpl
		 * <em>Composite Collection</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.CompositeCollectionImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getCompositeCollection()
		 * @generated
		 */
		EClass COMPOSITE_COLLECTION = eINSTANCE.getCompositeCollection();

		/**
		 * The meta object literal for the '<em><b>Composites</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference COMPOSITE_COLLECTION__COMPOSITES = eINSTANCE.getCompositeCollection_Composites();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.ColumnCompositeImpl
		 * <em>Column Composite</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.ColumnCompositeImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getColumnComposite()
		 * @generated
		 */
		EClass COLUMN_COMPOSITE = eINSTANCE.getColumnComposite();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.ColumnImpl <em>Column</em>}'
		 * class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.ColumnImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getColumn()
		 * @generated
		 */
		EClass COLUMN = eINSTANCE.getColumn();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.RenderableImpl
		 * <em>Renderable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.RenderableImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getRenderable()
		 * @generated
		 */
		EClass RENDERABLE = eINSTANCE.getRenderable();

		/**
		 * The meta object literal for the '<em><b>Visible</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RENDERABLE__VISIBLE = eINSTANCE.getRenderable_Visible();

		/**
		 * The meta object literal for the '<em><b>Enabled</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RENDERABLE__ENABLED = eINSTANCE.getRenderable_Enabled();

		/**
		 * The meta object literal for the '<em><b>Readonly</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute RENDERABLE__READONLY = eINSTANCE.getRenderable_Readonly();

		/**
		 * The meta object literal for the '<em><b>Diagnostic</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference RENDERABLE__DIAGNOSTIC = eINSTANCE.getRenderable_Diagnostic();

		/**
		 * The meta object literal for the '<em><b>Attachments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference RENDERABLE__ATTACHMENTS = eINSTANCE.getRenderable_Attachments();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.ActionImpl <em>Action</em>}'
		 * class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.ActionImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getAction()
		 * @generated
		 */
		EClass ACTION = eINSTANCE.getAction();

		/**
		 * The meta object literal for the '<em><b>Bundle</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ACTION__BUNDLE = eINSTANCE.getAction_Bundle();

		/**
		 * The meta object literal for the '<em><b>Class Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ACTION__CLASS_NAME = eINSTANCE.getAction_ClassName();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.VDomainModelReference
		 * <em>VDomain Model Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.VDomainModelReference
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getVDomainModelReference()
		 * @generated
		 */
		EClass VDOMAIN_MODEL_REFERENCE = eINSTANCE.getVDomainModelReference();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.ecp.view.model.impl.VFeaturePathDomainModelReferenceImpl
		 * <em>VFeature Path Domain Model Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.VFeaturePathDomainModelReferenceImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getVFeaturePathDomainModelReference()
		 * @generated
		 */
		EClass VFEATURE_PATH_DOMAIN_MODEL_REFERENCE = eINSTANCE.getVFeaturePathDomainModelReference();

		/**
		 * The meta object literal for the '<em><b>Domain Model EFeature</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VFEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EFEATURE = eINSTANCE
			.getVFeaturePathDomainModelReference_DomainModelEFeature();

		/**
		 * The meta object literal for the '<em><b>Domain Model EReference Path</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VFEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EREFERENCE_PATH = eINSTANCE
			.getVFeaturePathDomainModelReference_DomainModelEReferencePath();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.AttachmentImpl
		 * <em>Attachment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.AttachmentImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getAttachment()
		 * @generated
		 */
		EClass ATTACHMENT = eINSTANCE.getAttachment();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.VDiagnosticImpl
		 * <em>VDiagnostic</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.VDiagnosticImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getVDiagnostic()
		 * @generated
		 */
		EClass VDIAGNOSTIC = eINSTANCE.getVDiagnostic();

		/**
		 * The meta object literal for the '<em><b>Diagnostics</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VDIAGNOSTIC__DIAGNOSTICS = eINSTANCE.getVDiagnostic_Diagnostics();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.Alignment <em>Alignment</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.Alignment
		 * @see org.eclipse.emf.ecp.view.model.impl.ViewPackageImpl#getAlignment()
		 * @generated
		 */
		EEnum ALIGNMENT = eINSTANCE.getAlignment();

	}

} // ViewPackage
