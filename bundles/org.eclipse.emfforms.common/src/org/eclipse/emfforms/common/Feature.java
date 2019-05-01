/*******************************************************************************
 * Copyright (c) 2011-2019 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Mat Hansen - initial API and implementation
 * Christian W. Damus - bug 534829
 ******************************************************************************/
package org.eclipse.emfforms.common;

/**
 * ECP feature representation.
 *
 * @author Mat Hansen
 * @since 1.15
 *
 */
public final class Feature {

	/**
	 * Feature strategy enum.
	 *
	 */
	public enum STRATEGY {
		/**
		 * No strategy.
		 */
		NONE,
		/**
		 * Inherit the feature automatically to children.
		 */
		INHERIT,
	}

	private final String id;
	private final String description;
	private final STRATEGY strategy;

	/**
	 * Default constructor (no feature inheritance).
	 *
	 * @param id the feature id
	 * @param description the feature description
	 */
	public Feature(String id, String description) {
		this.id = id;
		this.description = description;
		strategy = STRATEGY.NONE;
	}

	/**
	 * Alternative constructor allowing to customize {@link STRATEGY}.
	 *
	 * @param id the feature id
	 * @param description the feature description
	 * @param strategy the feature {@link STRATEGY}
	 */
	public Feature(String id, String description, STRATEGY strategy) {
		this.id = id;
		this.description = description;
		this.strategy = strategy;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the strategy
	 */
	public STRATEGY getStrategy() {
		return strategy;
	}

	@Override
	public String toString() {
		return String.format("Feature(%s, %s)", id, strategy); //$NON-NLS-1$
	}

}
