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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.view.model.AbstractControl;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewFactory;

/**
 * This registry maps eObjects in a domain model to their corresponding renderables.
 * 
 * @author jfaltermeier
 * 
 */
public class ValidationRegistry {

	private final Map<EObject, LinkedHashSet<Renderable>> eObjectToRenderablesMap;
	private final Map<AbstractControl, LinkedHashSet<EObject>> controlToElementsMap;

	/**
	 * Default constructor.
	 */
	public ValidationRegistry() {
		eObjectToRenderablesMap = new LinkedHashMap<EObject, LinkedHashSet<Renderable>>();
		controlToElementsMap = new LinkedHashMap<AbstractControl, LinkedHashSet<EObject>>();
	}

	/**
	 * Maps eObjects from the model to their corresponding renderables.
	 * 
	 * @param model the domain model
	 * @param renderable the view model
	 */
	public void register(EObject model, Renderable renderable) {
		registerWithKeyResult(model, renderable);
	}

	/**
	 * Maps eObjects from the model to their corresponding renderables.
	 * 
	 * @param model the domain model
	 * @param renderable the view model
	 * @return the list of keys with which the given renderable was registered
	 */
	private List<EObject> registerWithKeyResult(EObject model, Renderable renderable) {
		final List<Renderable> childRenderables = getChildRenderables(renderable);
		final List<EObject> usedKeys = new ArrayList<EObject>();

		// register all children
		for (final Renderable r : childRenderables) {
			if (r instanceof Control) {
				final Control control = (Control) r;
				final List<EReference> references = new ArrayList<EReference>(control.getPathToFeature());

				if (references.isEmpty()) {
					usedKeys.addAll(registerWithKeyResult(model, r));
				} else {
					final List<EObject> models = new ArrayList<EObject>();
					models.add(model);
					final List<EObject> referencedModels = collectReferencedModelsForPathToFeature(references, models);
					for (final EObject refModel : referencedModels) {
						usedKeys.addAll(registerWithKeyResult(refModel, control));
					}
				}
			} else {
				usedKeys.addAll(registerWithKeyResult(model, r));
			}
		}

		// children are registered. register self based on result
		final ArrayList<EObject> keys = new ArrayList<EObject>();

		if (renderable.getDiagnostic() == null) {
			renderable.setDiagnostic(ViewFactory.eINSTANCE.createVDiagnostic());
		}

		if (renderable instanceof View) {
			// the view model will always be registered with the domain model
			putIntoMaps(model, renderable);
			keys.add(model);
		} else if (childRenderables.size() == 1) {
			// if the renderable has only one child we register the renderable with the same keys as the child
			for (final EObject o : usedKeys) {
				putIntoMaps(o, renderable);
				keys.add(o);
			}
		} else {
			// else register current eObject with current renderable
			putIntoMaps(model, renderable);
			keys.add(model);
		}

		return keys;
	}

	private List<EObject> collectReferencedModelsForPathToFeature(List<EReference> references, List<EObject> models) {

		if (references == null || models == null) {
			// TODO exception?
			return Collections.emptyList();
		}

		if (references.isEmpty()) {
			return models;
		}

		final EReference reference = references.remove(0);

		final List<EObject> result = new ArrayList<EObject>();
		for (final EObject model : models) {
			if (reference.isMany()) {
				@SuppressWarnings("unchecked")
				final List<EObject> childObjects = (List<EObject>) model.eGet(reference);
				result.addAll(collectReferencedModelsForPathToFeature(references, childObjects));
			} else {
				final EObject o = (EObject) model.eGet(reference);
				final List<EObject> childObjects = new ArrayList<EObject>();
				childObjects.add(o);
				result.addAll(collectReferencedModelsForPathToFeature(references, childObjects));
			}
		}
		return result;
	}

	private void putIntoMaps(EObject model, Renderable renderable) {
		if (!eObjectToRenderablesMap.containsKey(model)) {
			eObjectToRenderablesMap.put(model, new LinkedHashSet<Renderable>());
		}
		eObjectToRenderablesMap.get(model).add(renderable);

		if (renderable instanceof AbstractControl) {
			final Control control = (Control) renderable;
			if (!controlToElementsMap.containsKey(control)) {
				controlToElementsMap.put(control, new LinkedHashSet<EObject>());
			}
			controlToElementsMap.get(control).add(model);
		}
	}

	private List<Renderable> getChildRenderables(Renderable renderable) {
		final List<Renderable> result = new ArrayList<Renderable>();
		for (final EObject o : renderable.eContents()) {
			if (o instanceof Renderable) {
				result.add((Renderable) o);
			}
		}
		return result;
	}

	/**
	 * Returns all associated {@link Renderable}s for the given model. The list is ordered so that if two renderables
	 * are part of the same hierachy the child will have a lower index than the parent.
	 * 
	 * @param model the model
	 * @return list of all renderables
	 */
	public List<Renderable> getRenderablesForEObject(EObject model) {
		final Set<Renderable> result = eObjectToRenderablesMap.get(model);
		if (result != null) {
			return new ArrayList<Renderable>(result);
		}
		return new ArrayList<Renderable>();
	}

	/**
	 * Returns the elements that are associated with a control.
	 * 
	 * @param control the control
	 * @return the associated elements
	 */
	public List<EObject> getEObjectsForControl(AbstractControl control) {
		final LinkedHashSet<EObject> result = controlToElementsMap.get(control);
		if (result != null) {
			return new ArrayList<EObject>(result);
		}
		return new ArrayList<EObject>();
	}
}
