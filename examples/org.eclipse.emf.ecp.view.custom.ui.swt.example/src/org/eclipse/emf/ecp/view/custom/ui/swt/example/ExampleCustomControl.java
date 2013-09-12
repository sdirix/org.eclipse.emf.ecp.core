package org.eclipse.emf.ecp.view.custom.ui.swt.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT;
import org.eclipse.emf.ecp.ui.view.swt.SWTRenderingHelper;
import org.eclipse.emf.ecp.view.custom.model.ECPCustomControl;
import org.eclipse.emf.emfstore.bowling.BowlingPackage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class ExampleCustomControl extends ECPAbstractCustomControlSWT {

	private static Set<ECPCustomControlFeature> editableFeatures = createEditableFeatures();
	private static Set<ECPCustomControlFeature> referencedFeatures = createReferencedFeatures();
	private static ECPCustomControlFeature nameFeature;
	private static ECPCustomControlFeature eMailFeature;

	public ExampleCustomControl() {
		super(createFeatures());
	}

	private static Set<ECPCustomControlFeature> createFeatures() {
		HashSet<ECPCustomControlFeature> ret = new HashSet<ECPCustomControl.ECPCustomControlFeature>();
		ret.addAll(referencedFeatures);
		ret.addAll(editableFeatures);
		return ret;
	}

	private static Set<ECPCustomControlFeature> createReferencedFeatures() {
		return new HashSet<ECPCustomControl.ECPCustomControlFeature>();
	}

	private static Set<ECPCustomControlFeature> createEditableFeatures() {
		HashSet<ECPCustomControlFeature> ret = new HashSet<ECPCustomControl.ECPCustomControlFeature>();
		nameFeature = new ECPCustomControlFeature(null,
				BowlingPackage.eINSTANCE.getPlayer_Name(), true);
		ret.add(nameFeature);
		eMailFeature = new ECPCustomControlFeature(null,
				BowlingPackage.eINSTANCE.getPlayer_EMails(), true);
		ret.add(eMailFeature);

		return ret;
	}

	@Override
	protected List<RenderingResultRow<Control>> createControls(
			Composite composite) {
		final List<RenderingResultRow<Control>> result = new ArrayList<RenderingResultRow<Control>>();

		Label label = new Label(composite, SWT.NONE);
		label.setText("ExampleLabel");
		Composite nameComposite = createControl(nameFeature, composite);

		result.add(SWTRenderingHelper.INSTANCE.getResultRowFactory()
				.createRenderingResultRow(label, nameComposite));

		Composite emailComposite = createControl(eMailFeature, composite);

		result.add(SWTRenderingHelper.INSTANCE.getResultRowFactory()
				.createRenderingResultRow(emailComposite));

		return result;
	}

	@Override
	protected void handleContentValidation(int severity,
			EStructuralFeature feature) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void resetContentValidation() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void disposeCustomControl() {
		// TODO Auto-generated method stub

	}

}
