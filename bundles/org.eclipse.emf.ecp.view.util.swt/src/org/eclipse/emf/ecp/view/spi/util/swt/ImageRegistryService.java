package org.eclipse.emf.ecp.view.spi.util.swt;

import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

public interface ImageRegistryService {

	Image getImage(Bundle bundle, String path);
}
