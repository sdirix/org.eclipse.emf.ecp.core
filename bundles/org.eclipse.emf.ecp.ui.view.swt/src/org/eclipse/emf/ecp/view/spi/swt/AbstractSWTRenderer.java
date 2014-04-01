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
package org.eclipse.emf.ecp.view.spi.swt;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecp.view.internal.swt.SWTRendererFactoryImpl;
import org.eclipse.emf.ecp.view.spi.context.ModelChangeNotification;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext.ModelChangeListener;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.layout.GridCell;
import org.eclipse.emf.ecp.view.spi.swt.layout.GridDescription;
import org.eclipse.emf.ecp.view.spi.swt.layout.LayoutProviderHelper;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Common base class for all SWT specific renderer classes. {@link #init(VElement, ViewModelContext)} is called by the
 * framework when providing the renderer. You don't need to call this.
 * 
 * A renderer using other renderers to render its contents must call this methods in this order:
 * 
 * <pre>
 *  {@link #getGridDescription(GridDescription)}
 *  for each GridCell
 *  	{@link #render(GridCell, Composite)}
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

	private ModelChangeListener listener;
	private Map<GridCell, Control> controls;
	private SWTRendererFactory rendererFactory;
	private boolean renderingFinished;

	/**
	 * Default constructor.
	 */
	public AbstractSWTRenderer() {
		this(new SWTRendererFactoryImpl());
	}

	/**
	 * Constructor for testing purpose.
	 * 
	 * @param factory the factory to use
	 * @since 1.3
	 */
	protected AbstractSWTRenderer(SWTRendererFactory factory) {
		this.rendererFactory = factory;
	}

	/**
	 * Returns the GridDescription for this Renderer.
	 * 
	 * @param gridDescription the current {@link GridDescription}
	 * @return the number of controls per row
	 * @since 1.3
	 */
	public abstract GridDescription getGridDescription(GridDescription gridDescription);

	@Override
	public final void init(final VELEMENT vElement, final ViewModelContext viewContext) {
		super.init(vElement, viewContext);
		preInit();
		controls = new LinkedHashMap<GridCell, Control>();
		if (getViewModelContext() != null) {
			listener = new ModelChangeListener() {

				@Override
				public void notifyRemove(Notifier notifier) {
					// nothing to do
				}

				@Override
				public void notifyChange(ModelChangeNotification notification) {
					if (!renderingFinished) {
						return;
					}
					if (notification.getNotifier() != getVElement()) {
						return;
					}
					if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Visible()) {
						applyVisible();
					}
					if (notification.getStructuralFeature() == VViewPackage.eINSTANCE.getElement_Enabled()
						&& !vElement.isReadonly()) {
						applyEnable();
					}
					if (notification.getStructuralFeature() == VViewPackage.eINSTANCE
						.getElement_Diagnostic()) {
						applyValidation();
					}
				}

				@Override
				public void notifyAdd(Notifier notifier) {
					// nothing to do
				}
			};
			getViewModelContext().registerViewChangeListener(listener);
		}
		postInit();
	}

	/**
	 * Returns a copy of the {@link GridCell} to {@link Control} map.
	 * 
	 * @return a copy of the controls map
	 * @since 1.3
	 */
	protected final Map<GridCell, Control> getControls() {
		if (controls == null) {
			return Collections.emptyMap();
		}
		return new LinkedHashMap<GridCell, Control>(controls);
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
	 */
	@Override
	protected void dispose() {
		getViewModelContext().unregisterViewChangeListener(listener);
		listener = null;
		controls = null;
		super.dispose();
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
	public Control render(final GridCell cell, Composite parent)
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
	public final void finalizeRendering(Composite parent) {
		renderingFinished = true;
		applyVisible();
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
	protected abstract Control renderControl(final GridCell cell, Composite parent) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption;

	/**
	 * Marks a controls as readonly.
	 * 
	 * @since 1.3
	 * 
	 */
	protected void applyReadOnly() {
		for (final GridCell gridCell : controls.keySet()) {
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
		for (final GridCell gridCell : controls.keySet()) {
			setControlEnabled(gridCell, controls.get(gridCell), getVElement().isEnabled());
		}
	}

	/**
	 * Wraps the call to enable/disable a control.
	 * 
	 * @param gridCell the {@link GridCell} to enable/disable
	 * @param control the {@link Control} to enable/disable
	 * @param enabled true if control should be enabled, false otherwise
	 * @since 1.3
	 */
	protected void setControlEnabled(GridCell gridCell, Control control, boolean enabled) {
		control.setEnabled(enabled);
	}

	/**
	 * Allows implementers to check and set the visibility on the whole result row.
	 * 
	 * @since 1.3
	 * 
	 */
	protected void applyVisible() {
		for (final GridCell gridCell : controls.keySet()) {
			final Object layoutData = controls.get(gridCell).getLayoutData();
			if (GridData.class.isInstance(layoutData)) {
				final GridData gridData = (GridData) layoutData;
				if (gridData != null) {
					gridData.exclude = false;
				}
			}
			controls.get(gridCell).setVisible(getVElement().isVisible());
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
	 * Sets the LayoutData for the specified control.
	 * 
	 * @param gridCell the {@link GridCell} used to render the control
	 * @param gridDescription the {@link GridDescription} of the parent which rendered the control
	 * @param currentRowGridDescription the {@link GridDescription} of the current row
	 * @param fullGridDescription the {@link GridDescription} of the whole container
	 * @param vElement the {@link VElement} to set the layoutData for
	 * @param control the control to set the layout to
	 * @since 1.3
	 */
	protected void setLayoutDataForControl(GridCell gridCell, GridDescription gridDescription,
		GridDescription currentRowGridDescription, GridDescription fullGridDescription, VElement vElement,
		Control control) {

		control.setLayoutData(LayoutProviderHelper.getLayoutData(gridCell, gridDescription, currentRowGridDescription,
			fullGridDescription,
			vElement, control));

	}

	/**
	 * The {@link SWTRendererFactory} to use.
	 * 
	 * @return the {@link SWTRendererFactory}
	 * @since 1.3
	 */
	protected final SWTRendererFactory getSWTRendererFactory() {
		return rendererFactory;
	}
}
