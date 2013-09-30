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
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * A Link Control for creating VFeaturePathDomainReference Objects.
 * 
 * @author Eugen Neufeld
 * 
 */
public class ControlTargetFeatureControl extends LinkControl {
	/**
	 * Th default constructor.
	 * 
	 * @param showLabel whether to show label
	 * @param itemPropertyDescriptor the descriptor of the feature
	 * @param feature the feature
	 * @param modelElementContext the modelelement context
	 * @param embedded whether this control is embedded
	 */
	public ControlTargetFeatureControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
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
			getItemPropertyDescriptor(), getStructuralFeature()) {

			@Override
			public void run() {
				super.run();
				final VFeaturePathDomainModelReference modelReference =
					(VFeaturePathDomainModelReference) getModelElementContext().getModelElement();
				modelReference.getDomainModelEReferencePath().clear();
				modelReference.setDomainModelEFeature(null);
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

	private class FilteredReferenceCommand extends AbstractFilteredReferenceCommand<EStructuralFeature> {

		public FilteredReferenceCommand(Notifier notifier, ComposedAdapterFactory composedAdapterFactory, Shell shell) {
			super(notifier, composedAdapterFactory, shell, Helper.getRootEClass(getModelElementContext()
				.getModelElement()), new ISelectionStatusValidator() {

				public IStatus validate(Object[] selection) {
					if (selection.length != 0 && EStructuralFeature.class.isInstance(selection[0])) {

						return Status.OK_STATUS;
					}
					return new Status(IStatus.ERROR, org.eclipse.emf.ecp.view.editor.controls.Activator.PLUGIN_ID,
						"This is not an " + EStructuralFeature.class.getSimpleName() + ".");
				}
			});
		}

		@Override
		protected void setSelectedValues(EStructuralFeature selectedFeature, List<EReference> bottomUpPath) {
			final VFeaturePathDomainModelReference modelReference = (VFeaturePathDomainModelReference) getModelElementContext()
				.getModelElement();
			// final VFeaturePathDomainModelReference modelReference = ViewFactory.eINSTANCE
			// .createVFeaturePathDomainModelReference();
			modelReference.setDomainModelEFeature(selectedFeature);
			modelReference.getDomainModelEReferencePath().addAll(bottomUpPath);
			// condition.setTargetFeature(selectedFeature);
			// condition.getPathToFeature().clear();
			// condition.getPathToFeature().addAll(bottomUpPath);
			// condition.setDomainModelReference(modelReference);
		}

	}

}
