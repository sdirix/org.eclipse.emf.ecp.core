package org.eclipse.emf.ecp.graphiti.internal.integration;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.graphiti.GraphitiDiagramEditorInput;
import org.eclipse.emf.ecp.internal.graphiti.feature.LoadProjectFeature;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

@SuppressWarnings("restriction")
public class OpenGraphitiEditorHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil
				.getCurrentSelection(event);

		Object selObject = selection.getFirstElement();
		if (!ECPProject.class.isInstance(selObject))
			return null;
		InternalProject selectedProject=(InternalProject) selObject;
		Notifier projectRoot=selectedProject.getProvider().getRoot(selectedProject);
		if(!EObject.class.isInstance(projectRoot)){
			return null;
		}
		EObject selectedEObject=(EObject) projectRoot;
		TransactionalEditingDomain editingDomain = (TransactionalEditingDomain) selectedProject.getEditingDomain();

		ResourceSet resourceSet = editingDomain.getResourceSet();
		// create temporal resource
		final Resource resource = resourceSet.createResource(URI.createURI(
				"VIRTUAL_URI", false));

		String diagramText=selectedProject.getName();
		
		final Diagram createDiagram = Graphiti.getPeCreateService()
				.createDiagram("org.eclipse.emf.ecp.graphiti.diagramType",
						diagramText, true);
		createDiagram.setName(diagramText);
		// add the diagram to the resource
		editingDomain.getCommandStack().execute(
				new RecordingCommand(editingDomain) {

					@Override
					protected void doExecute() {
						resource.getContents().add(createDiagram);
					}
				});


		
		
		GraphitiDiagramEditorInput input = new GraphitiDiagramEditorInput(createDiagram,selectedEObject
				);
		
		try {
			// Needs IEditorInput (GraphitiDiagramEditorInput) for "openEditor"
			GenericECPGraphitiDiagramEditor editor=(GenericECPGraphitiDiagramEditor) PlatformUI
					.getWorkbench()
					.getActiveWorkbenchWindow()
					.getActivePage()
					.openEditor(input,
							GenericECPGraphitiDiagramEditor.EDITOR_ID, true);
			
			String providerId = GraphitiUi.getExtensionManager()
					.getDiagramTypeProviderId(createDiagram.getDiagramTypeId());
			final IDiagramTypeProvider dtp = GraphitiUi.getExtensionManager()
					.createDiagramTypeProvider(createDiagram, providerId);
			
			AddContext context=new AddContext();
			
			context.setTargetContainer(createDiagram);
			
			editor.executeFeature(new LoadProjectFeature(dtp.getFeatureProvider(),selectedProject), context);
			
		} catch (PartInitException e) {
			System.out.println("Error");

		}
		return null;
	}

}
