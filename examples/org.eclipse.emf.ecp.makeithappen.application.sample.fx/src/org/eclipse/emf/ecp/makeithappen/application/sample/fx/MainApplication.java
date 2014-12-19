package org.eclipse.emf.ecp.makeithappen.application.sample.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.eclipse.emf.ecp.makeithappen.model.task.TaskFactory;
import org.eclipse.emf.ecp.makeithappen.model.task.User;
import org.eclipse.emf.ecp.view.model.fx.ECPFXView;
import org.eclipse.emf.ecp.view.model.fx.ECPFXViewRenderer;
import org.eclipse.equinox.app.IApplicationContext;

public class MainApplication extends AbstractJFXApplication {

	private void fillPane(BorderPane pane) {
		final User submission = TaskFactory.eINSTANCE.createUser();
		final ECPFXView ecpfxView = ECPFXViewRenderer.INSTANCE.render(submission);
		pane.setCenter(ecpfxView.getFXNode());
	}

	protected void jfxStart(IApplicationContext applicationContext, Application jfxApplication, Stage primaryStage) {
		final BorderPane pane = new BorderPane();
		final Scene s = new Scene(pane);
		primaryStage.setScene(s);
		primaryStage.setWidth(800);
		primaryStage.setHeight(600);
		primaryStage.setTitle("Make it happen Demo");
		fillPane(pane);
		primaryStage.show();
	}
}
