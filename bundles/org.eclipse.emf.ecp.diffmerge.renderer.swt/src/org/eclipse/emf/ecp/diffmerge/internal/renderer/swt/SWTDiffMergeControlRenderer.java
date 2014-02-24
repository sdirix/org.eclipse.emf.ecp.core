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
package org.eclipse.emf.ecp.diffmerge.internal.renderer.swt;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.diffmerge.spi.context.DiffMergeModelContext;
import org.eclipse.emf.ecp.diffmerge.swt.DiffDialogHelper;
import org.eclipse.emf.ecp.edit.spi.ECPAbstractControl;
import org.eclipse.emf.ecp.spi.diffmerge.model.VDiffAttachment;
import org.eclipse.emf.ecp.spi.diffmerge.model.VDiffmergePackage;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.core.swt.SWTControlRenderer;
import org.eclipse.emf.ecp.view.spi.model.VAttachment;
import org.eclipse.emf.ecp.view.spi.model.VControl;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A specific DiffMerge Control Renderer.
 * 
 * @author Eugen Neufeld
 * 
 */
public class SWTDiffMergeControlRenderer extends SWTControlRenderer {

	@Override
	protected Control createLabelControl(Composite parent, final VControl vControl, final ECPAbstractControl control,
		final ViewModelContext viewContext)
		throws NoPropertyDescriptorFoundExeption {
		final DiffMergeModelContext diffModelContext = (DiffMergeModelContext) viewContext;
		final Composite labelDiffComposite = new Composite(parent, SWT.NONE);
		labelDiffComposite.setBackground(parent.getBackground());
		int numColumns = 1;
		final Control label = super.createLabelControl(labelDiffComposite, vControl, control, viewContext);
		if (label != null) {
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(label);
			numColumns++;
		}
		GridLayoutFactory.fillDefaults().numColumns(numColumns).equalWidth(false).applyTo(labelDiffComposite);

		final Button diffButton = new Button(labelDiffComposite, SWT.PUSH);
		diffButton.setText(Messages.getString("SWTDiffMergeControlRenderer.DiffButton")); //$NON-NLS-1$
		diffButton.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_compare_button"); //$NON-NLS-1$
		diffButton.addSelectionListener(new SelectionAdapter() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				openDiffDialog(diffModelContext, vControl, control);
			}

		});
		for (final VAttachment attachment : vControl.getAttachments()) {
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

		return labelDiffComposite;
	}

	private void updateButton(Button diffButton, VDiffAttachment attachment) {
		if (attachment.getMergedDiffs() == 0) {
			diffButton.setImage(Activator.getImage("icons/lightning.png")); //$NON-NLS-1$
		} else {
			diffButton.setImage(Activator.getImage("icons/accept.png")); //$NON-NLS-1$
		}
	}

	private void openDiffDialog(DiffMergeModelContext diffModelContext, VControl vControl,
		ECPAbstractControl ecpControl) {

		final Setting setting = ecpControl.getFirstSetting();
		if (setting == null) {
			return;
		}
		final IItemPropertyDescriptor itemPropertyDescriptor = ecpControl.getItemPropertyDescriptor(setting);
		final String label = itemPropertyDescriptor.getDisplayName(setting.getEObject());

		DiffDialogHelper.showDialog(diffModelContext, vControl, label);

	}
}
