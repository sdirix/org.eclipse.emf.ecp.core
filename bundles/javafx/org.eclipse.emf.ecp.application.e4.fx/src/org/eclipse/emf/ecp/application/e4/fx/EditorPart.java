package org.eclipse.emf.ecp.application.e4.fx;

import javafx.scene.layout.BorderPane;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.view.model.fx.ECPFXView;
import org.eclipse.emf.ecp.view.model.fx.ECPFXViewRenderer;

public class EditorPart {
	private BorderPane parent;

	@Inject
	public EditorPart() {

	}

	@PostConstruct
	public void postConstruct(BorderPane parent) {
		this.parent = parent;
	}

	@Inject
	void setSelection(
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) EObject person) {
		if (person == null) {
			/* implementation not shown */
		} else {
			final ECPFXView ecpfxView = ECPFXViewRenderer.INSTANCE.render(
					person);

			parent.setCenter(ecpfxView.getFXNode());
		}
	}

	@Focus
	public void onFocus() {
		// TODO Your code here
	}

	

}