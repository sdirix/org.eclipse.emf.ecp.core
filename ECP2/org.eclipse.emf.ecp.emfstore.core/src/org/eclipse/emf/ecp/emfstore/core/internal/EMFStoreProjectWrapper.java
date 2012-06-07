package org.eclipse.emf.ecp.emfstore.core.internal;

import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPCheckoutSource;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;

public class EMFStoreProjectWrapper implements ECPCheckoutSource
{

  private final InternalRepository repository;

  private final EMFStoreCheckoutData checkoutData;

  // private final ProjectInfo projectInfo;
  //
  // private final ServerInfo serverInfo;

  public EMFStoreProjectWrapper(InternalRepository repository, EMFStoreCheckoutData checkoutData)
  {
    this.repository = repository;
    // this.projectInfo = projectInfo;
    // this.serverInfo = serverInfo;
    this.checkoutData = checkoutData;
  }

  public ECPRepository getRepository()
  {
    // TODO Auto-generated method stub
    return repository;
  }

  public ECPProvider getProvider()
  {
    return repository.getProvider();
  }

  public String getDefaultCheckoutName()
  {
    return checkoutData.getProjectInfo().getName();
  }

  public void checkout(String projectName, ECPProperties projectProperties)
  {
    ProjectSpace projectSpace = EMFStoreProvider.INSTANCE.getUIProvider().getAdapter(checkoutData, ProjectSpace.class);
    if (projectSpace != null)
    {
      projectProperties.addProperty(EMFStoreProvider.PROP_PROJECTSPACEID, projectSpace.getIdentifier());
    }
    ECPProjectManager.INSTANCE.createProject(getRepository(), projectName, projectProperties);
  }

}
