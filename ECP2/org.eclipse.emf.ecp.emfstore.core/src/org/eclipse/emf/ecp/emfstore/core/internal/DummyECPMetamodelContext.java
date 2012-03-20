/**
 * 
 */
package org.eclipse.emf.ecp.emfstore.core.internal;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecp.core.ECPMetamodelContext;
import org.eclipse.emf.ecp.core.ECPProject;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Eugen
 */
public class DummyECPMetamodelContext implements ECPMetamodelContext
{

  private ECPProject ecpProject;

  /**
   * @param ecpProject
   */
  public DummyECPMetamodelContext(ECPProject ecpProject)
  {
    this.ecpProject = ecpProject;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.core.ECPMetamodelContext#getAllRootEClasses()
   */
  public Set<EClass> getAllRootEClasses()
  {

    Set<EClass> result = new HashSet<EClass>();

    // obtain all EClasses that are direct contents of the EPackage
    for (EClassifier classifier : Registry.INSTANCE.getEPackage("http://org/eclipse/example/bowling").getEClassifiers())
    {
      if (classifier instanceof EClass)
      {
        result.add((EClass)classifier);
      }
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.core.ECPMetamodelContext#isNonDomainElement(org.eclipse.emf.ecore.EClass)
   */
  public boolean isNonDomainElement(EClass eClass)
  {
    // TODO Auto-generated method stub
    return false;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.ecp.core.ECPMetamodelContext#isGuessed()
   */
  public boolean isGuessed()
  {
    // TODO Auto-generated method stub
    return false;
  }

}
