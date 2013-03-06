/**
 * 
 */
package org.eclipse.emf.ecp.edit.internal.swt;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * @author Eugen Neufeld
 * 
 */
public class ImageDescriptorToImage {
	private final ImageDescriptor imageDescriptor;

	public ImageDescriptorToImage(ImageDescriptor imageDescriptor) {
		this.imageDescriptor = imageDescriptor;
	}

	private Image image;

	public Image getImage() {
		if (image == null) {
			image = imageDescriptor.createImage();
		}
		return image;
	}

	public ImageDescriptor getImageDescriptor() {
		return imageDescriptor;
	}
}
