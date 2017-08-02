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
import org.eclipse.sirius.diagram.BackgroundStyle
import org.eclipse.sirius.diagram.LineStyle
import org.eclipse.sirius.diagram.description.style.ContainerStyleDescription
import org.eclipse.sirius.viewpoint.LabelAlignment
import org.eclipse.sirius.viewpoint.FontFormat
import org.eclipse.sirius.diagram.description.NodeMapping
import org.eclipse.sirius.diagram.description.style.NodeStyleDescription
import org.eclipse.sirius.diagram.description.style.RoundedCornerStyleDescription
import org.eclipse.sirius.viewpoint.description.style.BasicLabelStyleDescription
import org.eclipse.sirius.viewpoint.description.style.LabelStyleDescription
import org.eclipse.sirius.diagram.ContainerLayout
import org.eclipse.sirius.diagram.description.EdgeMapping
import org.eclipse.sirius.diagram.EdgeArrows
import java.util.LinkedList
import org.eclipse.sirius.diagram.EdgeRouting
import org.eclipse.sirius.diagram.description.style.BeginLabelStyleDescription

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
		val nodes = new LinkedList
		val edges = new LinkedList
		val iterator = description.eAllContents
		while(iterator.hasNext){
		    val next = iterator.next
			switch next{
				ContainerMapping: nodes.add(next)
				NodeMapping: nodes.add(next)
				EdgeMapping: edges.add(next)
			}
		}
		val jsonObject = new JsonObject()
		jsonObject.with("id", "Diagram")
		jsonObject.with("nodes", nodes)
		jsonObject.with("edges", edges)		
	}
	
	private def dispatch JsonElement createJsonGraphElement(ContainerMapping containerMapping) {
		val jsonObject = new JsonObject()
		val style = containerMapping.style
		jsonObject.with("id", containerMapping.name)
		jsonObject.with("domainClass", containerMapping.domainClass)
		jsonObject.with("shapeStyle", createShapeStyleObject(style))
		jsonObject.with("nodesStyle", containerMapping.childrenPresentation)
		val nodes = new LinkedList
		nodes.addAll(containerMapping.allNodeMappings.map[nm|nm.name])
		nodes.addAll(containerMapping.allContainerMappings.map[cm|cm.name])
		jsonObject.withArray("nodes", nodes)
		if(!containerMapping.allBorderedNodeMappings.empty){
			jsonObject.withArray("borderNodes", containerMapping.allBorderedNodeMappings.map[bnm|bnm.name])
		}
		jsonObject
	}
	
	private def dispatch JsonElement createJsonGraphElement(NodeMapping nodeMapping) {
		val jsonObject = new JsonObject()
		val style = nodeMapping.style
		jsonObject.with("id", nodeMapping.name)
		jsonObject.with("domainClass", nodeMapping.domainClass)
		jsonObject.with("shapeStyle", createShapeStyleObject(style))
	}
	
	private def dispatch JsonElement createJsonGraphElement(EdgeMapping edgeMapping) {
		val jsonObject = new JsonObject()
		val style = edgeMapping.style
		jsonObject.with("id", edgeMapping.name)
		if(edgeMapping.domainClass != null){
			jsonObject.with("domainClass", edgeMapping.domainClass)			
		}
		jsonObject.withArray("sourceNodes", edgeMapping.sourceMapping.map[source|source.name])
		jsonObject.withArray("targetNodes", edgeMapping.targetMapping.map[target|target.name])
		jsonObject.with("lineStyle", style.lineStyle)
		jsonObject.with("lineColor", style.strokeColor)
		jsonObject.with("routingStyle",style.routingStyle)
		jsonObject.with("sourceDecoration", style.sourceArrow)
		jsonObject.with("targetDecoration", style.targetArrow)
		if(style.beginLabelStyleDescription != null){
			jsonObject.with("beginLabelStyle",createLabelStyleObject(style.beginLabelStyleDescription))
		}
		if(style.centerLabelStyleDescription != null){
			jsonObject.with("centerLabelStyle",createLabelStyleObject(style.centerLabelStyleDescription))
		}
		if(style.endLabelStyleDescription != null){
			jsonObject.with("endLabelStyle",createLabelStyleObject(style.endLabelStyleDescription))
		}
		jsonObject
	}
	
	private def JsonElement createShapeStyleObject(ContainerStyleDescription style){
		val jsonObject = new JsonObject()
		jsonObject.with("shape", "rectangle")
		jsonObject.with("backgroundColor", createBackgroundColorObject(style))
		jsonObject.with("cornerArc", style.arcHeight)
		jsonObject.with("borderStyle", style.borderLineStyle)
		jsonObject.with("borderColor", style.borderColor)
		jsonObject.with("labelStyle", createLabelStyleObject(style))
	}
	
	private def JsonElement createShapeStyleObject(NodeStyleDescription style){
		val jsonObject = new JsonObject()
		jsonObject.with("shape", "line")
		jsonObject.with("labelStyle", createLabelStyleObject(style))
	}
	
	private def dispatch JsonElement createBackgroundColorObject(FlatContainerStyleDescription description) {
		val jsonObject = new JsonObject()
		jsonObject.with("colorStart", description.backgroundColor)
		jsonObject.with("colorEnd", description.foregroundColor)
		jsonObject.with("direction", description.backgroundStyle)
	}
	
	private def dispatch JsonElement createBackgroundColorObject(EObject object) {
		throw new UnsupportedOperationException(
			"Unexpected Type: " + object.eClass.name)
	}
	
	private def JsonElement createLabelStyleObject(BasicLabelStyleDescription style) {
		val jsonObject = new JsonObject()
		jsonObject.with("color", style.labelColor)
		if(style instanceof LabelStyleDescription){
			jsonObject.with("alignment", style.labelAlignment)
		}
		jsonObject.with("italic", style.labelFormat.contains(FontFormat::ITALIC_LITERAL))
		jsonObject.with("bold", style.labelFormat.contains(FontFormat::BOLD_LITERAL))
		jsonObject.with("underline", style.labelFormat.contains(FontFormat::UNDERLINE_LITERAL))
		jsonObject.with("strikethrough", style.labelFormat.contains(FontFormat::STRIKE_THROUGH_LITERAL))
		jsonObject.with("size", style.labelSize)
		jsonObject.with("showIcon", style.showIcon)
		if(style.showIcon){
			jsonObject.with("iconPath", style.iconPath)			
		}
		jsonObject
	}
	
	
	protected def dispatch with(JsonObject jsonObject, String propertyName, FixedColor color) {
		jsonObject.with(propertyName, "#" + String.format("%02X", color.red) + String.format("%02X", color.green) + String.format("%02X", color.blue))
	}
	
	protected def dispatch with(JsonObject jsonObject, String propertyName, BackgroundStyle style) {
		val convertedStyle = switch style {
			case BackgroundStyle::GRADIENT_LEFT_TO_RIGHT_LITERAL: "left-to-right"
			case BackgroundStyle::LIQUID_LITERAL: "diagonal"
			default: "top-to-bottom"
		}
		jsonObject.with(propertyName, convertedStyle)
	}
	
	protected def dispatch with(JsonObject jsonObject, String propertyName, LineStyle style) {
		val convertedStyle = switch style {
			case LineStyle::DASH_DOT_LITERAL: "dash-dot"
			case LineStyle::DASH_LITERAL: "dash"
			case LineStyle::DOT_LITERAL: "dot"
			default: "solid"
		}
		jsonObject.with(propertyName, convertedStyle)
	}

	protected def dispatch with(JsonObject jsonObject, String propertyName, LabelAlignment alignment) {
		val convertedStyle = switch alignment {
			case LabelAlignment::RIGHT: "right"
			case LabelAlignment::LEFT: "left"
			default: "center"
		}
		jsonObject.with(propertyName, convertedStyle)
	}

	protected def dispatch with(JsonObject jsonObject, String propertyName, ContainerLayout layout) {
		val convertedStyle = switch layout {
			case ContainerLayout::FREE_FORM: "freeform"
			case ContainerLayout::HORIZONTAL_STACK: "horizontal-stack"
			case ContainerLayout::VERTICAL_STACK: "vertical-stack"
			default: "list"
		}
		jsonObject.with(propertyName, convertedStyle)
	}
	
	protected def dispatch with(JsonObject jsonObject, String propertyName, EdgeArrows arrows) {
		val list = new LinkedList<String>();
		switch arrows {
			case EdgeArrows::DIAMOND_LITERAL: list.add("diamond")
			case EdgeArrows::FILL_DIAMOND_LITERAL: {list.add("diamond") list.add("fill")}
			case EdgeArrows::INPUT_ARROW_LITERAL: list.add("input")
			case EdgeArrows::INPUT_ARROW_WITH_DIAMOND_LITERAL: {list.add("input") list.add("diamond")}
			case EdgeArrows::INPUT_ARROW_WITH_FILL_DIAMOND_LITERAL: {list.add("input") list.add("diamond") list.add("fill")}
			case EdgeArrows::INPUT_CLOSED_ARROW_LITERAL: {list.add("input") list.add("closed")}
			case EdgeArrows::INPUT_FILL_CLOSED_ARROW_LITERAL: {list.add("fill") list.add("closed")}
			case EdgeArrows::OUTPUT_ARROW_LITERAL: list.add("output")
			case EdgeArrows::OUTPUT_CLOSED_ARROW_LITERAL: {list.add("output") list.add("closed")}
			case EdgeArrows::OUTPUT_FILL_CLOSED_ARROW_LITERAL: {list.add("output") list.add("fill") list.add("closed")}
            default: list.add("none")
		}
		jsonObject.withArray(propertyName, list)
	}
	
	protected def dispatch with(JsonObject jsonObject, String propertyName, EdgeRouting edgeRouting) {
		val convertedStyle = switch edgeRouting {
			case EdgeRouting::MANHATTAN_LITERAL: "manhattan"
			case EdgeRouting::TREE_LITERAL: "tree"
			default: "straight"
		}
		jsonObject.with(propertyName, convertedStyle)
	}

}