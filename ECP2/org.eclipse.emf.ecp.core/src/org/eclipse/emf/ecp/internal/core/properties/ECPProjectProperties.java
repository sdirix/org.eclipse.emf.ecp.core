/*
 * Copyright (c) 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.ecp.internal.core.properties;

import org.eclipse.net4j.util.properties.DefaultPropertyTester;
import org.eclipse.net4j.util.properties.IProperties;
import org.eclipse.net4j.util.properties.Properties;
import org.eclipse.net4j.util.properties.Property;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProvider;

/**
 * @author Eike Stepper
 */
public class ECPProjectProperties extends Properties<ECPProject> {
	public static final IProperties<ECPProject> INSTANCE = new ECPProjectProperties();

	public ECPProjectProperties() {
		super(ECPProject.class);

		add(new Property<ECPProject>("name", "Name", "The name of this project.") {
			@Override
			protected Object eval(ECPProject project) {
				return project.getName();
			}
		});

		add(new Property<ECPProject>("repositoryName") {
			@Override
			protected Object eval(ECPProject project) {
				return project.getRepository().getName();
			}
		});

		add(new Property<ECPProject>("repositoryLabel", "Repository", "The repository of this project.") {
			@Override
			protected Object eval(ECPProject project) {
				return project.getRepository().getLabel();
			}
		});

		add(new Property<ECPProject>("providerName") {
			@Override
			protected Object eval(ECPProject project) {
				ECPProvider provider = project.getProvider();
				if (provider != null) {
					return provider.getName();
				}

				return "<unknown provider>";
			}
		});

		add(new Property<ECPProject>("providerLabel", "Provider", "The provider of this project.") {
			@Override
			protected Object eval(ECPProject project) {
				ECPProvider provider = project.getProvider();
				if (provider != null) {
					return provider.getLabel();
				}

				return "<unknown provider>";
			}
		});
		add(new Property<ECPProject>("isDirty", "Is project dirty", "Has this project unsaved changes?") {
			@Override
			protected Object eval(ECPProject project) {
				return project.isModelDirty();
			}
		});
	}

	public static void main(String[] args) {
		new Tester().dumpContributionMarkup();
	}

	/**
	 * @author Eike Stepper
	 */
	public static final class Tester extends DefaultPropertyTester<ECPProject> {
		public static final String NAMESPACE = "org.eclipse.emf.ecp.core.project";

		public Tester() {
			super(NAMESPACE, INSTANCE);
		}
	}
}
