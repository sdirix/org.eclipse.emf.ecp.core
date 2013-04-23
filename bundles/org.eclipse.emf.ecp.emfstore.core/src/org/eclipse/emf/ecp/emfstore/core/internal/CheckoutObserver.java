package org.eclipse.emf.ecp.emfstore.core.internal;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProjectManager.ProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.observer.ESCheckoutObserver;

import java.text.DateFormat;
import java.util.Date;

public class CheckoutObserver implements ESCheckoutObserver {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.emfstore.client.observer.ESCheckoutObserver#checkoutDone(org.eclipse.emf.emfstore.client.
	 * ESLocalProject)
	 */
	public void checkoutDone(ESLocalProject project) {

		boolean ecpProjectExists = false;

		for (ECPProject ecpProject : ECPProjectManager.INSTANCE.getProjects()) {
			InternalProject internalProject = (InternalProject) ecpProject;
			Object localProject = internalProject.getProviderSpecificData();
			if (localProject instanceof ESLocalProject) {
				if (localProject == project) {
					ecpProjectExists = true;
					break;
				}
			}
		}

		if (!ecpProjectExists) {
			try {
				ECPProjectManager.INSTANCE.createProject(EMFStoreProvider.INSTANCE.getProvider(),
					project.getProjectName() + "@" + createDateString(), createECPProperties(project));
			} catch (ProjectWithNameExistsException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
	}

	private ECPProperties createECPProperties(ESLocalProject project) {
		ECPProperties projectProperties = ECPUtil.createProperties();
		projectProperties.addProperty(EMFStoreProvider.PROP_PROJECTSPACEID, project.getLocalProjectId().getId());
		return projectProperties;
	}

	private String createDateString() {
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
		return format.format(new Date());
	}

}
