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
 * 
 *******************************************************************************/
package org.eclipse.emf.ecp.edit.internal.swt.controls;

import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
/**
 * This class defines a Control which is used for displaying {@link EStructuralFeature}s which have a double
 * value.
 * 
 * @author Eugen Neufeld
 * 
 */
public class DoubleControl extends AbstractTextControl<Double> {
	/**
	 * Constructor for a double control.
	 * 
	 * @param showLabel whether to show a label
	 * @param itemPropertyDescriptor the {@link IItemPropertyDescriptor} to use
	 * @param feature the {@link EStructuralFeature} to use
	 * @param modelElementContext the {@link EditModelElementContext} to use
	 * @param embedded whether this control is embedded in another control
	 */
	public DoubleControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature,
		EditModelElementContext modelElementContext,boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext,embedded);
	}
	/**
	 * This block comes from {@link Double#valueOf(String)}.
	 */
	private static final String DIGITS = "(\\p{Digit}+)";
	private static final String HEX_DIGITS = "(\\p{XDigit}+)";
	// an exponent is 'e' or 'E' followed by an optionally
	// signed decimal integer.
	private static final String EXP = "[eE][+-]?" + DIGITS;
	private static final String FP_REGEX = "[\\x00-\\x20]*" + // Optional leading "whitespace"
		"[+-]?(" + // Optional sign character
		"NaN|" + // "NaN" string
		"Infinity|" + // "Infinity" string

		// A decimal floating-point string representing a finite positive
		// number without a leading sign has at most five basic pieces:
		// Digits . Digits ExponentPart FloatTypeSuffix
		//
		// Since this method allows integer-only strings as input
		// in addition to strings of floating-point literals, the
		// two sub-patterns below are simplifications of the grammar
		// productions from the Java Language Specification, 2nd
		// edition, section 3.10.2.

		// Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
		"(((" + DIGITS + "(\\.)?(" + DIGITS + "?)(" + EXP + ")?)|" +

	// . Digits ExponentPart_opt FloatTypeSuffix_opt
		"(\\.(" + DIGITS + ")(" + EXP + ")?)|" +

		// Hexadecimal strings
		"((" +
		// 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
		"(0[xX]" + HEX_DIGITS + "(\\.)?)|" +

		// 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
		"(0[xX]" + HEX_DIGITS + "?(\\.)" + HEX_DIGITS + ")" +

		")[pP][+-]?" + DIGITS + "))" + "[fFdD]?))" + "[\\x00-\\x20]*";// Optional trailing "whitespace"

	private static final Pattern DOUBLE_PATTERN = Pattern.compile(FP_REGEX);
	@Override
	protected Double convertStringToModel(String s) {
		return Double.parseDouble(s);
	}

	@Override
	protected boolean validateString(String s) {
		/*
		 * Do not perform any validation here, since a double can be represented with characters which include 'E', 'f'
		 * or
		 * 'd'. Furthermore if values become to be, 'Infinity' is also a valid value.
		 */
		return DOUBLE_PATTERN.matcher(s).matches();
	}

	@Override
	protected String convertModelToString(Double t) {
		return Double.toString(t);
	}

	@Override
	protected void postValidate(String text) {
		try {
			setUnvalidatedString(Double.toString(Double.parseDouble(text)));
		} catch (NumberFormatException e) {
			setUnvalidatedString(Double.toString(getDefaultValue()));
		}
	}

	@Override
	protected Double getDefaultValue() {
		return 0.0;
	}

}
