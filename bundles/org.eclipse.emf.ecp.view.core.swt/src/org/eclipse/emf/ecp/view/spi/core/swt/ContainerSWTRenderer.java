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
import org.eclipse.emf.ecp.view.spi.model.VContainedElement;
import org.eclipse.emf.ecp.view.spi.model.VElement;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.AbstractAdditionalSWTRenderer;
import org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory;
import org.eclipse.emf.ecp.view.spi.swt.layout.GridCell;
import org.eclipse.emf.ecp.view.spi.swt.layout.GridDescription;
import org.eclipse.emf.ecp.view.spi.swt.layout.GridDescriptionFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/**
 * The {@link ContainerSWTRenderer} is a super class for all Renderer which renders its contents vertically.
 * 
 * @param <VELEMENT> the {@link VElement} of the renderer
 * @author Eugen Neufeld
 * 
 */
public abstract class ContainerSWTRenderer<VELEMENT extends VElement> extends AbstractSWTRenderer<VELEMENT> {
	private GridDescription rendererGridDescription;

	/**
	 * Default constructor.
	 */
	public ContainerSWTRenderer() {
		super();
	}

	/**
	 * Test constructor.
	 * 
	 * @param factory the {@link SWTRendererFactory} to use.
	 */
	protected ContainerSWTRenderer(SWTRendererFactory factory) {
		super(factory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#getGridDescription(GridDescription)
	 */
	@Override
	public GridDescription getGridDescription(GridDescription gridDescription) {
		if (rendererGridDescription == null) {
			rendererGridDescription = GridDescriptionFactory.INSTANCE.createSimpleGrid(1, 1, this);
		}
		return rendererGridDescription;
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

		// int maxNumberControlsPerRow = 1;
		final Map<VContainedElement, Collection<AbstractSWTRenderer<VElement>>> elementRendererMap = new LinkedHashMap<VContainedElement, Collection<AbstractSWTRenderer<VElement>>>();
		// final Map<VContainedElement, Collection<AbstractAdditionalSWTRenderer<VElement>>>
		// elementAdditionalRendererMap = new LinkedHashMap<VContainedElement,
		// Collection<AbstractAdditionalSWTRenderer<VElement>>>();
		GridDescription maximalGridDescription = null;
		final Map<VContainedElement, GridDescription> mainGridDescription = new LinkedHashMap<VContainedElement, GridDescription>();
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
			final Collection<AbstractAdditionalSWTRenderer<VElement>> additionalRenderers = getSWTRendererFactory()
				.getAdditionalRenderer(child,
					getViewModelContext());
			GridDescription gridDescription = renderer.getGridDescription(GridDescriptionFactory.INSTANCE
				.createEmptyGridDescription());
			// int maxAdditionalColumn = 0;
			for (final AbstractAdditionalSWTRenderer<VElement> additionalRenderer : additionalRenderers) {
				gridDescription = additionalRenderer.getGridDescription(gridDescription);
				// if (additionalRenderer.getGridDescription().getColumns() > maxAdditionalColumn) {
				// maxAdditionalColumn = additionalRenderer.getGridDescription().getColumns();
				// }
			}
			mainGridDescription.put(child, gridDescription);
			if (maximalGridDescription == null
				|| maximalGridDescription.getColumns() < gridDescription.getColumns())
			{
				maximalGridDescription = gridDescription;
			}

			// final int maxColumnsInCurrentRow = renderer.getGridDescription().getColumns()
			// + maxAdditionalColumn;
			// if (maxNumberControlsPerRow < maxColumnsInCurrentRow) {
			// maxNumberControlsPerRow = maxColumnsInCurrentRow;
			// }
			final Set<AbstractSWTRenderer<VElement>> allRenderer = new LinkedHashSet<AbstractSWTRenderer<VElement>>();
			allRenderer.add(renderer);
			allRenderer.addAll(additionalRenderers);
			elementRendererMap.put(child, allRenderer);
			// elementAdditionalRendererMap.put(child, additionalRenderers);
		}
		if (maximalGridDescription == null) {
			return columnComposite;
		}
		columnComposite.setLayout(getLayout(maximalGridDescription.getColumns(), false));
		for (final VContainedElement child : getChildren()) {
			try {
				final GridDescription gridDescription = mainGridDescription.get(child);
				for (final GridCell childGridCell : gridDescription.getGrid()) {

					final Control control = childGridCell.getRenderer().render(childGridCell,
						columnComposite);
					// TODO who should apply the layout
					setLayoutDataForControl(childGridCell, gridDescription, maximalGridDescription, control);

				}
				for (final AbstractSWTRenderer<VElement> renderer : elementRendererMap.get(child)) {
					renderer.finalizeRendering(columnComposite);
				}
			} catch (final NoPropertyDescriptorFoundExeption ex) {
				Activator.getDefault().getLog()
					.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, ex.getMessage(), ex));
				continue;
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#dispose()
	 */
	@Override
	protected void dispose() {
		rendererGridDescription = null;
		super.dispose();
	}

}
