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
package org.eclipse.emf.ecp.ui.view.test;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.exceptions.ECPProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.explorereditorbridge.internal.ECPControlContextImpl;
import org.eclipse.emf.ecp.internal.core.ECPProjectManagerImpl;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Jonas
 * 
 */
public class ViewTestHelper {

	/**
	 * @param view
	 * @param shell
	 * @return an {@link ECPControlContext}
	 */
	public static ECPControlContextImpl createECPControlContext(View view, Shell shell) {
		// setup context
		@SuppressWarnings("restriction")
		final ECPProvider provider = ECPUtil.getECPProviderRegistry().getProvider(EMFStoreProvider.NAME);
		ECPProject project;
		try {
			project = ECPProjectManagerImpl.INSTANCE.createProject(provider, "test");
			project.getContents().add(view);
			return new ECPControlContextImpl(view, project, shell);
		} catch (final ECPProjectWithNameExistsException ex) {
			// Should no happen during tests
			System.err.println("Project with name already exists, clean-up test environment");
		}
		return null;
	}

}
