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
package org.eclipse.emf.ecp.view.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.AbstractControl;
import org.eclipse.emf.ecp.view.model.Categorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.Composite;
import org.eclipse.emf.ecp.view.model.CompositeCollection;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;
import org.osgi.framework.Bundle;

/**
 * This registry maps eObjects in a domain model to their corresponding renderables.
 * 
 * @author jfaltermeier
 * 
 * 
 */
public class ValidationRegistry {

	/**
	 * The list is ordered so that if two renderables
	 * are part of the same hierarchy the child will have a lower index than the parent.
	 */
	private final Map<EObject, Set<AbstractControl>> domainObjectToAffectedControls;
	private final Set<Renderable> addedRenderables = new LinkedHashSet<Renderable>();
	private final Map<AbstractControl, Set<EObject>> controlToUsedDomainObjects;
	private final Map<Class<Renderable>, ECPValidationSubProcessor> subProcessors;

	/**
	 * Default constructor.
	 */
	public ValidationRegistry() {
		domainObjectToAffectedControls = new LinkedHashMap<EObject, Set<AbstractControl>>();
		controlToUsedDomainObjects = new LinkedHashMap<AbstractControl, Set<EObject>>();
		subProcessors = new HashMap<Class<Renderable>, ECPValidationSubProcessor>();

		final IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
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
			// TODO log
			ex.printStackTrace();
		} catch (final InvalidRegistryObjectException ex) {
			ex.printStackTrace();
		} catch (final ClassNotFoundException ex) {
			ex.printStackTrace();
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
	 * @param model the domain model
	 * @param renderable the view model
	 */
	public void register(EObject model, Renderable renderable) {
		final Map<EObject, Set<AbstractControl>> domainToControlMapping = getDomainToControlMapping(model, renderable);
		for (final EObject domainObject : domainToControlMapping.keySet()) {
			if (domainObjectToAffectedControls.containsKey(domainObject)) {
				domainObjectToAffectedControls.get(domainObject).addAll(domainToControlMapping.get(domainObject));
			} else {
				domainToControlMapping.put(domainObject, domainToControlMapping.get(domainObject));
			}
		}
		// registerWithKeyResult(model, renderable);
	}

	/**
	 * @param model
	 * @param renderable
	 * @return
	 */
	public Map<EObject, Set<AbstractControl>> getDomainToControlMapping(EObject domainModel, Renderable renderable) {
		final Map<EObject, Set<AbstractControl>> result = new HashMap<EObject, Set<AbstractControl>>();
		final Class<?> renderableClass = renderable.getClass().getInterfaces()[0];
		if (subProcessors.containsKey(renderableClass)) {
			result.putAll(subProcessors.get(renderableClass).processRenderable(
				domainModel, renderable, this));
		}
		else if (View.class.isInstance(renderable)) {
			final View view = (View) renderable;
			for (final Composite composite : view.getChildren()) {
				result.putAll(getDomainToControlMapping(domainModel, composite));
			}
			for (final AbstractCategorization categorization : view.getCategorizations()) {
				result.putAll(getDomainToControlMapping(domainModel, categorization));
			}
		}
		else if (CompositeCollection.class.isInstance(renderable)) {
			final CompositeCollection collection = (CompositeCollection) renderable;
			for (final Composite composite : collection.getComposites()) {
				result.putAll(getDomainToControlMapping(domainModel, composite));
			}
		}
		// TODO subprocessor for CustomControl
		else if (Control.class.isInstance(renderable)) {
			final Control control = (Control) renderable;
			final List<EReference> references = new ArrayList<EReference>(control.getPathToFeature());
			EObject referencedDomainModel = domainModel;
			for (final EReference eReference : references) {
				referencedDomainModel = (EObject) referencedDomainModel.eGet(eReference);
			}
			if (!result.containsKey(referencedDomainModel)) {
				result.put(referencedDomainModel, new LinkedHashSet<AbstractControl>());
			}
			result.get(referencedDomainModel).add((AbstractControl) renderable);
		}
		// TODO move in processor
		else if (Categorization.class.isInstance(renderable)) {
			final Categorization view = (Categorization) renderable;
			for (final AbstractCategorization categorization : view.getCategorizations()) {
				result.putAll(getDomainToControlMapping(domainModel, categorization));
			}
		}
		else if (Category.class.isInstance(renderable)) {
			final Category view = (Category) renderable;
			result.putAll(getDomainToControlMapping(domainModel, view.getComposite()));
		}
		return result;
	}

	/**
	 * Maps eObjects from the model to their corresponding renderables.
	 * 
	 * @param model the domain model
	 * @param renderable the view model
	 * @return the list of keys with which the given renderable was registered
	 */
	// private void registerWithKeyResult(EObject model, Renderable renderable) {
	//
	// putIntoMaps(model, renderable);
	//
	// final Class<?> renderableClass = renderable.getClass().getInterfaces()[0];
	// if (subProcessors.containsKey(renderableClass)) {
	// subProcessors.get(renderableClass).processRenderable(
	// model, renderable, this);
	// }
	// else {
	// final List<Renderable> childRenderables = getChildRenderables(renderable);
	// // register all children
	// for (final Renderable r : childRenderables) {
	// if (r instanceof Control) {
	// final Control control = (Control) r;
	// final List<EReference> references = new ArrayList<EReference>(control.getPathToFeature());
	//
	// if (references.isEmpty()) {
	// registerWithKeyResult(model, r);
	// } else {
	// final List<EObject> models = new ArrayList<EObject>();
	// models.add(model);
	// final List<EObject> referencedModels = collectReferencedModelsForPathToFeature(references,
	// models);
	// for (final EObject refModel : referencedModels) {
	// registerWithKeyResult(refModel, control);
	// }
	// }
	// } else {
	// // test renderebale
	// registerWithKeyResult(model, r);
	//
	// }
	// }
	// }
	// // children are registered. register self based on result
	// // final ArrayList<EObject> keys = new ArrayList<EObject>();
	//
	// // TODO Why?
	// // if (renderable instanceof View) {
	// // // the view model will always be registered with the domain model
	// // putIntoMaps(model, renderable);
	// // keys.add(model);
	// // } else if (childRenderables.size() == 1) {
	// // // if the renderable has only one child we register the renderable with the same keys as the child
	// // for (final EObject o : usedKeys) {
	// // putIntoMaps(o, renderable);
	// // keys.add(o);
	// // }
	// // } else {
	// // else register current eObject with current renderable
	// // if (init)
	// // {
	// // putIntoMaps(model, renderable, init);
	// // // keys.add(model);
	// // // }
	// // }
	//
	// // return keys;
	// }

	// private List<EObject> collectReferencedModelsForPathToFeature(List<EReference> references, List<EObject> models)
	// {
	//
	// if (references == null || models == null) {
	// // TODO exception?
	// return Collections.emptyList();
	// }
	//
	// if (references.isEmpty()) {
	// return models;
	// }
	//
	// final EReference reference = references.remove(0);
	//
	// final List<EObject> result = new ArrayList<EObject>();
	// for (final EObject model : models) {
	// if (reference.isMany()) {
	// @SuppressWarnings("unchecked")
	// final List<EObject> childObjects = (List<EObject>) model.eGet(reference);
	// result.addAll(collectReferencedModelsForPathToFeature(references, childObjects));
	// } else {
	// try {
	// EObject o = (EObject) model.eGet(reference);
	// if (o == null) {
	// o = EcoreUtil.create(reference.getEReferenceType());
	// model.eSet(reference, o);
	// }
	// final List<EObject> childObjects = new ArrayList<EObject>();
	// childObjects.add(o);
	// result.addAll(collectReferencedModelsForPathToFeature(references, childObjects));
	// } catch (final ClassCastException e) {
	// System.out.println(reference);
	// System.out.println(model);
	// }
	// }
	// }
	// return result;
	// }

	// private void putIntoMaps(EObject model, AbstractControl renderable) {
	// if (renderable.getDiagnostic() == null) {
	// renderable.setDiagnostic(ViewFactory.eINSTANCE.createVDiagnostic());
	// }
	// addedRenderables.add(renderable);
	//
	// if (!domainObjectToAffectedControls.containsKey(model)) {
	// domainObjectToAffectedControls.put(model, new LinkedList<AbstractControl>());
	// }
	// if (!domainObjectToAffectedControls.get(model).contains(renderable)) {
	// domainObjectToAffectedControls.get(model).add(0, renderable);
	// }
	//
	// if (renderable instanceof AbstractControl) {
	// final Control control = (Control) renderable;
	// if (!controlToUsedDomainObjects.containsKey(control)) {
	// controlToUsedDomainObjects.put(control, new LinkedHashSet<EObject>());
	// }
	// if (!controlToUsedDomainObjects.get(control).contains(model)) {
	// controlToUsedDomainObjects.get(control).add(model);
	// }
	// }
	// }

	// private List<Renderable> getChildRenderables(Renderable renderable) {
	// final List<Renderable> result = new ArrayList<Renderable>();
	// for (final EObject o : renderable.eContents()) {
	// if (o instanceof Renderable) {
	// result.add((Renderable) o);
	// }
	// }
	// return result;
	// }

	/**
	 * Returns all associated {@link Renderable}s for the given model. The list is ordered so that if two renderables
	 * are part of the same hierachy the child will have a lower index than the parent.
	 * 
	 * @param model the model
	 * @return list of all renderables
	 */
	public Set<AbstractControl> getRenderablesForEObject(EObject model) {
		if (!domainObjectToAffectedControls.containsKey(model)) {
			return Collections.emptySet();
		}
		return domainObjectToAffectedControls.get(model);
	}

	/**
	 * Returns the elements that are associated with a control.
	 * 
	 * @param control the control
	 * @return the associated elements
	 */
	public List<EObject> getEObjectsForControl(AbstractControl control) {
		final Set<EObject> result = controlToUsedDomainObjects.get(control);
		if (result != null) {
			return new ArrayList<EObject>(result);
		}
		return new ArrayList<EObject>();
	}

	public boolean contains(Renderable renderable) {
		return addedRenderables.contains(renderable);
	}

}
