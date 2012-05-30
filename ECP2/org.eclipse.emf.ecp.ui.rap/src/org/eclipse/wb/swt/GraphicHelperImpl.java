package org.eclipse.wb.swt;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

public class GraphicHelperImpl extends GraphicHelper{

	@Override
	public Image getImage(Display display, ImageData data) {
		return new Image(display, data);
	}

	@Override
	public GC getGC(Image image) {
		return  new GC(image.getDevice());
	}

}
