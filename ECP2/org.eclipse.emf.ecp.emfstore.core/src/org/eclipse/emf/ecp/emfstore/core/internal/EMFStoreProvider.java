package org.eclipse.emf.ecp.emfstore.core.internal;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotifyingList;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.internal.core.ECPProjectImpl;
import org.eclipse.emf.ecp.spi.core.DefaultProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.WorkspaceManager;
import org.eclipse.emf.emfstore.server.model.ProjectInfo;

import java.util.HashMap;
import java.util.Map;

public class EMFStoreProvider extends DefaultProvider
{
  public static final String NAME = "org.eclipse.emf.ecp.emfstore.provider";

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
      ProjectSpace projectSpace = getProjectSpace(project);

      childrenList.addChildren(projectSpace.getProject().getModelElements());
      // TODO: provide interface at emfstore
      NotifyingList<EObject> modelElements = (NotifyingList<EObject>)projectSpace.getProject().getModelElements();
      modelElements.getNotifier();

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
    return new ECPProjectImpl(this, name, properties);
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

  public EList<EObject> getElements(ECPProject ecpProject)
  {
    ProjectSpace projectSpace = getProjectSpace(ecpProject);
    return projectSpace.getProject().getModelElements();
  }

  private Map<ECPProject, ProjectSpace> cachedProjectSpaces = new HashMap<ECPProject, ProjectSpace>();

  private AdapterImpl adapter;

  /**
   * Method for caching the loading of {@link ProjectSpace}
   * 
   * @return {@link EList} of {@link ProjectSpace}
   */
  private ProjectSpace getProjectSpace(ECPProject ecpProject)
  {
    if (!cachedProjectSpaces.containsKey(ecpProject))
    {

      EList<ProjectSpace> projectSpaces = WorkspaceManager.getInstance().getCurrentWorkspace().getProjectSpaces();
      for (ProjectSpace projectSpace : projectSpaces)
      {
        String projectSpaceID = ecpProject.getProperties().getValue(EMFStoreProvider.PROP_PROJECTSPACEID);
        if (projectSpace.getIdentifier().equals(projectSpaceID))
        {
          cachedProjectSpaces.put(ecpProject, projectSpace);
        }
      }
    }
    return cachedProjectSpaces.get(ecpProject);

  }

  @Override
  public void handleLifecycle(ECPModelContext context, LifecycleEvent event)
  {
    switch (event)
    {
    case INIT:
      handleInit(context);
      break;
    case DISPOSE:
      handelDispose(context);
    default:
      break;
    }
    // TODO Trace properly
    String providerClass = getClass().getSimpleName();
    String contextClass = context.getClass().getSimpleName();
    System.out.println(providerClass + " received " + event + " for " + contextClass + " " + context);
  }

  /**
   * @param context
   */
  private void handelDispose(ECPModelContext context)
  {
    if (context instanceof InternalProject)
    {
      ProjectSpace projectSpace = getProjectSpace((ECPProject)context);

      projectSpace.getProject().eAdapters().remove(adapter);

    }

  }

  /**
   * @param context
   */
  private void handleInit(final ECPModelContext context)
  {
    if (context instanceof InternalProject)
    {
      ProjectSpace projectSpace = getProjectSpace((ECPProject)context);

      adapter = new AdapterImpl()
      {
        @Override
        public void notifyChanged(Notification notification)
        {
          ((InternalProject)context).notifyObjectsChanged(new Object[] { context });
        }
      };

      projectSpace.getProject().eAdapters().add(adapter);

    }

  }
}
