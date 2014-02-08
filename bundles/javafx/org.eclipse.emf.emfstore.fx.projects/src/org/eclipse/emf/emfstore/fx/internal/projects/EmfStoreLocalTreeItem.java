package org.eclipse.emf.emfstore.fx.internal.projects;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TreeItem;

import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.ESWorkspace;
import org.eclipse.fx.emf.edit.ui.AdapterFactoryTreeItem;
import org.eclipse.fx.emf.edit.ui.CellUtil;

class EmfStoreLocalTreeItem extends TreeItem<Object> {

	private final ProjectsView projectsView;
	private final ObservableList<TreeItem<Object>> children;
	private Control view;

	public EmfStoreLocalTreeItem(ProjectsView projectsView, Object root,
			Control view) {
		super(root);
		this.projectsView = projectsView;
		this.view = view;

		children = FXCollections
				.unmodifiableObservableList(super.getChildren());
		updateChildren();
	}

	@Override
	public ObservableList<TreeItem<Object>> getChildren() {
		return children;
	}

	void updateChildren() {
		ObservableList<TreeItem<Object>> childTreeItems = super.getChildren();

		MultipleSelectionModel<?> selectionModel = CellUtil
				.getSelectionModel(view);
		List<?> selection = selectionModel.getSelectedItems();
		ArrayList<Object> selectedItems = new ArrayList<>();
		ArrayList<TreeItem<?>> selectedTreeItems = new ArrayList<>();
		ArrayList<Object> expandedItems = new ArrayList<>();

		// remember the expanded items
		for (TreeItem<Object> childTreeItem : childTreeItems) {
			if (childTreeItem.isExpanded())
				expandedItems.add(childTreeItem.getValue());
		}

		// remember the selected items
		for (Object selectedTreeItem : selection) {
			for (TreeItem<Object> childTreeItem : childTreeItems) {
				if (selectedTreeItem == childTreeItem) {
					selectedTreeItems.add(childTreeItem);
					selectedItems.add(childTreeItem.getValue());
				}
			}
		}

		// clear the selection
		for (TreeItem<?> selectedTreeItem : selectedTreeItems) {
			int treeItemIndex = selectionModel.getSelectedItems().indexOf(
					selectedTreeItem);
			int selectionIndex = selectionModel.getSelectedIndices().get(
					treeItemIndex);
			selectionModel.clearSelection(selectionIndex);
		}

		// remove the old tree items
		childTreeItems.clear();

		Object selectedValue = getValue();
		if (ESWorkspace.class.isInstance(selectedValue)) {
			updatedWorkspace((ESWorkspace) selectedValue, expandedItems,selectedItems,selectionModel);
		} else if (ESLocalProject.class.isInstance(selectedValue)) {
			updateProject((ESLocalProject) selectedValue,  expandedItems,selectedItems,selectionModel);
		} else {
			updateEObjects(selectedValue,  expandedItems,selectedItems,selectionModel);
		}

	}
	
	private void updateTree(Object child,TreeItem<?> treeItem,List<Object> expandedItems,List<Object> selectedItems,SelectionModel<?> selectionModel) {
		// expand the new tree items
		if (expandedItems.contains(child))
			treeItem.setExpanded(true);

		// restore the selection
		if (selectedItems.contains(child)
				&& "javafx.scene.control.TreeView$TreeViewBitSetSelectionModel"
						.equals(selectionModel.getClass().getName())) {
			try {
				Method m = selectionModel.getClass().getDeclaredMethod(
						"select", new Class[] { TreeItem.class });
				m.setAccessible(true);
				m.invoke(selectionModel, treeItem);
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	private void updatedWorkspace(ESWorkspace selectedValue, List<Object> expandedItems, List<Object> selectedItems, SelectionModel<?> selectionModel) {
		ObservableList<TreeItem<Object>> childTreeItems = super.getChildren();
		for (ESLocalProject localProject : selectedValue.getLocalProjects()) {
			EmfStoreLocalTreeItem treeItem = new EmfStoreLocalTreeItem(
					this.projectsView, localProject, view);

			childTreeItems.add(treeItem);

			updateTree(localProject,treeItem,expandedItems,selectedItems,selectionModel);

		}
	}

	

	private void updateEObjects(Object selectedValue, ArrayList<Object> expandedItems, ArrayList<Object> selectedItems, MultipleSelectionModel<?> selectionModel) {
		Object adapter = this.projectsView.adapterFactory.adapt(selectedValue,
				ITreeItemContentProvider.class);
		ITreeItemContentProvider provider = (adapter instanceof ITreeItemContentProvider) ? (ITreeItemContentProvider) adapter
				: null;

		ObservableList<TreeItem<Object>> childTreeItems = super.getChildren();
		for (Object child : provider.getChildren(getValue())) {
			AdapterFactoryTreeItem treeItem = new AdapterFactoryTreeItem(child,
					view, this.projectsView.adapterFactory);

			childTreeItems.add(treeItem);
			updateTree(child,treeItem,expandedItems,selectedItems,selectionModel);
		}
	}

	void updateProject(ESLocalProject selectedValue, ArrayList<Object> expandedItems, ArrayList<Object> selectedItems, MultipleSelectionModel<?> selectionModel) {
		ObservableList<TreeItem<Object>> childTreeItems = super.getChildren();
		for (Object child : selectedValue.getModelElements()) {
			AdapterFactoryTreeItem treeItem = new AdapterFactoryTreeItem(child,
					view, this.projectsView.adapterFactory);

			childTreeItems.add(treeItem);
			updateTree(child,treeItem,expandedItems,selectedItems,selectionModel);
		}
	}

}