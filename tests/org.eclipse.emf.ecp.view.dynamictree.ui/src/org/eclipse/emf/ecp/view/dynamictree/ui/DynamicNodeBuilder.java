/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edgar Mueller - initial API and implementation
 */
package org.eclipse.emf.ecp.view.dynamictree.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.ECPAction;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree;
import org.eclipse.emf.ecp.view.dynamictree.model.ModelFactory;
import org.eclipse.emf.ecp.view.dynamictree.model.TestElement;
import org.eclipse.emf.ecp.view.dynamictree.model.TestElementContainer;
import org.eclipse.emf.ecp.view.model.VAction;
import org.eclipse.emf.ecp.view.model.VContainedElement;
import org.eclipse.emf.ecp.view.model.util.ViewModelUtil;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.osgi.framework.Bundle;

/**
 * Node buidler for a {@link DynamicContainmentTree}.
 * 
 * @author emueller
 * 
 */
public class DynamicNodeBuilder implements NodeBuilder<DynamicContainmentTree> {

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilder#build(org.eclipse.emf.ecp.view.model.VElement,
	 *      org.eclipse.emf.ecp.edit.spi.ECPControlContext, org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator)
	 */
	public Node<DynamicContainmentTree> build(DynamicContainmentTree dynamicTree,
		ECPControlContext controlContext,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator) {

		final Node<DynamicContainmentTree> node = new Node<DynamicContainmentTree>(dynamicTree, controlContext);

		final Node<VContainedElement> compositeNode = NodeBuilders.INSTANCE.build(dynamicTree.getComposite(),
			controlContext);

		node.addChild(compositeNode);

		EObject current = controlContext.getModelElement();
		for (final EReference eReference : dynamicTree.getPathToRoot()) {
			EObject eGet = (EObject) current.eGet(eReference);
			if (eGet == null) {
				final EObject newObject = EcoreUtil.create(eReference.getEReferenceType());
				current.eSet(eReference, newObject);
				eGet = newObject;
			}
			current = eGet;
		}

		final TestElementContainer type = (TestElementContainer) current;
		node.setLabelObject(type);
		dynamicTree.setDomainModel(type);

		final VAction[] actions = dynamicTree.getActions().toArray(new VAction[0]);
		@SuppressWarnings("unchecked")
		final List<TestElement> testElements = (List<TestElement>) adapterFactoryItemDelegator.getChildren(type);
		for (final TestElement testElement : testElements) {
			final ECPControlContext childContext = controlContext.createSubContext(testElement);
			final DynamicContainmentItem item = ModelFactory.eINSTANCE.createDynamicContainmentItem();
			item.setComposite(EcoreUtil.copy(dynamicTree.getChildComposite()));
			item.setDomainModel(testElement);
			ViewModelUtil.resolveDomainReferences(item, testElement);
			dynamicTree.getItems().add(item);

			final Node<DynamicContainmentItem> n = NodeBuilders.INSTANCE.build(item,
				childContext);
			node.addChild(n);
			n.setLabelObject(testElement);
			createActions(n, actions);

			addChildren(n, item, adapterFactoryItemDelegator,
				childContext, actions);
		}

		if (actions.length > 0) {
			createActions(node, actions[0]);
		}

		return node;
	}

	/**
	 * Creates the actions for the given node.
	 * 
	 * @param node
	 *            the {@link Node} for which to create the action
	 * @param actions
	 *            the {@link VAction}s to be created
	 */
	public static void createActions(Node<?> node, VAction... actions) {
		final List<ECPAction> nodeActions = new ArrayList<ECPAction>();
		for (final VAction action : actions) {
			try {
				final Bundle bundle = Platform.getBundle(action.getBundle());
				if (bundle == null) {
					// TODO externalize strings
					throw new ClassNotFoundException(action.getClassName()
						+ " cannot be loaded because bundle " + action.getBundle() //$NON-NLS-1$
						+ " cannot be resolved"); //$NON-NLS-1$
				}
				final Class<?> loadClass = bundle.loadClass(action.getClassName());
				final ECPAction treeAction = (ECPAction) loadClass.getConstructors()[0].newInstance();
				nodeActions.add(treeAction);
			} catch (final ClassNotFoundException e) {
				e.printStackTrace();
			} catch (final IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		node.setActions(nodeActions);
	}

	private void addChildren(Node<?> node, DynamicContainmentItem containmentItem,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		ECPControlContext controlContext, VAction[] actions) {
		@SuppressWarnings("unchecked")
		final List<TestElement> children = (List<TestElement>) adapterFactoryItemDelegator
			.getChildren(controlContext.getModelElement());

		for (final TestElement testElement : children) {
			final ECPControlContext childContext = controlContext.createSubContext(testElement);
			final DynamicContainmentItem pi = ModelFactory.eINSTANCE.createDynamicContainmentItem();
			pi.setComposite(EcoreUtil.copy(containmentItem.getComposite()));
			pi.setDomainModel(testElement);
			ViewModelUtil.resolveDomainReferences(pi, testElement);

			final EList<DynamicContainmentItem> uvbPackingItems = containmentItem.getItems();

			uvbPackingItems.add(pi);

			final Node<DynamicContainmentItem> n = NodeBuilders.INSTANCE.build(pi, childContext);
			node.addChild(n);
			n.setLabelObject(testElement);
			createActions(n, actions);
			addChildren(n, pi, adapterFactoryItemDelegator, childContext, actions);
		}
	}

}
