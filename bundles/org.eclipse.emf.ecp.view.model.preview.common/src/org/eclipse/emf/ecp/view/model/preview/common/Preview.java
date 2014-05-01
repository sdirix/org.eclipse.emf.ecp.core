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
package org.eclipse.emf.ecp.view.model.preview.common;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTView;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.ecp.view.spi.categorization.model.impl.VCategorizationElementImpl;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContext;
import org.eclipse.emf.ecp.view.spi.context.ViewModelContextFactory;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.model.VViewPackage;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.internal.ErrorViewPart;

/** This class renders the contents of a {@link VView} in a {@link Composite}. */
public class Preview {

	private ECPSWTView render;
	private Adapter adapter;
	private VView view;
	private boolean updateAutomatic;

	private final Composite parent;
	private Composite composite;

	/**
	 * The constructor.
	 * 
	 * @param parent - the {@link Composite} in which to render
	 * */
	public Preview(Composite parent) {
		this.parent = parent;
	}

	/**
	 * Render the contents of the {@link VView}.
	 * 
	 * @param view -the {@link VView}
	 * @throws ECPRendererException on rendering fail
	 * */
	public void render(final VView view) {
		this.view = view;
		adapter = new EContentAdapter() {

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.emf.ecore.util.EContentAdapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
			 */
			@Override
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);
				if (!updateAutomatic) {
					return;
				}
				if (notification.isTouch()) {
					return;
				}

				// TODO needed?
				if (notification.getFeature() == VViewPackage.eINSTANCE.getElement_Diagnostic()) {
					return;
				}

				if (VCategorizationElementImpl.class.isInstance(notification.getNotifier())) {
					return;
				}
				internalRender(view);
			}
		};
		view.eAdapters().add(adapter);
		internalRender(view);
	}

	private void internalRender(VView view) {
		try {
			clear();
			final EClass myPreviewEClass = view.getRootEClass();
			final EObject dummyData = EcoreUtil.create(myPreviewEClass);
			final PreviewReferenceService previewRefServ = new PreviewReferenceService();
			final ViewModelContext viewModelContext = ViewModelContextFactory.INSTANCE.createViewModelContext(view,
				dummyData, previewRefServ);
			composite = createComposite(parent);
			render = ECPSWTViewRenderer.INSTANCE.render(composite, viewModelContext);
			composite.layout();
			parent.layout();
		} catch (final RuntimeException e) {
			displayError(e);
		} catch (final ECPRendererException ex) {
			displayError(ex);
		}

	}

	// FIXME move to e3 PreviewView
	private void displayError(Exception e) {
		clear();
		Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		final IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
			"Rendering failed. Please re-check your model.", e); //$NON-NLS-1$
		final ErrorViewPart part = new ErrorViewPart(status);
		part.createPartControl(parent);
	}

	/**
	 * 
	 */
	public void removeAdapter() {
		if (view == null) {
			return;
		}
		for (final Adapter a : view.eAdapters()) {
			if (a == adapter) {
				view.eAdapters().remove(adapter);
				return;
			}
		}
	}

	/**
	 * Removes the previous rendering result from the parent Composite.
	 */
	public void clear() {
		if (render != null) {
			render.dispose();
		}
		if (composite != null) {
			composite.dispose();
			composite = null;
		}
		if (parent.isDisposed()) {
			return;
		}
		for (final Control c : parent.getChildren()) {
			c.dispose();
		}
	}

	/** Removes the cached view. */
	public void removeView() {
		view = null;
	}

	/**
	 * Creates the composite.
	 * 
	 * @param parent the parent
	 * @return the composite
	 */
	private Composite createComposite(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		composite.setBackground(parent.getBackground());
		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(composite);
		return composite;
	}

	/**
	 * @return the canAutomaticallyRender
	 */
	public boolean isUpdateAutomatic() {
		return updateAutomatic;
	}

	/**
	 * @param canAutomaticallyRender the canAutomaticallyRender to set
	 */
	public void setUpdateAutomatic(boolean canAutomaticallyRender) {
		updateAutomatic = canAutomaticallyRender;
		if (updateAutomatic && view != null) {
			internalRender(view);
			parent.layout();
		}
	}
}
