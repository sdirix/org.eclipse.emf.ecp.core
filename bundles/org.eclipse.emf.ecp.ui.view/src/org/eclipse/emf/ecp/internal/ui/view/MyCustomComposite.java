package org.eclipse.emf.ecp.internal.ui.view;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class MyCustomComposite extends Composite {

    public MyCustomComposite(Composite parent, EObject eObject) {
        super(parent, SWT.NONE);
        fillWithData(eObject);
    }

    private void fillWithData(EObject eObject) {
        GridLayoutFactory.fillDefaults().numColumns(2).applyTo(this);
        Label topLabel = new Label(this, SWT.NONE);
        topLabel.setText("The custom composite");
        GridDataFactory.fillDefaults().span(2, 1).align(SWT.FILL, SWT.FILL).applyTo(topLabel);

        for (EAttribute attribute : eObject.eClass().getEAttributes()) {
            new Label(this, SWT.NONE).setText(attribute.getName());
            Object value = eObject.eGet(attribute);
            new Label(this, SWT.NONE).setText(value == null ? "" : value.toString());
        }
    }

}
