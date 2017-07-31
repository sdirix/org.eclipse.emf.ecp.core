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
package org.eclipse.emf.ecp.emf2web.graph.json.controller

import java.util.Collection
import java.util.LinkedList
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecp.emf2web.controller.GenerationController
import org.eclipse.emf.ecp.emf2web.controller.GenerationInfo
import org.eclipse.emf.ecp.emf2web.graph.json.generator.SiriusJsonGenerator

/**
 * @author Stefan Dirix
 *
 */
class SiriusJsonGenerationController implements GenerationController {
	
	override generate(Collection<? extends EObject> objects) {
		val result = new LinkedList<GenerationInfo>

		val modelGenerator = new SiriusJsonGenerator
		val schemaInfo = new GenerationInfo(GenerationInfo.OTHER_TYPE,null,null,"exportSirius.json",null)
		schemaInfo.generatedString = modelGenerator.generate(objects.get(0))
		result.add(schemaInfo)

		result
	}
	
}