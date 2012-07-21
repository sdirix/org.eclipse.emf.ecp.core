/**
 * 
 */
package org.eclipse.emf.ecp.ui.common;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;

/**
 * @author Eugen Neufeld
 */
class ModelExplorerDropAdapter extends EditingDomainViewerDropAdapter
{
  /*
   * @param viewer
   */
  public ModelExplorerDropAdapter(Viewer viewer)
  {
    super(null, viewer);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
   */
  @Override
  public void dragOver(DropTargetEvent event)
  {
    Object target = extractDropTarget(event.item);
    if (target instanceof ECPProject)
    {

      source = getDragSource(event);

      ECPProject project = (ECPProject)target;
      if (project.getElements().contains(source.iterator().next()))
      {
        event.detail = DND.DROP_NONE;
      }
      else
      {
        event.feedback = DND.FEEDBACK_SELECT | getAutoFeedback();
      }
    }
    else
    {
      super.dragOver(event);
    }
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter#drop(org.eclipse.swt.dnd.DropTargetEvent)
   */
  @Override
  public void drop(DropTargetEvent event)
  {
    Object target = extractDropTarget(event.item);

    if (target instanceof ECPProject)
    {
      ECPProject project = (ECPProject)target;

      source = getDragSource(event);

      project.getElements().add((EObject)source.iterator().next());
    }
    else
    {
      super.drop(event);
    }
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
   */
  @Override
  public void dropAccept(DropTargetEvent event)
  {
    Object target = extractDropTarget(event.item);
    if (target instanceof ECPProject)
    {
      event.feedback = DND.FEEDBACK_SELECT | getAutoFeedback();

    }
    else
    {
      super.dropAccept(event);
    }
  }

  void setEditingDomain(EditingDomain editingDomain)
  {
    domain = editingDomain;

  }

}