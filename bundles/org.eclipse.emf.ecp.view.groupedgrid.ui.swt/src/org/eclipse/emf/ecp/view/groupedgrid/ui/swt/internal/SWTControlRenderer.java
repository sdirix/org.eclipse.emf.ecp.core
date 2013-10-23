/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen Neufeld - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.groupedgrid.ui.swt.internal;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.edit.spi.ECPControlFactory;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.swt.internal.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.model.Alignment;
import org.eclipse.emf.ecp.view.model.VControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * The AbstractSWTRenderer of an SWTControlRenderer.
 * 
 * @author Eugen Neufeld
 * 
 */
public class SWTControlRenderer extends AbstractSWTRenderer<VControl> {
	/**
	 * The instance of the SWTControlRenderer.
	 */
	public static final SWTControlRenderer INSTANCE = new SWTControlRenderer();
	private static final int IDENT = 10;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.internal.AbstractSWTRenderer#renderSWT(org.eclipse.emf.ecp.internal.ui.view.renderer.Node,
	 *      org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator, java.lang.Object[])
	 */
	@Override
	public List<RenderingResultRow<org.eclipse.swt.widgets.Control>> renderSWT(Node<VControl> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator, Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		final VControl modelControl = node.getRenderable();
		final Setting setting = modelControl.getDomainModelReference().getIterator().next();
		final EClass dataClass = setting.getEStructuralFeature().getEContainingClass();
		final ECPControlContext subContext = node.getControlContext();

		if (dataClass == null) {
			return null;
		}

		final IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator
			.getPropertyDescriptor(setting.getEObject(),
				setting.getEStructuralFeature());

		if (itemPropertyDescriptor == null) {
			throw new NoPropertyDescriptorFoundExeption(setting.getEObject(), setting.getEStructuralFeature());
		}

		final ECPControlFactory controlFactory = Activator.getDefault().getECPControlFactory();

		if (controlFactory == null) {
			Activator.getDefault().ungetECPControlFactory();
			return null;
		}

		final SWTControl control = controlFactory.createControl(SWTControl.class, itemPropertyDescriptor,
			subContext);

		return doRender(node, modelControl, subContext, itemPropertyDescriptor, control, initData);
	}

	private List<RenderingResultRow<org.eclipse.swt.widgets.Control>> doRender(Node<VControl> node,
		final VControl modelControl, final ECPControlContext subContext,
		final IItemPropertyDescriptor itemPropertyDescriptor, final SWTControl control, Object... initData) {
		if (control != null) {
			final Composite parent = getParentFromInitData(initData);
			int numControl = 2;
			Label label = null;
			// if (modelControl.getLabelAlignment() == Alignment.TOP) {
			// final Composite newParent = new Composite(parent, SWT.NONE);
			// newParent.setBackground(parent.getBackground());
			// parent = newParent;
			// GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(true).applyTo(parent);
			// }
			if (modelControl.getLabelAlignment() == Alignment.LEFT) {
				numControl = 1;
				label = new Label(parent, SWT.NONE);
				label.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_label");
				label.setBackground(parent.getBackground());
				String extra = "";
				if (((EStructuralFeature) itemPropertyDescriptor.getFeature(null)).getLowerBound() > 0) {
					extra = "*";
				}
				label.setText(itemPropertyDescriptor.getDisplayName(subContext.getModelElement())
					+ extra);
				label.setToolTipText(itemPropertyDescriptor.getDescription(subContext.getModelElement()));
				GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(label);

				GridDataFactory.createFrom((GridData) label.getLayoutData()).indent(IDENT, 0).applyTo(label);
			}

			final Composite controlComposite = control.createControl(parent);
			controlComposite.setEnabled(!modelControl.isReadonly());
			controlComposite.setBackground(parent.getBackground());

			if (label == null) {
				node.addRenderingResultDelegator(withSWTControls(control, modelControl, controlComposite));
			} else {
				node.addRenderingResultDelegator(withSWTControls(control, modelControl, controlComposite, label));
			}

			// if (!node.isVisible()) {
			// node.show(false);
			// }

			GridDataFactory.fillDefaults().grab(true, false)
				.align(SWT.FILL, SWT.CENTER).span(numControl, 1)
				.applyTo(controlComposite);

			// if (modelControl.getLabelAlignment() == Alignment.TOP) {
			// return createResult(parent);
			// }
			// else
			if (label == null) {
				return createResult(controlComposite);
			}
			return createResult(label, controlComposite);
		}

		Activator.getDefault().ungetECPControlFactory();

		return null;
	}
}
