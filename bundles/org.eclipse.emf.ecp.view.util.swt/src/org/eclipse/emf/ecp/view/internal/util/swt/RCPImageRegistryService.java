package org.eclipse.emf.ecp.view.internal.util.swt;

import java.net.URL;

import org.eclipse.emf.ecp.view.spi.util.swt.ImageRegistryService;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

public class RCPImageRegistryService implements ImageRegistryService{

	private ImageRegistry registry;
	
	public RCPImageRegistryService(){
		registry=new ImageRegistry();
	}
	
	@Override
	public Image getImage(Bundle bundle, String path) {
		Image image = registry.get(path);
		if (image == null) {
			final URL url = bundle.getResource(path);
			if (url == null) {
				return null;
			}
			image = ImageDescriptor.createFromURL(url).createImage();
			registry.put(path, image);
		}
		return image;
	}

}
