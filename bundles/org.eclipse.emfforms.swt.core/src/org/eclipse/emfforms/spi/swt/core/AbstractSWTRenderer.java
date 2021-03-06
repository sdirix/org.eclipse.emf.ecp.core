/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
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
package org.eclipse.emfforms.spi.swt.core;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecp.view.model.common.AbstractRenderer;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeListener;
import org.eclipse.emf.ecp.view.spi.model.ModelChangeNotification;
import org.eclipse.emf.ecp.view.spi.model.VDiagnostic;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emfforms.spi.common.report.ReportService;
import org.eclipse.emfforms.spi.swt.core.layout.EMFFormsSWTLayoutUtil;
import org.eclipse.emfforms.spi.swt.core.layout.SWTGridCell;
import org.eclipse.emfforms.spi.swt.core.layout.SWTGridDescription;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Common base class for all SWT specific renderer classes.
 *
 * A renderer using other renderers to render its contents must call this methods in this order:
 *
 * <pre>
 *  {@link #getGridDescription(SWTGridDescription)}
 *  for each SWTGridCell
 *  	{@link #render(SWTGridCell, Composite)}
 * {@link #finalizeRendering(Composite)}
 * </pre>
 *
 * If you don't call {@link #finalizeRendering(Composite)} after the rendering, the automatic disposing of the renderer
 * will not work, as well as the initial validation check.
 *
 * @author Eugen Neufeld
 *
 * @param <VELEMENT> the actual type of the {@link VElement} to be drawn
 * @since 1.2
 */
public abstract class AbstractSWTRenderer<VELEMENT extends VElement> extends AbstractRenderer<VELEMENT> {

	/**
	 * Variant constant for indicating RAP controls.
	 */
	protected static final String CUSTOM_VARIANT = "org.eclipse.rap.rwt.customVariant"; //$NON-NLS-1$
	private ModelChangeListener listener;
	private Map<SWTGridCell, Control> controls;
	private boolean renderingFinished;

	/**
	 * Default Constructor.
	 *
	 * @param vElement the view element to be rendered
	 * @param viewContext The view model context
	 * @param reportService the ReportService to use
	 * @since 1.6
	 */
	public AbstractSWTRenderer(final VELEMENT vElement, final ViewModelContext viewContext,
		ReportService reportService) {
		super(vElement, viewContext, reportService);
	}

	/**
	 * Returns the GridDescription for this Renderer.
	 *
	 * @param gridDescription the current {@link SWTGridDescription}
	 * @return the number of controls per row
	 * @since 1.3
	 */
	public abstract SWTGridDescription getGridDescription(SWTGridDescription gridDescription);

	/**
	 * Initializes the {@link AbstractSWTRenderer}.
	 *
	 * @since 1.6
	 */
	public final void init() {
		preInit();
		controls = new LinkedHashMap<SWTGridCell, Control>();
		if (getViewModelContext() != null) {
			listener = new ModelChangeListener() {

				@Override
				public void notifyChange(ModelChangeNotification notification) {
					if (!renderingFinished) {
						return;
					}
					if (notification.getRawNotification().isTouch()) {
						return;
					}
					if (notification.getNotifier() != getVElement()) {
						return;
					}
					if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Visible()) {
						applyVisible();
					}
					if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Enabled()
						&& !getVElement().isReadonly()) {
						applyEnable();
					}
					if (notification.getStructuralFeature() == VViewPackage.eINSTANCE
						.getElement_Diagnostic()) {
						final VDiagnostic newDia = (VDiagnostic) notification.getRawNotification().getNewValue();
						final VDiagnostic oldDia = (VDiagnostic) notification.getRawNotification().getOldValue();
						applyValidation(oldDia, newDia);
						applyValidation();
					}
				}

			};
			getViewModelContext().registerViewChangeListener(listener);
		}
		getViewModelContext().addContextUser(this);

		postInit();
	}

	/**
	 * Returns a copy of the {@link GridCell} to {@link Control} map.
	 *
	 * @return a copy of the controls map
	 * @since 1.3
	 */
	protected final Map<SWTGridCell, Control> getControls() {
		if (controls == null) {
			return Collections.emptyMap();
		}
		return new LinkedHashMap<SWTGridCell, Control>(controls);
	}

	/**
	 * Use this method to initialize objects which are needed already before rendering.
	 *
	 * @since 1.3
	 */
	protected void preInit() {

	}

	/**
	 * Use this method to initialize objects which are needed during rendering.
	 *
	 * @since 1.3
	 */
	protected void postInit() {

	}

	/**
	 * Disposes all resources used by the renderer.
	 * Don't forget to call super.dispose if overwriting this method.
	 *
	 * @since 1.3
	 */
	@Override
	protected void dispose() {
		if (getViewModelContext() != null) {
			getViewModelContext().unregisterViewChangeListener(listener);
		}
		listener = null;
		controls = null;
		getViewModelContext().removeContextUser(this);

		super.dispose();
	}

	/**
	 * Renders the passed {@link VElement}.
	 *
	 * @param cell the {@link SWTGridCell} of the control to render
	 * @param parent the {@link Composite} to render on
	 * @return the rendered {@link Control}
	 * @throws NoRendererFoundException this is thrown when a renderer cannot be found
	 * @throws NoPropertyDescriptorFoundExeption this is thrown when no property descriptor can be found
	 * @since 1.3
	 */
	public Control render(final SWTGridCell cell, Composite parent)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

		Control control = controls.get(cell);
		if (control != null) {
			return control;
		}

		control = renderControl(cell, parent);
		if (control == null) {
			// something went wrong, log
			return null;
		}
		controls.put(cell, control);

		// register dispose listener to rerender if disposed
		control.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (controls != null) {
					controls.remove(cell);
				}
			}
		});

		return control;
	}

	/**
	 * Called by the framework to initialize listener.
	 *
	 * @param parent the parent used during render
	 * @since 1.3
	 */
	public void finalizeRendering(Composite parent) {
		if (renderingFinished) {
			return;
		}
		renderingFinished = true;
		if (!getVElement().isVisible()) {
			/* convention is to render visible, so only call apply if we are invisible */
			applyVisible();
		}
		applyReadOnly();
		if (!getVElement().isReadonly()) {
			applyEnable();
		}
		applyValidation();
		parent.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent event) {
				dispose();
			}
		});
	}

	/**
	 * Renders the passed {@link VElement}.
	 *
	 * @param cell the {@link GridCell} of the control to render
	 * @param parent the {@link Composite} to render on
	 * @return the rendered {@link Control}
	 * @throws NoRendererFoundException this is thrown when a renderer cannot be found
	 * @throws NoPropertyDescriptorFoundExeption this is thrown when no property descriptor can be found
	 * @since 1.3
	 */
	protected abstract Control renderControl(SWTGridCell cell, Composite parent) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption;

	/**
	 * Marks a controls as readonly.
	 *
	 * @since 1.3
	 *
	 */
	protected void applyReadOnly() {
		for (final SWTGridCell gridCell : controls.keySet()) {
			setControlEnabled(gridCell, controls.get(gridCell), !getVElement().isReadonly());
		}
	}

	/**
	 * Allows implementers to set a control to enabled.
	 *
	 * @since 1.3
	 *
	 */
	protected void applyEnable() {
		for (final SWTGridCell gridCell : controls.keySet()) {
			setControlEnabled(gridCell, controls.get(gridCell), getVElement().isEnabled());
		}
	}

	/**
	 * Wraps the call to enable/disable a control.
	 *
	 * @param gridCell the {@link SWTGridCell} to enable/disable
	 * @param control the {@link Control} to enable/disable
	 * @param enabled true if control should be enabled, false otherwise
	 * @since 1.3
	 */
	protected void setControlEnabled(SWTGridCell gridCell, Control control, boolean enabled) {
		control.setEnabled(enabled);
	}

	/**
	 * Allows implementers to check and set the visibility on the whole result row.
	 *
	 * @since 1.3
	 *
	 */
	protected void applyVisible() {
		final boolean visible = getVElement().isVisible();
		/* avoid multiple layout calls by saving the parents which need to be relayouted */
		final Set<Composite> parents = new LinkedHashSet<Composite>();
		for (final SWTGridCell gridCell : controls.keySet()) {
			final Object layoutData = controls.get(gridCell).getLayoutData();
			if (GridData.class.isInstance(layoutData)) {
				final GridData gridData = (GridData) layoutData;
				if (gridData != null) {
					gridData.exclude = !visible;
				}
			}
			controls.get(gridCell).setVisible(visible);
			parents.add(controls.get(gridCell).getParent());
		}
		for (final Composite composite : parents) {
			EMFFormsSWTLayoutUtil.adjustParentSize(composite.getChildren()[0]);
		}
	}

	/**
	 * Allows implementers to display the validation state of the control.
	 * The default implementation does nothing.
	 *
	 * @since 1.3
	 */
	protected void applyValidation() {

	}

	/**
	 * Called before the {@link #applyValidation()}. This method allows to create a diff between the old diagnostic and
	 * the new diagnostic and thus improve the performance of the overlay apply by triggering it only on the relevant
	 * elements.
	 *
	 * @param oldDiagnostic The previous {@link VDiagnostic}
	 * @param newDiagnostic The current {@link VDiagnostic}
	 * @since 1.14
	 */
	protected void applyValidation(VDiagnostic oldDiagnostic, VDiagnostic newDiagnostic) {

	}

	/**
	 * @return String the default font name on the system.
	 * @param control The control to derive the default font name from
	 *
	 * @since 1.5
	 */
	protected String getDefaultFontName(Control control) {
		return control.getDisplay().getSystemFont().getFontData()[0].getName();
	}

}
