package org.eclipse.emf.ecp.controls.internal.fx;

import javafx.scene.Node;
import javafx.scene.control.TextField;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.fx.core.databinding.JFXBeanProperties;

@SuppressWarnings("restriction")
//TODO no api
public class ECPFXText extends ECPFXControl {

	private TextField text;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleValidation(Diagnostic diagnostic) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetValidation() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEditable(boolean isEditable) {
		// TODO Auto-generated method stub

	}

	@Override
	public Node render() {
		text = new TextField();
		bindControls();
		return text;
	}

	private void bindControls() {
		IObservableValue targetObservableValue = JFXBeanProperties
				.value("text").observe(text);
		Setting setting = getDomainModelReference().getIterator().next();
		getDataBindingContext().bindValue(
				targetObservableValue,
				EMFObservables.observeValue(setting.getEObject(),
						setting.getEStructuralFeature()));
	}

	

}
