/*******************************************************************************
 * Copyright (c) 2011-2014 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Johannes Faltermeier - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.ecp.view.spi.table.swt;

import java.util.Collections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTView;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.ecp.view.internal.table.swt.Activator;
import org.eclipse.emf.ecp.view.spi.model.VView;
import org.eclipse.emf.ecp.view.spi.provider.ViewProviderHelper;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * Render for a {@link org.eclipse.emf.ecp.view.spi.table.model.VTableControl VTableControl} with a detail editing
 * panel.
 *
 * @author jfaltermeier
 *
 */
public class TableControlDetailPanelRenderer extends TableControlSWTRenderer {

	private ECPSWTView ecpView;
	private VView view;
	private Composite detailPanel;
	private Composite border;
	private ScrolledComposite scrolledComposite;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.spi.table.swt.TableControlSWTRenderer#createControlComposite(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Composite createControlComposite(Composite composite) {
		/* border */
		border = new Composite(composite, SWT.BORDER);
		final GridLayout gridLayout = GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).create();
		border.setLayout(gridLayout);
		final int totalHeight = getTableHeightHint() + getDetailPanelHeightHint() + gridLayout.verticalSpacing;
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).hint(1, totalHeight).applyTo(border);

		/* table composite */
		final Composite tableComposite = new Composite(border, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).hint(1, getTableHeightHint())
		.applyTo(tableComposite);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(border);

		/* scrolled composite */
		scrolledComposite = new ScrolledComposite(border, SWT.V_SCROLL);
		scrolledComposite.setBackground(composite.getBackground());
		scrolledComposite.setLayout(GridLayoutFactory.fillDefaults().create());
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(scrolledComposite);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setExpandHorizontal(true);

		/* detail panel */
		detailPanel = createDetailPanel(scrolledComposite);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(detailPanel);
		scrolledComposite.setContent(detailPanel);

		detailPanel.layout();
		final Point point = detailPanel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scrolledComposite.setMinHeight(point.y);

		return tableComposite;
	}

	/**
	 * Returns the prefereed height for the detail panel. This will be passed to the layoutdata.
	 *
	 * @return the height in px
	 */
	protected int getDetailPanelHeightHint() {
		return 400;
	}

	/**
	 * Creates the detail panel.
	 *
	 * @param composite the parent
	 * @return the detail panel
	 */
	protected Composite createDetailPanel(ScrolledComposite composite) {
		final Composite detail = new Composite(scrolledComposite, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).applyTo(detail);
		return detail;
	}

	/**
	 * Returns a fresh copy of the {@link VView} used for detail editing.
	 *
	 * @return the view
	 */
	protected VView getView() {
		if (view == null) {
			VView detailView = getVElement().getDetailView();
			if (detailView == null) {
				final Setting setting = getVElement().getDomainModelReference().getIterator().next();
				final EReference reference = (EReference) setting.getEStructuralFeature();
				detailView = ViewProviderHelper.getView(EcoreUtil.create(reference.getEReferenceType()),
					Collections.<String, Object> emptyMap());
			}
			view = detailView;
		}
		return EcoreUtil.copy(view);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecp.view.spi.table.swt.TableControlSWTRenderer#viewerSelectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	protected void viewerSelectionChanged(SelectionChangedEvent event) {
		if (event.getSelection().isEmpty()) {
			handleEmptySelection();
		} else if (IStructuredSelection.class.cast(event.getSelection()).size() != 1) {
			handleMultiSelection((IStructuredSelection) event.getSelection());
		} else {
			handleSingleSelection((IStructuredSelection) event.getSelection());
		}
		super.viewerSelectionChanged(event);
	}

	/**
	 * Handle a single selection.
	 *
	 * @param selection the selection
	 */
	protected void handleSingleSelection(IStructuredSelection selection) {
		try {
			disposeDetail();
			final EObject object = (EObject) selection.getFirstElement();
			final VView detailView = getView();

			if (detailView == null) {
				if (isDebug()) {
					final Label label = new Label(detailPanel, SWT.NONE);
					label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
					label.setText("No Detail View found."); //$NON-NLS-1$
				}
			} else {
				ecpView = ECPSWTViewRenderer.INSTANCE.render(detailPanel,
					object, detailView);
			}
			border.layout(true, true);
			final Point point = detailPanel.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			scrolledComposite.setMinHeight(point.y);
		} catch (final ECPRendererException ex) {
			Activator.log(ex);
		}
	}

	/**
	 * Handle multi selection.
	 *
	 * @param selection the selection
	 */
	protected void handleMultiSelection(IStructuredSelection selection) {
		disposeDetail();
	}

	/**
	 * Handle empty selection.
	 */
	protected void handleEmptySelection() {
		disposeDetail();
	}

	private void disposeDetail() {
		if (ecpView != null) {
			ecpView.getSWTControl().dispose();
			ecpView = null;
		}
		for (final Control control : detailPanel.getChildren()) {
			control.dispose();
		}
	}

}