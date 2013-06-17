package org.eclipse.emf.ecp.internal.ui.view.renderer.swt.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTTreeRenderLeaf extends SWTTreeRenderNode {
	
	
	private SWTControl swtControl;
	
	public SWTTreeRenderLeaf(Control result, org.eclipse.emf.ecp.view.model.Composite model, SWTControl swtControl, ECPControlContext controlContext) {
		super(result, model, controlContext);
		this.swtControl = swtControl;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SWTTreeRenderNode> getChildren() {
		return Collections.EMPTY_LIST;
	}
	
	@Override
	public void addChild(SWTTreeRenderNode node) {
		throw new UnsupportedOperationException();
	}
	
	public boolean canValidate() {
		return swtControl != null;
	}

	public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {
		
		if (!canValidate()) {
			return;
		}
		
		swtControl.resetValidation();
		for (Diagnostic diagnostic : affectedObjects.get(swtControl)) {
			swtControl.handleValidation(diagnostic);
		}
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}
}
