/**
 */
package org.eclipse.emf.ecp.view.dynamictree.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.view.model.VViewPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.ecp.view.dynamictree.model.ModelFactory
 * @model kind="package"
 * @generated
 */
public interface ModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://org/eclipse/emf/ecp/view/dynamictree/model";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.emf.ecp.view.dynamictree.model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModelPackage eINSTANCE = org.eclipse.emf.ecp.view.dynamictree.model.impl.ModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.dynamictree.model.impl.DynamicContainmentTreeImpl <em>Dynamic Containment Tree</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.DynamicContainmentTreeImpl
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.ModelPackageImpl#getDynamicContainmentTree()
	 * @generated
	 */
	int DYNAMIC_CONTAINMENT_TREE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_TREE__NAME = VViewPackage.CATEGORY__NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_TREE__VISIBLE = VViewPackage.CATEGORY__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_TREE__ENABLED = VViewPackage.CATEGORY__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_TREE__READONLY = VViewPackage.CATEGORY__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_TREE__DIAGNOSTIC = VViewPackage.CATEGORY__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_TREE__ATTACHMENTS = VViewPackage.CATEGORY__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Actions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_TREE__ACTIONS = VViewPackage.CATEGORY__ACTIONS;

	/**
	 * The feature id for the '<em><b>Composite</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_TREE__COMPOSITE = VViewPackage.CATEGORY__COMPOSITE;

	/**
	 * The feature id for the '<em><b>Domain Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_TREE__DOMAIN_MODEL = VViewPackage.CATEGORY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Child Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_TREE__CHILD_REFERENCE = VViewPackage.CATEGORY_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Path To Root</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_TREE__PATH_TO_ROOT = VViewPackage.CATEGORY_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Child Composite</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_TREE__CHILD_COMPOSITE = VViewPackage.CATEGORY_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Items</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_TREE__ITEMS = VViewPackage.CATEGORY_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Dynamic Containment Tree</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_TREE_FEATURE_COUNT = VViewPackage.CATEGORY_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.dynamictree.model.impl.DynamicContainmentItemImpl <em>Dynamic Containment Item</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.DynamicContainmentItemImpl
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.ModelPackageImpl#getDynamicContainmentItem()
	 * @generated
	 */
	int DYNAMIC_CONTAINMENT_ITEM = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_ITEM__NAME = VViewPackage.ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_ITEM__VISIBLE = VViewPackage.ELEMENT__VISIBLE;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_ITEM__ENABLED = VViewPackage.ELEMENT__ENABLED;

	/**
	 * The feature id for the '<em><b>Readonly</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_ITEM__READONLY = VViewPackage.ELEMENT__READONLY;

	/**
	 * The feature id for the '<em><b>Diagnostic</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_ITEM__DIAGNOSTIC = VViewPackage.ELEMENT__DIAGNOSTIC;

	/**
	 * The feature id for the '<em><b>Attachments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_ITEM__ATTACHMENTS = VViewPackage.ELEMENT__ATTACHMENTS;

	/**
	 * The feature id for the '<em><b>Domain Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_ITEM__DOMAIN_MODEL = VViewPackage.ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Items</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_ITEM__ITEMS = VViewPackage.ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Composite</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_ITEM__COMPOSITE = VViewPackage.ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Dynamic Containment Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DYNAMIC_CONTAINMENT_ITEM_FEATURE_COUNT = VViewPackage.ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.dynamictree.model.impl.TestElementImpl <em>Test Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.TestElementImpl
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.ModelPackageImpl#getTestElement()
	 * @generated
	 */
	int TEST_ELEMENT = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_ELEMENT__ID = EcorePackage.EOBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_ELEMENT__ELEMENTS = EcorePackage.EOBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Parent Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_ELEMENT__PARENT_ID = EcorePackage.EOBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Test Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_ELEMENT_FEATURE_COUNT = EcorePackage.EOBJECT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.dynamictree.model.impl.DomainRootImpl <em>Domain Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.DomainRootImpl
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.ModelPackageImpl#getDomainRoot()
	 * @generated
	 */
	int DOMAIN_ROOT = 3;

	/**
	 * The feature id for the '<em><b>Intermediate</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOMAIN_ROOT__INTERMEDIATE = 0;

	/**
	 * The number of structural features of the '<em>Domain Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOMAIN_ROOT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.dynamictree.model.impl.DomainIntermediateImpl <em>Domain Intermediate</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.DomainIntermediateImpl
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.ModelPackageImpl#getDomainIntermediate()
	 * @generated
	 */
	int DOMAIN_INTERMEDIATE = 4;

	/**
	 * The feature id for the '<em><b>Test Element Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOMAIN_INTERMEDIATE__TEST_ELEMENT_CONTAINER = 0;

	/**
	 * The number of structural features of the '<em>Domain Intermediate</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOMAIN_INTERMEDIATE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.ecp.view.dynamictree.model.impl.TestElementContainerImpl <em>Test Element Container</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.TestElementContainerImpl
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.ModelPackageImpl#getTestElementContainer()
	 * @generated
	 */
	int TEST_ELEMENT_CONTAINER = 5;

	/**
	 * The feature id for the '<em><b>Test Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_ELEMENT_CONTAINER__TEST_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_ELEMENT_CONTAINER__ID = 1;

	/**
	 * The number of structural features of the '<em>Test Element Container</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_ELEMENT_CONTAINER_FEATURE_COUNT = 2;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree <em>Dynamic Containment Tree</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dynamic Containment Tree</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree
	 * @generated
	 */
	EClass getDynamicContainmentTree();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree#getDomainModel <em>Domain Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Domain Model</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree#getDomainModel()
	 * @see #getDynamicContainmentTree()
	 * @generated
	 */
	EReference getDynamicContainmentTree_DomainModel();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree#getChildReference <em>Child Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Child Reference</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree#getChildReference()
	 * @see #getDynamicContainmentTree()
	 * @generated
	 */
	EReference getDynamicContainmentTree_ChildReference();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree#getPathToRoot <em>Path To Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Path To Root</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree#getPathToRoot()
	 * @see #getDynamicContainmentTree()
	 * @generated
	 */
	EReference getDynamicContainmentTree_PathToRoot();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree#getChildComposite <em>Child Composite</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Child Composite</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree#getChildComposite()
	 * @see #getDynamicContainmentTree()
	 * @generated
	 */
	EReference getDynamicContainmentTree_ChildComposite();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree#getItems <em>Items</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Items</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree#getItems()
	 * @see #getDynamicContainmentTree()
	 * @generated
	 */
	EReference getDynamicContainmentTree_Items();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem <em>Dynamic Containment Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dynamic Containment Item</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem
	 * @generated
	 */
	EClass getDynamicContainmentItem();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem#getDomainModel <em>Domain Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Domain Model</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem#getDomainModel()
	 * @see #getDynamicContainmentItem()
	 * @generated
	 */
	EReference getDynamicContainmentItem_DomainModel();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem#getItems <em>Items</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Items</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem#getItems()
	 * @see #getDynamicContainmentItem()
	 * @generated
	 */
	EReference getDynamicContainmentItem_Items();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem#getComposite <em>Composite</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Composite</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem#getComposite()
	 * @see #getDynamicContainmentItem()
	 * @generated
	 */
	EReference getDynamicContainmentItem_Composite();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.dynamictree.model.TestElement <em>Test Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Test Element</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.TestElement
	 * @generated
	 */
	EClass getTestElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.dynamictree.model.TestElement#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.TestElement#getId()
	 * @see #getTestElement()
	 * @generated
	 */
	EAttribute getTestElement_Id();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.ecp.view.dynamictree.model.TestElement#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Elements</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.TestElement#getElements()
	 * @see #getTestElement()
	 * @generated
	 */
	EReference getTestElement_Elements();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.dynamictree.model.TestElement#getParentId <em>Parent Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parent Id</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.TestElement#getParentId()
	 * @see #getTestElement()
	 * @generated
	 */
	EAttribute getTestElement_ParentId();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.dynamictree.model.DomainRoot <em>Domain Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Domain Root</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.DomainRoot
	 * @generated
	 */
	EClass getDomainRoot();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.ecp.view.dynamictree.model.DomainRoot#getIntermediate <em>Intermediate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Intermediate</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.DomainRoot#getIntermediate()
	 * @see #getDomainRoot()
	 * @generated
	 */
	EReference getDomainRoot_Intermediate();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.dynamictree.model.DomainIntermediate <em>Domain Intermediate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Domain Intermediate</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.DomainIntermediate
	 * @generated
	 */
	EClass getDomainIntermediate();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.ecp.view.dynamictree.model.DomainIntermediate#getTestElementContainer <em>Test Element Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Test Element Container</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.DomainIntermediate#getTestElementContainer()
	 * @see #getDomainIntermediate()
	 * @generated
	 */
	EReference getDomainIntermediate_TestElementContainer();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecp.view.dynamictree.model.TestElementContainer <em>Test Element Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Test Element Container</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.TestElementContainer
	 * @generated
	 */
	EClass getTestElementContainer();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.ecp.view.dynamictree.model.TestElementContainer#getTestElements <em>Test Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Test Elements</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.TestElementContainer#getTestElements()
	 * @see #getTestElementContainer()
	 * @generated
	 */
	EReference getTestElementContainer_TestElements();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.ecp.view.dynamictree.model.TestElementContainer#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.emf.ecp.view.dynamictree.model.TestElementContainer#getId()
	 * @see #getTestElementContainer()
	 * @generated
	 */
	EAttribute getTestElementContainer_Id();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ModelFactory getModelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.dynamictree.model.impl.DynamicContainmentTreeImpl <em>Dynamic Containment Tree</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.DynamicContainmentTreeImpl
		 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.ModelPackageImpl#getDynamicContainmentTree()
		 * @generated
		 */
		EClass DYNAMIC_CONTAINMENT_TREE = eINSTANCE.getDynamicContainmentTree();

		/**
		 * The meta object literal for the '<em><b>Domain Model</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DYNAMIC_CONTAINMENT_TREE__DOMAIN_MODEL = eINSTANCE.getDynamicContainmentTree_DomainModel();

		/**
		 * The meta object literal for the '<em><b>Child Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DYNAMIC_CONTAINMENT_TREE__CHILD_REFERENCE = eINSTANCE.getDynamicContainmentTree_ChildReference();

		/**
		 * The meta object literal for the '<em><b>Path To Root</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DYNAMIC_CONTAINMENT_TREE__PATH_TO_ROOT = eINSTANCE.getDynamicContainmentTree_PathToRoot();

		/**
		 * The meta object literal for the '<em><b>Child Composite</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DYNAMIC_CONTAINMENT_TREE__CHILD_COMPOSITE = eINSTANCE.getDynamicContainmentTree_ChildComposite();

		/**
		 * The meta object literal for the '<em><b>Items</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DYNAMIC_CONTAINMENT_TREE__ITEMS = eINSTANCE.getDynamicContainmentTree_Items();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.dynamictree.model.impl.DynamicContainmentItemImpl <em>Dynamic Containment Item</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.DynamicContainmentItemImpl
		 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.ModelPackageImpl#getDynamicContainmentItem()
		 * @generated
		 */
		EClass DYNAMIC_CONTAINMENT_ITEM = eINSTANCE.getDynamicContainmentItem();

		/**
		 * The meta object literal for the '<em><b>Domain Model</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DYNAMIC_CONTAINMENT_ITEM__DOMAIN_MODEL = eINSTANCE.getDynamicContainmentItem_DomainModel();

		/**
		 * The meta object literal for the '<em><b>Items</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DYNAMIC_CONTAINMENT_ITEM__ITEMS = eINSTANCE.getDynamicContainmentItem_Items();

		/**
		 * The meta object literal for the '<em><b>Composite</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DYNAMIC_CONTAINMENT_ITEM__COMPOSITE = eINSTANCE.getDynamicContainmentItem_Composite();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.dynamictree.model.impl.TestElementImpl <em>Test Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.TestElementImpl
		 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.ModelPackageImpl#getTestElement()
		 * @generated
		 */
		EClass TEST_ELEMENT = eINSTANCE.getTestElement();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_ELEMENT__ID = eINSTANCE.getTestElement_Id();

		/**
		 * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEST_ELEMENT__ELEMENTS = eINSTANCE.getTestElement_Elements();

		/**
		 * The meta object literal for the '<em><b>Parent Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_ELEMENT__PARENT_ID = eINSTANCE.getTestElement_ParentId();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.dynamictree.model.impl.DomainRootImpl <em>Domain Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.DomainRootImpl
		 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.ModelPackageImpl#getDomainRoot()
		 * @generated
		 */
		EClass DOMAIN_ROOT = eINSTANCE.getDomainRoot();

		/**
		 * The meta object literal for the '<em><b>Intermediate</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOMAIN_ROOT__INTERMEDIATE = eINSTANCE.getDomainRoot_Intermediate();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.dynamictree.model.impl.DomainIntermediateImpl <em>Domain Intermediate</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.DomainIntermediateImpl
		 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.ModelPackageImpl#getDomainIntermediate()
		 * @generated
		 */
		EClass DOMAIN_INTERMEDIATE = eINSTANCE.getDomainIntermediate();

		/**
		 * The meta object literal for the '<em><b>Test Element Container</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOMAIN_INTERMEDIATE__TEST_ELEMENT_CONTAINER = eINSTANCE.getDomainIntermediate_TestElementContainer();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.ecp.view.dynamictree.model.impl.TestElementContainerImpl <em>Test Element Container</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.TestElementContainerImpl
		 * @see org.eclipse.emf.ecp.view.dynamictree.model.impl.ModelPackageImpl#getTestElementContainer()
		 * @generated
		 */
		EClass TEST_ELEMENT_CONTAINER = eINSTANCE.getTestElementContainer();

		/**
		 * The meta object literal for the '<em><b>Test Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEST_ELEMENT_CONTAINER__TEST_ELEMENTS = eINSTANCE.getTestElementContainer_TestElements();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_ELEMENT_CONTAINER__ID = eINSTANCE.getTestElementContainer_Id();

	}

} //ModelPackage
