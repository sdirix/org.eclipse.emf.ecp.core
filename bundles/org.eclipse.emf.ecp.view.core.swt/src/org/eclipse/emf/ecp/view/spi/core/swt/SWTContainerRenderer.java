/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Eugen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.core.swt;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecp.view.internal.core.swt.Activator;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCell;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridCellDescription;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridDescription;
import org.eclipse.emf.ecp.view.spi.layout.grid.GridDescriptionFactory;
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.AbstractSWTAdditionalRenderer;
import org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/**
 * The {@link SWTContainerRenderer} is a super class for all Renderer which renders its contents vertically.
 * 
 * @param <VELEMENT> the {@link VElement} of the renderer
 * @author Eugen Neufeld
 * 
 */
public abstract class SWTContainerRenderer<VELEMENT extends VElement> extends AbstractSWTRenderer<VELEMENT> {
	/**
	 * Default constructor.
	 */
	public SWTContainerRenderer() {
		super();
	}

	/**
	 * Test constructor.
	 * 
	 * @param factory the {@link SWTRendererFactory} to use.
	 */
	protected SWTContainerRenderer(SWTRendererFactory factory) {
		super(factory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#getGridDescription()
	 */
	@Override
	public GridDescription getGridDescription() {
		return GridDescriptionFactory.INSTANCE.createSimpleGrid(1, 1);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#renderControl(int, org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.emf.ecp.view.spi.model.VElement, org.eclipse.emf.ecp.view.spi.context.ViewModelContext)
	 */
	@Override
	protected Control renderControl(GridCell gridCell, Composite parent)
		throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		if (gridCell.getColumn() != 0) {
			return null;
		}
		final Composite columnComposite = getComposite(parent);
		columnComposite.setData(CUSTOM_VARIANT, getCustomVariant());
		columnComposite.setBackground(parent.getBackground());

		int maxNumberControlsPerRow = 1;
		final Map<VContainedElement, AbstractSWTRenderer<VElement>> elementRendererMap = new LinkedHashMap<VContainedElement, AbstractSWTRenderer<VElement>>();
		final Map<VContainedElement, Collection<AbstractSWTAdditionalRenderer<VElement>>> elementAdditionalRendererMap = new LinkedHashMap<VContainedElement, Collection<AbstractSWTAdditionalRenderer<VElement>>>();
		for (final VContainedElement child : getChildren()) {

			final AbstractSWTRenderer<VElement> renderer = getSWTRendererFactory().getRenderer(child,
				getViewModelContext());
			if (renderer == null) {
				Activator
					.getDefault()
					.getLog()
					.log(
						new Status(IStatus.INFO, Activator.PLUGIN_ID, String.format(
							"No Renderer for %s found.", child.eClass().getName()))); //$NON-NLS-1$
				continue;
			}
			final Collection<AbstractSWTAdditionalRenderer<VElement>> additionalRenderers = getSWTRendererFactory()
				.getAdditionalRenderer(child,
					getViewModelContext());

			int maxAdditionalColumn = 0;
			for (final AbstractSWTAdditionalRenderer<VElement> additionalRenderer : additionalRenderers) {
				if (additionalRenderer.getAdditionalColumns() > maxAdditionalColumn) {
					maxAdditionalColumn = additionalRenderer.getAdditionalColumns();
				}
			}
			final int maxColumnsInCurrentRow = renderer.getGridDescription().getColumns()
				+ maxAdditionalColumn;

			if (maxNumberControlsPerRow < maxColumnsInCurrentRow) {
				maxNumberControlsPerRow = maxColumnsInCurrentRow;
			}
			elementRendererMap.put(child, renderer);
			elementAdditionalRendererMap.put(child, additionalRenderers);
		}
		// Gridlayout does not have an overflow as other Layouts might have.
		columnComposite.setLayout(getLayout(maxNumberControlsPerRow, false));
		for (final VContainedElement child : elementRendererMap.keySet()) {
			final AbstractSWTRenderer<VElement> renderer = elementRendererMap.get(child);
			final Collection<AbstractSWTAdditionalRenderer<VElement>> additionalRenderers = elementAdditionalRendererMap
				.get(child);
			try {
				final Set<GridCellDescription> preControlDescriptions = new LinkedHashSet<GridCellDescription>();
				final Set<GridCellDescription> postControlDescriptions = new LinkedHashSet<GridCellDescription>();
				for (final GridCell childGridCell : renderer.getGridDescription().getGrid()) {
					for (final AbstractSWTAdditionalRenderer<VElement> additionalRenderer : additionalRenderers) {
						final GridCellDescription preCellRenderControl = additionalRenderer.preCellRenderControl(
							renderer.getGridDescription(), childGridCell,
							columnComposite);
						if (preCellRenderControl != null) {
							preControlDescriptions.add(preCellRenderControl);
						}
					}

					final Control control = renderer.render(childGridCell,
						columnComposite);
					for (final AbstractSWTAdditionalRenderer<VElement> additionalRenderer : additionalRenderers) {
						final GridCellDescription postCellRenderControl = additionalRenderer.postCellRenderControl(
							renderer.getGridDescription(), childGridCell,
							columnComposite);
						if (postCellRenderControl != null) {
							postControlDescriptions.add(postCellRenderControl);
						}
					}
					// TODO who should apply the layout
					setLayoutDataForControl(childGridCell, renderer.getGridDescription(),
						maxNumberControlsPerRow, preControlDescriptions, postControlDescriptions, control);

				}
				renderer.postRender(columnComposite);
			} catch (final NoPropertyDescriptorFoundExeption ex) {
				Activator.getDefault().getLog()
					.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, ex.getMessage(), ex));
				continue;
			}
			for (final AbstractSWTAdditionalRenderer<VElement> additionalRenderer : additionalRenderers) {
				additionalRenderer.postRender(columnComposite);
			}
		}

		return columnComposite;
	}

	/**
	 * The custom variant to set for styling.
	 * 
	 * @return the string used by rap for styling
	 */
	protected abstract String getCustomVariant();

	/**
	 * The collection of children to iterate over.
	 * 
	 * @return the collection of children to render
	 */
	protected abstract Collection<VContainedElement> getChildren();

	/**
	 * Allows to customize the composite which is used to render the children onto.
	 * 
	 * @param parent the parent {@link Composite} to use as a parent
	 * @return the {@link Composite} or a subclass to use
	 */
	protected Composite getComposite(Composite parent) {
		return new Composite(parent, SWT.NONE);
	}

	/**
	 * Returns the layout to use.
	 * 
	 * @param numControls number of columns to create
	 * @param equalWidth whether the columns should be equal
	 * @return the {@link Layout}
	 */
	protected Layout getLayout(int numControls, boolean equalWidth) {
		return getLayoutHelper().getColumnLayout(numControls, equalWidth);
	}
}
