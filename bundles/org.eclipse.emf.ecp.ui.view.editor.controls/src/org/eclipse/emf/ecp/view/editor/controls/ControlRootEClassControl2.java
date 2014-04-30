/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Alexandra Buzila - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.editor.controls;

import java.net.URL;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl;
import org.eclipse.emf.ecp.edit.spi.util.ECPModelElementChangeListener;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * @author Alexandra Buzila
 * 
 */
@SuppressWarnings("deprecation")
public class ControlRootEClassControl2 extends SWTControl {
	// TODO extend SimpleControlSWTControlSWTRenderer instead of SWTControl

	private Composite labelComposite;
	private Label label;
	private ComposedAdapterFactory composedAdapterFactory;
	private AdapterFactoryItemDelegator adapterFactoryItemDelegator;
	private Label imageLabel;
	private ECPModelElementChangeListener modelElementChangeListener;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#fillControlComposite(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void fillControlComposite(Composite composite) {
		final Composite parent = new Composite(composite, SWT.NONE);
		parent.setBackground(composite.getBackground());
		GridLayoutFactory.fillDefaults().numColumns(1).spacing(0, 0).equalWidth(false).applyTo(parent);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(parent);

		labelComposite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(labelComposite);
		labelComposite.setBackground(parent.getBackground());

		// create labels

		imageLabel = new Label(labelComposite, SWT.NONE);
		imageLabel.setBackground(labelComposite.getBackground());
		label = new Label(labelComposite, SWT.NONE);
		label.setBackground(labelComposite.getBackground());

		composedAdapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
			new ReflectiveItemProviderAdapterFactory(),
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });
		adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#bindValue()
	 */
	@Override
	public Binding bindValue() {

		final IObservableValue value = SWTObservables.observeText(label);

		getDataBindingContext().bindValue(value, getModelValue(), new UpdateValueStrategy() {

			@Override
			public Object convert(Object value) {
				return getModelValue().getValue();
			}
		}, new UpdateValueStrategy() {
			@Override
			public Object convert(Object value) {
				updateChangeListener((EObject) value);
				return getText(value);
			}
		});
		final IObservableValue tooltipValue = SWTObservables.observeTooltipText(label);
		getDataBindingContext().bindValue(tooltipValue, getModelValue(), new UpdateValueStrategy() {

			@Override
			public Object convert(Object value) {
				return getModelValue().getValue();
			}
		}, new UpdateValueStrategy() {
			@Override
			public Object convert(Object value) {
				return getText(value);
			}
		});

		final IObservableValue imageValue = SWTObservables.observeImage(imageLabel);
		getDataBindingContext().bindValue(imageValue, getModelValue(), new UpdateValueStrategy() {

			@Override
			public Object convert(Object value) {
				return getModelValue().getValue();
			}
		}, new UpdateValueStrategy() {
			@Override
			public Object convert(Object value) {
				return getImage(value);
			}
		});

		return null;
	}

	private Object getImage(Object value) {
		return Activator.getImage((URL) adapterFactoryItemDelegator.getImage(value));
	}

	private Object getText(Object value) {
		final String textName = adapterFactoryItemDelegator.getText(value);
		return textName == null ? "" : textName; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#getUnsetLabelText()
	 */
	@Override
	protected String getUnsetLabelText() {
		return "(Not Set)"; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#getUnsetButtonTooltip()
	 */
	@Override
	protected String getUnsetButtonTooltip() {
		return "Unset"; //$NON-NLS-1$
	}

	private void updateChangeListener(final EObject value) {
		if (modelElementChangeListener != null) {
			if (modelElementChangeListener.getTarget().equals(value)) {
				return;
			}
			modelElementChangeListener.remove();
			modelElementChangeListener = null;
		}

		modelElementChangeListener = new ECPModelElementChangeListener(value) {

			@Override
			public void onChange(Notification notification) {
				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						getDataBindingContext().updateTargets();
						labelComposite.layout();

					}

				});

			}
		};

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#getControlsForTooltip()
	 */
	@Override
	protected Control[] getControlsForTooltip() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dispose() {
		composedAdapterFactory.dispose();
		if (modelElementChangeListener != null) {
			modelElementChangeListener.remove();
		}
		label.dispose();
		super.dispose();
	}
}
