package org.eclipse.emf.ecp.emfstore.internal.ui.decorator;

/**
 * @author Tobias Verhoeven
 */
public class EMFStoreDirtyTreeNode {

	private int changeCount;
	private boolean childChanges;

	public EMFStoreDirtyTreeNode(int changeCount, boolean childChanges) {
		this.changeCount = changeCount;
		this.childChanges = childChanges;
	}

	public boolean shouldDisplayDirtyIndicator() {
		return childChanges || changeCount > 0;
	}

	public int getChangeCount() {
		return changeCount;
	}

	public void setChangeCount(int changeCount) {
		this.changeCount = changeCount;
	}

	public boolean isChildChanges() {
		return childChanges;
	}

	public void setChildChanges(boolean childChanges) {
		this.childChanges = childChanges;
	}
}