/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.provider;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * This interface is only temporary, as it will be replaces with a proper model migrator in the next version.
 * 
 * @author Eugen Neufeld
 * 
 */
public interface Migrator {

	/**
	 * Migrate an {@link EObject} based on the attributes and missing features.
	 * 
	 * @param eObject the {@link EObject} to migrate
	 * @param anyAttribute the {@link FeatureMap} containing the missing attributes
	 * @param mixed the {@link FeatureMap} containing the missing references
	 */
	void migrate(EObject eObject, FeatureMap anyAttribute, FeatureMap mixed);

	/**
	 * @param eObject the {@link EObject} to migrate
	 * @return true if the instance can migrate the {@link EObject}
	 */
	boolean isApplicable(EObject eObject);
}
