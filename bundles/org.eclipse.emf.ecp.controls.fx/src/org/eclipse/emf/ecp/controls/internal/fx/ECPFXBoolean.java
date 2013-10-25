package org.eclipse.emf.ecp.controls.internal.fx;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.fx.core.databinding.JFXBeanProperties;

@SuppressWarnings("restriction")
//TODO no api
public class ECPFXBoolean extends ECPFXControl {

	private CheckBox checkBox;

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
		checkBox = new CheckBox();
		bindControls();
		return checkBox;
	}

	private void bindControls() {
		IObservableValue targetObservableValue = JFXBeanProperties
				.value("selected").observe(checkBox);
		Setting setting = getDomainModelReference().getIterator().next();
		getDataBindingContext().bindValue(
				targetObservableValue,
				EMFObservables.observeValue(setting.getEObject(),
						setting.getEStructuralFeature()));
	}
}
