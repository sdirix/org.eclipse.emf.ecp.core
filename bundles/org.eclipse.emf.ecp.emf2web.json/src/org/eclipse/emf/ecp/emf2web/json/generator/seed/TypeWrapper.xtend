package org.eclipse.emf.ecp.emf2web.json.generator.seed

import org.eclipse.emf.ecp.emf2web.controller.GenerationInfo
import org.eclipse.emf.ecp.emf2web.exporter.SchemaWrapper

class TypeWrapper implements SchemaWrapper {

	override getName() {
		return "JSONForms TypeScript Seed Project";
	}

	override getFileExtension() {
		"ts"
	}

	override wrap(String toWrap, String type) {
		switch (type) {
			case GenerationInfo.MODEL_TYPE:
				wrapModel(toWrap).toString
			case GenerationInfo.VIEW_TYPE:
				wrapView(toWrap).toString
			default:
				throw new IllegalArgumentException("Could not wrap: " + type)
		}

	}

	def wrapModel(String model) {
		'''
			export const Schema = «model»;
		'''
	}

	def wrapView(String view) {
		'''
			export const UISchema = «view»;
		'''
	}

}