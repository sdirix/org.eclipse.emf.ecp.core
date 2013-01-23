/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Jonas Helming - initial API and implementation
 * 
 *******************************************************************************/

package org.eclipse.emf.ecp.editor.controls;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Abstract class for the controls.
 * 
 * @author helming
 */
public abstract class AbstractControl {

	/**
	 * The default constant in case the widgets decides it shouldn't render the attribute.
	 */
	public static final int DO_NOT_RENDER = -1;

	/**
	 * gui toolkit used for rendering.
	 */
	private FormToolkit toolkit;

	private boolean showLabel;

	private IItemPropertyDescriptor itemPropertyDescriptor;

	private EditModelElementContext context;

	private EStructuralFeature structuralFeature;

	/**
	 * Default constructor.
	 */
	public AbstractControl() {
		super();
	}

	/**
	 * If a control can render a feature of a modelelement.
	 * 
	 * @param itemPropertyDescriptor
	 *            the {@link IItemPropertyDescriptor}
	 * @param modelElement
	 *            the modelelement
	 * @return the priority
	 */
	public int canRender(IItemPropertyDescriptor itemPropertyDescriptor, EObject modelElement) {
		EStructuralFeature feature = (EStructuralFeature) itemPropertyDescriptor.getFeature(modelElement);

		if (getEStructuralFeatureType().isInstance(feature) && isAssignable(getFeatureClass(feature))
			&& isMultiplicityCorrect(feature)) {

			return getPriority();
		}

		return AbstractControl.DO_NOT_RENDER;
	}

	/**
	 * @return the attribute
	 */
	public EStructuralFeature getStructuralFeature() {
		return structuralFeature;
	}

	/**
	 * Returns the Class of this {@link EStructuralFeature}. Is either {@link org.eclipse.emf.ecore.EAttribute
	 * EAttribute} or {@link org.eclipse.emf.ecore.EReference EReference}.
	 * 
	 * @return the class of this {@link EStructuralFeature}
	 */
	protected abstract Class<? extends EStructuralFeature> getEStructuralFeatureType();

	/**
	 * The supported Class of this control.
	 * 
	 * @return the supported class
	 */
	protected abstract Class<?> getSupportedClassType();

	/**
	 * @return the toolkit
	 */
	public FormToolkit getToolkit() {
		return toolkit;
	}

	/**
	 * @return
	 */
	private boolean isMultiplicityCorrect(EStructuralFeature feature) {
		return feature.isMany() == isMulti();
	}

	/**
	 * Whether this control supports multi values.
	 * 
	 * @return true if {@link EStructuralFeature#isMany()} is supported, false otherwise
	 */
	protected abstract boolean isMulti();

	/**
	 * Checks whether the classType of the control is assignable to the class of the {@link EStructuralFeature}.
	 * 
	 * @param featureClass the {@link Class} of the feature to check
	 * @return true isAssignable returns true, false otherwise
	 */
	protected boolean isAssignable(Class<?> featureClass) {
		return getSupportedClassType().isAssignableFrom(featureClass);
	}

	/**
	 * Return the Class of this {@link EStructuralFeature}. This is used to identify whether this control can be
	 * assigned to the class of the {@link EStructuralFeature}.
	 * 
	 * @param feature the {@link EStructuralFeature} to get the {@link Class} for
	 * @return the InstanceClass of this {@link EStructuralFeature}
	 */
	protected Class<?> getFeatureClass(EStructuralFeature feature) {
		return feature.getEType().getInstanceClass();
	}

	/**
	 * Returns the priority by which the control should be rendered. The priority determines which control will be used
	 * to
	 * render a specific type since multiple controls may be registered to render the same type. <br>
	 * This implementation returns 1. A subclass providing a control that should be used instead of the default
	 * implementations should override this method.
	 * 
	 * @return an integer value representing the priority by which the control gets rendered
	 */
	protected int getPriority() {
		return 1;
	}

	/**
	 * @param toolkit
	 *            the toolkit to set
	 */
	private void setToolkit(FormToolkit toolkit) {
		this.toolkit = toolkit;
	}

	/**
	 * @return if the label for this control should be shown.
	 */
	public boolean getShowLabel() {
		return showLabel;
	}

	/**
	 * Sets if the label should be shown.
	 * 
	 * @param show
	 *            if the Label should be shown
	 */
	public void setShowLabel(boolean show) {
		showLabel = show;
	}

	/**
	 * Setter for the {@link IItemPropertyDescriptor}.
	 * 
	 * @param itemPropertyDescriptor
	 *            the {@link IItemPropertyDescriptor}
	 */
	private void setItemPropertyDescriptor(IItemPropertyDescriptor itemPropertyDescriptor) {
		this.itemPropertyDescriptor = itemPropertyDescriptor;
	}

	/**
	 * Getter for the {@link IItemPropertyDescriptor}.
	 * 
	 * @return the {@link IItemPropertyDescriptor}
	 */
	public IItemPropertyDescriptor getItemPropertyDescriptor() {
		return itemPropertyDescriptor;
	}

	/**
	 * Setter for the {@link EditModelElementContext}.
	 * 
	 * @param context
	 *            the {@link EditModelElementContext}
	 */
	public void setContext(EditModelElementContext context) {
		this.context = context;
	}

	/**
	 * Getter for the {@link EditModelElementContext}.
	 * 
	 * @return the {@link EditModelElementContext}
	 */
	public EditModelElementContext getContext() {
		return context;
	}

	/**
	 * Disposes the contents of this control.
	 */
	public void dispose() {
		toolkit.dispose();
	}

	/**
	 * Method for applying a custom layout data to widgets, as for MERichTextControl.
	 */
	public void applyCustomLayoutData() {
		// by default none. must me implemented by the subclass.
	}

	/**
	 * Creates the widget for this control.
	 * 
	 * @param parent
	 *            the parent composite
	 * @param itemPropertyDescriptor
	 *            the {@link IItemPropertyDescriptor}
	 * @param modelElement
	 *            the model element
	 * @param context
	 *            the context of the model element
	 * @param toolkit
	 *            the {@link FormToolkit}
	 * @param style
	 *            the style
	 * @return the widget
	 */
	public Control createControl(Composite parent, int style, IItemPropertyDescriptor itemPropertyDescriptor,
		EObject modelElement, EditModelElementContext context, FormToolkit toolkit) {
		setContext(context);
		setToolkit(toolkit);
		setItemPropertyDescriptor(itemPropertyDescriptor);

		setStructuralFeature();
		return createControl(parent, style);

	}

	/**
   * 
   */
	private void setStructuralFeature() {
		Object feature = getItemPropertyDescriptor().getFeature(getContext().getModelElement());
		structuralFeature = (EStructuralFeature) feature;
	}

	/**
	 * Shall be overridden to create the control.
	 * 
	 * @param parent
	 *            the paren composite
	 * @param style
	 *            the SWT style
	 * @return the create Control
	 */
	protected abstract Control createControl(Composite parent, int style);

	/**
	 * Whether the current {@link EStructuralFeature} if this {@link EObject} is editable.
	 * 
	 * @return true if editable, false otherwise
	 */
	protected boolean isEditable() {
		return getItemPropertyDescriptor().canSetProperty(getContext().getModelElement());
	}

	/**
	 * the Shell of this control.
	 * 
	 * @return the {@link Shell}
	 */
	protected Shell getShell() {
		return shell;
	}

	private Shell shell;

	/**
	 * Sets the shell for this control.
	 * 
	 * @param shell the {@link Shell}
	 */
	public void setShell(Shell shell) {
		this.shell = shell;
	}
}
