package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecp.edit.ECPControlContext;
import org.eclipse.emf.ecp.internal.ui.view.emf.AdapterFactoryLabelProvider;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.TreeCategory;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class SWTViewRenderer extends AbstractSWTRenderer<View>  {
	
	@Override
	public Control render(Node<View> node,
			final ECPControlContext controlContext,
			final AdapterFactoryItemDelegator adapterFactoryItemDelegator)
			throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
				
		View view = node.getRenderable();
		EList<AbstractCategorization> categorizations = view.getCategorizations();
		
		if (categorizations.size() == 1 && categorizations.get(0) instanceof Category) {
			Control control = SWTRenderers.INSTANCE.render(getParent(), node.getChildren().get(0), controlContext, adapterFactoryItemDelegator);
			node.lift(withSWT(control));
			return control;
		} else {
			Composite composite = createComposite(getParent());
			TreeViewer treeViewer = createTreeViewer(composite);
			final ScrolledComposite scrolledComposite = createScrolledComposite(composite);
			GridDataFactory.fillDefaults()
                .grab(true, true)
                .align(SWT.FILL, SWT.FILL)
                .applyTo(scrolledComposite);
			
			Composite editorComposite = createComposite(scrolledComposite);
			scrolledComposite.setContent(editorComposite);

			treeViewer.setContentProvider(new ITreeContentProvider() {

				@Override
				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
					// TODO Auto-generated method stub

				}

				@Override
				public void dispose() {
					// TODO Auto-generated method stub

				}

				@Override
				public boolean hasChildren(Object element) {
					Object[] children = getChildren(element);

					if (children == null) {
						return false;
					}

					return children.length > 0;
				}

				@Override
				public Object getParent(Object element) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Object[] getElements(Object inputElement) {
					return getChildren(inputElement);
				}

				@Override
				public Object[] getChildren(Object parentElement) {
					Node node = (Node) parentElement;
					return node.getChildren().toArray();
				}
			});

			// TODO: check if adapter factory will be disposed 
			treeViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactoryItemDelegator.getAdapterFactory()) {

				@Override
				public String getText(Object object) {
					Node node = (Node) object;
					return super.getText(node.getRenderable());
				}

				@Override
				public Image getImage(Object object) {
					Node node = (Node) object;
					return super.getImage(node.getRenderable());
				}
			});

			treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent event) {

					ComposedAdapterFactory composedAdapterFactory = 
							new ComposedAdapterFactory(
									ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
					AdapterFactoryItemDelegator newAdapterFactoryItemDelegator = 
							new AdapterFactoryItemDelegator(composedAdapterFactory);

					try {
						TreeSelection treeSelection = (TreeSelection) event.getSelection();
						Object selection = treeSelection.getFirstElement();

						if (selection == null) {
							return;
						}
						
						
						 for (Control control : scrolledComposite.getChildren()) {
							 control.dispose();
						 }
						 
						 final Composite childComposite = createComposite(scrolledComposite);
						 childComposite.setBackground(getParent().getBackground());
						 scrolledComposite.setContent(childComposite);

						// TODO: REVIEW
						if (Node.class.isInstance(selection)) {
							Node node = (Node) selection;
							Renderable renderable = node.getRenderable();
							 
							if (renderable instanceof Category) {
								Category category = (Category) renderable;
								try {
									SWTRenderers.INSTANCE.render(childComposite, node, controlContext, newAdapterFactoryItemDelegator);
								} catch (NoRendererFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (NoPropertyDescriptorFoundExeption e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							} else {
								 TreePath path = treeSelection.getPathsFor(selection)[0];
								 
								 if (path.getSegmentCount() < 2) {
									 return;
								 }

								 Object parent = null;
								 for (int i = path.getSegmentCount() - 2; i >= 0; i--) {
									 Object segment = path.getSegment(i);
									 if (segment instanceof Node) {
										 Node n = (Node) segment;
										 if (TreeCategory.class.isInstance(n.getRenderable())) {
											 parent = path.getSegment(i);
											 break;											 
										 }
									 }
								 }
								 
								 if (parent == null)
									 return;
							}
							
							childComposite.layout();
						}	
					} finally {
						composedAdapterFactory.dispose();
					}
				}
			});

			treeViewer.setInput(node);
			
			node.lift(withSWT(composite));

			return composite;
		}
	}

	/**
	 * @return
	 */
	private ScrolledComposite createScrolledComposite(Composite parent) {
		final ScrolledComposite scrolledComposite = new ScrolledComposite(parent, 
			SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		scrolledComposite.setShowFocusedControl(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setBackground(getParent().getBackground());

		final Composite childComposite = new Composite(scrolledComposite, SWT.NONE);
		childComposite.setBackground(getParent().getBackground());

		return scrolledComposite;
	}

	/**
	 * @param composite
	 * @return
	 */
	private TreeViewer createTreeViewer(Composite composite) {
		TreeViewer treeViewer = new TreeViewer(composite);
		GridDataFactory.fillDefaults()
		.grab(false, true)
		.align(SWT.BEGINNING, SWT.FILL)
		.applyTo(treeViewer.getControl());
		return treeViewer;
	}

	/**
	 * @return
	 */
	private Composite createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(parent.getBackground());
		
		GridLayoutFactory.fillDefaults()
			.numColumns(2)
			.equalWidth(false)
			.applyTo(composite);
		GridDataFactory.fillDefaults()
			.grab(true, true)
			.align(SWT.FILL, SWT.FILL)
			.applyTo(composite);
		return composite;
	}

	
}
