package org.eclipse.emf.ecp.ui.view.swt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.edit.internal.swt.util.OverlayImageDescriptor;
import org.eclipse.emf.ecp.internal.ui.view.emf.AdapterFactoryLabelProvider;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.internal.ui.view.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.internal.ui.view.renderer.Node;
import org.eclipse.emf.ecp.internal.ui.view.renderer.ValidationOccurredListener;
import org.eclipse.emf.ecp.internal.ui.view.renderer.WithRenderedObjectAdapter;
import org.eclipse.emf.ecp.view.model.AbstractCategorization;
import org.eclipse.emf.ecp.view.model.Category;
import org.eclipse.emf.ecp.view.model.Renderable;
import org.eclipse.emf.ecp.view.model.View;
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
import org.eclipse.jface.viewers.TreePath;
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


public class SWTViewRenderer extends AbstractSWTRenderer<View> {
    
    private static ImageDescriptor ERROR_DESCRIPTOR = Activator
            .getImageDescriptor("icons/error_decorate.png");
    private static ImageDescriptor WARNING_DESCRIPTOR = Activator
            .getImageDescriptor("icons/warning_decorate.png");

    private TreeViewer treeViewer;
    private WithRenderedObjectAdapter treeViewerRefreshAdapter = new WithRenderedObjectAdapter() {
        @Override
        public void show(boolean shouldShow) {
            treeViewer.refresh();
        }
    };
    
    @Override
    public Control render(final Node<View> viewNode,
            final AdapterFactoryItemDelegator adapterFactoryItemDelegator)
            throws NoRendererFoundException, NoPropertyDescriptorFoundExeption {

        View view = viewNode.getRenderable();
        EList<AbstractCategorization> categorizations = view.getCategorizations();

        if (categorizations.size() == 1 && categorizations.get(0) instanceof Category) {
            Control control = SWTRenderers.INSTANCE.render(getParent(),
                    viewNode.getChildren().get(0), adapterFactoryItemDelegator);
            viewNode.lift(withSWT(control));
            return control;
        } else {
            Composite composite = createComposite(getParent());
            createTreeViewer(composite,adapterFactoryItemDelegator,viewNode);
            createdEditorPane(composite);
            
            viewNode.lift(withSWT(composite));

            
            
            initTreeViewer(viewNode);
            
            return composite;
        }
    }

    protected void createdEditorPane(Composite composite) {
    	editorComposite = createScrolledComposite(composite);
        editorComposite.setExpandHorizontal(true);
        editorComposite.setExpandVertical(true);
        editorComposite.setShowFocusedControl(true);
        
        GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL)
                .applyTo(editorComposite);

	}

	private List<Node<?>> filterVisisbleNodes(Node node) {
        List<Node<?>> result = new ArrayList<Node<?>>();
        List<Node> children = node.getChildren();
        for (Node child : children) {
            if (child.isVisible()) {
                if (!child.isLifted()) {
                    child.lift(treeViewerRefreshAdapter);
                }
                result.add(child);
            }
        }
        return result;
    }

    /**
     * @return
     */
    private ScrolledComposite createScrolledComposite(Composite parent) {
        final ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL
                | SWT.H_SCROLL | SWT.BORDER);
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
     * @param adapterFactoryItemDelegator 
     * @param viewNode 
     * @return
     */
    protected void createTreeViewer(Composite composite, AdapterFactoryItemDelegator adapterFactoryItemDelegator, final Node<View> viewNode) {
        treeViewer = new TreeViewer(composite);
        
        GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.FILL).grab(false, true)
        .hint(400, SWT.DEFAULT).applyTo(treeViewer.getTree());
        
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
                Node<?> node = (Node<?>) parentElement;

                List<Node<?>> visisbleNodes = filterVisisbleNodes(node);

                return visisbleNodes.toArray();
            }
        });

        treeViewer.setLabelProvider(new TreeTableLabelProvider(adapterFactoryItemDelegator
                .getAdapterFactory()));

        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            private Node<?> lastSelection;
            @Override
            public void selectionChanged(SelectionChangedEvent event) {

                ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
                        ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
                AdapterFactoryItemDelegator newAdapterFactoryItemDelegator = new AdapterFactoryItemDelegator(
                        composedAdapterFactory);

                try {
                    TreeSelection treeSelection = (TreeSelection) event.getSelection();
                    Object selection = treeSelection.getFirstElement();
                    addButtons(treeViewer, treeSelection, viewNode.getControlContext()
                            .getModelElement());

                    if (selection == null) {
                        return;
                    }
                    if(lastSelection!=null){
                        lastSelection.dispose();
                        lastSelection=(Node<?>) selection;
                    }
                    final Composite childComposite = createComposite(editorComposite);
                    childComposite.setBackground(getParent().getBackground());
                    editorComposite.setContent(childComposite);
                    

                    // TODO: REVIEW
                    if (Node.class.isInstance(selection)) {
                        Node<?> node = (Node<?>) selection;
                        try {
                            SWTRenderers.INSTANCE.render(childComposite, node,
                                    newAdapterFactoryItemDelegator);
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

        
    }

    protected void initTreeViewer(Node<View> viewNode){
    	viewNode.setCallback(new ValidationOccurredListener() {
            
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
        GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL)
                .applyTo(composite);
        return composite;
    }

    private List<TreeEditor> editors = new ArrayList<TreeEditor>();
	private ScrolledComposite editorComposite;

    protected void addTreeEditor(final TreeViewer treeViewer, final EObject article, View view) {
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
            editor.setColumn(i+1);

            editors.add(editor);
        }

        tree.addTreeListener(new TreeListener() {

            @Override
            public void treeExpanded(TreeEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void treeCollapsed(TreeEvent e) {
                cleanUpTreeEditors();
            }
        });

    }

    // Clean up any previous editor control
    private void cleanUpTreeEditors() {
        for (TreeEditor editor : editors) {
            Control oldEditor = editor.getEditor();
            if (oldEditor != null)
                oldEditor.dispose();
        }
    }

    protected void addButtons(final TreeViewer treeViewer, TreeSelection treeSelection,
            EObject article) {

        cleanUpTreeEditors();
        if(treeSelection.getPaths().length==0)
            return;
        final TreePath currentPath = treeSelection.getPaths()[0];
        // Identify the selected row
        TreeItem item = treeViewer.getTree().getSelection()[0];
        if (item == null)
            return;

        final Node object = (Node) treeSelection.getFirstElement();
        if(object.getActions()==null)
            return;
        // if(AbstractCategorization.class.isInstance(object.getRenderable())){
        // List<Action> actions=((AbstractCategorization)object.getRenderable()).getActions();
        for (int i = 0; i < object.getActions().size(); i++) {
            ECPTreeViewAction action = (ECPTreeViewAction) object.getActions().get(i);
            TreeEditor editor = editors.get(i);
            action.init(treeViewer, treeSelection, editor, article);
            action.execute();
        }
        // }

    }

    protected class TreeTableLabelProvider extends AdapterFactoryLabelProvider implements
            ITableItemLabelProvider {

        public TreeTableLabelProvider(AdapterFactory adapterFactory) {
            super(adapterFactory);
        }

        @Override
        public Image getColumnImage(Object object, int columnIndex) {
            
            if (columnIndex != 0)
                return null;
            
            Image image = super.getColumnImage(object, columnIndex);
            
            if (!Node.class.isInstance(object)) {
                return image;
            }
           
            Node node = (Node) object;
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
            
            if (overlay == null)
                return image;
            OverlayImageDescriptor imageDescriptor = new OverlayImageDescriptor(image, overlay,
                    OverlayImageDescriptor.LOWER_RIGHT);
            Image resultImage = imageDescriptor.createImage();
            
            return resultImage;            
        }

        @Override
        public String getColumnText(Object object, int columnIndex) {
            if (columnIndex != 0)
                return "";
//            return super.getColumnText(object, columnIndex);
            Node node = (Node) object;
            return super.getText(node.getLabelObject());
        }

    }

}
