/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.internal.swt.treemasterdetail.defaultprovider;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.ecp.common.spi.ChildrenDescriptorCollector;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emfforms.spi.swt.treemasterdetail.MenuProvider;
import org.eclipse.emfforms.spi.swt.treemasterdetail.TreeMasterDetailMenuListener;
import org.eclipse.emfforms.spi.swt.treemasterdetail.util.CreateElementCallback;
import org.eclipse.emfforms.spi.swt.treemasterdetail.util.MasterDetailAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Menu;

/**
 * The default menu provider.
 *
 * @author Johannes Faltermeier
 *
 */
public final class DefaultMenuProvider implements MenuProvider {

	private ChildrenDescriptorCollector childrenDescriptorCollector;
	private Collection<MasterDetailAction> rightClickActions;
	private CreateElementCallback createElementCallback;

	/**
	 * Default constructor.
	 */
	public DefaultMenuProvider() {
		childrenDescriptorCollector = new ChildrenDescriptorCollector();
		rightClickActions = Collections.<MasterDetailAction> emptySet();
		createElementCallback = new CreateElementCallback() {
			@Override
			public boolean beforeCreateElement(Object newElement) {
				return true;
			}
		};
	}

	/**
	 * Constructor.
	 * 
	 * @param childrenDescriptorCollector the child description collector to use
	 * @param rightClickActions the right click actions to use
	 * @param createElementCallback the create element callback
	 */
	public DefaultMenuProvider(ChildrenDescriptorCollector childrenDescriptorCollector,
		Collection<MasterDetailAction> rightClickActions,
		CreateElementCallback createElementCallback) {
		this.childrenDescriptorCollector = childrenDescriptorCollector;
		this.rightClickActions = rightClickActions;
		this.createElementCallback = createElementCallback;
	}

	@Override
	public Menu getMenu(TreeViewer treeViewer, EditingDomain editingDomain) {
		final MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr
			.addMenuListener(new TreeMasterDetailMenuListener(childrenDescriptorCollector, menuMgr, treeViewer,
				editingDomain, rightClickActions, createElementCallback));
		final Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		return menu;
	}
}