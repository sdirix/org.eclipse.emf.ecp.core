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
 * Edgar Mueller - added remove methods to avoid leaks
 ******************************************************************************/
package org.eclipse.emf.ecp.view.validation;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.view.model.VControl;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.model.VElement;
import org.eclipse.emf.ecp.view.model.VViewFactory;
import org.osgi.framework.Bundle;

/**
 * This registry maps eObjects in a domain model to their corresponding renderables.
 * 
 * @author jfaltermeier
 * @author Eugen Neufeld
 * @author emueller
 */
public class ValidationRegistry {

	/**
	 * The list is ordered so that if two renderables
	 * are part of the same hierarchy the child will have a lower index than the parent.
	 */
	private final Map<EObject, Set<VControl>> domainObjectToAffectedControls;
	private final Map<Class<VElement>, ECPValidationSubProcessor> subProcessors;
	private final Set<VElement> processedRenderables = new LinkedHashSet<VElement>();
	private Map<VElement, Set<EObject>> controlToDomainMapping;

	/**
	 * Default constructor.
	 */
	public ValidationRegistry() {
		controlToDomainMapping = new LinkedHashMap<VElement, Set<EObject>>();
		domainObjectToAffectedControls = new LinkedHashMap<EObject, Set<VControl>>();
		subProcessors = new LinkedHashMap<Class<VElement>, ECPValidationSubProcessor>();
		final IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		if (extensionRegistry == null) {
			return;
		}
		final IConfigurationElement[] config = extensionRegistry.getConfigurationElementsFor(
			"org.eclipse.emf.ecp.view.validation.validationSubProcessor");
		try {
			for (final IConfigurationElement e : config) {
				final ECPValidationSubProcessor o = (ECPValidationSubProcessor) e.createExecutableExtension("class");
				// TODO move into subprocessor or use helper
				final String associatedRenderable = e.getAttribute("associatedRenderable");

				final Class<VElement> supportedClassType = loadClass(e.getContributor().getName(),
					associatedRenderable);

				subProcessors.put(supportedClassType, o);
			}
		} catch (final CoreException ex) {
			Activator.logException(ex);
		} catch (final InvalidRegistryObjectException ex) {
			Activator.logException(ex);
		} catch (final ClassNotFoundException ex) {
			Activator.logException(ex);
		}

	}

	@SuppressWarnings("unchecked")
	private static <T> Class<T> loadClass(String bundleName, String clazz) throws ClassNotFoundException {
		final Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null) {
			throw new ClassNotFoundException(clazz + "cannot be loaded"
				+ bundleName
				+ "cannot be resolved");
		}
		return (Class<T>) bundle.loadClass(clazz);

	}

	/**
	 * Maps eObjects from the model to their corresponding renderables.
	 * 
	 * @param domainModel the domain model
	 * @param renderable the view model
	 */
	public void register(EObject domainModel, VElement renderable) {
		final Map<EObject, Set<VControl>> domainToControlMapping = getDomainToControlMapping(domainModel,
			renderable);
		fillMap(domainToControlMapping, domainObjectToAffectedControls);
	}

	/**
	 * This method walks down the containment tree of the renderable and collects the EObject to Control mapping.
	 * All renderables are added to the {@link #processedRenderables} set, which contains all {@link VElement
	 * Renderables} which were parsed.
	 * Furthermore this method adds a default {@link org.eclipse.emf.ecp.view.model.VDiagnostic VDiagnostic} to each
	 * {@link VElement} if it was not set before.
	 * 3 cases are differentiated:
	 * - there is a subprocessor for the {@link VElement}.
	 * - the {@link VElement} is a {@link AbstractControl}.
	 * - the renderable is searched for containment {@link VElement Renderables}, which are then passed recursively
	 * 
	 * @param domainModel the {@link EObject} representing the root Domain Element
	 * @param renderable the {@link VElement} to check
	 * @return a Map of EObject to {@link AbstractControl AbstractControls}
	 */
	public Map<EObject, Set<VControl>> getDomainToControlMapping(EObject domainModel, VElement renderable) {
		processedRenderables.add(renderable);
		if (renderable.getDiagnostic() == null) {
			renderable.setDiagnostic(VViewFactory.eINSTANCE.createDiagnostic());
		}
		final Map<EObject, Set<VControl>> result = new LinkedHashMap<EObject, Set<VControl>>();
		final Class<?> renderableClass = renderable.getClass().getInterfaces()[0];
		if (subProcessors.containsKey(renderableClass)) {
			fillMap(subProcessors.get(renderableClass).processRenderable(
				domainModel, renderable, this), result);
		}
		else if (VControl.class.isInstance(renderable)) {
			final VControl control = (VControl) renderable;
			final VDomainModelReference domainModelReference = control.getDomainModelReference();
			final Iterator<Setting> iterator = domainModelReference.getIterator();
			while (iterator.hasNext()) {
				final Setting setting = iterator.next();
				final EObject referencedDomainModel = setting.getEObject();
				if (!result.containsKey(referencedDomainModel)) {
					result.put(referencedDomainModel, new LinkedHashSet<VControl>());
				}
				result.get(referencedDomainModel).add(control);
			}

		}
		else {
			for (final EObject eObject : renderable.eContents()) {
				if (VElement.class.isInstance(eObject)) {
					fillMap(getDomainToControlMapping(domainModel, (VElement) eObject), result);
				}
			}
		}

		return result;
	}

	/**
	 * Resolved an {@link EObject} based on the list of {@link EReference EReferences}.
	 * 
	 * @param domainModel the root domain Model
	 * @param references the list of {@link EReference} to use for navigation
	 * @return the resolved {@link EObject}
	 */
	public EObject resolveDomainModel(EObject domainModel, List<EReference> references) {
		EObject referencedDomainModel = domainModel;
		for (final EReference eReference : references) {
			if (!eReference.getEContainingClass().isInstance(referencedDomainModel)) {
				continue;
			}
			EObject child = (EObject) referencedDomainModel.eGet(eReference);
			if (child == null) {
				child = EcoreUtil.create(eReference.getEReferenceType());
				referencedDomainModel.eSet(eReference, child);
			}
			referencedDomainModel = child;
		}
		return referencedDomainModel;
	}

	private void fillMap(Map<EObject, Set<VControl>> source, Map<EObject, Set<VControl>> target) {
		for (final EObject domainSource : source.keySet()) {
			final Set<VControl> controlSet = source.get(domainSource);
			fillMapEntry(target, domainSource, controlSet);
		}
	}

	private void fillMapEntry(Map<EObject, Set<VControl>> target, final EObject domainSource,
		Set<VControl> controlSet) {
		for (final VControl abstractControl : controlSet) {
			final Set<EObject> set = controlToDomainMapping.get(abstractControl);
			if (set == null) {
				controlToDomainMapping.put(abstractControl, new LinkedHashSet<EObject>());
			}
			controlToDomainMapping.get(abstractControl).add(domainSource);
		}
		if (!target.containsKey(domainSource)) {
			target.put(domainSource, controlSet);
		} else {
			target.get(domainSource).addAll(controlSet);
		}
	}

	/**
	 * Adds a single control for a domainObject to the registry.
	 * 
	 * @param domainObject the domain object
	 * @param control the control to be registered for the domain object
	 */
	public void addEObjectControlMapping(EObject domainObject, VControl control) {
		final LinkedHashSet<VControl> controlSet = new LinkedHashSet<VControl>();
		controlSet.add(control);
		fillMapEntry(domainObjectToAffectedControls, domainObject, controlSet);
	}

	/**
	 * Removes the given {@link VElement} from the mappings of the registry.
	 * 
	 * @param renderable
	 *            the {@link VElement} to be removed from the registry
	 */
	public void removeRenderable(VElement renderable) {
		final Set<EObject> eObjects = controlToDomainMapping.get(renderable);
		if (eObjects != null) {
			for (final EObject eObject : eObjects) {
				final Set<VControl> set = domainObjectToAffectedControls.get(eObject);
				if (set != null && set.contains(renderable)) {
					domainObjectToAffectedControls.get(eObject).remove(renderable);
				}
			}
		}
		controlToDomainMapping.remove(renderable);
		processedRenderables.remove(renderable);
	}

	/**
	 * Removes the given domain object from the mappings of the registry.
	 * 
	 * @param domainObject
	 *            the domain object to be removed from the registry
	 */
	public void removeDomainObject(EObject domainObject) {
		final Set<VControl> set = domainObjectToAffectedControls.get(domainObject);
		if (set != null) {
			for (final VControl abstractControl : set) {
				controlToDomainMapping.remove(abstractControl);
			}
		}
		domainObjectToAffectedControls.remove(domainObject);
	}

	/**
	 * Returns all associated {@link VElement}s for the given model. The list is ordered so that if two renderables
	 * are part of the same hierachy the child will have a lower index than the parent.
	 * 
	 * @param model the model
	 * @return list of all renderables
	 */
	public Set<VControl> getRenderablesForEObject(EObject model) {
		if (!domainObjectToAffectedControls.containsKey(model)) {
			return Collections.emptySet();
		}
		return domainObjectToAffectedControls.get(model);
	}

	/**
	 * Checks whether a {@link VElement} was already checked for controls a thus added to the
	 * {@link #domainObjectToAffectedControls}.
	 * 
	 * @param renderable the {@link VElement} to check
	 * @return true if {@link VElement} was already checked
	 */
	public boolean containsRenderable(VElement renderable) {
		return processedRenderables.contains(renderable);
	}

}
