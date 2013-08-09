package org.eclipse.emf.ecp.view.editor.controls;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.databinding.Binding;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.edit.internal.swt.controls.SingleControl;
import org.eclipse.emf.ecp.view.rule.model.LeafCondition;
import org.eclipse.emf.ecp.view.rule.model.RulePackage;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.ListDialog;

public class ExpectedValueControl extends SingleControl {

	private Label text;

	public ExpectedValueControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, ECPControlContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
		// TODO Auto-generated constructor stub
	}

	protected String getTextVariantID() {
		return "org_eclipse_emf_ecp_view_editor_controls_ruleattribute";
	}

	@Override
	protected void updateValidationColor(Color color) {
		text.setBackground(color);
	}

	@Override
	protected void fillControlComposite(Composite composite) {
		((GridLayout) composite.getLayout()).numColumns = 2;
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
				final EAttribute attribute = condition.getAttribute();
				if (attribute == null) {
					MessageDialog.openError(text.getShell(), "No Attribute selected",
						"Please select an attribute first. Without the attribute we can't provide you with support!");
					return;
				}
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

	public void setEditable(boolean isEditable) {
		// text.setEditable(isEditable);
	}

	@Override
	public Binding bindValue() {
		if (getModelValue().getValue() != null) {
			text.setText(getModelValue().getValue().toString());
		}
		// IObservableValue value = SWTObservables.observeText(text,
		// SWT.FocusOut);
		// getDataBindingContext().bindValue(value, getModelValue());
		// ,
		// new EMFUpdateValueStrategy() {
		// @Override
		// public Object convert(Object value) {
		// updateValidationColor(null);
		// return super.convert(value);
		// }
		// },null
		return null;
	}

	@Override
	protected String getHelpText() {
		return "";
	}

	@Override
	protected Control[] getControlsForTooltip() {
		// TODO Auto-generated method stub
		return new Control[] { text };
	}

	@Override
	protected String getUnsetButtonTooltip() {
		// TODO Auto-generated method stub
		return "Unset Button";
	}

	@Override
	protected String getUnsetLabelText() {
		// TODO Auto-generated method stub
		return "Unset Button";
	}

}
