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
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.model.ViewFactory;
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
	private final Map<EObject, Set<Control>> domainObjectToAffectedControls;
	private final Map<Class<Renderable>, ECPValidationSubProcessor> subProcessors;
	private final Set<Renderable> processedRenderables = new LinkedHashSet<Renderable>();
	private Map<Renderable, Set<EObject>> controlToDomainMapping;

	/**
	 * Default constructor.
	 */
	public ValidationRegistry() {
		controlToDomainMapping = new LinkedHashMap<Renderable, Set<EObject>>();
		domainObjectToAffectedControls = new LinkedHashMap<EObject, Set<Control>>();
		subProcessors = new LinkedHashMap<Class<Renderable>, ECPValidationSubProcessor>();
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

				final Class<Renderable> supportedClassType = loadClass(e.getContributor().getName(),
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
	public void register(EObject domainModel, Renderable renderable) {
		final Map<EObject, Set<Control>> domainToControlMapping = getDomainToControlMapping(domainModel,
			renderable);
		fillMap(domainToControlMapping, domainObjectToAffectedControls);
	}

	/**
	 * This method walks down the containment tree of the renderable and collects the EObject to Control mapping.
	 * All renderables are added to the {@link #processedRenderables} set, which contains all {@link Renderable
	 * Renderables} which were parsed.
	 * Furthermore this method adds a default {@link org.eclipse.emf.ecp.view.model.VDiagnostic VDiagnostic} to each
	 * {@link Renderable} if it was not set before.
	 * 3 cases are differentiated:
	 * - there is a subprocessor for the {@link Renderable}.
	 * - the {@link Renderable} is a {@link AbstractControl}.
	 * - the renderable is searched for containment {@link Renderable Renderables}, which are then passed recursively
	 * 
	 * @param domainModel the {@link EObject} representing the root Domain Element
	 * @param renderable the {@link Renderable} to check
	 * @return a Map of EObject to {@link AbstractControl AbstractControls}
	 */
	public Map<EObject, Set<Control>> getDomainToControlMapping(EObject domainModel, Renderable renderable) {
		processedRenderables.add(renderable);
		if (renderable.getDiagnostic() == null) {
			renderable.setDiagnostic(ViewFactory.eINSTANCE.createVDiagnostic());
		}
		final Map<EObject, Set<Control>> result = new LinkedHashMap<EObject, Set<Control>>();
		final Class<?> renderableClass = renderable.getClass().getInterfaces()[0];
		if (subProcessors.containsKey(renderableClass)) {
			fillMap(subProcessors.get(renderableClass).processRenderable(
				domainModel, renderable, this), result);
		}
		else if (Control.class.isInstance(renderable)) {
			final Control control = (Control) renderable;
			final VDomainModelReference domainModelReference = control.getDomainModelReference();
			final Iterator<Setting> iterator = domainModelReference.getIterator();
			while (iterator.hasNext()) {
				final Setting setting = iterator.next();
				final EObject referencedDomainModel = setting.getEObject();
				if (!result.containsKey(referencedDomainModel)) {
					result.put(referencedDomainModel, new LinkedHashSet<Control>());
				}
				result.get(referencedDomainModel).add(control);
			}

		}
		else {
			for (final EObject eObject : renderable.eContents()) {
				if (Renderable.class.isInstance(eObject)) {
					fillMap(getDomainToControlMapping(domainModel, (Renderable) eObject), result);
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

	private void fillMap(Map<EObject, Set<Control>> source, Map<EObject, Set<Control>> target) {
		for (final EObject domainSource : source.keySet()) {
			final Set<Control> controlSet = source.get(domainSource);
			fillMapEntry(target, domainSource, controlSet);
		}
	}

	private void fillMapEntry(Map<EObject, Set<Control>> target, final EObject domainSource,
		Set<Control> controlSet) {
		for (final Control abstractControl : controlSet) {
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
	public void addEObjectControlMapping(EObject domainObject, Control control) {
		final LinkedHashSet<Control> controlSet = new LinkedHashSet<Control>();
		controlSet.add(control);
		fillMapEntry(domainObjectToAffectedControls, domainObject, controlSet);
	}

	/**
	 * Removes the given {@link Renderable} from the mappings of the registry.
	 * 
	 * @param renderable
	 *            the {@link Renderable} to be removed from the registry
	 */
	public void removeRenderable(Renderable renderable) {
		final Set<EObject> eObjects = controlToDomainMapping.get(renderable);
		if (eObjects != null) {
			for (final EObject eObject : eObjects) {
				final Set<Control> set = domainObjectToAffectedControls.get(eObject);
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
		final Set<Control> set = domainObjectToAffectedControls.get(domainObject);
		if (set != null) {
			for (final Control abstractControl : set) {
				controlToDomainMapping.remove(abstractControl);
			}
		}
		domainObjectToAffectedControls.remove(domainObject);
	}

	/**
	 * Returns all associated {@link Renderable}s for the given model. The list is ordered so that if two renderables
	 * are part of the same hierachy the child will have a lower index than the parent.
	 * 
	 * @param model the model
	 * @return list of all renderables
	 */
	public Set<Control> getRenderablesForEObject(EObject model) {
		if (!domainObjectToAffectedControls.containsKey(model)) {
			return Collections.emptySet();
		}
		return domainObjectToAffectedControls.get(model);
	}

	/**
	 * Checks whether a {@link Renderable} was already checked for controls a thus added to the
	 * {@link #domainObjectToAffectedControls}.
	 * 
	 * @param renderable the {@link Renderable} to check
	 * @return true if {@link Renderable} was already checked
	 */
	public boolean containsRenderable(Renderable renderable) {
		return processedRenderables.contains(renderable);
	}

}
