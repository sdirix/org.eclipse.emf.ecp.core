/**
 * 
 */
package org.eclipse.wb.swt;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

/**
 * @author Eugen Neufeld
 */
public abstract class GraphicHelper
{
  public static final GraphicHelper IMPL = ImplementationLoader.createInstance(GraphicHelper.class);

  public abstract Image getImage(Display display, ImageData data);

  public abstract GC getGC(Image image);
}
