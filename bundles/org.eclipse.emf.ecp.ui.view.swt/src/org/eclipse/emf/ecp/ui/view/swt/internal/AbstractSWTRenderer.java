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

import org.eclipse.emf.ecp.edit.internal.swt.util.DoubleColumnRow;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTRenderingHelper;
import org.eclipse.emf.ecp.edit.internal.swt.util.SingleColumnRow;
import org.eclipse.emf.ecp.internal.ui.view.renderer.LayoutHelper;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.view.model.VElement;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/**
 * Common base class for all SWT specific renderer classes.
 * 
 * @author emueller
 * 
 * @param <R> the actual type of the {@link VElement} to be drawn
 */
public abstract class AbstractSWTRenderer<R extends VElement> implements SWTRenderer<R> {

	protected org.eclipse.swt.widgets.Composite getParentFromInitData(Object[] initData) {
		return (Composite) initData[0];
	}

	public abstract List<RenderingResultRow<Control>> render(R vElement,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption;

	/**
	 * @param resultRows
	 */
	protected void setLayoutDataForResultRows(final List<RenderingResultRow<Control>> resultRows) {
		for (final RenderingResultRow<Control> row : resultRows) {
			if (SingleColumnRow.class.isInstance(row)) {
				((SingleColumnRow) row).getControl().setLayoutData(getLayoutHelper().getSpanningLayoutData(2, 1));
			}
			else if (DoubleColumnRow.class.isInstance(row)) {
				((DoubleColumnRow) row).getLeftControl().setLayoutData(getLayoutHelper().getLeftColumnLayoutData());
				((DoubleColumnRow) row).getRightControl().setLayoutData(getLayoutHelper().getRightColumnLayoutData());
			}
		}
	}

	/**
	 * @param categoryComposite
	 * @param rowFactory
	 * @return
	 */
	protected List<RenderingResultRow<Control>> createResult(
		final Control... controls) {
		final List<RenderingResultRow<Control>> result = new ArrayList<RenderingResultRow<Control>>();

		result.add(SWTRenderingHelper.INSTANCE.getResultRowFactory()
			.createRenderingResultRow(controls));
		return result;
	}

	protected LayoutHelper<Layout> getLayoutHelper() {
		return SWTRenderingHelper.INSTANCE.getLayoutHelper();
	}
}
