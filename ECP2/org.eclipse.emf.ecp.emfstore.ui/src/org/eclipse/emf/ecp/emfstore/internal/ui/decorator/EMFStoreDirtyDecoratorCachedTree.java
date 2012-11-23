package org.eclipse.emf.ecp.emfstore.internal.ui.decorator;

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
public final class EMFStoreDirtyDecoratorCachedTree extends AbstractCachedTree<Boolean> {

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
