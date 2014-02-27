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
package org.eclipse.emf.ecp.diffmerge.internal.renderer.swt;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext;
import org.eclipse.emf.ecp.diffmerge.swt.DiffDialogHelper;
import org.eclipse.emf.ecp.spi.diffmerge.model.VDiffAttachment;
import org.eclipse.emf.ecp.spi.diffmerge.model.VDiffmergePackage;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCell;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCellDescription;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridDescription;
import org.eclipse.emf.ecp.view.spi.model.VAttachment;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.swt.AbstractSWTAdditionalRenderer;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Eugen
 * 
 */
public class SWTDiffMergeAddition extends AbstractSWTAdditionalRenderer<VControl> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTAdditionalRenderer#getAdditionalColumns()
	 */
	@Override
	public int getAdditionalColumns() {
		return 1;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTAdditionalRenderer#preCellRenderControl(org.eclipse.emf.ecp.view.spi.layout.grid.GridDescription,
	 *      org.eclipse.emf.ecp.view.spi.layout.grid.GridCell, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public GridCellDescription preCellRenderControl(GridDescription gridDescription, GridCell cell, Composite parent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTAdditionalRenderer#postCellRenderControl(org.eclipse.emf.ecp.view.spi.layout.grid.GridDescription,
	 *      org.eclipse.emf.ecp.view.spi.layout.grid.GridCell, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public GridCellDescription postCellRenderControl(GridDescription gridDescription, GridCell cell, Composite parent) {
		if (gridDescription.getColumns() == cell.getColumn() + 1) {
			// if (cell.getColumn() == 1) {
			createDiffButton(parent);
			return new GridCellDescription();
		}
		return null;
	}

	/**
	 * @param parent
	 */
	private void createDiffButton(Composite parent) {
		final Button diffButton = new Button(parent, SWT.PUSH);
		diffButton.setText(Messages.getString("SWTDiffMergeControlRenderer.DiffButton")); //$NON-NLS-1$
		diffButton.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_compare_button"); //$NON-NLS-1$
		// diffButton.setLayoutData(SWTRenderingHelper.INSTANCE.getLayoutHelper().getLeftColumnLayoutData());
		diffButton.addSelectionListener(new SelectionAdapter() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				openDiffDialog((DiffMergeModelContext) getViewModelContext(), getVElement());
			}

		});
		for (final VAttachment attachment : getVElement().getAttachments()) {
			if (VDiffAttachment.class.isInstance(attachment)) {
				attachment.eAdapters().add(new AdapterImpl() {

					/**
					 * {@inheritDoc}
					 * 
					 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#notifyChanged(org.eclipse.emf.common.notify.Notification)
					 */
					@Override
					public void notifyChanged(Notification msg) {
						super.notifyChanged(msg);
						if (msg.getFeature() == VDiffmergePackage.eINSTANCE.getDiffAttachment_MergedDiffs()) {
							updateButton(diffButton, (VDiffAttachment) attachment);
						}
					}

				});
				updateButton(diffButton, (VDiffAttachment) attachment);
				break;
			}
		}
	}

	private void updateButton(Button diffButton, VDiffAttachment attachment) {
		if (attachment.getMergedDiffs() == 0) {
			diffButton.setImage(Activator.getImage("icons/lightning.png")); //$NON-NLS-1$
		} else {
			diffButton.setImage(Activator.getImage("icons/accept.png")); //$NON-NLS-1$
		}
	}

	private void openDiffDialog(DiffMergeModelContext diffModelContext, VControl vControl) {

		final Setting setting = vControl.getDomainModelReference().getIterator().next();
		if (setting == null) {
			return;
		}
		final IItemPropertyDescriptor itemPropertyDescriptor = getItemPropertyDescriptor(setting);
		final String label = itemPropertyDescriptor.getDisplayName(setting.getEObject());

		DiffDialogHelper.showDialog(diffModelContext, vControl, label);

	}

	private IItemPropertyDescriptor getItemPropertyDescriptor(Setting setting) {
		final ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
			new ReflectiveItemProviderAdapterFactory(),
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		final AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
			composedAdapterFactory);

		final IItemPropertyDescriptor descriptor = adapterFactoryItemDelegator.getPropertyDescriptor(
			setting.getEObject(),
			setting.getEStructuralFeature());
		composedAdapterFactory.dispose();
		return descriptor;

	}

}
