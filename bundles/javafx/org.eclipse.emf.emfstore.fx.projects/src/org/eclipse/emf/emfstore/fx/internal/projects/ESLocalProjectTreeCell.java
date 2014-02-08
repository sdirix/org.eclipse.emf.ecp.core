package org.eclipse.emf.emfstore.fx.internal.projects;

import java.net.URL;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Pane;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.util.fx.EMFUtil;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.ESUsersession;
import org.eclipse.emf.emfstore.client.ESWorkspaceProvider;
import org.eclipse.emf.emfstore.server.exceptions.ESException;

public final class ESLocalProjectTreeCell extends TreeCell<Object> {
	/**
	 * 
	 */
	private final ProjectsView projectsView;
	private EObject currentItem;
	private AdapterImpl adapter = new AdapterImpl() {
		@Override
		public void notifyChanged(Notification msg) {
			Node result = update(msg.getNotifier());
			if (result != null)
				setGraphic(result);
		}
	};
	private ContextMenu localProjectMenu = new ContextMenu();
	private ContextMenu sharedProjectMenu = new ContextMenu();
	private ContextMenu eObjectMenu = new ContextMenu();
	private EmfStoreLocalTreeItem projectTreeItem;

	/**
	 * @param projectTreeItem
	 */
	public ESLocalProjectTreeCell(ProjectsView projectsView,EmfStoreLocalTreeItem projectTreeItem) {
		
		this.projectsView = projectsView;
		this.projectTreeItem = projectTreeItem;
		setupRemoteProjectContextMenu();
	}

	private void setupRemoteProjectContextMenu() {
		MenuItem commitItem = new MenuItem();
		ImageView imgCommit  = new ImageView(Activator.getContext().getBundle().getResource("icons/commit.png").toExternalForm());
		commitItem.setGraphic(HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(imgCommit,new Label("Commit")).build());
		commitItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				ESLocalProject localProject = (ESLocalProject) getTreeItem()
						.getValue();
				try {
					localProject.getRemoteProject().getServer().login("super", "super");
					localProject.commit(new NullProgressMonitor());
					projectTreeItem.updateChildren();
				} catch (ESException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
		MenuItem updateItem = new MenuItem();
		ImageView imgUpdate  = new ImageView(Activator.getContext().getBundle().getResource("icons/update.png").toExternalForm());
		updateItem.setGraphic(HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(imgUpdate,new Label("Update")).build());
		updateItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				ESLocalProject localProject = (ESLocalProject) getTreeItem()
						.getValue();
				//TODO select server
				ESUsersession session=ESWorkspaceProvider.INSTANCE.getWorkspace().getServers().get(0).getLastUsersession();
				try {
					session.refresh();
					localProject.update(new NullProgressMonitor());
					projectTreeItem.updateChildren();
				} catch (ESException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		MenuItem shareItem = new MenuItem();
		ImageView imgShare  = new ImageView(Activator.getContext().getBundle().getResource("icons/share.png").toExternalForm());
		shareItem.setGraphic(HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(imgShare,new Label("Share")).build());
		shareItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				ESLocalProject localProject = (ESLocalProject) getTreeItem()
						.getValue();
				//TODO select server
				ESUsersession session=ESWorkspaceProvider.INSTANCE.getWorkspace().getServers().get(0).getLastUsersession();
				try {
					session.refresh();
					localProject.shareProject(session, new NullProgressMonitor());
					projectTreeItem.updateChildren();
				} catch (ESException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		MenuItem addElement = new MenuItem();
		ImageView imgAdd  = new ImageView(Activator.getContext().getBundle().getResource("icons/add.png").toExternalForm());
		addElement.setGraphic(HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(imgAdd,new Label("Add Element")).build());
		
//		ImageView imgShare  = new ImageView(Activator.getContext().getBundle().getResource("icons/share.png").toExternalForm());
//		shareItem.setGraphic(HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(imgCommit,new Label("Share")).build());
		addElement.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				ESLocalProject localProject = (ESLocalProject) getTreeItem()
						.getValue();

					// EObject result = TaskFactory.eINSTANCE.createUser();
					CreateEObjectStage stage = new CreateEObjectStage();

					stage.showAndWait();

					EObject result = stage.getResult();
					if (result == null)
						return;
					localProject.getModelElements().add(result);
					projectTreeItem.updateChildren();
				
			}
		});
		
		final MenuItem saveItem = new MenuItem();
		ImageView imgSave  = new ImageView(Activator.getContext().getBundle().getResource("icons/save.png").toExternalForm());
		saveItem.setGraphic(HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(imgSave,new Label("Save")).build());
		saveItem.setDisable(true);
		saveItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Object value=getTreeItem().getValue();
				
				ESLocalProject localProject = null;
				if(ESLocalProject.class.isInstance(value)){
					localProject=(ESLocalProject)value;
				}

				else if (EObject.class.isInstance(value)) {
					localProject = ESWorkspaceProvider.INSTANCE.getWorkspace()
							.getLocalProject((EObject) value);

				}
				if (localProject != null)
					localProject.save();
				saveItem.setDisable(true);
				
			}
		});
		
		
		treeItemProperty().addListener(new ChangeListener<TreeItem<Object>>() {

			@Override
			public void changed(
					ObservableValue<? extends TreeItem<Object>> observable,
					TreeItem<Object> oldValue, TreeItem<Object> newValue) {
				saveItem.setDisable(true);
				if(newValue==null)
					return;
				Object value=newValue.getValue();
				ESLocalProject localProject=null;
				if(EObject.class.isInstance(value)){
					localProject=ESWorkspaceProvider.INSTANCE.getWorkspace().getLocalProject((EObject) value);
				}
				else if(ESLocalProject.class.isInstance(value))
					localProject=(ESLocalProject) value;
				
				if(localProject!=null && localProject.hasUnsavedChanges()){
					saveItem.setDisable(false);
				}
			}
			
		});
		
		
		sharedProjectMenu.getItems().add(saveItem);
		sharedProjectMenu.getItems().add(commitItem);
		sharedProjectMenu.getItems().add(updateItem);
		
		sharedProjectMenu.getItems().add(addElement);
		localProjectMenu.getItems().add(saveItem);
		localProjectMenu.getItems().add(shareItem);
		localProjectMenu.getItems().add(addElement);
		
		eObjectMenu.getItems().add(saveItem);
	}

	

	@Override
	public void updateItem(Object item, boolean empty) {
		super.updateItem(item, empty);
		update(item);

		if (item != null) {

			Node result = null;
			if (ESLocalProject.class.isInstance(item)){
				ESLocalProject localProject=(ESLocalProject) item;
				
				Node label = createLabel(localProject
						.getProjectName()+(localProject.isShared()?(" ["+localProject.getBaseVersion().getBranch()+" "+localProject.getBaseVersion().getIdentifier()+"]"):""));
				ImageView image  = new ImageView(Activator.getContext().getBundle().getResource("icons/localProject.gif").toExternalForm());
				result=HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(image,label).build();
				if(localProject.isShared())
					setContextMenu(sharedProjectMenu);
				else
					setContextMenu(localProjectMenu);
			}
			
			if (EObject.class.isInstance(item)) {
				if (currentItem != item) {
					updatedAdapter((EObject) item);
				}
				result = update(item);
				
				
				MenuItem childrenMenu = EMFUtil.getCreateChildrenMenu(currentItem);
				
				if(childrenMenu!=null){
					eObjectMenu.getItems().add(childrenMenu);
				}
				setContextMenu(eObjectMenu);
			}
			if (result != null)
				setGraphic(result);
		}
	}

	private Node createLabel(String value) {
		return new Label(value);
	}

	private Node update(Object item) {
		IItemLabelProvider labelProvider = (IItemLabelProvider) this.projectsView.adapterFactory
				.adapt(item, IItemLabelProvider.class);

		if (labelProvider != null) {
			String value = labelProvider.getText(item);
			Node label = createLabel(value);
			Node image = graphicFromObject(labelProvider
					.getImage(item));

			Node result = label;
			if (image != null) {
				HBox hBox = new HBox();
				hBox.alignmentProperty().set(
						Pos.CENTER_LEFT);
				hBox.getChildren().add(image);
				hBox.getChildren().add(label);
				result = hBox;
			}
			return result;
		}
		return null;
	}

	private Node graphicFromObject(Object object) {
		if (object instanceof URL) {
			return new ImageView(((URL) object)
					.toExternalForm());
		} else if (object instanceof ComposedImage) {
			Pane pane = new Pane();

			for (Object image : ((ComposedImage) object)
					.getImages()) {
				if (image instanceof URL) {
					ImageView imageView = new ImageView(
							((URL) image)
									.toExternalForm());
					pane.getChildren().add(imageView);
				}
			}

			return pane;
		}
		return null;
	}

	private void updatedAdapter(EObject item) {
		// remove the adapter if attached
		if (currentItem instanceof Notifier)
			((Notifier) currentItem).eAdapters()
					.remove(adapter);

		// update the current item
		currentItem = item;

		// attach the adapter to the new item
		if (currentItem instanceof Notifier)
			((Notifier) currentItem).eAdapters().add(
					adapter);
	}
}