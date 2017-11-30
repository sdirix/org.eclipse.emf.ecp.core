/*******************************************************************************
 * Copyright (c) 2011-2017 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.table.internal.validation;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.calls;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.test.common.DefaultRealm;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.ecp.view.spi.model.VViewModelProperties;
import org.eclipse.emf.ecp.view.spi.provider.EMFFormsViewService;
import org.eclipse.emf.ecp.view.spi.table.model.DetailEditing;
import org.eclipse.emf.ecp.view.spi.table.model.VTableControl;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emf.ecp.view.spi.table.model.VTableFactory;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.EMFFormsDatabinding;
import org.junit.BeforeClass;
import org.junit.Test;

public class TableValidationInitiator_Test {

	private static DefaultRealm realm;

	@BeforeClass
	public static void beforeClass() {
		realm = new DefaultRealm();
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	@Test
	public void testInstantiateTableWithPanel() throws DatabindingFailedException {
		final VView view = VViewFactory.eINSTANCE.createView();
		final VTableControl table = VTableFactory.eINSTANCE.createTableControl();
		view.getChildren().add(table);
		table.setDetailEditing(DetailEditing.WITH_PANEL);
		final VTableDomainModelReference tableDMR = VTableFactory.eINSTANCE.createTableDomainModelReference();
		table.setDomainModelReference(tableDMR);
		final VFeaturePathDomainModelReference pathDMR = VViewFactory.eINSTANCE.createFeaturePathDomainModelReference();
		tableDMR.setDomainModelReference(pathDMR);
		pathDMR.setDomainModelEFeature(EcorePackage.eINSTANCE.getEPackage_EClassifiers());

		final EPackage domainModel = EcoreFactory.eINSTANCE.createEPackage();
		final EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		domainModel.getEClassifiers().add(eClass);

		final TableValidationInitiator initiator = new TableValidationInitiator();
		final ViewModelContext context = mock(ViewModelContext.class);

		when(context.getViewModel()).thenReturn(view);
		when(context.getDomainModel()).thenReturn(domainModel);
		final EMFFormsViewService viewService = mock(EMFFormsViewService.class);
		when(context.getService(EMFFormsViewService.class)).thenReturn(viewService);
		when(viewService.getView(same(eClass), any(VViewModelProperties.class)))
			.thenReturn(VViewFactory.eINSTANCE.createView());
		final EMFFormsDatabinding databinding = mock(EMFFormsDatabinding.class);
		when(context.getService(EMFFormsDatabinding.class)).thenReturn(databinding);
		final IObservableValue observeValue = EMFObservables.observeValue(realm, domainModel,
			EcorePackage.eINSTANCE.getEPackage_EClassifiers());
		when(databinding.getObservableValue(pathDMR, domainModel)).thenReturn(observeValue);
		when(context.getChildContext(any(EObject.class), any(VElement.class), any(VView.class)))
			.thenReturn(mock(ViewModelContext.class));
		initiator.instantiate(context);
		inOrder(context).verify(context, calls(1)).getChildContext(same(eClass), same(table), any(VView.class));
	}

}
