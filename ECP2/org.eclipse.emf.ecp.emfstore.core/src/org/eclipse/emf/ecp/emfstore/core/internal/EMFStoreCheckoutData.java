/**
 * 
 */
package org.eclipse.emf.ecp.emfstore.core.internal;

import org.eclipse.emf.emfstore.client.model.ServerInfo;
import org.eclipse.emf.emfstore.server.model.ProjectInfo;

/**
 * @author Eugen Neufeld
 */
public class EMFStoreCheckoutData
{
  private final ServerInfo serverInfo;

  private final ProjectInfo projectInfo;

  /**
   * @param serverInfo
   * @param projectInfo
   */
  public EMFStoreCheckoutData(ServerInfo serverInfo, ProjectInfo projectInfo)
  {
    super();
    this.serverInfo = serverInfo;
    this.projectInfo = projectInfo;
  }

  /**
   * @return the serverInfo
   */
  public ServerInfo getServerInfo()
  {
    return serverInfo;
  }

  /**
   * @return the projectInfo
   */
  public ProjectInfo getProjectInfo()
  {
    return projectInfo;
  }

}
