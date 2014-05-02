/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Alexandra Buzila- initial API and implementation
 ******************************************************************************/

package org.eclipse.emf.ecp.view.model.internal.preview.e3.views;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecp.ide.editor.view.ViewEditorPart;
import org.eclipse.emf.ecp.view.model.internal.preview.Activator;
import org.eclipse.emf.ecp.view.model.preview.common.Preview;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

/** The {@link ViewPart} containing a rendered version a {@link VView}. */
public class PreviewView extends ViewPart implements ISelectionListener {

	private Preview preView;
	private IPartListener2 partListener;
	private ScrolledForm form;
	private EContentAdapter adapter;
	private VView view;
	private boolean updateAutomatic;
	private Composite parent;
	private Action automaticToggleButton, manualRefreshButton;

	/** The constructor. */
	public PreviewView() {
		super();
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService()
		//			.addSelectionListener("org.eclipse.emf.ecp.ui.ModelExplorerView", this); //$NON-NLS-1$
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void dispose() {
		super.dispose();
		getSite().getPage().removePartListener(partListener);
		preView.clear();
		preView.removeAdapter();
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		final FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);

		final GridLayout layout = GridLayoutFactory.fillDefaults().create();
		layout.marginTop = 10;
		layout.marginLeft = 10;
		layout.marginRight = 10;
		form.getBody().setLayout(layout);

		toolkit.decorateFormHeading(form.getForm());
		form.setText("View Model Preview"); //$NON-NLS-1$
		final ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(Activator.getDefault()
			.getBundle()
			.getResource("icons/preview.png")); //$NON-NLS-1$
		form.setImage(new Image(parent.getDisplay(), imageDescriptor.getImageData()));

		addButtonsToFormToolbar(form.getToolBarManager());

		preView = new Preview(form.getBody());

		final IWorkbench wb = PlatformUI.getWorkbench();
		final IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		final IWorkbenchPage page = win.getActivePage();

		for (final IEditorReference reference : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
			.getEditorReferences()) {
			final IEditorPart part = reference.getEditor(false);
			if (page.isPartVisible(part)) {
				if (ViewEditorPart.class.isInstance(part)) {
					final ViewEditorPart viewPart = (ViewEditorPart) part;
					setView(viewPart.getView());
					render(view);
				}
			}
		}

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
		partListener = new IPartListener2() {

			@Override
			public void partActivated(IWorkbenchPartReference partRef) {
				if (ViewEditorPart.class.isInstance(partRef.getPart(true))) {
					final ViewEditorPart part = (ViewEditorPart) partRef.getPart(true);
					setView(part.getView());
					if (updateAutomatic) {
						render(view);
					}
				}
			}

			@Override
			public void partClosed(IWorkbenchPartReference partRef) {
				if (PreviewView.class.isInstance(partRef.getPart(true))) {

					getSite().getPage().removePartListener(this);
				}

				if (ViewEditorPart.class.isInstance(partRef.getPart(true))) {
					if (updateAutomatic) {
						preView.clear();
					}
					preView.removeView();
					view = null;
				}

			}

			@Override
			public void partDeactivated(IWorkbenchPartReference partRef) {
				final IWorkbenchPart part = partRef.getPart(true);
				if (ViewEditorPart.class.isInstance(part) || PreviewView.class.isInstance(part)) {
					removeAdapters();
				}
			}

			@Override
			public void partOpened(IWorkbenchPartReference partRef) {
			}

			@Override
			public void partHidden(IWorkbenchPartReference partRef) {
			}

			@Override
			public void partVisible(IWorkbenchPartReference partRef) {
			}

			@Override
			public void partInputChanged(IWorkbenchPartReference partRef) {
			}

			@Override
			public void partBroughtToTop(IWorkbenchPartReference partRef) {
			}
		};
		getSite().getPage().addPartListener(partListener);

	}

	/**
	 * @param toolBarManager
	 */
	private void addButtonsToFormToolbar(IToolBarManager toolBarManager) {
		// toggle automatic refresh state button

		// automatic refresh
		automaticToggleButton = new Action("", IAction.AS_CHECK_BOX) { //$NON-NLS-1$
			@Override
			public void run() {
				super.run();
				setUpdateAutomatic(isChecked());
				manualRefreshButton.setEnabled(!isChecked());
			}
		};

		final String autoRefreshImagePath = "icons/arrow_rotate_anticlockwise.png";//$NON-NLS-1$
		automaticToggleButton.setImageDescriptor(ImageDescriptor.createFromURL(Activator.getDefault()
			.getBundle()
			.getResource(autoRefreshImagePath)));

		automaticToggleButton.setText("Automatically refresh Preview View"); //$NON-NLS-1$
		automaticToggleButton.setEnabled(true);
		automaticToggleButton.setChecked(false);

		// manual refresh
		manualRefreshButton = new Action() {
			@Override
			public void run() {
				super.run();
				render();
			}
		};
		final String manualRefreshImagePath = "icons/arrow_refresh.png";//$NON-NLS-1$
		manualRefreshButton.setImageDescriptor(ImageDescriptor.createFromURL(Activator.getDefault()
			.getBundle()
			.getResource(manualRefreshImagePath)));

		manualRefreshButton.setText("Refresh Preview View"); //$NON-NLS-1$
		manualRefreshButton.setEnabled(true);

		toolBarManager.add(manualRefreshButton);
		toolBarManager.add(automaticToggleButton);

		toolBarManager.update(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		final IStructuredSelection ss = (IStructuredSelection) selection;
		final Object firstElement = ss.getFirstElement();
		if (!VView.class.isInstance(firstElement)) {
			return;
		}
		setView((VView) firstElement);
		render(view);
	}

	private void render(VView view) {
		if (adapter != null) {
			// remove adapter
			removeAdapters();

		}
		adapter = new EContentAdapter() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.ecore.util.EContentAdapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
			 */
			@Override
			public void notifyChanged(Notification notification) {
				if (form.isDisposed()) {
					return;
				}
				super.notifyChanged(notification);
				if (notification.isTouch()) {
					return;
				}

				// TODO needed?
				if (notification.getFeature() == VViewPackage.eINSTANCE.getElement_Diagnostic()) {
					return;
				}

				form.reflow(true);
			}
		};

		view.eAdapters().add(adapter);

		try {
			preView.render(view);
		} catch (final Exception ex) {
			displayError(ex);
		}
		form.reflow(true);
	}

	private void setView(VView view) {
		if (view != this.view) {
			removeAdapters();
			this.view = view;
		}
	}

	private void removeAdapters() {
		if (view != null && adapter != null) {
			for (final Adapter a : view.eAdapters()) {
				if (a.equals(adapter)) {
					view.eAdapters().remove(adapter);
					adapter = null;
					break;
				}
			}
		}
		preView.removeAdapter();
	}

	/**
	 * @return the updateAutomatic
	 */
	public boolean isUpdateAutomatic() {
		return updateAutomatic;
	}

	/**
	 * @param updateAutomatic the updateAutomatic to set
	 */
	private void setUpdateAutomatic(boolean updateAutomatic) {
		this.updateAutomatic = updateAutomatic;
		if (preView != null) {
			preView.setUpdateAutomatic(updateAutomatic);
			form.reflow(true);
		}
	}

	private void render() {
		if (preView != null) {
			if (view != null) {
				render(view);
				form.reflow(true);
				form.getBody().layout();
				parent.layout();
			} else {
				preView.clear();
			}

		}
	}
}