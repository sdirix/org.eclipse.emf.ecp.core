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
package org.eclipse.emf.ecp.internal.core.util;

import org.eclipse.emf.ecp.core.util.ECPElement;
import org.eclipse.emf.ecp.core.util.ECPUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Eugen Neufeld
 * 
 */
public final class InternalUtil {

	private InternalUtil() {
	}

	/**
	 * Returns the Set of names of a set of {@link ECPElement ECPElements}.
	 * 
	 * @param elements the set of elements to get the names for
	 * @return the set of names of this elements
	 */
	public static Set<String> getElementNames(Set<? extends ECPElement> elements) {
		Set<String> names = new HashSet<String>();
		for (ECPElement element : elements) {
			names.add(element.getName());
		}

		return names;
	}

	/**
	 * Finds the set of all Elements that are in the new array but not in the old.
	 * 
	 * @param oldElements the array containing the old elements
	 * @param newElements the array containing the new elements
	 * @param <E> the type of the elements
	 * @return the Set<E> of elements which are only in the newElements array
	 */
	public static <E> Set<E> getAddedElements(E[] oldElements, E[] newElements) {
		return getRemovedElements(newElements, oldElements);
	}

	/**
	 * Finds the set of all Elements that are in the old array but not in the new.
	 * 
	 * @param oldElements the array containing the old elements
	 * @param newElements the array containing the new elements
	 * @param <E> the type of the elements
	 * @return the Set<E> of elements which are only in the oldElements array
	 */
	public static <E> Set<E> getRemovedElements(E[] oldElements, E[] newElements) {
		Set<E> removed = new HashSet<E>();
		for (int i = 0; i < oldElements.length; i++) {
			E element = oldElements[i];
			if (!ECPUtil.containsElement(newElements, element)) {
				removed.add(element);
			}
		}

		return removed;
	}
}
