/*******************************************************************************
 * Copyright (c) 2011-2012 EclipseSource Muenchen GmbH and others.
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
package org.eclipse.emf.ecp.core.emffilter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecp.core.util.IFilterProvider;

/**
 * This class provides all EPackages that are per default in an Eclipse Modeling
 * Edition.
 * 
 * @author Eugen Neufeld
 * 
 */
public class DefaultFilter implements IFilterProvider {

	/**
	 * Convenient constructor.
	 */
	public DefaultFilter() {
	}

	/**
	 * This returns all package uris known in an default modeling edition including emfstore.
	 * 
	 * @return a {@link Collection} of {@link String}s of the default packages in the modeling edition of eclipse
	 */
	public Collection<String> getFilteredPackages() {
		Set<String> packages = new HashSet<String>();
		addE4Models(packages);
		addEMFStoreModels(packages);
		addEMFModels(packages);
		addCDOModels(packages);
		addOCLModels(packages);
		addUMLModels(packages);
		addEMFCompareModels(packages);
		addGMFModels(packages);
		
		// xml
		packages.add("http://www.eclipse.org/xsd/2002/XSD");
		packages.add("http://www.w3.org/XML/1998/namespace");
		// other default
		packages.add("http://www.eclipse.org/amalgamation/discovery/1.0");
		packages.add("http://www.eclipse.org/acceleo/profiler/3.0");
		packages.add("http://www.eclipse.org/acceleo/mtl/3.0");

		//TODO add Graphiti
		
		return packages;
	}

	/**
	 * @param packages
	 */
	private void addGMFModels(Set<String> packages) {
		// gmf
		packages.add("http://www.eclipse.org/gmf/runtime/1.0.0/notation");
		packages.add("http://www.eclipse.org/gmf/runtime/1.0.2/notation");
		packages.add("http://www.eclipse.org/gmf/runtime/1.0.1/notation");
	}

	/**
	 * @param packages
	 */
	private void addEMFCompareModels(Set<String> packages) {
		// emf compare
		packages.add("http://www.eclipse.org/emf/compare/match/1.1");
		packages.add("http://www.eclipse.org/emf/compare/epatch/0.1");
		packages.add("http://www.eclipse.org/emf/compare/diff/1.1");
	}

	/**
	 * @param packages
	 */
	private void addUMLModels(Set<String> packages) {
		// UML
		packages.add("http://www.eclipse.org/uml2/2.2.0/GenModel");
		packages.add("http://www.eclipse.org/uml2/1.1.0/GenModel");
		packages.add("http://www.eclipse.org/uml2/schemas/Standard/1");
		packages.add("http://www.eclipse.org/uml2/2.1.0/UML");
		packages.add("http://www.eclipse.org/uml2/3.0.0/UML");
		packages.add("http://www.eclipse.org/uml2/4.0.0/Types");
		packages.add("http://www.eclipse.org/uml2/4.0.0/UML/Profile/L3");
		packages.add("http://www.eclipse.org/uml2/4.0.0/UML/Profile/L2");
		packages.add("http://www.eclipse.org/uml2/2.0.0/UML");
		packages.add("http://www.eclipse.org/uml2/4.0.0/UML");
	}

	/**
	 * @param packages
	 */
	private void addOCLModels(Set<String> packages) {
		// OCL
		packages.add("http://www.eclipse.org/ocl/1.1.0/OCL/CST");
		packages.add("http://www.eclipse.org/ocl/1.1.0/OCL/Expressions");
		packages.add("http://www.eclipse.org/ocl/1.1.0/Ecore");
		packages.add("http://www.eclipse.org/ocl/1.1.0/UML");
		packages.add("http://www.eclipse.org/ocl/1.1.0/OCL");
		packages.add("http://www.eclipse.org/ocl/1.1.0/OCL/Types");
		packages.add("http://www.eclipse.org/ocl/1.1.0/OCL/Utilities");
	}

	/**
	 * @param packages
	 */
	private void addCDOModels(Set<String> packages) {
		// CDO
		packages.add("http://www.eclipse.org/emf/CDO/Eresource/4.0.0");
		packages.add("http://www.eclipse.org/emf/CDO/security/4.1.0");
		packages.add("http://www.eclipse.org/emf/CDO/Etypes/4.0.0");
	}

	/**
	 * @param packages
	 */
	private void addEMFModels(Set<String> packages) {
		// emf
		packages.add("http://www.eclipse.org/emf/2002/Ecore");
		packages.add("http://www.eclipse.org/emf/2002/Tree");
		packages.add("http://www.eclipse.org/emf/2003/Change");
		packages.add("http://www.eclipse.org/emf/2003/XMLType");
		packages.add("http://www.eclipse.org/emf/2004/Ecore2Ecore");
		packages.add("http://www.eclipse.org/emf/2009/Validation");
		packages.add("http://www.eclipse.org/emf/2002/Mapping");
		packages.add("http://www.eclipse.org/emf/2002/GenModel");
		packages.add("http://www.eclipse.org/emf/2005/Ecore2XML");
		packages.add("http://www.eclipse.org/emf/2002/XSD2Ecore");
	}

	/**
	 * @param packages
	 */
	private void addEMFStoreModels(Set<String> packages) {
		// emfstore
		packages.add("http://eclipse.org/emf/emfstore/client/model");
		packages.add("http://eclipse.org/emf/emfstore/common/model");
		packages.add("http://eclipse.org/emf/emfstore/server/model");
		packages.add("http://eclipse.org/emf/emfstore/server/model/roles");
		packages.add("http://eclipse.org/emf/emfstore/server/model/versioning");
		packages.add("http://eclipse.org/emf/emfstore/server/model/versioning/operations");
		packages.add("http://eclipse.org/emf/emfstore/server/model/versioning/events");
		packages.add("http://eclipse.org/emf/emfstore/server/model/versioning/events/server/");
		packages.add("http://eclipse.org/emf/emfstore/server/model/versioning/operations/semantic");
		packages.add("http://eclipse.org/emf/emfstore/server/model/url");
		packages.add("http://eclipse.org/emf/emfstore/server/model/accesscontrol");
	}

	/**
	 * @param packages
	 */
	private void addE4Models(Set<String> packages) {
		// e4
		packages.add("http://www.eclipse.org/ui/2010/UIModel/application/ui/menu");
		packages.add("http://www.eclipse.org/ui/2010/UIModel/application/ui");
		packages.add("http://www.eclipse.org/ui/2010/UIModel/fragment");
		packages.add("http://www.eclipse.org/ui/2010/UIModel/application/ui/basic");
		packages.add("http://www.eclipse.org/ui/2010/UIModel/application");
		packages.add("http://www.eclipse.org/ui/2010/UIModel/application/ui/advanced");
		packages.add("http://www.eclipse.org/ui/2010/UIModel/application/commands");
		packages.add("http://www.eclipse.org/ui/2010/UIModel/application/descriptor/basic");
	}

}
