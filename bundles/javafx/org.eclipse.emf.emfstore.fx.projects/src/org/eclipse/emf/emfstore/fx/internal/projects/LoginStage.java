package org.eclipse.emf.emfstore.fx.internal.projects;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginStage extends Stage {

	private String name;
	private String password;
	
	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public LoginStage(){
		initModality(Modality.WINDOW_MODAL);
		setTitle("Enter Login information");
		
		final TextField nameField = new TextField();
		HBox nameBox = HBoxBuilder.create().children(new Label("Name"),nameField).build();
		final TextField pwField = new TextField();
		HBox pwBox = HBoxBuilder.create().children(new Label("Password"),pwField).build();
		
		final Button buttonOK = new Button("OK");
		buttonOK.setMaxWidth(Double.MAX_VALUE);
		buttonOK.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				name = nameField.getText();
				password=pwField.getText();
				close();
			}
		});
		
		final Button buttonCancel = new Button("Cancel");
		buttonCancel.setMaxWidth(Double.MAX_VALUE);
		buttonCancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				close();
			}
		});
		HBox buttonBox = HBoxBuilder.create().children(buttonOK,buttonCancel).build();
		setScene(new Scene(VBoxBuilder.create().children(nameBox,pwBox,buttonBox)
				.build()));
	}
}
