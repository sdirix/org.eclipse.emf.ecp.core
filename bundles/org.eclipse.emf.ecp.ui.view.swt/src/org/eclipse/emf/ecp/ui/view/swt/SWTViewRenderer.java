package org.eclipse.emf.ecp.ui.view.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RendererNode;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.Categorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.Rule;
import org.eclipse.emf.ecp.view.model.ShowRule;
import org.eclipse.emf.ecp.view.model.TreeCategory;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewPackage;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTViewRenderer extends AbstractSWTRenderer<View>  {

	@Override
	public RendererNode<Control> render(View view,
			final ECPControlContext controlContext,
			final AdapterFactoryItemDelegator adapterFactoryItemDelegator) throws NoRendererFoundException {
		
		EList<AbstractCategorization> categorizations = view.getCategorizations();
		
		if (categorizations.size() == 1 && categorizations.get(0) instanceof Category) {
				Category category = (Category) categorizations.get(0);
				RendererNode<Control> node = SWTRenderers.INSTANCE.render(getParent(), category.getComposite(), controlContext, adapterFactoryItemDelegator);
				return node;
	} else {
			
			 final Composite treeComposite = new Composite(getParent(), SWT.NONE);
			 // TODO: do we need to dispose anything here?
			 // Currently the adapter facotyr 
//			 treeComposite.addDisposeListener(new DisposeListener() {
//				 @Override
//				 public void widgetDisposed(DisposeEvent e) {
//					 composedAdapterFactory.dispose();
//				 } 
//			 });

			 GridDataFactory.fillDefaults()
			 	.align(SWT.FILL, SWT.FILL)
			 	.grab(true, true)
			 	.applyTo(treeComposite);			 	
			 GridLayoutFactory.fillDefaults()
			 	.numColumns(2)
			 	.equalWidth(false)
			 	.applyTo(treeComposite);
			 
			 final TreeViewer treeViewer = new TreeViewer(treeComposite, SWT.NONE);
			 
			 treeViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactoryItemDelegator.getAdapterFactory()) {
				 
				 @Override
				 public Object[] getElements(Object object) {
					 Object[] result = super.getElements(object);
					 return checkVisibility(result);
				 }

				 @Override
				 public Object[] getChildren(Object object) {
					 if (TreeCategory.class.isInstance(object)) {
						 TreeCategory treeCategory = (TreeCategory) object;
						 EObject current = controlContext.getModelElement();
						 for (EReference eReference : treeCategory.getPathToFeature()) {
							 current = (EObject) current.eGet(eReference);
						 }
						 Object[] result = super.getChildren(current);
						 List<Object> filtered = new ArrayList<Object>();
						 for (Object obj : result) {
							 if (((EReference) treeCategory.getTargetFeature()).getEReferenceType()
									 .isInstance(obj)) {
								 filtered.add(obj);
							 }
						 }
						 return filtered.toArray();
					 }

					 Object[] result = super.getChildren(object);
					 return checkVisibility(result);
				 }


				 @Override
				 public boolean hasChildren(Object object) {
					 if (Category.class.isInstance(object)) {
						 return false;
					 }
					 if (Categorization.class.isInstance(object)) {
						 return !((Categorization) object).getCategorizations().isEmpty();
					 }
					 if (TreeCategory.class.isInstance(object)) {
						 TreeCategory treeCategory = (TreeCategory) object;
						 EObject current = controlContext.getModelElement();
						 for (EReference eReference : treeCategory.getPathToFeature()) {
							 current = (EObject) current.eGet(eReference);
							 if (current == null) {
								 return false;
							 }
						 }
						 return !((List<?>) current.eGet(treeCategory.getTargetFeature())).isEmpty();
					 }
					 return super.hasChildren(object);
					 // return false;
				 }
			 });

			 // TODO: register label provider
			 treeViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactoryItemDelegator.getAdapterFactory()));
			 
			 final SWTRendererNode node = new SWTRendererNode(treeComposite, view, controlContext);


			 GridDataFactory.fillDefaults()
			 	.align(SWT.BEGINNING, SWT.FILL)
			 	.grab(false, true)
			 	.applyTo(treeViewer.getControl());
			 
			 
			 final ScrolledComposite scrolledComposite = new ScrolledComposite(treeComposite, SWT.V_SCROLL
					 | SWT.H_SCROLL | SWT.BORDER);
			 scrolledComposite.setShowFocusedControl(true);
			 scrolledComposite.setExpandVertical(true);
			 scrolledComposite.setExpandHorizontal(true);
			 scrolledComposite.setBackground(getParent().getBackground());
			 
			 final Composite childComposite = new Composite(scrolledComposite, SWT.NONE);
			 childComposite.setBackground(getParent().getBackground());

			 scrolledComposite.setContent(childComposite);
			 
			 GridLayoutFactory.fillDefaults()
			 	.applyTo(scrolledComposite);
			 GridDataFactory.fillDefaults()
			 	.align(SWT.FILL, SWT.FILL)
			 	.grab(true, true)
			 	.applyTo(scrolledComposite);

			 GridLayoutFactory.fillDefaults()
			 	.applyTo(childComposite);
			 GridDataFactory.fillDefaults()
			 	.align(SWT.FILL, SWT.FILL)
			 	.grab(true, true)
			 	.applyTo(childComposite);
			 
			 
			
			 // TODO: enable selection listener
			 treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

				 // private TreeItem lastSelection;

				 @Override
				 public void selectionChanged(SelectionChangedEvent event) {
					 
					 final TreeViewer treeViewer = new TreeViewer(treeComposite, SWT.NONE);
					 ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
				    			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
					 AdapterFactoryItemDelegator newAdapterFactoryItemDelegator = new AdapterFactoryItemDelegator(composedAdapterFactory);
						

					 TreeSelection treeSelection = (TreeSelection) event.getSelection();
					 Object selection = treeSelection.getFirstElement();
					 
					 if (selection == null) {
						 return;
					 }
					 
					 org.eclipse.emf.ecp.view.model.Composite composite = null;
					 boolean treeCategoryElement = false;
					 
					 if (Category.class.isInstance(selection)) {
						 Category category = (Category) selection;
						 composite = category.getComposite();
					 } else {
						 TreePath path = treeSelection.getPathsFor(selection)[0];
						 if (path.getSegmentCount() < 2)
							 return;

						 Object parent = null;
						 for (int i = path.getSegmentCount() - 2; i >= 0; i--) {

							 if (TreeCategory.class.isInstance(path.getSegment(i))) {
								 parent = path.getSegment(i);
								 break;
							 }
						 }
						 if (parent == null)
							 return;

						 TreeCategory category = (TreeCategory) parent;
						 composite = category.getChildComposite();
						 treeCategoryElement = true;
					 }

					 for (Control control : scrolledComposite.getChildren()) {
						 control.dispose();
					 }
//					 if (factory != null) {
//						 rendererContext.removeListener(factory);
//						 factory.dispose();
//					 }
//					 factory = new CompositeFactoryImpl();
//					 rendererContext.addListener(factory);
//					 Composite tabContent = factory.getComposite(scrolledComposite, composite,
//							 treeCategoryElement ? (EObject) selection : controlContext.getModelElement());
					 
					 
					 // TODO: duplicate code
					 final Composite childComposite = new Composite(scrolledComposite, SWT.NONE);
					 childComposite.setBackground(getParent().getBackground());

					 GridLayoutFactory.fillDefaults()
					 	.applyTo(childComposite);
					 GridDataFactory.fillDefaults()
					 	.align(SWT.FILL, SWT.FILL)
					 	.grab(true, true)
					 	.applyTo(childComposite);
			
					 scrolledComposite.setContent(childComposite);
					 
					 // TODO: dupliate code, maybe provider convenience render method for abstract categorization type
					 // TODO: current child node must be exchanged?
					 // TODO: create adapter factory item delegator
					 RendererNode<Control> childNode = null;
					 try {
							System.out.println("Rendering with adapter factory " + newAdapterFactoryItemDelegator);
							 childNode = SWTRenderers.INSTANCE.render(childComposite, (AbstractCategorization) selection, controlContext, newAdapterFactoryItemDelegator);
					 } catch (NoRendererFoundException e) {
						 // TODO Auto-generated catch block
						 e.printStackTrace();
					 } finally {
						 composedAdapterFactory.dispose();
					 }
					 
					 // TODO: node may be null here
					 // TODO: clarify how to handle validation
//					 factory.validationChanged(rendererContext.getValidationMap());
//					 Point point = childNode.getRenderedResult().computeSize(SWT.FILL, SWT.FILL);
//					 childNode.getRenderedResult().setSize(point);
//					 scrolledComposite.setMinSize(point);
					 childComposite.layout(true);
					 
					 childNode.getRenderedResult().setFocus();
					 node.getChildren().clear();
					 node.addChild(childNode);
				 }
			 });

			 // TODO: register context menu
//			 addContextMenu(treeViewer,article);
			 
			 treeViewer.setInput(view);
			 treeViewer.expandAll();
			 // TODO: set selection 
//			 Object selection = findFirstCategory(view);
			 
//			 if (selection != null) {
//				 treeViewer.setSelection(new StructuredSelection(selection));
//			 }
			 
			 AbstractCategorization abstractCategorization = categorizations.get(0);
			 RendererNode<Control> childNode = SWTRenderers.INSTANCE.render(childComposite, abstractCategorization, controlContext, adapterFactoryItemDelegator);
			 
			 node.addChild(childNode);
			 
			 Point point = childNode.getRenderedResult().computeSize(SWT.DEFAULT, SWT.DEFAULT);
			 childNode.getRenderedResult().setSize(point);
			 scrolledComposite.setMinSize(point);
			 childNode.getRenderedResult().setFocus();
			 
			 return node;

		}
	}

	/**
	 * 
	 * @param result
	 * @return
	 */
	private Object[] checkVisibility(Object[] result) {
		List<Object> objects = new ArrayList<Object>();
		for (Object ob : result) {
			if (!EObject.class.isInstance(ob))
				continue;
			EObject eObject = (EObject) ob;
			if (!ViewPackage.eINSTANCE.equals(eObject.eClass().getEPackage())) {
				objects.add(ob);
				continue;
			}
			if (!AbstractCategorization.class.isInstance(ob))
				continue;
			AbstractCategorization abstractCategorization = (AbstractCategorization) ob;
			Rule rule = abstractCategorization.getRule();
			if (ShowRule.class.isInstance(rule)) {
				// TODO: condition evaluation
//				boolean valid = ConditionEvaluator.evaluate(article, rule.getCondition()).evaluate();
//				if (valid == ((ShowRule) rule).isHide()) {
//					continue;
//				}
			}
			objects.add(ob);
		}
		return objects.toArray();
	}

}
