package org.eclipse.emf.ecp.view.editor.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.view.editor.controls.Helper;
import org.eclipse.emf.ecp.view.model.CompositeCollection;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.ViewFactory;

public class ControlGenerator {

    public static void addControls(ECPProject project, CompositeCollection compositeToFill,
            EClass datasegment, Set<EStructuralFeature> features) {
        
        EClass rootClass = Helper.getRootEClass(project);
        addControls(rootClass, compositeToFill, datasegment, features);
        
    }
    public static void addControls(EClass rootClass, CompositeCollection compositeToFill, EClass datasegment, Set<EStructuralFeature> features){
    	Map<EClass, EReference> childParentReferenceMap = new HashMap<EClass, EReference>();
    	Helper.getReferenceMap(rootClass, childParentReferenceMap);
        List<EReference> bottomUpPath = Helper.getReferencePath(datasegment,
                childParentReferenceMap);

        for (EStructuralFeature feature : features) {
            Control control = ViewFactory.eINSTANCE.createControl();
            // control.setData(datasegment);
            control.setMandatory(false);
            control.setName("Control " + feature.getName());
            control.setReadonly(false);
            control.setTargetFeature(feature);
            control.getPathToFeature().addAll(bottomUpPath);
            // add to the composite
            compositeToFill.getComposites().add(control);
        }
    }

}
