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

import org.eclipse.emf.ecp.core.util.ECPElement;
import org.eclipse.emf.ecp.core.util.observer.IECPObserver;
import org.eclipse.emf.ecp.internal.core.Activator;
import org.eclipse.emf.ecp.spi.core.util.InternalElementRegistry.ResolveListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public abstract class ElementRegistry<ELEMENT extends ECPElement, OBSERVER extends IECPObserver> extends
	Registry<ELEMENT, OBSERVER> {
	private final List<ResolveListener<ELEMENT>> resolveListeners = new ArrayList<ResolveListener<ELEMENT>>();

	public ElementRegistry() {
	}

	public final synchronized void addResolveListener(ResolveListener<ELEMENT> listener) {
		resolveListeners.add(listener);
	}

	public final synchronized void removeResolveListener(ResolveListener<ELEMENT> listener) {
		resolveListeners.remove(listener);
	}

	@SuppressWarnings("unchecked")
	protected void descriptorChanged(ElementDescriptor<ELEMENT> descriptor, boolean resolved) {
		ResolveListener<ELEMENT>[] listeners;
		synchronized (this) {
			listeners = resolveListeners.toArray(new ResolveListener[resolveListeners.size()]);
		}

		for (ResolveListener<ELEMENT> listener : listeners) {
			try {
				listener.descriptorChanged(descriptor, resolved);
			} catch (Exception ex) {
				Activator.log(ex);
			}
		}
	}

	@Override
	protected String getElementName(ELEMENT element) {
		return element.getName();
	}
}
