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

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.edit.internal.swt.table.TableColumnConfiguration;
import org.eclipse.emf.ecp.edit.internal.swt.table.TableControlConfiguration;
import org.eclipse.emf.ecp.internal.ui.view.Activator;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.ui.view.swt.internal.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.table.model.VTableColumn;
import org.eclipse.emf.ecp.view.table.model.VTableControl;
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
// APITODO
@SuppressWarnings("restriction")
public class SWTTableControlRenderer extends AbstractSWTRenderer<VTableControl> {
	/**
	 * Singleton instance of the swt table control renderer.
	 */
	public static final SWTTableControlRenderer INSTANCE = new SWTTableControlRenderer();

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.AbstractSWTRenderer#renderSWT(org.eclipse.emf.ecp.internal.ui.view.renderer.Node,
	 *      org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator, java.lang.Object[])
	 */
	@Override
	public List<RenderingResultRow<Control>> render(VTableControl vTableControl,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		final Iterator<Setting> settings = vTableControl.getDomainModelReference().getIterator();
		if (!settings.hasNext()) {
			return null;
		}
		final Setting setting = settings.next();
		if (setting.getEStructuralFeature() == null) {
			return null;
		}
		final EClass dataClass = setting.getEStructuralFeature().getEContainingClass();

		if (dataClass == null) {
			return null;
		}

		final IItemPropertyDescriptor itemPropertyDescriptor = adapterFactoryItemDelegator
			.getPropertyDescriptor(setting.getEObject(), setting.getEStructuralFeature());
		if (itemPropertyDescriptor == null) {
			return null;
		}

		final TableControlConfiguration tcc = new TableControlConfiguration();
		tcc.setAddRemoveDisabled(vTableControl.isAddRemoveDisabled());

		for (final VTableColumn column : vTableControl.getColumns()) {
			tcc.getColumns().add(
				new TableColumnConfiguration(column.isReadOnly(), column.getAttribute()));
		}

		final org.eclipse.emf.ecp.edit.internal.swt.controls.TableControl control = new org.eclipse.emf.ecp.edit.internal.swt.controls.TableControl();
		control.setTableControlConfiguration(tcc);
		control.init(null, vTableControl.getDomainModelReference());

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

			label.setText(itemPropertyDescriptor.getDisplayName(null)
				+ extra);
			label.setToolTipText(itemPropertyDescriptor.getDescription(null));
		}

		final Composite controlComposite = control.createControl(parent);
		controlComposite.setBackground(parent.getBackground());

		control.setEditable(!vTableControl.isReadonly());

		if (label == null) {
			return createResult(controlComposite);
		}
		Activator.getDefault().ungetECPControlFactory();
		return createResult(label, controlComposite);
	}
}
