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

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecp.edit.internal.swt.util.DoubleColumnRow;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTRenderingHelper;
import org.eclipse.emf.ecp.edit.internal.swt.util.SingleColumnRow;
import org.eclipse.emf.ecp.internal.ui.view.renderer.LayoutHelper;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.view.context.ModelChangeNotification;
import org.eclipse.emf.ecp.view.context.ViewModelContext;
import org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener;
import org.eclipse.emf.ecp.view.model.VElement;
import org.eclipse.emf.ecp.view.model.VViewPackage;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
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
public abstract class AbstractSWTRenderer<R extends VElement> {

	/**
	 * Variant constant for indicating RAP controls.
	 */
	protected static final String CUSTOM_VARIANT = "org.eclipse.rap.rwt.customVariant"; //$NON-NLS-1$

	/**
	 * Renders the passed {@link VElement}.
	 * 
	 * @param parent the {@link Composite} to render on
	 * @param vElement the {@link VElement} to render
	 * @param viewContext the {@link ViewModelContext} to use
	 * @return a list of {@link RenderingResultRow}
	 * @throws NoRendererFoundException
	 * @throws NoPropertyDescriptorFoundExeption
	 */
	public final List<RenderingResultRow<Control>> render(Composite parent, final R vElement,
		final ViewModelContext viewContext)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final List<RenderingResultRow<Control>> result = renderModel(parent, vElement, viewContext);
		if (result == null) {
			return null;
		}
		final ModelChangeListener listener = new ModelChangeListener() {

			public void notifyRemove(Notifier notifier) {
				// TODO Auto-generated method stub

			}

			public void notifyChange(ModelChangeNotification notification) {
				if (notification.getNotifier() != vElement) {
					return;
				}
				if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Enabled()
					&& !vElement.isReadonly()) {
					applyEnable(vElement, result);
				}
			}

			public void notifyAdd(Notifier notifier) {
				// TODO Auto-generated method stub

			}
		};
		viewContext.registerViewChangeListener(listener);
		parent.addDisposeListener(new DisposeListener() {

			private static final long serialVersionUID = 1L;

			public void widgetDisposed(DisposeEvent e) {
				viewContext.unregisterViewChangeListener(listener);
			}
		});
		applyVisible(vElement, result);
		applyReadOnly(vElement, result);
		if (!vElement.isReadonly()) {
			applyEnable(vElement, result);
		}

		return result;
	}

	private static void applyReadOnly(VElement vElement, List<RenderingResultRow<Control>> resultRows) {
		for (final RenderingResultRow<Control> row : resultRows) {
			for (final Control control : row.getControls()) {
				control.setEnabled(!vElement.isReadonly());
			}
		}
	}

	private static void applyEnable(VElement vElement, List<RenderingResultRow<Control>> resultRows) {
		for (final RenderingResultRow<Control> row : resultRows) {
			for (final Control control : row.getControls()) {
				control.setEnabled(vElement.isEnabled());
			}
		}
	}

	private static void applyVisible(VElement vElement, List<RenderingResultRow<Control>> resultRows) {
		for (final RenderingResultRow<Control> row : resultRows) {
			for (final Control control : row.getControls()) {
				final Object layoutData = control.getLayoutData();
				if (GridData.class.isInstance(layoutData)) {
					final GridData gridData = (GridData) layoutData;
					if (gridData != null) {
						gridData.exclude = false;
					}
				}
				control.setVisible(vElement.isVisible());
			}
		}
	}

	/**
	 * Renders the passed {@link VElement}.
	 * 
	 * @param parent the {@link Composite} to render on
	 * @param vElement the {@link VElement} to render
	 * @param viewContext the {@link ViewModelContext} to use
	 * @return a list of {@link RenderingResultRow}
	 * @throws NoRendererFoundException
	 * @throws NoPropertyDescriptorFoundExeption
	 */
	protected abstract List<RenderingResultRow<Control>> renderModel(Composite parent, R vElement,
		ViewModelContext viewContext) throws NoRendererFoundException, NoPropertyDescriptorFoundExeption;

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
