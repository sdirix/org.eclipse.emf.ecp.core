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

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecp.test.common.DefaultRealm;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeListener;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeNotification;
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
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class TableValidationInitiator_Test {

	private static DefaultRealm realm;

	@BeforeClass
	public static void beforeClass() {
		realm = new DefaultRealm();
	}

	@SuppressWarnings({ "rawtypes" })
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

	@SuppressWarnings({ "rawtypes" })
	@Test
	public void testInstantiateMultipleTablesWithPanel() throws DatabindingFailedException {
		final VView view = VViewFactory.eINSTANCE.createView();
		final VTableControl table1 = VTableFactory.eINSTANCE.createTableControl();
		view.getChildren().add(table1);
		table1.setDetailEditing(DetailEditing.WITH_PANEL);
		final VTableDomainModelReference tableDMR1 = VTableFactory.eINSTANCE.createTableDomainModelReference();
		table1.setDomainModelReference(tableDMR1);
		final VFeaturePathDomainModelReference pathDMR1 = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		tableDMR1.setDomainModelReference(pathDMR1);
		pathDMR1.setDomainModelEFeature(EcorePackage.eINSTANCE.getEPackage_EClassifiers());

		final VTableControl table2 = VTableFactory.eINSTANCE.createTableControl();
		view.getChildren().add(table2);
		table2.setDetailEditing(DetailEditing.WITH_PANEL);
		final VTableDomainModelReference tableDMR2 = VTableFactory.eINSTANCE.createTableDomainModelReference();
		table2.setDomainModelReference(tableDMR2);
		final VFeaturePathDomainModelReference pathDMR2 = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		tableDMR2.setDomainModelReference(pathDMR2);
		pathDMR2.setDomainModelEFeature(EcorePackage.eINSTANCE.getEPackage_EClassifiers());

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
		final IObservableValue observeValue1 = EMFObservables.observeValue(realm, domainModel,
			EcorePackage.eINSTANCE.getEPackage_EClassifiers());
		final IObservableValue observeValue2 = EMFObservables.observeValue(realm, domainModel,
			EcorePackage.eINSTANCE.getEPackage_EClassifiers());
		when(databinding.getObservableValue(pathDMR1, domainModel)).thenReturn(observeValue1);
		when(databinding.getObservableValue(pathDMR2, domainModel)).thenReturn(observeValue2);
		when(context.getChildContext(any(EObject.class), any(VElement.class), any(VView.class)))
			.thenReturn(mock(ViewModelContext.class));
		initiator.instantiate(context);
		inOrder(context).verify(context, calls(1)).getChildContext(same(eClass), same(table1), any(VView.class));
		inOrder(context).verify(context, calls(1)).getChildContext(same(eClass), same(table2), any(VView.class));
	}

	private void createTable(VTableControl table, VFeaturePathDomainModelReference pathDMR) {
		table.setDetailEditing(DetailEditing.WITH_PANEL);
		final VTableDomainModelReference tableDMR = VTableFactory.eINSTANCE.createTableDomainModelReference();
		table.setDomainModelReference(tableDMR);
		tableDMR.setDomainModelReference(pathDMR);
	}

	private ViewModelContext mockContext(VView view, EObject domainModel, final Set<ModelChangeListener> listeners,
		EMFFormsDatabinding databinding) {
		final ViewModelContext context = mock(ViewModelContext.class);

		when(context.getViewModel()).thenReturn(view);
		when(context.getDomainModel()).thenReturn(domainModel);
		final EMFFormsViewService viewService = mock(EMFFormsViewService.class);
		when(context.getService(EMFFormsViewService.class)).thenReturn(viewService);
		when(viewService.getView(any(EClass.class), any(VViewModelProperties.class)))
			.thenReturn(VViewFactory.eINSTANCE.createView());
		when(context.getService(EMFFormsDatabinding.class)).thenReturn(databinding);
		when(context.getChildContext(any(EObject.class), any(VElement.class), any(VView.class)))
			.thenReturn(mock(ViewModelContext.class));

		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				listeners.add((ModelChangeListener) invocation.getArguments()[0]);
				return null;
			}
		}).when(context).registerDomainChangeListener(any(ModelChangeListener.class));

		return context;
	}

	@SuppressWarnings({ "rawtypes" })
	@Test
	public void testInstantiateMultipleTablesWithPanelAdd() throws DatabindingFailedException {
		final VView view = VViewFactory.eINSTANCE.createView();
		final VTableControl table1 = VTableFactory.eINSTANCE.createTableControl();
		view.getChildren().add(table1);
		final VFeaturePathDomainModelReference pathDMR1 = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		pathDMR1.setDomainModelEFeature(EcorePackage.eINSTANCE.getEPackage_EClassifiers());
		createTable(table1, pathDMR1);

		final VTableControl table2 = VTableFactory.eINSTANCE.createTableControl();
		view.getChildren().add(table2);
		final VFeaturePathDomainModelReference pathDMR2 = VViewFactory.eINSTANCE
			.createFeaturePathDomainModelReference();
		pathDMR2.setDomainModelEFeature(EcorePackage.eINSTANCE.getEPackage_EClassifiers());
		createTable(table2, pathDMR2);

		final EPackage domainModel = EcoreFactory.eINSTANCE.createEPackage();
		final EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		domainModel.getEClassifiers().add(eClass);

		final TableValidationInitiator initiator = new TableValidationInitiator();
		final Set<ModelChangeListener> listeners = new LinkedHashSet<ModelChangeListener>();
		final EMFFormsDatabinding databinding = mock(EMFFormsDatabinding.class);

		final IObservableValue observeValue1 = EMFObservables.observeValue(realm, domainModel,
			EcorePackage.eINSTANCE.getEPackage_EClassifiers());
		final IObservableValue observeValue2 = EMFObservables.observeValue(realm, domainModel,
			EcorePackage.eINSTANCE.getEPackage_EClassifiers());
		when(databinding.getObservableValue(pathDMR1, domainModel)).thenReturn(observeValue1);
		when(databinding.getObservableValue(pathDMR2, domainModel)).thenReturn(observeValue2);

		final ViewModelContext context = mockContext(view, domainModel, listeners, databinding);
		initiator.instantiate(context);

		final EClass newClass = EcoreFactory.eINSTANCE.createEClass();
		domainModel.getEClassifiers().add(newClass);

		final ModelChangeNotification notification = mock(ModelChangeNotification.class);
		when(notification.getStructuralFeature()).thenReturn(EcorePackage.eINSTANCE.getEPackage_EClassifiers());
		when(notification.getNotifier()).thenReturn(domainModel);
		final Collection<EObject> newEobjects = new LinkedHashSet<EObject>();
		newEobjects.add(newClass);
		when(notification.getNewEObjects()).thenReturn(newEobjects);
		final Notification rawNotification = mock(Notification.class);
		when(rawNotification.isTouch()).thenReturn(false);
		when(notification.getRawNotification()).thenReturn(rawNotification);

		for (final ModelChangeListener listener : listeners) {
			listener.notifyChange(notification);
		}

		inOrder(context).verify(context, calls(1)).getChildContext(same(eClass), same(table1), any(VView.class));
		inOrder(context).verify(context, calls(1)).getChildContext(same(eClass), same(table2), any(VView.class));
		inOrder(context).verify(context, calls(1)).getChildContext(same(newClass), same(table1), any(VView.class));
		inOrder(context).verify(context, calls(1)).getChildContext(same(newClass), same(table2), any(VView.class));
	}
}
