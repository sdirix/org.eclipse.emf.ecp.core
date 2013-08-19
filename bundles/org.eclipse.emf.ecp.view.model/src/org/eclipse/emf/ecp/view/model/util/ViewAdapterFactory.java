/**
 */
package org.eclipse.emf.ecp.view.model.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.AbstractControl;
import org.eclipse.emf.ecp.view.model.Action;
import org.eclipse.emf.ecp.view.model.Attachment;
import org.eclipse.emf.ecp.view.model.Categorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.Column;
import org.eclipse.emf.ecp.view.model.ColumnComposite;
import org.eclipse.emf.ecp.view.model.Composite;
import org.eclipse.emf.ecp.view.model.CompositeCollection;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.CustomComposite;
import org.eclipse.emf.ecp.view.model.Group;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.TableColumn;
import org.eclipse.emf.ecp.view.model.TableControl;
import org.eclipse.emf.ecp.view.model.VDiagnostic;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewPackage;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides
 * an adapter <code>createXXX</code> method for each class of the model. <!--
 * end-user-doc -->
 * 
 * @see org.eclipse.emf.ecp.view.model.ViewPackage
 * @generated
 */
public class ViewAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static ViewPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public ViewAdapterFactory() {
		if (modelPackage == null)
		{
			modelPackage = ViewPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc --> This implementation returns <code>true</code> if
	 * the object is either the model's package or is an instance object of the
	 * model. <!-- end-user-doc -->
	 * 
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage)
		{
			return true;
		}
		if (object instanceof EObject)
		{
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ViewSwitch<Adapter> modelSwitch = new ViewSwitch<Adapter>()
	{
		@Override
		public Adapter caseView(View object)
		{
			return createViewAdapter();
		}

		@Override
		public Adapter caseAbstractCategorization(AbstractCategorization object)
		{
			return createAbstractCategorizationAdapter();
		}

		@Override
		public Adapter caseCategorization(Categorization object)
		{
			return createCategorizationAdapter();
		}

		@Override
		public Adapter caseCategory(Category object)
		{
			return createCategoryAdapter();
		}

		@Override
		public Adapter caseComposite(Composite object)
		{
			return createCompositeAdapter();
		}

		@Override
		public Adapter caseControl(Control object)
		{
			return createControlAdapter();
		}

		@Override
		public Adapter caseTableControl(TableControl object)
		{
			return createTableControlAdapter();
		}

		@Override
		public Adapter caseTableColumn(TableColumn object)
		{
			return createTableColumnAdapter();
		}

		@Override
		public Adapter caseCustomComposite(CustomComposite object)
		{
			return createCustomCompositeAdapter();
		}

		@Override
		public Adapter caseCompositeCollection(CompositeCollection object)
		{
			return createCompositeCollectionAdapter();
		}

		@Override
		public Adapter caseColumnComposite(ColumnComposite object)
		{
			return createColumnCompositeAdapter();
		}

		@Override
		public Adapter caseColumn(Column object)
		{
			return createColumnAdapter();
		}

		@Override
		public Adapter caseGroup(Group object)
		{
			return createGroupAdapter();
		}

		@Override
		public Adapter caseRenderable(Renderable object)
		{
			return createRenderableAdapter();
		}

		@Override
		public Adapter caseAction(Action object)
		{
			return createActionAdapter();
		}

		@Override
		public Adapter caseAbstractControl(AbstractControl object)
		{
			return createAbstractControlAdapter();
		}

		@Override
		public Adapter caseAttachment(Attachment object)
		{
			return createAttachmentAdapter();
		}

		@Override
		public Adapter caseVDiagnostic(VDiagnostic object)
		{
			return createVDiagnosticAdapter();
		}

		@Override
		public Adapter defaultCase(EObject object)
		{
			return createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class ' {@link org.eclipse.emf.ecp.view.model.View <em>View</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we
	 * can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.View
	 * @generated
	 */
	public Adapter createViewAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecp.view.model.AbstractCategorization
	 * <em>Abstract Categorization</em>}'.
	 * <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.AbstractCategorization
	 * @generated
	 */
	public Adapter createAbstractCategorizationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecp.view.model.Categorization
	 * <em>Categorization</em>}'.
	 * <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.Categorization
	 * @generated
	 */
	public Adapter createCategorizationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class ' {@link org.eclipse.emf.ecp.view.model.Category <em>Category</em>}
	 * '. <!--
	 * begin-user-doc --> This default implementation returns null so that we
	 * can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.Category
	 * @generated
	 */
	public Adapter createCategoryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecp.view.model.Composite <em>Composite</em>}
	 * '.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.Composite
	 * @generated
	 */
	public Adapter createCompositeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class ' {@link org.eclipse.emf.ecp.view.model.Control <em>Control</em>}'.
	 * <!--
	 * begin-user-doc --> This default implementation returns null so that we
	 * can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.Control
	 * @generated
	 */
	public Adapter createControlAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecp.view.model.TableControl
	 * <em>Table Control</em>}'.
	 * <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.TableControl
	 * @generated
	 */
	public Adapter createTableControlAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecp.view.model.TableColumn
	 * <em>Table Column</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so
	 * that we can easily ignore cases; it's useful to ignore a case when
	 * inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.TableColumn
	 * @generated
	 */
	public Adapter createTableColumnAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecp.view.model.CustomComposite
	 * <em>Custom Composite</em>}'.
	 * <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.CustomComposite
	 * @generated
	 */
	public Adapter createCustomCompositeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecp.view.model.CompositeCollection
	 * <em>Composite Collection</em>}'.
	 * <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.CompositeCollection
	 * @generated
	 */
	public Adapter createCompositeCollectionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecp.view.model.ColumnComposite
	 * <em>Column Composite</em>}'.
	 * <!-- begin-user-doc --> This default
	 * implementation returns null so that we can easily ignore cases; it's
	 * useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.ColumnComposite
	 * @generated
	 */
	public Adapter createColumnCompositeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class ' {@link org.eclipse.emf.ecp.view.model.Column <em>Column</em>}'.
	 * <!--
	 * begin-user-doc --> This default implementation returns null so that we
	 * can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.Column
	 * @generated
	 */
	public Adapter createColumnAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class ' {@link org.eclipse.emf.ecp.view.model.Group <em>Group</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we
	 * can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.Group
	 * @generated
	 */
	public Adapter createGroupAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecp.view.model.Renderable
	 * <em>Renderable</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.Renderable
	 * @generated
	 */
	public Adapter createRenderableAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class ' {@link org.eclipse.emf.ecp.view.model.Action <em>Action</em>}'.
	 * <!--
	 * begin-user-doc --> This default implementation returns null so that we
	 * can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.Action
	 * @generated
	 */
	public Adapter createActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecp.view.model.AbstractControl
	 * <em>Abstract Control</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.AbstractControl
	 * @generated
	 */
	public Adapter createAbstractControlAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecp.view.model.Attachment
	 * <em>Attachment</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.Attachment
	 * @generated
	 */
	public Adapter createAttachmentAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecp.view.model.VDiagnostic
	 * <em>VDiagnostic</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecp.view.model.VDiagnostic
	 * @generated
	 */
	public Adapter createVDiagnosticAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} // ViewAdapterFactory
