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
package org.eclipse.emf.ecp.view.editor.controls;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.internal.ui.Messages;
import org.eclipse.emf.ecp.ui.common.CompositeFactory;
import org.eclipse.emf.ecp.ui.common.SelectionComposite;
import org.eclipse.emf.ecp.view.editor.handler.CreateDomainModelReferenceWizard;
import org.eclipse.emf.ecp.view.model.common.edit.provider.CustomReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.ecp.view.spi.core.swt.SimpleControlSWTRenderer;
import org.eclipse.emf.ecp.view.spi.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;
import org.eclipse.emf.ecp.view.spi.table.model.VTableDomainModelReference;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * @author Eugen
 * 
 */
@SuppressWarnings("restriction")
public class TableColumnsDMRTableControl extends SimpleControlSWTRenderer {

	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryLabelProvider labelProvider;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.SimpleControlSWTRenderer#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createControl(Composite parent) {
		final Setting setting = getVElement().getDomainModelReference().getIterator().next();

		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackgroundMode(SWT.INHERIT_FORCE);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(composite);
		final Composite titleComposite = new Composite(composite, SWT.NONE);
		titleComposite.setBackgroundMode(SWT.INHERIT_FORCE);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(titleComposite);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(titleComposite);

		final Label filler = new Label(titleComposite, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(filler);

		final Composite buttonComposite = new Composite(titleComposite, SWT.NONE);
		buttonComposite.setBackgroundMode(SWT.INHERIT_FORCE);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(true).applyTo(buttonComposite);
		GridDataFactory.fillDefaults().align(SWT.END, SWT.BEGINNING).grab(false, false).applyTo(buttonComposite);

		final Button buttonAdd = new Button(buttonComposite, SWT.PUSH);
		buttonAdd.setText("Add");
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(false, false).applyTo(buttonAdd);

		final Button buttonRemove = new Button(buttonComposite, SWT.PUSH);
		buttonRemove.setText("Remove");
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(false, false).applyTo(buttonRemove);

		final Composite tableComposite = new Composite(composite, SWT.NONE);
		tableComposite.setBackgroundMode(SWT.INHERIT_FORCE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).hint(1, SWT.DEFAULT)
			.applyTo(tableComposite);
		final TableColumnLayout layout = new TableColumnLayout();
		tableComposite.setLayout(layout);

		final TableViewer viewer = new TableViewer(tableComposite);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(viewer.getControl());

		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		final TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		final IItemPropertyDescriptor propertyDescriptor = getItemPropertyDescriptor(setting);
		column.getColumn().setText(propertyDescriptor.getDisplayName(null));
		column.getColumn().setToolTipText(propertyDescriptor.getDescription(null));
		layout.setColumnData(column.getColumn(), new ColumnWeightData(1, true));

		viewer.setLabelProvider(labelProvider);
		viewer.setContentProvider(new ObservableListContentProvider());
		final IObservableList list = EMFEditObservables.observeList(getEditingDomain(setting),
			setting.getEObject(), setting.getEStructuralFeature());
		viewer.setInput(list);

		buttonAdd.addSelectionListener(new SelectionAdapter() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				final VTableDomainModelReference tableDomainModelReference = VTableDomainModelReference.class
					.cast(setting.getEObject());

				final EClass eclass = EReference.class.cast(
					tableDomainModelReference.getEStructuralFeatureIterator().next()).getEReferenceType();

				final Collection<EClass> classes = ECPUtil.getSubClasses(VViewPackage.eINSTANCE
					.getDomainModelReference());

				final CreateDomainModelReferenceWizard wizard = new CreateDomainModelReferenceWizard(
					setting, getEditingDomain(setting), eclass, "New Reference Element", //$NON-NLS-1$
					Messages.NewModelElementWizard_WizardTitle_AddModelElement,
					Messages.NewModelElementWizard_PageTitle_AddModelElement,
					Messages.NewModelElementWizard_PageDescription_AddModelElement,
					(VDomainModelReference) IStructuredSelection.class.cast(viewer.getSelection()).getFirstElement());

				final SelectionComposite<TreeViewer> helper = CompositeFactory.getSelectModelClassComposite(
					new HashSet<EPackage>(),
					new HashSet<EPackage>(), classes);
				wizard.setCompositeProvider(helper);

				final WizardDialog wd = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
				wd.open();
				tableComposite.layout();
			}
		});

		buttonRemove.addSelectionListener(new SelectionAdapter() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				final Object object = IStructuredSelection.class.cast(viewer.getSelection()).getFirstElement();
				if (object == null) {
					return;
				}

				final EditingDomain editingDomain = getEditingDomain(setting);
				final Command command = RemoveCommand.create(editingDomain, setting.getEObject(),
					setting.getEStructuralFeature(), object);
				editingDomain.getCommandStack().execute(command);
			}
		});

		return composite;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.AbstractControlSWTRenderer#postInit()
	 */
	@Override
	protected void postInit() {
		super.postInit();
		composedAdapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
			new CustomReflectiveItemProviderAdapterFactory(),
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		labelProvider = new AdapterFactoryLabelProvider(composedAdapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.SimpleControlSWTRenderer#dispose()
	 */
	@Override
	protected void dispose() {
		labelProvider.dispose();
		composedAdapterFactory.dispose();
		super.dispose();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.SimpleControlSWTRenderer#getUnsetText()
	 */
	@Override
	protected String getUnsetText() {
		return "No columns set";
	}

}
