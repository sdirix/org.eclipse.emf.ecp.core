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
package org.eclipse.emf.ecp.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager.ProjectWithNameExistsException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * ECPProject-tests
 * 
 * @author Tobias Verhoeven
 */
public class ECPProjectTests extends AbstractTest {

	
	/** The project. */
	private ECPProject project;
	
	/**
	 * Setup.
	 */
	@Before
	public void setup()  {
		
		boolean done = false;
		
		while(!done)
			try {
				this.project = getProjectManager().createProject(getProvider(), "Projekt " + UUID.randomUUID());
				done = true;
			} catch (ProjectWithNameExistsException e) {}
	}
			
	/**
	 * Tear down.
	 */
	@After
	public void tearDown() {
		project.delete();
	}
	
	/**
	 * Test contains.
	 */
	@Test
	public void testContains() {
		EObject clazz = EcoreFactory.eINSTANCE.createEClass();
		EObject reference = EcoreFactory.eINSTANCE.createEReference();
		
		assertFalse(project.contains(clazz));
		assertFalse(project.contains(reference));
		
		project.getElements().add(clazz);
		
		assertTrue(project.contains(clazz));
		assertFalse(project.contains(reference));
		
		project.getElements().add(reference);
		
		assertTrue(project.contains(clazz));
		assertTrue(project.contains(reference));
		
		project.getElements().remove(clazz);
		
		assertFalse(project.contains(clazz));
		assertTrue(project.contains(reference));
		
		project.getElements().remove(reference);
		assertFalse(project.contains(clazz));
		assertFalse(project.contains(reference));
	}
	
	/**
	 * Test get elements.
	 */
	@Test
	public void testGetElements() {
		assertEquals(0,project.getElements().size());

		EObject clazz = EcoreFactory.eINSTANCE.createEClass();
		EObject reference = EcoreFactory.eINSTANCE.createEReference();
		
		project.getElements().add(clazz);
		
		assertEquals(1,project.getElements().size());
		assertTrue(project.contains(clazz));
		assertTrue(project.getElements().contains(clazz));
		
		project.getElements().add(reference);
		assertEquals(2,project.getElements().size());
		assertTrue(project.contains(clazz));
		assertTrue(project.getElements().contains(clazz));
		assertTrue(project.contains(reference));
		assertTrue(project.getElements().contains(reference));
		
		project.getElements().remove(clazz);
		assertEquals(1,project.getElements().size());
		assertFalse(project.contains(clazz));
		assertFalse(project.getElements().contains(clazz));
		assertTrue(project.contains(reference));
		assertTrue(project.getElements().contains(reference));
		
		project.getElements().remove(reference);
		assertEquals(0,project.getElements().size());
		assertFalse(project.contains(clazz));
		assertFalse(project.getElements().contains(clazz));
		assertFalse(project.contains(reference));
		assertFalse(project.getElements().contains(reference));
	}
	
	/**
	 * Test get unsupported e packages.
	 */
	@Test
	public void testGetUnsupportedEPackages() {
		Collection<EPackage> unsupportedPackages = project.getUnsupportedEPackages();
		fail("This method always returns an empty list");
	}
	

	/**
	 * Test visible packages.
	 */
	@Test
	public void testVisiblePackages() {
		Set<EPackage> packages = project.getVisiblePackages();
		assertFalse( packages.contains(EcorePackage.eINSTANCE));
		EObject clazz = EcoreFactory.eINSTANCE.createEClass();
		EObject reference = EcoreFactory.eINSTANCE.createEReference();
		
		project.getElements().add(clazz);
		assertTrue(project.contains(clazz));
		
		packages.add(EcorePackage.eINSTANCE);
		assertTrue(packages.contains(EcorePackage.eINSTANCE));
				
		project.getElements().add(reference);
		assertTrue(project.contains(reference));	
	}
	
	/**
	 * Test visible classes.
	 */
	@Test
	public void testVisibleClasses() {
		boolean failed=true;
		EObject clazz = EcoreFactory.eINSTANCE.createEClass();
		EObject reference = EcoreFactory.eINSTANCE.createEReference();
		
		try {
			project.getVisibleEClasses().add(clazz.eClass()); 
			}
		catch (UnsupportedOperationException upe) {
			failed = false;
		}
		
		if (failed) {
			fail("Class list is modifiable");
		}
		
		Set<EClass> classes = new HashSet<EClass>(project.getVisibleEClasses());
		assertEquals(0,classes.size());
		classes.add(clazz.eClass());
		project.setVisibleEClasses(classes);
		
		assertEquals(1,project.getVisibleEClasses().size());
		assertTrue(project.getVisibleEClasses().contains(clazz.eClass()));
		
		classes.add(reference.eClass());
		
		assertEquals(2,project.getVisibleEClasses().size());
		assertTrue(project.getVisibleEClasses().contains(clazz.eClass()));
		assertTrue(project.getVisibleEClasses().contains(reference.eClass()));	
	}
	
	/**
	 * Test get reference candidates.
	 */
	@Test
	public void testGetReferenceCandidates() {
		
		EObject clazz = EcoreFactory.eINSTANCE.createEClass();
		
		EObject referenceObjectA = EcoreFactory.eINSTANCE.createEReference();
		referenceObjectA.eSet( referenceObjectA.eClass().getEStructuralFeature("name"), "my little reference");
		
		EObject referenceObjectB = EcoreFactory.eINSTANCE.createEReference();
		referenceObjectB.eSet( referenceObjectB.eClass().getEStructuralFeature("name"), "my big reference");
		
		EReference reference = (EReference) clazz.eClass().getEStructuralFeature(EcorePackage.EREFERENCE);
		
		project.getElements().add(clazz);
		
		assertFalse(project.getReferenceCandidates(clazz, reference).hasNext());
		
		project.getElements().add(referenceObjectA);
		
		Iterator<EObject> iterator = project.getReferenceCandidates(clazz, reference);
		List<EObject> objects = new ArrayList<EObject>(2);
		while (iterator.hasNext()) {
			objects.add(iterator.next());
		}
		
		assertFalse(iterator.hasNext());
		assertEquals(1,objects.size());
		assertTrue(objects.contains(referenceObjectA));
		objects.clear();
		
		project.getElements().add(referenceObjectB);
		iterator = project.getReferenceCandidates(clazz, reference);
		
		while (iterator.hasNext()) {
			objects.add(iterator.next());
		}
	
		assertFalse(iterator.hasNext());
		assertEquals(2,objects.size());
		assertTrue(objects.contains(referenceObjectA));
		assertTrue(objects.contains(referenceObjectB));		
	}
	
	/**
	 * Test save properties.
	 */
	@Test
	public void testSaveProperties() {
		project.saveProperties();	
	}
	
	/**
	 * Test dirty save model.
	 */
	@Test
	public void testDirtySaveModel() {
		EObject clazz = EcoreFactory.eINSTANCE.createEClass();	
		assertFalse(project.isModelDirty());
		project.getElements().add(clazz);
		assertTrue(project.isModelDirty());
		project.saveModel();
		assertFalse(project.isModelDirty());
	}
	
	/**
	 * Test delete elements.
	 */
	@Test
	public void testDeleteElements() {
		EObject clazz = EcoreFactory.eINSTANCE.createEClass();
		EObject reference = EcoreFactory.eINSTANCE.createEReference();
		EObject attribute = EcoreFactory.eINSTANCE.createEAttribute();
		
		assertEquals(0,project.getElements().size());
		project.getElements().add(clazz);
		project.getElements().add(reference);
		assertEquals(2,project.getElements().size());
		
		project.deleteElements(Collections.singleton(clazz));
		assertEquals(1,project.getElements().size());
		assertTrue(project.contains(reference));
		assertFalse(project.contains(clazz));
		
		project.deleteElements(Collections.singleton(reference));
		assertEquals(0,project.getElements().size());
		
		project.getElements().add(clazz);
		project.getElements().add(reference);
		project.getElements().add(attribute);
		
		assertEquals(3,project.getElements().size());
		project.deleteElements(Arrays.asList(clazz,reference,attribute));
		assertEquals(0,project.getElements().size());
		
		boolean thrown=false;
		try {
			project.deleteElements(Collections.singleton(clazz));
		} catch (IllegalArgumentException iae) {
			thrown = true;
		}
		assertTrue(thrown);
	}
	
	/**
	 * Checks if is modelroot.
	 */
	@Test
	public void testIsModelroot() {
		EObject clazz = EcoreFactory.eINSTANCE.createEClass();
		EObject reference = EcoreFactory.eINSTANCE.createEReference();
		EObject attribute = EcoreFactory.eINSTANCE.createEAttribute();
		
		assertEquals(0,project.getElements().size());
		project.getElements().add(clazz);
		project.getElements().add(reference);
		project.getElements().add(attribute);
		assertEquals(3,project.getElements().size());
		
		assertFalse(project.isModelRoot(clazz));
		assertFalse(project.isModelRoot(reference));
		assertFalse(project.isModelRoot(attribute));
	
		assertTrue(project.isModelRoot(clazz.eContainer()));	
	}
}
