package org.eclipse.emf.ecp.internal.ui.view.renderer;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;

public interface ValidationOccurredListener {

     void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects);
}
