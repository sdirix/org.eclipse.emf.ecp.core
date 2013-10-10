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
package org.eclipse.emf.ecp.ui.view.swt;

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.ECPControlFactory;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Renderer for {@link org.eclipse.swt.widgets.Control Controls}.
 * 
 * @author Eugen Neufeld
 * 
 */
public class SWTControlRenderer extends AbstractSWTRenderer<Control> {
	/**
	 * Instance field to access this renderer as a singleton.
	 */
	public static final SWTControlRenderer INSTANCE = new SWTControlRenderer();

	@Override
	public List<RenderingResultRow<org.eclipse.swt.widgets.Control>> renderSWT(Node<Control> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		final Control modelControl = node.getRenderable();
		final Setting setting = modelControl.getDomainModelReferences().get(0).getIterator().next();
		final ECPControlContext subContext = node.getControlContext();

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

		if (control != null) {
			final Composite parent = getParentFromInitData(initData);
			Label label = null;
			if (control.showLabel()) {
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

			}

			final Composite controlComposite = control.createControl(parent);
			// controlComposite.setEnabled(!modelControl.isReadonly());
			control.setEditable(!modelControl.isReadonly());
			controlComposite.setBackground(parent.getBackground());

			if (label == null) {
				node.addRenderingResultDelegator(withSWTControls(control, modelControl, controlComposite));
			} else {
				node.addRenderingResultDelegator(withSWTControls(control, modelControl, controlComposite, label));
			}

			if (label == null) {
				return createResult(controlComposite);
			}
			return createResult(label, controlComposite);

		}

		Activator.getDefault().ungetECPControlFactory();

		return null;
	}

}
