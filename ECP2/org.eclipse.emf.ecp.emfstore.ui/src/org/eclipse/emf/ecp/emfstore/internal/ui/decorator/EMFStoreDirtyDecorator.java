/**
 * 
 */
package org.eclipse.emf.ecp.emfstore.internal.ui.decorator;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.emfstore.internal.ui.Activator;
import org.eclipse.emf.ecp.spi.core.InternalProject;

import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

/**
 * @author Eugen Neufeld
 */
public class EMFStoreDirtyDecorator implements ILightweightLabelDecorator
{

  private String providerLabel = "org.eclipse.emf.ecp.emfstore.provider";

  private String dirtyPath = "icons/dirty.png";

  /**
   * {@inheritDoc}
   * 
   * @see org.eclipse.jface.viewers.ILightweightLabelDecorator#decorate(java.lang .Object,
   *      org.eclipse.jface.viewers.IDecoration)
   * @param element
   *          element
   * @param decoration
   *          decoration
   */
  public void decorate(Object element, IDecoration decoration)
  {
    /**
     * Checks that the element is an IResource with the 'Read-only' attribute and adds the decorator based on the
     * specified image description and the integer representation of the placement option.
     */

    if (element instanceof ECPProject)
    {
      ECPProject project = (ECPProject)element;
      ECPProvider provider = project.getProvider();
      if (provider != null && providerLabel.equalsIgnoreCase(provider.getName())
          && EMFStoreProvider.getProjectSpace((InternalProject)project).isDirty())
      {
        decoration.addOverlay(Activator.getImageDescriptor(dirtyPath), IDecoration.BOTTOM_LEFT);
      }
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse. jface.viewers.ILabelProviderListener)
   */
  public void addListener(ILabelProviderListener listener)
  {
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
   */
  public void dispose()
  {
  }

  /**
   * . {@inheritDoc}
   * 
   * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang .Object, java.lang.String)
   */
  public boolean isLabelProperty(Object element, String property)
  {
    return false;
  }

  /**
   * . {@inheritDoc}
   * 
   * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse .jface.viewers.ILabelProviderListener)
   */
  public void removeListener(ILabelProviderListener listener)
  {
  }
}
