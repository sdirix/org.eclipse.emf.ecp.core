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
package org.eclipse.emf.ecp.view.internal.core.swt.renderer;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecp.edit.internal.swt.controls.ControlMessages;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.core.swt.SWTControlRenderer;
import org.eclipse.emf.ecp.view.spi.swt.SWTRendererFactory;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * Renders texts.
 * 
 * @author Eugen Neufeld
 * 
 */
public class SWTTextControlRenderer extends SWTControlRenderer {
	/**
	 * Default constructor.
	 */
	public SWTTextControlRenderer() {
		super();
	}

	/**
	 * Test constructor.
	 * 
	 * @param factory the {@link SWTRendererFactory} to use.
	 */
	SWTTextControlRenderer(SWTRendererFactory factory) {
		super(factory);
	}

	@Override
	protected Binding[] createBindings(Control control, Setting setting) {
		final TargetToModelUpdateStrategy targetToModelUpdateStrategy = new TargetToModelUpdateStrategy(
			setting.getEStructuralFeature().isUnsettable());
		final ModelToTargetUpdateStrategy modelToTargetUpdateStrategy = new ModelToTargetUpdateStrategy();
		final Binding binding = bindValue(control, getModelValue(setting), getDataBindingContext(),
			targetToModelUpdateStrategy,
			modelToTargetUpdateStrategy);
		final Binding tooltipBinding = createTooltipBinding(control, getModelValue(setting), getDataBindingContext(),
			targetToModelUpdateStrategy,
			modelToTargetUpdateStrategy);
		return new Binding[] { binding, tooltipBinding };
	}

	@Override
	protected Control createControl(Composite parent, Setting setting) {
		final Text text = new Text(parent, getTextWidgetStyle());
		text.setData(CUSTOM_VARIANT, getTextVariantID());
		text.setMessage(getTextMessage(setting, getViewModelContext()));
		return text;
	}

	/**
	 * Returns the text which should be set as the message text on the Text field.
	 * 
	 * @param setting the setting being shown in the text field
	 */
	protected String getTextMessage(Setting setting, ViewModelContext viewModelContext) {
		return getItemPropertyDescriptor(setting).getDisplayName(null);
	}

	/**
	 * Creates a focus out binding for this control.
	 * 
	 * @param text the {@link Text} to bind
	 * @param modelValue the {@link IObservableValue} to bind
	 * @param dataBindingContext the {@link DataBindingContext} to use
	 * @param targetToModel the {@link UpdateValueStrategy} from target to Model
	 * @param modelToTarget the {@link UpdateValueStrategy} from model to target
	 * @return the created {@link Binding}
	 */
	protected Binding bindValue(Control text, IObservableValue modelValue, DataBindingContext dataBindingContext,
		UpdateValueStrategy targetToModel, UpdateValueStrategy modelToTarget) {
		final IObservableValue value = SWTObservables.observeText(text, SWT.FocusOut);
		final Binding binding = dataBindingContext.bindValue(value, modelValue, targetToModel, modelToTarget);
		return binding;
	}

	/**
	 * Creates a tooltip binding for this control.
	 * 
	 * @param text the {@link Text} to bind
	 * @param modelValue the {@link IObservableValue} to bind
	 * @param dataBindingContext the {@link DataBindingContext} to use
	 * @param targetToModel the {@link UpdateValueStrategy} from target to Model
	 * @param modelToTarget the {@link UpdateValueStrategy} from model to target
	 * @return the created {@link Binding}
	 */
	protected Binding createTooltipBinding(Control text, IObservableValue modelValue,
		DataBindingContext dataBindingContext, UpdateValueStrategy targetToModel, UpdateValueStrategy modelToTarget) {
		final IObservableValue toolTip = SWTObservables.observeTooltipText(text);
		return dataBindingContext.bindValue(toolTip, modelValue, targetToModel, modelToTarget);
	}

	/**
	 * The style to apply to the text widget. This can be changed by the concrete classes.
	 * 
	 * @return the style to apply
	 */
	protected int getTextWidgetStyle() {
		return SWT.SINGLE | SWT.BORDER;
	}

	/**
	 * The VariantId to use e.g. for RAP
	 * 
	 * @return the String identifying this control
	 */
	protected String getTextVariantID() {
		return "org_eclipse_emf_ecp_control_string"; //$NON-NLS-1$
	}

	@Override
	protected void setControlEnabled(int index, Control control, boolean enabled) {
		if (index == 2) {
			final Setting setting = getVElement().getDomainModelReference().getIterator().next();
			if (!setting.isSet()) {
				return;
			}
			Control controlToUnset = control;
			if (setting.getEStructuralFeature().isUnsettable()) {
				controlToUnset = Composite.class.cast(Composite.class.cast(control).getChildren()[0]).getChildren()[0];
			}
			Text.class.cast(controlToUnset).setEditable(enabled);
		} else {
			super.setControlEnabled(index, control, enabled);
		}
	}

	/**
	 * An {@link EMFUpdateConvertValueStrategy} that encapsulates the converting
	 * of the actual value. Use this class to provide a specific context
	 * for the conversion of the value, but likewise enable it clients to modify
	 * the conversion behavior.
	 * 
	 * @author emueller
	 * 
	 */
	class EMFUpdateConvertValueStrategy extends EMFUpdateValueStrategy {

		/**
		 * Constructor.
		 */
		public EMFUpdateConvertValueStrategy() {
			super();
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.core.databinding.UpdateValueStrategy#convert(java.lang.Object)
		 */
		@Override
		public Object convert(Object value) {
			return convertValue(value);
		}

		protected Object convertValue(Object value) {
			return super.convert(value);
		}
	}

	protected class ModelToTargetUpdateStrategy extends EMFUpdateConvertValueStrategy {

		@Override
		public Object convert(Object value) {
			return convertValue(value);
		}

	}

	protected class TargetToModelUpdateStrategy extends EMFUpdateConvertValueStrategy {

		private final boolean unsetable;

		public TargetToModelUpdateStrategy(boolean unsetable) {
			this.unsetable = unsetable;

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object convert(Object value) {
			try {
				if ("".equals(value)) { //$NON-NLS-1$
					value = null;
				}
				if (value == null && unsetable) {
					return SetCommand.UNSET_VALUE;
				}

				return convertValue(value);

			} catch (final IllegalArgumentException e) {
				throw e;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.core.swt.SimpleControlSWTRenderer#getUnsetText()
	 */
	@Override
	protected String getUnsetText() {
		return ControlMessages.StringControl_NoTextSetClickToSetText;
	}

}
