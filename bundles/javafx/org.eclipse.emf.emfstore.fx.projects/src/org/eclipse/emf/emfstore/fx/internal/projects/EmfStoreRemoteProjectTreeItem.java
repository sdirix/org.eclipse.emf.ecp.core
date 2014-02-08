package org.eclipse.emf.emfstore.fx.internal.projects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import org.eclipse.emf.emfstore.client.ESRemoteProject;
import org.eclipse.emf.emfstore.client.ESServer;
import org.eclipse.emf.emfstore.client.ESUsersession;
import org.eclipse.emf.emfstore.client.ESWorkspace;
import org.eclipse.emf.emfstore.server.exceptions.ESException;

public class EmfStoreRemoteProjectTreeItem extends TreeItem<Object> {
	private ObservableList<TreeItem<Object>> children;

	public EmfStoreRemoteProjectTreeItem(Object root) {
		super(root);
		children = FXCollections.unmodifiableObservableList(super
				.getChildren());
		updateChildren();
	}

	private void updateChildren() {
		ObservableList<TreeItem<Object>> children = super.getChildren();
		children.clear();
		Object selectedValue = getValue();
		if(ESWorkspace.class.isInstance(selectedValue)){
			updateWorkspace((ESWorkspace) selectedValue);
		}
		else if(ESServer.class.isInstance(selectedValue)){
			ESServer server=(ESServer)selectedValue;
			try {
				ESUsersession session=server.getLastUsersession();
				if(session==null || !session.isLoggedIn())
					session = server.login("super", "super");
				updateServer(server,session);
			} catch (ESException e) {
				//TODO log
				//e.printStackTrace();
				
			}
		}
	}

	private void updateServer(ESServer selectedValue,ESUsersession session) throws ESException {
		ObservableList<TreeItem<Object>> children = super.getChildren();
		for(ESRemoteProject project:selectedValue.getRemoteProjects(session)){
			children.add(new EmfStoreRemoteProjectTreeItem(project));
		}
	}

	private void updateWorkspace(ESWorkspace workspace) {
		ObservableList<TreeItem<Object>> children = super.getChildren();
		for(ESServer server:workspace.getServers()){
			children.add(new EmfStoreRemoteProjectTreeItem(server));
		}
	}

	@Override
	public ObservableList<TreeItem<Object>> getChildren() {
		return children;
	}
}
