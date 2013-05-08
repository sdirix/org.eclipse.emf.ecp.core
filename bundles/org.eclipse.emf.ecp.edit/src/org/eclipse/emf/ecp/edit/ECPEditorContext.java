/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.edit;

/**
 * This Context adds methods that are specifically needed by an editor.
 * @author Eugen Neufeld
 *
 */
public interface ECPEditorContext extends ECPControlContext{

	
	/**
	 * Called if the context is not used anymore. Use for cleanup.
	 */
	void dispose();
	
	/**
	 * Adds a {@link ECPContextDisposedListener}.
	 * 
	 * @param ecpContextDisposedListener
	 *            the {@link ECPContextDisposedListener}
	 */
	void addECPContextDisposeListener(ECPContextDisposedListener ecpContextDisposedListener);
}
