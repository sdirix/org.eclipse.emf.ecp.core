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
import java.util.Set;

/**
 * Cached tree implementation for dirty decorators of model elements managed by EMFStore.
 * 
 * @author emueller
 * 
 */
public final class EMFStoreDirtyDecoratorCachedTree extends AbstractCachedTree<EMFStoreDirtyTreeNode> {

	private static Map<ECPProject, EMFStoreDirtyDecoratorCachedTree> cashedTrees = new HashMap<ECPProject, EMFStoreDirtyDecoratorCachedTree>();

	/**
	 * Static {@link EMFStoreDirtyDecoratorCachedTree} singleton.
	 * 
	 * @param project the {@link ECPProject} to initialize this CashedTree on
	 * @return Static instance of the {@link EMFStoreDirtyDecoratorCachedTree}
	 */
	public static EMFStoreDirtyDecoratorCachedTree getInstance(final ECPProject project) {
		if (!cashedTrees.containsKey(project)) {
			cashedTrees.put(project, new EMFStoreDirtyDecoratorCachedTree(new IExcludedObjectsCallback() {

				public boolean isExcluded(Object object) {
					return project.isModelRoot(object);
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
	public class CachedDirtyStateTreeNode extends CachedTreeNode<EMFStoreDirtyTreeNode> {

		/**
		 * Constructor.
		 * 
		 * @param value
		 *            the initial value for this entry
		 */
		public CachedDirtyStateTreeNode(EMFStoreDirtyTreeNode value) {
			super(value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void update() {
			for (EMFStoreDirtyTreeNode node : values()) {
				if (node.getChangeCount() > 0 || node.isChildChanges()) {
					getOwnValue().setChildChanges(true);
					return;
				}
			}
			getOwnValue().setChildChanges(false);
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.emf.ecp.ui.common.CachedTreeNode#getDisplayValue()
		 */
		@Override
		public EMFStoreDirtyTreeNode getDisplayValue() {
			return getOwnValue();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EMFStoreDirtyTreeNode getDefaultValue() {
		return new EMFStoreDirtyTreeNode(0, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CachedTreeNode<EMFStoreDirtyTreeNode> createdCachedTreeNode(EMFStoreDirtyTreeNode t) {
		return new CachedDirtyStateTreeNode(t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateParentNode(Object parent, Object object, EMFStoreDirtyTreeNode value) {
		EMFStoreDirtyTreeNode parentValue = getCachedValue(parent);
		parentValue.setChildChanges(value.shouldDisplayDirtyIndicator());
		super.updateParentNode(parent, object, parentValue);
	}

	/**
	 * Call to indicate that an {@link EObject} was added.
	 * 
	 * @param eObject the new {@link EObject}
	 * @return the {@link Set} of {@link EObject} affected by this change
	 */
	public Set<EObject> addOperation(EObject eObject) {
		EMFStoreDirtyTreeNode node = getCachedValue(eObject);
		node.setChangeCount(node.getChangeCount() + 1);
		return update(eObject, node);
	}

	/**
	 * Call to indicate that an {@link EObject} was removed.
	 * 
	 * @param eObject the removed {@link EObject}
	 * @return the {@link Set} of {@link EObject} affected by this change
	 */
	public Set<EObject> removeOperation(EObject eObject) {
		EMFStoreDirtyTreeNode node = getCachedValue(eObject);
		node.setChangeCount(node.getChangeCount() - 1);
		boolean hasChanges = false;
		for (EObject child : eObject.eContents()) {
			EMFStoreDirtyTreeNode childNode = getCachedValue(child);
			if (childNode.shouldDisplayDirtyIndicator()) {
				hasChanges = true;
				break;
			}
		}
		node.setChildChanges(hasChanges);
		return update(eObject, node);
	}
}
