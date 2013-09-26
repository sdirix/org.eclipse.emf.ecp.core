package org.eclipse.emf.ecp.view.editor.controls;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.actions.DeleteReferenceAction;
import org.eclipse.emf.ecp.edit.internal.swt.controls.LinkControl;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.table.model.VTableColumn;
import org.eclipse.emf.ecp.view.table.model.VTableControl;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListDialog;

public class TableColumnAttributeControl extends LinkControl {

	public TableColumnAttributeControl(boolean showLabel,
		IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, ECPControlContext modelElementContext,
		boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext,
			embedded);
	}

	@Override
	protected int getNumButtons() {
		return 2;
	}

	@Override
	protected Button[] createButtons(Composite composite) {
		final Button[] buttons = new Button[2];
		buttons[0] = createButtonForAction(new DeleteReferenceAction(
			getModelElementContext(), getItemPropertyDescriptor(),
			getStructuralFeature()), composite);
		buttons[1] = createButtonForAction(new FilteredReferenceAction(
			(EReference) getStructuralFeature(),
			getItemPropertyDescriptor(), composite.getShell()), composite);
		return buttons;
	}

	private class FilteredReferenceAction extends
		AbstractFilteredReferenceAction {

		public FilteredReferenceAction(EReference eReference,
			IItemPropertyDescriptor descriptor, Shell shell) {
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
					new FilteredReferenceCommand(
						getModelElementContext().getModelElement(),
						shell));
		}
	}

	private class FilteredReferenceCommand extends ChangeCommand {

		private final Shell shell;

		public FilteredReferenceCommand(Notifier notifier, Shell shell) {
			super(notifier);
			this.shell = shell;
		}

		@Override
		protected void doExecute() {

			final AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(
				composedAdapterFactory);
			final EReference eref = (EReference) ((VFeaturePathDomainModelReference) ((VTableControl) getModelElementContext()
				.getModelElement()
				.eContainer()).getDomainModelReference()).getDomainModelEFeature();
			final ListDialog ld = new ListDialog(shell);
			ld.setLabelProvider(labelProvider);
			ld.setContentProvider(ArrayContentProvider.getInstance());
			ld.setInput(eref.getEReferenceType().getEAllAttributes());
			ld.setTitle("Select Attribute for TableColumn");
			final int result = ld.open();
			if (Window.OK == result) {
				final Object selection = ld.getResult()[0];
				if (EAttribute.class.isInstance(selection)) {
					final EAttribute selectedFeature = (EAttribute) selection;

					((VTableColumn) getModelElementContext().getModelElement()).setAttribute(selectedFeature);
				}
			}
			labelProvider.dispose();
		}
	}
}
