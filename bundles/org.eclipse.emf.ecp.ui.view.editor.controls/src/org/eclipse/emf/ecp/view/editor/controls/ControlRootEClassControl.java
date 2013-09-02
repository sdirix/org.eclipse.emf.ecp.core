package org.eclipse.emf.ecp.view.editor.controls;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Descriptor;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.actions.DeleteReferenceAction;
import org.eclipse.emf.ecp.edit.internal.swt.controls.LinkControl;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

public class ControlRootEClassControl extends LinkControl {

	public ControlRootEClassControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
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
		final Button[] buttons = new Button[2];
		buttons[0] = createButtonForAction(new DeleteReferenceAction(getModelElementContext(),
			getItemPropertyDescriptor(), getStructuralFeature()), composite);
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
			getModelElementContext().getEditingDomain().getCommandStack()
				.execute(new FilteredReferenceCommand(getModelElementContext().getModelElement(), shell));
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

			final AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);
			final ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, labelProvider,
				getContentProvider());
			dialog.setAllowMultiple(false);
			dialog.setValidator(new ISelectionStatusValidator() {

				public IStatus validate(Object[] selection) {
					if (selection.length != 0 && EClass.class.isInstance(selection[0])) {

						return Status.OK_STATUS;
					}
					return new Status(IStatus.ERROR, org.eclipse.emf.ecp.view.editor.controls.Activator.PLUGIN_ID,
						"This is not an EClass.");
				}
			});
			dialog.setInput(Registry.INSTANCE);
			dialog.setMessage("Select an EClass.");
			dialog.setTitle("Select an EClass.");
			dialog.setComparator(new ViewerComparator());
			final int result = dialog.open();
			if (Window.OK == result) {
				final Object selection = dialog.getFirstResult();
				if (EClass.class.isInstance(selection)) {
					final EClass selectedFeature = (EClass) selection;

					((View) getModelElementContext().getModelElement()).setRootEClass(selectedFeature);
				}
			}
			labelProvider.dispose();
		}

		private ITreeContentProvider getContentProvider() {
			return new ITreeContentProvider() {

				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
					// TODO Auto-generated method stub

				}

				public void dispose() {
					// TODO Auto-generated method stub

				}

				public boolean hasChildren(Object element) {
					if (EPackage.class.isInstance(element)) {
						return true;
					}
					if (Descriptor.class.isInstance(element)) {
						final Descriptor descriptor = (Descriptor) element;
						descriptor.getEPackage();
						return true;
					}
					return false;
				}

				public Object getParent(Object element) {
					if (EClass.class.isInstance(element)) {
						return ((EClass) element).eContainer();
					}
					return null;
				}

				public Object[] getElements(Object inputElement) {
					if (Registry.class.isInstance(inputElement)) {
						return ((Registry) inputElement).values().toArray();
					}
					return null;
				}

				public Object[] getChildren(Object parentElement) {
					if (EPackage.class.isInstance(parentElement)) {
						final EPackage ePackage = (EPackage) parentElement;
						final Set<EClass> result = new LinkedHashSet<EClass>();
						for (final EClassifier classifier : ePackage.getEClassifiers()) {
							if (EClass.class.isInstance(classifier)) {
								result.add((EClass) classifier);
							}
						}
						return result.toArray();
					}
					if (Descriptor.class.isInstance(parentElement)) {
						return getChildren(((Descriptor) parentElement).getEPackage());
					}
					return null;
				}
			};
		}

	}

}
