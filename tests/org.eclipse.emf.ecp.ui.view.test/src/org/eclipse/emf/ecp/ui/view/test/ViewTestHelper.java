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

import java.util.Collection;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.exceptions.ECPProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider;
import org.eclipse.emf.ecp.explorereditorbridge.internal.ECPControlContextImpl;
import org.eclipse.emf.ecp.internal.core.ECPProjectManagerImpl;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
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
	public static ECPControlContext createECPControlContext(View view, Shell shell) {
		// setup context
		@SuppressWarnings("restriction")
		final ECPProvider provider = ECPUtil.getECPProviderRegistry().getProvider(EMFStoreProvider.NAME);
		final Collection<ECPProject> projects = ECPUtil.getECPProjectManager().getProjects();
		for (final ECPProject ecpProject : projects) {
			ecpProject.delete();
		}
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

	/**
	 * @param node
	 * @return
	 */
	public static int countNodes(Node<?> node) {
		int i = 0;
		if (node != null) {
			i++;
		}
		for (final Node<?> subNode : node.getChildren()) {
			i = i + countNodes(subNode);
		}
		return i;
	}

	/**
	 * @param view
	 * @return
	 */
	public static Node<View> build(View view) {
		return NodeBuilders.INSTANCE.build(view, null);
	}

}
