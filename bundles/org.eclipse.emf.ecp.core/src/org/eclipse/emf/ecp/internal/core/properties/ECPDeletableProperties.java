/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eike Stepper - initial API and implementation
 * Eugen Neufeld - JavaDoc
 *******************************************************************************/
package org.eclipse.emf.ecp.internal.core.properties;

import org.eclipse.net4j.util.properties.DefaultPropertyTester;
import org.eclipse.net4j.util.properties.IProperties;
import org.eclipse.net4j.util.properties.Properties;
import org.eclipse.net4j.util.properties.Property;

import org.eclipse.emf.ecp.core.util.ECPDeletable;

/**
 * @author Eugen Neufeld
 */
public final class ECPDeletableProperties extends Properties<ECPDeletable> {
	private static final IProperties<ECPDeletable> INSTANCE = new ECPDeletableProperties();

	private ECPDeletableProperties() {
		super(ECPDeletable.class);

		add(new Property<ECPDeletable>("canDelete") {
			@Override
			protected Object eval(ECPDeletable deletable) {
				return deletable.canDelete();
			}
		});
	}

	/**
	 * @author Eike Stepper
	 */
	public static final class Tester extends DefaultPropertyTester<ECPDeletable> {
		private static final String NAMESPACE = "org.eclipse.emf.ecp.core.deletable";

		/** The tester constructor. */
		public Tester() {
			super(NAMESPACE, INSTANCE);
		}
	}
}
