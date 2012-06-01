/**
 * 
 */
package org.eclipse.emf.ecp.emfstore.internal.ui.property;

import org.eclipse.emf.ecp.core.ECPRepository;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.spi.core.InternalRepository;
import org.eclipse.emf.emfstore.client.model.ServerInfo;
import org.eclipse.emf.emfstore.client.model.Usersession;
import org.eclipse.emf.emfstore.client.model.util.EMFStoreCommandWithResult;

import org.eclipse.core.expressions.PropertyTester;

/**
 * @author Eugen Neufeld
 */
public class EMFStoreIsLoggedInTester extends PropertyTester
{

  /**
   * {@inheritDoc}
   * 
   * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[],
   *      java.lang.Object)
   */
  public boolean test(Object receiver, String property, Object[] args, final Object expectedValue)
  {
    if (receiver instanceof ECPRepository && expectedValue instanceof Boolean)
    {
      final ECPRepository ecpRepository = (ECPRepository)receiver;
      final ServerInfo serverInfo = EMFStoreProvider.getServerInfo((InternalRepository)ecpRepository);
      EMFStoreCommandWithResult<Boolean> command = new EMFStoreCommandWithResult<Boolean>()
      {
        @Override
        protected Boolean doRun()
        {
          Usersession usersession = serverInfo.getLastUsersession();
          Boolean ret = new Boolean(usersession != null && usersession.isLoggedIn());
          return ret.equals(expectedValue);
        }
      };
      Boolean result = command.run(false);
      return result;
    }
    return false;
  }

}
