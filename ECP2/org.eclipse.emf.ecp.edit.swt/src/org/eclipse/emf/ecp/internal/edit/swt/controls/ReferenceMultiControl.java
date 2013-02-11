package org.eclipse.emf.ecp.internal.edit.swt.controls;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.edit.swt.actions.AddReferenceAction;
import org.eclipse.emf.ecp.edit.swt.actions.ECPSWTAction;
import org.eclipse.emf.ecp.edit.swt.actions.NewReferenceAction;
import org.eclipse.emf.ecp.internal.edit.StaticApplicableTester;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public class ReferenceMultiControl extends MultiControl {

	public ReferenceMultiControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, EditModelElementContext modelElementContext,boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext,embedded);
	}

	@Override
	protected ECPSWTAction[] instantiateActions() {
		ECPSWTAction[] actions = new ECPSWTAction[2];
			actions[0]=new AddReferenceAction(getModelElementContext(), getItemPropertyDescriptor(), getStructuralFeature());
			actions[1]=new NewReferenceAction(getModelElementContext(), getItemPropertyDescriptor(), getStructuralFeature());
			return actions;
	}
	@Override
	protected int getTesterPriority(StaticApplicableTester tester, IItemPropertyDescriptor itemPropertyDescriptor,
		EObject eObject) {
		return ReferenceMultiControlTester.getTesterPriority(tester, itemPropertyDescriptor, eObject);
	}
}
