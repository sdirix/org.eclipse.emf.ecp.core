package org.eclipse.emf.ecp.edit.internal.swt.controls;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.xml.type.internal.XMLCalendar;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

import javax.xml.datatype.XMLGregorianCalendar;

import java.util.Date;

public class XmlDateControl extends SingleControl {

	public XmlDateControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, ECPControlContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
	}

	private DateTime dateWidget;

	@Override
	protected void fillControlComposite(Composite dateTimeComposite) {
		GridLayoutFactory.fillDefaults().spacing(2, 0).numColumns(4).applyTo(dateTimeComposite);
		createDateAndTimeWidget(dateTimeComposite);
	}

	private void createDateAndTimeWidget(Composite composite) {
		int numColumns = 3;
		if (isEmbedded()) {
			numColumns = 2;
		}
		GridLayoutFactory.fillDefaults().numColumns(numColumns).spacing(2, 0).equalWidth(false).applyTo(composite);

		dateWidget = new DateTime(composite, SWT.DATE | SWT.DROP_DOWN | SWT.BORDER);
		dateWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		dateWidget.setData(CUSTOM_VARIANT, "org_eclipse_emf_ecp_control_swt_xml_date");
	}

	@Override
	public void setEditable(boolean isEditable) {
		dateWidget.setEnabled(isEditable);
	}

	@Override
	protected Binding bindValue() {
		IObservableValue dateObserver = SWTObservables.observeSelection(dateWidget);
		Binding binding = getDataBindingContext().bindValue(dateObserver, getModelValue(), new UpdateValueStrategy() {

			@Override
			public Object convert(Object value) {
				Date date = (Date) value;
				return new XMLCalendar(date, XMLCalendar.DATE);
			}

		}, new UpdateValueStrategy() {
			@Override
			public Object convert(Object value) {
				XMLGregorianCalendar gregorianCalendar = (XMLGregorianCalendar) value;
				return gregorianCalendar.toGregorianCalendar().getTime();
			}
		});
		binding.updateTargetToModel();
		return binding;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.controls.SingleControl#getUnsetLabelText()
	 */
	@Override
	protected String getUnsetLabelText() {
		// TODO language
		return "No date set! Click to set date."; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.controls.SingleControl#getUnsetButtonTooltip()
	 */
	@Override
	protected String getUnsetButtonTooltip() {
		return "Unset date";
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.edit.internal.swt.util.SWTControl#getControlForTooltip()
	 */
	@Override
	protected Control[] getControlsForTooltip() {
		return new Control[] { dateWidget };
	}

}
