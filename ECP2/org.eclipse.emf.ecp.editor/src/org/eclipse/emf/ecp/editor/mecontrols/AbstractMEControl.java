/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.ecp.editor.mecontrols;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.editor.EditorModelelementContext;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class for the ME controls.
 * 
 * @author helming
 */
public abstract class AbstractMEControl {
	public static final Map<Class<?>, Class<?>> primitives = new HashMap<Class<?>, Class<?>>();

	static {
		primitives.put(Boolean.class, boolean.class);
		primitives.put(Byte.class, byte.class);
		primitives.put(Short.class, short.class);
		primitives.put(Character.class, char.class);
		primitives.put(Integer.class, int.class);
		primitives.put(Long.class, long.class);
		primitives.put(Float.class, float.class);
		primitives.put(Double.class, double.class);
	}

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

	/**
	 * the editingDomain.
	 */
	private EditingDomain editingDomain;

	private boolean showLabel;

	private IItemPropertyDescriptor itemPropertyDescriptor;

	private EditorModelelementContext context;

	private EStructuralFeature structuralFeature;

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
	 * If a control can render a feature of a modelelement.
	 * 
	 * @param itemPropertyDescriptor
	 *            the {@link IItemPropertyDescriptor}
	 * @param modelElement
	 *            the modelelement
	 * @return the priority
	 */
	public int canRender(IItemPropertyDescriptor itemPropertyDescriptor, EObject modelElement) {
		Object feature = itemPropertyDescriptor.getFeature(modelElement);

		if (getEStructuralFeatureType().isInstance(feature) && isAssignable((EStructuralFeature) feature)
			&& isMultiplicityCorrect((EStructuralFeature) feature)) {

			return getPriority();
		}

		return AbstractMEControl.DO_NOT_RENDER;
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
	private boolean isAssignable(EStructuralFeature feature) {
		Class<?> featureInstance = getFeatureClass(feature);
		if (featureInstance.isPrimitive()) {
			return primitives.get(getClassType()).isAssignableFrom(featureInstance);
		}
		return getClassType().isAssignableFrom(featureInstance);
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
	 * @return the editingDomain
	 */
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	/**
	 * @param editingDomain
	 *            the editingDomain to set
	 */
	private void setEditingDomain(EditingDomain editingDomain) {
		this.editingDomain = editingDomain;
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
	public void setContext(EditorModelelementContext context) {
		this.context = context;
	}

	/**
	 * Getter for the {@link EditorModelelementContext}.
	 * 
	 * @return the {@link EditorModelelementContext}
	 */
	public EditorModelelementContext getContext() {
		return context;
	}

	/**
	 * Default constructor.
	 */
	public AbstractMEControl() {
		super();
	}

	public void dispose() {

	}

	/**
	 * Method for applying a custom layout data to widgets, as for {@link MERichTextControl}.
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
		EObject modelElement, EditorModelelementContext context, FormToolkit toolkit) {
		setContext(context);
		setEditingDomain(context.getEditingDomain());
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
