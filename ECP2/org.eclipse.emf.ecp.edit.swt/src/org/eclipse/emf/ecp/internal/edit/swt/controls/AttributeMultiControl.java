package org.eclipse.emf.ecp.internal.edit.swt.controls;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.edit.swt.actions.AddAttributeAction;
import org.eclipse.emf.ecp.edit.swt.actions.ECPSWTAction;
import org.eclipse.emf.ecp.editor.util.StaticApplicableTester;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public class AttributeMultiControl extends MultiControl {

	public AttributeMultiControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, EditModelElementContext modelElementContext,boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext,embedded);

	}
	@Override
	protected ECPSWTAction[] instantiateActions() {
		ECPSWTAction[] actions = new ECPSWTAction[1];
		actions[0] = new AddAttributeAction(getModelElementContext(), getItemPropertyDescriptor(),
			getStructuralFeature());
		return actions;
	}
	@Override
	protected int getTesterPriority(StaticApplicableTester tester, IItemPropertyDescriptor itemPropertyDescriptor,
		EObject eObject) {
		return AttributeMultiControlTester.getTesterPriority(tester, itemPropertyDescriptor, eObject);
	}

	

}
