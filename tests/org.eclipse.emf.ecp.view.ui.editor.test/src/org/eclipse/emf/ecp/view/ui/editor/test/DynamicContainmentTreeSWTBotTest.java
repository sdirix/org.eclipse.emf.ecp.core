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
package org.eclipse.emf.ecp.view.ui.editor.test;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.edit.spi.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.ECPAction;
import org.eclipse.emf.ecp.internal.ui.view.builders.NodeBuilders;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.categorization.model.VCategorization;
import org.eclipse.emf.ecp.view.categorization.model.VCategorizationElement;
import org.eclipse.emf.ecp.view.categorization.model.VCategorizationFactory;
import org.eclipse.emf.ecp.view.context.ViewModelContextImpl;
import org.eclipse.emf.ecp.view.dynamictree.model.DomainIntermediate;
import org.eclipse.emf.ecp.view.dynamictree.model.DomainRoot;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentItem;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTree;
import org.eclipse.emf.ecp.view.dynamictree.model.DynamicContainmentTreeDomainModelReference;
import org.eclipse.emf.ecp.view.dynamictree.model.ModelFactory;
import org.eclipse.emf.ecp.view.dynamictree.model.ModelPackage;
import org.eclipse.emf.ecp.view.dynamictree.model.TestElement;
import org.eclipse.emf.ecp.view.dynamictree.model.TestElementContainer;
import org.eclipse.emf.ecp.view.dynamictree.model.test.DynamicContainmentTreeTestEditContext;
import org.eclipse.emf.ecp.view.model.VContainedElement;
import org.eclipse.emf.ecp.view.model.VControl;
import org.eclipse.emf.ecp.view.model.VDomainModelReference;
import org.eclipse.emf.ecp.view.model.VElement;
import org.eclipse.emf.ecp.view.model.VFeaturePathDomainModelReference;
import org.eclipse.emf.ecp.view.model.VView;
import org.eclipse.emf.ecp.view.model.VViewFactory;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;

public class DynamicContainmentTreeSWTBotTest extends ECPCommonSWTBotTest {

	private Node<DynamicContainmentTree> node;

	private static final String ELEMENT_CONTAINER_ID = "111";
	private static final String ELEMENT_ID = "222";
	private static final String TEST_ELEMENT_NAME = "foo";
	private static final String TEST_ELEMENT_NAME_2 = "bar";

	@Override
	public VView createView() {

		// set up scoping
		final DynamicContainmentTree tree = ModelFactory.eINSTANCE.createDynamicContainmentTree();
		tree.getPathToRoot().add(ModelPackage.eINSTANCE.getDomainRoot_Intermediate());
		tree.getPathToRoot().add(ModelPackage.eINSTANCE.getDomainIntermediate_TestElementContainer());
		tree.setChildReference(ModelPackage.eINSTANCE.getTestElementContainer_TestElements());

		// used for validation
		final VControl childNameControl = VViewFactory.eINSTANCE.createControl();

		final DynamicContainmentTreeDomainModelReference reference =
			ModelFactory.eINSTANCE.createDynamicContainmentTreeDomainModelReference();
		final VFeaturePathDomainModelReference rootRef = VViewFactory.eINSTANCE.createFeaturePathDomainModelReference();

		rootRef.getDomainModelEReferencePath().addAll(tree.getPathToRoot());
		rootRef.setDomainModelEFeature(ModelPackage.eINSTANCE.getTestElementContainer_TestElements());

		reference.setPathFromRoot(rootRef);
		reference.setPathFromBase(createFeaturePathDomainModelReference(ModelPackage.eINSTANCE.getTestElement_Name()));

		childNameControl.setDomainModelReference(reference
			);
		tree.setChildComposite(childNameControl);

		final VControl viewControl = VViewFactory.eINSTANCE.createControl();
		final VFeaturePathDomainModelReference modelRef = createFeaturePathDomainModelReference(
			ModelPackage.eINSTANCE.getTestElementContainer_Id(),
			ModelPackage.eINSTANCE.getDomainRoot_Intermediate(),
			ModelPackage.eINSTANCE.getDomainIntermediate_TestElementContainer());
		viewControl.setDomainModelReference(modelRef);
		tree.setComposite(viewControl);

		final VView view = VViewFactory.eINSTANCE.createView();
		view.setRootEClass(ModelPackage.eINSTANCE.getDomainRoot());

		final VCategorization categorization = VCategorizationFactory.eINSTANCE.createCategorization();
		categorization.setName("quux");
		categorization.getCategorizations().add(tree);
		final VCategorizationElement element = VCategorizationFactory.eINSTANCE.createCategorizationElement();
		element.getCategorizations().add(categorization);
		view.getChildren().add(element);
		return view;
	}

	private VFeaturePathDomainModelReference createFeaturePathDomainModelReference(EStructuralFeature feature,
		EReference... references) {
		final VFeaturePathDomainModelReference result = VViewFactory.eINSTANCE.createFeaturePathDomainModelReference();
		result.setDomainModelEFeature(feature);
		result.getDomainModelEReferencePath().addAll(Arrays.asList(references));
		return result;
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.ui.editor.test.ECPCommonSWTBotTest#createDomainObject()
	 */
	@Override
	public EObject createDomainObject() {
		final DomainRoot root = ModelFactory.eINSTANCE.createDomainRoot();
		final DomainIntermediate intermediate = ModelFactory.eINSTANCE.createDomainIntermediate();
		root.setIntermediate(intermediate);

		final TestElementContainer elementContainer = ModelFactory.eINSTANCE.createTestElementContainer();
		elementContainer.setId(ELEMENT_CONTAINER_ID);
		intermediate.setTestElementContainer(elementContainer);

		final TestElement testElement = ModelFactory.eINSTANCE.createTestElement();
		testElement.setId(ELEMENT_ID);
		testElement.setName(TEST_ELEMENT_NAME);
		testElement.setParentId(ELEMENT_CONTAINER_ID);
		elementContainer.getTestElements().add(testElement);
		return root;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.ui.editor.test.ECPCommonSWTBotTest#createContext(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecp.view.model.VView)
	 */
	@Override
	public ECPControlContext createContext(EObject domainObject, VView view) {
		return new DynamicContainmentTreeTestEditContext(
			domainObject, new ViewModelContextImpl(view, domainObject));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.ui.editor.test.ECPCommonSWTBotTest#logic()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void logic() {

		final Node<?> childNodenode2 = getViewNode().getChildren().get(0).getChildren().get(0);
		node = (Node<DynamicContainmentTree>) childNodenode2.getChildren().get(0);

		final String id = "123";
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				addItem(id, node);
			}
		});
		final List<Node<?>> children = node.getChildren();
		final Node<?> lastChild = children.get(children.size() - 1);
		final Object labelObjectOfLastChild = lastChild.getLabelObject();
		assertTrue(TestElement.class.isInstance(labelObjectOfLastChild));

		final TestElement testElement = (TestElement) labelObjectOfLastChild;

		UIThreadRunnable.syncExec(new VoidResult() {

			public void run() {
				// causes UI update
				testElement.setName(TEST_ELEMENT_NAME_2);
				bot.tree().getAllItems()[0].getItems()[0].expand();
				bot.tree().getAllItems()[0].getItems()[0].getItems()[0].select();
				assertEquals("foo", bot.text().getText());

				bot.tree().getAllItems()[0].getItems()[0].getItems()[1].select();
				assertEquals("bar", bot.text().getText());
			}
		});

	}
}
