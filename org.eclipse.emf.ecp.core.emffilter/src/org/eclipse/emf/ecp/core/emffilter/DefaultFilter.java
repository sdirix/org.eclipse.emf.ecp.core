package org.eclipse.emf.ecp.core.emffilter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecp.core.util.IFilterProvider;

/**
 * This class provides all EPackages that are per default in an Eclipse Modeling
 * Edition
 * 
 * @author Eugen Neufeld
 * 
 */
public class DefaultFilter implements IFilterProvider {

	public DefaultFilter() {
	}

	@Override
	public Collection<String> getFilteredPackages() {
		Set<String> packages = new HashSet<>();
		//e4
		packages.add("http://www.eclipse.org/ui/2010/UIModel/application/ui/menu");
		packages.add("http://www.eclipse.org/ui/2010/UIModel/application/ui");
		packages.add("http://www.eclipse.org/ui/2010/UIModel/fragment");
		packages.add("http://www.eclipse.org/ui/2010/UIModel/application/ui/basic");
		packages.add("http://www.eclipse.org/ui/2010/UIModel/application");
		packages.add("http://www.eclipse.org/ui/2010/UIModel/application/ui/advanced");
		packages.add("http://www.eclipse.org/ui/2010/UIModel/application/commands");
		packages.add("http://www.eclipse.org/ui/2010/UIModel/application/descriptor/basic");
		//emfstore
		packages.add("http://eclipse.org/emf/emfstore/server/model/roles");
		packages.add("http://eclipse.org/emf/emfstore/common/model");
		packages.add("http://eclipse.org/emf/emfstore/server/model/versioning/events");
		packages.add("http://eclipse.org/emf/emfstore/client/model");
		packages.add("http://eclipse.org/emf/emfstore/server/model/versioning");
		packages.add("http://eclipse.org/emf/emfstore/server/model/versioning/operations");
		packages.add("http://eclipse.org/emf/emfstore/server/model/versioning/events/server/");
		packages.add("http://eclipse.org/emf/emfstore/server/model/url");
		packages.add("http://eclipse.org/emf/emfstore/server/model/versioning/operations/semantic");
		packages.add("http://eclipse.org/emf/emfstore/server/model");
		packages.add("http://eclipse.org/emf/emfstore/server/model/accesscontrol");
		//emf
		packages.add("http://www.eclipse.org/emf/2002/Ecore");
		packages.add("http://www.eclipse.org/emf/2002/Tree");
		packages.add("http://www.eclipse.org/emf/2003/Change");
		packages.add("http://www.eclipse.org/emf/2003/XMLType");
		//other default
		//xml
		packages.add("http://www.eclipse.org/xsd/2002/XSD");
		packages.add("http://www.w3.org/XML/1998/namespace");
		
		return packages;
	}

}
