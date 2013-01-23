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

package org.eclipse.emf.ecp.editor.mecontrols;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.EditModelElementContext;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Abstract class for the ME controls.
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

	/**
	 * the modelElement.
	 */
	private EObject modelElement;

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

	protected abstract Class<? extends EStructuralFeature> getEStructuralFeatureType();

	protected abstract Class<?> getClassType();

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

	protected abstract boolean isMulti();

	/**
	 * @return
	 */
	protected boolean isAssignable(Class<?> featureClass) {
		return getClassType().isAssignableFrom(featureClass);
	}

	/**
	 * @return
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
	 * @return the modelElement
	 */
	public EObject getModelElement() {
		return modelElement;
	}

	/**
	 * @param modelElement
	 *            the modelElement to set
	 */
	private void setModelElement(EObject modelElement) {
		this.modelElement = modelElement;
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
	 * Setter for the {@link EditorModelelementContext}.
	 * 
	 * @param context
	 *            the {@link EditorModelelementContext}
	 */
	public void setContext(EditModelElementContext context) {
		this.context = context;
	}

	/**
	 * Getter for the {@link EditorModelelementContext}.
	 * 
	 * @return the {@link EditorModelelementContext}
	 */
	public EditModelElementContext getContext() {
		return context;
	}

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
		setModelElement(modelElement);
		setToolkit(toolkit);
		setItemPropertyDescriptor(itemPropertyDescriptor);

		setStructuralFeature();
		return createControl(parent, style);

	}

	/**
   * 
   */
	private void setStructuralFeature() {
		Object feature = getItemPropertyDescriptor().getFeature(getModelElement());
		structuralFeature = (EStructuralFeature) feature;
	}

	/**
	 * Shall be overriden to create the control.
	 * 
	 * @param parent
	 *            the paren composite
	 * @param style
	 *            the SWT style
	 * @return the create Control
	 */
	protected abstract Control createControl(Composite parent, int style);

	protected boolean isEditable() {
		return getItemPropertyDescriptor().canSetProperty(getModelElement());
	}

	protected Shell getShell() {
		return shell;
	}

	private Shell shell;

	/**
	 * @param shell
	 */
	public void setShell(Shell shell) {
		this.shell = shell;
	}
}
