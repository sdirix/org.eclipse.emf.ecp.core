/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.model.util;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.model.VControl;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.model.VElement;

/**
 * This Util class provides common methods used often when working with the view model.
 * 
 * @author Eugen Neufeld
 * 
 */
public final class ViewModelUtil {

	private ViewModelUtil() {

	}

	/**
	 * Resolves all {@link VDomainModelReference VDomainModelReferences} which are contained under the provided
	 * renderable using the provided domain model.
	 * 
	 * @param renderable the renderable to analyze for {@link VDomainModelReference VDomainModelReferences}
	 * @param domainModelRoot the domain model to use for resolving
	 */
	public static void resolveDomainReferences(VElement renderable, EObject domainModelRoot) {
		final TreeIterator<EObject> eAllContents = renderable.eAllContents();
		while (eAllContents.hasNext()) {
			final EObject eObject = eAllContents.next();

			if (VControl.class.isInstance(eObject)) {
				final VControl control = (VControl) eObject;
				final boolean resolve = control.getDomainModelReference().resolve(domainModelRoot);
				if (!resolve) {
					// log
					System.out.println("Not resolved: " + control.getDomainModelReference() + " on control "
						+ control);
				}
			}
		}
	}
}
