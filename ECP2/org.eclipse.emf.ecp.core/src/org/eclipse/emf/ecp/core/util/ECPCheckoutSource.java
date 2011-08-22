/**
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.core.util;

import org.eclipse.emf.ecp.core.ECPProject;

/**
 * @author Eike Stepper
 */
public interface ECPCheckoutSource extends ECPRepositoryAware
{
  public String getDefaultCheckoutName();

  public ECPProject checkout(String projectName, ECPProperties projectProperties);
}
