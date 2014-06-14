/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.internal.categorization.swt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.model.common.ECPRendererTester;
import org.eclipse.emf.ecp.view.spi.categorization.model.VCategorization;
import org.eclipse.emf.ecp.view.spi.categorization.model.VCategorizationElement;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;

/**
 * Renderer Tester for composite category as tabs.
 * 
 * @author Eugen Neufeld
 * 
 */
@SuppressWarnings("restriction")
public class CompositeCategoryTabRendererTester implements ECPRendererTester {

	@Override
	public int isApplicable(VElement vElement, ViewModelContext viewModelContext) {
		if (VCategorization.class.isInstance(vElement)) {
			final VCategorization categorization = VCategorization.class.cast(vElement);
			int depth = 0;
			EObject parent = categorization.eContainer();
			while (!VCategorizationElement.class.isInstance(parent)) {
				parent = parent.eContainer();
				depth++;
			}
			if (VCategorizationElement.class.cast(parent).getMainCategoryDepth() > depth + 1) {
				return 1;
			}
		}
		return NOT_APPLICABLE;
	}

}
