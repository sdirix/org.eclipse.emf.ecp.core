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
import org.eclipse.emf.emfstore.client.ESWorkspace;
import org.eclipse.emf.emfstore.server.exceptions.ESException;

public final class ESRemoteProjectTreeCell extends TreeCell<Object> {

	private class LoginHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent t) {
			ESServer server = (ESServer) getTreeItem().getValue();
			LoginStage stage = new LoginStage();
			stage.showAndWait();
			if (stage.getPassword() == null)
				return;
			try {
				server.login(stage.getName(), stage.getPassword());
			} catch (ESException e) {
				e.printStackTrace();
			}
		}
	}

	private class LogoutHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent t) {
			ESServer server = (ESServer) getTreeItem().getValue();

			try {
				server.getLastUsersession().logout();
			} catch (ESException e) {
				e.printStackTrace();
			}
		}
	}

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

	private ContextMenu remoteProjectMenu = new ContextMenu();
	private ContextMenu workspaceMenu = new ContextMenu();

	// private ContextMenu serverMenu = new ContextMenu();

	public ESRemoteProjectTreeCell() {

		{
			MenuItem addMenuItem = new MenuItem();
			ImageView image = new ImageView(Activator.getContext().getBundle()
					.getResource("icons/checkout.png").toExternalForm());
			addMenuItem.setGraphic(HBoxBuilder.create()
					.alignment(Pos.CENTER_LEFT)
					.children(image, new Label("Checkout")).build());
			remoteProjectMenu.getItems().add(addMenuItem);
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
					} catch (ESException e) {
						e.printStackTrace();
					}
				}
			});
		}

	}

	@Override
	public void updateSelected(boolean arg0) {
		super.updateSelected(arg0);
		Object item = getItem();
		if (ESRemoteProject.class.isInstance(item)) {
			setContextMenu(remoteProjectMenu);

		} else if (ESServer.class.isInstance(item)) {
			ESServer server = (ESServer) item;

			ContextMenu serverMenu = new ContextMenu();
			if (!server.getLastUsersession().isLoggedIn()) {
				MenuItem addMenuItem = new MenuItem();
				// ImageView image = new
				// ImageView(Activator.getContext().getBundle().getResource("icons/checkout.png").toExternalForm());
				addMenuItem.setGraphic(HBoxBuilder.create()
						.alignment(Pos.CENTER_LEFT)
						.children(new Label("Login")).build());
				serverMenu.getItems().add(addMenuItem);
				addMenuItem.setOnAction(new LoginHandler());
			}
			if (server.getLastUsersession().isLoggedIn()) {
				MenuItem addMenuItem = new MenuItem();
				// ImageView image = new
				// ImageView(Activator.getContext().getBundle().getResource("icons/checkout.png").toExternalForm());
				addMenuItem.setGraphic(HBoxBuilder.create()
						.alignment(Pos.CENTER_LEFT)
						.children(new Label("Logout")).build());
				serverMenu.getItems().add(addMenuItem);
				addMenuItem.setOnAction(new LogoutHandler());
			}

			setContextMenu(serverMenu);
		}
	}

	@Override
	public void updateItem(Object item, boolean empty) {
		super.updateItem(item, empty);

		if (item != null) {

			Node result = null;
			if (ESWorkspace.class.isInstance(item)) {
				Label label = new Label("Known Server");
				// ImageView image = new
				// ImageView(Activator.getContext().getBundle().getResource("icons/remoteProject.png").toExternalForm());
				result = HBoxBuilder.create().alignment(Pos.CENTER_LEFT)
						.children(label).build();

			} else if (ESRemoteProject.class.isInstance(item)) {
				Label label = new Label(
						((ESRemoteProject) item).getProjectName());
				ImageView image = new ImageView(Activator.getContext()
						.getBundle().getResource("icons/remoteProject.png")
						.toExternalForm());
				result = HBoxBuilder.create().alignment(Pos.CENTER_LEFT)
						.children(image, label).build();

			} else if (ESServer.class.isInstance(item)) {
				ESServer server = (ESServer) item;
				Label label = new Label(server.getName() + "("
						+ server.getURL() + ":" + server.getPort() + ")");
				ImageView image = new ImageView(Activator.getContext()
						.getBundle().getResource("icons/server.gif")
						.toExternalForm());
				result = HBoxBuilder.create().alignment(Pos.CENTER_LEFT)
						.children(image, label).build();

			}

			if (result != null)
				setGraphic(result);
		}
	}
}