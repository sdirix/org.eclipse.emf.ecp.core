package org.eclipse.emf.ecp.internal.ui.view.renderer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControl;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class RendererLeaf<T> extends RendererNode<T> {

	private ECPControl ecpControl;
	
	public RendererLeaf(T result, 
			org.eclipse.emf.ecp.view.model.Composite model, 
			ECPControlContext controlContext,
			ECPControl ecpControl) {
		super(result, model, controlContext);
		this.setEcpControl(ecpControl);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RendererNode<T>> getChildren() {
		return Collections.EMPTY_LIST;
	}
	
	@Override
	public void addChild(RendererNode<T> node) {
		throw new UnsupportedOperationException();
	}
	
	public boolean canValidate() {
		return getEcpControl() != null;
	}

	public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {
		
		if (!canValidate()) {
			return;
		}
		
		getEcpControl().resetValidation();
		if (affectedObjects.containsKey(getModel())) { 
			for (Diagnostic diagnostic : affectedObjects.get(getModel())) {
				getEcpControl().handleValidation(diagnostic);
			}
		}
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}

	public ECPControl getEcpControl() {
		return ecpControl;
	}

	public void setEcpControl(ECPControl ecpControl) {
		this.ecpControl = ecpControl;
	}
}
