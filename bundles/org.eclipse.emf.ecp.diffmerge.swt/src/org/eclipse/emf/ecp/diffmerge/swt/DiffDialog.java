/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.diffmerge.swt;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext;
import org.eclipse.emf.ecp.edit.spi.ECPControlFactory;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.ecp.view.spi.model.LabelAlignment;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewFactory;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Class creating a diff Dialog.
 * 
 * @author Eugen Neufeld
 * 
 */
// TODO API
@SuppressWarnings("restriction")
public class DiffDialog {

	/**
	 * Variant constant for indicating RAP controls.
	 */
	private static final String CUSTOM_VARIANT = "org.eclipse.rap.rwt.customVariant"; //$NON-NLS-1$

	private final VControl left;
	private final VControl right;
	private final VControl main;
	private final String diffAttribute;
	private final DiffMergeModelContext viewModelContext;

	private VControl mergeControl;

	/**
	 * Constructor for the diff dialog.
	 * 
	 * @param viewModelContext the {@link org.eclipse.emf.ecp.view.spi.context.ViewModelContext ViewModelContext}
	 * @param diffAttribute the display name of the attribute
	 * @param left the left {@link VControl}
	 * @param right the right {@link VControl}
	 * @param main the main {@link VControl}
	 */
	public DiffDialog(DiffMergeModelContext viewModelContext, String diffAttribute, VControl left,
		VControl right, VControl main) {
		this.viewModelContext = viewModelContext;
		this.diffAttribute = diffAttribute;
		this.left = left;
		this.right = right;
		this.main = main;
	}

	/**
	 * Creates the dialog content.
	 * 
	 * @param parent the parent {@link Composite}
	 */
	public void create(Composite parent) {
		final BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();

		final ServiceReference<ECPControlFactory> serviceReference = bundleContext
			.getServiceReference(ECPControlFactory.class);
		final ECPControlFactory controlFactory = bundleContext.getService(serviceReference);

		final ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		// GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setShowFocusedControl(true);
		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).applyTo(scrolledComposite);

		final Composite composite = new Composite(scrolledComposite, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).extendedMargins(10, 10, 10, 10)
			.applyTo(composite);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(composite);

		final Control title = createTitleLabel(composite);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(title);

		final Control diff = createDiff(composite, controlFactory);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(diff);

		final Control merge = createMerge(composite, controlFactory);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(merge);

		scrolledComposite.setContent(composite);
		composite.layout();
		final Point point = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scrolledComposite.setMinSize(point);

		bundleContext.ungetService(serviceReference);

	}

	/**
	 * Creates the Merge content.
	 * 
	 * @param parent the {@link Composite}
	 * @param ecpControlFactory the {@link ECPControlFactory}
	 * @return the control showing the merge
	 */
	private Control createMerge(final Composite parent, final ECPControlFactory ecpControlFactory) {
		final Composite mainObjectComposite = new Composite(parent, SWT.NONE);
		mainObjectComposite.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_compare_dialog_merge"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(false).applyTo(mainObjectComposite);

		final Label mainObject = new Label(mainObjectComposite, SWT.NONE);
		mainObject.setText(Messages.DiffDialog_mainObject);
		mainObject.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_compare_dialog_merge_label"); //$NON-NLS-1$
		mergeControl = EcoreUtil.copy(main);
		final ResourceSet resourceSet = new ResourceSetImpl();
		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE),
			new BasicCommandStack(), resourceSet);
		final EObject domainObject = EcoreUtil.copy(viewModelContext.getDomainModel());
		resourceSet.eAdapters().add(new AdapterFactoryEditingDomain.EditingDomainProvider(domain));
		final Resource resource = resourceSet.createResource(URI.createURI("VIRTUAL_URI_TEMP"));
		resource.getContents().add(domainObject);
		createControl(mainObjectComposite, mergeControl, domainObject, false);

		final Button bConfirm = new Button(mainObjectComposite, SWT.PUSH);
		bConfirm.setText(Messages.DiffDialog_Confirm);
		bConfirm.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_compare_dialog_merge_confirm"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(false, false).applyTo(bConfirm);
		bConfirm.addSelectionListener(new SelectionAdapter() {

			private static final long serialVersionUID = 1L;

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				replaceMainWith(mergeControl, false);
				bConfirm.getShell().dispose();
			}

		});

		return mainObjectComposite;
	}

	/**
	 * Creates the Diff content.
	 * 
	 * @param parent the {@link Composite}
	 * @param ecpControlFactory the {@link ECPControlFactory}
	 * @return the control showing the diff
	 */
	private Control createDiff(final Composite parent, final ECPControlFactory ecpControlFactory) {
		final Group group = new Group(parent, SWT.NONE);
		group.setText(Messages.DiffDialog_DifferenceGroup);
		group.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_compare_dialog_diff"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(false).applyTo(group);

		final Label leftObject = new Label(group, SWT.NONE);
		leftObject.setText(Messages.DiffDialog_leftObject);
		leftObject.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_compare_dialog_diff_left"); //$NON-NLS-1$

		createControl(group, EcoreUtil.copy(left), EcoreUtil.copy(viewModelContext.getLeftModel()), true);

		final Button bReplaceWithLeft = new Button(group, SWT.PUSH);
		bReplaceWithLeft.setText(Messages.DiffDialog_replaceWithLeft);
		bReplaceWithLeft.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_compare_dialog_diff_leftReplace"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(false, false).applyTo(bReplaceWithLeft);
		bReplaceWithLeft.addSelectionListener(new SelectionAdapter() {

			private static final long serialVersionUID = 1L;

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				replaceMainWith(left);
			}

		});

		final Label rightObject = new Label(group, SWT.NONE);
		rightObject.setText(Messages.DiffDialog_rightObject);
		leftObject.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_compare_dialog_diff_right"); //$NON-NLS-1$

		// final SWTControl rightControl = ecpControlFactory.createControl(SWTControl.class,
		// right.getDomainModelReference());
		// rightControl.init(viewModelContext, right);
		// final List<RenderingResultRow<Control>> rightControls = rightControl.createControls(group);
		// applyLayout(rightControls);
		// rightControl.setEditable(false);
		createControl(group, EcoreUtil.copy(right), EcoreUtil.copy(viewModelContext.getRightModel()), true);

		final Button bReplaceWithRight = new Button(group, SWT.PUSH);
		bReplaceWithRight.setText(Messages.DiffDialog_replaceWithRight);
		bReplaceWithLeft.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_compare_dialog_diff_rightReplace"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(false, false).applyTo(bReplaceWithRight);
		bReplaceWithRight.addSelectionListener(new SelectionAdapter() {

			private static final long serialVersionUID = 1L;

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				replaceMainWith(right);
			}

		});

		return group;
	}

	private void createControl(Composite parent, VControl control, EObject domainObject, boolean readonly) {
		final VView leftView = VViewFactory.eINSTANCE.createView();
		leftView.setRootEClass(domainObject.eClass());
		final VControl leftControl = control;
		leftControl.setReadonly(readonly);
		leftControl.setLabelAlignment(LabelAlignment.NONE);
		leftView.getChildren().add(leftControl);
		try {
			ECPSWTViewRenderer.INSTANCE.render(parent, domainObject, leftView);
		} catch (final ECPRendererException ex) {
			ex.printStackTrace();
		}
	}

	private void replaceMainWith(VControl replaceControl) {
		replaceMainWith(replaceControl, true);
	}

	private void replaceMainWith(VControl replaceControl, boolean updateMerge) {
		DefaultMergeUtil.copyValues(replaceControl, main);
		if (updateMerge) {
			DefaultMergeUtil.copyValues(replaceControl, mergeControl);
		}

	}

	/**
	 * Creates the title {@link Label}.
	 * 
	 * @param parent the {@link Composite}
	 * @return the control showing the title
	 */
	private Control createTitleLabel(final Composite parent) {
		final Label title = new Label(parent, SWT.NONE);
		title.setText(diffAttribute);
		title.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_compare_dialog_title"); //$NON-NLS-1$
		return title;
	}
}
