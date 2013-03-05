package org.eclipse.emf.ecp.edit.internal.swt.table;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.editor.util.ECPApplicableTester;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public class XmlDateCellEditorTester implements ECPApplicableTester {

	public XmlDateCellEditorTester() {
		// TODO Auto-generated constructor stub
	}

	public int isApplicable(IItemPropertyDescriptor itemPropertyDescriptor, EObject eObject) {
		EStructuralFeature feature=(EStructuralFeature)itemPropertyDescriptor.getFeature(null);
		if (EAttribute.class.isInstance(feature)) {
			Class<?> instanceClass = ((EAttribute) feature).getEAttributeType().getInstanceClass();
			if(XMLGregorianCalendar.class.isAssignableFrom(instanceClass)){
				return 1;
			}
		}
		return NOT_APPLICABLE;
	}

}
