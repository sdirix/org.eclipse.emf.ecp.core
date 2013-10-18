/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.custom.internal.ui;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControl;
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControlFeature;
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControlInitException;
import org.eclipse.emf.ecp.view.custom.model.VCustomControl;
import org.eclipse.emf.ecp.view.model.AbstractControl;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.validation.ECPValidationSubProcessor;
import org.eclipse.emf.ecp.view.validation.ValidationRegistry;

/**
 * {@link ECPValidationSubProcessor} to notify a {@link VCustomControl} if any of its children has a validation error.
 * 
 * @author jfaltermeier
 * 
 */
public class CustomControlSubprocessor implements ECPValidationSubProcessor {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.validation.ECPValidationSubProcessor#processRenderable(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecp.view.model.Renderable, org.eclipse.emf.ecp.view.validation.ValidationRegistry)
	 */
	public Map<EObject, Set<AbstractControl>> processRenderable(EObject domainObject, Renderable parentRenderable,
		final ValidationRegistry validationRegistry) {

		final VCustomControl customControl = (VCustomControl) parentRenderable;
		final Map<EObject, Set<AbstractControl>> result = new LinkedHashMap<EObject, Set<AbstractControl>>();

		final Set<ECPCustomControlFeature> allFeatures = new LinkedHashSet<ECPCustomControlFeature>();
		ECPCustomControl categoryComposite;
		try {
			categoryComposite = customControl.getECPCustomControl();
		} catch (final ECPCustomControlInitException ex) {
			return result;
		}

		allFeatures.addAll(categoryComposite.getECPCustomControlFeatures());

		for (final ECPCustomControlFeature ccFeature : allFeatures) {

			final EObject referencedDomainModel = validationRegistry.resolveDomainModel(domainObject,
				ccFeature.geteReferencePath());
			if (referencedDomainModel == null) {
				continue;
			}

			final Set<EObject> customControlContents = new LinkedHashSet<EObject>();
			customControlContents.add(referencedDomainModel);

			addControlToMap(customControl, result, referencedDomainModel);
			// EMF API
			for (final EObject eObject : customControlContents) {
				addControlToMap(customControl, result, eObject);
			}
		}

		return result;
	}

	private void addControlToMap(VCustomControl customControl, Map<EObject, Set<AbstractControl>> result,
		EObject referencedDomainModel) {
		if (!result.containsKey(referencedDomainModel)) {
			result.put(referencedDomainModel, new LinkedHashSet<AbstractControl>());
		}
		result.get(referencedDomainModel).add(customControl);
	}

}
