/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Alexandra Buzila - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.ide.editor.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.ecp.view.internal.provider.Migrator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Alexandra Buzila
 * 
 */
public class UnknownFeaturesMigrationDialog extends UnknownFeaturesDialog {
	/* the registered migrators */
	private final List<Migrator> migrators;
	private final Map<EObject, AnyType> nonMigratedFeatures = new HashMap<EObject, AnyType>();
	private final Shell parentShell;

	/**
	 * @param parentShell the shell for creating the dialog
	 * @param title the title of the dialog
	 * @param objects the map of unresolved {@link EObject}s and their corresponding {@link AnyType}
	 * @param migrators the list of registered {@link Migrator}
	 */
	public UnknownFeaturesMigrationDialog(Shell parentShell, String title, Map<EObject, AnyType> objects,
		List<Migrator> migrators) {
		super(parentShell, title, objects);
		this.parentShell = parentShell;
		this.migrators = migrators;

		final String desc1 = "Unkown view model elements or attributes have been found in your view model.\nThey will be dropped on the next save."; //$NON-NLS-1$
		final String desc2 = "Unkown view model elements or attributes have been found in your view model.\nShould they be migrated? Non-migrated elements will be dropped on the next save."; //$NON-NLS-1$
		setDescription(migrators.isEmpty() ? desc1 : desc2);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createButton(org.eclipse.swt.widgets.Composite, int, java.lang.String,
	 *      boolean)
	 */
	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		// TODO Auto-generated method stub
		if (id == IDialogConstants.OK_ID) {
			label = migrators.isEmpty() ? label : "Migrate"; //$NON-NLS-1$
		}
		return super.createButton(parent, id, label, defaultButton);
	}

	private void migrateModels() {

		for (final Iterator<Entry<EObject, AnyType>> itr = objects.entrySet().iterator(); itr
			.hasNext();) {
			final Entry<EObject, AnyType> entry = itr.next();
			final EObject key = entry.getKey();
			final AnyType value = entry.getValue();
			final FeatureMap anyAttribute = value.getAnyAttribute();
			final FeatureMap mixed = value.getMixed();
			final Migrator migrator = getMigrator(key);
			if (migrator != null) {
				migrator.migrate(key, anyAttribute, mixed);
			} else {
				nonMigratedFeatures.put(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * @return
	 */
	private Migrator getMigrator(EObject key) {
		if (migrators == null) {
			return null;
		}
		for (final Migrator migrator : migrators) {
			if (migrator.isApplicable(key)) {
				return migrator;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		if (!migrators.isEmpty()) {
			migrateModels();
			if (!nonMigratedFeatures.isEmpty()) {
				final UnknownFeaturesDialog dialog = new UnknownFeaturesDialog(parentShell, "Not migrated features", //$NON-NLS-1$
					nonMigratedFeatures);
				dialog.open();
			}
		}
		super.okPressed();
	}

	/**
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return description;
	}

}
