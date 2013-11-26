package org.eclipse.emf.ecp.view.custom.ui.swt.example;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTRenderingHelper;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.custom.swt.ECPAbstractCustomControlSWT;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.VViewFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.eclipsesource.makeithappen.model.task.TaskPackage;

@SuppressWarnings("restriction")
public class MyCustomControl extends ECPAbstractCustomControlSWT {

	private Set<VDomainModelReference> features = new LinkedHashSet<VDomainModelReference>();
	private VFeaturePathDomainModelReference lastNameFeature;
	private VFeaturePathDomainModelReference genderFeature;


	
	public MyCustomControl() {
		super();
	}
	public Set<VDomainModelReference> getNeededDomainModelReferences() {
		if(features.isEmpty()){
			lastNameFeature = VViewFactory.eINSTANCE.createFeaturePathDomainModelReference();
			lastNameFeature.setDomainModelEFeature(TaskPackage.eINSTANCE.getUser_LastName());
			features.add(lastNameFeature);

			genderFeature = VViewFactory.eINSTANCE.createFeaturePathDomainModelReference();
			genderFeature.setDomainModelEFeature(TaskPackage.eINSTANCE.getUser_Gender());
			features.add(genderFeature);
		}
		return features;
	}

	@Override
	protected List<RenderingResultRow<Control>> createControl(Composite composite) {
		Composite parent=new Composite(composite, SWT.NONE);
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		
		Label userAttributes=new Label(parent, SWT.NONE);
		userAttributes.setText("User Attributes");
		userAttributes.setAlignment(SWT.CENTER);
		
		createControl(lastNameFeature, parent);
		createControl(genderFeature, parent);
		
		List<RenderingResultRow<Control>> result = new ArrayList<RenderingResultRow<Control>>();
		result.add(SWTRenderingHelper.INSTANCE.getResultRowFactory().createRenderingResultRow(parent));
		return result;
	}

	@Override
	protected void handleContentValidation(int severity, EStructuralFeature feature) {
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
