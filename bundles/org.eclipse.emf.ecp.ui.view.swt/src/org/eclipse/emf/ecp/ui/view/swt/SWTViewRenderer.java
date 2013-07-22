package org.eclipse.emf.ecp.ui.view.swt;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.internal.swt.util.OverlayImageDescriptor;
import org.eclipse.emf.ecp.internal.ui.view.emf.AdapterFactoryLabelProvider;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.RenderingResultDelegatorAdapter;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;
import org.eclipse.emf.ecp.view.model.impl.ColumnCompositeImpl;
import org.eclipse.emf.ecp.view.model.impl.ColumnImpl;
import org.eclipse.emf.ecp.view.model.impl.ControlImpl;
import org.eclipse.emf.ecp.view.model.impl.GroupImpl;
import org.eclipse.emf.ecp.view.model.impl.TableControlImpl;
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
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SWTViewRenderer extends AbstractSWTRenderer<View> {
	public static final SWTViewRenderer INSTANCE = new SWTViewRenderer();
	private static ImageDescriptor ERROR_DESCRIPTOR = Activator.getImageDescriptor("icons/error_decorate.png");
	private static ImageDescriptor WARNING_DESCRIPTOR = Activator.getImageDescriptor("icons/warning_decorate.png");

	// protected TreeViewer treeViewer;

	// TODO: move somewhere and filter based on interface types
	@SuppressWarnings("serial")
	private Set<Class<?>> filteredClasses = new LinkedHashSet<Class<?>>() {
		{
			add(ColumnCompositeImpl.class);
			add(ColumnImpl.class);
			add(ControlImpl.class);
			add(TableControlImpl.class);
			add(GroupImpl.class);
		}
	};

	@Override
	public Control renderSWT(final Node<View> viewNode, final AdapterFactoryItemDelegator adapterFactoryItemDelegator,
		Object... initData) throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {
		Composite parent = getParentFromInitData(initData);
		View view = viewNode.getRenderable();
		EList<AbstractCategorization> categorizations = view.getCategorizations();

		if (categorizations.size() == 1 && categorizations.get(0) instanceof Category) {
			Control control = SWTRenderers.INSTANCE.render(parent, viewNode.getChildren().get(0),
				adapterFactoryItemDelegator);
			viewNode.addRenderingResultDelegator(withSWT(control));
			return control;
		} else {
			Composite composite = createComposite(parent);
			TreeViewer treeViewer = createTreeViewer(composite, adapterFactoryItemDelegator, viewNode);
			createdEditorPane(composite);

			viewNode.addRenderingResultDelegator(withSWT(composite));

			initTreeViewer(treeViewer, viewNode);

			return composite;
		}
	}

	protected void createdEditorPane(Composite composite) {
		editorComposite = createScrolledComposite(composite);
		editorComposite.setExpandHorizontal(true);
		editorComposite.setExpandVertical(true);
		editorComposite.setShowFocusedControl(true);

		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(editorComposite);

	}

	protected List<Node<? extends Renderable>> filterVisibleNodes(TreeViewer treeViewer, Node<? extends Renderable> node) {
		List<Node<?>> result = new ArrayList<Node<?>>();
		List<Node<?>> children = node.getChildren();
		for (Node<?> child : children) {
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
	 * @return
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
	 * @param composite
	 * @param adapterFactoryItemDelegator
	 * @param viewNode
	 * @return
	 */
	protected TreeViewer createTreeViewer(final Composite composite,
		AdapterFactoryItemDelegator adapterFactoryItemDelegator, final Node<View> viewNode) {
		final TreeViewer treeViewer = new TreeViewer(composite);

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
				Object[] children = getChildren(element);

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
				Node<?> node = (Node<?>) parentElement;

				List<Node<?>> visisbleNodes = filterVisibleNodes(treeViewer, node);

				return visisbleNodes.toArray();
			}
		});

		treeViewer.setLabelProvider(new TreeTableLabelProvider(adapterFactoryItemDelegator.getAdapterFactory()));

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			private Node<?> lastSelection;

			public void selectionChanged(SelectionChangedEvent event) {

				ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
					ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
				AdapterFactoryItemDelegator newAdapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
					composedAdapterFactory);

				try {
					TreeSelection treeSelection = (TreeSelection) event.getSelection();
					Object selection = treeSelection.getFirstElement();
					addButtons(treeViewer, treeSelection, viewNode.getControlContext().getModelElement());

					if (selection == null) {
						return;
					}
					if (lastSelection != null) {
						lastSelection.dispose();
						lastSelection = (Node<?>) selection;
					}
					final Composite childComposite = createComposite(editorComposite);
					childComposite.setBackground(composite.getBackground());
					editorComposite.setContent(childComposite);

					// TODO: REVIEW
					if (Node.class.isInstance(selection)) {
						Node<?> node = (Node<?>) selection;
						try {
							SWTRenderers.INSTANCE.render(childComposite, node, newAdapterFactoryItemDelegator);
							viewNode.fireSelectedChildNodeChanged(node);
						} catch (NoRendererFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoPropertyDescriptorFoundExeption e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						childComposite.layout();
						Point point = childComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
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
	 * @return
	 */
	private Composite createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(parent.getBackground());

		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(composite);
		return composite;
	}

	private List<TreeEditor> editors = new ArrayList<TreeEditor>();
	private ScrolledComposite editorComposite;

	protected void addTreeEditor(final TreeViewer treeViewer, final EObject modelElement, View view) {
		// The text column
		final Tree tree = treeViewer.getTree();
		TreeColumn columnText = new TreeColumn(tree, SWT.NONE);
		columnText.setWidth(300);
		columnText.setAlignment(SWT.FILL);

		int maxActions = 0;
		Iterator<EObject> viewContents = view.eAllContents();
		while (viewContents.hasNext()) {
			EObject object = viewContents.next();
			if (AbstractCategorization.class.isInstance(object)) {
				AbstractCategorization abstractCategorization = (AbstractCategorization) object;
				if (maxActions < abstractCategorization.getActions().size()) {
					maxActions = abstractCategorization.getActions().size();
				}
			}
		}
		for (int i = 0; i < maxActions; i++) {
			// The column
			TreeColumn column = new TreeColumn(tree, SWT.NONE);
			column.setWidth(50);

			TreeEditor editor = new TreeEditor(tree);
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
	private void cleanUpTreeEditors() {
		for (TreeEditor editor : editors) {
			Control oldEditor = editor.getEditor();
			if (oldEditor != null) {
				oldEditor.dispose();
			}
		}
	}

	protected void addButtons(final TreeViewer treeViewer, TreeSelection treeSelection, EObject modelElement) {

		cleanUpTreeEditors();

		if (treeSelection.getPaths().length == 0) {
			return;
		}

		// Identify the selected row
		TreeItem item = treeViewer.getTree().getSelection()[0];
		if (item == null) {
			return;
		}

		final Node<?> object = (Node<?>) treeSelection.getFirstElement();
		if (object.getActions() == null) {
			return;
		}
		for (int i = 0; i < object.getActions().size(); i++) {
			ECPTreeViewAction action = (ECPTreeViewAction) object.getActions().get(i);
			TreeEditor editor = editors.get(i);
			action.init(treeViewer, treeSelection, editor, modelElement);
			action.execute();
		}
	}

	protected class TreeTableLabelProvider extends AdapterFactoryLabelProvider implements ITableItemLabelProvider {

		public TreeTableLabelProvider(AdapterFactory adapterFactory) {
			super(adapterFactory);
		}

		@Override
		public Image getColumnImage(Object object, int columnIndex) {

			if (columnIndex != 0) {
				return null;
			}

			Image image = super.getColumnImage(object, columnIndex);

			if (!Node.class.isInstance(object)) {
				return image;
			}

			Node<?> node = (Node<?>) object;
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
			OverlayImageDescriptor imageDescriptor = new OverlayImageDescriptor(image, overlay,
				OverlayImageDescriptor.LOWER_RIGHT);
			Image resultImage = imageDescriptor.createImage();

			return resultImage;
		}

		@Override
		public String getColumnText(Object object, int columnIndex) {

			if (columnIndex != 0) {
				return "";
			}

			Node<?> node = (Node<?>) object;

			return super.getText(node.getLabelObject());
		}

	}

}
