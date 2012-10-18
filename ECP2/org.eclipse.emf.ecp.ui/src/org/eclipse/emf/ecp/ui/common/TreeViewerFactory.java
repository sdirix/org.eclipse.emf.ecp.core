/**
 * 
 */
package org.eclipse.emf.ecp.ui.common;

import org.eclipse.emf.ecp.core.ECPProject;
import org.eclipse.emf.ecp.core.ECPProjectManager;
import org.eclipse.emf.ecp.core.ECPRepositoryManager;
import org.eclipse.emf.ecp.core.util.ECPModelContext;
import org.eclipse.emf.ecp.core.util.ECPModelContextProvider;
import org.eclipse.emf.ecp.core.util.ECPUtil;
import org.eclipse.emf.ecp.ui.model.ModelContentProvider;
import org.eclipse.emf.ecp.ui.model.ModelLabelProvider;
import org.eclipse.emf.ecp.ui.model.RepositoriesContentProvider;
import org.eclipse.emf.ecp.ui.model.RepositoriesLabelProvider;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.emf.edit.ui.dnd.ViewerDragAdapter;

import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;

/**
 * @author Eugen Neufeld
 */
public class TreeViewerFactory {
	public static TreeViewer createModelExplorerViewer(Composite parent, boolean hasDnD, ILabelDecorator labelDecorator) {
		final ModelContentProvider contentProvider = new ModelContentProvider();
		final TreeViewer viewer = createTreeViewer(parent, new ModelLabelProvider(contentProvider), contentProvider,
			ECPProjectManager.INSTANCE, labelDecorator);
		if (hasDnD) {
			final ModelExplorerDropAdapter dropAdapter = new ModelExplorerDropAdapter(viewer);

			int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
			Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance() };
			viewer.addDragSupport(dndOperations, transfers, new ViewerDragAdapter(viewer));// new ECPDragAdapter(viewer)
			viewer.addDropSupport(dndOperations, transfers, dropAdapter);// ComposedDropAdapter
			viewer.addSelectionChangedListener(new ISelectionChangedListener() {

				public void selectionChanged(SelectionChangedEvent event) {
					Object[] elements = ((IStructuredSelection) event.getSelection()).toArray();
					if (elements == null || elements.length == 0) {
						return;
					}
					EditingDomain domain = null;
					ECPProject project = null;
					if (elements[0] instanceof ECPProject) {
						ECPModelContext context = ECPUtil.getModelContext(contentProvider, elements);
						if (context != null && context instanceof ECPProject) {
							project = (ECPProject) context;
						}
					} else {
						ECPModelContextProvider contextProvider = (ECPModelContextProvider) viewer.getContentProvider();
						project = (ECPProject) ECPUtil.getModelContext(contextProvider, elements[0]);
					}
					domain = project.getEditingDomain();
					dropAdapter.setEditingDomain(domain);
				}
			});
		}
		return viewer;
	}

	public static TreeViewer createRepositoryExplorerViewer(Composite parent, ILabelDecorator labelDecorator) {
		RepositoriesContentProvider contentProvider = new RepositoriesContentProvider();
		TreeViewer viewer = createTreeViewer(parent, new RepositoriesLabelProvider(contentProvider), contentProvider,
			ECPRepositoryManager.INSTANCE, labelDecorator);
		return viewer;
	}

	public static TreeViewer createTreeViewer(Composite parent, ILabelProvider labelProvider,
		ITreeContentProvider contentProvider, Object input, ILabelDecorator labelDecorator) {
		TreeViewer viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		createTreeViewer(labelProvider, contentProvider, input, labelDecorator, viewer);
		return viewer;
	}

	public static TreeViewer createCheckedTreeViewer(Composite parent, ILabelProvider labelProvider,
		ITreeContentProvider contentProvider, Object input, ILabelDecorator labelDecorator) {
		final ContainerCheckedTreeViewer viewer = new ContainerCheckedTreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
			| SWT.V_SCROLL);
		createTreeViewer(labelProvider, contentProvider, input, labelDecorator, viewer);
		return viewer;
	}

	/**
	 * @param labelProvider
	 * @param contentProvider
	 * @param input
	 * @param labelDecorator
	 * @param viewer
	 */
	private static void createTreeViewer(ILabelProvider labelProvider, ITreeContentProvider contentProvider,
		Object input, ILabelDecorator labelDecorator, TreeViewer viewer) {
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(labelProvider);
		viewer.setSorter(new ViewerSorter());
		viewer.setInput(input);

		if (labelDecorator != null) {
			if (!(labelProvider instanceof DecoratingLabelProvider)) {
				viewer.setLabelProvider(new DecoratingLabelProvider(labelProvider, labelDecorator));
			}
		}
	}
}
