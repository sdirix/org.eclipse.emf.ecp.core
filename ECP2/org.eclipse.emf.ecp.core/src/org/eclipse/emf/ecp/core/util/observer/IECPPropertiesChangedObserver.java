/**
 * 
 */
package org.eclipse.emf.ecp.core.util.observer;

import java.util.Map;

/**
 * @author Edgar
 * 
 */
public interface IECPPropertiesChangedObserver extends IECPObserver {

	void propertiesChanged(Map.Entry<String, String>[] oldProperties, Map.Entry<String, String>[] newProperties);
}
