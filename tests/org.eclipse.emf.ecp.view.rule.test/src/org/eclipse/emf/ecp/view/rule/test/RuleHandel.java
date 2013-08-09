/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.rule.test;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.rule.model.Rule;

/**
 * @author Jonas
 * 
 */
public class RuleHandel {

	public final Rule rule;
	public final Renderable parent;
	public final EObject domainObject;

	/**
	 * @param rule
	 * @param renderable
	 * @param domainObject
	 */
	public RuleHandel(Rule rule, Renderable renderable, EObject domainObject) {
		this.rule = rule;
		// TODO Auto-generated constructor stub
		parent = renderable;
		this.domainObject = domainObject;
	}

}
