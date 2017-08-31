/*******************************************************************************
 * Copyright (c) 2011-2017 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Mat Hansen - initial API and implementation
 ******************************************************************************/
package org.eclipse.emfforms.spi.swt.table;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emfforms.common.Optional;
import org.eclipse.emfforms.internal.swt.table.util.StaticCellLabelProviderFactory;
import org.eclipse.emfforms.spi.swt.table.TableViewerSWTCustomization.ColumnConfiguration;
import org.eclipse.emfforms.spi.swt.table.TableViewerSWTCustomization.ConfigurationCallback;
import org.eclipse.jface.viewers.AbstractTableViewer;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

/**
 * @author Mat Hansen <mhansen@eclipsesource.com>
 *
 * @param <P> parent builder (if used in builder chain)
 */
public class ColumnConfigurationBuilder<P> {

	private boolean resizeable = true;
	private boolean moveable; // not movable
	private int styleBits = SWT.NONE;
	private int weight = ColumnConfiguration.NO_WEIGHT;
	private int minWidth;

	@SuppressWarnings("rawtypes")
	private IObservableValue textObservable;
	@SuppressWarnings("rawtypes")
	private IObservableValue tooltipObservable;

	private CellLabelProviderFactory labelProviderFactory;
	private EditingSupportCreator editingSupportCreator;

	private Image image;
	private Map<String, Object> data = new LinkedHashMap<String, Object>();
	private List<ConfigurationCallback<AbstractTableViewer, ViewerColumn>> configurationCallbacks;

	/**
	 * Listener interface for this builders lifecycle, useful for builder chaining.
	 *
	 * @author Mat Hansen <mhansen@eclipsesource.com>
	 *
	 */
	interface BuilderLifeCycleListener {

		/**
		 * Callback for child builder initialization (i.e. for inheriting defaults).
		 *
		 * @param childBuilder the {@link ColumnConfigurationBuilder} instance
		 */
		void onInit(ColumnConfigurationBuilder<?> childBuilder);

		/**
		 * Callback for child builder completion (used for builder chaining).
		 *
		 * @param config the created {@link ColumnConfiguration}
		 */
		void onCreate(ColumnConfiguration config);

	}

	private final Optional<P> parentBuilder;
	private Optional<BuilderLifeCycleListener> lifeCycleListener = Optional.empty();

	/**
	 * The default constructor.
	 */
	public ColumnConfigurationBuilder() {
		super();
		this.parentBuilder = null;
	}

	/**
	 * Internal constructor used for builder chaining (currently only TableViewerSWTBuilder).
	 *
	 * @param parentBuilder the parent builder instance
	 * @param lifeCycleListener the lifecycle listener
	 */
	ColumnConfigurationBuilder(P parentBuilder, BuilderLifeCycleListener lifeCycleListener) {
		this.parentBuilder = Optional.of(parentBuilder);
		this.lifeCycleListener = Optional.of(lifeCycleListener);
	}

	/**
	 * Constructor which allows to inherit an existing configuration.
	 *
	 * @param columnConfiguration the {@link ColumnConfiguration} to inherit.
	 */
	public ColumnConfigurationBuilder(ColumnConfiguration columnConfiguration) {
		super();
		final ColumnConfigurationImpl config = (ColumnConfigurationImpl) columnConfiguration;
		resizable(config.isResizeable());
		moveable(config.isMoveable());
		styleBits(config.getStyleBits());
		weight(config.getWeight());
		minWidth(config.getMinWidth());
		// skip: text, tooltip
		labelProviderFactory(config.getLabelProviderFactory());
		if (config.getEditingSupportCreator().isPresent()) {
			editingSupportCreator(config.getEditingSupportCreator().get());
		}
		// skip: image
		for (final ConfigurationCallback<AbstractTableViewer, ViewerColumn> callback : config
			.getConfigurationCallbacks()) {
			callback(callback);
		}
		this.parentBuilder = null;

		if (lifeCycleListener.isPresent()) {
			lifeCycleListener.get().onInit(this);
		}
	}

	/**
	 * Makes the column resizable.
	 *
	 * @param resizable true for resizable columns
	 * @return self
	 */
	public ColumnConfigurationBuilder<P> resizable(boolean resizable) {
		resizeable = resizable;
		return this;
	}

	/**
	 * Makes the column moveable.
	 *
	 * @param moveable true for movable columns
	 * @return self
	 */
	public ColumnConfigurationBuilder<P> moveable(boolean moveable) {
		this.moveable = moveable;
		return this;
	}

	/**
	 * Add SWT style bits.
	 *
	 * @param styleBits the SWT style bits
	 * @return self
	 */
	public ColumnConfigurationBuilder<P> styleBits(int styleBits) {
		this.styleBits = styleBits;
		return this;
	}

	/**
	 * Add column weight.
	 *
	 * @param weight the weight
	 * @return self
	 */
	public ColumnConfigurationBuilder<P> weight(int weight) {
		this.weight = weight;
		return this;
	}

	/**
	 * Add a minimal width.
	 *
	 * @param minWidth the minimal width
	 * @return self
	 */
	public ColumnConfigurationBuilder<P> minWidth(int minWidth) {
		this.minWidth = minWidth;
		return this;
	}

	/**
	 * Add a text observable.
	 *
	 * @param textObservable the column text observable
	 * @return self
	 */
	@SuppressWarnings("rawtypes")
	public ColumnConfigurationBuilder<P> text(IObservableValue textObservable) {
		this.textObservable = textObservable;
		return this;
	}

	/**
	 * Add a static text.
	 *
	 * @param text the column text
	 * @return self
	 */
	public ColumnConfigurationBuilder<P> text(String text) {
		return text(Observables.constantObservableValue(text, String.class));
	}

	/**
	 * Add a tooltip observable.
	 *
	 * @param tooltipObservable the tooltip observable
	 * @return self
	 */
	@SuppressWarnings("rawtypes")
	public ColumnConfigurationBuilder<P> tooltip(IObservableValue tooltipObservable) {
		this.tooltipObservable = tooltipObservable;
		return this;
	}

	/**
	 * Add a static tooltip.
	 *
	 * @param tooltip the tooltip
	 * @return self
	 */
	public ColumnConfigurationBuilder<P> tooltip(String tooltip) {
		return tooltip(Observables.constantObservableValue(tooltip, String.class));
	}

	/**
	 * Add a label provider factory.
	 *
	 * @param labelProviderFactory the label provider factory
	 * @return self
	 */
	public ColumnConfigurationBuilder<P> labelProviderFactory(CellLabelProviderFactory labelProviderFactory) {
		this.labelProviderFactory = labelProviderFactory;
		return this;
	}

	/**
	 * Add a label provider.
	 *
	 * @param labelProvider the label provider
	 * @return self
	 */
	public ColumnConfigurationBuilder<P> labelProvider(CellLabelProvider labelProvider) {
		return labelProviderFactory(new StaticCellLabelProviderFactory(labelProvider));
	}

	/**
	 * Add an editing support creator.
	 *
	 * @param editingSupportCreator the editing support creator
	 * @return self
	 */
	public ColumnConfigurationBuilder<P> editingSupportCreator(EditingSupportCreator editingSupportCreator) {
		this.editingSupportCreator = editingSupportCreator;
		return this;
	}

	/**
	 * Add a column image.
	 *
	 * @param image the image
	 * @return self
	 */
	public ColumnConfigurationBuilder<P> image(Image image) {
		this.image = image;
		return this;
	}

	/**
	 * Add a pre-initialized data map.
	 *
	 * @param data the data map
	 * @return self
	 */
	public ColumnConfigurationBuilder<P> dataMap(Map<String, Object> data) {
		if (!this.data.isEmpty()) {
			throw new IllegalArgumentException("Data map values have already been set"); //$NON-NLS-1$
		}
		if (data == null) {
			throw new NullPointerException("Data map cannot be null"); //$NON-NLS-1$
		}
		this.data = data;
		return this;
	}

	/**
	 * Add a data map entry.
	 *
	 * @param key the data map key
	 * @param value the data map value
	 * @return self
	 */
	public ColumnConfigurationBuilder<P> dataMapEntry(String key, Object value) {
		data.put(key, value);
		return this;
	}

	/**
	 * Add a column configuration callback.
	 *
	 * @param callback the callback
	 * @return self
	 */
	public ColumnConfigurationBuilder<P> callback(ConfigurationCallback<AbstractTableViewer, ViewerColumn> callback) {
		if (configurationCallbacks == null) {
			configurationCallbacks = //
				new ArrayList<TableViewerSWTCustomization.ConfigurationCallback<AbstractTableViewer, ViewerColumn>>();
		}
		configurationCallbacks.add(callback);
		return this;
	}

	/**
	 * Create a new {@link ColumnConfiguration} based on the current builder state.
	 *
	 * @return the new {@link ColumnConfiguration}
	 */
	public ColumnConfiguration create() {
		final ColumnConfiguration config = new ColumnConfigurationImpl(
			resizeable,
			moveable,
			styleBits,
			weight,
			minWidth,
			textObservable,
			tooltipObservable,
			labelProviderFactory,
			editingSupportCreator,
			image,
			data,
			configurationCallbacks);

		if (lifeCycleListener.isPresent()) {
			lifeCycleListener.get().onCreate(config);
		}

		return config;
	}

	/**
	 * Create a new {@link ColumnConfiguration} based on the current builder state.
	 * Afterwards return the parent builder instance.
	 *
	 * @return the parent builder instance
	 *
	 * @param<P> the parent builder
	 */
	public P finish() {
		create();
		if (!parentBuilder.isPresent()) {
			throw new IllegalAccessError("No parent builder configured"); //$NON-NLS-1$
		}
		return parentBuilder.get();
	}

	/**
	 * Create a new {@link ColumnConfiguration} based on the current builder state.
	 * Afterwards assign the new instance to the given argument.
	 *
	 * @param config the variable to assign the new instance to
	 */
	public void createAndSet(ColumnConfiguration config) {
		config = create();
	}

}
