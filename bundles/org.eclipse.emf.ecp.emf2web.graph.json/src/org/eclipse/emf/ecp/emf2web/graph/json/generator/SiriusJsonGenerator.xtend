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
import com.google.gson.JsonElement
import org.eclipse.sirius.viewpoint.description.Group
import org.eclipse.sirius.viewpoint.description.Viewpoint
import org.eclipse.sirius.diagram.description.DiagramDescription
import org.eclipse.sirius.diagram.description.Layer
import org.eclipse.sirius.diagram.description.ContainerMapping
import org.eclipse.sirius.diagram.description.style.FlatContainerStyleDescription
import org.eclipse.sirius.viewpoint.description.FixedColor
import org.eclipse.emf.common.util.Enumerator

/**
 * @author Stefan Dirix
 *
 */
class SiriusJsonGenerator extends JsonGenerator {
	
	override createJsonElement(EObject object) {
		createJsonGraphElement(object)
	}
	
	private def dispatch JsonElement createJsonGraphElement(EObject object) {
		throw new UnsupportedOperationException(
			"Unexpected Type: " + object.eClass.name)
	}
	
	private def dispatch JsonElement createJsonGraphElement(Group group) {
		createJsonGraphElement(group.ownedViewpoints.get(0))
	}
	
	private def dispatch JsonElement createJsonGraphElement(Viewpoint viewpoint) {
		createJsonGraphElement(viewpoint.ownedRepresentations.get(0))
	}	
	
	private def dispatch JsonElement createJsonGraphElement(DiagramDescription description) {
		createJsonGraphElement(description.defaultLayer)
	}
	
	private def dispatch JsonElement createJsonGraphElement(Layer layer) {
		val jsonObject = new JsonObject()
		jsonObject.with("nodes",layer.containerMappings)
		jsonObject
	}
	
	private def dispatch JsonElement createJsonGraphElement(ContainerMapping containerMapping) {
		val jsonObject = new JsonObject()
		val style = containerMapping.style
		jsonObject.with("id", containerMapping.name)
		jsonObject.with("gradient", createGradientObject(containerMapping.style))
		jsonObject.with("textcolor", style.labelColor)
		jsonObject.with("shape", "rectangle")
		
	}
	
	private def dispatch JsonElement createGradientObject(FlatContainerStyleDescription description) {
		val jsonObject = new JsonObject()
		jsonObject.with("colorA", description.backgroundColor)
		jsonObject.with("colorB", description.foregroundColor)
		jsonObject.with("direction", description.backgroundStyle)
		jsonObject
	}
	
	private def dispatch JsonElement createGradientObject(EObject object) {
		throw new UnsupportedOperationException(
			"Unexpected Type: " + object.eClass.name)
	}
	
	protected def dispatch with(JsonObject jsonObject, String propertyName, FixedColor color) {
		jsonObject.with(propertyName, "#" + String.format("%02X", color.red) + String.format("%02X", color.green) + String.format("%02X", color.blue))
	}
	
	protected def dispatch with(JsonObject jsonObject, String propertyName, Enumerator enumerator) {
		jsonObject.with(propertyName, enumerator.literal)
	}

}