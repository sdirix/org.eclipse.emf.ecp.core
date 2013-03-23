/**
 * 
 */
package org.eclipse.emf.ecp.emfstore.internal.ui.decorator;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.observer.ESShareObserver;
import org.eclipse.emf.emfstore.server.model.versionspec.ESPrimaryVersionSpec;

import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * @author jfaltermeier
 * 
 */
public class ProjectEMFStoreBranchDecorator implements ILabelDecorator, ESShareObserver {

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {

	}

	public void shareDone(ESLocalProject localProject) {
		decorateText(localProject.getProjectName(), localProject);
	}

	public Image decorateImage(Image image, Object element) {
		return image;
	}

	public String decorateText(String text, Object element) {
		ESLocalProject localProject;
		if (element instanceof ESLocalProject) {
			localProject = (ESLocalProject) element;
		} else if (element instanceof ECPProject) {
			ECPProvider ecpProvider = ECPProviderRegistry.INSTANCE.getProvider(EMFStoreProvider.NAME);
			EMFStoreProvider provider = (EMFStoreProvider) ECPUtil.getResolvedElement(ecpProvider);
			InternalProject project = (InternalProject) element;
			localProject = provider.getProjectSpace(project);
		} else {
			return text;
		}

		if (localProject == null) {
			return text;
		}

		if (localProject.isShared()) {
			ESPrimaryVersionSpec versSpec;
			try {
				versSpec = localProject.getBaseVersion();
				return text + " [" + versSpec.getBranch() + ", Version " + versSpec.getIdentifier() + "]";
			} catch (NullPointerException e) {
				// possible npe because share is async. -> shareobserver
			}

		}
		return text;
	}

}
