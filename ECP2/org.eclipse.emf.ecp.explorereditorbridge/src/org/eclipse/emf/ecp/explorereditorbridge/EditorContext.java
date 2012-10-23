package org.eclipse.emf.ecp.explorereditorbridge;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProjectManager.Listener;
import org.eclipse.emf.ecp.editor.EditorMetamodelContext;
import org.eclipse.emf.ecp.editor.EditorModelelementContext;
import org.eclipse.emf.ecp.editor.EditorModelelementContextListener;
import org.eclipse.emf.ecp.ui.util.ActionHelper;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class EditorContext implements EditorModelelementContext {

	private final EObject modelElement;

	private final ECPProject ecpProject;

	private MetaModeElementContext metaModeElementContext;

	private List<EditorModelelementContextListener> contextListeners = new ArrayList<EditorModelelementContextListener>();

	public EditorContext(EObject modelElement, ECPProject ecpProject) {
		this.modelElement = modelElement;
		this.ecpProject = ecpProject;
		metaModeElementContext = new MetaModeElementContext();

		ECPProjectManager.INSTANCE.addListener(new Listener() {

			public void projectsChanged(ECPProject[] oldProjects, ECPProject[] newProjects) throws Exception {
				// TODO Auto-generated method stub

			}

			public void projectChanged(ECPProject project, boolean opened) throws Exception {
				if (!opened) {
					for (EditorModelelementContextListener contextListener : contextListeners) {
						contextListener.onContextDeleted();
					}
					dispose();
				}
			}

			public void objectsChanged(ECPProject project, Object[] objects) throws Exception {
				// do nothing
			}
		});
	}

	public void addModelElementContextListener(EditorModelelementContextListener modelElementContextListener) {
		contextListeners.add(modelElementContextListener);
	}

	public void removeModelElementContextListener(EditorModelelementContextListener modelElementContextListener) {
		contextListeners.remove(modelElementContextListener);
	}

	public Collection<EObject> getAllModelElementsbyClass(EClass clazz, boolean association) {
		// TODO Auto-generated method stub
		return null;
	}

	public EditingDomain getEditingDomain() {
		if (ecpProject != null) {
			return ecpProject.getEditingDomain();
		}
		return AdapterFactoryEditingDomain.getEditingDomainFor(modelElement);

	}

	public EditorMetamodelContext getMetaModelElementContext() {
		// TODO Auto-generated method stub
		return metaModeElementContext;
	}

	public boolean contains(EObject eObject) {
		// TODO Auto-generated method stub
		return false;
	}

	public void dispose() {

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.EditorModelelementContext#getLinkElements(org.eclipse.emf.ecore.EReference)
	 */
	public Iterator<EObject> getLinkElements(EReference eReference) {
		return ecpProject.getLinkElements(modelElement, eReference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.EditorModelelementContext#openEditor(org.eclipse.emf.ecore.EObject)
	 */
	public void openEditor(EObject o, String source) {
		ActionHelper.openModelElement(o, source, ecpProject);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.EditorModelelementContext#addModelElement(org.eclipse.emf.ecore.EObject)
	 */
	public void addModelElement(EObject newMEInstance) {
		ecpProject.addModelElement(newMEInstance);
	}
}
