package org.eclipse.emf.ecp.view.editor.handler;

import org.eclipse.emf.ecp.ui.common.dnd.ECPDropAdapter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DropTargetEvent;

public class ViewEditorDropAdapter extends ECPDropAdapter {

    public ViewEditorDropAdapter() {
    }

    public ViewEditorDropAdapter(EditingDomain domain, Viewer viewer) {
        super(domain, viewer);
    }

    @Override
    public void dragOver(DropTargetEvent event) {
        // Object target = extractDropTarget(event.item);
        // if (target == null) {
        // return;
        // }
        // source = getDragSource(event);
        //
        // Object sourceObject = null;
        //
        // if (source == null) {
        // ISelection selection = viewer.getSelection();
        // if (selection instanceof IStructuredSelection) {
        // sourceObject = ((IStructuredSelection) viewer.getSelection())
        // .getFirstElement();
        // }
        //
        // } else {
        // sourceObject = source.iterator().next();
        // }
        super.dragOver(event);
    }

    @Override
    public void dropAccept(DropTargetEvent event) {
        super.dropAccept(event);
    }

    @Override
    public void drop(final DropTargetEvent event) {
        // source = getDragSource(event);
        //
        // Object sourceObject = null;
        //
        // if (source == null) {
        // ISelection selection = viewer.getSelection();
        // if (selection instanceof IStructuredSelection) {
        // sourceObject = ((IStructuredSelection) viewer.getSelection())
        // .getFirstElement();
        // }
        //
        // } else {
        // sourceObject = source.iterator().next();
        // }

        // if (event.detail == DND.DROP_COPY) {
        //
        // TreeItem itemChild=(TreeItem) event.item;
        // Composite composite=null;
        // Composite compositeSource = null;
        // if(Composite.class.isInstance(sourceObject)){
        // compositeSource=(Composite) sourceObject;
        // }
        // if(Composite.class.isInstance(itemChild.getData())){
        // composite=(Composite)itemChild.getData();
        // }
        // TreeItem parentItem=itemChild.getParentItem();
        // if(composite!=null&&CompositeCollection.class.isInstance(parentItem.getData())){
        // CompositeCollection compositeCollection=(CompositeCollection)parentItem.getData();
        // int index=compositeCollection.getComposites().indexOf(composite);
        // int indexSource=compositeCollection.getComposites().indexOf(compositeSource);
        // if(index!=-1&&indexSource!=-1){
        // domain.getCommandStack().execute(MoveCommand.create(domain, compositeCollection,
        // ViewPackage.eINSTANCE.getCompositeCollection_Composites(), compositeSource, index));
        // }
        // }
        // }
        // else{
        super.drop(event);
        // if (event.detail == DND.DROP_LINK) {
        // //TODO change to parameter
        // ((StructuredViewer)viewer).refresh(((EObject)sourceObject).eContainer());
        // }
        // }
    }
}
