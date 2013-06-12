/**
 */
package org.eclipse.emf.ecp.view.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecp.view.model.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ViewFactoryImpl extends EFactoryImpl implements ViewFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ViewFactory init() {
		try {
			ViewFactory theViewFactory = (ViewFactory)EPackage.Registry.INSTANCE.getEFactory("http://org/eclipse/emf/ecp/view/model"); 
			if (theViewFactory != null) {
				return theViewFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ViewFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ViewFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ViewPackage.VIEW: return createView();
			case ViewPackage.SHOW_RULE: return createShowRule();
			case ViewPackage.ENABLE_RULE: return createEnableRule();
			case ViewPackage.LEAF_CONDITION: return createLeafCondition();
			case ViewPackage.OR_CONDITION: return createOrCondition();
			case ViewPackage.AND_CONDITION: return createAndCondition();
			case ViewPackage.CATEGORIZATION: return createCategorization();
			case ViewPackage.CATEGORY: return createCategory();
			case ViewPackage.CONTROL: return createControl();
			case ViewPackage.TABLE_CONTROL: return createTableControl();
			case ViewPackage.TABLE_COLUMN: return createTableColumn();
			case ViewPackage.CUSTOM_COMPOSITE: return createCustomComposite();
			case ViewPackage.SEPERATOR: return createSeperator();
			case ViewPackage.COLUMN_COMPOSITE: return createColumnComposite();
			case ViewPackage.COLUMN: return createColumn();
			case ViewPackage.GROUP: return createGroup();
			case ViewPackage.TREE_CATEGORY: return createTreeCategory();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public View createView() {
		ViewImpl view = new ViewImpl();
		return view;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ShowRule createShowRule() {
		ShowRuleImpl showRule = new ShowRuleImpl();
		return showRule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnableRule createEnableRule() {
		EnableRuleImpl enableRule = new EnableRuleImpl();
		return enableRule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LeafCondition createLeafCondition() {
		LeafConditionImpl leafCondition = new LeafConditionImpl();
		return leafCondition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OrCondition createOrCondition() {
		OrConditionImpl orCondition = new OrConditionImpl();
		return orCondition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AndCondition createAndCondition() {
		AndConditionImpl andCondition = new AndConditionImpl();
		return andCondition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Categorization createCategorization() {
		CategorizationImpl categorization = new CategorizationImpl();
		return categorization;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Category createCategory() {
		CategoryImpl category = new CategoryImpl();
		return category;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Control createControl() {
		ControlImpl control = new ControlImpl();
		return control;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TableControl createTableControl() {
		TableControlImpl tableControl = new TableControlImpl();
		return tableControl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TableColumn createTableColumn() {
		TableColumnImpl tableColumn = new TableColumnImpl();
		return tableColumn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CustomComposite createCustomComposite() {
		CustomCompositeImpl customComposite = new CustomCompositeImpl();
		return customComposite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Seperator createSeperator() {
		SeperatorImpl seperator = new SeperatorImpl();
		return seperator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ColumnComposite createColumnComposite() {
		ColumnCompositeImpl columnComposite = new ColumnCompositeImpl();
		return columnComposite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Column createColumn() {
		ColumnImpl column = new ColumnImpl();
		return column;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Group createGroup() {
		GroupImpl group = new GroupImpl();
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TreeCategory createTreeCategory() {
		TreeCategoryImpl treeCategory = new TreeCategoryImpl();
		return treeCategory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ViewPackage getViewPackage() {
		return (ViewPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ViewPackage getPackage() {
		return ViewPackage.eINSTANCE;
	}

} //ViewFactoryImpl
