/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.diffmerge.swt;

import java.net.URL;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.BundleContext;

/**
 * Activator for this plugin.
 * 
 * @author Eugen Neufeld
 * 
 */
public class Activator extends Plugin {

	private static Activator instance;
	private ImageRegistry registry;

	// BEGIN SUPRESS CATCH EXCEPTION
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		instance = this;
		super.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		instance = null;
		if (registry != null) {
			registry.dispose();
		}
		super.stop(bundleContext);
	}

	// END SUPRESS CATCH EXCEPTION

	/**
	 * Finds and returns an image for the provided path.
	 * 
	 * @param path the path to get the image from
	 * @return the image or null if nothing could be found
	 */
	public static Image getImage(String path) {
		if (instance.registry == null) {
			instance.registry = new ImageRegistry();
		}
		Image image = instance.registry.get(path);
		if (image == null) {
			final URL url = instance.getBundle().getResource(path);
			if (url == null) {
				return null;
			}
			image = ImageDescriptor.createFromURL(url).createImage();
			instance.registry.put(path, image);
		}
		return image;
	}
}
