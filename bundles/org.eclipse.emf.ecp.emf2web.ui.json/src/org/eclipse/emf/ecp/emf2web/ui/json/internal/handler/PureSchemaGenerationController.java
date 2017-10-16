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
package org.eclipse.emf.ecp.emf2web.ui.json.internal.handler;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.emf2web.controller.GenerationController;
import org.eclipse.emf.ecp.emf2web.controller.GenerationInfo;
import org.eclipse.emf.ecp.emf2web.json.generator.EcoreJsonGenerator;
import org.eclipse.emf.ecp.emf2web.json.generator.seed.SeedWrapper;

/**
 * @author stefan
 *
 */
public class PureSchemaGenerationController implements GenerationController {
	@Override
	public List<GenerationInfo> generate(Collection<? extends EObject> objects) {
		final List<GenerationInfo> result = new LinkedList<GenerationInfo>();

		final EcoreJsonGenerator modelGenerator = new EcoreJsonGenerator();

		for (final EObject object : objects) {
			final EClass eClass = (EClass) object;
			final String schemaIdentifier = eClass.getName();

			final String schemaFile = modelGenerator.generate(eClass);
			final GenerationInfo schemaInfo = new GenerationInfo(GenerationInfo.MODEL_TYPE, eClass, null,
				schemaIdentifier + "Model.json", new SeedWrapper());
			schemaInfo.setGeneratedString(schemaFile);
			result.add(schemaInfo);
		}
		return result;
	}
}
