package org.eclipse.emf.ecp.ui.view.swt;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RendererLeaf;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RendererNode;
import org.eclipse.emf.ecp.view.model.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTRendererLeaf extends SWTRendererNode {
	
	private SWTControl control;

	public SWTRendererLeaf(Control result, Composite model,
			ECPControlContext controlContext, SWTControl swtControl) {
		super(result, model, controlContext);
		this.control = swtControl;
	}

	@Override
	public List<RendererNode<Control>> getChildren() {
		return Collections.EMPTY_LIST;
	}
	
	@Override
	public void addChild(RendererNode<Control> node) {
		throw new UnsupportedOperationException();
	}
	
	public boolean canValidate() {
		return control != null;
	}

public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {
		
		if (!canValidate()) {
			return;
		}
		
		control.resetValidation();
		if (affectedObjects.containsKey(getRenderable())) { 
			for (Diagnostic diagnostic : affectedObjects.get(getRenderable())) {
				control.handleValidation(diagnostic);
			}
		}
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}
}
