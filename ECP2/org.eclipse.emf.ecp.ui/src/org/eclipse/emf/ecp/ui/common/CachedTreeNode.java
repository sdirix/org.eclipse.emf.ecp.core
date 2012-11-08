package org.eclipse.emf.ecp.ui.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A node within an {@link AbstractCachedTree}.
 * 
 * @param <T> the type of the value stored by this node
 * 
 * @author emueller
 */
public abstract class CachedTreeNode<T> {

	private T value;
	private Map<Object, T> cache;
	public Object parent;

	/**
	 * Constructor.
	 * 
	 * @param initialValue
	 *            the initial value
	 */
	public CachedTreeNode(T initialValue) {
		this.value = initialValue;
		cache = new HashMap<Object, T>();
	}

	/**
	 * Recomputes the cached value of this node.
	 */
	protected abstract void update();

	/**
	 * Puts a value into the cache.
	 * 
	 * @param key
	 *            the (child) object that contains the given value
	 * @param value
	 *            an additional value that will be considered for the computation of the
	 *            actual value that results to a {@link #update()} call
	 */
	public void putIntoCache(Object key, T value) {
		cache.put(key, value);
		update();
	}

	/**
	 * Removes a (child) object from the cache.
	 * 
	 * @param key
	 *            the object to be removed
	 */
	public void removeFromCache(Object key) {
		cache.remove(key);
		update();
	}

	/**
	 * Returns the value of this node.
	 * 
	 * @return the value stored within this node
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Sets the value of this node.
	 * 
	 * @param newValue
	 *            the new value to be associated with this node
	 */
	public void setValue(T newValue) {
		this.value = newValue;
	}

	/**
	 * Returns the cached values that are stored in the children nodes.
	 * 
	 * @return a set of values stored in the children nodes of this node
	 */
	public Collection<T> values() {
		return cache.values();
	}
}