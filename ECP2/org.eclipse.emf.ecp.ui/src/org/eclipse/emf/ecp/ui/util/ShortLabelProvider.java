/**
 * 
 */
package org.eclipse.emf.ecp.ui.util;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

//TODO: Review this class
/**
 * Label provider to shorten the getText Method.
 * 
 * @author helming
 */
public class ShortLabelProvider extends AdapterFactoryLabelProvider
{

  /**
   * Default constructor.
   */
  public ShortLabelProvider()
  {
    super(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));

  }

  /**
   * {@inheritDoc}
   * 
   * @see org.eclipse.emf.edit.provider.IItemLabelProvider#getText(java.lang.Object)
   * @override
   */
  @Override
  public String getText(Object object)
  {
    int limit = 30;
    String name = super.getText(object);
    if (name == null)
    {
      name = "";
    }
    if (name.length() > limit + 5)
    {
      name = name.substring(0, limit).concat("[...]");
    }
    return name;
  }
}
