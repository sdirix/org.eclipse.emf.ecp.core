package org.eclipse.emf.ecp.edit.internal.swt.controls;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.ecp.edit.internal.swt.Activator;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;

public class XmlDateControl extends SingleControl {

	public XmlDateControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, EditModelElementContext modelElementContext, boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext, embedded);
	}
	private DateTime dateWidget;

	private Composite parentComposite;

	private StackLayout sl;

	private Label unsetLabel;
	
	@Override
	protected void fillInnerComposite(Composite composite) {
		parentComposite = new Composite(composite, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(parentComposite);
		sl = new StackLayout();
		parentComposite.setLayout(sl);
		final Composite dateTimeComposite = new Composite(parentComposite, SWT.NONE);
		GridLayoutFactory.fillDefaults().spacing(2, 0).numColumns(4).applyTo(dateTimeComposite);

		createDateAndTimeWidget(dateTimeComposite);

		unsetLabel = new Label(parentComposite, SWT.NONE);
		//TODO language
		unsetLabel.setText("No date set! Click to set date.");//$NON-NLS-1$
		unsetLabel.setForeground(composite.getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		unsetLabel.setAlignment(SWT.CENTER);
		unsetLabel.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent e) {
				sl.topControl = dateTimeComposite;
				parentComposite.layout(true);
			}

			public void mouseDown(MouseEvent e) {
				//nothing to do
			}

			public void mouseDoubleClick(MouseEvent e) {
				//nothing to do
			}
		});
		if (getModelElementContext().getModelElement().eIsSet(getStructuralFeature())) {
			sl.topControl = dateTimeComposite;
		} else {
			sl.topControl = unsetLabel;
		}
		parentComposite.layout();
	}

	private void createDateAndTimeWidget(Composite composite) {
		int numColumns = 3;
		if (isEmbedded()) {
			numColumns = 2;
		}
		GridLayoutFactory.fillDefaults().numColumns(numColumns).spacing(0, 0).equalWidth(false).applyTo(composite);
		
		dateWidget = new DateTime(composite, SWT.DATE|SWT.DROP_DOWN);
		dateWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		if(!isEmbedded()&&getStructuralFeature().isUnsettable()){
			Button unsetdate=new Button(composite, SWT.PUSH);
			unsetdate.setToolTipText("UnsetDate");
			unsetdate.setImage(Activator.getDefault().getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE));
			unsetdate.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					getModelElementContext().getEditingDomain().getCommandStack().execute(
						new SetCommand(getModelElementContext().getEditingDomain(), getModelElementContext().getModelElement(),
							getStructuralFeature(), SetCommand.UNSET_VALUE));

					sl.topControl = unsetLabel;
					parentComposite.layout();
				}
				
			});
		}
	}
	
	@Override
	public void setEditable(boolean isEditable) {
		dateWidget.setEnabled(isEditable);
	}

	@Override
	protected void bindValue() {
		IObservableValue dateObserver = SWTObservables.observeSelection(dateWidget);
		getDataBindingContext().bindValue(dateObserver, getModelValue());
	}


}
