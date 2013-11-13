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

package org.eclipse.emf.ecp.view.editor.controls;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.databinding.Binding;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.internal.swt.controls.SingleControl;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.rule.model.LeafCondition;
import org.eclipse.emf.ecp.view.rule.model.RulePackage;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.ListDialog;

/**
 * A control for defining an value in a leaf condition.
 * 
 * @author Eugen Neufeld
 * 
 */
// APITODO no api yet
@SuppressWarnings("restriction")
public class ExpectedValueControl extends SingleControl {

	private Label text;

	private String getTextVariantID() {
		return "org_eclipse_emf_ecp_view_editor_controls_ruleattribute";
	}

	@Override
	protected void updateValidationColor(Color color) {
		text.setBackground(color);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#fillControlComposite(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void fillControlComposite(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(composite);
		final Button bSelectObject = new Button(composite, SWT.PUSH);
		bSelectObject.setText("Select Object");

		text = new Label(composite, SWT.SINGLE | SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text.setData(CUSTOM_VARIANT, getTextVariantID());

		bSelectObject.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				final LeafCondition condition = (LeafCondition) getModelElementContext().getModelElement();
				final EStructuralFeature structuralFeature = ((VFeaturePathDomainModelReference) condition
					.getDomainModelReference()).getDomainModelEFeature();
				if (structuralFeature == null) {
					MessageDialog.openError(text.getShell(), "No Attribute selected",
						"Please select an attribute first. Without the attribute we can't provide you with support!");
					return;
				}
				if (EReference.class.isInstance(structuralFeature)) {
					// TODO show all references
					return;
				}
				final EAttribute attribute = (EAttribute) structuralFeature;
				Class<?> attribuetClazz = attribute.getEAttributeType().getInstanceClass();
				if (attribuetClazz.isPrimitive()) {
					if (int.class.isAssignableFrom(attribuetClazz)) {
						attribuetClazz = Integer.class;
					} else if (long.class.isAssignableFrom(attribuetClazz)) {
						attribuetClazz = Long.class;
					} else if (float.class.isAssignableFrom(attribuetClazz)) {
						attribuetClazz = Float.class;
					} else if (double.class.isAssignableFrom(attribuetClazz)) {
						attribuetClazz = Double.class;
					} else if (boolean.class.isAssignableFrom(attribuetClazz)) {
						attribuetClazz = Boolean.class;
					} else if (char.class.isAssignableFrom(attribuetClazz)) {
						attribuetClazz = Character.class;
					}
				}
				Object object = null;
				if (Enum.class.isAssignableFrom(attribuetClazz)) {
					final Object[] enumValues = attribuetClazz.getEnumConstants();
					final ListDialog ld = new ListDialog(text.getShell());
					ld.setLabelProvider(new LabelProvider());
					ld.setContentProvider(ArrayContentProvider.getInstance());
					ld.setInput(enumValues);
					ld.setInitialSelections(new Object[] { enumValues[0] });
					ld.setMessage("Please select the enum value to set.");
					ld.setTitle("Select a value");
					final int enumSelectionResult = ld.open();
					if (Window.OK == enumSelectionResult) {
						object = ld.getResult()[0];
					}
				} else if (String.class.isAssignableFrom(attribuetClazz)
					|| Number.class.isAssignableFrom(attribuetClazz) || Boolean.class.isAssignableFrom(attribuetClazz)) {
					try {
						final Constructor<?> constructor = attribuetClazz.getConstructor(String.class);
						final InputDialog id = new InputDialog(
							text.getShell(),
							"Insert the value",
							"The value must be parseable by the "
								+ attribuetClazz.getSimpleName()
								+ " class. For a double value please use the #.# format. For boolean values 'true' or 'false'.",
							null, null);
						final int inputResult = id.open();
						if (Window.OK == inputResult) {
							object = constructor.newInstance(id.getValue());
						}
					} catch (final IllegalArgumentException ex) {

					} catch (final SecurityException ex) {

					} catch (final NoSuchMethodException ex) {
					} catch (final InstantiationException ex) {
					} catch (final IllegalAccessException ex) {
					} catch (final InvocationTargetException ex) {
					}
				} else {
					MessageDialog.openError(text.getShell(), "Not primitive Attribute selected",
						"The selected attribute has a not primitive type. We can't provide you support for it!");
				}

				if (object != null) {
					final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(condition);
					editingDomain.getCommandStack().execute(
						SetCommand.create(editingDomain, condition,
							RulePackage.eINSTANCE.getLeafCondition_ExpectedValue(), object));
					text.setText(object.toString());
				}
			}

		});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.edit.spi.ECPControl#setEditable(boolean)
	 */
	public void setEditable(boolean isEditable) {
		// text.setEditable(isEditable);
	}

	@Override
	public Binding bindValue() {
		if (getModelValue().getValue() != null) {
			text.setText(getModelValue().getValue().toString());
		}
		return null;
	}

	@Override
	protected String getHelpText() {
		return "";
	}

	@Override
	protected Control[] getControlsForTooltip() {
		return new Control[] { text };
	}

	@Override
	protected String getUnsetButtonTooltip() {
		return "Unset Button";
	}

	@Override
	protected String getUnsetLabelText() {
		return "Unset Button";
	}

}
