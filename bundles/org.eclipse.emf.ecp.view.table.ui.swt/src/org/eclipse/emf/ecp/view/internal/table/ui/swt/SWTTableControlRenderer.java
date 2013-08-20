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
package org.eclipse.emf.ecp.view.internal.table.ui.swt;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.table.TableColumnConfiguration;
import org.eclipse.emf.ecp.edit.internal.swt.table.TableControlConfiguration;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.swt.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.table.model.TableColumn;
import org.eclipse.emf.ecp.view.table.model.TableControl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * SWT Renderer for Table Control.
 * 
 * @author Eugen Neufeld
 * 
 */
public class SWTTableControlRenderer extends AbstractSWTRenderer<TableControl> {
	/**
	 * Singleton instance of the swt table control renderer.
	 */
	public static final SWTTableControlRenderer INSTANCE = new SWTTableControlRenderer();

	@Override
	public List<RenderingResultRow<Control>> renderSWT(Node<TableControl> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		final TableControl modelTableControl = node.getRenderable();
		final ECPControlContext subContext = node.getControlContext();
		if (modelTableControl.getTargetFeature() == null) {
			return null;
		}
		final EClass dataClass = modelTableControl.getTargetFeature().getEContainingClass();

		if (dataClass == null) {
			return null;
		}

		final IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator
			.getPropertyDescriptor(subContext.getModelElement(),

				modelTableControl.getTargetFeature());
		if (itemPropertyDescriptor == null) {
			return null;
		}

		final TableControlConfiguration tcc = new TableControlConfiguration();
		tcc.setAddRemoveDisabled(modelTableControl.isAddRemoveDisabled());

		for (final TableColumn column : modelTableControl.getColumns()) {
			tcc.getColumns().add(
				new TableColumnConfiguration(column.isReadOnly(), column.getAttribute()));
		}

		final org.eclipse.emf.ecp.edit.internal.swt.controls.TableControl control = new org.eclipse.emf.ecp.edit.internal.swt.controls.TableControl(
			false, itemPropertyDescriptor,
			(EStructuralFeature) itemPropertyDescriptor.getFeature(subContext
				.getModelElement()), subContext, false, tcc);

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
				label.setToolTipText(itemPropertyDescriptor.getDescription(subContext
					.getModelElement()));
			}

			final Composite controlComposite = control.createControl(parent);
			controlComposite.setBackground(parent.getBackground());

			controlComposite.setEnabled(!modelTableControl.isReadonly());

			if (label == null) {
				node.addRenderingResultDelegator(withSWTControls(control, modelTableControl, controlComposite));
			} else {
				node.addRenderingResultDelegator(withSWTControls(control, modelTableControl, controlComposite, label));
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
