package org.eclipse.emf.ecp.graphiti.internal.integration;


public class ECPImageProvider extends AbstractImageProvider {

	@Override
	protected void addAvailableImages() {
		addImageFilePath("addEObject", "icons/add.png");
		addImageFilePath("containment", "icons/connect.png");
		addImageFilePath("reference", "icons/link.png");
	}

}
