package org.eclipse.emf.ecp.internal.core;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPUtil;

import org.eclipse.core.runtime.IAdapterFactory;

public class ECPProjectAdapterFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (ECPProject.class.isAssignableFrom(adapterType)) {
			return ECPUtil.getECPProjectManager().getProject(adaptableObject);
		}
		return null;
	}

	public Class[] getAdapterList() {
		// TODO Auto-generated method stub
		return new Class[] { ECPProject.class };
	}

}
