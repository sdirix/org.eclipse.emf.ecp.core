/**
 * Copyright (c) 2011-2013 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * EclipseSource Munich - initial API and implementation
 */
package org.eclipse.emf.ecp.view.template.controls.swt;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecp.edit.internal.swt.controls.AbstractTextControl;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedException;
import org.eclipse.emfforms.spi.core.services.databinding.DatabindingFailedReport;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.osgi.framework.Bundle;

/**
 * Control for setting the url of an icon.
 *
 * @author Eugen Neufeld
 *
 */
@SuppressWarnings("restriction")
public class TemplateIconControl extends AbstractTextControl {

	@Override
	protected void fillControlComposite(final Composite composite) {
		final Composite main = new Composite(composite, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(3).applyTo(main);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(main);
		super.fillControlComposite(main);

		final Button selectExternalFile = new Button(main, SWT.PUSH);
		selectExternalFile.setText(Messages.TemplateIconControl_ExternalFile);

		selectExternalFile.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				final FileDialog fd = new FileDialog(composite.getShell(), SWT.OPEN);
				final String filePath = fd.open();
				if (filePath == null) {
					return;
				}
				try {
					final String url = new File(filePath).toURL().toExternalForm();
					final IObservableValue observableValue = Activator
						.getDefault()
						.getEMFFormsDatabinding()
						.getObservableValue(getControl().getDomainModelReference(),
							getViewModelContext().getDomainModel());
					final EStructuralFeature structuralFeature = (EStructuralFeature) observableValue.getValueType();
					final EObject eObject = (EObject) ((IObserving) observableValue).getObserved();
					eObject.eSet(structuralFeature, url);
				} catch (final MalformedURLException ex) {

				} catch (final DatabindingFailedException ex) {
					Activator.getDefault().getReportService().report(new DatabindingFailedReport(ex));
					return; // TODO: is this correct?
				}
			}

		});
		final Button selectFileFromPlugin = new Button(main, SWT.PUSH);
		selectFileFromPlugin.setText(Messages.TemplateIconControl_PluginFile);

		selectFileFromPlugin.addSelectionListener(new SelectFileFromPluginAdapter(composite));
	}

	@Override
	protected String getTextVariantID() {
		return "org_eclipse_emf_ecp_view_template_control_icon"; //$NON-NLS-1$
	}

	@Override
	protected String getUnsetLabelText() {
		return Messages.TemplateIconControl_NoIconSet;
	}

	@Override
	protected String getUnsetButtonTooltip() {
		return Messages.TemplateIconControl_UnsetIcon;
	}

	/**
	 * @author Jonas
	 *
	 */
	private final class SelectFileFromPluginAdapter extends SelectionAdapter {
		private final Composite composite;

		/**
		 * @param composite
		 */
		private SelectFileFromPluginAdapter(Composite composite) {
			this.composite = composite;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			final IconTreeContentProvider iconTreeContentProvider = new IconTreeContentProvider();
			final ElementTreeSelectionDialog etsd = new ElementTreeSelectionDialog(composite.getShell(),
				new LabelProvider() {

					/**
					 * {@inheritDoc}
					 *
					 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
					 */
					@Override
					public String getText(Object element) {
						if (Bundle.class.isInstance(element)) {
							return ((Bundle) element).getSymbolicName();
						}
						return super.getText(element);
					}

				}, iconTreeContentProvider);

			etsd.setInput(Activator.getContext().getBundles());
			final int result = etsd.open();
			if (Window.OK != result) {
				return;
			}
			final Object selectedResult = etsd.getFirstResult();
			if (!String.class.isInstance(selectedResult)) {
				return;
			}
			final String file = (String) selectedResult;
			if (file.endsWith("png") || file.endsWith("gif") || file.endsWith("jpg")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				getDomainModelReference()
					.getIterator()
					.next()
					.set(
						"platform:/plugin/" + iconTreeContentProvider.getLastBundle().getSymbolicName() + "/" //$NON-NLS-1$ //$NON-NLS-2$
							+ file);
			}
		}
	}

	/**
	 * The tree content provider for selecting the icon url.
	 *
	 * @author Eugen Neufeld
	 *
	 */
	private class IconTreeContentProvider implements ITreeContentProvider {

		private Bundle lastBundle;

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
			if (element.getClass().isArray()) {
				final Object[] array = (Object[]) element;
				return array.length > 0;
			}
			if (Bundle.class.isInstance(element)) {
				final Bundle bundle = (Bundle) element;
				return bundle.getEntryPaths("/").hasMoreElements(); //$NON-NLS-1$
			}
			if (String.class.isInstance(element) && lastBundle != null) {
				if (!((String) element).endsWith("/")) { //$NON-NLS-1$
					return false;
				}
				return lastBundle.getEntryPaths((String) element).hasMoreElements();
			}
			return false;
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
			if (parentElement.getClass().isArray()) {
				return (Object[]) parentElement;
			}
			if (Bundle.class.isInstance(parentElement)) {
				lastBundle = (Bundle) parentElement;
				final Set<String> result = new LinkedHashSet<String>();
				final Enumeration<String> resources = lastBundle.getEntryPaths("/"); //$NON-NLS-1$
				while (resources.hasMoreElements()) {
					result.add(resources.nextElement());
				}

				return result.toArray();
			}
			if (String.class.isInstance(parentElement) && lastBundle != null) {
				final Set<String> result = new LinkedHashSet<String>();
				final Enumeration<String> resources = lastBundle.getEntryPaths((String) parentElement);
				while (resources.hasMoreElements()) {
					result.add(resources.nextElement());
				}

				return result.toArray();
			}
			return null;
		}

		/**
		 * @return the lastBundle
		 */
		public Bundle getLastBundle() {
			return lastBundle;
		}
	}
}
