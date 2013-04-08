/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.internal.core.util;

import org.eclipse.net4j.util.lifecycle.Lifecycle;

import org.eclipse.emf.ecp.core.util.ECPDisposable;
import org.eclipse.emf.ecp.core.util.ECPDisposable.DisposeListener;
import org.eclipse.emf.ecp.core.util.observer.ECPObserver;
import org.eclipse.emf.ecp.internal.core.Activator;
import org.eclipse.emf.ecp.internal.core.util.observer.ECPObserverBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public abstract class Registry<ELEMENT, OBSERVER extends ECPObserver> extends Lifecycle implements DisposeListener {
	private static final ThreadLocal<Boolean> DISPOSING_ELEMENT = new InheritableThreadLocal<Boolean>();

	private Map<String, ELEMENT> elements = new HashMap<String, ELEMENT>();

	public Registry() {
	}

	public final ELEMENT getElement(String key) {
		checkActive();
		synchronized (this) {
			return elements.get(key);
		}
	}

	public final Set<String> getElementNames() {
		checkActive();
		synchronized (this) {
			// return elements.keySet().toArray(new String[elements.size()]);
			return Collections.unmodifiableSet(new HashSet<String>(elements.keySet()));
		}
	}

	public final int getElementCount() {
		checkActive();
		synchronized (this) {
			return elements.size();
		}
	}

	public final Collection<ELEMENT> getElements() {
		checkActive();
		synchronized (this) {
			return Collections.unmodifiableCollection(new ArrayList<ELEMENT>(elements.values()));
			// ELEMENT[] result = createElementArray(elements.size());
			// return elements.values().toArray(result);
		}
	}

	public final boolean hasElements() {
		checkActive();
		synchronized (this) {
			return !elements.isEmpty();
		}
	}

	public final boolean hasElement(String name) {
		checkActive();
		synchronized (this) {
			return elements.containsKey(name);
		}
	}

	public final void changeElements(Set<String> remove, Set<ELEMENT> add) {
		checkActive();
		doChangeElements(remove, add);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	public final void disposed(ECPDisposable disposable) {
		if (isRemoveDisposedElements()) {
			String name = getElementName((ELEMENT) disposable);
			if (name != null) {
				try {
					DISPOSING_ELEMENT.set(true);
					doChangeElements(Collections.singleton(name), null);
				} finally {
					DISPOSING_ELEMENT.remove();
				}
			}
		}
	}

	protected boolean isRemoveDisposedElements() {
		return true;
	}

	protected final Set<ELEMENT> doChangeElements(Set<String> remove, Set<ELEMENT> add) {
		Set<ELEMENT> result = null;
		Set<ELEMENT> oldElements = new HashSet<ELEMENT>();
		Set<ELEMENT> newElements = null;

		synchronized (this) {
			oldElements.addAll(elements.values());

			if (remove != null) {
				for (String name : remove) {
					ELEMENT element = elements.remove(name);
					if (element != null) {
						if (element instanceof ECPDisposable) {
							ECPDisposable disposable = (ECPDisposable) element;
							disposable.removeDisposeListener(this);
						}

						if (result == null) {
							result = new HashSet<ELEMENT>();
						}

						result.add(element);
					}
				}
			}

			if (add != null) {
				for (ELEMENT element : add) {
					String name = getElementName(element);
					elements.put(name, element);

					if (element instanceof ECPDisposable) {
						ECPDisposable disposable = (ECPDisposable) element;
						disposable.addDisposeListener(this);
					}
				}
			}

			if (!oldElements.equals(elements)) {
				newElements = new HashSet<ELEMENT>(elements.values());
			}
		}

		if (newElements != null) {
			// ELEMENT[] oldArray = oldElements.toArray(createElementArray(oldElements.size()));
			// ELEMENT[] newArray = newElements.toArray(createElementArray(newElements.size()));
			Collection<ELEMENT> unmodifiableOld = Collections.unmodifiableCollection(oldElements);
			Collection<ELEMENT> unmodifiableNew = Collections.unmodifiableCollection(newElements);
			elementsChanged(unmodifiableOld, unmodifiableNew);

			try {
				notifyObservers(unmodifiableOld, unmodifiableNew);
			} catch (Exception ex) {
				Activator.log(ex);
			}
		}

		return result;
	}

	public void addObserver(OBSERVER observer) {
		ECPObserverBus.getInstance().register(observer);
	}

	public void removeObserver(OBSERVER observer) {
		ECPObserverBus.getInstance().unregister(observer);
	}

	// private void notifyObservers(Collection<ELEMENT> oldArray, Collection<ELEMENT> newArray) throws Exception {
	// // TODO: remove warning
	// Class<OBSERVER> observerType = (Class<OBSERVER>) ((ParameterizedType) getClass().getGenericSuperclass())
	// .getActualTypeArguments()[1];
	// OBSERVER notify = ECPObserverBus.getInstance().notify(observerType);
	// notifyObservers(notify, oldArray, newArray);
	// }

	protected abstract void notifyObservers(Collection<ELEMENT> oldArray, Collection<ELEMENT> newArray)
		throws Exception;

	protected void elementsChanged(Collection<ELEMENT> oldElements, Collection<ELEMENT> newElements) {
		// Can be overridden in subclasses
	}

	// protected abstract ELEMENT[] createElementArray(int size);

	protected abstract String getElementName(ELEMENT element);

	protected static boolean isDisposingElement() {
		Boolean disposingElement = DISPOSING_ELEMENT.get();
		return disposingElement != null && disposingElement;
	}
}
