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
 * @see org.eclipse.emf.ecp.view.model.VViewFactory
 * @model kind="package"
 * @generated
 */
public interface VViewPackage extends EPackage {
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
	VViewPackage eINSTANCE = org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.VElementImpl <em>Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.VElementImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getElement()
	 * @generated
	 */
	int ELEMENT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.VDiagnosticImpl <em>Diagnostic</em>}'
	 * class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.VDiagnosticImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getDiagnostic()
	 * @generated
	 */
	int DIAGNOSTIC = 0;

	/**
	 * The feature id for the '<em><b>Diagnostics</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGNOSTIC__DIAGNOSTICS = 0;

	/**
	 * The number of structural features of the '<em>Diagnostic</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGNOSTIC_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.VAbstractCategorizationImpl
	 * <em>Abstract Categorization</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.VAbstractCategorizationImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getAbstractCategorization()
	 * @generated
	 */
	int ABSTRACT_CATEGORIZATION = 9;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.VCategorizationImpl
	 * <em>Categorization</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.VCategorizationImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getCategorization()
	 * @generated
	 */
	int CATEGORIZATION = 10;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.VViewImpl <em>View</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.VViewImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getView()
	 * @generated
	 */
	int VIEW = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.VCategoryImpl <em>Category</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.VCategoryImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getCategory()
	 * @generated
	 */
	int CATEGORY = 11;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.VControlImpl <em>Control</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.VControlImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getControl()
	 * @generated
	 */
	int CONTROL = 8;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.VActionImpl <em>Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.VActionImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getAction()
	 * @generated
	 */
	int ACTION = 12;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.VAttachmentImpl <em>Attachment</em>}'
	 * class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.VAttachmentImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getAttachment()
	 * @generated
	 */
	int ATTACHMENT = 1;

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
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.VDomainModelReference
	 * <em>Domain Model Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.VDomainModelReference
	 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getDomainModelReference()
	 * @generated
	 */
	int DOMAIN_MODEL_REFERENCE = 2;

	/**
	 * The number of structural features of the '<em>Domain Model Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOMAIN_MODEL_REFERENCE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.VFeaturePathDomainModelReferenceImpl
	 * <em>Feature Path Domain Model Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.VFeaturePathDomainModelReferenceImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getFeaturePathDomainModelReference()
	 * @generated
	 */
	int FEATURE_PATH_DOMAIN_MODEL_REFERENCE = 3;

	/**
	 * The feature id for the '<em><b>Domain Model EFeature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EFEATURE = DOMAIN_MODEL_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Domain Model EReference Path</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EREFERENCE_PATH = DOMAIN_MODEL_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Feature Path Domain Model Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FEATURE_PATH_DOMAIN_MODEL_REFERENCE_FEATURE_COUNT = DOMAIN_MODEL_REFERENCE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ELEMENT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ELEMENT__VISIBLE = 1;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ELEMENT__ENABLED = 2;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ELEMENT__READONLY = 3;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ELEMENT__DIAGNOSTIC = 4;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ELEMENT__ATTACHMENTS = 5;

	/**
	 * The number of structural features of the '<em>Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ELEMENT_FEATURE_COUNT = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__NAME = ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__VISIBLE = ELEMENT__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__ENABLED = ELEMENT__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__READONLY = ELEMENT__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__DIAGNOSTIC = ELEMENT__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__ATTACHMENTS = ELEMENT__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Root EClass</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__ROOT_ECLASS = ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__CHILDREN = ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Categorizations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW__CATEGORIZATIONS = ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>View</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VIEW_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.VContainedElementImpl
	 * <em>Contained Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.VContainedElementImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getContainedElement()
	 * @generated
	 */
	int CONTAINED_ELEMENT = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTAINED_ELEMENT__NAME = ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTAINED_ELEMENT__VISIBLE = ELEMENT__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTAINED_ELEMENT__ENABLED = ELEMENT__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTAINED_ELEMENT__READONLY = ELEMENT__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTAINED_ELEMENT__DIAGNOSTIC = ELEMENT__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTAINED_ELEMENT__ATTACHMENTS = ELEMENT__ATTACHMENTS;

	/**
	 * The number of structural features of the '<em>Contained Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTAINED_ELEMENT_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.impl.VContainerImpl <em>Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.impl.VContainerImpl
	 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getContainer()
	 * @generated
	 */
	int CONTAINER = 7;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTAINER__NAME = CONTAINED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTAINER__VISIBLE = CONTAINED_ELEMENT__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTAINER__ENABLED = CONTAINED_ELEMENT__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTAINER__READONLY = CONTAINED_ELEMENT__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTAINER__DIAGNOSTIC = CONTAINED_ELEMENT__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTAINER__ATTACHMENTS = CONTAINED_ELEMENT__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTAINER__CHILDREN = CONTAINED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTAINER_FEATURE_COUNT = CONTAINED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL__NAME = CONTAINED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL__VISIBLE = CONTAINED_ELEMENT__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL__ENABLED = CONTAINED_ELEMENT__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL__READONLY = CONTAINED_ELEMENT__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL__DIAGNOSTIC = CONTAINED_ELEMENT__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL__ATTACHMENTS = CONTAINED_ELEMENT__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Label Alignment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL__LABEL_ALIGNMENT = CONTAINED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Domain Model Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL__DOMAIN_MODEL_REFERENCE = CONTAINED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Control</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CONTROL_FEATURE_COUNT = CONTAINED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CATEGORIZATION__NAME = ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CATEGORIZATION__VISIBLE = ELEMENT__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CATEGORIZATION__ENABLED = ELEMENT__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CATEGORIZATION__READONLY = ELEMENT__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CATEGORIZATION__DIAGNOSTIC = ELEMENT__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CATEGORIZATION__ATTACHMENTS = ELEMENT__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Actions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CATEGORIZATION__ACTIONS = ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Abstract Categorization</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_CATEGORIZATION_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 1;

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
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CATEGORY__NAME = ABSTRACT_CATEGORIZATION__NAME;

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
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.model.LabelAlignment <em>Label Alignment</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecp.view.model.LabelAlignment
	 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getLabelAlignment()
	 * @generated
	 */
	int LABEL_ALIGNMENT = 13;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.VElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Element</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VElement
	 * @generated
	 */
	EClass getElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.VElement#getName <em>Name</em>}
	 * '.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VElement#getName()
	 * @see #getElement()
	 * @generated
	 */
	EAttribute getElement_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.VElement#isVisible
	 * <em>Visible</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Visible</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VElement#isVisible()
	 * @see #getElement()
	 * @generated
	 */
	EAttribute getElement_Visible();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.VElement#isEnabled
	 * <em>Enabled</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Enabled</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VElement#isEnabled()
	 * @see #getElement()
	 * @generated
	 */
	EAttribute getElement_Enabled();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.VElement#isReadonly
	 * <em>Readonly</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Readonly</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VElement#isReadonly()
	 * @see #getElement()
	 * @generated
	 */
	EAttribute getElement_Readonly();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link org.eclipse.emf.ecp.view.model.VElement#getDiagnostic <em>Diagnostic</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Diagnostic</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VElement#getDiagnostic()
	 * @see #getElement()
	 * @generated
	 */
	EReference getElement_Diagnostic();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.ecp.view.model.VElement#getAttachments <em>Attachments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Attachments</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VElement#getAttachments()
	 * @see #getElement()
	 * @generated
	 */
	EReference getElement_Attachments();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.VDiagnostic <em>Diagnostic</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Diagnostic</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VDiagnostic
	 * @generated
	 */
	EClass getDiagnostic();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.emf.ecp.view.model.VDiagnostic#getDiagnostics
	 * <em>Diagnostics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute list '<em>Diagnostics</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VDiagnostic#getDiagnostics()
	 * @see #getDiagnostic()
	 * @generated
	 */
	EAttribute getDiagnostic_Diagnostics();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.VView <em>View</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>View</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VView
	 * @generated
	 */
	EClass getView();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.ecp.view.model.VView#getRootEClass
	 * <em>Root EClass</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Root EClass</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VView#getRootEClass()
	 * @see #getView()
	 * @generated
	 */
	EReference getView_RootEClass();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.ecp.view.model.VView#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VView#getChildren()
	 * @see #getView()
	 * @generated
	 */
	EReference getView_Children();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.ecp.view.model.VView#getCategorizations <em>Categorizations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Categorizations</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VView#getCategorizations()
	 * @see #getView()
	 * @generated
	 */
	EReference getView_Categorizations();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.VContainedElement
	 * <em>Contained Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Contained Element</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VContainedElement
	 * @generated
	 */
	EClass getContainedElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.VAbstractCategorization
	 * <em>Abstract Categorization</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Abstract Categorization</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VAbstractCategorization
	 * @generated
	 */
	EClass getAbstractCategorization();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.ecp.view.model.VAbstractCategorization#getActions <em>Actions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Actions</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VAbstractCategorization#getActions()
	 * @see #getAbstractCategorization()
	 * @generated
	 */
	EReference getAbstractCategorization_Actions();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.VCategorization <em>Categorization</em>}
	 * '.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Categorization</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VCategorization
	 * @generated
	 */
	EClass getCategorization();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.ecp.view.model.VCategorization#getCategorizations <em>Categorizations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Categorizations</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VCategorization#getCategorizations()
	 * @see #getCategorization()
	 * @generated
	 */
	EReference getCategorization_Categorizations();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.VCategory <em>Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Category</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VCategory
	 * @generated
	 */
	EClass getCategory();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link org.eclipse.emf.ecp.view.model.VCategory#getComposite <em>Composite</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Composite</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VCategory#getComposite()
	 * @see #getCategory()
	 * @generated
	 */
	EReference getCategory_Composite();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.VControl <em>Control</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Control</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VControl
	 * @generated
	 */
	EClass getControl();

	/**
	 * Returns the meta object for the containment reference '
	 * {@link org.eclipse.emf.ecp.view.model.VControl#getDomainModelReference <em>Domain Model Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference '<em>Domain Model Reference</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VControl#getDomainModelReference()
	 * @see #getControl()
	 * @generated
	 */
	EReference getControl_DomainModelReference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.VContainer <em>Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Container</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VContainer
	 * @generated
	 */
	EClass getContainer();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.emf.ecp.view.model.VContainer#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VContainer#getChildren()
	 * @see #getContainer()
	 * @generated
	 */
	EReference getContainer_Children();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.VControl#getLabelAlignment
	 * <em>Label Alignment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Label Alignment</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VControl#getLabelAlignment()
	 * @see #getControl()
	 * @generated
	 */
	EAttribute getControl_LabelAlignment();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.VAction <em>Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Action</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VAction
	 * @generated
	 */
	EClass getAction();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.VAction#getBundle
	 * <em>Bundle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Bundle</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VAction#getBundle()
	 * @see #getAction()
	 * @generated
	 */
	EAttribute getAction_Bundle();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.model.VAction#getClassName
	 * <em>Class Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Class Name</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VAction#getClassName()
	 * @see #getAction()
	 * @generated
	 */
	EAttribute getAction_ClassName();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.emf.ecp.view.model.LabelAlignment <em>Label Alignment</em>}
	 * '.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Label Alignment</em>'.
	 * @see org.eclipse.emf.ecp.view.model.LabelAlignment
	 * @generated
	 */
	EEnum getLabelAlignment();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.VDomainModelReference
	 * <em>Domain Model Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Domain Model Reference</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VDomainModelReference
	 * @generated
	 */
	EClass getDomainModelReference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference
	 * <em>Feature Path Domain Model Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Feature Path Domain Model Reference</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference
	 * @generated
	 */
	EClass getFeaturePathDomainModelReference();

	/**
	 * Returns the meta object for the reference '
	 * {@link org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference#getDomainModelEFeature
	 * <em>Domain Model EFeature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Domain Model EFeature</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference#getDomainModelEFeature()
	 * @see #getFeaturePathDomainModelReference()
	 * @generated
	 */
	EReference getFeaturePathDomainModelReference_DomainModelEFeature();

	/**
	 * Returns the meta object for the reference list '
	 * {@link org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference#getDomainModelEReferencePath
	 * <em>Domain Model EReference Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Domain Model EReference Path</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference#getDomainModelEReferencePath()
	 * @see #getFeaturePathDomainModelReference()
	 * @generated
	 */
	EReference getFeaturePathDomainModelReference_DomainModelEReferencePath();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.model.VAttachment <em>Attachment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Attachment</em>'.
	 * @see org.eclipse.emf.ecp.view.model.VAttachment
	 * @generated
	 */
	EClass getAttachment();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	VViewFactory getViewFactory();

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
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.VElementImpl <em>Element</em>}'
		 * class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.VElementImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getElement()
		 * @generated
		 */
		EClass ELEMENT = eINSTANCE.getElement();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ELEMENT__NAME = eINSTANCE.getElement_Name();

		/**
		 * The meta object literal for the '<em><b>Visible</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ELEMENT__VISIBLE = eINSTANCE.getElement_Visible();

		/**
		 * The meta object literal for the '<em><b>Enabled</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ELEMENT__ENABLED = eINSTANCE.getElement_Enabled();

		/**
		 * The meta object literal for the '<em><b>Readonly</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ELEMENT__READONLY = eINSTANCE.getElement_Readonly();

		/**
		 * The meta object literal for the '<em><b>Diagnostic</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ELEMENT__DIAGNOSTIC = eINSTANCE.getElement_Diagnostic();

		/**
		 * The meta object literal for the '<em><b>Attachments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ELEMENT__ATTACHMENTS = eINSTANCE.getElement_Attachments();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.VDiagnosticImpl
		 * <em>Diagnostic</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.VDiagnosticImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getDiagnostic()
		 * @generated
		 */
		EClass DIAGNOSTIC = eINSTANCE.getDiagnostic();

		/**
		 * The meta object literal for the '<em><b>Diagnostics</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DIAGNOSTIC__DIAGNOSTICS = eINSTANCE.getDiagnostic_Diagnostics();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.VViewImpl <em>View</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.VViewImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getView()
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
		 * The meta object literal for the '<em><b>Categorizations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference VIEW__CATEGORIZATIONS = eINSTANCE.getView_Categorizations();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.VContainedElementImpl
		 * <em>Contained Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.VContainedElementImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getContainedElement()
		 * @generated
		 */
		EClass CONTAINED_ELEMENT = eINSTANCE.getContainedElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.VAbstractCategorizationImpl
		 * <em>Abstract Categorization</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.VAbstractCategorizationImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getAbstractCategorization()
		 * @generated
		 */
		EClass ABSTRACT_CATEGORIZATION = eINSTANCE.getAbstractCategorization();

		/**
		 * The meta object literal for the '<em><b>Actions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ABSTRACT_CATEGORIZATION__ACTIONS = eINSTANCE.getAbstractCategorization_Actions();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.VCategorizationImpl
		 * <em>Categorization</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.VCategorizationImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getCategorization()
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
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.VCategoryImpl <em>Category</em>}'
		 * class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.VCategoryImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getCategory()
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
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.VControlImpl <em>Control</em>}'
		 * class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.VControlImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getControl()
		 * @generated
		 */
		EClass CONTROL = eINSTANCE.getControl();

		/**
		 * The meta object literal for the '<em><b>Domain Model Reference</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CONTROL__DOMAIN_MODEL_REFERENCE = eINSTANCE.getControl_DomainModelReference();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.VContainerImpl
		 * <em>Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.VContainerImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getContainer()
		 * @generated
		 */
		EClass CONTAINER = eINSTANCE.getContainer();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CONTAINER__CHILDREN = eINSTANCE.getContainer_Children();

		/**
		 * The meta object literal for the '<em><b>Label Alignment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CONTROL__LABEL_ALIGNMENT = eINSTANCE.getControl_LabelAlignment();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.VActionImpl <em>Action</em>}'
		 * class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.VActionImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getAction()
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
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.LabelAlignment
		 * <em>Label Alignment</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.LabelAlignment
		 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getLabelAlignment()
		 * @generated
		 */
		EEnum LABEL_ALIGNMENT = eINSTANCE.getLabelAlignment();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.VDomainModelReference
		 * <em>Domain Model Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.VDomainModelReference
		 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getDomainModelReference()
		 * @generated
		 */
		EClass DOMAIN_MODEL_REFERENCE = eINSTANCE.getDomainModelReference();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.emf.ecp.view.model.impl.VFeaturePathDomainModelReferenceImpl
		 * <em>Feature Path Domain Model Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.VFeaturePathDomainModelReferenceImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getFeaturePathDomainModelReference()
		 * @generated
		 */
		EClass FEATURE_PATH_DOMAIN_MODEL_REFERENCE = eINSTANCE.getFeaturePathDomainModelReference();

		/**
		 * The meta object literal for the '<em><b>Domain Model EFeature</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference FEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EFEATURE = eINSTANCE
			.getFeaturePathDomainModelReference_DomainModelEFeature();

		/**
		 * The meta object literal for the '<em><b>Domain Model EReference Path</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference FEATURE_PATH_DOMAIN_MODEL_REFERENCE__DOMAIN_MODEL_EREFERENCE_PATH = eINSTANCE
			.getFeaturePathDomainModelReference_DomainModelEReferencePath();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.model.impl.VAttachmentImpl
		 * <em>Attachment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.emf.ecp.view.model.impl.VAttachmentImpl
		 * @see org.eclipse.emf.ecp.view.model.impl.VViewPackageImpl#getAttachment()
		 * @generated
		 */
		EClass ATTACHMENT = eINSTANCE.getAttachment();

	}

} // ViewPackage
