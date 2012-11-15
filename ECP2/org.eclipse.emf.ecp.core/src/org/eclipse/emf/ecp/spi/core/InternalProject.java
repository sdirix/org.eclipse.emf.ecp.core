/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.spi.core;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.util.ECPProjectAware;
import org.eclipse.emf.ecp.internal.core.util.PropertiesStore.StorableElement;

/**
 * @author Eike Stepper
 * @author Eugen Neufeld
 */
public interface InternalProject extends ECPProject, ECPProjectAware, StorableElement, Cloneable {
	public InternalRepository getRepository();

	public InternalProvider getProvider();

	public Object getProviderSpecificData();

	public void setProviderSpecificData(Object data);

	public void notifyObjectsChanged(Object[] objects, boolean structural);

	public void undispose(InternalRepository repository);

	/**
	 * @return
	 */
	public InternalProject clone();
}
