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
package org.eclipse.emf.ecp.view.dynamictree.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.ECPAction;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.context.ViewModelContextImpl;
import org.eclipse.emf.ecp.view.dynamictree.model.DomainIntermediate;
import org.eclipse.emf.ecp.view.dynamictree.model.DomainRoot;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree;
import org.eclipse.emf.ecp.view.dynamictree.model.ModelFactory;
import org.eclipse.emf.ecp.view.dynamictree.model.ModelPackage;
import org.eclipse.emf.ecp.view.dynamictree.model.TestElement;
import org.eclipse.emf.ecp.view.dynamictree.model.TestElementContainer;
import org.eclipse.emf.ecp.view.model.Composite;
import org.eclipse.emf.ecp.view.model.Control;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.ViewFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DynamicContainmentTreeTest {

	private DomainRoot root;
	private Node<DynamicContainmentTree> node;

	private static final String elementContainerId = "111";
	private static final String elementId = "222";

	@Before
	public void setUp() throws Exception {

		root = ModelFactory.eINSTANCE.createDomainRoot();
		final DomainIntermediate intermediate = ModelFactory.eINSTANCE.createDomainIntermediate();
		root.setIntermediate(intermediate);

		final TestElementContainer elementContainer = ModelFactory.eINSTANCE.createTestElementContainer();
		elementContainer.setId(elementContainerId);
		intermediate.setTestElementContainer(elementContainer);

		final TestElement testElement = ModelFactory.eINSTANCE.createTestElement();
		testElement.setId(elementId);
		testElement.setParentId(elementContainerId);

		elementContainer.getTestElements().add(testElement);

		final DynamicContainmentTree tree = ModelFactory.eINSTANCE.createDynamicContainmentTree();
		tree.getPathToRoot().add(ModelPackage.eINSTANCE.getDomainRoot_Intermediate());
		tree.getPathToRoot().add(ModelPackage.eINSTANCE.getDomainIntermediate_TestElementContainer());
		tree.setChildReference(ModelPackage.eINSTANCE.getTestElementContainer_TestElements());

		final Control viewControlChild = ViewFactory.eINSTANCE.createControl();

		viewControlChild.setDomainModelReference(
			createFeaturePathDomainModelReference(ModelPackage.eINSTANCE.getTestElement_Id()));
		tree.setChildComposite(viewControlChild);

		final Control viewControl = ViewFactory.eINSTANCE.createControl();
		final VFeaturePathDomainModelReference modelRef = createFeaturePathDomainModelReference(
			ModelPackage.eINSTANCE.getTestElementContainer_Id(),
			ModelPackage.eINSTANCE.getDomainRoot_Intermediate(),
			ModelPackage.eINSTANCE.getDomainIntermediate_TestElementContainer());

		viewControl.setDomainModelReference(modelRef);
		tree.setComposite(viewControl);

		node = NodeBuilders.INSTANCE.build(tree, new DynamicContainmentTreeTestEditContext(
			root, new ViewModelContextImpl(tree, root)));
	}

	@After
	public void tearDown() throws Exception {

	}

	private VFeaturePathDomainModelReference createFeaturePathDomainModelReference(EStructuralFeature feature,
		EReference... references) {
		final VFeaturePathDomainModelReference modelReference2 = ViewFactory.eINSTANCE
			.createVFeaturePathDomainModelReference();
		for (final EReference eReference : references) {
			modelReference2.getDomainModelEReferencePath().add(eReference);
		}
		modelReference2.getDomainModelEReferencePath().add(ModelPackage.eINSTANCE.getDomainRoot_Intermediate());
		modelReference2.getDomainModelEReferencePath().add(
			ModelPackage.eINSTANCE.getDomainIntermediate_TestElementContainer());
		modelReference2.setDomainModelEFeature(ModelPackage.eINSTANCE.getTestElementContainer_Id());
		return modelReference2;
	}

	@Test
	public void testUVBNodesNotFiltered() {

		final List<Node<?>> filterVisibleNodes = AbstractCategorizationFilterHelper
			.filterNodes(node);

		assertEquals(1, filterVisibleNodes.size());
	}

	@Test
	public void testAddNodeToArticle() {
		final String id = "123";
		addPackingItem(id, node);
		final List<Node<?>> children = node.getChildren();
		final Node<?> childNode = children.get(children.size() - 1);
		final Object labelObject = childNode.getLabelObject();
		assertTrue(TestElement.class.isInstance(labelObject));

		final TestElement testElement = (TestElement) labelObject;

		assertEquals(id, testElement.getId());
		assertEquals(elementContainerId, testElement.getParentId());
	}

	@Test
	public void testAddNodeToPackingItem() {
		final String id = "123456789012";
		@SuppressWarnings("unchecked")
		final Node<DynamicContainmentItem> existingPackingItem = (Node<DynamicContainmentItem>) node.getChildren().get(
			node.getChildren().size() - 1);
		addPackingItem(id, existingPackingItem);

		final List<Node<?>> children = existingPackingItem.getChildren();
		final Node<?> childNode = children.get(children.size() - 1);
		final Object labelObject = childNode.getLabelObject();
		assertTrue(TestElement.class.isInstance(labelObject));

		final TestElement testElement = (TestElement) labelObject;

		assertEquals(id, testElement.getId());
		assertEquals(elementId, testElement.getParentId());
	}

	public static Node<?> addPackingItem(String id, Node<?> virtualParentNode) {

		EObject virtualParent = (EObject) virtualParentNode.getLabelObject();
		final TestElement newValue = ModelFactory.eINSTANCE.createTestElement();
		newValue.setParentId((String) virtualParent.eGet(virtualParent
			.eClass().getEStructuralFeature("id")));
		newValue.setId(id);

		Renderable renderable = null;
		DynamicContainmentTree tree = null;
		List<ECPAction> actions = null;

		if (!TestElementContainer.class.isInstance(virtualParent)) {
			virtualParent = virtualParent.eContainer();
			final DynamicContainmentItem item = (DynamicContainmentItem) virtualParentNode
				.getRenderable();

			EObject parent = item.eContainer();
			while (!DynamicContainmentTree.class.isInstance(parent)) {
				parent = parent.eContainer();
			}
			tree = (DynamicContainmentTree) parent;
		}
		actions = virtualParentNode.getActions();

		if (tree == null) {
			tree = (DynamicContainmentTree) virtualParentNode.getRenderable();
		}
		renderable = tree.getChildComposite();

		final ECPControlContext childContext = virtualParentNode.getControlContext()
			.createSubContext(newValue);
		final DynamicContainmentItem pi = ModelFactory.eINSTANCE.createDynamicContainmentItem();
		pi.setComposite((Composite) EcoreUtil.copy(renderable));
		pi.setDomainModel(newValue);
		resolveDomainReferences(pi, newValue);
		if (DynamicContainmentItem.class.isInstance(virtualParentNode.getRenderable())) {
			final DynamicContainmentItem parent = (DynamicContainmentItem) virtualParentNode
				.getRenderable();
			parent.getItems().add(pi);
		} else {
			tree.getItems().add(pi);
		}
		final Node<?> n = NodeBuilders.INSTANCE.build(pi, childContext);

		virtualParentNode.addChild(n);
		n.setLabelObject(newValue);
		n.setActions(actions);

		((TestElementContainer) virtualParent).getTestElements().add(newValue);

		return n;
	}

	private static void resolveDomainReferences(Renderable renderable,
		EObject domainModelRoot) {
		final TreeIterator<EObject> eAllContents = renderable.eAllContents();
		while (eAllContents.hasNext()) {
			final EObject eObject = eAllContents.next();

			if (VDomainModelReference.class.isInstance(eObject)) {
				final VDomainModelReference modelReference = (VDomainModelReference) eObject;
				final boolean resolve = modelReference.resolve(domainModelRoot);
				if (!resolve) {
					// log
				}

			}
		}
	}
}
