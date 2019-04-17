/*******************************************************************************
 * Copyright (c) 2011-2019 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Lucas Koehler - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.editor.controls;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.eclipse.emf.databinding.IEMFValueProperty;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.rule.model.Condition;
import org.eclipse.emf.ecp.view.spi.rule.model.EnableRule;
import org.eclipse.emf.ecp.view.spi.rule.model.IterateCondition;
import org.eclipse.emf.ecp.view.spi.rule.model.RuleFactory;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.emf.EMFFormsDatabindingEMF;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link RuleConditionDmrUtil}.
 *
 * @author Lucas Koehler
 *
 */
public class RuleConditionDmrUtil_Test {

	private EMFFormsDatabindingEMF databinding;
	private ReportService reportService;
	private Condition condition;

	@Before
	public void setUp() {
		databinding = mock(EMFFormsDatabindingEMF.class);
		reportService = mock(ReportService.class);
		condition = RuleFactory.eINSTANCE.createLeafCondition();
	}

	@Test
	public void getDmrRootEClass_noView() {
		final IterateCondition iterateCondition = RuleFactory.eINSTANCE.createIterateCondition();
		iterateCondition.setItemCondition(condition);

		final Optional<EClass> result = RuleConditionDmrUtil.getDmrRootEClass(databinding, reportService, condition);
		assertFalse(result.isPresent());
		verify(reportService, never()).report(any());
	}

	@Test
	public void getDmrRootEClass_noIterateConditions() {
		final EnableRule rule = RuleFactory.eINSTANCE.createEnableRule();
		rule.setCondition(condition);
		final VView view = VViewFactory.eINSTANCE.createView();
		view.getAttachments().add(rule);
		view.setRootEClass(EcorePackage.Literals.EATTRIBUTE);

		final Optional<EClass> result = RuleConditionDmrUtil.getDmrRootEClass(databinding, reportService, condition);
		assumeTrue(result.isPresent());
		assertSame(EcorePackage.Literals.EATTRIBUTE, result.get());
		verify(reportService, never()).report(any());
	}

	@Test
	public void getDmrRootEClass_oneIterateCondition() throws DatabindingFailedException {
		final IterateCondition iterate = RuleFactory.eINSTANCE.createIterateCondition();
		final VDomainModelReference iterateDmr = VViewFactory.eINSTANCE.createDomainModelReference();
		iterate.setItemReference(iterateDmr);
		iterate.setItemCondition(condition);
		final EnableRule rule = RuleFactory.eINSTANCE.createEnableRule();
		rule.setCondition(iterate);
		final VView view = VViewFactory.eINSTANCE.createView();
		view.getAttachments().add(rule);
		view.setRootEClass(EcorePackage.Literals.EREFERENCE);

		final IEMFValueProperty valueProperty = mock(IEMFValueProperty.class);
		when(valueProperty.getValueType()).thenReturn(EcorePackage.Literals.ESTRUCTURAL_FEATURE__ECONTAINING_CLASS);
		when(databinding.getValueProperty(iterateDmr, EcorePackage.Literals.EREFERENCE)).thenReturn(valueProperty);

		final Optional<EClass> result = RuleConditionDmrUtil.getDmrRootEClass(databinding, reportService, condition);
		assertTrue(result.isPresent());
		assertSame(EcorePackage.Literals.ECLASS, result.get());
		verify(reportService, never()).report(any());
	}

	@Test
	public void getDmrRootEClass_nestedIterateConditions() throws DatabindingFailedException {
		// Hierarchy: View -> Rule -> iterate -> iterateNested -> condition

		// deeper nested
		final IterateCondition iterateNested = RuleFactory.eINSTANCE.createIterateCondition();
		final VDomainModelReference iterateNestedDmr = VViewFactory.eINSTANCE.createDomainModelReference();
		iterateNested.setItemReference(iterateNestedDmr);
		iterateNested.setItemCondition(condition);

		final IterateCondition iterate = RuleFactory.eINSTANCE.createIterateCondition();
		final VDomainModelReference iterateDmr = VViewFactory.eINSTANCE.createDomainModelReference();
		iterate.setItemReference(iterateDmr);
		iterate.setItemCondition(iterateNested);

		final EnableRule rule = RuleFactory.eINSTANCE.createEnableRule();
		rule.setCondition(iterate);
		final VView view = VViewFactory.eINSTANCE.createView();
		view.getAttachments().add(rule);
		view.setRootEClass(EcorePackage.Literals.EREFERENCE);

		final IEMFValueProperty valueProperty = mock(IEMFValueProperty.class);
		when(valueProperty.getValueType()).thenReturn(EcorePackage.Literals.ESTRUCTURAL_FEATURE__ECONTAINING_CLASS);
		when(databinding.getValueProperty(iterateDmr, EcorePackage.Literals.EREFERENCE)).thenReturn(valueProperty);

		final IEMFValueProperty valuePropertyNested = mock(IEMFValueProperty.class);
		when(valuePropertyNested.getValueType()).thenReturn(EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS);
		when(databinding.getValueProperty(iterateNestedDmr, EcorePackage.Literals.ECLASS))
			.thenReturn(valuePropertyNested);

		final Optional<EClass> result = RuleConditionDmrUtil.getDmrRootEClass(databinding, reportService, condition);
		assertTrue(result.isPresent());
		assertSame(EcorePackage.Literals.EANNOTATION, result.get());
		verify(reportService, never()).report(any());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getDmrRootEClass_iterateCondition_databindingFailed() throws DatabindingFailedException {
		final IterateCondition iterate = RuleFactory.eINSTANCE.createIterateCondition();
		final VDomainModelReference iterateDmr = VViewFactory.eINSTANCE.createDomainModelReference();
		iterate.setItemReference(iterateDmr);
		iterate.setItemCondition(condition);
		final EnableRule rule = RuleFactory.eINSTANCE.createEnableRule();
		rule.setCondition(iterate);
		final VView view = VViewFactory.eINSTANCE.createView();
		view.getAttachments().add(rule);
		view.setRootEClass(EcorePackage.Literals.EREFERENCE);

		when(databinding.getValueProperty(iterateDmr, EcorePackage.Literals.EREFERENCE))
			.thenThrow(DatabindingFailedException.class);

		final Optional<EClass> result = RuleConditionDmrUtil.getDmrRootEClass(databinding, reportService, condition);
		assertFalse(result.isPresent());
		verify(reportService, times(1)).report(any());
	}

	@Test
	public void getDmrRootEClass_iterateCondition_noEReference() throws DatabindingFailedException {
		final IterateCondition iterate = RuleFactory.eINSTANCE.createIterateCondition();
		final VDomainModelReference iterateDmr = VViewFactory.eINSTANCE.createDomainModelReference();
		iterate.setItemReference(iterateDmr);
		iterate.setItemCondition(condition);
		final EnableRule rule = RuleFactory.eINSTANCE.createEnableRule();
		rule.setCondition(iterate);
		final VView view = VViewFactory.eINSTANCE.createView();
		view.getAttachments().add(rule);
		view.setRootEClass(EcorePackage.Literals.EREFERENCE);

		final IEMFValueProperty valueProperty = mock(IEMFValueProperty.class);
		when(valueProperty.getValueType()).thenReturn(EcorePackage.Literals.ESTRUCTURAL_FEATURE__VOLATILE);
		when(databinding.getValueProperty(iterateDmr, EcorePackage.Literals.EREFERENCE)).thenReturn(valueProperty);

		final Optional<EClass> result = RuleConditionDmrUtil.getDmrRootEClass(databinding, reportService, condition);
		assertFalse(result.isPresent());
		verify(reportService, never()).report(any());
	}
}
