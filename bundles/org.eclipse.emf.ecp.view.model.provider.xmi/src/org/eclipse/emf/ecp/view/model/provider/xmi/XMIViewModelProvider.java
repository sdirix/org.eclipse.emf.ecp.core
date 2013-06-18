package org.eclipse.emf.ecp.view.model.provider.xmi;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecp.internal.ui.view.IViewProvider;
import org.eclipse.emf.ecp.internal.view.model.provider.xmi.Activator;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewPackage;

public abstract class XMIViewModelProvider implements IViewProvider{

	
	public View generate(EObject eObject) {
		ResourceSet resourceSet = new ResourceSetImpl();
		Map<String, Object> extensionToFactoryMap = resourceSet
				.getResourceFactoryRegistry().getExtensionToFactoryMap();
		extensionToFactoryMap.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
				new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(ViewPackage.eNS_URI,
				ViewPackage.eINSTANCE);
		Resource resource = resourceSet.createResource(URI.createFileURI(getXMIPath()));
		try {
			resource.load(null);
		} catch (IOException exception) {
			Activator.log(exception);
		}
		View result = EcoreUtil.copy((View) resource.getContents().get(0));
		return result;
	}
	
	protected abstract String getXMIPath();
}
