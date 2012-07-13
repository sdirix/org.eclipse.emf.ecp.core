/**
 * 
 */
package org.eclipse.emf.ecp.wizards;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.ui.common.CheckedModelElementHelper;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Eugen Neufeld
 */
public class FilterModelElementWizard extends Wizard
{

  private Collection<EPackage> ePackages;

  private Collection<EPackage> unsupportedEPackages;

  private Collection<EPackage> filteredEPackages;

  private Collection<EClass> filteredEClasses;

  private Object[] initialSelection;

  private CheckedModelElementHelper helper;

  private ECPProject ecpProject;

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
  @Override
  public boolean performFinish()
  {
    Object[] dialogSelection = helper.getChecked();
    Set<EPackage> filtererdPackages = new HashSet<EPackage>();
    Set<EClass> filtererdEClasses = new HashSet<EClass>();
    for (Object object : dialogSelection)
    {
      if (object instanceof EPackage)
      {
        filtererdPackages.add((EPackage)object);
      }
      else if (object instanceof EClass)
      {
        EClass eClass = (EClass)object;
        if (!filtererdPackages.contains(eClass.getEPackage()))
        {
          filtererdEClasses.add(eClass);
        }
      }
    }
    ecpProject.setFilteredPackages(filtererdPackages);
    ecpProject.setFilteredEClasses(filtererdEClasses);
    return true;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.wizard.Wizard#addPages()
   */
  @Override
  public void addPages()
  {
    super.addPages();
    WizardPage page = new WizardPage("Filter")
    {

      public void createControl(Composite parent)
      {
        helper = new CheckedModelElementHelper(ePackages, unsupportedEPackages, filteredEPackages, filteredEClasses);
        Composite composite = helper.createUI(parent);
        helper.setInitialSelection(initialSelection);
        setPageComplete(true);
        setControl(composite);
      }
    };
    addPage(page);
    page.setTitle("Select EPacakges and EClasses");
    page.setDescription("Select the pacakges that should be suggested when creating new model elements.");
  }

  public void init(Collection<EPackage> ePackages, Collection<EPackage> unsupportedEPackages,
      Collection<EPackage> filteredEPackages, Collection<EClass> filteredEClasses, ECPProject ecpProject)
  {
    this.ePackages = ePackages;
    this.unsupportedEPackages = unsupportedEPackages;
    this.filteredEPackages = filteredEPackages;
    this.filteredEClasses = filteredEClasses;
    this.ecpProject = ecpProject;
    Set<Object> initialSelectionSet = new HashSet<Object>();
    initialSelectionSet.addAll(ecpProject.getFilteredPackages());
    initialSelectionSet.addAll(ecpProject.getFilteredEClasses());
    initialSelection = initialSelectionSet.toArray();
  }

}
