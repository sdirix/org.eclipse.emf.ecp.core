/**
 */
package org.eclipse.emf.ecp.view.model.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.view.model.TreeCategory;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.model.ViewPackage;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link org.eclipse.emf.ecp.view.model.TreeCategory} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class TreeCategoryItemProvider
	extends AbstractCategorizationItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TreeCategoryItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addTargetFeaturePropertyDescriptor(object);
			addPathToFeaturePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Target Feature feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTargetFeaturePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TreeCategory_targetFeature_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TreeCategory_targetFeature_feature", "_UI_TreeCategory_type"),
				 ViewPackage.Literals.TREE_CATEGORY__TARGET_FEATURE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Path To Feature feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPathToFeaturePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TreeCategory_pathToFeature_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TreeCategory_pathToFeature_feature", "_UI_TreeCategory_type"),
				 ViewPackage.Literals.TREE_CATEGORY__PATH_TO_FEATURE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(ViewPackage.Literals.TREE_CATEGORY__CHILD_COMPOSITE);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns TreeCategory.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/TreeCategory"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((TreeCategory)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_TreeCategory_type") :
			getString("_UI_TreeCategory_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(TreeCategory.class)) {
			case ViewPackage.TREE_CATEGORY__CHILD_COMPOSITE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(ViewPackage.Literals.TREE_CATEGORY__CHILD_COMPOSITE,
				 ViewFactory.eINSTANCE.createControl()));

		newChildDescriptors.add
			(createChildParameter
				(ViewPackage.Literals.TREE_CATEGORY__CHILD_COMPOSITE,
				 ViewFactory.eINSTANCE.createTableControl()));

		newChildDescriptors.add
			(createChildParameter
				(ViewPackage.Literals.TREE_CATEGORY__CHILD_COMPOSITE,
				 ViewFactory.eINSTANCE.createCustomComposite()));

		newChildDescriptors.add
			(createChildParameter
				(ViewPackage.Literals.TREE_CATEGORY__CHILD_COMPOSITE,
				 ViewFactory.eINSTANCE.createSeperator()));

		newChildDescriptors.add
			(createChildParameter
				(ViewPackage.Literals.TREE_CATEGORY__CHILD_COMPOSITE,
				 ViewFactory.eINSTANCE.createColumnComposite()));

		newChildDescriptors.add
			(createChildParameter
				(ViewPackage.Literals.TREE_CATEGORY__CHILD_COMPOSITE,
				 ViewFactory.eINSTANCE.createColumn()));

		newChildDescriptors.add
			(createChildParameter
				(ViewPackage.Literals.TREE_CATEGORY__CHILD_COMPOSITE,
				 ViewFactory.eINSTANCE.createGroup()));
	}

}
