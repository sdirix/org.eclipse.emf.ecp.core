/**
 * 
 */
package org.eclipse.emf.ecp.core.util.observer;

import org.eclipse.emf.ecp.core.ECPRepository;

/**
 * @author Edgar
 * 
 */
public interface IECPRepositoriesChangedObserver extends IECPObserver {

	void repositoriesChanged(ECPRepository[] oldRepositories, ECPRepository[] newRepositories) throws Exception;

	void objectsChanged(ECPRepository repository, Object[] objects) throws Exception;
}
