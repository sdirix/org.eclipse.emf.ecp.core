/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Müller - initial API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.ui.common;

import org.eclipse.emf.ecore.EObject;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A cached tree resembles a tree structure where each node is associated with a specific value.
 * The value stored by parent nodes within the tree hereby are influenced by the values stored within their children.
 * Thus, if the value of a child changes, it needs to be propagated upwards.
 * Analogously, if the child is deleted the value associated with the deleted child must not influence the value of
 * the parent anymore. This class is responsible for adding and removing nodes and for correctly maintaining such
 * parent/child relationships.
 * 
 * @param <T> the actual value type that is stored by the tree
 * 
 * @author emueller
 */
public abstract class AbstractCachedTree<T> {

	private Map<Object, CachedTreeNode<T>> nodes;
	private CachedTreeNode<T> rootValue;

	/**
	 * Empty set provided for convenience reasons.
	 */
	protected static final Set<? extends Object> EMPTY_SET = Collections.emptySet();

	/**
	 * Private constructor.
	 */
	public AbstractCachedTree() {
		nodes = new LinkedHashMap<Object, CachedTreeNode<T>>();
		rootValue = createdCachedTreeNode(getDefaultValue());
	}

	/**
	 * Returns the default value for a cached node.<br/>
	 * The root value will be initialized with this value, too
	 * 
	 * @return the default value for a cached tree node
	 */
	public abstract T getDefaultValue();

	/**
	 * Creates a cached tree node.
	 * 
	 * @param value
	 *            the value stored by the cached tree node
	 * @return the created node
	 */
	protected abstract CachedTreeNode<T> createdCachedTreeNode(T value);

	/**
	 * Updates the cached entry for the given {@link EObject} with the given value.<br/>
	 * If the cached entry does not yet exist, it will be created.
	 * 
	 * @param eObject
	 *            the {@link EObject}
	 * @param value
	 *            the value associated with the {@link EObject}
	 * @param excludedObjects
	 *            a set of excluded objects that are ignored during propagation of updated value
	 */
	public void update(EObject eObject, T value, Set<? extends Object> excludedObjects) {

		if (excludedObjects.contains(eObject)) {
			return;
		}

		updateNode(eObject, value);
		rootValue.putIntoCache(eObject, value);

		// propagate upwards
		EObject parent = eObject.eContainer();

		while (parent != null && !excludedObjects.contains(parent)) {// !isExcludedType(excludedTypes,
																		// parent.getClass())
			updateParentNode(parent, eObject, value);
			parent = parent.eContainer();
			eObject = parent;
		}
	}

	/**
	 * Returns the root value of this tree.<br/>
	 * Note that the root is purely notional.
	 * 
	 * @return the node
	 */
	public T getRootValue() {
		return rootValue.getValue();
	}

	/**
	 * Returns the cached value stored for the given {@link EObject}.
	 * 
	 * @param eObject
	 *            the {@link EObject} whose cached value should be found
	 * 
	 * @return the cached value associated with the given {@link EObject}, if found, otherwise
	 *         the default value which is returned via {@link #getDefaultValue()}
	 */
	public T getCachedValue(Object eObject) {
		CachedTreeNode<T> nodeEntry = nodes.get(eObject);

		if (nodeEntry != null) {
			return nodes.get(eObject).getValue();
		}

		return getDefaultValue();
	}

	/**
	 * Removes the cache entry that contains the given {@link EObject}.
	 * 
	 * @param eObject
	 *            the {@link EObject} that needs to be removed from the cached tree
	 * @param excludedObjects
	 *            a set of excluded objects that are ignored during propagation of updates values
	 */
	public void remove(EObject eObject, Set<? extends Object> excludedObjects) {

		CachedTreeNode<T> node = nodes.get(eObject);
		CachedTreeNode<T> parentNode = nodes.get(node.parent);

		nodes.remove(eObject);
		rootValue.removeFromCache(eObject);

		if (parentNode == null) {
			return;
		}

		// propagate removed value upwards
		parentNode.removeFromCache(eObject);
		EObject parent = eObject.eContainer();

		while (parent != null && !excludedObjects.contains(parent)) {// !isExcludedType(excludedTypes,
																		// parent.getClass())

			node = nodes.get(parent);
			node.removeFromCache(eObject);

			parent = parent.eContainer();
			eObject = parent;
		}
	}

	/**
	 * Removes the cache entry that contains the given {@link EObject}.
	 * 
	 * @param eObject
	 *            the {@link EObject} that needs to be removed from the cached tree
	 */
	public void remove(EObject eObject) {
		remove(eObject, EMPTY_SET);
	}

	private void updateNode(Object object, T t) {
		CachedTreeNode<T> node = nodes.get(object);

		if (node == null) {
			node = createNodeEntry(object, t);
		}

		node.setValue(t);
	}

	private CachedTreeNode<T> createNodeEntry(Object object, T t) {
		CachedTreeNode<T> node = createdCachedTreeNode(t);
		nodes.put(object, node);
		return node;
	}

	private void updateParentNode(Object parent, Object object, T value) {
		CachedTreeNode<T> node = nodes.get(object);
		CachedTreeNode<T> parentNode = nodes.get(parent);
		node.parent = parent;

		if (parentNode == null) {
			parentNode = createNodeEntry(parent, value);
		}

		parentNode.putIntoCache(object, value);
		rootValue.putIntoCache(parent, value);
	}

	/**
	 * Clears the cache.
	 */
	public void clear() {
		nodes.clear();
		rootValue = createdCachedTreeNode(getDefaultValue());
	}
}
