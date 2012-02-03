package org.eclipse.emf.ecp.emfstore.core.internal;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.spi.core.DefaultProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.WorkspaceManager;
import org.eclipse.emf.emfstore.server.model.ProjectInfo;

public class EMFStoreProvider extends DefaultProvider
{
  public static final String NAME = "org.eclipse.emf.ecp.cdo.provider";

  public static EMFStoreProvider INSTANCE = new EMFStoreProvider();

  public static final String PROP_REPOSITORY_URL = "repositoryUrl";

  public static final String PROP_PORT = "port";

  public static final String PROP_PROJECTSPACEID = "projectSpaceID";

  public EMFStoreProvider()
  {
    super(NAME);
    INSTANCE = this;
  }

  public EMFStoreProvider(String name)
  {
    super(name);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void fillChildren(ECPModelContext context, Object parent, InternalChildrenList childrenList)
  {
    // TODO Auto-generated method stub
    if (parent instanceof InternalProject)
    {
      InternalProject project = (InternalProject)parent;
      String projectSpaceID = project.getProperties().getValue(EMFStoreProvider.PROP_PROJECTSPACEID);
      EList<ProjectSpace> projectSpaces = WorkspaceManager.getInstance().getCurrentWorkspace().getProjectSpaces();
      for (ProjectSpace projectSpace : projectSpaces)
      {
        if (projectSpace.getIdentifier().equals(projectSpaceID))
        {
          childrenList.addChildren(projectSpace.getProject().getModelElements());
        }
      }

    }
    if (parent instanceof InternalRepository)
    {
      EList<ProjectInfo> projectInfos = WorkspaceManager.getInstance().getCurrentWorkspace().getServerInfos().get(0)
          .getProjectInfos();
      for (ProjectInfo projectInfo : projectInfos)
      {
        childrenList.addChild(new EMFStoreProjectWrapper((InternalRepository)parent, projectInfo, WorkspaceManager
            .getInstance().getCurrentWorkspace().getServerInfos().get(0)));
      }

    }
    super.fillChildren(context, parent, childrenList);
  }

  public ECPProject createProject(String name, ECPProperties properties)
  {
    // TODO: implement WorkspaceProvider.createProject(name, properties)
    throw new UnsupportedOperationException();
  }

  public boolean hasUnsharedProjectSupport()
  {
    return false;
  }

  public void shareProject(ECPProject project, ECPRepository repository)
  {
    throw new UnsupportedOperationException();
  }

  public ECPRepository unshareProject(ECPProject project)
  {
    throw new UnsupportedOperationException();
  }

  public void addRootElement(ECPProject project, EObject rootElement)
  {
    String id = project.getProperties().getValue(EMFStoreProvider.PROP_PROJECTSPACEID);
    EList<ProjectSpace> projectSpaces = WorkspaceManager.getInstance().getCurrentWorkspace().getProjectSpaces();
    for (ProjectSpace projectSpace : projectSpaces)
    {
      if (projectSpace.getIdentifier().equals(id))
      {
        projectSpace.getProject().addModelElement(rootElement);
      }
    }
  }
}
