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

//		final Button addElementButton = new Button("Add Element");
//		addElementButton.setMaxWidth(Double.MAX_VALUE);
//		addElementButton.setDisable(true);
//		addElementButton.setOnAction(new EventHandler<ActionEvent>() {
//
//			@Override
//			public void handle(ActionEvent event) {
//				TreeItem<Object> selectedItem = localProjectsView
//						.getSelectionModel().getSelectedItem();
//				if (selectedItem == null)
//					return;
//				Object selectedValue = selectedItem.getValue();
//				if (ESLocalProject.class.isInstance(selectedValue)) {
//					ESLocalProject localProject = (ESLocalProject) selectedValue;
//
//					// EObject result = TaskFactory.eINSTANCE.createUser();
//					CreateEObjectStage stage = new CreateEObjectStage();
//
//					stage.showAndWait();
//
//					EObject result = stage.getResult();
//					if (result == null)
//						return;
//					localProject.getModelElements().add(result);
//					projectTreeItem.updateChildren();
//				}
//			}
//		});

//		final Button btnSaveProject = new Button("Save");
//		btnSaveProject.setMaxWidth(Double.MAX_VALUE);
//		btnSaveProject.setDisable(true);
//		btnSaveProject.setOnAction(new EventHandler<ActionEvent>() {
//
//			@Override
//			public void handle(ActionEvent event) {
//				TreeItem<Object> selectedItem = localProjectsView
//						.getSelectionModel().getSelectedItem();
//				if (selectedItem == null)
//					return;
//				Object selectedValue = selectedItem.getValue();
//				ESLocalProject localProject = null;
//				if (ESLocalProject.class.isInstance(selectedValue)) {
//					localProject = (ESLocalProject) selectedValue;
//				} else if (EObject.class.isInstance(selectedValue)) {
//					localProject = ESWorkspaceProvider.INSTANCE.getWorkspace()
//							.getLocalProject((EObject) selectedValue);
//
//				}
//				if (localProject != null)
//					localProject.save();
//			}
//		});

		

	
		HBox box = new HBox();
		HBox.setHgrow(btnAddProject, Priority.ALWAYS);
		box.getChildren().add(btnAddProject);
//		box.getChildren().add(addElementButton);
//		box.getChildren().add(btnSaveProject);

		
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
//				Object selectedValue = selectedItem.getValue();

//				addElementButton.setDisable(true);

//				if (ESLocalProject.class.isInstance(selectedValue)) {
//					ESLocalProject localProject = (ESLocalProject) selectedValue;
//					btnSaveProject.setDisable(!localProject
//							.hasUnsavedChanges());
////					addElementButton.setDisable(false);
//				} else if (EObject.class.isInstance(selectedValue)) {
//					ESLocalProject localProject = ESWorkspaceProvider.INSTANCE
//							.getWorkspace().getLocalProject(
//									(EObject) selectedValue);
//					btnSaveProject.setDisable(!localProject
//							.hasUnsavedChanges());
//				}
				
				
			}
		});

		

		final TreeView<Object> remoteProjectsView = new TreeView<>();
		remoteProjectsView.setShowRoot(false);
		remoteProjectsView.setRoot(new EmfStoreRemoteProjectTreeItem(
				ESWorkspaceProvider.INSTANCE.getWorkspace()));
		remoteProjectsView
				.setCellFactory(new Callback<TreeView<Object>, TreeCell<Object>>() {

					@Override
					public TreeCell<Object> call(TreeView<Object> param) {

						return new ESRemoteProjectTreeCell(projectTreeItem);
					}
				});

		

		
		
		VBox vBox = new VBox();
		vBox.getChildren().add(box);
		vBox.getChildren().add(new Label("Local Projects"));
		vBox.getChildren().add(localProjectsView);
		vBox.getChildren().add(new Label("Remote Projects"));
		vBox.getChildren().add(remoteProjectsView);
		VBox.setMargin(remoteProjectsView, new Insets(5,5,5,5));
		VBox.setMargin(localProjectsView, new Insets(5,5,5,5));
		parent.setCenter(vBox);

	}
}
