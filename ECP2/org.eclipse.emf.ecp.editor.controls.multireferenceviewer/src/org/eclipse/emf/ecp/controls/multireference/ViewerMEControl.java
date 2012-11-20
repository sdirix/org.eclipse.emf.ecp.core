/*******************************************************************************
 * Copyright (c) 2012 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Erdal Karaca - initial implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.controls.multireference;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.editor.Activator;
import org.eclipse.emf.ecp.editor.mecontrols.AbstractMEControl;
import org.eclipse.emf.ecp.editor.mecontrols.melinkcontrol.AddReferenceAction;
import org.eclipse.emf.ecp.editor.mecontrols.melinkcontrol.MEHyperLinkDeleteAdapter;
import org.eclipse.emf.ecp.editor.mecontrols.melinkcontrol.NewReferenceAction;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.dnd.ViewerDragAdapter;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.Section;

public class ViewerMEControl extends AbstractMEControl {
	@Override
	public int canRender(IItemPropertyDescriptor itemPropertyDescriptor, EObject modelElement) {
		Object feature = itemPropertyDescriptor.getFeature(modelElement);

		if (feature instanceof EReference
			&& EObject.class.isAssignableFrom(((EReference) feature).getEType().getInstanceClass())
			&& ((EReference) feature).isMany() && ((EReference) feature).isOrdered()) {
			return 2;
		}

		return AbstractMEControl.DO_NOT_RENDER;
	}

	@Override
	protected Control createControl(Composite parent, int style) {
		Section section = getToolkit().createSection(parent,
			Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED | Section.DESCRIPTION);
		section.setDescription("Double click element to edit it.");
		section.setText(getItemPropertyDescriptor().getDisplayName(getModelElement()));
		Composite comp = getToolkit().createComposite(section);
		section.setClient(comp);
		comp.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());

		final EReference ref = (EReference) getItemPropertyDescriptor().getFeature(getModelElement());

		TreeViewer viewer = new TreeViewer(comp, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		{
			Tree control = viewer.getTree();
			control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

			ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
			AdapterFactoryLabelProvider adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(adapterFactory);
			AdapterFactoryContentProvider contentProvider = new AdapterFactoryContentProvider(adapterFactory) {
				@SuppressWarnings("unchecked")
				public Object[] getElements(Object object) {
					// force this content adapter to listen to notifications on
					// root object
					super.getElements(object);

					// root object must be model element
					assert object == getModelElement();

					// see TableViewerMEControl.canRender()
					EObject modelElement = getModelElement();
					EList<EObject> input = (EList<EObject>) modelElement.eGet(ref);
					return input.toArray();
				}

				@Override
				public boolean hasChildren(Object object) {
					return false;
				}
			};
			viewer.setLabelProvider(adapterFactoryLabelProvider);
			viewer.setContentProvider(contentProvider);
			viewer.setInput(getModelElement());
		}

		// set up tool bar to handle commands
		{
			ToolBar toolBar = new ToolBar(comp, SWT.FLAT | SWT.RIGHT | SWT.VERTICAL);
			GridData layoutData = new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
			// layoutData.widthHint = 32;
			toolBar.setLayoutData(layoutData);
			createToolbar(toolBar, ref, viewer);
		}

		// set up double click support
		{
			viewer.addDoubleClickListener(new IDoubleClickListener() {
				public void doubleClick(DoubleClickEvent event) {
					ISelection selection = event.getSelection();

					if (selection instanceof IStructuredSelection && !selection.isEmpty()) {
						EObject target = (EObject) ((IStructuredSelection) selection).getFirstElement();
						getContext().openEditor(target, "org.eclipse.emf.ecp.editor");
						// ActionHelper.openModelElement(target, , getContext().getEcpProject());
					}
				}
			});
		}

		// set up drag support
		{
			viewer.addDragSupport(DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK,
				new Transfer[] { LocalSelectionTransfer.getTransfer() }, new ViewerDragAdapter(viewer));
		}

		return section;
	}

	private void createToolbar(ToolBar toolbar, final EReference ref, final TreeViewer viewer) {
		ToolBarManager tbm = new ToolBarManager(toolbar);

		tbm.add(new Action("Delete", Activator.getImageDescriptor("icons/delete.gif")) {
			@Override
			public void run() {
				ISelection selection = viewer.getSelection();

				if (!selection.isEmpty()) {
					@SuppressWarnings("unchecked")
					List<EObject> list = ((IStructuredSelection) selection).toList();

					if (MEHyperLinkDeleteAdapter.askConfirmation(list.get(0))) {

						Command deleteCommand = RemoveCommand.create(getContext().getEditingDomain(),
							getModelElement(), ref, list);
						getContext().getEditingDomain().getCommandStack().execute(deleteCommand);

						viewer.refresh();
					}
				}
			}
		});

		tbm.add(new AddReferenceAction(getModelElement(), ref, getItemPropertyDescriptor(), getContext(), this
			.getShell()));
		tbm.add(new NewReferenceAction(getModelElement(), ref, getItemPropertyDescriptor(), getContext(), this
			.getShell()));

		tbm.add(new Action("Up", Activator.getImageDescriptor("icons/arrow_up.png")) {
			@Override
			public void run() {
				getContext().getEditingDomain().getCommandStack().execute(new ChangeCommand(getModelElement()) {
					@Override
					protected void doExecute() {
						move(viewer, ref, -1);
					}
				});
			}
		});

		tbm.add(new Action("Down", Activator.getImageDescriptor("icons/arrow_down.png")) {
			@Override
			public void run() {
				getContext().getEditingDomain().getCommandStack().execute(new ChangeCommand(getModelElement()) {
					@Override
					protected void doExecute() {
						move(viewer, ref, +1);
					}
				});
			}
		});

		tbm.update(true);
	}

	@SuppressWarnings("unchecked")
	private void move(final ISelectionProvider isp, final EReference ref, int offset) {
		ISelection selection = isp.getSelection();

		if (selection instanceof IStructuredSelection && !selection.isEmpty()) {
			EObject eo = (EObject) ((IStructuredSelection) selection).getFirstElement();

			EList<EObject> containmentList = (EList<EObject>) getModelElement().eGet(ref);

			if (containmentList != null) {
				int pos = containmentList.indexOf(eo);

				if (offset < 0) {
					if (pos > 0) {
						containmentList.move(pos - 1, pos);
					}
				} else if (offset > 0) {
					if (pos < containmentList.size() - 1) {
						containmentList.move(pos + 1, pos);
					}
				}
			}
		}
	}

	@Override
	protected Class<? extends EStructuralFeature> getEStructuralFeatureType() {
		return EReference.class;
	}

	@Override
	protected Class<?> getClassType() {
		return EObject.class;
	}

	@Override
	protected boolean isMulti() {
		return true;
	}
}
