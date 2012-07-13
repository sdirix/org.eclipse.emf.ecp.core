/**
 * 
 */
package org.eclipse.emf.ecp.wizards;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPProviderRegistry;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.internal.ui.Activator;
import org.eclipse.emf.ecp.spi.core.InternalProvider;
import org.eclipse.emf.ecp.spi.core.InternalProvider.LifecycleEvent;
import org.eclipse.emf.ecp.ui.common.UICreateProject;
import org.eclipse.emf.ecp.ui.common.UICreateProject.CreateProjectChangeListener;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugen Neufeld
 */
public class CreateProjectWizard extends Wizard
{

  private UICreateProject project;

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
  @Override
  public boolean performFinish()
  {
    ECPProvider selectedProvider = project.getProvider();
    if (selectedProvider == null)
    {
      return false;
    }
    ECPProperties projectProperties = ECPUtil.createProperties();

    String projectName = project.getProjectName();
    ECPProject project = ECPProjectManager.INSTANCE.createProject(selectedProvider, projectName, projectProperties);

    ((InternalProvider)selectedProvider).handleLifecycle(project, LifecycleEvent.CREATE);
    project.open();
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
    List<ECPProvider> providers = new ArrayList<ECPProvider>();
    for (ECPProvider provider : ECPProviderRegistry.INSTANCE.getProviders())
    {
      if (provider.hasUnsharedProjectSupport())
      {
        providers.add(provider);
      }
    }
    project = new UICreateProject(providers);
    WizardPage wp = new WizardPage("CreateProject")
    {

      public void createControl(Composite parent)
      {
        Composite composite = project.createUI(parent);
        project.setListener(new CreateProjectChangeListener()
        {

          public void providerChanged(ECPProvider provider)
          {
          }

          public void projectNameChanged(String projectName)
          {
            if (projectName != null)
            {
              setPageComplete(true);
            }
            else
            {
              setPageComplete(false);
            }
          }
        });
        setPageComplete(false);
        setControl(composite);
      }
    };
    addPage(wp);
    String title = "Create Project";
    String message = "Select a provider and set the project name.";
    if (providers.size() == 1)
    {
      title = "Create " + providers.get(0).getLabel() + " Project";
      message = "Set the project name.";
    }
    wp.setTitle(title);
    wp.setImageDescriptor(Activator.getImageDescriptor("icons/checkout_project_wiz.png"));
    wp.setMessage(message);
  }

}
