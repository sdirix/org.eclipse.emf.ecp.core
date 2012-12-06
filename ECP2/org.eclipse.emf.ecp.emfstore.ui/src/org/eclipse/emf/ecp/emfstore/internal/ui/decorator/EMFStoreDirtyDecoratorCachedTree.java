/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.emfstore.internal.ui.decorator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.ui.common.AbstractCachedTree;
import org.eclipse.emf.ecp.ui.common.CachedTreeNode;
import org.eclipse.emf.ecp.ui.common.IExcludedObjectsCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Cached tree implementation for dirty decorators of model elements managed by EMFStore.
 * 
 * @author emueller
 * 
 */
public final class EMFStoreDirtyDecoratorCachedTree extends AbstractCachedTree<Integer> {

	private static Map<ECPProject, EMFStoreDirtyDecoratorCachedTree> cashedTrees = new HashMap<ECPProject, EMFStoreDirtyDecoratorCachedTree>();

	/**
	 * Static {@link EMFStoreDirtyDecoratorCachedTree} singleton.
	 * 
	 * @return Static instance of the {@link EMFStoreDirtyDecoratorCachedTree}
	 */
	public static EMFStoreDirtyDecoratorCachedTree getInstance(final ECPProject project) {
		if (!cashedTrees.containsKey(project)) {
			cashedTrees.put(project, new EMFStoreDirtyDecoratorCachedTree(new IExcludedObjectsCallback() {

				public boolean isExcluded(Object object) {
					return project.getModelRoot(object);
				}
			}));
		}
		return cashedTrees.get(project);
	}

	/**
	 * Private constructor.
	 */
	private EMFStoreDirtyDecoratorCachedTree(IExcludedObjectsCallback callback) {
		super(callback);
	}

	/**
	 * Cached tree node that stores the dirty state of a model element managed by EMFStore.
	 */
	public class CachedDirtyStateTreeNode extends CachedTreeNode<Integer> {

		/**
		 * Constructor.
		 * 
		 * @param value
		 *            the initial value for this entry
		 */
		public CachedDirtyStateTreeNode(Integer value) {
			super(value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void update() {
			for (Integer isDirty : values()) {
				if (isDirty > 0) {
					setValue(isDirty);
					return;
				}
			}

			setValue(0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getDefaultValue() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CachedTreeNode<Integer> createdCachedTreeNode(Integer t) {
		return new CachedDirtyStateTreeNode(t);
	}

	public void addOperation(EObject eObject) {
		update(eObject, getCachedValue(eObject) + 1);
	}

	public void removeOperation(EObject eObject) {
		update(eObject, getCachedValue(eObject) - 1);
	}
}
