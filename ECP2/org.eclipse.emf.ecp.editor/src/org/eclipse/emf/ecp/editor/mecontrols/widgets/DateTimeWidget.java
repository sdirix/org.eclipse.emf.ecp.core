/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols.widgets;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.editor.commands.ECPCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.core.databinding.observable.value.DateAndTimeObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

/**
 * @author Eugen Neufeld
 */
public class DateTimeWidget extends ECPAttributeWidget {
	private ImageHyperlink dateDeleteButton;

	private DateTime dateWidget;

	private DateTime timeWidget;

	private Composite parentComposite;

	private EObject modelElement;

	private EStructuralFeature feature;

	private EditingDomain domain;

	private StackLayout sl;

	private Label unsetLabel;

	/**
	 * @param dbc
	 */
	public DateTimeWidget(EMFDataBindingContext dbc, EObject modelElement, EStructuralFeature feature,
		EditingDomain domain) {
		super(dbc);
		this.modelElement = modelElement;
		this.feature = feature;
		this.domain = domain;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#createWidget(org.eclipse.ui.forms.widgets.
	 * FormToolkit
	 * , org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	public Control createWidget(FormToolkit toolkit, Composite composite, int style) {
		parentComposite = toolkit.createComposite(composite);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(parentComposite);
		sl = new StackLayout();
		parentComposite.setLayout(sl);
		final Composite dateTimeComposite = toolkit.createComposite(parentComposite);
		GridLayoutFactory.fillDefaults().spacing(2, 0).numColumns(4).applyTo(dateTimeComposite);

		createDateAndTimeWidget(dateTimeComposite);

		unsetLabel = toolkit.createLabel(parentComposite, "No date set! Click to set date.");
		unsetLabel.setBackground(composite.getBackground());
		unsetLabel.setForeground(toolkit.getColors().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		unsetLabel.setAlignment(SWT.CENTER);
		unsetLabel.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent e) {
				sl.topControl = dateTimeComposite;
				parentComposite.layout(true);
			}

			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		if (modelElement.eIsSet(feature)) {
			sl.topControl = dateTimeComposite;
		} else {
			sl.topControl = unsetLabel;
		}
		parentComposite.layout();
		return parentComposite;
	}

	private void createDateAndTimeWidget(Composite composite) {

		dateWidget = new DateTime(composite, SWT.DATE);
		dateWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		timeWidget = new DateTime(composite, SWT.TIME | SWT.SHORT);
		timeWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		dateDeleteButton = new ImageHyperlink(composite, SWT.TOP);
		dateDeleteButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE));

		dateDeleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				new ECPCommand(modelElement, domain) {

					@Override
					protected void doRun() {
						modelElement.eSet(feature, null);
						sl.topControl = unsetLabel;
						parentComposite.layout();
					}
				}.run(true);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#bindValue(org.eclipse.core.databinding.observable
	 * .value .IObservableValue)
	 */
	@Override
	public void bindValue(IObservableValue modelValue, final ControlDecoration controlDecoration) {
		IObservableValue dateObserver = SWTObservables.observeSelection(dateWidget);
		IObservableValue timeObserver = SWTObservables.observeSelection(timeWidget);
		IObservableValue target = new DateAndTimeObservableValue(dateObserver, timeObserver);
		getDbc().bindValue(target, modelValue);

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#setEditable(boolean)
	 */
	@Override
	public void setEditable(boolean isEditable) {
		dateDeleteButton.setEnabled(isEditable);
		dateWidget.setEnabled(isEditable);
		timeWidget.setEnabled(isEditable);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPAttributeWidget#getControl()
	 */
	@Override
	public Control getControl() {
		return parentComposite;
	}

}
