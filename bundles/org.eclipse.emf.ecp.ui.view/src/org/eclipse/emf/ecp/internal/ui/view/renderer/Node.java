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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.ConditionEvaluator;
import org.eclipse.emf.ecp.internal.ui.view.ECPAction;
import org.eclipse.emf.ecp.ui.view.RendererContext.ValidationListener;
import org.eclipse.emf.ecp.view.model.Condition;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.EnableRule;
import org.eclipse.emf.ecp.view.model.Group;
import org.eclipse.emf.ecp.view.model.LeafCondition;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.ShowRule;

/**
 * 
 * @author emueller
 * 
 * @param <CONTROL>
 *            the type of the actual control
 */
// FIXME:
public class Node<T extends Renderable> implements ValidationListener {

	private T viewModelElement;
	private List<Node<?>> children;
	protected List<RenderingResultDelegator> delegators;
	private ECPControlContext controlContext;
	private Object labelObject;

	protected boolean isVisible;
	protected boolean isEnabled;
	protected int severity;

	private List<ECPAction> actions;
	private List<SelectedChildNodeListener> selectedChildNodeListeners;

	public Node(T model, ECPControlContext controlContext) {
		this.viewModelElement = model;
		this.labelObject = model;
		this.controlContext = controlContext;
		this.children = new ArrayList<Node<?>>();
		this.delegators = new ArrayList<RenderingResultDelegator>();
		this.selectedChildNodeListeners = new ArrayList<SelectedChildNodeListener>();
		isVisible = evalShowCondition();
		isEnabled = evalEnableCondition();
	}

	public Node(T model, ECPControlContext context, boolean isVisible) {
		this(model, context);
		this.isVisible = isVisible;
	}

	public T getRenderable() {
		return viewModelElement;
	}

	public void addChild(Node<? extends Renderable> node) {
		children.add(node);
	}

	public List<Node<?>> getChildren() {
		return children;
	}

	// FIXME: change type of notification to attribute
	public void checkShow(Notification notification) {

		if (isLeafCondition()) {
			final Condition condition = viewModelElement.getRule().getCondition();
			if (isShowRule()) {
				final LeafCondition leaf = (LeafCondition) condition;
				EAttribute attr = null;

				if (isEAttributeNotification(notification)) {
					attr = (EAttribute) notification.getFeature();
					if (leaf.getAttribute().getFeatureID() == attr.getFeatureID()) {

						final boolean isVisible = evalShowCondition();

						if (!isVisible) {
							show(false);
							final TreeIterator<EObject> eAllContents = viewModelElement.eAllContents();
							while (eAllContents.hasNext()) {
								final EObject o = eAllContents.next();
								unset(controlContext, o);
							}
							unset(controlContext, viewModelElement);
						} else {
							show(true);
						}

						layout();
					}
				}
			}

		}
		for (final Node<?> child : getChildren()) {
			child.checkShow(notification);
		}
	}

	public boolean hasRule() {
		return viewModelElement.getRule() != null;
	}

	public boolean evalShowCondition() {

		if (!hasRule()) {
			return true;
		}

		final Condition condition = viewModelElement.getRule().getCondition();

		if (!isShowRule()) {
			return true;
		}

		final boolean v = ConditionEvaluator.evaluate(controlContext.getModelElement(), condition);

		if (((ShowRule) viewModelElement.getRule()).isHide()) {
			if (v) {
				return false;
			} else {
				return true;
			}
		} else {
			if (v) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * @return
	 */
	private boolean isShowRule() {
		return ShowRule.class.isInstance(viewModelElement.getRule());
	}

	public boolean evalEnableCondition() {

		if (viewModelElement.getRule() == null) {
			return true;
		}

		final Condition condition = viewModelElement.getRule().getCondition();

		if (!EnableRule.class.isInstance(viewModelElement.getRule())) {
			return true;
		}

		final boolean v = ConditionEvaluator.evaluate(controlContext.getModelElement(), condition);

		if (((EnableRule) viewModelElement.getRule()).isDisable()) {
			if (v) {
				return false;
			} else {
				return true;
			}
		} else {
			if (v) {
				return true;
			} else {
				return false;
			}
		}
	}

	private void unset(ECPControlContext context, EObject eObject) {
		// TODO: missing unset for custom controls
		if (eObject instanceof Control) {
			final Control control = (Control) eObject;
			EObject parent = context.getModelElement();
			for (final EReference eReference : control.getPathToFeature()) {
				parent = (EObject) parent.eGet(eReference);
			}
			final ECPControlContext editContext = context.createSubContext(parent);
			final EStructuralFeature targetFeature = control.getTargetFeature();
			if (targetFeature.isMany()) {
				final Collection<?> collection = (Collection<?>) editContext.getModelElement().eGet(targetFeature);
				if (collection.size() > 0) {
					collection.clear();
				}
			} else {
				final Object targetFeatureValue = editContext.getModelElement().eGet(targetFeature);

				if (targetFeatureValue == null) {
					return;
				}

				if (!targetFeatureValue.equals(targetFeature.getDefaultValue())) {
					editContext.getModelElement().eSet(targetFeature, // null);
						targetFeature.getDefaultValue());
				}
			}
		} else if (eObject instanceof Group) {
			final Group group = (Group) eObject;
			final EList<org.eclipse.emf.ecp.view.model.Composite> composites = group.getComposites();
			for (final org.eclipse.emf.ecp.view.model.Composite composite : composites) {
				unset(context, composite);
			}
		}
	}

	// FIXME: change type of notification to attribute
	public void checkEnable(Notification notification) {

		if (isLeafCondition()) {
			// FIXME: MKVMR
			final Condition condition = viewModelElement.getRule().getCondition();
			if (EnableRule.class.isInstance(viewModelElement.getRule())) {

				final LeafCondition leaf = (LeafCondition) condition;
				EAttribute attr = null;

				if (isEAttributeNotification(notification)) {
					attr = (EAttribute) notification.getFeature();
					if (leaf.getAttribute().getFeatureID() == attr.getFeatureID()) {

						final boolean isEnabled = evalEnableCondition();

						if (!isEnabled) {
							enable(false);
						} else {
							enable(true);
						}
					}
				}
			}
		}
		for (final Node<?> child : getChildren()) {
			child.checkEnable(notification);
		}

	}

	private boolean isLeafCondition() {
		if (viewModelElement.getRule() == null) {
			return false;
		}

		return viewModelElement.getRule().getCondition() instanceof LeafCondition;
	}

	private boolean isEAttributeNotification(Notification notification) {
		if (notification.getFeature() instanceof EAttribute) {
			return true;
		}

		return false;
	}

	public void enable(final boolean shouldBeEnabled) {
		isEnabled = shouldBeEnabled & evalEnableCondition();
		for (final Node<? extends Renderable> child : getChildren()) {
			child.enable(shouldBeEnabled);
		}
		for (final RenderingResultDelegator delegator : delegators) {
			delegator.enable(shouldBeEnabled);
		}
	}

	public void show(final boolean isVisible) {
		this.isVisible = isVisible & evalShowCondition();
		for (final Node<? extends Renderable> child : getChildren()) {
			child.show(isVisible);
		}
		for (final RenderingResultDelegator delegator : delegators) {
			delegator.show(isVisible);
		}
	}

	public void layout() {
		for (final RenderingResultDelegator delegator : delegators) {
			delegator.layout();
		}
	}

	public void cleanup() {
		for (final RenderingResultDelegator delegator : delegators) {
			delegator.cleanup();
		}
		delegators.clear();
		for (final Node<? extends Renderable> child : getChildren()) {
			child.cleanup();
		}
	}

	public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {

		severity = calculateSeverity(affectedObjects);

		for (final Node<?> child : getChildren()) {
			child.validationChanged(affectedObjects);
		}

		notifyDelegatorsAboutValidationChanged(affectedObjects);
	}

	/**
	 * @param affectedObjects
	 */
	protected void notifyDelegatorsAboutValidationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {
		for (final RenderingResultDelegator delegator : delegators) {
			delegator.validationChanged(affectedObjects);
		}
	}

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

	public boolean isLeaf() {
		return false;
	}

	public void dispose() {
		for (final Node<? extends Renderable> child : getChildren()) {
			child.dispose();
		}
		children.clear();
		cleanup();
		selectedChildNodeListeners.clear();
	}

	public void addRenderingResultDelegator(RenderingResultDelegator delegator) {
		delegators.add(delegator);
	}

	public void removeRenderingResultDelegator(RenderingResultDelegator delegator) {
		delegators.remove(delegator);
	}

	/**
	 * Whether this node is visible.
	 * 
	 * @return {@code true}, if this node is visible, {@code false} otherwise
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Whether this node is enabled.
	 * 
	 * @return {@code true}, if this node is enabled, {@code false} otherwise
	 */
	public boolean isEnabled() {
		return isEnabled;
	}

	public ECPControlContext getControlContext() {
		return controlContext;
	}

	public Object getLabelObject() {
		return labelObject;
	}

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

	public boolean isLifted() {
		return !delegators.isEmpty();
	}

	public int getSeverity() {
		return severity;
	}

	public void addSelectedChildNodeListener(SelectedChildNodeListener listener) {
		selectedChildNodeListeners.add(listener);
	}

	public void removeSelectedChildNodeListener(SelectedChildNodeListener listener) {
		selectedChildNodeListeners.add(listener);
	}

	public void fireSelectedChildNodeChanged(Node<?> selectedChild) {
		for (final SelectedChildNodeListener listener : selectedChildNodeListeners) {
			listener.childSelected(selectedChild);
		}
	}
}
