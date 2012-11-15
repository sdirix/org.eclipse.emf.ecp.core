package org.eclipse.emf.ecp.application3x;

import org.eclipse.emf.ecp.ui.views.ModelExplorerView;
import org.eclipse.emf.ecp.ui.views.ModelRepositoriesView;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	  public void createInitialLayout(IPageLayout layout)
	  {
	    String editorArea = layout.getEditorArea();
	    layout.addView(ModelExplorerView.ID, IPageLayout.LEFT, 0.25f, editorArea);
	    layout.addView(ModelRepositoriesView.ID, IPageLayout.BOTTOM, 0.75f, editorArea);
	  }
}
