/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.table.migrate;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.ecp.view.internal.provider.Migrator;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.table.model.VReadOnlyColumnConfiguration;
import org.eclipse.emf.ecp.view.spi.table.model.VTableColumnConfiguration;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emf.ecp.view.spi.table.model.VTableFactory;

/**
 * A special migrator for tables.
 *
 * @author Eugen Neufeld
 *
 */
@SuppressWarnings("restriction")
public class TableMigrator implements Migrator {

	@Override
	public void migrate(EObject eObject, FeatureMap anyAttribute, FeatureMap mixed) {
		if (!VTableControl.class.isInstance(eObject)) {
			return;
		}
		for (int i = 0; i < mixed.size(); i++) {
			final EStructuralFeature feature = mixed.getEStructuralFeature(i);
			final AnyType object = (AnyType) mixed.getValue(i);
			if ("columns".equals(feature.getName())) { //$NON-NLS-1$
				migrateColumns(VTableControl.class.cast(eObject), object.getAnyAttribute(), object.getMixed());
			}
		}
	}

	private static void migrateColumns(VTableControl cast,
		FeatureMap anyAttribute, FeatureMap mixed) {
		boolean readOnly = false;
		for (int i = 0; i < anyAttribute.size(); i++) {
			if ("readOnly".equals(anyAttribute.getEStructuralFeature(i).getName())) { //$NON-NLS-1$
				readOnly = Boolean.parseBoolean((String) anyAttribute.getValue(i));
			}
		}
		for (int i = 0; i < mixed.size(); i++) {
			if ("attribute".equals(mixed.getEStructuralFeature(i).getName())) { //$NON-NLS-1$
				final AnyType object = (AnyType) mixed.getValue(i);
				final URI uri = EcoreUtil.getURI(object);
				final EObject eObject = cast.eResource().getResourceSet().getEObject(uri, true);
				final VFeaturePathDomainModelReference dmr = VViewFactory.eINSTANCE
					.createFeaturePathDomainModelReference();
				dmr.setDomainModelEFeature((EStructuralFeature) eObject);
				final VTableDomainModelReference tdmr = getTableDomainModelReference(cast);
				if (tdmr == null) {
					continue;
				}
				tdmr.getColumnDomainModelReferences().add(dmr);
				if (readOnly) {
					final VReadOnlyColumnConfiguration readOnlyColumnConfiguration = getReadOnlyColumnConfiguration(cast);
					readOnlyColumnConfiguration.getColumnDomainReferences().add(dmr);
				}
			}
		}

	}

	private static VTableDomainModelReference getTableDomainModelReference(VTableControl tableControl) {
		final VDomainModelReference dmr = tableControl.getDomainModelReference();
		if (VTableDomainModelReference.class.isInstance(dmr)) {
			return (VTableDomainModelReference) dmr;
		}
		for (final EObject eObject : dmr.eContents()) {
			if (VTableDomainModelReference.class.isInstance(eObject)) {
				return (VTableDomainModelReference) eObject;
			}
		}
		return null;
	}

	private static VReadOnlyColumnConfiguration getReadOnlyColumnConfiguration(
		VTableControl cast) {
		for (final VTableColumnConfiguration cc : cast.getColumnConfigurations()) {
			if (VReadOnlyColumnConfiguration.class.isInstance(cc)) {
				return (VReadOnlyColumnConfiguration) cc;
			}
		}
		final VReadOnlyColumnConfiguration config = VTableFactory.eINSTANCE.createReadOnlyColumnConfiguration();
		cast.getColumnConfigurations().add(config);
		return config;
	}

	@Override
	public boolean isApplicable(EObject eObject) {
		return VTableControl.class.isInstance(eObject);
	}
}
