/**
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 */
package org.eclipse.emf.ecp.view.mappingdmr.tooling;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.editor.controls.EditableEReferenceLabelControlSWTRenderer;
import org.eclipse.emf.ecp.view.spi.mappingdmr.model.VMappingDomainModelReference;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * Control for a {@link org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference
 * VFeaturePathDomainModelReference} that is a child of a {@link VMappingDomainModelReference}.
 * 
 * @author Eugen Neufeld
 * 
 */
@SuppressWarnings("restriction")
public class FeaturePathDMRSubMappedEClassControlSWTRenderer extends
	EditableEReferenceLabelControlSWTRenderer {

	/**
	 * @author Eugen
	 *
	 */
	private static final class EStructuralFeatureTreeContentProvider implements ITreeContentProvider {
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean hasChildren(Object element) {

			if (EPackage.class.isInstance(element)) {
				return true;
			}
			if (EClass.class.isInstance(element)) {
				final EClass eClass = (EClass) element;
				final boolean hasReferences = !eClass.getEAllReferences().isEmpty();
				final boolean hasAttributes = !eClass.getEAllAttributes().isEmpty();
				return hasReferences || hasAttributes;

			}
			if (EReference.class.isInstance(element)) {
				final EReference eReference = (EReference) element;

				return hasChildren(eReference
					.getEReferenceType());
			}
			return false;
		}

		@Override
		public Object getParent(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (EClass.class.isInstance(parentElement)) {
				final EClass eClass = (EClass) parentElement;
				final Set<Object> result = getElementsForEClass(eClass);
				return result.toArray();
			}
			if (EReference.class.isInstance(parentElement)) {
				final EReference eReference = (EReference) parentElement;
				final Set<Object> result = getElementsForEClass(eReference.getEReferenceType());
				return result.toArray();
			}
			if (EPackage.Registry.class.isInstance(parentElement)) {
				return EPackage.Registry.class.cast(parentElement).values().toArray();
			}
			if (EPackage.class.isInstance(parentElement)) {
				final Set<Object> children = new LinkedHashSet<Object>();
				children.addAll(EPackage.class.cast(parentElement).getESubpackages());
				children.addAll(EPackage.class.cast(parentElement).getEClassifiers());
				return children.toArray();
			}
			return null;
		}

		private Set<Object> getElementsForEClass(EClass eClass) {
			final Set<Object> result = new LinkedHashSet<Object>();
			if (eClass.isAbstract() || eClass.isInterface()) {
				// find eClasses which are not abstract
				for (final EClassifier eClassifier : eClass.getEPackage().getEClassifiers()) {
					if (eClass != eClassifier && EClass.class.isInstance(eClassifier)
						&& eClass.isSuperTypeOf((EClass) eClassifier)) {
						result.add(eClassifier);
					}
				}
			}
			else {
				result.addAll(eClass.getEAllReferences());
				result.addAll(eClass.getEAllAttributes());
			}
			return result;
		}
	}

	@Override
	protected void linkValue(Shell shell) {
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
			new ReflectiveItemProviderAdapterFactory(),
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		final AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(
			adapterFactory);
		final ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, labelProvider,
			getContentProvider());
		dialog.setAllowMultiple(false);
		dialog.setValidator(new ISelectionStatusValidator() {

			@Override
			public IStatus validate(Object[] selection) {
				if (selection.length != 0 && EStructuralFeature.class.isInstance(selection[0])) {

					return Status.OK_STATUS;
				}
				return new Status(IStatus.ERROR, "org.eclipse.emf.ecp.view.mappingdmr.tooling", //$NON-NLS-1$
					"This is not an EStructuralFeature."); //$NON-NLS-1$
			}
		});
		dialog.setInput(getInput());
		dialog.setMessage("Select an EClass."); //$NON-NLS-1$
		dialog.setTitle("Select an EClass."); //$NON-NLS-1$
		dialog.setComparator(new ViewerComparator());
		final int result = dialog.open();
		if (Window.OK == result) {
			final Object selection = dialog.getFirstResult();
			if (EClass.class.isInstance(selection)) {
				final EClass selectedFeature = (EClass) selection;
				final VMappingDomainModelReference view = (VMappingDomainModelReference) getVElement()
					.getDomainModelReference().getIterator().next().getEObject();

				view.setMappedClass(selectedFeature);

			}
		}
		labelProvider.dispose();
		adapterFactory.dispose();
	}

	private ITreeContentProvider getContentProvider() {
		return new EStructuralFeatureTreeContentProvider();
	}

	/**
	 * @return an instance of the {@link org.eclipse.emf.ecore.EPackage.Registry}
	 */
	private Object getInput() {
		final Setting setting = getVElement().getDomainModelReference().getIterator().next();
		return VMappingDomainModelReference.class.cast(setting
			.getEObject()
			.eContainer()).getMappedClass();
	}

}
