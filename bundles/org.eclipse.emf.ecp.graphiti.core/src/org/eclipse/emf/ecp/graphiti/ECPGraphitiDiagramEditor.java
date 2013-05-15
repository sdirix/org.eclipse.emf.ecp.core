package org.eclipse.emf.ecp.graphiti;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

public abstract class ECPGraphitiDiagramEditor extends DiagramEditor {

	public ECPGraphitiDiagramEditor() {
		super();
	}

	protected GraphitiDiagramEditorInput input;

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.input = (GraphitiDiagramEditorInput) input;
		super.init(site, input);
	}

	@Override
	protected DiagramBehavior createDiagramBehavior() {
		ECPDiagramBehavior diagramBehavior = new ECPDiagramBehavior(this,input.getDiagram(),input.getBusinessObject());
		diagramBehavior.setParentPart(this);
		diagramBehavior.initDefaultBehaviors();

		return diagramBehavior;
	}

	

	
}
