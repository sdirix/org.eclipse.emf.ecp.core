/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.table.ui.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.ui.view.test.ViewTestHelper;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.eclipse.emf.ecp.view.table.model.VTableColumn;
import org.eclipse.emf.ecp.view.table.model.VTableControl;
import org.eclipse.emf.ecp.view.table.model.VTableFactory;
import org.eclipse.emf.ecp.view.test.common.swt.DatabindingClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Jonas
 * 
 */
@RunWith(DatabindingClassRunner.class)
public class TableControlTest {

	@Test
	public void testTableWithoutColumns() {
		// setup model
		final TableControlHandle handle = createInitializedTableWithoutTableColumns();

		// Test NodeBuidlers
		final Node<Renderable> node = buildNode(handle.getTableControl());
		assertEquals(1, ViewTestHelper.countNodes(node));
		assertEquals(handle.getTableControl(), node.getRenderable());
	}

	@Test
	public void testTableWithTwoColumns() {
		// setup model
		final TableControlHandle handle = createTableWithTwoTableColumns();

		// Test NodeBuidlers
		final Node<Renderable> node = buildNode(handle.getTableControl());
		assertEquals(1, ViewTestHelper.countNodes(node));
		assertEquals(handle.getTableControl(), node.getRenderable());
	}

	public static TableControlHandle createTableWithTwoTableColumns() {
		final TableControlHandle tableControlHandle = createInitializedTableWithoutTableColumns();
		final VTableColumn tableColumn1 = createTableColumn();
		tableColumn1.setAttribute(EcorePackage.eINSTANCE.getEClass_Abstract());
		tableControlHandle.addFirstTableColumn(tableColumn1);
		final VTableColumn tableColumn2 = createTableColumn();
		tableColumn2.setAttribute(EcorePackage.eINSTANCE.getEClass_Abstract());
		tableControlHandle.addSecondTableColumn(tableColumn2);
		return tableControlHandle;
	}

	/**
	 * @return
	 */
	private static VTableColumn createTableColumn() {
		return VTableFactory.eINSTANCE.createTableColumn();
	}

	public static TableControlHandle createInitializedTableWithoutTableColumns() {
		final TableControlHandle tableControlHandle = createUninitializedTableWithoutColumns();
		final VFeaturePathDomainModelReference domainModelReference = ViewFactory.eINSTANCE
			.createVFeaturePathDomainModelReference();
		domainModelReference.setDomainModelEFeature(EcorePackage.eINSTANCE.getEClass_ESuperTypes());
		tableControlHandle.getTableControl().setDomainModelReference(domainModelReference);

		return tableControlHandle;
	}

	public static TableControlHandle createUninitializedTableWithoutColumns() {
		final VTableControl tableControl = createTableControl();
		return new TableControlHandle(tableControl);
	}

	/**
	 * @return
	 */
	private static VTableControl createTableControl() {
		final VTableControl tc = VTableFactory.eINSTANCE.createTableControl();
		tc.setDomainModelReference(ViewFactory.eINSTANCE.createVFeaturePathDomainModelReference());
		return tc;
	}

	private Node<Renderable> buildNode(final Renderable renderable) {
		final EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		return ViewTestHelper.build(renderable, eClass);
	}

}
