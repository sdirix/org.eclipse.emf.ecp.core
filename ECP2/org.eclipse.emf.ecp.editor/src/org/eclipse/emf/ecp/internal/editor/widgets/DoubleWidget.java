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

package org.eclipse.emf.ecp.internal.editor.widgets;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.core.databinding.DataBindingContext;

import java.util.regex.Pattern;

/**
 * The widget implementation for editing a Double value.
 * 
 * @author Eugen Neufeld
 */
public class DoubleWidget extends AbstractTextWidget<Double> {
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

	/**
	 * Constructor for the {@link DoubleWidget}.
	 * 
	 * @param dbc the {@link DataBindingContext} to use
	 * @param editingDomain the {@link EditingDomain} to use
	 * @param eObject the {@link EObject} for this widget
	 */
	public DoubleWidget(DataBindingContext dbc, EditingDomain editingDomain, EObject eObject) {
		super(dbc, editingDomain, eObject);
	}

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
