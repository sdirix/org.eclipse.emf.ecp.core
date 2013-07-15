package org.eclipse.emf.ecp.internal.ui.view.renderer;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;

/**
 * Convenience adapter implementation for a {@link RenderingResultDelegator} that does nothing. 
 * 
 * 
 * @author emueller
 *
 */
public class RenderingResultDelegatorAdapter implements RenderingResultDelegator{

    @Override
    public void enable(boolean shouldEnable) { }
    
    @Override
    public void show(boolean shouldShow) { }

    @Override
    public void layout() { }

    @Override
    public void cleanup() { }
    
    @Override
    public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) { }
}
