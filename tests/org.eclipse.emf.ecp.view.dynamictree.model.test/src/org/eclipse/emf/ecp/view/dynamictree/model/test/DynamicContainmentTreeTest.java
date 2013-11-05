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

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
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
import org.eclipse.emf.ecp.view.model.VContainedElement;
import org.eclipse.emf.ecp.view.model.VControl;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.model.VElement;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.VViewFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DynamicContainmentTreeTest {

	private DomainRoot root;
	private Node<DynamicContainmentTree> node;
	private DynamicContainmentTree tree;

	private static final String ELEMENT_CONTAINER_ID = "111";
	private static final String ELEMENT_ID = "222";

	@Before
	public void setUp() throws Exception {

		root = ModelFactory.eINSTANCE.createDomainRoot();
		final DomainIntermediate intermediate = ModelFactory.eINSTANCE.createDomainIntermediate();
		root.setIntermediate(intermediate);

		final TestElementContainer elementContainer = ModelFactory.eINSTANCE.createTestElementContainer();
		elementContainer.setId(ELEMENT_CONTAINER_ID);
		intermediate.setTestElementContainer(elementContainer);

		final TestElement testElement = ModelFactory.eINSTANCE.createTestElement();
		testElement.setId(ELEMENT_ID);
		testElement.setParentId(ELEMENT_CONTAINER_ID);
		elementContainer.getTestElements().add(testElement);

		tree = ModelFactory.eINSTANCE.createDynamicContainmentTree();
		tree.getPathToRoot().add(ModelPackage.eINSTANCE.getDomainRoot_Intermediate());
		tree.getPathToRoot().add(ModelPackage.eINSTANCE.getDomainIntermediate_TestElementContainer());
		tree.setChildReference(ModelPackage.eINSTANCE.getTestElementContainer_TestElements());

		// used for validation
		final VControl childNameControl = VViewFactory.eINSTANCE.createControl();
		childNameControl.setDomainModelReference(
			createFeaturePathDomainModelReference(ModelPackage.eINSTANCE.getTestElement_Name()));
		tree.setChildComposite(childNameControl);

		// set up scoping
		final VControl viewControl = VViewFactory.eINSTANCE.createControl();
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
		final VFeaturePathDomainModelReference result = VViewFactory.eINSTANCE.createFeaturePathDomainModelReference();
		result.setDomainModelEFeature(feature);
		result.getDomainModelEReferencePath().addAll(Arrays.asList(references));
		return result;
	}

	@Test
	public void testFilter() {

		final List<Node<?>> filterVisibleNodes = CategorizationFilterHelper
			.filterNodes(node);

		assertEquals(1, filterVisibleNodes.size());
	}

	@Test
	public void testAddNodeToDomainNode() {
		final String id = "123";
		addItem(id, node);
		final List<Node<?>> children = node.getChildren();
		final Node<?> lastChild = children.get(children.size() - 1);
		final Object labelObjectOfLastChild = lastChild.getLabelObject();
		assertTrue(TestElement.class.isInstance(labelObjectOfLastChild));

		final TestElement testElement = (TestElement) labelObjectOfLastChild;

		assertEquals(id, testElement.getId());
		assertEquals(ELEMENT_CONTAINER_ID, testElement.getParentId());
	}

	@Test
	public void testValidationErrorInit() {
		final String id = "123";
		addItem(id, node);
		assertEquals("Severity must be error", Diagnostic.ERROR,
			tree.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testValidationErrorToError() {
		final String id = "123";
		addItem(id, node);
		final List<Node<?>> children = node.getChildren();
		final Node<?> lastChild = children.get(children.size() - 1);
		final Object labelObjectOfLastChild = lastChild.getLabelObject();
		assertTrue(TestElement.class.isInstance(labelObjectOfLastChild));

		final TestElement testElement = (TestElement) labelObjectOfLastChild;
		testElement.setName("foo");

		assertEquals("Severity must be error", Diagnostic.ERROR,
			tree.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testValidationErrorToOkByRemove() {
		final String id = "123";
		addItem(id, node);
		final List<Node<?>> children = node.getChildren();
		final Node<?> lastChild = children.get(children.size() - 1);
		final Object labelObjectOfLastChild = lastChild.getLabelObject();
		assertTrue(TestElement.class.isInstance(labelObjectOfLastChild));

		final TestElement testElement = (TestElement) labelObjectOfLastChild;
		final TestElementContainer container = (TestElementContainer) testElement.eContainer();

		container.getTestElements().clear();

		assertEquals("Severity must be ok", Diagnostic.OK,
			tree.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testValidationErrorToOk() {
		final String id = "123";
		addItem(id, node);
		final List<Node<?>> children = node.getChildren();
		final Node<?> lastChild = children.get(children.size() - 1);
		final Object labelObjectOfLastChild = lastChild.getLabelObject();
		assertTrue(TestElement.class.isInstance(labelObjectOfLastChild));

		final TestElement testElement = (TestElement) labelObjectOfLastChild;
		for (final TestElement el : ((TestElementContainer) testElement.eContainer()).getTestElements()) {
			el.setName("bar");
		}

		assertEquals("Severity must be ok", Diagnostic.OK,
			tree.getDiagnostic().getHighestSeverity());
	}

	@Test
	public void testAddNodeToTestElement() {
		final String id = "123456789012";
		@SuppressWarnings("unchecked")
		final Node<DynamicContainmentItem> existingItem = (Node<DynamicContainmentItem>) node.getChildren().get(
			node.getChildren().size() - 1);
		addItem(id, existingItem);

		final List<Node<?>> children = existingItem.getChildren();
		final Node<?> childNode = children.get(children.size() - 1);
		final Object labelObject = childNode.getLabelObject();
		assertTrue(TestElement.class.isInstance(labelObject));

		final TestElement testElement = (TestElement) labelObject;

		assertEquals(id, testElement.getId());
		assertEquals(ELEMENT_ID, testElement.getParentId());
	}

	public static Node<?> addItem(String id, Node<?> virtualParentNode) {

		EObject virtualParent = (EObject) virtualParentNode.getLabelObject();
		final TestElement newValue = ModelFactory.eINSTANCE.createTestElement();
		newValue.setParentId((String) virtualParent.eGet(virtualParent
			.eClass().getEStructuralFeature("id")));
		newValue.setId(id);

		VElement renderable = null;
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
		pi.setComposite((VContainedElement) EcoreUtil.copy(renderable));
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

	private static void resolveDomainReferences(VElement renderable,
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
