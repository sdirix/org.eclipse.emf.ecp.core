package org.eclipse.emf.ecp.internal.edit.swt.controls;

import org.eclipse.core.databinding.observable.value.DateAndTimeObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;

public class DateTimeControl extends SingleControl {

	private DateTime dateWidget;

	private DateTime timeWidget;

	private Composite parentComposite;

	private StackLayout sl;

	private Label unsetLabel;
	
	public DateTimeControl(boolean showLabel, IItemPropertyDescriptor itemPropertyDescriptor,
		EStructuralFeature feature, EditModelElementContext modelElementContext,boolean embedded) {
		super(showLabel, itemPropertyDescriptor, feature, modelElementContext,embedded);
	}

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
		unsetLabel.setText("No date set! Click to set date.");
		unsetLabel.setBackground(composite.getBackground());
		unsetLabel.setForeground(composite.getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		unsetLabel.setAlignment(SWT.CENTER);
		unsetLabel.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent e) {
				sl.topControl = dateTimeComposite;
				parentComposite.layout(true);
			}

			public void mouseDown(MouseEvent e) {
			}

			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		if (getModelElementContext().getModelElement().eIsSet(getStructuralFeature())) {
			sl.topControl = dateTimeComposite;
		} else {
			sl.topControl = unsetLabel;
		}
		parentComposite.layout();
	}

	/**
	 * This method creates the date widget, the time widget and the delete button.
	 * 
	 * @param composite the parent {@link Composite}
	 */
	private void createDateAndTimeWidget(Composite composite) {

		dateWidget = new DateTime(composite, SWT.DATE);
		dateWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		timeWidget = new DateTime(composite, SWT.TIME | SWT.SHORT);
		timeWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		//FIXME add button
//		dateDeleteButton = new ImageHyperlink(composite, SWT.TOP);
//		dateDeleteButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE));
//
//		dateDeleteButton.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				new ECPCommand(getModelElementContext().getModelElement(), getEditingDomain()) {
//
//					@Override
//					protected void doRun() {
//						getEditingDomain().getCommandStack().execute(
//							new RemoveCommand(getEditingDomain(), getModelElementContext().getModelElement(),
//								getFeature(), getModelElementContext().getModelElement().eGet(getFeature())));
//
//						sl.topControl = unsetLabel;
//						parentComposite.layout();
//					}
//				}.run(true);
//			}
//		});
	}
	
	@Override
	public void setEditable(boolean isEditable) {
		dateWidget.setEnabled(isEditable);
		timeWidget.setEnabled(isEditable);
	}

	@Override
	public void bindValue() {
		IObservableValue dateObserver = SWTObservables.observeSelection(dateWidget);
		IObservableValue timeObserver = SWTObservables.observeSelection(timeWidget);
		IObservableValue target = new DateAndTimeObservableValue(dateObserver, timeObserver);
		getDataBindingContext().bindValue(target, getModelValue());
	}

}
