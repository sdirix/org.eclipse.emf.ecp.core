package org.eclipse.emf.ecp.emfstore.core.internal;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotifyingList;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPMetamodelContext;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPProperties;
import org.eclipse.emf.ecp.internal.core.ECPProjectImpl;
import org.eclipse.emf.ecp.spi.core.DefaultProvider;
import org.eclipse.emf.ecp.spi.core.InternalProject;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.ecp.spi.core.util.InternalChildrenList;
import org.eclipse.emf.emfstore.client.model.ModelFactory;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.ServerInfo;
import org.eclipse.emf.emfstore.client.model.Usersession;
import org.eclipse.emf.emfstore.client.model.Workspace;
import org.eclipse.emf.emfstore.client.model.WorkspaceManager;
import org.eclipse.emf.emfstore.client.model.util.EMFStoreClientUtil;
import org.eclipse.emf.emfstore.server.exceptions.AccessControlException;
import org.eclipse.emf.emfstore.server.exceptions.EmfStoreException;
import org.eclipse.emf.emfstore.server.model.ProjectInfo;

import java.util.HashMap;
import java.util.Map;

public class EMFStoreProvider extends DefaultProvider
{
  public static final String NAME = "org.eclipse.emf.ecp.emfstore.provider";

  public static EMFStoreProvider INSTANCE = new EMFStoreProvider();

  public static final String PROP_REPOSITORY_URL = "repositoryUrl";

  public static final String PROP_PORT = "port";

  public static final String PROP_CERTIFICATE = "certificate";

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
  public ECPMetamodelContext getMetamodelContext(ECPProject ecpProject)
  {
    // TODO Auto-generated method stub
    return new DummyECPMetamodelContext(ecpProject);
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
      ServerInfo serverInfo = getServerInfo((InternalRepository)parent);
      if (serverInfo.getLastUsersession() == null)
      {
        Usersession usersession = ModelFactory.eINSTANCE.createUsersession();
        usersession.setServerInfo(serverInfo);
        usersession.setUsername("super");
        usersession.setPassword("super");
        serverInfo.setLastUsersession(usersession);
        try
        {
          usersession.logIn();
        }
        catch (AccessControlException ex)
        {
          // TODO Auto-generated catch block
          ex.printStackTrace();
        }
        catch (EmfStoreException ex)
        {
          // TODO Auto-generated catch block
          ex.printStackTrace();
        }
      }
      EList<ProjectInfo> projectInfos = serverInfo.getProjectInfos();
      for (ProjectInfo projectInfo : projectInfos)
      {
        childrenList.addChild(new EMFStoreProjectWrapper((InternalRepository)parent, projectInfo, serverInfo));
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

  private Map<ECPRepository, ServerInfo> cachedServerInfos = new HashMap<ECPRepository, ServerInfo>();

  private AdapterImpl adapter;

  public ServerInfo getServerInfo(ECPRepository repository)
  {
    if (!cachedServerInfos.containsKey(repository))
    {
      ServerInfo serverInfo = EMFStoreClientUtil.createServerInfo(
          repository.getProperties().getValue(EMFStoreProvider.PROP_REPOSITORY_URL), Integer.parseInt(repository
              .getProperties().getValue(EMFStoreProvider.PROP_PORT)),
          repository.getProperties().getValue(EMFStoreProvider.PROP_CERTIFICATE));
      cachedServerInfos.put(repository, serverInfo);
    }
    return cachedServerInfos.get(repository);

  }

  /**
   * Method for caching the loading of {@link ProjectSpace}
   * 
   * @return {@link EList} of {@link ProjectSpace}
   */
  public ProjectSpace getProjectSpace(ECPProject project)
  {
    if (!cachedProjectSpaces.containsKey(project))
    {

      EList<ProjectSpace> projectSpaces = WorkspaceManager.getInstance().getCurrentWorkspace().getProjectSpaces();
      for (ProjectSpace projectSpace : projectSpaces)
      {
        String projectSpaceID = project.getProperties().getValue(EMFStoreProvider.PROP_PROJECTSPACEID);
        if (projectSpace.getIdentifier().equals(projectSpaceID))
        {
          cachedProjectSpaces.put(project, projectSpace);
        }
      }
    }
    return cachedProjectSpaces.get(project);

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
      break;
    case CREATE:
      handleCreate(context);
      break;
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
  private void handleCreate(final ECPModelContext context)
  {

    if (context instanceof InternalRepository)
    {

      InternalRepository repository = (InternalRepository)context;
      ServerInfo serverInfo = getServerInfo(repository);
      Workspace workspace = WorkspaceManager.getInstance().getCurrentWorkspace();
      workspace.getServerInfos().add(serverInfo);
      workspace.save();
    }
    else if (context instanceof InternalProject)
    {
      InternalProject project = (InternalProject)context;
      ServerInfo serverInfo = getServerInfo(project.getRepository());
      if (serverInfo.getLastUsersession() != null && serverInfo.getLastUsersession().isLoggedIn())
      {
        for (ProjectInfo projectInfo : serverInfo.getProjectInfos())
        {
          if (projectInfo.getProjectId().getId().equals(project.getProperties().getValue(PROP_PROJECTSPACEID)))
          {
            try
            {
              ProjectSpace projectSpace = WorkspaceManager.getInstance().getCurrentWorkspace()
                  .checkout(serverInfo.getLastUsersession(), projectInfo);

              WorkspaceManager.getInstance().getCurrentWorkspace().save();
              if (!cachedProjectSpaces.containsKey(project))
              {
                cachedProjectSpaces.put(project, projectSpace);
              }
            }
            catch (EmfStoreException ex)
            {
              // TODO Auto-generated catch block
              ex.printStackTrace();
            }

          }
        }

      }
    }
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

  /**
   * @param repository
   */
  public EList<ProjectInfo> getAllProjects(ECPRepository repository, String user, String password)
  {
    // TODO Auto-generated method stub
    ServerInfo serverInfo = getServerInfo(repository);
    if (serverInfo.getLastUsersession() == null)
    {
      Usersession usersession = ModelFactory.eINSTANCE.createUsersession();
      usersession.setServerInfo(serverInfo);
      usersession.setUsername(user);
      usersession.setPassword(password);
      serverInfo.setLastUsersession(usersession);
    }
    if (!serverInfo.getLastUsersession().isLoggedIn())
    {
      try
      {
        serverInfo.getLastUsersession().logIn();
      }
      catch (Exception ex)
      {
        // TODO Auto-generated catch block
        ex.printStackTrace();
      }
    }

    return serverInfo.getProjectInfos();
  }
}
