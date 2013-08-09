package org.eclipse.emf.ecp.view.editor.controls;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.actions.DeleteReferenceAction;
import org.eclipse.emf.ecp.edit.internal.swt.controls.LinkControl;
import org.eclipse.emf.ecp.view.model.TreeCategory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

public class TreeCategoryTargetFeatureControl extends LinkControl {

	public TreeCategoryTargetFeatureControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, ECPControlContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getNumButtons() {
		return 2;
	}

	@Override
	protected Button[] createButtons(Composite composite) {
		Button[] buttons = new Button[2];
		buttons[0] = createButtonForAction(new DeleteReferenceAction(getModelElementContext(),
			getItemPropertyDescriptor(), getStructuralFeature()) {

			@Override
			public void run() {
				super.run();
				((TreeCategory) getModelElementContext().getModelElement()).getPathToFeature().clear();
			}

		}, composite);
		buttons[1] = createButtonForAction(new FilteredReferenceAction((EReference) getStructuralFeature(),
			getItemPropertyDescriptor(), composite.getShell()), composite);
		return buttons;
	}

	private class FilteredReferenceAction extends AbstractFilteredReferenceAction {

		public FilteredReferenceAction(EReference eReference, IItemPropertyDescriptor descriptor, Shell shell) {
			super(eReference, descriptor, shell);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			getModelElementContext()
				.getEditingDomain()
				.getCommandStack()
				.execute(
					new FilteredReferenceCommand(getModelElementContext().getModelElement(), composedAdapterFactory,
						shell));
		}
	}

	private class FilteredReferenceCommand extends AbstractFilteredReferenceCommand<EReference> {

		public FilteredReferenceCommand(Notifier notifier, ComposedAdapterFactory composedAdapterFactory, Shell shell) {
			super(notifier, composedAdapterFactory, shell, Helper.getRootEClass(getModelElementContext()
				.getModelElement()), new ISelectionStatusValidator() {

				public IStatus validate(Object[] selection) {
					if (selection.length != 0 && EReference.class.isInstance(selection[0])) {

						return Status.OK_STATUS;
					}
					return new Status(IStatus.ERROR, org.eclipse.emf.ecp.view.editor.controls.Activator.PLUGIN_ID,
						"This is not an " + EReference.class.getSimpleName() + ".");
				}
			});
		}

		@Override
		protected void setSelectedValues(EReference selectedFeature, List<EReference> bottomUpPath) {
			TreeCategory condition = (TreeCategory) getModelElementContext().getModelElement();
			condition.setTargetFeature(selectedFeature);
			condition.getPathToFeature().clear();
			condition.getPathToFeature().addAll(bottomUpPath);
		}

	}

}
