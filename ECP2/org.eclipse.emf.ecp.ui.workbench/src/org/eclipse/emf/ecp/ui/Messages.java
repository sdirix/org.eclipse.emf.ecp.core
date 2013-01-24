package org.eclipse.emf.ecp.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.emf.ecp.ui.messages"; //$NON-NLS-1$
	
	public static String TreeView_Exception_ViewerNotCreated;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
