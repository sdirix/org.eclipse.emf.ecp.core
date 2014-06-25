/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.template.internal.tooling.controls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.editor.controls.EditableEReferenceLabelControlSWTRenderer;
import org.eclipse.emf.ecp.view.model.common.edit.provider.CustomReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;
import org.eclipse.emf.ecp.view.template.internal.tooling.Messages;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * Renderer for the
 * {@link org.eclipse.emf.ecp.view.template.selector.viewModelElement.model.VTViewModelElementSelector#setClassType(EClass)
 * VTViewModelElementSelector#setClassType(EClass)}.
 * 
 * @author Eugen Neufeld
 * 
 */
@SuppressWarnings("restriction")
public class ViewModelSelectControlSWTRenderer extends EditableEReferenceLabelControlSWTRenderer {

	/**
	 * @author Eugen
	 * 
	 */
	private static final class EClassSelectionContentProvider implements ITreeContentProvider {
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// do nothing
		}

		@Override
		public void dispose() {
			// do nothing
		}

		@Override
		public boolean hasChildren(Object element) {
			if (EPackage.Registry.class.isInstance(element)) {
				return true;
			}
			if (EPackage.Descriptor.class.isInstance(element)) {
				return true;
			}
			if (EPackage.class.isInstance(element)) {
				return true;
			}
			return false;
		}

		@Override
		public Object getParent(Object element) {
			if (EObject.class.isInstance(element)) {
				return EObject.class.cast(element).eContainer();
			}
			return null;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (EPackage.Registry.class.isInstance(parentElement)) {
				final List<Object> result = new ArrayList<Object>();
				for (final Object object : EPackage.Registry.class.cast(parentElement).values()) {
					if (EPackage.Descriptor.class.isInstance(object)) {
						result.add(EPackage.Descriptor.class.cast(object).getEPackage());
					}
					else {
						result.add(object);
					}
				}
				Collections.sort(result, new Comparator<Object>() {

					@Override
					public int compare(Object object1, Object object2) {
						if (EPackage.class.isInstance(object1) && EPackage.class.isInstance(object2)) {
							return EPackage.class.cast(object1).getName()
								.compareTo(EPackage.class.cast(object2).getName());
						}
						return object1.toString().compareTo(object2.toString());
					}
				});
				return result.toArray();
			}
			if (EPackage.Descriptor.class.isInstance(parentElement)) {
				return getChildren(EPackage.Descriptor.class.cast(parentElement).getEPackage());
			}
			if (EPackage.class.isInstance(parentElement)) {
				return EPackage.class.cast(parentElement).getEClassifiers().toArray();
			}
			return null;
		}
	}

	private static final String PLUGIN_ID = "org.eclipse.emf.ecp.view.template.tooling"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.editor.controls.EditableEReferenceLabelControlSWTRenderer#linkValue(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void linkValue(Shell shell) {
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
			new CustomReflectiveItemProviderAdapterFactory(),
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		final AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(
			adapterFactory);

		final ElementTreeSelectionDialog etsd = new ElementTreeSelectionDialog(shell, labelProvider,
			getContentProvider());
		etsd.setAllowMultiple(false);
		etsd.setValidator(new ISelectionStatusValidator() {

			@Override
			public IStatus validate(Object[] selection) {
				if (selection == null || selection.length == 0) {
					return new Status(IStatus.ERROR, PLUGIN_ID,
						Messages.ViewModelSelectControlSWTRenderer_ErrorNoSelection);
				}
				final Object object = selection[0];
				if (!EClass.class.isInstance(object)) {
					return new Status(IStatus.ERROR, PLUGIN_ID,
						Messages.ViewModelSelectControlSWTRenderer_ErrorNoEClass);
				}
				if (!VViewPackage.eINSTANCE.getElement().isSuperTypeOf(EClass.class.cast(object))) {
					return new Status(IStatus.ERROR, PLUGIN_ID,
						Messages.ViewModelSelectControlSWTRenderer_ErrorNoVElement);
				}
				return Status.OK_STATUS;
			}
		});
		etsd.setBlockOnOpen(true);
		etsd.setInput(EPackage.Registry.INSTANCE);
		final int open = etsd.open();
		labelProvider.dispose();
		adapterFactory.dispose();
		if (Window.CANCEL == open) {
			return;
		}
		final Object result = etsd.getFirstResult();
		final Setting setting = getVElement().getDomainModelReference().getIterator().next();
		final EditingDomain editingDomain = getEditingDomain(setting);
		final Command command = SetCommand.create(editingDomain, setting.getEObject(), setting.getEStructuralFeature(),
			result);
		editingDomain.getCommandStack().execute(command);

	}

	private ITreeContentProvider getContentProvider() {
		return new EClassSelectionContentProvider();
	}

}
