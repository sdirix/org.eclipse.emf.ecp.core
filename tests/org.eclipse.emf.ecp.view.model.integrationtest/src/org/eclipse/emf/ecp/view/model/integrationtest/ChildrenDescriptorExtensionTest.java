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
package org.eclipse.emf.ecp.view.model.integrationtest;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.edit.groupedgrid.model.GroupedGridPackage;
import org.eclipse.emf.ecp.view.group.model.GroupPackage;
import org.eclipse.emf.ecp.view.model.ViewPackage;
import org.eclipse.emf.ecp.view.rule.model.RulePackage;
import org.eclipse.emf.ecp.view.separator.model.SeparatorPackage;
import org.eclipse.emf.ecp.view.table.model.VTablePackage;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.junit.Test;

public class ChildrenDescriptorExtensionTest {

	/**
	 * Needs to be adapted after refactoring
	 * These are the counts for a fully integrated model with all elements
	 */
	private static final int RENDERABLE_CHILD_COUNT = 3;
	private static final int COMPOSITE_CHILD_COUNT = RENDERABLE_CHILD_COUNT;
	private static final int ABSTRACTCATEGORIZATION_CHILD_COUNT = RENDERABLE_CHILD_COUNT + 1;
	private static final int NUMBER_OF_COMPOSITES = 12;
	private static final int NUMBER_OF_CATEGORIZATIONS = 3;
	private static final int CATEGORIZATION_CHILD_COUNT = ABSTRACTCATEGORIZATION_CHILD_COUNT
		+ NUMBER_OF_CATEGORIZATIONS;
	private static final int COMPOSITECOLLECTION_CHILD_COUNT = COMPOSITE_CHILD_COUNT + NUMBER_OF_COMPOSITES;
	private static final int VIEW_CHILD_COUNT = NUMBER_OF_COMPOSITES + CATEGORIZATION_CHILD_COUNT;
	private static final int SHOWRULE_CHILD_COUNT = 3;
	private static final int ENABLERULE_CHILD_COUNT = 3;
	private static final int LEAFCONDITION_CHILD_COUNT = 1;
	private static final int ORCONDITION_CHILD_COUNT = 3;
	private static final int ANDCONDITION_CHILD_COUNT = 3;

	private static final int CATEGORY_CHILD_COUNT = NUMBER_OF_COMPOSITES + ABSTRACTCATEGORIZATION_CHILD_COUNT;
	private static final int CONTROL_CHILD_COUNT = RENDERABLE_CHILD_COUNT + 1;
	// TODO: Should be not -3, span and rules are missing
	private static final int TABLECONTROL_CHILD_COUNT = CONTROL_CHILD_COUNT + 1 - 3;
	private static final int TABLECOLUMN_CHILD_COUNT = 0;
	private static final int CUSTOMCOMPOSITE_CHILD_COUNT = RENDERABLE_CHILD_COUNT;
	// TODO: Should be not -3, span and rules are missing
	private static final int SEPARATOR_CHILD_COUNT = RENDERABLE_CHILD_COUNT - 3;
	private static final int COLUMNCOMPOSITE_CHILD_COUNT = COMPOSITECOLLECTION_CHILD_COUNT;
	private static final int COLUMN_CHILD_COUNT = COMPOSITECOLLECTION_CHILD_COUNT;
	// TODO: Should be not -9, labels, custom control, groupedgrid, span, 2 rules, vertical, horizontal, separator,table
	// are missing
	private static final int GROUP_CHILD_COUNT = COMPOSITECOLLECTION_CHILD_COUNT - 10;
	private static final int ACTION_CHILD_COUNT = 0;
	// TODO: Should be not -2, rules are missing
	private static final int GROUPEDGRID_CHILD_COUNT = RENDERABLE_CHILD_COUNT + 1 - 2;
	private static final int GRIDEDGROUP_CHILD_COUNT = 1;
	// TODO: Should be not -6, labels, group, custom composites, vertical, horizontal, separator ,table are missing
	private static final int GRIDEDGROUPROW_CHILD_COUNT = NUMBER_OF_COMPOSITES - 7;
	private static final int GRIDEDGROUPSPAN_CHILD_COUNT = 0;

	private final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new ComposedAdapterFactory(
		ComposedAdapterFactory.Descriptor.Registry.INSTANCE), new BasicCommandStack());

	@Test
	public void testGroupedGridChildDescriptors() {
		final int size = getChildrenSize(GroupedGridPackage.eINSTANCE.getGroupedGrid());
		assertEquals(GROUPEDGRID_CHILD_COUNT, size);
	}

	@Test
	public void testGridedGroupChildDescriptors() {
		final int size = getChildrenSize(GroupedGridPackage.eINSTANCE.getGroup());
		assertEquals(GRIDEDGROUP_CHILD_COUNT, size);
	}

	@Test
	public void testGridedGroupRowChildDescriptors() {
		final int size = getChildrenSize(GroupedGridPackage.eINSTANCE.getRow());
		assertEquals(GRIDEDGROUPROW_CHILD_COUNT, size);
	}

	@Test
	public void testGridedSpanChildDescriptors() {
		final int size = getChildrenSize(GroupedGridPackage.eINSTANCE.getSpan());
		assertEquals(GRIDEDGROUPSPAN_CHILD_COUNT, size);
	}

	@Test
	public void testViewChildDescriptors() {
		final int size = getChildrenSize(ViewPackage.eINSTANCE.getView());
		assertEquals(VIEW_CHILD_COUNT, size);
	}

	/**
	 * Class is abstract, Exception expected
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAbstractCategorizationChildDescriptors() {
		getChildrenSize(ViewPackage.eINSTANCE.getAbstractCategorization());
	}

	/**
	 * Class is abstract, Exception expected
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRuleChildDescriptors() {
		getChildrenSize(RulePackage.eINSTANCE.getRule());
	}

	@Test
	public void testShowRuleChildDescriptors() {
		final int size = getChildrenSize(RulePackage.eINSTANCE.getShowRule());
		assertEquals(SHOWRULE_CHILD_COUNT, size);
	}

	@Test
	public void testEnableRuleChildDescriptors() {
		final int size = getChildrenSize(RulePackage.eINSTANCE.getEnableRule());
		assertEquals(ENABLERULE_CHILD_COUNT, size);
	}

	/**
	 * Class is abstract, Exception expected
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConditionChildDescriptors() {
		getChildrenSize(RulePackage.eINSTANCE.getCondition());
	}

	@Test
	public void testLeafConditionChildDescriptors() {
		final int size = getChildrenSize(RulePackage.eINSTANCE.getLeafCondition());
		assertEquals(LEAFCONDITION_CHILD_COUNT, size);
	}

	@Test
	public void testOrConditionChildDescriptors() {
		final int size = getChildrenSize(RulePackage.eINSTANCE.getOrCondition());
		assertEquals(ORCONDITION_CHILD_COUNT, size);
	}

	@Test
	public void testAndConditionChildDescriptors() {
		final int size = getChildrenSize(RulePackage.eINSTANCE.getAndCondition());
		assertEquals(ANDCONDITION_CHILD_COUNT, size);
	}

	@Test
	public void testCategorizationChildDescriptors() {
		final int size = getChildrenSize(ViewPackage.eINSTANCE.getCategorization());
		assertEquals(CATEGORIZATION_CHILD_COUNT, size);
	}

	@Test
	public void testCategoryChildDescriptors() {
		final int size = getChildrenSize(ViewPackage.eINSTANCE.getCategory());
		assertEquals(CATEGORY_CHILD_COUNT, size);
	}

	/**
	 * Class is abstract, Exception expected
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCompositeChildDescriptors() {
		getChildrenSize(ViewPackage.eINSTANCE.getComposite());
	}

	@Test
	public void testControlChildDescriptors() {
		final int size = getChildrenSize(ViewPackage.eINSTANCE.getControl());
		assertEquals(CONTROL_CHILD_COUNT, size);
	}

	@Test
	public void testTableControlChildDescriptors() {
		final int size = getChildrenSize(VTablePackage.eINSTANCE.getTableControl());
		assertEquals(TABLECONTROL_CHILD_COUNT, size);
	}

	@Test
	public void testTableColumnChildDescriptors() {
		final int size = getChildrenSize(VTablePackage.eINSTANCE.getTableColumn());
		assertEquals(TABLECOLUMN_CHILD_COUNT, size);
	}

	@Test
	public void testCustomCompositeColumnChildDescriptors() {
		final int size = getChildrenSize(ViewPackage.eINSTANCE.getCustomComposite());
		assertEquals(CUSTOMCOMPOSITE_CHILD_COUNT, size);
	}

	@Test
	public void testSeparatorChildDescriptors() {
		final int size = getChildrenSize(SeparatorPackage.eINSTANCE.getSeparator());
		assertEquals(SEPARATOR_CHILD_COUNT, size);
	}

	/**
	 * Class is abstract, Exception expected
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCompositeCollectionDescriptors() {
		getChildrenSize(ViewPackage.eINSTANCE.getCompositeCollection());
	}

	@Test
	public void testColumnCompositeDescriptors() {
		final int size = getChildrenSize(ViewPackage.eINSTANCE.getColumnComposite());
		assertEquals(COLUMNCOMPOSITE_CHILD_COUNT, size);
	}

	@Test
	public void testColumnDescriptors() {
		final int size = getChildrenSize(ViewPackage.eINSTANCE.getColumn());
		assertEquals(COLUMN_CHILD_COUNT, size);
	}

	@Test
	public void testGroupDescriptors() {
		final int size = getChildrenSize(GroupPackage.eINSTANCE.getGroup());
		assertEquals(GROUP_CHILD_COUNT, size);
	}

	/**
	 * Class is abstract, Exception expected
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRenderableDescriptors() {
		getChildrenSize(ViewPackage.eINSTANCE.getRenderable());
	}

	@Test
	public void testActionDescriptors() {
		final int size = getChildrenSize(ViewPackage.eINSTANCE.getAction());
		assertEquals(ACTION_CHILD_COUNT, size);
	}

	/**
	 * @param category
	 * @return
	 */
	private int getChildrenSize(EClass eClass) {
		final EObject eObject = EcoreUtil.create(eClass);
		return domain.getNewChildDescriptors(eObject, null).size();
	}

}
