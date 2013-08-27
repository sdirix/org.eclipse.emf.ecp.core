/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.validation;

import org.eclipse.emf.ecp.view.model.VDiagnostic;

/**
 * @author Eugen Neufeld
 * 
 */
public class VDiagnosticHelper {

	public static boolean isEqual(VDiagnostic vDiagnostic1, VDiagnostic vDiagnostic2) {
		if (vDiagnostic1.getHighestSeverity() != vDiagnostic2.getHighestSeverity()) {
			return false;
		}
		if (!vDiagnostic1.getMessage().equals(vDiagnostic2.getMessage())) {
			return false;
		}
		// if (vDiagnostic1.getDiagnostics().size() != vDiagnostic2.getDiagnostics().size()) {
		// return false;
		// }
		// for (int i = 0; i < vDiagnostic1.getDiagnostics().size(); i++) {
		// final Diagnostic diagnostic1 = (Diagnostic) vDiagnostic1.getDiagnostics().get(i);
		// final Diagnostic diagnostic2 = (Diagnostic) vDiagnostic2.getDiagnostics().get(i);
		// if (diagnostic1 != diagnostic2) {
		// return false;
		// }
		// }
		return true;
	}
}
