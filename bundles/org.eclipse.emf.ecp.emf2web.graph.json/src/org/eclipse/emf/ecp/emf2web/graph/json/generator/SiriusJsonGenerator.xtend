/*******************************************************************************
 * Copyright (c) 2011-2017 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * stefan - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.emf2web.graph.json.generator

import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecp.emf2web.json.generator.JsonGenerator
import com.google.gson.JsonObject

/**
 * @author Stefan Dirix
 *
 */
class SiriusJsonGenerator extends JsonGenerator {
	
	override createJsonElement(EObject object) {
		new JsonObject()
	}

}