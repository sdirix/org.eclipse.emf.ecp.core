package org.eclipse.emf.ecp.view.model.internal.fx;

import java.util.Locale;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

@SuppressWarnings("restriction")
public class FXControlContext implements ECPControlContext {

	private final EMFDataBindingContext dataBindingContext = new EMFDataBindingContext();
	private EObject root;

	public FXControlContext(EObject root) {
		this.root = root;
	}

	@Override
	public DataBindingContext getDataBindingContext() {
		return dataBindingContext;
	}

	@Override
	public EditingDomain getEditingDomain() {
		return AdapterFactoryEditingDomain
				.getEditingDomainFor(getModelElement());
	}

	@Override
	public EObject getModelElement() {
		return root;
	}

	@Override
	public void addModelElement(EObject eObject, EReference eReference) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EObject getNewElementFor(EReference eReference) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EObject getExistingElementFor(EReference eReference) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void openInNewContext(EObject eObject) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRunningAsWebApplication() {
		return false;
	}

	@Override
	public Locale getLocale() {
		return Locale.getDefault();
	}

	@Override
	public ECPControlContext createSubContext(EObject eObject) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ViewModelContext getViewContext() {
		throw new UnsupportedOperationException();
	}

}
