/*******************************************************************************
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Anas Chakfeh - initial API and implementation
 * Eugen Neufeld - Refactoring
 * Alexandra Buzila
 ******************************************************************************/
package org.eclipse.emf.ecp.view.treemasterdetail.ui.swt.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecp.common.ChildrenDescriptorCollector;
import org.eclipse.emf.ecp.edit.spi.ReferenceService;
import org.eclipse.emf.ecp.internal.ui.view.emf.AdapterFactoryContentProvider;
import org.eclipse.emf.ecp.internal.ui.view.emf.AdapterFactoryLabelProvider;
import org.eclipse.emf.ecp.spi.ui.DynamicReferenceService;
import org.eclipse.emf.ecp.spi.ui.ECPReferenceServiceImpl;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContextFactory;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.provider.ViewProviderHelper;
import org.eclipse.emf.ecp.view.spi.renderer.NoPropertyDescriptorFoundExeption;
import org.eclipse.emf.ecp.view.spi.renderer.NoRendererFoundException;
import org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer;
import org.eclipse.emf.ecp.view.spi.swt.layout.GridCell;
import org.eclipse.emf.ecp.view.spi.swt.layout.GridDescription;
import org.eclipse.emf.ecp.view.spi.swt.layout.GridDescriptionFactory;
import org.eclipse.emf.ecp.view.treemasterdetail.model.VTreeMasterDetail;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.action.ecp.CreateChildAction;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.emf.edit.ui.dnd.ViewerDragAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.osgi.framework.FrameworkUtil;

/**
 * SWT Renderer for a {@link VTreeMasterDetail} element.
 * 
 * @author Anas Chakfeh
 * 
 */
public class TreeMasterDetailSWTRenderer extends AbstractSWTRenderer<VTreeMasterDetail> {
	private GridDescription rendererGridDescription;
	private ScrolledForm form;

	private List<MasterDetailAction> commands;
	/**
	 * Static string.
	 * 
	 */
	public static final String GLOBAL_ADDITIONS = "global_additions"; //$NON-NLS-1$

	/**
	 * @author Anas Chakfeh
	 * 
	 */
	private class RootObject {

		private final EObject modelElement;

		/**
		 * @param modelElement
		 */
		public RootObject(EObject modelElement) {
			this.modelElement = modelElement;
		}

		/**
		 * @return the root object
		 */
		public EObject getRoot() {
			return modelElement;
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#dispose()
	 */
	@Override
	protected void dispose() {
		rendererGridDescription = null;
		super.dispose();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#getGridDescription(GridDescription)
	 */
	@Override
	public GridDescription getGridDescription(GridDescription gridDescription) {
		if (rendererGridDescription == null) {
			rendererGridDescription = GridDescriptionFactory.INSTANCE.createSimpleGrid(1, 1, this);
		}
		return rendererGridDescription;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecp.view.spi.swt.AbstractSWTRenderer#renderControl(org.eclipse.emf.ecp.view.spi.swt.layout.GridCell,
	 *      org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control renderControl(GridCell cell, Composite parent) throws NoRendererFoundException,
		NoPropertyDescriptorFoundExeption {

		final EObject modelElement = getViewModelContext().getDomainModel();

		final FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);

		final GridLayout layout = GridLayoutFactory.fillDefaults().create();
		layout.marginTop = 10;
		layout.marginLeft = 10;
		layout.marginRight = 10;
		form.getBody().setLayout(layout);

		final SashForm sash = new SashForm(form.getBody(), SWT.HORIZONTAL);
		sash.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(sash);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(sash);
		sash.setSashWidth(5);

		final Composite leftPanel = new Composite(sash, SWT.NONE);
		leftPanel.setLayout(GridLayoutFactory.fillDefaults().create());
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.FILL).grab(false, true).applyTo(leftPanel);
		leftPanel.setBackground(parent.getBackground());

		final Section section = toolkit.createSection(sash, ExpandableComposite.TITLE_BAR);
		section.setText("Details"); //$NON-NLS-1$
		section.setLayout(GridLayoutFactory.fillDefaults().create());

		final ScrolledComposite rightPanel = new ScrolledComposite(section, SWT.V_SCROLL | SWT.H_SCROLL);
		rightPanel.setShowFocusedControl(true);
		rightPanel.setExpandVertical(true);
		rightPanel.setExpandHorizontal(true);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(rightPanel);
		rightPanel.setBackground(sash.getBackground());
		section.setClient(rightPanel);
		sash.setWeights(new int[] { 1, 3 });

		final VView detailView = getVElement().getDetailView();

		final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(modelElement);

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(new AdapterFactory[] {
			new ReflectiveItemProviderAdapterFactory(),
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE) });

		final AdapterFactoryContentProvider adapterFactoryContentProvider = new AdapterFactoryContentProvider(
			adapterFactory) {

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.ecp.internal.ui.view.emf.AdapterFactoryContentProvider#getElements(java.lang.Object)
			 */
			@Override
			public Object[] getElements(Object object) {

				return new Object[] { ((RootObject) object).getRoot() };
			}

		};
		final AdapterFactoryLabelProvider adapterFactoryLabelProvider = new
			AdapterFactoryLabelProvider(adapterFactory);

		final TreeViewer treeViewer = new TreeViewer(leftPanel);

		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).hint(100, SWT.DEFAULT)
			.applyTo(treeViewer.getTree());

		treeViewer.setContentProvider(adapterFactoryContentProvider);
		treeViewer.setLabelProvider(adapterFactoryLabelProvider);
		treeViewer.setAutoExpandLevel(2);
		treeViewer.setInput(new RootObject(modelElement));

		// Drag and Drop
		addDragAndDropSupport(modelElement, treeViewer, editingDomain);

		// Selection Listener
		treeViewer.addSelectionChangedListener(new TreeMasterViewSelectionListener(rightPanel, detailView));
		treeViewer.setSelection(new StructuredSelection(modelElement));
		fillContextMenu(treeViewer, editingDomain);

		treeViewer.getTree().addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent event) {
				adapterFactoryContentProvider.dispose();
				adapterFactoryLabelProvider.dispose();
				adapterFactory.dispose();
			}
		});

		if (modelElement.eContainer() == null && !DynamicEObjectImpl.class.isInstance(modelElement)) {

			toolkit.decorateFormHeading(form.getForm());
			form.setText("View Editor"); //$NON-NLS-1$
			final ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(Activator.getDefault()
				.getBundle()
				.getResource("icons/view.png")); //$NON-NLS-1$
			form.setImage(new Image(parent.getDisplay(), imageDescriptor.getImageData()));

			readToolbarActions(treeViewer, editingDomain);

		}
		commands = readMasterDetailActions();
		return form;
	}

	/**
	 * 
	 */
	private void readToolbarActions(final TreeViewer treeViewer, final EditingDomain editingDomain) {
		final IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		if (extensionRegistry == null) {
			return;
		}
		final IConfigurationElement[] controls = extensionRegistry
			.getConfigurationElementsFor("org.eclipse.emf.ecp.view.treemasterdetail.ui.swt.masterDetailActions"); //$NON-NLS-1$
		for (final IConfigurationElement e : controls) {
			try {
				final String location = e.getAttribute("location"); //$NON-NLS-1$
				if (!location.equals("toolbar")) { //$NON-NLS-1$
					continue;
				}
				final Object root = ((RootObject) treeViewer.getInput()).getRoot();
				if (!VView.class.isInstance(root)) {
					return;
				}
				final VView view = (VView) root;
				final String label = e.getAttribute("label"); //$NON-NLS-1$
				final String imagePath = e.getAttribute("imagePath"); //$NON-NLS-1$
				final MasterDetailAction command = (MasterDetailAction) e.createExecutableExtension("command"); //$NON-NLS-1$
				// if (command.shouldShow(view)) {
				final Action newAction = new Action() {
					@Override
					public void run() {
						super.run();
						command.execute(view);
					}
				};

				newAction.setImageDescriptor(ImageDescriptor.createFromURL(FrameworkUtil.getBundle(command.getClass())
					.getResource(imagePath)));
				newAction.setText(label);

				final IToolBarManager mToolbar = form.getToolBarManager();
				mToolbar.add(newAction);
				mToolbar.update(true);

				// }
			} catch (final CoreException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void addDragAndDropSupport(final EObject modelElement, final TreeViewer treeViewer,
		EditingDomain editingDomain) {

		final int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
		final Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance() };
		treeViewer.addDragSupport(dndOperations, transfers, new ViewerDragAdapter(treeViewer));
		final EditingDomainViewerDropAdapter editingDomainViewerDropAdapter = new EditingDomainViewerDropAdapter(
			editingDomain,
			treeViewer);
		treeViewer.addDropSupport(dndOperations, transfers, editingDomainViewerDropAdapter);
	}

	/**
	 * @param treeViewer
	 * @param editingDomain
	 */
	private void fillContextMenu(final TreeViewer treeViewer, final EditingDomain editingDomain) {
		final ChildrenDescriptorCollector childrenDescriptorCollector = new ChildrenDescriptorCollector();

		final MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (treeViewer.getSelection().isEmpty()) {
					return;
				}
				final EObject root = ((RootObject) treeViewer.getInput()).getRoot();

				if (treeViewer.getSelection() instanceof IStructuredSelection) {
					final IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();

					if (selection.size() == 1) {
						final EObject eObject = (EObject) selection.getFirstElement();
						final EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(eObject);
						if (domain == null) {
							return;
						}
						final Collection<?> descriptors = childrenDescriptorCollector.getDescriptors(eObject);
						fillContextMenu(manager, descriptors, editingDomain, eObject);
					}
					if (!selection.toList().contains(root)) {
						manager.add(new Separator(GLOBAL_ADDITIONS));
						addDeleteActionToContextMenu(editingDomain, manager, selection);
					}
					manager.add(new Separator());

					if (selection.getFirstElement() != null && EObject.class.isInstance(selection.getFirstElement())) {
						final EObject selectedObject = (EObject) selection.getFirstElement();
						for (final MasterDetailAction command : commands) {
							if (command.shouldShow(selectedObject)) {
								final Action newAction = new Action() {
									@Override
									public void run() {
										super.run();
										command.execute(selectedObject);
									}
								};

								newAction.setImageDescriptor(ImageDescriptor.createFromURL(FrameworkUtil.getBundle(
									command.getClass())
									.getResource(command.getImagePath())));
								newAction.setText(command.getLabel());

								manager.add(newAction);
							}
						}
					}
				}
			}
		});
		final Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);
	}

	/**
	 * @param editingDomain
	 * @param manager The menu manager responsible for the context menu
	 * @return
	 * 
	 */
	private List<MasterDetailAction> readMasterDetailActions() {
		final List<MasterDetailAction> commands = new ArrayList<MasterDetailAction>();
		final IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		if (extensionRegistry == null) {
			return commands;
		}

		final IConfigurationElement[] controls = extensionRegistry
			.getConfigurationElementsFor("org.eclipse.emf.ecp.view.treemasterdetail.ui.swt.masterDetailActions"); //$NON-NLS-1$
		for (final IConfigurationElement e : controls) {
			try {
				final String location = e.getAttribute("location"); //$NON-NLS-1$
				if (!location.equals("menu")) { //$NON-NLS-1$
					continue;
				}
				final String label = e.getAttribute("label"); //$NON-NLS-1$
				final String imagePath = e.getAttribute("imagePath"); //$NON-NLS-1$
				final MasterDetailAction command = (MasterDetailAction) e.createExecutableExtension("command"); //$NON-NLS-1$
				command.setLabel(label);
				command.setImagePath(imagePath);

				commands.add(command);

			} catch (final CoreException ex) {
				Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.getDefault().getBundle().getSymbolicName(),
						ex.getMessage(), ex));
			}
		}

		return commands;

	}

	/**
	 * @param manager The menu manager responsible for the context menu
	 * @param descriptors The menu items to be added
	 * @param domain The editing domain of the current EObject
	 * @param eObject The model element
	 */
	private void fillContextMenu(IMenuManager manager, Collection<?> descriptors, final EditingDomain domain,
		final EObject eObject) {
		for (final Object descriptor : descriptors) {

			final CommandParameter cp = (CommandParameter) descriptor;
			if (!CommandParameter.class.isInstance(descriptor)) {
				continue;
			}
			if (cp.getEReference() == null) {
				continue;
			}
			if (!cp.getEReference().isMany() && eObject.eIsSet(cp.getEStructuralFeature())) {
				continue;
			} else if (cp.getEReference().isMany() && cp.getEReference().getUpperBound() != -1
				&& cp.getEReference().getUpperBound() <= ((List<?>) eObject.eGet(cp.getEReference())).size()) {
				continue;
			}

			manager.add(new CreateChildAction(domain, new StructuredSelection(eObject), descriptor) {
				@Override
				public void run() {
					super.run();

					final EReference reference = ((CommandParameter) descriptor).getEReference();
					// if (!reference.isContainment()) {
					// domain.getCommandStack().execute(
					// AddCommand.create(domain, eObject.eContainer(), null, cp.getEValue()));
					// }

					domain.getCommandStack().execute(
						AddCommand.create(domain, eObject, reference, cp.getEValue()));
				}
			});
		}

	}

	/**
	 * @param editingDomain
	 * @param manager
	 * @param selection
	 */
	private void addDeleteActionToContextMenu(final EditingDomain editingDomain, final IMenuManager manager,
		final IStructuredSelection selection) {

		final Action deleteAction = new Action() {
			@Override
			public void run() {
				super.run();
				for (final Object obj : selection.toList())
				{
					editingDomain.getCommandStack().execute(
						RemoveCommand.create(editingDomain, obj));
				}

			}
		};
		final String deleteImagePath = "icons/delete.png";//$NON-NLS-1$
		deleteAction.setImageDescriptor(ImageDescriptor.createFromURL(Activator.getDefault()
			.getBundle()
			.getResource(deleteImagePath)));
		deleteAction.setText("Delete"); //$NON-NLS-1$
		manager.add(deleteAction);
	}

	/**
	 * 
	 * @author Anas Chakfeh
	 *         This class is responsible for handling selection changed events which happen on the tree
	 * 
	 */
	private class TreeMasterViewSelectionListener implements ISelectionChangedListener {

		private final ScrolledComposite parent;
		private Composite childComposite;
		private final VView vView;

		/**
		 * @param rightPanel
		 * @param vView
		 */
		public TreeMasterViewSelectionListener(ScrolledComposite rightPanel, VView vView) {
			parent = rightPanel;
			this.vView = vView;
		}

		@Override
		public void selectionChanged(SelectionChangedEvent event) {

			final Object selected = ((IStructuredSelection) event.getSelection()).getFirstElement();
			if (selected instanceof EObject) {
				try {
					if (childComposite != null) {
						childComposite.dispose();
					}
					childComposite = createComposite(parent);
					parent.setContent(childComposite);

					final Object root = ((RootObject) ((TreeViewer) event.getSource()).getInput()).getRoot();

					if (selected.equals(root)) {
						if (DynamicEObjectImpl.class.isInstance(selected)) {
							final ViewModelContext viewContext = ViewModelContextFactory.INSTANCE
								.createViewModelContext(vView,
									(EObject) selected, new DynamicReferenceService());
							ECPSWTViewRenderer.INSTANCE.render(childComposite, viewContext);

						} else {
							ECPSWTViewRenderer.INSTANCE.render(childComposite, (EObject) selected, vView);
						}

					} else {
						ReferenceService refServ;
						if (DynamicEObjectImpl.class.isInstance(selected)) {
							refServ = new DynamicReferenceService();
						}
						else {
							refServ = new ECPReferenceServiceImpl();
						}
						final VView view = ViewProviderHelper.getView((EObject) selected);
						final ViewModelContext viewContext = ViewModelContextFactory.INSTANCE
							.createViewModelContext(view,
								(EObject) selected, refServ);
						ECPSWTViewRenderer.INSTANCE.render(childComposite, viewContext);
						// ECPSWTViewRenderer.INSTANCE
						// .render(childComposite, (EObject) selected);
					}
					parent.layout();
					childComposite.layout();
					final Point point = childComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
					parent.setMinSize(point);
				} catch (final ECPRendererException e) {
					Activator
						.getDefault()
						.getLog()
						.log(
							new Status(IStatus.ERROR, Activator.getDefault().getBundle().getSymbolicName(), e
								.getMessage(), e));
				}
			}
		}

		private Composite createComposite(Composite parent) {
			final Composite composite = new Composite(parent, SWT.NONE);
			composite.setBackground(parent.getBackground());

			final GridLayout gridLayout = GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).create();
			gridLayout.marginTop = 7;
			gridLayout.marginLeft = 5;
			composite.setLayout(gridLayout);
			GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).indent(10, 10).applyTo(composite);
			composite.layout(true, true);
			return composite;
		}
	}

}
