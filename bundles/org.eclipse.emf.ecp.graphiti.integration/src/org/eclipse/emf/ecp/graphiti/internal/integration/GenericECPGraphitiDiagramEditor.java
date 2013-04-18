package org.eclipse.emf.ecp.graphiti.internal.integration;

import org.eclipse.emf.ecp.graphiti.ECPGraphitiDiagramEditor;

public class GenericECPGraphitiDiagramEditor extends ECPGraphitiDiagramEditor {
	public static final String EDITOR_ID="org.eclipse.emf.ecp.graphiti.editor";
	@Override
	public boolean isDirty() {
		return false;
	}

}
