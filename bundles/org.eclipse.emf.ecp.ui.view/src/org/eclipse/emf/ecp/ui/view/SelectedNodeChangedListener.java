package org.eclipse.emf.ecp.ui.view;

import org.eclipse.emf.ecp.view.model.Renderable;

public interface SelectedNodeChangedListener {

    <T extends Renderable> void selectionChanged(T selectedRenderable);
    
}
