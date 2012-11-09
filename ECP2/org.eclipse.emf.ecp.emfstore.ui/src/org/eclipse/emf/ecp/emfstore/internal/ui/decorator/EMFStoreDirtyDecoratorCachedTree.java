package org.eclipse.emf.ecp.emfstore.internal.ui.decorator;

import org.eclipse.emf.ecp.ui.common.AbstractCachedTree;
import org.eclipse.emf.ecp.ui.common.CachedTreeNode;

/**
 * Cached tree implementation for dirty decorators of model elements managed by EMFStore.
 * 
 * @author emueller
 * 
 */
public final class EMFStoreDirtyDecoratorCachedTree extends AbstractCachedTree<Boolean> {

	/**
	 * Initializes the singleton instance statically.
	 */
	private static class SingletonHolder {
		public static final EMFStoreDirtyDecoratorCachedTree INSTANCE = new EMFStoreDirtyDecoratorCachedTree();
	}

	/**
	 * Static {@link EMFStoreDirtyDecoratorCachedTree} singleton.
	 * 
	 * @return Static instance of the {@link EMFStoreDirtyDecoratorCachedTree}
	 */
	public static EMFStoreDirtyDecoratorCachedTree getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * Private constructor.
	 */
	private EMFStoreDirtyDecoratorCachedTree() {

	}

	/**
	 * Cached tree node that stores the dirty state of a model element managed by EMFStore.
	 */
	public class CachedDirtyStateTreeNode extends CachedTreeNode<Boolean> {

		/**
		 * Constructor.
		 * 
		 * @param value
		 *            the initial value for this entry
		 */
		public CachedDirtyStateTreeNode(Boolean value) {
			super(value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void update() {
			for (Boolean isDirty : values()) {
				if (isDirty) {
					setValue(isDirty);
					return;
				}
			}

			setValue(false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getDefaultValue() {
		return Boolean.FALSE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CachedTreeNode<Boolean> createdCachedTreeNode(Boolean t) {
		return new CachedDirtyStateTreeNode(t);
	}

}
