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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
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

	private final Map<EObject, HashSet<Renderable>> eObjectToRenderablesMap;

	/**
	 * Default constructor.
	 */
	public ValidationRegistry() {
		eObjectToRenderablesMap = new LinkedHashMap<EObject, HashSet<Renderable>>();
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
				final List<EReference> references = control.getPathToFeature();
				if (references.isEmpty()) {
					usedKeys.addAll(registerWithKeyResult(model, r));
				} else {
					for (final EReference reference : references) {
						if (reference.isMany()) {
							final List<Object> childObjects = (List<Object>) model.eGet(reference);
							for (final Object o : childObjects) {
								if (o instanceof EObject) {
									usedKeys.addAll(registerWithKeyResult((EObject) o, control));
								}
							}
						} else {
							final EObject o = (EObject) model.eGet(reference);
							usedKeys.addAll(registerWithKeyResult(o, control));
						}
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
			getOrCreateRenderables(model).add(renderable);
			keys.add(model);
		} else if (childRenderables.size() == 1) {
			// if the renderable has only one child we register the renderable with the same keys as the child
			for (final EObject o : usedKeys) {
				getOrCreateRenderables(o).add(renderable);
				keys.add(o);
			}
		} else {
			// else register current eObject with current renderable
			getOrCreateRenderables(model).add(renderable);
			keys.add(model);
		}

		return keys;
	}

	/**
	 * Returns the entry for the given paramter from the registry. If there is no entry one will be created.
	 * 
	 * @param object the key
	 * @return the value
	 */
	private Set<Renderable> getOrCreateRenderables(EObject object) {
		if (!eObjectToRenderablesMap.containsKey(object)) {
			eObjectToRenderablesMap.put(object, new HashSet<Renderable>());
		}
		return eObjectToRenderablesMap.get(object);
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
	 * Returns all associated {@link Renderable}s for the given model.
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

}
