/*******************************************************************************
 * Copyright (c) 2011-2016 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.swt.treemasterdetail.decorator.validation.ecp;

import java.util.Collection;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecp.common.spi.cachetree.AbstractCachedTree;
import org.eclipse.emf.ecp.common.spi.cachetree.CachedTreeNode;
import org.eclipse.emf.ecp.common.spi.cachetree.IExcludedObjectsCallback;

/**
 * Cache for diagnostic results.
 *
 * @author Johannes Faltermeier
 *
 */
public class DiagnosticCache extends AbstractCachedTree<Diagnostic> {

	/**
	 * Default constructor.
	 */
	public DiagnosticCache() {
		super(new IExcludedObjectsCallback() {

			@Override
			public boolean isExcluded(Object object) {
				return false;
			}
		});
	}

	@Override
	public Diagnostic getDefaultValue() {
		return Diagnostic.OK_INSTANCE;
	}

	@Override
	protected CachedTreeNode<Diagnostic> createdCachedTreeNode(Diagnostic value) {
		return new DiagnosticTreeNode(value);
	}

	/**
	 * Tree node for diagnostics.
	 * 
	 * @author Johannes Faltermeier
	 *
	 */
	private class DiagnosticTreeNode extends CachedTreeNode<Diagnostic> {

		DiagnosticTreeNode(Diagnostic diagnostic) {
			super(diagnostic);
		}

		@Override
		public void update() {
			final Collection<Diagnostic> severities = values();

			if (severities.size() > 0) {
				Diagnostic mostSevereDiagnostic = values().iterator().next();
				for (final Diagnostic diagnostic : severities) {
					if (diagnostic.getSeverity() > mostSevereDiagnostic.getSeverity()) {
						mostSevereDiagnostic = diagnostic;
					}
				}
				setChildValue(mostSevereDiagnostic);
				return;
			}
			setChildValue(getDefaultValue());
		}

		@Override
		public Diagnostic getDisplayValue() {
			if (getChildValue() == null) {
				return getOwnValue();
			}
			return getOwnValue().getSeverity() > getChildValue().getSeverity() ? getOwnValue() : getChildValue();
		}
	}

}
