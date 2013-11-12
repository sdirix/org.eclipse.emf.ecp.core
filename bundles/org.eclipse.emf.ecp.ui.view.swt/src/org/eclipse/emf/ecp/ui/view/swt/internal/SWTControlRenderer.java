/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edagr Mueller - initial API and implementation
 * Eugen Neufeld - Refactoring
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.edit.internal.swt.util.ECPControlSWT;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTRenderingHelper;
import org.eclipse.emf.ecp.edit.spi.ECPAbstractControl;
import org.eclipse.emf.ecp.edit.spi.ECPControlFactory;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.view.model.LabelAlignment;
import org.eclipse.emf.ecp.view.model.VControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Renderer for {@link org.eclipse.swt.widgets.Control Controls}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class SWTControlRenderer extends AbstractSWTRenderer<VControl> {
	/**
	 * Instance field to access this renderer as a singleton.
	 */
	public static final SWTControlRenderer INSTANCE = new SWTControlRenderer();

	@Override
	public List<RenderingResultRow<org.eclipse.swt.widgets.Control>> renderSWT(Node<VControl> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		final VControl modelControl = node.getRenderable();

		final ECPControlFactory controlFactory = Activator.getDefault().getECPControlFactory();

		if (controlFactory == null) {
			Activator.getDefault().ungetECPControlFactory();
			return null;
		}

		final ECPAbstractControl control = controlFactory.createControl(ECPAbstractControl.class,
			modelControl.getDomainModelReference());

		if (control != null) {
			control.init(node.getControlContext(), modelControl.getDomainModelReference());
			final Composite parent = getParentFromInitData(initData);
			Label label = null;
			if (control.showLabel() && modelControl.getLabelAlignment() == LabelAlignment.LEFT) {
				final Setting setting = modelControl.getDomainModelReference().getIterator().next();
				final IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator
					.getPropertyDescriptor(setting.getEObject(),
						setting.getEStructuralFeature());

				if (itemPropertyDescriptor == null) {
					throw new NoPropertyDescriptorFoundExeption(setting.getEObject(), setting.getEStructuralFeature());
				}

				label = new Label(parent, SWT.NONE);
				label.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_label"); //$NON-NLS-1$
				label.setBackground(parent.getBackground());
				String extra = ""; //$NON-NLS-1$
				if (setting.getEStructuralFeature().getLowerBound() > 0) {
					extra = "*"; //$NON-NLS-1$
				}
				final String labelText = itemPropertyDescriptor.getDisplayName(setting.getEObject());
				if (labelText != null && labelText.trim().length() != 0) {
					label.setText(labelText + extra);
					label.setToolTipText(itemPropertyDescriptor.getDescription(setting.getEObject()));
				}

			}

			final List<RenderingResultRow<org.eclipse.swt.widgets.Control>> createControls = ((ECPControlSWT) control)
				.createControls(parent);
			if (createControls == null) {
				return null;
			}
			control.setEditable(!modelControl.isReadonly());
			List<RenderingResultRow<org.eclipse.swt.widgets.Control>> result = new ArrayList<RenderingResultRow<org.eclipse.swt.widgets.Control>>();
			final Control next = createControls.iterator().next().getControls().iterator().next();
			if (label != null) {
				result.add(SWTRenderingHelper.INSTANCE.getResultRowFactory()
					.createRenderingResultRow(label, next));
			}
			else {
				result = createControls;
			}

			if (label == null) {
				node.addRenderingResultDelegator(withSWTControls(control, modelControl, next));
			} else {
				node.addRenderingResultDelegator(withSWTControls(control, modelControl, next, label));
			}
			return result;

		}

		Activator.getDefault().ungetECPControlFactory();

		return null;
	}

}
