/**
 * 
 */
package org.eclipse.emf.ecp.emfstore.ui.search;

import java.lang.annotation.Inherited;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.util.ECPModelContextProvider;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.ui.util.ActionHelper;
import org.eclipse.emf.ecp.ui.views.TreeView;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
/**
 * 
 * @author Eugen Neufeld
 * This class shows the search model element dialog for emfstore projects
 */
public class SearchModelElementHandler extends AbstractHandler {

	/**
	 * {@link Inherited}
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection=(IStructuredSelection)HandlerUtil.getCurrentSelection(event);

		//TODO replace code with util call
		ECPModelContextProvider contextProvider = (ECPModelContextProvider)((TreeView)HandlerUtil.getActivePart(event))
		        .getViewer().getContentProvider();
		

		
		InternalProject project = (InternalProject) contextProvider.getModelContext(selection.getFirstElement());
		
		ProjectSpace projectSpace=EMFStoreProvider.getProjectSpace(project);
		
		Set<EObject> eObjects=projectSpace.getProject().getAllModelElements();
		
		if (project == null) {
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Information", "You must first select the Project.");
		} else {
			ElementListSelectionDialog dialog = new ElementListSelectionDialog(HandlerUtil.getActiveShell(event), new AdapterFactoryLabelProvider(
					new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE)));
				dialog.setElements(eObjects.toArray());
				dialog.setMultipleSelection(false);
				dialog.setMessage("Enter model element name prefix or pattern (e.g. *Trun?)");
				dialog.setTitle("Search Model Element");
				if (dialog.open() == Dialog.OK) {
					Object[] selections = dialog.getResult();

					if (selections != null && selections.length == 1 && selections[0] instanceof EObject) {
						ActionHelper.openModelElement((EObject) selections[0],
							"org.eclipse.emf.ecp.ui.commands.SearchModelElementHandler", project);
					}
				}
		}
		
		return null;
	}

}
