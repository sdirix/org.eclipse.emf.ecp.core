package org.eclipse.emf.ecp.internal.ui.view.renderer;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;

import java.util.Map;
import java.util.Set;

/**
 * Convenience adapter implementation for a {@link RenderingResultDelegator} that does nothing.
 * 
 * 
 * @author emueller
 * 
 */
public class RenderingResultDelegatorAdapter implements RenderingResultDelegator {

	public void enable(boolean shouldEnable) {
	}

	public void show(boolean shouldShow) {
	}

	public void layout() {
	}

	public void cleanup() {
	}

	public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {
	}
}
