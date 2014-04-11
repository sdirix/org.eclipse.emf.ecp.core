/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.internal.ui.validation;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecp.ui.validation.ECPValidationViewService;

/**
 * Default implementation of the {@link ECPValidationViewService}.
 * 
 * @author jfaltermeier
 * 
 */
// TODO should there be a dedicated plugin with the implementation?
public class ValidationViewServiceImpl implements ECPValidationViewService {

	private final Set<ECPValidationViewServiceListener> listener;

	/**
	 * Constructor.
	 */
	public ValidationViewServiceImpl() {
		listener = new LinkedHashSet<ECPValidationViewService.ECPValidationViewServiceListener>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.validation.ECPValidationViewService#setInput(org.eclipse.emf.common.util.Diagnostic[])
	 */
	@Override
	public void setInput(Diagnostic[] diagnostic) {
		notifyListener(diagnostic);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.validation.ECPValidationViewService#setInput(org.eclipse.emf.common.util.Diagnostic)
	 */
	@Override
	public void setInput(Diagnostic diagnostic) {
		notifyListener(diagnostic);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.validation.ECPValidationViewService#setInput(java.util.List)
	 */
	@Override
	public void setInput(List<Diagnostic> diagnostic) {
		notifyListener(diagnostic);
	}

	private void notifyListener(Object diagnostic) {
		final Iterator<ECPValidationViewServiceListener> iterator = listener.iterator();
		while (iterator.hasNext()) {
			iterator.next().inputChanged(diagnostic);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.validation.ECPValidationViewService#register(org.eclipse.emf.ecp.ui.validation.ECPValidationViewService.ECPValidationViewServiceListener)
	 */
	@Override
	public void register(ECPValidationViewServiceListener listener) {
		this.listener.add(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.validation.ECPValidationViewService#deregister(org.eclipse.emf.ecp.ui.validation.ECPValidationViewService.ECPValidationViewServiceListener)
	 */
	@Override
	public void deregister(ECPValidationViewServiceListener listener) {
		this.listener.remove(listener);
	}

}
