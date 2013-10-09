/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.common.test;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

import org.eclipse.emf.ecore.EObject;

/**
 * Convenience class for 
 * 
 * @author emueller
 */
public class GCCollectable {

	private static final int DEFAULT_TIMEOUT = 1000;

	private final ReferenceQueue<EObject> queue;
	private final PhantomReference<EObject> phantomRef;

	private boolean result;

	private boolean runMe;
	private Thread t;

	/**
	 * Constructor.
	 * 
	 * @param obj
	 * 			the object that is supposed to be finalized
	 */
	public GCCollectable(EObject obj) {
		queue = new ReferenceQueue<EObject>();
		runMe = true;
		phantomRef = new PhantomReference<EObject>(obj, queue);
		result = false;
		t = new Thread() {
			@Override
			public void run() {
				while (runMe) {
					
					Reference<?> ref = null;
					try {
						ref = queue.remove(DEFAULT_TIMEOUT);
					} catch (final IllegalArgumentException e) {
						// ignore
					} catch (final InterruptedException e) {
						// ignore
					}
					if (phantomRef == ref) {
						result = true;
					}
				}
			}
		};
		
		t.start();
	}

	/**
	 * Checks whether the the object contained by the {@link GCCollectable} may be finalized.
	 * 
	 * @return {@code true}, if the contained object may be finalized, {@code false} otherwise 
	 */
	public boolean isCollectable() {
		int retry = 0;
		// FIXME: GC is non-deterministic :(
		while (retry < 3 && !result) {
			System.gc();
			System.runFinalization();
			retry++;
		}
		runMe = false;
		
		return result;
	}

}
