/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.rule;

import java.util.LinkedHashSet;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;

/**
 * Settings registry containing a set of settings.
 * 
 * @author emueller
 * 
 */
@SuppressWarnings("serial")
public class SettingsRegistry extends LinkedHashSet<Setting> {

	/**
	 * Returns the setting that consists of the given {@link EObject} and the attribute.
	 * 
	 * @param eObject
	 *            the {@link EObject} that has the given attribute
	 * @param attribute
	 *            an attribute of the {@link EObject}
	 * @return a setting consisting of the {@link EObject} and the {@link EAttribute}
	 */
	public Setting getByEObjectAndEAttribute(EObject eObject, EAttribute attribute) {

		for (final Setting setting : this) {

			final EAttribute attr = (EAttribute) setting.getEStructuralFeature();
			final EObject obj = setting.getEObject();

			if (attr == attribute && obj == eObject) {
				return setting;
			}
		}

		return null;
	}
}
