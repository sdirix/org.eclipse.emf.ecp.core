package org.eclipse.emf.ecp.graphiti;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.editor.DefaultPersistencyBehavior;
import org.eclipse.graphiti.ui.editor.DefaultUpdateBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

public abstract class ECPGraphitiDiagramEditor extends DiagramEditor {

	public ECPGraphitiDiagramEditor() {
		super();
	}

	protected GraphitiDiagramEditorInput input;
	private UpdateBehavior updateBehavior;

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.input = (GraphitiDiagramEditorInput) input;

		// set diagram in the update behavior
		Diagram mydiagram = this.input.getDiagram();
		((UpdateBehavior) getUpdateBehavior()).setDiagram(mydiagram);

		super.init(site, input);
	}

	@Override
	protected DefaultUpdateBehavior createUpdateBehavior() {
		updateBehavior = new UpdateBehavior(this);
		return updateBehavior;
	}

	@Override
	protected DefaultPersistencyBehavior createPersistencyBehavior() {
		return new DefaultPersistencyBehavior(this) {

			@Override
			public Diagram loadDiagram(URI uri) {
				return input.getDiagram();
			}

		};

	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		getEditingDomain().getCommandStack().execute(
				new RecordingCommand(getEditingDomain()) {

					@Override
					protected void doExecute() {
						getDiagramTypeProvider().getFeatureProvider().link(
								getDiagramTypeProvider().getDiagram(),
								input.getBusinessObject());
					}
				});
	}

	
}
