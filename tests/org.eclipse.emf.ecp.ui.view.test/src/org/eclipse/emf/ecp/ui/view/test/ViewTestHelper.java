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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;
import org.eclipse.emf.ecp.core.exceptions.ECPProjectWithNameExistsException;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.explorereditorbridge.internal.ECPControlContextImpl;
import org.eclipse.emf.ecp.internal.core.ECPProjectManagerImpl;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Jonas
 * 
 */
// APITODO missing API
@SuppressWarnings("restriction")
public final class ViewTestHelper {

	private static ECPControlContext context;

	private ViewTestHelper() {

	}

	/**
	 * Creates an {@link ECPControlContext} for the given domain object.
	 * 
	 * @param domainObject
	 *            the domain to create the context for
	 * @param shell
	 *            the shell used by the created context
	 * @return an {@link ECPControlContext} for the given domain object
	 */
	public static ECPControlContext createECPControlContext(EObject domainObject, Shell shell) {
		// setup context
		final ECPProvider provider = ECPUtil.getECPProviderRegistry().getProvider(
			org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider.NAME);
		final Collection<ECPProject> projects = ECPUtil.getECPProjectManager().getProjects();

		for (final ECPProject ecpProject : projects) {
			ecpProject.delete();
		}

		ECPProject project;
		try {
			project = ECPProjectManagerImpl.INSTANCE.createProject(provider, "test");
			project.getContents().add(domainObject);
			return new ECPControlContextImpl(domainObject, project, shell);
		} catch (final ECPProjectWithNameExistsException ex) {
			// Should not happen during tests
			System.err.println("Project with name already exists, clean-up test environment");
		}

		return null;
	}

	/**
	 * Creates an {@link ECPControlContext} for the given domain object.
	 * 
	 * @param domainObject
	 *            the domain to create the context for
	 * @param shell
	 *            the shell used by the created context
	 * @param view
	 *            the view that is used to create the {@link ViewModelContext} of the ECP control context
	 * @return an {@link ECPControlContext} for the given domain object
	 */
	public static ECPControlContext createECPControlContext(EObject domainObject, Shell shell, Renderable view) {
		// setup context

		final ECPProvider provider = ECPUtil.getECPProviderRegistry().getProvider(
			org.eclipse.emf.ecp.emfstore.core.internal.EMFStoreProvider.NAME);
		final Collection<ECPProject> projects = ECPUtil.getECPProjectManager().getProjects();

		for (final ECPProject ecpProject : projects) {
			ecpProject.delete();
		}

		ECPProject project;
		try {
			project = ECPProjectManagerImpl.INSTANCE.createProject(provider, "test");
			project.getContents().add(domainObject);
			return new ECPControlContextImpl(domainObject, project, shell, view);
		} catch (final ECPProjectWithNameExistsException ex) {
			// Should not happen during tests
			System.err.println("Project with name already exists, clean-up test environment");
		}

		return null;
	}

	/**
	 * Counts the node and all its children.
	 * 
	 * @param node
	 *            the node whose children should be counted. The node itself is also considered.
	 * @return the number of children of the given node + 1 (the node itself)
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
	 * Creates a {@link Node} containing the the given {@link Renderable}.
	 * 
	 * @param view
	 *            the {@link Renderable} for which to create a node tree
	 * @param domainObject
	 *            the domain object belonging to the given renderable
	 * @return the created node tree
	 */
	public static Node<Renderable> build(Renderable view, EObject domainObject) {
		final Shell shell = new Shell();
		if (domainObject != null) {
			context = createECPControlContext(domainObject, shell, view);
		}
		final Node<Renderable> node = NodeBuilders.INSTANCE.build(view, context);
		return node;
	}

	public static ViewModelContext getViewModelContext() {
		return context.getViewContext();
	}

}
