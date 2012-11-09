package org.eclipse.emf.ecp.emfstore.internal.ui.decorator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.util.observer.IECPProjectsChangedObserver;

import java.util.HashSet;

/**
 * Project change observer that marks elements as dirty.
 */
public class EMFStoreDirtyObserver implements IECPProjectsChangedObserver {

	private HashSet<Class<?>> excludedTypes;

	/**
	 * Default constructor.
	 */
	public EMFStoreDirtyObserver() {
		excludedTypes = new HashSet<Class<?>>();
		excludedTypes.add(ECPProject.class);

		for (ECPProvider provider : ECPProviderRegistry.INSTANCE.getProviders()) {
			excludedTypes.add(provider.getContainerClass());
		}
	}

	// BEGIN SUPRESS CATCH EXCEPTION
	public void projectsChanged(ECPProject[] oldProjects, ECPProject[] newProjects) throws Exception {

	}

	public void projectChanged(ECPProject project, boolean opened) throws Exception {

	}

	public void objectsChanged(ECPProject project, Object[] objects) throws Exception {

		for (Object object : objects) {

			if (!(object instanceof EObject)) {
				continue;
			}

			EObject eObject = (EObject) object;

			if (project.contains(eObject)) {
				EMFStoreDirtyDecoratorCachedTree.getInstance().update(eObject, Boolean.TRUE, excludedTypes);
			} else {
				if (eObject.eContainer() != null) {
					EMFStoreDirtyDecoratorCachedTree.getInstance().update(eObject.eContainer(), Boolean.TRUE,
						excludedTypes);
					EMFStoreDirtyDecoratorCachedTree.getInstance().remove(eObject, excludedTypes);
				}
			}
		}
	}
	// END SUPRESS CATCH EXCEPTION
}