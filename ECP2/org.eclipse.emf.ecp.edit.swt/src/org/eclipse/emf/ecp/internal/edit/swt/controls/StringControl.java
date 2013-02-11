package org.eclipse.emf.ecp.internal.edit.swt.controls;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class StringControl extends AbstractTextControl<String> {

	public StringControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor, EStructuralFeature feature,
		EditModelElementContext modelElementContext,boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext,embedded);
	}

	/** {@inheritDoc} */
	@Override
	protected void createTextWidget(Composite composite) {
		int textStyle = SWT.BORDER;
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		if (getItemPropertyDescriptor().isMultiLine(getModelElementContext().getModelElement())) {
			textStyle = textStyle | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL;
			gridData.heightHint = 200;
		} else {
			textStyle = textStyle | SWT.SINGLE;
		}
		text = new Text(composite, textStyle);
		text.setLayoutData(gridData);
	}

	/** {@inheritDoc} */
	@Override
	protected String convertStringToModel(String s) {
		return s;
	}

	/** {@inheritDoc} */
	@Override
	protected boolean validateString(String s) {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected String convertModelToString(String t) {
		return t;
	}

	/** {@inheritDoc} */
	@Override
	protected String getDefaultValue() {
		return "";
	}

	/** {@inheritDoc} */
	@Override
	protected void postValidate(String text) {
		// do nothing

	}
}
