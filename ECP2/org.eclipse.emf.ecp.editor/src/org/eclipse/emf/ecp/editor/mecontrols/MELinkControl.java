/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.editor.ModelElementChangeListener;
import org.eclipse.emf.ecp.editor.mecontrols.melinkcontrol.AddReferenceAction;
import org.eclipse.emf.ecp.editor.mecontrols.melinkcontrol.NewReferenceAction;
import org.eclipse.emf.ecp.editor.mecontrols.melinkcontrol.ReferenceAction;
import org.eclipse.emf.ecp.editor.mecontrols.widgets.ECPWidget;
import org.eclipse.emf.ecp.editor.mecontrols.widgets.LinkWidget;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eugen Neufeld
 */
public class MELinkControl extends AbstractMEControl {

	private Label labelWidgetImage;

	private ControlDecoration controlDecoration;

	private Control control;

	private ModelElementChangeListener modelElementChangeListener;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#getEStructuralFeatureType()
	 */
	@Override
	protected Class<? extends EStructuralFeature> getEStructuralFeatureType() {
		return EReference.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#getClassType()
	 */
	@Override
	protected Class<?> getClassType() {
		return EObject.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#isMulti()
	 */
	@Override
	protected boolean isMulti() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#createControl(org.eclipse.swt.widgets.Composite,
	 * int)
	 */
	@Override
	protected Control createControl(Composite parent, final int style) {
		final Composite composite = getToolkit().createComposite(parent, style);
		composite.setBackgroundMode(SWT.INHERIT_FORCE);
		GridLayoutFactory.fillDefaults().numColumns(4).spacing(2, 0).applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(composite);

		labelWidgetImage = getToolkit().createLabel(composite, "    ");
		labelWidgetImage.setBackground(parent.getBackground());
		final Composite linkComposite = getToolkit().createComposite(composite, SWT.NONE);
		linkComposite.setLayout(new FillLayout());
		GridDataFactory.fillDefaults().grab(true, false).applyTo(linkComposite);
		createWidgetControl(linkComposite, style);

		modelElementChangeListener = new ModelElementChangeListener(getModelElement()) {

			@Override
			public void onChange(Notification notification) {
				if (notification.getFeature() == getStructuralFeature()) {
					createWidgetControl(linkComposite, style);
				}
			}
		};

		for (Action action : initActions()) {
			createButtonForAction(action, composite);
		}

		return composite;
	}

	private void createWidgetControl(final Composite composite, final int style) {
		if (control != null) {
			control.dispose();
			controlDecoration.dispose();
		}
		final EObject opposite = (EObject) getModelElement().eGet(getStructuralFeature());
		if (opposite != null) {
			ECPWidget widget = getWidget(opposite);
			control = widget.createWidget(getToolkit(), composite, style);
			widget.setEditable(isEditable());
		} else {
			control = getToolkit().createLabel(composite, "(Not Set)");
			control.setBackground(composite.getBackground());
			control.setForeground(composite.getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
		}
		// GridDataFactory.fillDefaults().grab(true, true).applyTo(control);

		controlDecoration = new ControlDecoration(control, SWT.RIGHT | SWT.TOP);
		controlDecoration.setDescriptionText("Invalid input");
		controlDecoration.setShowHover(true);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
			FieldDecorationRegistry.DEC_ERROR);
		controlDecoration.setImage(fieldDecoration.getImage());
		controlDecoration.hide();

		composite.layout();
	}

	/**
	 * Creates the actions for the control.
	 * 
	 * @return list of actions
	 */
	private List<Action> initActions() {
		List<Action> result = new ArrayList<Action>();
		AddReferenceAction addAction = new AddReferenceAction(getModelElement(), (EReference) getStructuralFeature(),
			getItemPropertyDescriptor(), getContext(), getShell());
		result.add(addAction);
		ReferenceAction newAction = new NewReferenceAction(getModelElement(), (EReference) getStructuralFeature(),
			getItemPropertyDescriptor(), getContext(), getShell());
		result.add(newAction);
		return result;
	}

	/**
	 * Creates a button for an action.
	 * 
	 * @param action
	 *            the action
	 */
	private void createButtonForAction(final Action action, Composite composite) {
		Button selectButton = getToolkit().createButton(composite, "", SWT.PUSH);
		selectButton.setImage(action.getImageDescriptor().createImage());
		selectButton.setToolTipText(action.getToolTipText());
		selectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				action.run();
			}

		});
	}

	/**
	 * @return
	 */
	private ECPWidget getWidget(EObject opposite) {

		return new LinkWidget(getModelElement(), opposite, (EReference) getStructuralFeature(), getContext());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl#getPriority()
	 */
	@Override
	protected int getPriority() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (control != null) {
			control.dispose();
			controlDecoration.dispose();
		}
		labelWidgetImage.dispose();
		modelElementChangeListener.remove();
	}

}
