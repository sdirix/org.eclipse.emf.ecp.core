package org.eclipse.emf.emfstore.fx.internal.projects;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.emfstore.client.ESWorkspaceProvider;

public class ProjectsView {

	@Inject
	private ESelectionService selectionService;
	ComposedAdapterFactory adapterFactory;

	@Inject
	public ProjectsView() {
		adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
	}

	@PostConstruct
	public void postConstruct(BorderPane parent) {

		final TreeView<Object> localProjectsView = new TreeView<>();

		Button btnAddProject = new Button("Add LocalProject");
		btnAddProject.setMaxWidth(Double.MAX_VALUE);
		final EmfStoreLocalTreeItem projectTreeItem = new EmfStoreLocalTreeItem(
				this, ESWorkspaceProvider.INSTANCE.getWorkspace(),
				localProjectsView);
		btnAddProject.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ESWorkspaceProvider.INSTANCE.getWorkspace().createLocalProject(
						"test");
				projectTreeItem.updateChildren();
			}
		});



		

	
		HBox box = new HBox();
		HBox.setHgrow(btnAddProject, Priority.ALWAYS);
		box.getChildren().add(btnAddProject);

		
		localProjectsView.setRoot(projectTreeItem);
		localProjectsView.setShowRoot(false);

		localProjectsView
				.setCellFactory(new Callback<TreeView<Object>, TreeCell<Object>>() {

					@Override
					public TreeCell<Object> call(TreeView<Object> param) {

						return new ESLocalProjectTreeCell(ProjectsView.this,projectTreeItem);
					}
				});
		localProjectsView.getSelectionModel().selectedItemProperty()
		.addListener(new ChangeListener<TreeItem<Object>>() {

			@Override
			public void changed(
					ObservableValue<? extends TreeItem<Object>> observable,
					TreeItem<Object> oldValue, TreeItem<Object> newValue) {
				
				if (newValue != null)
					selectionService.setSelection(newValue.getValue());
				
				TreeItem<Object> selectedItem = localProjectsView
						.getSelectionModel().getSelectedItem();
				
				if (selectedItem == null)
					return;
			}
		});
		parent.setTop(box);
		parent.setCenter(localProjectsView);

	}
}
