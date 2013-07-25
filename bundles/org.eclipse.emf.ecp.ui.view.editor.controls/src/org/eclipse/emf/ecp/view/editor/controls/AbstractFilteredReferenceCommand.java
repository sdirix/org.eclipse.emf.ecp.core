package org.eclipse.emf.ecp.view.editor.controls;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractFilteredReferenceCommand<T extends EStructuralFeature> extends ChangeCommand {

	private Shell shell;
	private Map<EClass, EReference> childParentReferenceMap = new HashMap<EClass, EReference>();
	private ComposedAdapterFactory composedAdapterFactory;
	private EClass rootClass;
	private ISelectionStatusValidator validator;

	public AbstractFilteredReferenceCommand(Notifier notifier, ComposedAdapterFactory composedAdapterFactory,
		Shell shell, EClass rootClass, ISelectionStatusValidator validator) {
		super(notifier);
		this.shell = shell;
		this.composedAdapterFactory = composedAdapterFactory;
		this.rootClass = rootClass;
		this.validator = validator;
	}

	public Class returnedClass() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		return (Class) parameterizedType.getActualTypeArguments()[0];
	}

	@Override
	protected void doExecute() {

		AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, labelProvider,
			getContentProvider(rootClass));
		dialog.setAllowMultiple(false);
		dialog.setValidator(validator);
		dialog.setInput(rootClass);
		dialog.setMessage("Select a " + returnedClass().getSimpleName());
		dialog.setTitle("Select a " + returnedClass().getSimpleName());
		int result = dialog.open();
		if (Window.OK == result) {
			Object selection = dialog.getFirstResult();
			if (returnedClass().isInstance(selection)) {
				T selectedFeature = (T) selection;

				List<EReference> bottomUpPath = new ArrayList<EReference>();
				EClass selectedClass = selectedFeature.getEContainingClass();

				while (childParentReferenceMap.containsKey(selectedClass)) {
					EReference parentReference = childParentReferenceMap.get(selectedClass);
					bottomUpPath.add(parentReference);
					selectedClass = parentReference.getEContainingClass();
				}
				Collections.reverse(bottomUpPath);

				setSelectedValues(selectedFeature, bottomUpPath);
			}
		}
		labelProvider.dispose();
	}

	protected abstract void setSelectedValues(T selectedFeature, List<EReference> bottomUpPath);

	private ITreeContentProvider getContentProvider(EClass rootClass) {
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
				if (EClass.class.isInstance(element)) {
					EClass eClass = (EClass) element;
					boolean hasContainments = !eClass.getEAllContainments().isEmpty();
					boolean hasAttributes = !eClass.getEAllAttributes().isEmpty();
					return hasContainments || hasAttributes;

				}
				return false;
			}

			public Object getParent(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			public Object[] getElements(Object inputElement) {
				return getChildren(inputElement);
			}

			public Object[] getChildren(Object parentElement) {
				if (EClass.class.isInstance(parentElement)) {
					EClass eClass = (EClass) parentElement;
					Set<Object> result = new LinkedHashSet<Object>();
					for (EReference eReference : eClass.getEAllContainments()) {
						EClass child = eReference.getEReferenceType();
						result.add(child);
						childParentReferenceMap.put(child, eReference);
						if (returnedClass().isInstance(eReference) && eReference.isMany()) {
							result.add(eReference);
						}
					}
					result.addAll(eClass.getEAllAttributes());
					return result.toArray();
				}
				return null;
			}
		};
	}

}
