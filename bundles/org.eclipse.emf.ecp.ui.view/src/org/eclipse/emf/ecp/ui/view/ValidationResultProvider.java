package org.eclipse.emf.ecp.ui.view;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;

public interface ValidationResultProvider {

    Map<EObject, Set<Diagnostic>> provideValidationResult();
    
}
