/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.rule;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Convenience class for mapping keys to values and vice versa.
 * Both, keys and values, have to be unique.
 * 
 * @author emuller
 * 
 * @param <K>
 *            the type of the key
 * @param <V>
 *            the type of the value
 */
public class BidirectionalMap<K, V> {

	private final Map<K, V> kToV;
	private final Map<V, K> vToK;

	/**
	 * Default constructor.
	 */
	public BidirectionalMap() {
		kToV = new LinkedHashMap<K, V>();
		vToK = new LinkedHashMap<V, K>();
	}

	/**
	 * Associates the specified value with the specified key in this map
	 * and vice versa.
	 * 
	 * @param k
	 *            the key
	 * @param v
	 *            the value
	 */
	public void put(K k, V v) {
		kToV.put(k, v);
		vToK.put(v, k);
	}

	public void removeK(K k) {
		final V v = kToV.get(k);
		kToV.remove(k);
		vToK.remove(v);
	}

	public void removeV(V v) {
		final K k = vToK.get(v);
		vToK.remove(v);
		kToV.remove(k);
	}

	public V getK(K k) {
		return kToV.get(k);
	}

	public K getV(V v) {
		return vToK.get(v);
	}

}
