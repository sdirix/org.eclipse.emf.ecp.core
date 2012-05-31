package org.eclipse.emf.ecp.explorereditorbridge;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.editor.EditorMetamodelContext;
import org.eclipse.emf.ecp.editor.EditorModelelementContext;
import org.eclipse.emf.ecp.editor.EditorModelelementContextListener;
import org.eclipse.emf.edit.domain.EditingDomain;

import java.util.Collection;
import java.util.Iterator;

public class EditorContext implements EditorModelelementContext
{

  private final EObject modelElement;

  private final ECPProject ecpProject;

  private MetaModeElementContext metaModeElementContext;

  public EditorContext(EObject modelElement, ECPProject ecpProject)
  {
    this.modelElement = modelElement;
    this.ecpProject = ecpProject;
    metaModeElementContext = new MetaModeElementContext();
  }

  public void addModelElementContextListener(EditorModelelementContextListener modelElementContextListener)
  {
    // TODO Auto-generated method stub

  }

  public void removeModelElementContextListener(EditorModelelementContextListener modelElementContextListener)
  {
    // TODO Auto-generated method stub

  }

  public Collection<EObject> getAllModelElementsbyClass(EClass clazz, boolean association)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public EditingDomain getEditingDomain()
  {
    // TODO Auto-generated method stub
    return ecpProject.getEditingDomain();
  }

  public EditorMetamodelContext getMetaModelElementContext()
  {
    // TODO Auto-generated method stub
    return metaModeElementContext;
  }

  public boolean contains(EObject eObject)
  {
    // TODO Auto-generated method stub
    return false;
  }

  public void dispose()
  {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.EditorModelelementContext#getLinkElements(org.eclipse.emf.ecore.EReference)
   */
  public Iterator<EObject> getLinkElements(EReference eReference)
  {
    return ecpProject.getLinkElements(modelElement, eReference);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.editor.EditorModelelementContext#getEcpProject()
   */
  public ECPProject getEcpProject()
  {
    return ecpProject;
  }
}
