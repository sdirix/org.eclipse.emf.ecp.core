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
package org.eclipse.emf.ecp.view.internal.table.ui;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.view.model.VControl;
import org.eclipse.emf.ecp.view.model.VElement;
import org.eclipse.emf.ecp.view.table.model.VTableControl;
import org.eclipse.emf.ecp.view.validation.ECPValidationSubProcessor;
import org.eclipse.emf.ecp.view.validation.ValidationRegistry;

/**
 * @author Eugen Neufeld
 * 
 */
public class TableControlSubProcessor implements ECPValidationSubProcessor {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.validation.ECPValidationSubProcessor#processRenderable(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecp.view.model.VElement, org.eclipse.emf.ecp.view.validation.ValidationRegistry)
	 */
	public Map<EObject, Set<VControl>> processRenderable(EObject domainObject,
		final VElement parentRenderable,
		final ValidationRegistry validationRegistry) {
		final VTableControl tableControl = (VTableControl) parentRenderable;
		final Map<EObject, Set<VControl>> result = new LinkedHashMap<EObject, Set<VControl>>();
		// final EObject referencedDomainModel = validationRegistry.resolveDomainModel(domainObject,
		// tableControl.getPathToFeature());
		final Iterator<Setting> settings = tableControl.getDomainModelReference().getIterator();
		if (!settings.hasNext()) {
			return result;
		}
		final Setting setting = settings.next();
		final EObject referencedDomainModel = setting.getEObject();
		if (referencedDomainModel == null) {
			return result;
		}
		@SuppressWarnings("unchecked")
		final EList<EObject> tableContent = (EList<EObject>) referencedDomainModel
			.eGet(setting.getEStructuralFeature());

		if (!validationRegistry.getRenderablesForEObject(domainObject).contains(parentRenderable)) {
			referencedDomainModel.eAdapters().add(0, new AdapterImpl() {

				/**
				 * {@inheritDoc}
				 * 
				 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#notifyChanged(org.eclipse.emf.common.notify.Notification)
				 */
				@Override
				public void notifyChanged(Notification msg) {
					if (msg.isTouch()) {
						return;
					}
					if (msg.getFeature() != setting.getEStructuralFeature()) {
						return;
					}
					for (final EObject tableChild : tableContent) {
						validationRegistry.addEObjectControlMapping(tableChild, tableControl);
					}
				}

			});
		}

		addControlToMap(tableControl, result, referencedDomainModel);
		// EMF API
		for (final EObject eObject : tableContent) {
			addControlToMap(tableControl, result, eObject);
		}

		return result;
	}

	private void addControlToMap(VTableControl tableControl, Map<EObject, Set<VControl>> result,
		EObject referencedDomainModel) {
		if (!result.containsKey(referencedDomainModel)) {
			result.put(referencedDomainModel, new LinkedHashSet<VControl>());
		}
		result.get(referencedDomainModel).add(tableControl);
	}

}
