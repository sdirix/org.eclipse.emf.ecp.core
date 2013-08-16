/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.internal.ui.view.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.ECPAction;
import org.eclipse.emf.ecp.ui.view.RendererContext.ValidationListener;
import org.eclipse.emf.ecp.view.context.ModelChangeNotification;
import org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener;
import org.eclipse.emf.ecp.view.model.Renderable;

/**
 * 
 * @author emueller
 * 
 * @param <T>
 *            the type of the actual control
 */
// FIXME:
public class Node<T extends Renderable> implements ValidationListener, ModelChangeListener {

	protected List<RenderingResultDelegator> delegators;
	protected int severity;
	private final T viewModelElement;
	private final List<Node<?>> children;
	private final ECPControlContext controlContext;
	private Object labelObject;

	private List<ECPAction> actions;
	private final List<SelectedChildNodeListener> selectedChildNodeListeners;

	/**
	 * Constructor.
	 * 
	 * @param model
	 *            the view model element that should be associated with this node
	 * @param controlContext
	 *            the control context for this node
	 */
	public Node(T model, ECPControlContext controlContext) {
		this.viewModelElement = model;
		this.labelObject = model;
		this.controlContext = controlContext;
		this.children = new ArrayList<Node<?>>();
		this.delegators = new ArrayList<RenderingResultDelegator>();
		this.selectedChildNodeListeners = new ArrayList<SelectedChildNodeListener>();
	}

	/**
	 * Returns the view model element associated with this node.
	 * 
	 * @return the view model element associated with this node
	 */
	public T getRenderable() {
		return viewModelElement;
	}

	/**
	 * Adds a child to this node.
	 * 
	 * @param node
	 *            child to be added as a child to this node
	 */
	public void addChild(Node<? extends Renderable> node) {
		children.add(node);
	}

	/**
	 * Returns all children of this node.
	 * 
	 * @return all children of this node
	 */
	public List<Node<?>> getChildren() {
		return children;
	}

	/**
	 * Delegates the enable call to all {@link RenderingResultDelegator}s
	 * of this node.
	 * 
	 * @param isEnabled
	 *            whether this node's {@link RenderingResultDelegator}s
	 *            should be enabled
	 */
	public void enable(final boolean isEnabled) {
		// isEnabled = shouldBeEnabled;
		for (final Node<? extends Renderable> child : getChildren()) {
			child.enable(isEnabled);
		}
		for (final RenderingResultDelegator delegator : delegators) {
			delegator.enable(isEnabled);
		}
	}

	/**
	 * Delegates the show call to all {@link RenderingResultDelegator}s
	 * of this node.
	 * 
	 * @param isVisible
	 *            whether this node's {@link RenderingResultDelegator}s
	 *            should be visible
	 */
	public void show(final boolean isVisible) {
		// this.isVisible = isVisible;
		for (final Node<? extends Renderable> child : getChildren()) {
			child.show(isVisible);
		}
		for (final RenderingResultDelegator delegator : delegators) {
			delegator.show(isVisible);
		}
	}

	/**
	 * Delegates the layout call to all {@link RenderingResultDelegator}s
	 * of this node.
	 */
	public void layout() {
		for (final RenderingResultDelegator delegator : delegators) {
			delegator.layout();
		}
	}

	/**
	 * Template method for performing cleanup tasks. Called by {@code dispose}.
	 * This method only calls {@code cleanup()} on the {@link RenderingResultDelegator}s
	 * of this node.
	 */
	public void cleanup() {
		for (final RenderingResultDelegator delegator : delegators) {
			delegator.cleanup();
		}
		delegators.clear();
		for (final Node<? extends Renderable> child : getChildren()) {
			child.cleanup();
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.RendererContext.ValidationListener#validationChanged(java.util.Map)
	 */
	public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {

		severity = calculateSeverity(affectedObjects);

		for (final Node<?> child : getChildren()) {
			child.validationChanged(affectedObjects);
		}

		notifyDelegatorsAboutValidationChanged(affectedObjects);
	}

	/**
	 * Notifies the {@link RenderingResultDelegator} that the validation of the
	 * given affected objects has changed.
	 * 
	 * @param affectedObjects
	 *            the object that are affected by the validation
	 */
	protected void notifyDelegatorsAboutValidationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {
		for (final RenderingResultDelegator delegator : delegators) {
			delegator.validationChanged(affectedObjects);
		}
	}

	/**
	 * Calculates the severity returned by {@code getSeverity}.
	 * 
	 * @param affectedObjects
	 *            the object that are affected by the validation
	 * @return a {@link Diagnostic} constant that tells about the severity of this node
	 */
	protected int calculateSeverity(Map<EObject, Set<Diagnostic>> affectedObjects) {
		int max = Diagnostic.OK;
		if (affectedObjects.containsKey(viewModelElement)) {
			for (final Diagnostic diagnostic : affectedObjects.get(viewModelElement)) {
				if (viewModelElement != labelObject && !diagnostic.getData().get(0).equals(labelObject)) {
					continue;
				}
				if (diagnostic.getSeverity() > max) {
					max = diagnostic.getSeverity();
				}
			}
		}

		return max;
	}

	/**
	 * Whether this node is a leaf.
	 * 
	 * @return {@code true} if this node is a leaf, otherwise {@code false}
	 */
	public boolean isLeaf() {
		return false;
	}

	/**
	 * Disposes this node.
	 */
	public void dispose() {
		for (final Node<? extends Renderable> child : getChildren()) {
			child.dispose();
		}
		children.clear();
		cleanup();
		selectedChildNodeListeners.clear();
	}

	/**
	 * Adds an {@link RenderingResultDelegator} that will perform
	 * the task of updating the actual UI elements.
	 * 
	 * @param delegator
	 *            the rending delegator to be added
	 */
	public void addRenderingResultDelegator(RenderingResultDelegator delegator) {
		delegators.add(delegator);
		delegator.enable(viewModelElement.isEnabled());
		delegator.show(viewModelElement.isVisible());
	}

	/**
	 * Removes the given {@link RenderingResultDelegator}.
	 * 
	 * @param delegator
	 *            the rending delegator to be removed
	 */
	public void removeRenderingResultDelegator(RenderingResultDelegator delegator) {
		delegators.remove(delegator);
	}

	/**
	 * Whether this node is visible.
	 * 
	 * @return {@code true}, if this node is visible, {@code false} otherwise
	 */
	public boolean isVisible() {
		return viewModelElement.isVisible();
	}

	/**
	 * Whether this node is enabled.
	 * 
	 * @return {@code true}, if this node is enabled, {@code false} otherwise
	 */
	public boolean isEnabled() {
		return viewModelElement.isEnabled();
	}

	/**
	 * Returns the control context of this node.
	 * 
	 * @return the control context of this node
	 */
	public ECPControlContext getControlContext() {
		return controlContext;
	}

	/**
	 * Returns the label object of this node, that is the domain object.
	 * 
	 * @return the domain object
	 */
	public Object getLabelObject() {
		return labelObject;
	}

	/**
	 * Sets the domain object.
	 * 
	 * @param object
	 *            the domain object to be set
	 */
	public void setLabelObject(Object object) {
		labelObject = object;
	}

	/**
	 * Actions of this node.
	 * 
	 * @return the actions
	 */
	public List<ECPAction> getActions() {
		return actions;
	}

	/**
	 * Sets the available actions on this node.
	 * 
	 * @param actions the actions to set
	 */
	public void setActions(List<ECPAction> actions) {
		this.actions = actions;
	}

	/**
	 * Whether this node has an rendering result delegator.
	 * 
	 * @return {@code true}, if this node has a rendering result delegator, {@code false} otherwise
	 */
	public boolean isLifted() {
		return !delegators.isEmpty();
	}

	/**
	 * Returns the validation severity associated with this node.
	 * 
	 * @return the validation severity of this node
	 * 
	 */
	public int getSeverity() {
		return severity;
	}

	/**
	 * Adds an selection child node listener, that is a listener that will be notified
	 * if any of the children of this node has been selected.
	 * 
	 * @param listener
	 *            the listener to be added
	 */
	public void addSelectedChildNodeListener(SelectedChildNodeListener listener) {
		selectedChildNodeListeners.add(listener);
	}

	/**
	 * Removes the given selection child node listener.
	 * 
	 * @param listener
	 *            the listener to be removed
	 */
	public void removeSelectedChildNodeListener(SelectedChildNodeListener listener) {
		selectedChildNodeListeners.add(listener);
	}

	/**
	 * Notifies all {@link SelectedChildNodeListener} that a child of this node
	 * has been selected.
	 * 
	 * @param selectedChild
	 *            the selected child node
	 */
	public void fireSelectedChildNodeChanged(Node<?> selectedChild) {
		for (final SelectedChildNodeListener listener : selectedChildNodeListeners) {
			listener.childSelected(selectedChild);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.context.ViewModelContext.ModelChangeListener#notifyChange(org.eclipse.emf.ecp.view.context.ModelChangeNotification)
	 */
	public void notifyChange(ModelChangeNotification notification) {
		if (notification.getNotifier() == viewModelElement) {
			enable(viewModelElement.isEnabled());
			show(viewModelElement.isVisible());
		} else {
			for (final Node<?> child : getChildren()) {
				child.notifyChange(notification);
			}
		}
	}
}
