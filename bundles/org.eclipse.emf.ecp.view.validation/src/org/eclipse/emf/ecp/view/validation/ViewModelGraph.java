/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.ecp.view.validation;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.common.UniqueSetting;
import org.eclipse.emf.ecp.view.model.Renderable;

/**
 * The validation graph is a graph like structure where each {@link Renderable} is a represented as
 * {@link CachedGraphNode} with a value of type {@code T}. The graph structure follows the {@link EObject} hierarchy of
 * the view model (and in that sense conforms to the containment tree of the Renderable).
 * The actually computed values of the nodes are based on domain objects, which are also represented as
 * {@link CachedGraphNode}s, together with a feature, but in contrast to the nodes containing the {@link Renderable}s,
 * never
 * have any children.
 * Nodes containing domain objects also can have multiple parent.
 * For instance this is the case if a domain object's feature is visualized by multiple controls.
 * 
 * @author emueller
 * 
 * @param <T> the type stored by this tree
 */
public abstract class ViewModelGraph<T> {

	private final SettingsNodeMapping<T> viewModelSettings;
	private final SettingsNodeMapping<T> domainModelSettings;
	private final EObject domainModel;

	/**
	 * Constructor.
	 * 
	 * @param viewModel
	 *            the root of the view model
	 * @param domainModel
	 *            the root of the domain model
	 * @param comparator
	 *            the comparator that is used to compute the most distinctive value
	 */
	public ViewModelGraph(Renderable viewModel, EObject domainModel, Comparator<T> comparator) {
		this.domainModel = domainModel;
		viewModelSettings = new SettingsNodeMapping<T>(viewModel, comparator);
		domainModelSettings = new SettingsNodeMapping<T>(domainModel, comparator);
		buildRenderableNodes(viewModel);
	}

	private void buildRenderableNodes(EObject viewModel) {
		final EList<EObject> eContents = viewModel.eContents();
		final CachedGraphNode<T> parentNode = viewModelSettings.createNode(viewModel, SettingsNodeMapping.allFeatures(),
			getDefaultValue());
		for (final EObject content : eContents) {
			if (!Renderable.class.isInstance(content)) {
				continue;
			}
			final CachedGraphNode<T> childNode = viewModelSettings.createNode(content, SettingsNodeMapping.allFeatures(),
				getDefaultValue());
			parentNode.addChild(childNode);
			buildRenderableNodes(content);
		}
	}

	/**
	 * Returns the default value for a cached node.
	 * 
	 * @return the default value for a cached tree node
	 */
	public abstract T getDefaultValue();

	/**
	 * Returns the computed value for the given {@link Renderable}.
	 * 
	 * @param renderable
	 *            the {@link Renderable} to fetch the value for
	 * @return the value for the given {@link Renderable}
	 */
	public T getValue(Renderable renderable) {
		return viewModelSettings.getNode(renderable, SettingsNodeMapping.allFeatures()).getValue();
	}

	/**
	 * Updates the cached entry for the given {@link EObject} with the given value.<br/>
	 * If the cached entry does not yet exist, it will be created.
	 * 
	 * @param renderable
	 *            the {@link Renderable} that is referencing the {@code feature} of the {@code domainObject}
	 * @param domainObject
	 *            the domain object that caused the {@code value} to be computed
	 * @param feature
	 *            the {@link EStructuralFeature} referenced by the Renderable
	 * @param value
	 *            the actual value
	 */
	public void update(Renderable renderable, EObject domainObject, EStructuralFeature feature, T value) {

		final Set<CachedGraphNode<T>> parentNodes = updateNodes(renderable, domainObject, feature, value);

		for (final CachedGraphNode<T> cachedNode : parentNodes) {
			updateParentNodes(cachedNode);
		}
	}

	private Set<EObject> updateParentNodes(CachedGraphNode<T> node) {

		final Set<EObject> updated = new LinkedHashSet<EObject>();

		final Set<CachedGraphNode<T>> nodes = node.getParents();
		updateRenderable((Renderable) node.getSetting().getEObject());

		for (final CachedGraphNode<T> n : nodes) {
			final UniqueSetting setting = n.getSetting();
			updateRenderable((Renderable) setting.getEObject());
			updated.add(setting.getEObject());
		}

		for (final CachedGraphNode<T> cachedNode : node.getParents()) {
			updated.addAll(updateParentNodes(cachedNode));
		}

		return updated;
	}

	/**
	 * Removes the given domain object from the graph.
	 * 
	 * @param domainObject
	 *            the domain object to be removed
	 */
	public void removeDomainObject(EObject domainObject) {
		final EList<EStructuralFeature> eAllStructuralFeatures = domainObject.eClass().getEAllStructuralFeatures();
		for (final EStructuralFeature eStructuralFeature : eAllStructuralFeatures) {

			final CachedGraphNode<T> node = domainModelSettings.getNode(domainObject, eStructuralFeature);
			if (node == null) {
				continue;
			}
			// domain nodes are always leaf, no need to take care of their children
			final Set<CachedGraphNode<T>> parents = node.getParents();
			for (final CachedGraphNode<T> parentNode : new LinkedHashSet<CachedGraphNode<T>>(parents)) {
				parentNode.removeChild(node);
			}
		}
		domainModelSettings.removeAll(domainObject);
	}

	/**
	 * Removes the given {@link Renderable} from the graph.
	 * 
	 * @param renderable
	 *            the {@link Renderable} be removed
	 */
	public void removeRenderable(Renderable renderable) {
		final CachedGraphNode<T> node = viewModelSettings.getNode(renderable, SettingsNodeMapping.allFeatures());

		if (node != null) {
			// domain nodes are always leaf, no need to take care of their children
			final Set<CachedGraphNode<T>> parents = node.getParents();
			for (final CachedGraphNode<T> parentNode : new LinkedHashSet<CachedGraphNode<T>>(parents)) {
				parentNode.removeChild(node);
				updateParentNodes(parentNode);
			}
		}

		viewModelSettings.removeAll(renderable);
	}

	/**
	 * Allows clients to hook into the update call.
	 * 
	 * @param renderable
	 *            the {@link Renderable} being updated
	 */
	protected void updateRenderable(Renderable renderable) {
	}

	private Set<CachedGraphNode<T>> updateNodes(Renderable renderable, EObject domainObject,
		EStructuralFeature feature, T value) {

		final Set<CachedGraphNode<T>> affectedParentNodes = new LinkedHashSet<CachedGraphNode<T>>();

		final CachedGraphNode<T> renderableNode = getNodeForRenderable(renderable);
		final CachedGraphNode<T> domainNode = getNodeForDomainObject(domainObject, feature, value);

		if (!renderableNode.containsChild(domainNode)) {
			renderableNode.addChild(domainNode);
		}

		affectedParentNodes.add(renderableNode);

		if (domainObjectHasBeenRemoved(domainObject, feature)) {
			removeChildFromParents(domainNode);
		} else {
			// we only change a node's value if it contains a domain object
			domainNode.setValue(value);
		}

		return affectedParentNodes;
	}

	private CachedGraphNode<T> getNodeForDomainObject(EObject domainObject, EStructuralFeature feature, T value) {
		CachedGraphNode<T> node = domainModelSettings.getNode(domainObject, feature);

		if (node == null) {
			node = domainModelSettings.createNode(domainObject, feature, value);
		}

		return node;
	}

	private void removeChildFromParents(final CachedGraphNode<T> childNode) {
		final Set<CachedGraphNode<T>> parentNodes = new LinkedHashSet<CachedGraphNode<T>>(childNode.getParents());
		for (final CachedGraphNode<T> oldParent : parentNodes) {
			oldParent.removeChild(childNode);
		}
	}

	private boolean domainObjectHasBeenRemoved(EObject domainObject, EStructuralFeature feature) {
		final CachedGraphNode<T> node = domainModelSettings.getNode(domainObject, feature);
		if (node.isDomainObject() && !isDomainModelElement(domainObject)) {
			return true;
		}
		return false;
	}

	private CachedGraphNode<T> getNodeForRenderable(Renderable renderable) {
		CachedGraphNode<T> node = viewModelSettings.getNode(renderable, SettingsNodeMapping.allFeatures());

		// dynamic registration
		if (node == null) {
			node = viewModelSettings.createNode(renderable, SettingsNodeMapping.allFeatures(), getDefaultValue());
			final EObject eContainer = renderable.eContainer();
			// must have parent
			CachedGraphNode<T> possibleParent = viewModelSettings.getNode(eContainer, SettingsNodeMapping.allFeatures());
			if (possibleParent == null) {
				possibleParent = getNodeForRenderable((Renderable) eContainer);
			}
			possibleParent.addChild(node);
		}

		return node;
	}

	private boolean isDomainModelElement(EObject possibleChild) {
		return isContainedInModel(domainModel, possibleChild);
	}

	private boolean isContainedInModel(EObject root, EObject possibleChild) {

		if (root == possibleChild) {
			return true;
		}

		EObject container = possibleChild;
		while (container != null) {
			if (container == root) {
				return true;
			}
			container = container.eContainer();
		}

		return false;
	}
}
