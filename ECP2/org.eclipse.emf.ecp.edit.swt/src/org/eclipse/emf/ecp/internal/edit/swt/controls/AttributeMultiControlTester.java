package org.eclipse.emf.ecp.internal.edit.swt.controls;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ControlDescription;
import org.eclipse.emf.ecp.edit.ControlFactory;
import org.eclipse.emf.ecp.editor.util.ECPApplicableTester;
import org.eclipse.emf.ecp.internal.edit.StaticApplicableTester;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.resource.ColorDescriptor;

public class AttributeMultiControlTester implements ECPApplicableTester {

	public AttributeMultiControlTester() {
	}

	@Override
	public int isApplicable(IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		int bestPriority=NOT_APPLICABLE;
		for(ControlDescription description:ControlFactory.INSTANCE.getControlDescriptors()){
			if(StaticApplicableTester.class.isInstance(description.getTester())){
				StaticApplicableTester tester=(StaticApplicableTester) description.getTester();
				int priority=getTesterPriority(tester,itemPropertyDescriptor,eObject);
				if(bestPriority<priority)
					bestPriority=priority;
			}
			else
				continue;
		}
		return bestPriority;
	}

	public static int getTesterPriority(StaticApplicableTester tester,IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		if(!itemPropertyDescriptor.isMany(eObject))
			return NOT_APPLICABLE;
		EStructuralFeature feature=(EStructuralFeature) itemPropertyDescriptor.getFeature(eObject);
		
		if(EAttribute.class.isInstance(feature)){
			Class<?> instanceClass = ((EAttribute)feature).getEAttributeType().getInstanceClass();
			if (instanceClass.isPrimitive()) {
				try {
					Class<?> primitive = (Class<?>) tester.getSupportedClassType().getField("TYPE").get(null);
					if (!primitive.equals(instanceClass)) {
						return NOT_APPLICABLE;
					}
	
				} catch (IllegalArgumentException e) {
					return NOT_APPLICABLE;
				} catch (SecurityException e) {
					return NOT_APPLICABLE;
				} catch (IllegalAccessException e) {
					return NOT_APPLICABLE;
				} catch (NoSuchFieldException e) {
					return NOT_APPLICABLE;
				}
			}
			else if (!tester.getSupportedClassType().isAssignableFrom(instanceClass)) {
				return NOT_APPLICABLE;
			}
		}
		else if(EReference.class.isInstance(feature)){
				return NOT_APPLICABLE;
		}
		if (tester.getSupportedEObject().isInstance(eObject)
			&& (tester.getSupportedFeature() == null || eObject.eClass()
				.getEStructuralFeature(tester.getSupportedFeature()).equals(feature))) {
			return tester.getPriority();
		}
		return NOT_APPLICABLE;
	}

}
