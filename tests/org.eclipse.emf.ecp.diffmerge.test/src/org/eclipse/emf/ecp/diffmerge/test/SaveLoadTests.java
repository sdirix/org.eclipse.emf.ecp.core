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
package org.eclipse.emf.ecp.diffmerge.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeContextFactory;
import org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.emfstore.bowling.BowlingFactory;
import org.eclipse.emf.emfstore.bowling.BowlingPackage;
import org.eclipse.emf.emfstore.bowling.Gender;
import org.eclipse.emf.emfstore.bowling.Player;
import org.junit.Test;

/**
 * @author Eugen
 * 
 */
public class SaveLoadTests {

	/**
	 * Round trip test.
	 * 
	 * @throws IOException is thrown if file cannot be found
	 */
	@Test
	public void testRoundtrip() throws IOException {
		final VView view = VViewFactory.eINSTANCE.createView();

		final VControl vControlName = VViewFactory.eINSTANCE.createControl();
		vControlName.setDomainModelReference(BowlingPackage.eINSTANCE.getPlayer_Name());
		view.getChildren().add(vControlName);

		final VControl vControlGender = VViewFactory.eINSTANCE.createControl();
		vControlGender.setDomainModelReference(BowlingPackage.eINSTANCE.getPlayer_Gender());
		view.getChildren().add(vControlGender);

		final VControl vControlHeight = VViewFactory.eINSTANCE.createControl();
		vControlHeight.setDomainModelReference(BowlingPackage.eINSTANCE.getPlayer_Height());
		view.getChildren().add(vControlHeight);

		final VControl vControlProfessional = VViewFactory.eINSTANCE.createControl();
		vControlProfessional.setDomainModelReference(BowlingPackage.eINSTANCE.getPlayer_IsProfessional());
		view.getChildren().add(vControlProfessional);

		final VControl vControlNumberOfVictories = VViewFactory.eINSTANCE.createControl();
		vControlNumberOfVictories.setDomainModelReference(BowlingPackage.eINSTANCE.getPlayer_NumberOfVictories());
		view.getChildren().add(vControlNumberOfVictories);

		final Player left = BowlingFactory.eINSTANCE.createPlayer();
		left.setName("a"); //$NON-NLS-1$
		left.setGender(Gender.MALE);
		left.setHeight(1);
		left.setIsProfessional(true);
		left.setNumberOfVictories(1);
		final Player right = BowlingFactory.eINSTANCE.createPlayer();
		right.setName("a"); //$NON-NLS-1$
		right.setGender(Gender.FEMALE);
		right.setHeight(2);
		right.setIsProfessional(false);
		right.setNumberOfVictories(1);
		final Player target = BowlingFactory.eINSTANCE.createPlayer();
		target.setName("b"); //$NON-NLS-1$
		target.setGender(Gender.MALE);
		target.setHeight(3);
		target.setIsProfessional(true);
		target.setNumberOfVictories(1);

		final DiffMergeModelContext context = DiffMergeContextFactory.INSTANCE.createViewModelContext(view, target,
			left, right);
		context.markControl(vControlGender, true);
		context.markControl(vControlHeight, true);

		final Set<VDomainModelReference> mergedDomainObjects = context.getMergedDomainObjects();

		// save
		final ResourceSet resourceSet = new ResourceSetImpl();
		final Resource resource = resourceSet.createResource(URI.createFileURI("test.xmi")); //$NON-NLS-1$
		resource.getContents().addAll(mergedDomainObjects);
		resource.save(null);
		// load

		final ResourceSet resourceSet1 = new ResourceSetImpl();
		final Resource resource1 = resourceSet1.createResource(URI.createFileURI("test.xmi")); //$NON-NLS-1$
		resource1.load(null);

		final Set<VDomainModelReference> savedObjects = new LinkedHashSet<VDomainModelReference>();
		for (final EObject eObject : resource1.getContents()) {
			savedObjects.add((VDomainModelReference) eObject);
		}

		// check that stuff was loaded correctly
		final DiffMergeModelContext context2 = DiffMergeContextFactory.INSTANCE.createViewModelContext(view, target,
			left, right, savedObjects);

		assertTrue(context2.isControlMerged(vControlGender));
		assertTrue(context2.isControlMerged(vControlHeight));
	}

}
