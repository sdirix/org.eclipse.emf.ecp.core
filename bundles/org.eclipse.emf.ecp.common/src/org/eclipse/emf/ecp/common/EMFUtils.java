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
package org.eclipse.emf.ecp.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;

/**
 * Util class for basic EMF.
 * 
 * @author Eugen Neufeld
 * 
 */
public final class EMFUtils {

	private EMFUtils() {
	}

	/**
	 * This method looks through all known {@link EPackage}s to find all subclasses for the provided super class.
	 * 
	 * @param superClass
	 *            - the class for which to get the subclasses
	 * @return a {@link Collection} of {@link EClass EClasses}
	 */
	public static Collection<EClass> getSubClasses(EClass superClass) {
		final Collection<EClass> classes = new HashSet<EClass>();

		// avoid ConcurrentModificationException while iterating over the registry's key set
		final List<String> keySet = new ArrayList<String>(Registry.INSTANCE.keySet());
		for (final String nsURI : keySet)
		{
			final EPackage ePackage = Registry.INSTANCE.getEPackage(nsURI);
			for (final EClassifier eClassifier : ePackage.getEClassifiers()) {
				if (eClassifier instanceof EClass) {
					final EClass eClass = (EClass) eClassifier;
					if (superClass.isSuperTypeOf(eClass) && !eClass.isAbstract() && !eClass.isInterface()) {
						classes.add(eClass);
					}
				}
			}
		}
		return classes;
	}
}
