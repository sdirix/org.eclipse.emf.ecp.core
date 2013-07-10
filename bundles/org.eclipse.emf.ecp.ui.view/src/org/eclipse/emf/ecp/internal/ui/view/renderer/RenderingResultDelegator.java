package org.eclipse.emf.ecp.internal.ui.view.renderer;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;


/**
 * 
 * @author emueller
 *
 * @param <CONTROL>
 * 			the type of the actual control
 */
public interface RenderingResultDelegator {

	void enable(boolean shouldEnable);
	
	void show(boolean shouldShow);

	void layout();

	void cleanup();
	
	void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects);


}
