/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.diffmerge.swt;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * This util class provides a method, which allows to copy values from one {@link VControl} to another generically.
 * 
 * @author Eugen Neufeld
 * 
 */
public final class DefaultMergeUtil {

	private DefaultMergeUtil() {

	}

	/**
	 * Copies the values from one {@link VControl} to another.
	 * 
	 * @param from the {@link VControl} holding the values
	 * @param to the {@link VControl} which values should be updated
	 */
	public static void copyValues(VControl from, VControl to) {
		final Iterator<Setting> fromIterator = from.getDomainModelReference().getIterator();
		Iterator<Setting> toIterator = to.getDomainModelReference().getIterator();
		int stepsChecked = 0;
		while (fromIterator.hasNext()) {
			final Setting toSetting = toIterator.next();
			final Setting fromSetting = fromIterator.next();
			stepsChecked++;
			if (toSetting.getEStructuralFeature().isDerived()) {
				break;
			}
			if (!toSetting.getEStructuralFeature().isChangeable()) {
				continue;
			}
			final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(toSetting
				.getEObject());

			if (toSetting.getEStructuralFeature().isMany()) {
				editingDomain.getCommandStack().execute(
					RemoveCommand.create(editingDomain, toSetting.getEObject(), toSetting.getEStructuralFeature(),
						toSetting.get(true)));
			}
			if (EAttribute.class.isInstance(toSetting.getEStructuralFeature())) {

				if (toSetting.getEStructuralFeature().isMany()) {
					editingDomain.getCommandStack().execute(
						AddCommand.create(editingDomain, toSetting.getEObject(), toSetting.getEStructuralFeature(),
							fromSetting.get(true)));
				}
				else {
					editingDomain.getCommandStack().execute(
						SetCommand.create(editingDomain, toSetting.getEObject(), toSetting.getEStructuralFeature(),
							fromSetting.get(true)));
				}
			}
			if (EReference.class.isInstance(toSetting.getEStructuralFeature())) {
				if (toSetting.getEStructuralFeature().isMany()) {
					for (final EObject eObject : (Collection<EObject>) fromSetting.get(true)) {
						editingDomain.getCommandStack().execute(
							AddCommand.create(editingDomain, toSetting.getEObject(), toSetting.getEStructuralFeature(),
								EcoreUtil.copy(eObject)));
					}
					toIterator = to.getDomainModelReference().getIterator();
					for (int i = 0; i < stepsChecked; i++) {
						toIterator.next();
					}
				}
				else {
					editingDomain.getCommandStack().execute(
						SetCommand.create(editingDomain, toSetting.getEObject(), toSetting.getEStructuralFeature(),
							EcoreUtil.copy((EObject) fromSetting.get(true))));
				}
			}
		}
	}
}
