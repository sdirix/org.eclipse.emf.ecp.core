package org.eclipse.emf.emfstore.fx.internal.projects;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.emfstore.client.ESRemoteProject;
import org.eclipse.emf.emfstore.client.ESServer;
import org.eclipse.emf.emfstore.server.exceptions.ESException;

public final class ESRemoteProjectTreeCell extends TreeCell<Object> {

	private class CheckoutNameStage extends Stage {
		private String result;

		public CheckoutNameStage() {
			super();
			initModality(Modality.WINDOW_MODAL);
			setTitle("Enter Name for LocalProject");
			final TextField tf = new TextField();
			final Button button = new Button("Create");
			button.setMaxWidth(Double.MAX_VALUE);
			button.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					result = tf.getText();
					close();
				}
			});

			setScene(new Scene(VBoxBuilder.create().children(tf, button)
					.build()));
		}
	}

	private ContextMenu addMenu = new ContextMenu();

	public ESRemoteProjectTreeCell(final EmfStoreLocalTreeItem projectTreeItem) {
		
		ImageView image  = new ImageView(Activator.getContext().getBundle().getResource("icons/checkout.png").toExternalForm());
		
		MenuItem addMenuItem = new MenuItem();
		addMenuItem.setGraphic(HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(image,new Label("Checkout")).build());
		addMenu.getItems().add(addMenuItem);
		addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				ESRemoteProject remoteProject = (ESRemoteProject) getTreeItem()
						.getValue();
				CheckoutNameStage stage = new CheckoutNameStage();
				stage.showAndWait();
				if (stage.result == null)
					return;
				try {
					remoteProject.checkout(stage.result, remoteProject
							.getServer().getLastUsersession(),
							new NullProgressMonitor());
					projectTreeItem.updateChildren();
				} catch (ESException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void updateItem(Object item, boolean empty) {
		super.updateItem(item, empty);

		if (item != null) {

			Node result = null;
			if (ESRemoteProject.class.isInstance(item)) {
				Label label = new Label(((ESRemoteProject) item).getProjectName());
				ImageView image  = new ImageView(Activator.getContext().getBundle().getResource("icons/remoteProject.png").toExternalForm());
				result=HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(image,label).build();
				setContextMenu(addMenu);

			} else if (ESServer.class.isInstance(item)) {
				ESServer server = (ESServer) item;
				Label label = new Label(server.getName() + "("
						+ server.getURL() + ":" + server.getPort() + ")");
				ImageView image  = new ImageView(Activator.getContext().getBundle().getResource("icons/server.gif").toExternalForm());
				result=HBoxBuilder.create().alignment(Pos.CENTER_LEFT).children(image,label).build();
			}

			if (result != null)
				setGraphic(result);
		}
	}
}