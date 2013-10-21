/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Edagr Mueller - initial API and implementation
 * Eugen Neufeld - Refactoring
 ******************************************************************************/
package org.eclipse.emf.ecp.ui.view.swt.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecp.edit.internal.swt.util.OverlayImageDescriptor;
import org.eclipse.emf.ecp.internal.ui.view.emf.AdapterFactoryLabelProvider;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultDelegatorAdapter;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultRow;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.ViewPackage;
import org.eclipse.emf.ecp.view.model.impl.ControlImpl;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ITableItemLabelProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

// TODO: Auto-generated Javadoc
/**
 * The Class SWTViewRenderer.
 */
public class SWTViewRenderer extends AbstractSWTRenderer<View> {

	/** The Constant INSTANCE. */
	public static final SWTViewRenderer INSTANCE = new SWTViewRenderer();

	/** The error descriptor. */
	private static ImageDescriptor ERROR_DESCRIPTOR = Activator.getImageDescriptor("icons/error_decorate.png");

	/** The warning descriptor. */
	private static ImageDescriptor WARNING_DESCRIPTOR = Activator.getImageDescriptor("icons/warning_decorate.png");

	// protected TreeViewer treeViewer;

	// TODO: move somewhere and filter based on interface types
	/** The filtered classes. */
	@SuppressWarnings("serial")
	private final Set<Class<?>> filteredClasses = new LinkedHashSet<Class<?>>() {
		{
			// add(ColumnCompositeImpl.class);
			// add(ColumnImpl.class);
			add(ControlImpl.class);
			// add(TableControlImpl.class);
			// add(GroupImpl.class);
		}
	};

	private AdapterImpl adapter;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.ui.view.swt.internal.AbstractSWTRenderer#renderSWT(org.eclipse.emf.ecp.internal.ui.view.renderer.Node,
	 *      org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator, java.lang.Object[])
	 */
	@Override
	public List<RenderingResultRow<Control>> renderSWT(final Node<View> viewNode,
		final AdapterFactoryItemDelegator adapterFactoryItemDelegator,

		Object... initData) throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		final Composite parent = getParentFromInitData(initData);
		final View view = viewNode.getRenderable();
		adapter = new EContentAdapter() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#notifyChanged(org.eclipse.emf.common.notify.Notification)
			 */
			@Override
			public void notifyChanged(Notification msg) {
				super.notifyChanged(msg);
				if (treeViewer == null) {
					return;
				}
				if (AbstractCategorization.class.isInstance(msg.getNotifier())
					&& ViewPackage.eINSTANCE.getRenderable_Diagnostic().equals(msg.getFeature())) {
					if (msg.getEventType() == Notification.SET) {
						treeViewer.refresh();
					}
				}
			}

		};
		view.eAdapters().add(adapter);
		parent.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent event) {
				view.eAdapters().remove(adapter);
			}
		});
		final EList<AbstractCategorization> categorizations = view.getCategorizations();

		if (categorizations.size() == 0) {
			return renderChildren(parent, viewNode, adapterFactoryItemDelegator);
		}
		else if (categorizations.size() == 1 && categorizations.get(0) instanceof Category) {
			final List<RenderingResultRow<Control>> resultRows = SWTRenderers.INSTANCE.render(parent, viewNode
				.getChildren().get(0),
				adapterFactoryItemDelegator);
			viewNode.addRenderingResultDelegator(withSWT(resultRows.get(0).getMainControl()));
			return resultRows;
		} else {
			final Composite composite = createComposite(parent);
			final TreeViewer treeViewer = createTreeViewer(composite, adapterFactoryItemDelegator, viewNode);
			createdEditorPane(composite);

			viewNode.addRenderingResultDelegator(withSWT(composite));

			initTreeViewer(treeViewer, viewNode);

			return createResult(composite);
		}
	}

	/**
	 * Render children.
	 * 
	 * @param parent the parent
	 * @param node the node
	 * @param adapterFactoryItemDelegator the adapter factory item delegator
	 * @return the composite
	 * @throws NoRendererFoundException the no renderer found exception
	 */
	private List<RenderingResultRow<Control>> renderChildren(Composite parent, Node<View> node,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator)
		throws NoRendererFoundException {
		final Composite columnComposite = new Composite(parent, SWT.NONE);
		columnComposite.setBackground(parent.getBackground());

		columnComposite.setLayout(getLayoutHelper().getColumnLayout(2, false));

		node.addRenderingResultDelegator(withSWT(columnComposite));

		for (final Node<? extends Renderable> child : node.getChildren()) {

			List<RenderingResultRow<Control>> resultRows;
			try {
				resultRows = SWTRenderers.INSTANCE.render(
					columnComposite, child, adapterFactoryItemDelegator);
			} catch (final NoPropertyDescriptorFoundExeption e) {
				continue;
			}

			// TOOD; when does this case apply?
			if (resultRows == null) {
				continue;
			}

			setLayoutDataForResultRows(resultRows);
		}

		return createResult(columnComposite);
	}

	/**
	 * Created editor pane.
	 * 
	 * @param composite the composite
	 */
	protected void createdEditorPane(Composite composite) {
		editorComposite = createScrolledComposite(composite);
		editorComposite.setExpandHorizontal(true);
		editorComposite.setExpandVertical(true);
		editorComposite.setShowFocusedControl(true);

		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(editorComposite);

	}

	/**
	 * Filter visible nodes.
	 * 
	 * @param treeViewer the tree viewer
	 * @param node the node
	 * @return the list< node<? extends renderable>>
	 */
	protected List<Node<? extends Renderable>> filterVisibleNodes(TreeViewer treeViewer, Node<? extends Renderable> node) {
		final List<Node<?>> result = new ArrayList<Node<?>>();
		final List<Node<?>> children = node.getChildren();
		for (final Node<?> child : children) {
			if (child.isVisible()) {
				if (filteredClasses.contains(child.getLabelObject().getClass())) {
					result.addAll(filterVisibleNodes(treeViewer, child));
				} else {
					result.add(child);
				}
			}
		}
		return result;
	}

	/**
	 * Creates the scrolled composite.
	 * 
	 * @param parent the parent
	 * @return the scrolled composite
	 */
	private ScrolledComposite createScrolledComposite(Composite parent) {
		final ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL
			| SWT.BORDER);
		scrolledComposite.setShowFocusedControl(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setBackground(parent.getBackground());

		final Composite childComposite = new Composite(scrolledComposite, SWT.NONE);
		childComposite.setBackground(parent.getBackground());

		return scrolledComposite;
	}

	/**
	 * Creates the tree viewer.
	 * 
	 * @param composite the composite
	 * @param adapterFactoryItemDelegator the adapter factory item delegator
	 * @param viewNode the view node
	 * @return the tree viewer
	 */
	protected TreeViewer createTreeViewer(final Composite composite,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator, final Node<View> viewNode
		) {
		treeViewer = new TreeViewer(composite);

		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.FILL).grab(false, true).hint(400, SWT.DEFAULT)
			.applyTo(treeViewer.getTree());

		treeViewer.setContentProvider(new ITreeContentProvider() {

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// TODO Auto-generated method stub
			}

			public void dispose() {
				// TODO Auto-generated method stub
			}

			public boolean hasChildren(Object element) {
				final Object[] children = getChildren(element);

				if (children == null) {
					return false;
				}

				return children.length > 0;
			}

			public Object getParent(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			public Object[] getElements(Object inputElement) {
				return getChildren(inputElement);
			}

			public Object[] getChildren(Object parentElement) {
				final Node<?> node = (Node<?>) parentElement;

				final List<Node<?>> visisbleNodes = filterVisibleNodes(treeViewer, node);

				return visisbleNodes.toArray();
			}
		});

		treeViewer.setLabelProvider(new TreeTableLabelProvider(adapterFactoryItemDelegator.getAdapterFactory()));

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			private Node<?> lastSelection;

			public void selectionChanged(SelectionChangedEvent event) {

				final ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
					ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
				final AdapterFactoryItemDelegator newAdapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
					composedAdapterFactory);

				try {
					final TreeSelection treeSelection = (TreeSelection) event.getSelection();
					final Object selection = treeSelection.getFirstElement();
					addButtons(treeViewer, treeSelection, viewNode.getControlContext().getModelElement());

					if (selection == null) {
						return;
					}
					if (lastSelection != null) {
						lastSelection.cleanup();
					}
					lastSelection = (Node<?>) selection;
					final Composite childComposite = createComposite(editorComposite);

					childComposite.setBackground(composite.getBackground());
					editorComposite.setContent(childComposite);

					// TODO: REVIEW
					if (Node.class.isInstance(selection)) {
						final Node<?> node = (Node<?>) selection;
						try {
							final List<RenderingResultRow<Control>> resultRows = SWTRenderers.INSTANCE.render(
								childComposite, node,
								newAdapterFactoryItemDelegator);
							GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true)
								.applyTo(resultRows.get(0).getMainControl());
							viewNode.fireSelectedChildNodeChanged(node);
						} catch (final NoRendererFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (final NoPropertyDescriptorFoundExeption e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						childComposite.layout();
						final Point point = childComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
						editorComposite.setMinSize(point);
					}
				} finally {
					composedAdapterFactory.dispose();
				}
			}
		});

		addTreeEditor(treeViewer, viewNode.getControlContext().getModelElement(), viewNode.getRenderable());

		return treeViewer;

	}

	/**
	 * Inits the tree viewer.
	 * 
	 * @param treeViewer the tree viewer
	 * @param viewNode the view node
	 */
	protected void initTreeViewer(final TreeViewer treeViewer, Node<View> viewNode) {
		viewNode.addRenderingResultDelegator(new RenderingResultDelegatorAdapter() {
			@Override
			public void validationChanged(Map<EObject, Set<Diagnostic>> affectedObjects) {
				treeViewer.refresh();
			}
		});

		treeViewer.setInput(viewNode);
		treeViewer.expandAll();
		treeViewer.setSelection(new StructuredSelection(viewNode.getChildren().get(0)));
	}

	/**
	 * Creates the composite.
	 * 
	 * @param parent the parent
	 * @return the composite
	 */
	private Composite createComposite(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(parent.getBackground());

		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(composite);
		return composite;
	}

	/** The editors. */
	private final List<TreeEditor> editors = new ArrayList<TreeEditor>();

	/** The editor composite. */
	private ScrolledComposite editorComposite;

	/**
	 * The {@link TreeViewer} rendered by this renderer.
	 */
	protected TreeViewer treeViewer;

	/**
	 * Adds the tree editor.
	 * 
	 * @param treeViewer the tree viewer
	 * @param modelElement the model element
	 * @param view the view
	 */
	protected void addTreeEditor(final TreeViewer treeViewer, final EObject modelElement, View view) {
		// The text column
		final Tree tree = treeViewer.getTree();
		final TreeColumn columnText = new TreeColumn(tree, SWT.NONE);
		columnText.setWidth(300);
		columnText.setAlignment(SWT.FILL);

		int maxActions = 0;
		final Iterator<EObject> viewContents = view.eAllContents();
		while (viewContents.hasNext()) {
			final EObject object = viewContents.next();
			if (AbstractCategorization.class.isInstance(object)) {
				final AbstractCategorization abstractCategorization = (AbstractCategorization) object;
				if (maxActions < abstractCategorization.getActions().size()) {
					maxActions = abstractCategorization.getActions().size();
				}
			}
		}
		for (int i = 0; i < maxActions; i++) {
			// The column
			final TreeColumn column = new TreeColumn(tree, SWT.NONE);
			column.setWidth(50);

			final TreeEditor editor = new TreeEditor(tree);
			// The editor must have the same size as the cell and must
			// not be any smaller than 50 pixels.
			editor.horizontalAlignment = SWT.CENTER;
			editor.grabHorizontal = true;
			editor.minimumWidth = 50;
			editor.setColumn(i + 1);

			editors.add(editor);
		}

		tree.addTreeListener(new TreeListener() {

			public void treeExpanded(TreeEvent e) {
			}

			public void treeCollapsed(TreeEvent e) {
				cleanUpTreeEditors();
			}
		});

	}

	// Clean up any previous editor control
	/**
	 * Clean up tree editors.
	 */
	private void cleanUpTreeEditors() {
		for (final TreeEditor editor : editors) {
			final Control oldEditor = editor.getEditor();
			if (oldEditor != null) {
				oldEditor.dispose();
			}
		}
	}

	/**
	 * Adds the buttons.
	 * 
	 * @param treeViewer the tree viewer
	 * @param treeSelection the tree selection
	 * @param modelElement the model element
	 */
	protected void addButtons(final TreeViewer treeViewer, TreeSelection treeSelection, EObject modelElement) {

		cleanUpTreeEditors();

		if (treeSelection.getPaths().length == 0) {
			return;
		}

		// Identify the selected row
		final TreeItem item = treeViewer.getTree().getSelection()[0];
		if (item == null) {
			return;
		}

		final Node<?> object = (Node<?>) treeSelection.getFirstElement();
		if (object.getActions() == null) {
			return;
		}
		for (int i = 0; i < object.getActions().size(); i++) {
			final ECPTreeViewAction action = (ECPTreeViewAction) object.getActions().get(i);
			final TreeEditor editor = editors.get(i);
			action.init(treeViewer, treeSelection, editor, modelElement);
			action.execute();
		}
	}

	/**
	 * The Class TreeTableLabelProvider.
	 */
	protected class TreeTableLabelProvider extends AdapterFactoryLabelProvider implements ITableItemLabelProvider {

		/**
		 * Instantiates a new tree table label provider.
		 * 
		 * @param adapterFactory the adapter factory
		 */
		public TreeTableLabelProvider(AdapterFactory adapterFactory) {
			super(adapterFactory);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.internal.ui.view.emf.AdapterFactoryLabelProvider#getColumnImage(java.lang.Object,
		 *      int)
		 */
		@Override
		public Image getColumnImage(Object object, int columnIndex) {

			if (columnIndex != 0) {
				return null;
			}

			Image image = super.getColumnImage(object, columnIndex);

			if (!Node.class.isInstance(object)) {
				return image;
			}

			final Node<?> node = (Node<?>) object;
			image = super.getImage(node.getLabelObject());
			ImageDescriptor overlay = null;
			switch (node.getSeverity()) {

			case Diagnostic.ERROR:
				overlay = ERROR_DESCRIPTOR;
				break;
			case Diagnostic.WARNING:
				overlay = WARNING_DESCRIPTOR;
				break;
			default:
				break;
			}

			if (overlay == null) {
				return image;
			}
			final OverlayImageDescriptor imageDescriptor = new OverlayImageDescriptor(image, overlay,
				OverlayImageDescriptor.LOWER_RIGHT);
			final Image resultImage = imageDescriptor.createImage();

			return resultImage;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.ecp.internal.ui.view.emf.AdapterFactoryLabelProvider#getColumnText(java.lang.Object,
		 *      int)
		 */
		@Override
		public String getColumnText(Object object, int columnIndex) {

			if (columnIndex != 0) {
				return "";
			}

			final Node<?> node = (Node<?>) object;

			return super.getText(node.getLabelObject());
		}

	}

}
